/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.auth;

import java.net.URI;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class OAuth2AuthorizationContextHolder implements AuthorizationContextHolder {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationContextHolder.class);

    private static final String CONTEXT_HOLDER_ATTRIBUTE = "CONTEXT_HOLDER_ATTRIBUTE";
    private static final String OAUTH_TOKEN_URI_PATH = "oauth/token"; // TODO to config properties?
    private static final String OAUTH_REVOKE_TOKEN_URI_PATH = "/oauth/revoke-token"; // TODO to config properties?
    private static final String CURRENT_USER_URI_PATH = "/user/me"; // TODO to config properties?

    private final String guiClientId;
    private final String guiClientSecret;
    private final RestCallBuilder restCallBuilder;

    @Autowired
    public OAuth2AuthorizationContextHolder(
            final RestCallBuilder restCallBuilder,
            @Value("${sebserver.oauth.clients.guiClient.id}") final String guiClientId,
            @Value("${sebserver.oauth.clients.guiClient.secret}") final String guiClientSecret) {

        this.restCallBuilder = restCallBuilder;
        this.guiClientId = guiClientId;
        this.guiClientSecret = guiClientSecret;

    }

    @Override
    public SEBServerAuthorizationContext getAuthorizationContext(final HttpSession session) {
        log.debug("Trying to get OAuth2AuthorizationContext from HttpSession: {}", session.getId());

        OAuth2AuthorizationContext context =
                (OAuth2AuthorizationContext) session.getAttribute(CONTEXT_HOLDER_ATTRIBUTE);

        if (context == null || !context.valid) {
            log.debug(
                    "OAuth2AuthorizationContext for HttpSession: {} is not present or is invalid. "
                            + "Create new OAuth2AuthorizationContext for this session",
                    session.getId());

            context = new OAuth2AuthorizationContext(
                    this.guiClientId,
                    this.guiClientSecret,
                    this.restCallBuilder);
            session.setAttribute(CONTEXT_HOLDER_ATTRIBUTE, context);
        }

        return context;
    }

    private static final class DisposableOAuth2RestTemplate extends OAuth2RestTemplate {

        private boolean enabled = true;

        public DisposableOAuth2RestTemplate(final OAuth2ProtectedResourceDetails resource) {
            super(resource, new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
        }

        @Override
        protected <T> T doExecute(
                final URI url,
                final HttpMethod method,
                final RequestCallback requestCallback,
                final ResponseExtractor<T> responseExtractor) throws RestClientException {

            if (this.enabled) {
                return super.doExecute(url, method, requestCallback, responseExtractor);
            } else {
                throw new IllegalStateException(
                        "Error: Forbidden execution call on disabled DisposableOAuth2RestTemplate");
            }
        }
    }

    private static final class OAuth2AuthorizationContext implements SEBServerAuthorizationContext {

        private boolean valid = true;

        private final ResourceOwnerPasswordResourceDetails resource;
        private final DisposableOAuth2RestTemplate restTemplate;
        private final String revokeTokenURI;
        private final String currentUserURI;

        private UserInfo loggedInUser = null;

        OAuth2AuthorizationContext(
                final String guiClientId,
                final String guiClientSecret,
                final RestCallBuilder restCallBuilder) {

            this.resource = new ResourceOwnerPasswordResourceDetails();
            this.resource.setAccessTokenUri(restCallBuilder.withPath(OAUTH_TOKEN_URI_PATH));
            this.resource.setClientId(guiClientId);
            this.resource.setClientSecret(guiClientSecret);
            this.resource.setGrantType("password");
            final ArrayList<String> scopes = new ArrayList<>();
            scopes.add("read");
            scopes.add("write");
            this.resource.setScope(scopes);

            this.restTemplate = new DisposableOAuth2RestTemplate(this.resource);

            this.revokeTokenURI = restCallBuilder.withPath(OAUTH_REVOKE_TOKEN_URI_PATH);
            this.currentUserURI = restCallBuilder.withPath(CURRENT_USER_URI_PATH);
        }

        @Override
        public boolean isValid() {
            return this.valid;
        }

        @Override
        public boolean isLoggedIn() {
            final OAuth2AccessToken accessToken = this.restTemplate.getOAuth2ClientContext().getAccessToken();
            return accessToken != null && !StringUtils.isEmpty(accessToken.toString());
        }

        @Override
        public boolean login(final String username, final String password) {
            if (!this.valid || this.isLoggedIn()) {
                return false;
            }

            this.resource.setUsername(username);
            this.resource.setPassword(password);

            log.debug("Trying to login for user: {}", username);

            try {
                final OAuth2AccessToken accessToken = this.restTemplate.getAccessToken();
                log.debug("Got token for user: {} : {}", username, accessToken);
                this.loggedInUser = getLoggedInUser();
                return true;
            } catch (final OAuth2AccessDeniedException | AccessDeniedException e) {
                log.info("Access Denied for user: {}", username);
                return false;
            }
        }

        @Override
        public boolean logout() {
            // set this context invalid to force creation of a new context on next request
            this.valid = false;
            this.loggedInUser = null;
            // delete the access-token (and refresh-token) on authentication server side
            this.restTemplate.delete(this.revokeTokenURI);
            // delete the access-token within the RestTemplate
            this.restTemplate.getOAuth2ClientContext().setAccessToken(null);
            // mark the RestTemplate as disposed
            this.restTemplate.enabled = false;

            return true;
        }

        @Override
        public RestTemplate getRestTemplate() {
            return this.restTemplate;
        }

        @Override
        public UserInfo getLoggedInUser() {
            if (this.loggedInUser != null) {
                return this.loggedInUser;
            }

            log.debug("Request logged in User from SEBserver web-service API");

            try {
                if (isValid() && isLoggedIn()) {
                    final ResponseEntity<UserInfo> response =
                            this.restTemplate.getForEntity(this.currentUserURI, UserInfo.class);
                    this.loggedInUser = response.getBody();
                    return this.loggedInUser;
                } else {
                    throw new IllegalStateException("Logged in User requested on invalid or not logged in ");
                }
            } catch (final AccessDeniedException | OAuth2AccessDeniedException ade) {
                log.error("Acccess denied while trying to request logged in User from API", ade);
                throw ade;
            } catch (final Exception e) {
                log.error("Unexpected error while trying to request logged in User from API", e);
                throw new RuntimeException("Unexpected error while trying to request logged in User from API", e);
            }
        }

        @Override
        public boolean hasRole(final String role) {
            if (!isValid() || !isLoggedIn()) {
                return false;
            }

            return getLoggedInUser().hasRole(role);
        }
    }
}
