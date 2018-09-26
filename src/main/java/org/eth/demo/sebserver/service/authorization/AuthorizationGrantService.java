/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.admin.UserFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class AuthorizationGrantService {

    private final Map<String, AuthorizationGrantRule<?>> rules = new HashMap<>();

    public AuthorizationGrantService(final Collection<AuthorizationGrantRule<?>> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("No AuthorizationGrantRule found");
        }
        rules.stream()
                .forEach(r -> this.rules.put(r.type().getName(), r));
    }

    public <T> boolean hasReadGrant(final T value, final Principal principal) {
        return hasReadGrant(value, UserFacade.extractFromPrincipal(principal));
    }

    public <T> boolean hasWriteGrant(final T value, final Principal principal) {
        return hasWriteGrant(value, UserFacade.extractFromPrincipal(principal));
    }

    public <T> boolean hasReadGrant(final T value, final User user) {
        @SuppressWarnings("unchecked")
        final AuthorizationGrantRule<T> authorizationGrantRule =
                (AuthorizationGrantRule<T>) this.rules.get(value.getClass().getName());

        if (authorizationGrantRule == null) {
            return false;
        }

        return authorizationGrantRule.hasReadGrant(value, user);
    }

    public <T> boolean hasWriteGrant(final T value, final User user) {
        @SuppressWarnings("unchecked")
        final AuthorizationGrantRule<T> authorizationGrantRule =
                (AuthorizationGrantRule<T>) this.rules.get(value.getClass().getName());

        if (authorizationGrantRule == null) {
            return false;
        }

        return authorizationGrantRule.hasWriteGrant(value, user);
    }

    public <T> Predicate<T> getReadGrantFilter(final Class<T> type, final Principal principal) {
        return getReadGrantFilter(type, UserFacade.extractFromPrincipal(principal));
    }

    public <T> Predicate<T> getWriteGrantFilter(final Class<T> type, final Principal principal) {
        return getWriteGrantFilter(type, UserFacade.extractFromPrincipal(principal));
    }

    public <T> Predicate<T> getReadGrantFilter(final Class<T> type, final User user) {
        @SuppressWarnings("unchecked")
        final AuthorizationGrantRule<T> authorizationGrantRule =
                (AuthorizationGrantRule<T>) this.rules.get(type.getName());

        if (authorizationGrantRule == null) {
            return t -> false;
        }

        return t -> authorizationGrantRule.hasReadGrant(t, user);
    }

    public <T> Predicate<T> getWriteGrantFilter(final Class<T> type, final User user) {
        @SuppressWarnings("unchecked")
        final AuthorizationGrantRule<T> authorizationGrantRule =
                (AuthorizationGrantRule<T>) this.rules.get(type.getName());

        if (authorizationGrantRule == null) {
            return t -> false;
        }

        return t -> authorizationGrantRule.hasWriteGrant(t, user);
    }

}
