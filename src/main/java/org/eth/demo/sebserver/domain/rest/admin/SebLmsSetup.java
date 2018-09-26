/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.service.lms.LmsSetup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SebLmsSetup implements LmsSetup {

    public enum LMSType {
        MOCKUP,
        MOODLE,
        OPEN_EDX,
        OPEN_OLAT
    }

    public final Long id;
    public final Long institutionId;
    public final LMSType lmsType;
    public final String lmsAuthName;
    public final String lmsAuthSecret;
    public final String lmsApiUrl;
    public final String sebAuthName;
    public final String sebAuthSecret;

    @JsonCreator
    public SebLmsSetup(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("lmsType") final LMSType lmsType,
            @JsonProperty("lmsAuthName") final String lmsAuthName,
            @JsonProperty("lmsAuthSecret") final String lmsAuthSecret,
            @JsonProperty("lmsApiUrl") final String lmsApiUrl,
            @JsonProperty("sebAuthName") final String sebAuthName,
            @JsonProperty("sebAuthSecret") final String sebAuthSecret) {

        this.id = id;
        this.institutionId = institutionId;
        this.lmsType = lmsType;
        this.lmsAuthName = lmsAuthName;
        this.lmsAuthSecret = lmsAuthSecret;
        this.lmsApiUrl = lmsApiUrl;
        this.sebAuthName = sebAuthName;
        this.sebAuthSecret = sebAuthSecret;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getInstitutionId() {
        return this.institutionId;
    }

    @Override
    public LMSType getLmsType() {
        return this.lmsType;
    }

    @Override
    public String getLmsAuthName() {
        return this.lmsAuthName;
    }

    @Override
    public String getLmsAuthSecret() {
        return this.lmsAuthSecret;
    }

    @Override
    public String getLmsApiUrl() {
        return this.lmsApiUrl;
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
        final SebLmsSetup other = (SebLmsSetup) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SebLmsSetup [id=" + this.id + ", institutionId=" + this.institutionId + ", lmsType=" + this.lmsType
                + ", lmsAuthName="
                + this.lmsAuthName + ", lmsAuthSecret=" + this.lmsAuthSecret + ", lmsApiUrl=" + this.lmsApiUrl
                + ", sebAuthName="
                + this.sebAuthName + ", sebAuthSecret=" + this.sebAuthSecret + "]";
    }

    public static SebLmsSetup of(final SebLmsSetupRecord record) {
        return new SebLmsSetup(
                record.getId(),
                record.getInstitutionId(),
                LMSType.valueOf(record.getLmsType()),
                record.getLmsClientname(),
                record.getLmsClientsecret(),
                record.getLmsUrl(),
                record.getSebClientname(),
                record.getSebClientsecret());
    }

}
