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

import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.GrantEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LmsSetup implements GrantEntity {

    public enum LMSType {
        MOCKUP,
        MOODLE,
        OPEN_EDX,
        OPEN_OLAT
    }

    public static final Collection<LMSType> LMS_TYPE_SELECTION = Collections
            .unmodifiableList(Arrays.asList(LMSType.values()));

    public final Long id;
    public final Long institutionId;
    public final String name;
    public final LMSType lmsType;
    public final String lmsAuthName;
    public final String lmsAuthSecret;
    public final String lmsApiUrl;
    public final String lmsRestApiToken;
    public final String sebAuthName;
    public final String sebAuthSecret;

    @JsonCreator
    public LmsSetup(
            @JsonProperty("id") final Long id,
            @JsonProperty(value = "institutionId", required = true) final Long institutionId,
            @JsonProperty(value = "name, required = true") final String name,
            @JsonProperty(value = "lmsType", required = true) final LMSType lmsType,
            @JsonProperty(value = "lmsAuthName, required = true") final String lmsAuthName,
            @JsonProperty(value = "lmsAuthSecret, required = true") final String lmsAuthSecret,
            @JsonProperty(value = "lmsApiUrl, required = true") final String lmsApiUrl,
            @JsonProperty(value = "lmsRestApiToken, required = true") final String lmsRestApiToken,
            @JsonProperty(value = "sebAuthName, required = true") final String sebAuthName,
            @JsonProperty(value = "sebAuthSecret, required = true") final String sebAuthSecret) {

        this.id = id;
        this.institutionId = institutionId;
        this.name = name;
        this.lmsType = lmsType;
        this.lmsAuthName = lmsAuthName;
        this.lmsAuthSecret = lmsAuthSecret;
        this.lmsApiUrl = lmsApiUrl;
        this.lmsRestApiToken = lmsRestApiToken;
        this.sebAuthName = sebAuthName;
        this.sebAuthSecret = sebAuthSecret;
    }

    @Override
    public GrantEntityType grantEntityType() {
        return GrantEntityType.SEB_LMS_SETUP;
    }

    @JsonIgnore
    @Override
    public String getOwner() {
        // NOTE: No owner id so far
        return null;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public Long getInstitutionId() {
        return this.institutionId;
    }

    public String getName() {
        return this.name;
    }

    public LMSType getLmsType() {
        return this.lmsType;
    }

    public String getLmsAuthName() {
        return this.lmsAuthName;
    }

    public String getLmsAuthSecret() {
        return this.lmsAuthSecret;
    }

    public String getLmsApiUrl() {
        return this.lmsApiUrl;
    }

    public String getLmsRestApiToken() {
        return this.lmsRestApiToken;
    }

    public String getSebAuthName() {
        return this.sebAuthName;
    }

    public String getSebAuthSecret() {
        return this.sebAuthSecret;
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
        final LmsSetup other = (LmsSetup) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SebLmsSetup [id=" + this.id + ", institutionId=" + this.institutionId + ", name=" + this.name
                + ", lmsType=" + this.lmsType
                + ", lmsAuthName=" + this.lmsAuthName + ", lmsAuthSecret=" + this.lmsAuthSecret + ", lmsApiUrl="
                + this.lmsApiUrl
                + ", lmsRestApiToken=" + this.lmsRestApiToken + ", sebAuthName=" + this.sebAuthName + ", sebAuthSecret="
                + this.sebAuthSecret + "]";
    }

    public static LmsSetup of(final SebLmsSetupRecord record) {
        return new LmsSetup(
                record.getId(),
                record.getInstitutionId(),
                record.getName(),
                LMSType.valueOf(record.getLmsType()),
                record.getLmsClientname(),
                record.getLmsClientsecret(),
                record.getLmsUrl(),
                record.getLmsRestApiToken(),
                record.getSebClientname(),
                record.getSebClientsecret());
    }
}
