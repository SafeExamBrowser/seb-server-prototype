/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.auth;

import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {

    private final AuthorizationContextHolder authorizationContextHolder;

    private UserInfo user = null;

    public CurrentUser(final AuthorizationContextHolder authorizationContextHolder) {
        this.authorizationContextHolder = authorizationContextHolder;
    }

    public UserInfo get() {
        if (this.user == null) {
            this.user = this.authorizationContextHolder
                    .getAuthorizationContext()
                    .getLoggedInUser();
        }

        return this.user;
    }
}
