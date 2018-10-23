/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** This adapts to external authentication methods that may be used in the future to provide LDAP or Shibboleth (SAML
 * 2.0) authentication regarding to an institution configuration.
 *
 * TODO: Different external authentication-strategies like LDAP or SAML 2.0 can be implemented here within a fall-back
 * approach.
 *
 * @author anhefti */
@Component
@Configuration
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
            UUID.randomUUID().toString(),
            "extUser",
            "extUser",
            null, DateTime.now(), Locale.ENGLISH,
            Role.EXAM_ADMIN);

    private void createInMemoryMockExample() {
        this.externalAuthProvider.add(new AbstractUserDetailsAuthenticationProvider() {

            @Override
            public boolean supports(final Class<?> authentication) {
                return authentication == UsernamePasswordAuthenticationToken.class;
            }

            @Override
            protected void additionalAuthenticationChecks(
                    final UserDetails userDetails,
                    final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                // TODO Auto-generated method stub
            }

            @Override
            protected UserDetails retrieveUser(
                    final String username,
                    final UsernamePasswordAuthenticationToken authentication)
                    throws AuthenticationException {

                // NOTE: like so we can implement external authentication and authorization. For example within an LDAP
                // TODO: get institutionId from authentication if possible,
                //       within this institution or over all institution for a specified method (LDAP /AAI)...
                //       check if this institution supports the specified external authentication method
                //       and if yes, try to authenticate this user within the given credentials on the server
                //       configured within the institutional attributes for authentication server binding.

                if ("extUser".equals(username)) {
                    return ExternalAuthProvider.this.mockUser;
                }

                throw new UsernameNotFoundException(username);
            }

        });
    }

}
