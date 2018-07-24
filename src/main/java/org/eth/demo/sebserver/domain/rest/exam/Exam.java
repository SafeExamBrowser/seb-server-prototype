/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.batis.gen.model.IndicatorRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam {

    public final Long id;
    public final String name;
    public final Integer status;
    public final String statusName;
    public final Long ownerId;
    private final Collection<IndicatorDefinition> indicators;

    @JsonCreator
    Exam(@JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final Integer status,
            @JsonProperty("ownerId") final Long ownerId,
            @JsonProperty("indicators") final Collection<IndicatorDefinition> indicators) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.ownerId = ownerId;
        this.statusName = ExamStatus.getDisplayName(status);
        this.indicators = (indicators != null) ? new ArrayList<>(indicators) : new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Collection<IndicatorDefinition> getIndicators() {
        return this.indicators;
    }

    public void addIndicator(final IndicatorDefinition indicator) {
        if (indicator == null) {
            return;
        }
        this.indicators.add(indicator);
    }

    public static final Exam create(final ExamIndicatorJoinMapper.JoinRecord record) {

        return new Exam(
                record.id,
                record.name,
                record.status,
                record.ownerId,
                new ArrayList<>());
    }

    public static final Exam create(
            final Long id,
            final String name,
            final Integer status,
            final Long ownerId) {

        return new Exam(id, name, status, ownerId, new ArrayList<>());
    }

    public final ExamRecord toExamRecord() {
        return new ExamRecord(this.id, this.name, this.status, this.ownerId);
    }

    public static final Exam fromRecords(
            final ExamRecord record,
            final Collection<IndicatorRecord> indicators) {

        return new Exam(
                record.getId(),
                record.getName(),
                record.getStatus(),
                record.getId(),
                indicators.stream()
                        .map(IndicatorDefinition::fromRecord)
                        .collect(Collectors.toList()));
    }
}
