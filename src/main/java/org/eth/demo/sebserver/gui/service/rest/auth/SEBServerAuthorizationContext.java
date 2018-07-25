/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.auth;

import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.eth.demo.sebserver.gui.domain.admin.UserRole;
import org.springframework.web.client.RestTemplate;

public interface SEBServerAuthorizationContext {

    boolean isValid();

    boolean isLoggedIn();

    boolean login(String username, String password);

    boolean logout();

    UserInfo getLoggedInUser();

    public boolean hasRole(UserRole role);

    RestTemplate getRestTemplate();

}
