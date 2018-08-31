/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.auth;

import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;

public interface AuthorizationContextHolder {

    SEBServerAuthorizationContext getAuthorizationContext(HttpSession session);

    // TODO error handling!?
    default SEBServerAuthorizationContext getAuthorizationContext() {
        return getAuthorizationContext(RWT.getUISession().getHttpSession());
    }
}
