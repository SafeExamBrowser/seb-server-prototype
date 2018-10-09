/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.GrantEntity;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam implements GrantEntity {

    public enum ExamStatus {
        READY,
        RUNNING,
        FINISHED
    }

    public enum ExamType {
        MANAGED,
        BYOD,
        VDI
    }

    public final Long id;
    public final Long institutionId;
    public final Long lmsSetupId;
    public final String external_uuid;
    public final String name;
    public final String description;
    public final ExamStatus status;
    public final DateTime startTime;
    public final DateTime endTime;
    public final String enrollmentURL;
    public final ExamType type;
    public final String owner;

    public final Collection<String> supporter;
    public final Collection<IndicatorDefinition> indicators;
    public final Collection<ExamSEBConfigMapping> sebConfigMapping;

    @JsonCreator
    public Exam(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("lmsSetupId") final Long lmsSetupId,
            @JsonProperty("external_uuid") final String external_uuid,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("status") final ExamStatus status,
            @JsonProperty("startTime") final DateTime startTime,
            @JsonProperty("endTime") final DateTime endTime,
            @JsonProperty("enrollmentURL") final String enrollmentURL,
            @JsonProperty("type") final ExamType type,
            @JsonProperty("owner") final String owner,
            @JsonProperty("supporter") final Collection<String> supporter,
            @JsonProperty("indicators") final Collection<IndicatorDefinition> indicators,
            @JsonProperty("sebConfigMapping") final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        this.id = id;
        this.institutionId = institutionId;
        this.lmsSetupId = lmsSetupId;
        this.external_uuid = external_uuid;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrollmentURL = enrollmentURL;
        this.type = type;
        this.owner = owner;

        this.supporter = (supporter != null)
                ? Collections.unmodifiableCollection(supporter)
                : Collections.emptyList();
        this.indicators = (indicators != null)
                ? Collections.unmodifiableCollection(indicators)
                : Collections.emptyList();
        this.sebConfigMapping = (sebConfigMapping != null)
                ? Collections.unmodifiableCollection(sebConfigMapping)
                : Collections.emptyList();
    }

    @Override
    public GrantEntityType grantEntityType() {
        return GrantEntityType.EXAM;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public Long getInstitutionId() {
        return this.institutionId;
    }

    @Override
    public String getOwner() {
        // NOTE: No owner for Exams so far
        return this.owner;
    }

    public Long getLmsSetupId() {
        return this.lmsSetupId;
    }

    public String getExternal_uuid() {
        return this.external_uuid;
    }

    public ExamType getType() {
        return this.type;
    }

    public Collection<String> getSupporter() {
        return this.supporter;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExamStatus getStatus() {
        return this.status;
    }

    public DateTime getStartTime() {
        return this.startTime;
    }

    public DateTime getEndTime() {
        return this.endTime;
    }

    public String getEnrollmentURL() {
        return this.enrollmentURL;
    }

    public Collection<IndicatorDefinition> getIndicators() {
        return this.indicators;
    }

    public Collection<ExamSEBConfigMapping> getSebConfigMapping() {
        return this.sebConfigMapping;
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
        final Exam other = (Exam) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    public static final Exam of(
            final Long id,
            final Long institutionId,
            final Long lmsSetupId,
            final String external_uuid,
            final String name,
            final String description,
            final ExamStatus status,
            final DateTime startTime,
            final DateTime endTime,
            final String enrollmentURL,
            final String type,
            final String owner,
            final String supporter) {

        final String[] split = StringUtils.split(supporter, ",");
        final Collection<String> supp = (split != null)
                ? Arrays.asList(split)
                : Lists.emptyList();

        return new Exam(
                id, institutionId, lmsSetupId, external_uuid, name, description,
                status, startTime, endTime, enrollmentURL,
                ExamType.valueOf(type), owner, supp, null, null);
    }

    public static final Exam of(
            final Exam prototype,
            final Collection<IndicatorDefinition> indicators,
            final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        return new Exam(
                prototype.id,
                prototype.institutionId,
                prototype.lmsSetupId,
                prototype.external_uuid,
                prototype.name,
                prototype.description,
                prototype.status,
                prototype.startTime,
                prototype.endTime,
                prototype.enrollmentURL,
                prototype.type,
                prototype.owner,
                prototype.supporter,
                indicators,
                sebConfigMapping);
    }

    @Override
    public String toString() {
        return "Exam [id=" + this.id + ", institutionId=" + this.institutionId + ", lmsSetupId=" + this.lmsSetupId
                + ", external_uuid="
                + this.external_uuid + ", name=" + this.name + ", description=" + this.description + ", status="
                + this.status
                + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", enrollmentURL="
                + this.enrollmentURL + ", type="
                + this.type + ", owner=" + this.owner + ", supporter=" + this.supporter + ", indicators="
                + this.indicators
                + ", sebConfigMapping=" + this.sebConfigMapping + "]";
    }

}
