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

import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public abstract class ClientConnectionAuth implements Authentication {

    private static final long serialVersionUID = -3884348408614505920L;

    public final Collection<GrantedAuthority> authorities;

    private ClientConnectionAuth(final GrantedAuthority authority) {
        this.authorities = Collections.unmodifiableList(Arrays.asList(authority));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    public static final ClientConnectionAuth lmsAuthOf(final SebLmsSetupRecord setup, final String clientAddress) {
        return new LMSConnectionAuth(setup.getInstitutionId(), setup.getLmsClientname(), clientAddress);
    }

    public static final ClientConnectionAuth sebAuthOf(final SebLmsSetupRecord setup, final String clientAddress) {
        return new SEBConnectionAuth(
                setup.getInstitutionId(),
                setup.getSebClientname(),
                clientAddress, setup.getLmsUrl());
    }

    public static final ClientConnectionAuth sebWebSocketAuthOf(final ClientConnectionRecord connection) {
        return new SEBWebSocketAuth(
                connection.getExamId(),
                connection.getId(),
                connection.getUserIdentifier());
    }

    public static final ClientConnectionAuth sebWebSocketAuthOf(
            final Long examId,
            final Long connectionId,
            final String userIdentifier) {

        return new SEBWebSocketAuth(examId, connectionId, userIdentifier);
    }

    public static final class SEBConnectionAuth extends ClientConnectionAuth {

        private static final long serialVersionUID = 2353164128396105555L;

        public final Long institutionId;
        public final String sebClientname;
        public final String clientAddress;
        public final String lmsUrl;

        private SEBConnectionAuth(
                final Long institutionId,
                final String sebClientname,
                final String clientAddress,
                final String lmsUrl) {

            super(Role.SEB_CLIENT);
            this.institutionId = institutionId;
            this.sebClientname = sebClientname;
            this.clientAddress = clientAddress;
            this.lmsUrl = lmsUrl;
        }

        @Override
        public String getName() {
            return this.sebClientname;
        }

        @Override
        public String toString() {
            return "SEBConnectionAuth [institutionId=" + this.institutionId + ", sebClientname=" + this.sebClientname
                    + ", clientAddress=" + this.clientAddress + ", lmsUrl=" + this.lmsUrl + "]";
        }
    }

    public static final class SEBWebSocketAuth extends ClientConnectionAuth {

        private static final long serialVersionUID = -2254669992045447197L;

        public final String userIdentifier;
        public final Long examId;
        public final Long connectionId;

        private SEBWebSocketAuth(
                final Long examId,
                final Long connectionId,
                final String userIdentifier) {

            super(Role.SEB_CLIENT);
            this.examId = examId;
            this.connectionId = connectionId;
            this.userIdentifier = userIdentifier;
        }

        @Override
        public String getName() {
            return this.userIdentifier;
        }

        @Override
        public String toString() {
            return "SEBWebSocketAuth [userIdentifier=" + this.userIdentifier + ", examId=" + this.examId
                    + ", connectionId="
                    + this.connectionId + "]";
        }

    }

    public static final class LMSConnectionAuth extends ClientConnectionAuth {

        private static final long serialVersionUID = 1112691657763662897L;

        public final Long institutionId;
        public final String lmsClientname;
        public final String clientAddress;

        private LMSConnectionAuth(final Long institutionId, final String lmsClientname, final String clientAddress) {
            super(Role.LMS_CLIENT);
            this.institutionId = institutionId;
            this.lmsClientname = lmsClientname;
            this.clientAddress = clientAddress;
        }

        @Override
        public String getName() {
            return this.lmsClientname;
        }

        @Override
        public String toString() {
            return "LMSConnectionAuth [institutionId=" + this.institutionId + ", lmsClientname=" + this.lmsClientname
                    + ", clientAddress=" + this.clientAddress + "]";
        }
    }

}
