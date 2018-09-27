/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;

public final class RoleTypeGrant {

    public final RoleTypeKey roleTypeKey;
    public final boolean read;
    public final boolean modify;
    public final boolean write;
    public final boolean modifyOwnerOnly;
    public final boolean writeOwnerOnly;
    public final boolean institutionOnly;

    public RoleTypeGrant(
            final boolean read,
            final boolean modify,
            final boolean write,
            final boolean institutionOnly,
            final boolean modifyOwnerOnly,
            final boolean writeOwnerOnly,
            final GrantEntityType type,
            final Role role) {

        this.roleTypeKey = new RoleTypeKey(type, role);
        this.read = read;
        this.modify = modify;
        this.write = write;
        this.modifyOwnerOnly = modifyOwnerOnly;
        this.writeOwnerOnly = writeOwnerOnly;
        this.institutionOnly = institutionOnly;
    }

    public static final class RoleTypeKey {
        public final GrantEntityType type;
        public final Role role;

        public RoleTypeKey(final GrantEntityType type, final Role role) {
            this.type = type;
            this.role = role;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.role == null) ? 0 : this.role.hashCode());
            result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
            final RoleTypeKey other = (RoleTypeKey) obj;
            if (this.role != other.role)
                return false;
            if (this.type != other.type)
                return false;
            return true;
        }
    }

}
