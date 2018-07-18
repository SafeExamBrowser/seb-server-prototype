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

import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class OAuth2AuthorizationContextHolder implements AuthorizationContextHolder {

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
        OAuth2AuthorizationContext context =
                (OAuth2AuthorizationContext) session.getAttribute(CONTEXT_HOLDER_ATTRIBUTE);

        if (context == null || !context.valid) {
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
        private boolean loggedIn = false;

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
        public boolean valid() {
            return this.valid;
        }

        @Override
        public boolean loggedIn() {
            return this.loggedIn;
        }

        @Override
        public boolean login(final String username, final String password) {
            if (!this.valid || this.loggedIn) {
                return false;
            }

            this.resource.setUsername(username);
            this.resource.setPassword(password);

            try {
                this.restTemplate.getAccessToken();
            } catch (final Exception e) {
                // TODO
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        public boolean logout() {
            this.valid = false;
            this.loggedIn = false;
            // TODO: http://www.baeldung.com/logout-spring-security-oauth
            this.restTemplate.enabled = false;
            return true;
        }

        @Override
        public RestTemplate getRestTemplate() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
