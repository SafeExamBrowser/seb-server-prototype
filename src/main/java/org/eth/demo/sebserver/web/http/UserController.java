/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.security.Principal;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.admin.UserDao;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final AuthorizationGrantService authorizationGrantService;

    public UserController(final UserDao userDao, final AuthorizationGrantService authorizationGrantService) {
        this.userDao = userDao;
        this.authorizationGrantService = authorizationGrantService;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User loggedInUser(final Authentication auth) {
        final Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }

        return this.userDao.byUserName(auth.getName());
    }

    @RequestMapping(value = "/hasTypeGrant/{entityType}/{grantType}", method = RequestMethod.GET)
    public Boolean userGrant(
            @PathVariable(required = true) final GrantEntityType entityType,
            @PathVariable(required = true) final GrantType grantType,
            final Principal principal) {

        return this.authorizationGrantService.hasTypeGrant(entityType, grantType, principal);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public User user(@PathVariable final Long userId) {
        return this.userDao.byId(userId);
    }

}
