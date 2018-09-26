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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Role implements GrantedAuthority {

    private static final long serialVersionUID = -5304544591727115010L;

    //@formatter:off
    public enum UserRole {
        SEB_SERVER_ADMIN(new Role("SEB_SERVER_ADMIN")),
        INSTITUTIONAL_ADMIN(new Role("INSTITUTIONAL_ADMIN")),
        EXAM_ADMIN(new Role("EXAM_ADMIN")),
        EXAM_SUPPORTER(new Role("EXAM_SUPPORTER")),

        LMS_CLIENT(new Role("LMS_CLIENT")),
        SEB_CLIENT(new Role("SEB_CLIENT"))

        ;

        public final Role role;

        private UserRole(final Role role) {
            this.role = role;
        }

        public static final UserRole byName(final String name) {
            for (final UserRole userRole : UserRole.values()) {
                if (userRole.role.roleName.equals(name)) {
                    return userRole;
                }
            }

            throw new IllegalArgumentException("No UserRole with name: " + name + " found");
        }
    }
    //@formatter:on

    public final String roleName;

    @JsonCreator
    public Role(@JsonProperty("role") final String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return this.roleName;
    }

    public static Role fromRecord(final RoleRecord record) {
        return new Role(record.getRoleName());
    }

}
