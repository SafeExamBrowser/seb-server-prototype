/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

public final class ClientConnectionAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionAuthenticationFilter.class);

    public static final String SPRING_NAME = "ClientConnectionAuthenticationFilter";
    public static final String HEADER_ATTR_LMS_AUTH = "LMS_AUTH_URL";
    public static final String HEADER_ATTR_CLIENT_TOKEN = "LMS_CLIENT_TOKEN";

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

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("Processing ClientConnectionAuthenticationFilter");

        final String lmsAuthUrl = httpRequest.getHeader(HEADER_ATTR_LMS_AUTH);
        final String clientToken = httpRequest.getHeader(HEADER_ATTR_CLIENT_TOKEN);
        final String authCredentials = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        assert lmsAuthUrl != null && !lmsAuthUrl.isEmpty() : "expecting lmsAuthUrl within http-header: "
                + HEADER_ATTR_LMS_AUTH;

        if (clientToken != null) {
            // Case 1: SEB_CLIENT_TOKEN sent
            // There is a client token already within the request. Let's authorize the client within the LMS and given token

            log.debug("Case 1: SEB_CLIENT_TOKEN sent");

            try {
                final HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("SEB_CLIENT_TOKEN", clientToken);
                httpHeaders.set(HttpHeaders.AUTHORIZATION, "TODO: seb-servers ceredentials to access LMS API");

                final ResponseEntity<String> authorizationResponse = this.restTemplate.exchange(
                        UriComponentsBuilder.fromHttpUrl(lmsAuthUrl).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<String>(httpHeaders),
                        String.class);

                final User clientUser = processAuthResponse(authorizationResponse);
                processSuccess(clientUser);

            } catch (final Exception e) {
                log.error("Unexpected error while trying to authorize SEB-Client User");
            }
        } else if (authCredentials != null) {
            // Case 2: No SEB_CLIENT_TOKEN sent but client-user credentials send
            // There are some Credentials within the client request, lets request access with them on the given LMS url

            log.debug("Case 2: No SEB_CLIENT_TOKEN sent but client-user credentials send");

            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "TODO: clients credentials");

            final ResponseEntity<String> loginResponse = this.restTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(lmsAuthUrl).toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<String>(httpHeaders),
                    String.class);

            final User clientUser = processLoginResponse(loginResponse, httpResponse);
            processSuccess(clientUser);

            // Case 3: Only a LMS URL was sent // TODO redirect to LMS login!?
        } else {
            // No Token nor Credentials found. Access Denied
            this.defaultAuthenticationEventPublisher
                    .publishAuthenticationFailure(new BadCredentialsException("ffbfbdfb"), null);
        }

        chain.doFilter(request, response);
    }

    private void processSuccess(final User clientUser) {
        final AbstractAuthenticationToken authentication =
                new AbstractAuthenticationToken(clientUser.getAuthorities()) {

                    private static final long serialVersionUID = -4003042365452567686L;

                    @Override
                    public Object getCredentials() {
                        return clientUser;
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return true;
                    }

                    @Override
                    public Object getPrincipal() {
                        return clientUser;
                    }
                };

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    private User processAuthResponse(final ResponseEntity<String> response) {
        // TODO parse response body String to User object

        // TODO insert client-user connection in client_connection table with status AUTHENTICATED

        return null;
    }

    private User processLoginResponse(
            final ResponseEntity<String> response,
            final HttpServletResponse httpResponse) {

        // TODO parse response body String to User object

        // TODO extract client-connection-token form LMS response header
        //      and add it to HttpServletResponse header to send it to the client along with the list of running exams

        // TODO insert client-user connection in client_connection table with status AUTHENTICATED

        return null;
    }
}