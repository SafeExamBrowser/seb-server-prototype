/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    SEB_SERVER_ADMIN,
    INSTITUTIONAL_ADMIN,
    EXAM_ADMIN,
    EXAM_SUPPORTER,

    LMS_CLIENT,
    SEB_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
