/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.exam.SEBClientAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Deprecated // NOTE: as we decided to skip SEB-Server proxy authentication there must be
            // two authentication filter, one for SEBClient connection attempt and one for LMS confirmation
public final class ClientConnectionAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionAuthenticationFilter.class);

    public static final String SPRING_NAME = "ClientConnectionAuthenticationFilter";
    public static final String HEADER_ATTR_LMS_AUTH = "LMS_AUTH_URL";
    public static final String HEADER_ATTR_CLIENT_TOKEN = "SEB_CLIENT_TOKEN";
    private static final String HEADER_ATTR_SEB_CLIENT_AUTHORIZATION = "SEB_CLIENT_AUTHORIZATION";

    private final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher;
    private final RestTemplate restTemplate;

    public ClientConnectionAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher) {

        this.defaultAuthenticationEventPublisher = defaultAuthenticationEventPublisher;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        System.out.println("****************************** ClientConnectionAuthenticationFilter");

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        //final HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("Processing ClientConnectionAuthenticationFilter");

        final String lmsAuthUrl = httpRequest.getHeader(HEADER_ATTR_LMS_AUTH);
        final String clientToken = httpRequest.getHeader(HEADER_ATTR_CLIENT_TOKEN);
        final String authCredentials = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // TODO: For now WebSocket connection attempt is just railroaded.
        //       Later the WebSocket connection must also have an access token within and a check with LMS must be done here
        if (httpRequest.getRequestURI().startsWith("/ws")) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new SEBClientAuthentication(new SEBClientAuth(
                            "[NO_TOKEN_YET]",
                            httpRequest.getRemoteAddr())));
        } else if (clientToken != null) {
            // Case 1: SEB_CLIENT_TOKEN sent
            // There is a client token already within the request. Let's authorize the client within the LMS and given token

            log.debug("Case 1: SEB_CLIENT_TOKEN sent");

            processCase1(clientToken, lmsAuthUrl, request.getRemoteAddr());

        } else if (authCredentials != null) {
            // Case 2: No SEB_CLIENT_TOKEN sent but client-user credentials send
            // There are some Credentials within the client request, lets request access with them on the given LMS url

            log.debug("Case 2: No SEB_CLIENT_TOKEN sent but client-user credentials send");

            processCase2(authCredentials, lmsAuthUrl, request.getRemoteAddr());

            // Case 3: Only a LMS URL was sent
            // TODO redirect to a login page and proxying the client login to the LMS.
            //      after successful login, create and store the client token here, send within the response header back to client
        } else {
            // No Token nor Credentials or lmsURL found on request headers. Access Denied
            this.defaultAuthenticationEventPublisher
                    .publishAuthenticationFailure(new BadCredentialsException("ffbfbdfb"), null);
        }

        chain.doFilter(request, response);
    }

    private String createSEBServerAuthorizationHeader() {
        return "Basic c2Vic2VydmVyOnNlYnNlcnZlcg==";
    }

    private void processCase1(
            final String clientToken,
            final String lmsAuthUrl,
            final String clientAddress) {

        final SEBClientAuth clientUser = requestAuthenticationOnLMS(
                clientToken,
                lmsAuthUrl,
                clientAddress);

        SecurityContextHolder
                .getContext()
                .setAuthentication(new SEBClientAuthentication(clientUser));

    }

    private String processCase2(
            final String clientCredentials,
            final String lmsAuthUrl,
            final String clientAddress) {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, createSEBServerAuthorizationHeader());
        httpHeaders.set(HEADER_ATTR_SEB_CLIENT_AUTHORIZATION, clientCredentials);

        final ResponseEntity<String> loginResponse = this.restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(lmsAuthUrl).toUriString(),
                HttpMethod.GET,
                new HttpEntity<String>(httpHeaders),
                String.class);

        final String clientToken = loginResponse
                .getHeaders()
                .get(HEADER_ATTR_CLIENT_TOKEN).get(0);

        final SEBClientAuth clientUser = requestAuthenticationOnLMS(
                clientToken,
                lmsAuthUrl,
                clientAddress);

        SecurityContextHolder
                .getContext()
                .setAuthentication(new SEBClientAuthentication(clientUser));

        return clientToken;
    }

    private SEBClientAuth requestAuthenticationOnLMS(
            final String clientToken,
            final String lmsAuthUrl,
            final String clientAddress) {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HEADER_ATTR_CLIENT_TOKEN, clientToken);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, createSEBServerAuthorizationHeader());

        final ResponseEntity<String> authorizationResponse = this.restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(lmsAuthUrl).toUriString(),
                HttpMethod.GET,
                new HttpEntity<String>(httpHeaders),
                String.class);

        final SEBClientAuth clientUser = extractClientUserFromResponse(
                authorizationResponse.getBody(),
                clientToken,
                clientAddress);

        return clientUser;
    }

    private SEBClientAuth extractClientUserFromResponse(
            final String bodyContent,
            final String token,
            final String clientAddress) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final JsonNode node = mapper.readTree(bodyContent);

            return new SEBClientAuth(
                    token,
                    clientAddress);

        } catch (final Exception e) {
            log.error("Unexpected error while trying to process authentication response from LMS login", e);
            e.printStackTrace();
            return null;
        }
    }

    private class SEBClientAuthentication implements Authentication {

        private static final long serialVersionUID = 4832650248834396365L;

        private final SEBClientAuth user;
        private final Collection<GrantedAuthority> authorities;

        public SEBClientAuthentication(final SEBClientAuth user) {
            this.user = user;
            final List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(Role.UserRole.SEB_CLIENT.role);
            this.authorities = Collections.unmodifiableList(roles);
        }

        @Override
        public String getName() {
            //return this.user.username;
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.authorities;
        }

        @Override
        public Object getCredentials() {
            return this.user;
        }

        @Override
        public Object getDetails() {
            return this.user;
        }

        @Override
        public Object getPrincipal() {
            return this.user;
        }

        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
    }
}