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

import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam {

    public final Long id;
    public final Long institutionId;
    public final Long ownerId;
    public final String name;
    public final ExamStatus status;
    public final DateTime startTime;
    public final DateTime endTime;
    public final String lmsExamURL;

    public final Collection<IndicatorDefinition> indicators;
    public final Collection<ExamSEBConfigMapping> sebConfigMapping;

    @JsonCreator
    public Exam(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("ownerId") final Long ownerId,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final ExamStatus status,
            @JsonProperty("startTime") final DateTime startTime,
            @JsonProperty("endTime") final DateTime endTime,
            @JsonProperty("lmsExamURL") final String lmsExamURL,
            @JsonProperty("indicators") final Collection<IndicatorDefinition> indicators,
            @JsonProperty("sebConfigMapping") final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        this.id = id;
        this.institutionId = institutionId;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lmsExamURL = lmsExamURL;

        this.indicators = (indicators != null)
                ? Collections.unmodifiableCollection(indicators)
                : Collections.emptyList();
        this.sebConfigMapping = (sebConfigMapping != null)
                ? Collections.unmodifiableCollection(sebConfigMapping)
                : Collections.emptyList();
    }

    public Long getId() {
        return this.id;
    }

    public Long getInstitutionId() {
        return this.institutionId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
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

    public String getLmsExamURL() {
        return this.lmsExamURL;
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

    public final ExamRecord toExamRecord() {
        return new ExamRecord(
                this.id,
                this.institutionId,
                this.ownerId,
                this.name,
                this.status.name(),
                this.startTime,
                this.endTime,
                this.lmsExamURL);
    }

    public static final Exam of(
            final Long id,
            final Long institutionId,
            final Long ownerId,
            final String name,
            final ExamStatus status,
            final DateTime startTime,
            final DateTime endTime,
            final String lmsLoginURL) {

        return new Exam(id, institutionId, ownerId, name, status, startTime, endTime, lmsLoginURL, null, null);
    }

    public static final Exam of(
            final Exam prototype,
            final Collection<IndicatorDefinition> indicators,
            final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        return new Exam(
                prototype.id,
                prototype.institutionId,
                prototype.ownerId,
                prototype.name,
                prototype.status,
                prototype.startTime,
                prototype.endTime,
                prototype.lmsExamURL,
                indicators,
                sebConfigMapping);
    }

    @Override
    public String toString() {
        return "Exam [id=" + this.id + ", institutionId=" + this.institutionId + ", ownerId=" + this.ownerId + ", name="
                + this.name
                + ", status=" + this.status + ", startTime=" + this.startTime + ", endTime=" + this.endTime
                + ", lmsExamURL="
                + this.lmsExamURL + ", indicators=" + this.indicators + ", sebConfigMapping=" + this.sebConfigMapping
                + "]";
    }

}
