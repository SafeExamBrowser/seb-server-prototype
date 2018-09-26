/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/** This adapts to external authentication methods that may be used in the future to provide LDAP or Shibboleth (SAML
 * 2.0) authentication regarding to an institution configuration.
 *
 * TODO: Different external authentication-strategies like LDAP or SAML 2.0 can be implemented here within a fall-back
 * approach.
 *
 * @author anhefti */
@Component
public class ExternalAuthProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(ExternalAuthProvider.class);

    private final Collection<AuthenticationProvider> externalAuthProvider = new ArrayList<>();

    public ExternalAuthProvider() {
        createInMemoryMockExample();
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        log.debug("Processing external authentication strategies...");

        for (final AuthenticationProvider authProvider : this.externalAuthProvider) {

            log.debug("Check external authentication: {}" + authProvider.getClass());

            if (!authProvider.supports(authentication.getClass())) {
                continue;
            }

            log.debug("Process external authentication: {}" + authProvider.getClass());

            try {
                final Authentication authenticate = authProvider.authenticate(authentication);

                if (authenticate != null) {
                    log.info("Successfully authenticated by external authentication: {}" + authProvider.getClass());
                    return authenticate;
                }
            } catch (final AuthenticationException e) {
                log.debug("External authentication: {} failed with: ", authProvider.getClass(), e.getMessage());
            }
        }

        log.debug("Finished processing of external authentication strategies with no match");
        return null;
    }

    /** NOTE: this creates an authentication provider with an in-memory user source (extUser) as an example and prove of
     * concept. */
    private final User mockUser = new User(
            null, 1L,
            "extUser", "extUser", "extUser",
            "", DateTime.now(), true,
            Arrays.asList(Role.UserRole.EXAM_ADMIN.role));

    private void createInMemoryMockExample() {
        this.externalAuthProvider.add(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
                final Object principal = authentication.getPrincipal();
                final Object credentials = authentication.getCredentials();

                if ("extUser".equals(principal) && "extUser".equals(credentials)) {
                    return new UsernamePasswordAuthenticationToken(
                            ExternalAuthProvider.this.mockUser,
                            ExternalAuthProvider.this.mockUser,
                            ExternalAuthProvider.this.mockUser.getAuthorities());
                }

                return null;
            }

            @Override
            public boolean supports(final Class<?> authentication) {
                return authentication == UsernamePasswordAuthenticationToken.class;
            }

        });
    }

}
