/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import java.util.Collection;
import java.util.Collections;

import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.GrantEntity;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam implements GrantEntity {

    public enum ExamStatus {
        READY,
        RUNNING,
        FINISHED
    }

    public final Long id;
    public final Long institutionId;
    public final Long lmsSetupId;
    public final String name;
    public final String description;
    public final ExamStatus status;
    public final DateTime startTime;
    public final DateTime endTime;
    public final String enrollmentURL;

    public final Collection<IndicatorDefinition> indicators;
    public final Collection<ExamSEBConfigMapping> sebConfigMapping;

    @JsonCreator
    public Exam(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("lmsSetupId") final Long lmsSetupId,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("status") final ExamStatus status,
            @JsonProperty("startTime") final DateTime startTime,
            @JsonProperty("endTime") final DateTime endTime,
            @JsonProperty("enrollmentURL") final String enrollmentURL,
            @JsonProperty("indicators") final Collection<IndicatorDefinition> indicators,
            @JsonProperty("sebConfigMapping") final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        this.id = id;
        this.institutionId = institutionId;
        this.lmsSetupId = lmsSetupId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrollmentURL = enrollmentURL;

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

    @JsonIgnore
    @Override
    public Long getOwnerId() {
        // NOTE: No owner for Exams so far
        return null;
    }

    public Long getLmsSetupId() {
        return this.lmsSetupId;
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
            final String name,
            final String description,
            final ExamStatus status,
            final DateTime startTime,
            final DateTime endTime,
            final String enrollmentURL) {

        return new Exam(
                id, institutionId, lmsSetupId, name, description,
                status, startTime, endTime, enrollmentURL, null, null);
    }

    public static final Exam of(
            final Exam prototype,
            final Collection<IndicatorDefinition> indicators,
            final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        return new Exam(
                prototype.id,
                prototype.institutionId,
                prototype.lmsSetupId,
                prototype.name,
                prototype.description,
                prototype.status,
                prototype.startTime,
                prototype.endTime,
                prototype.enrollmentURL,
                indicators,
                sebConfigMapping);
    }

    @Override
    public String toString() {
        return "Exam [id=" + this.id + ", institutionId=" + this.institutionId + ", lmsSetupId=" + this.lmsSetupId
                + ", name=" + this.name
                + ", description=" + this.description + ", status=" + this.status + ", startTime=" + this.startTime
                + ", endTime="
                + this.endTime + ", enrollmentURL=" + this.enrollmentURL + ", indicators=" + this.indicators
                + ", sebConfigMapping="
                + this.sebConfigMapping + "]";
    }

}
