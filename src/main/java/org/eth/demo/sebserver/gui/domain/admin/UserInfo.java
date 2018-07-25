/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.admin;

import java.util.Collection;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {

    public final Long id;
    public final String name;
    public final String username;
    public final String email;
    public final DateTime creationDate;
    public final Boolean active;
    public final Collection<Role> roles;

    @JsonCreator
    public UserInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("username") final String username,
            @JsonProperty("email") final String email,
            @JsonProperty("creationDate") final DateTime creationDate,
            @JsonProperty("active") final Boolean active,
            @JsonProperty("roles") final Collection<Role> roles) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
        this.roles = roles;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public DateTime getCreationDate() {
        return this.creationDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    public boolean hasRole(final UserRole role) {
        return hasRole(role.name());
    }

    public boolean hasRole(final String role) {
        for (final Role r : this.roles) {
            if (r.roleName.equals(role)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UserInfo other = (UserInfo) obj;
        if (this.username == null) {
            if (other.username != null)
                return false;
        } else if (!this.username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserInfo [name=" + this.name + ", username=" + this.username + ", email=" + this.email
                + ", creationDate="
                + this.creationDate + ", active=" + this.active + ", roles=" + this.roles + "]";
    }

    public static final class Role {

        public final String roleName;

        @JsonCreator
        public Role(@JsonProperty("role") final String roleName) {
            this.roleName = roleName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.roleName == null) ? 0 : this.roleName.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Role other = (Role) obj;
            if (this.roleName == null) {
                if (other.roleName != null)
                    return false;
            } else if (!this.roleName.equals(other.roleName))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Role [roleName=" + this.roleName + "]";
        }
    }

}
