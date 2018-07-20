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
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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
                    this.restCallBuilder.withPath("oauth/token"));
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
        //private boolean loggedIn = false;

        private final ResourceOwnerPasswordResourceDetails resource;
        private final DisposableOAuth2RestTemplate restTemplate;

        OAuth2AuthorizationContext(
                final String guiClientId,
                final String guiClientSecret,
                final String uri) {

            this.resource = new ResourceOwnerPasswordResourceDetails();
            this.resource.setAccessTokenUri(uri);
            this.resource.setClientId(guiClientId);
            this.resource.setClientSecret(guiClientSecret);
            this.resource.setGrantType("password");
            final ArrayList<String> scopes = new ArrayList<>();
            scopes.add("read");
            scopes.add("write");
            this.resource.setScope(scopes);

            this.restTemplate = new DisposableOAuth2RestTemplate(this.resource);
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
                return true;
            } catch (final Exception e) {
                // TODO
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean logout() {
            this.valid = false;

            // TODO: http://www.baeldung.com/logout-spring-security-oauth
            this.restTemplate.getOAuth2ClientContext().setAccessToken(null);
            this.restTemplate.enabled = false;
            return true;
        }

        @Override
        public RestTemplate getRestTemplate() {
            return this.restTemplate;
        }
    }
}
