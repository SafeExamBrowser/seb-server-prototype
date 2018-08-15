/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public final class ClientAuth {

    public final Long institutionId;
    public final String clientName;
    public final String clientAddress;
    public final String lmsUrl;

    public ClientAuth(
            final Long institutionId,
            final String clientName,
            final String clientAddress,
            final String lmsUrl) {

        this.institutionId = institutionId;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.lmsUrl = lmsUrl;
    }

    public ClientAuth(
            final Long institutionId,
            final String clientName,
            final String clientAddress) {

        this.institutionId = institutionId;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.lmsUrl = null;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Long getInstitutionId() {
        return this.institutionId;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public String getLmsUrl() {
        return this.lmsUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.clientName == null) ? 0 : this.clientName.hashCode());
        result = prime * result + ((this.institutionId == null) ? 0 : this.institutionId.hashCode());
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
        final ClientAuth other = (ClientAuth) obj;
        if (this.clientName == null) {
            if (other.clientName != null)
                return false;
        } else if (!this.clientName.equals(other.clientName))
            return false;
        if (this.institutionId == null) {
            if (other.institutionId != null)
                return false;
        } else if (!this.institutionId.equals(other.institutionId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ClientAuth [institutionId=" + this.institutionId + ", clientName=" + this.clientName
                + ", clientAddress="
                + this.clientAddress + ", lmsUrl=" + this.lmsUrl + "]";
    }

    public static final class ClientAuthentication implements Authentication {

        private static final long serialVersionUID = 4832650248834396365L;

        private final String name;
        private final ClientAuth principal;
        private final Collection<GrantedAuthority> authorities;

        public ClientAuthentication(
                final ClientAuth principal,
                final Role.UserRole userRole) {

            this.name = null;
            this.principal = principal;
            this.authorities = Collections.unmodifiableList(Arrays.asList(userRole.role));
        }

        public ClientAuthentication(
                final String name,
                final ClientAuth principal,
                final Role.UserRole userRole) {

            this.name = name;
            this.principal = principal;
            this.authorities = Collections.unmodifiableList(Arrays.asList(userRole.role));
        }

        @Override
        public String getName() {
            return (this.name != null) ? this.name : "SEBClientAuth";
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.authorities;
        }

        @Override
        public Object getCredentials() {
            return this.principal;
        }

        @Override
        public Object getDetails() {
            return this.principal;
        }

        @Override
        public Object getPrincipal() {
            return this.principal;
        }

        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
    }

}
