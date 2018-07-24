/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import org.eth.demo.sebserver.batis.gen.model.RoleRecord;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Role implements GrantedAuthority {

    private static final long serialVersionUID = -5304544591727115010L;

    public final String roleName;

    @JsonCreator
    public Role(@JsonProperty("role") final String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public String getAuthority() {
        return this.roleName;
    }

    public static Role fromRecord(final RoleRecord record) {
        return new Role(record.getRoleName());
    }

}
