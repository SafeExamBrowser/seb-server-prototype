/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.admin;

import java.security.Principal;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserFacadeImpl implements UserFacade {

    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No Authentication found within Springs SecurityContextHolder");
        }

        return fromPrincipal(authentication);
    }

    /** Use this to extract the internal User from a given Principal.
     *
     * This is supposed to apply some known strategies to extract the internal user from Principal. If there is no
     * internal user found on the given Principal, a IllegalArgumentException is thrown.
     *
     * If there is certainly a internal user within the given Principal but no strategy that finds it, this method can
     * be extended with the needed strategy.
     *
     * @param principal
     * @return internal User instance if it was found within the Principal and the existing strategies
     * @throws IllegalArgumentException if no internal User can be found */
    public static final User fromPrincipal(final Principal principal) {
        // 1. OAuth2Authentication strategy
        if (principal instanceof OAuth2Authentication) {
            final Authentication userAuthentication = ((OAuth2Authentication) principal).getUserAuthentication();
            if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
                final Object userPrincipal = ((UsernamePasswordAuthenticationToken) userAuthentication).getPrincipal();
                if (userPrincipal instanceof User) {
                    return (User) userPrincipal;
                }
            }
        }

        // add more strategies here if necessary

        throw new IllegalArgumentException("Unable to extract internal user from Principal: " + principal);
    }

}
