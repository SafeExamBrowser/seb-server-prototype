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

public final class Grant implements GrantedAuthority {

    private static final long serialVersionUID = -5304544591727115010L;

    private final String grantName;

    @JsonCreator
    public Grant(@JsonProperty("grant") final String grantName) {
        this.grantName = grantName;
    }

    public String getGrantName() {
        return this.grantName;
    }

    @Override
    public String getAuthority() {
        return this.grantName;
    }

    public static Grant fromRecord(final RoleRecord record) {
        return new Grant(record.getRoleName());
    }

}
