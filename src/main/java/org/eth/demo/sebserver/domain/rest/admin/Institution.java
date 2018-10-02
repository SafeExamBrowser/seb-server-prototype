/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.model.InstitutionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.SebLmsSetup.LMSType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.GrantEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Institution implements GrantEntity {

    public enum AuthType {
        INTERNAL,
        EXTERNAL_LDAP
    }

    public static final Collection<AuthType> AUTH_TYPE_SELECTION =
            Collections.unmodifiableList(Arrays.asList(AuthType.values()));

    public final Long id;
    public final String name;
    public final AuthType authType;
    public final Collection<SebLmsSetup> sebLmsSetup;

    @JsonCreator
    public Institution(
            @JsonProperty("id") final Long id,
            @JsonProperty(value = "name", required = true) final String name,
            @JsonProperty("authType") final AuthType authType,
            @JsonProperty("sebLmsSetup") final Collection<SebLmsSetup> sebLmsSetup) {

        this.id = id;
        this.name = name;
        this.authType = authType;
        this.sebLmsSetup = (sebLmsSetup != null)
                ? Collections.unmodifiableCollection(sebLmsSetup)
                : Collections.emptyList();
    }

    @Override
    public GrantEntityType grantEntityType() {
        return GrantEntityType.INSTITUTION;
    }

    @JsonIgnore
    @Override
    public Long getInstitutionId() {
        return this.id;
    }

    @JsonIgnore
    @Override
    public Long getOwnerId() {
        // NOTE: No owner for institution
        return null;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public AuthType getAuthType() {
        return this.authType;
    }

    public Collection<AuthType> getAuthTypeSelection() {
        return AUTH_TYPE_SELECTION;
    }

    public Collection<LMSType> getLmsTypeSelection() {
        return SebLmsSetup.LMS_TYPE_SELECTION;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        final Institution other = (Institution) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Institution [id=" + this.id + ", name=" + this.name + "]";
    }

    public static Institution of(final InstitutionRecord record, final List<SebLmsSetupRecord> setups) {
        return new Institution(
                record.getId(),
                record.getName(),
                AuthType.valueOf(record.getAuthtype()),
                (setups != null) ? setups.stream()
                        .map(SebLmsSetup::of)
                        .collect(Collectors.toList()) : Collections.emptyList());
    }

}
