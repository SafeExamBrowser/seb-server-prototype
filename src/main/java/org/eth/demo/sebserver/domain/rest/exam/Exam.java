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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam {

    public final Long id;
    public final String name;
    public final ExamStatus status;
    public final Long ownerId;
    public final Collection<IndicatorDefinition> indicators;
    public final Collection<ExamSEBConfigMapping> sebConfigMapping;

    @JsonCreator
    public Exam(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final ExamStatus status,
            @JsonProperty("ownerId") final Long ownerId,
            @JsonProperty("indicators") final Collection<IndicatorDefinition> indicators,
            @JsonProperty("sebConfigMapping") final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.ownerId = ownerId;
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

    public String getName() {
        return this.name;
    }

    public ExamStatus getStatus() {
        return this.status;
    }

    public Long getOwnerId() {
        return this.ownerId;
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

//    public static final Exam create(
//            final Long id,
//            final String name,
//            final Integer status,
//            final Long ownerId) {
//
//        return new Exam(id, name, status, ownerId, new ArrayList<>());
//    }

    public final ExamRecord toExamRecord() {
        return new ExamRecord(this.id, this.name, this.status.name(), this.ownerId);
    }

    public static final Exam of(
            final Exam prototype,
            final Collection<IndicatorDefinition> indicators,
            final Collection<ExamSEBConfigMapping> sebConfigMapping) {

        return new Exam(
                prototype.id,
                prototype.name,
                prototype.status,
                prototype.ownerId,
                indicators,
                sebConfigMapping);

    }

//    public static final Exam fromRecords(
//            final ExamRecord record,
//            final Collection<IndicatorRecord> indicators) {
//
//        return new Exam(
//                record.getId(),
//                record.getName(),
//                record.getStatus(),
//                record.getId(),
//                indicators.stream()
//                        .map(IndicatorDefinition::fromRecord)
//                        .collect(Collectors.toList()));
//    }
}
