/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.batis.gen.model.IndicatorRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Exam {

    public enum Status {
        NONE(-1, "NO STATUS"), IN_PROGRESS(0, "In Progress"), READY(1, "Ready to run"), RUNNING(2,
                "Running"), FINISHED(3, "Finished");

        public final int id;
        public final String displayName;

        private Status(final int id, final String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public static String getDisplayName(final int id) {
            for (final Status status : Status.values()) {
                if (status.id == id) {
                    return status.displayName;
                }
            }

            return NONE.displayName;
        }
    }

    public final Long id;
    public final String name;
    public final Integer status;
    public final String statusName;
    public final Long configurationId;
    private final Collection<Indicator> indicators;

    @JsonCreator
    Exam(@JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final Integer status,
            @JsonProperty("configurationId") final Long configurationId,
            @JsonProperty("indicators") final Collection<Indicator> indicators) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.statusName = Status.getDisplayName(status);
        this.configurationId = configurationId;
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

    public Long getConfigurationId() {
        return this.configurationId;
    }

    public Collection<Indicator> getIndicators() {
        return this.indicators;
    }

    public void addIndicator(final Indicator indicator) {
        if (indicator == null) {
            return;
        }
        this.indicators.add(indicator);
    }

    public final ExamRecord toRecord() {
        return new ExamRecord(this.id, this.name, this.status, this.configurationId);
    }

    public final Exam withId(final Long id) {
        return new Exam(id, this.name, this.status, this.configurationId, this.indicators);
    }

    public static final Exam fromRecord(final ExamRecord record, final Collection<IndicatorRecord> indicators) {
        return new Exam(
                record.getId(),
                record.getName(),
                record.getStatus(),
                record.getConfigurationId(),
                indicators.stream()
                        .map(Indicator::fromRecord)
                        .collect(Collectors.toList()));
    }

    public static final Exam fromRecord(final ExamRecord record) {
        return new Exam(record.getId(), record.getName(), record.getStatus(), record.getConfigurationId(), null);
    }

    public static final Exam create(final Long id, final String name, final Integer status,
            final Long configurationId) {
        return new Exam(id, name, status, configurationId, null);
    }

//    public final static class ExamDeserializer extends StdDeserializer<Exam> {
//
//        private static final long serialVersionUID = 2022986122392930339L;
//
//        protected ExamDeserializer(final Class<?> vc) {
//            super(vc);
//        }
//
//        @Override
//        public Exam deserialize(final JsonParser p, final DeserializationContext ctxt)
//                throws IOException, JsonProcessingException {
//
//            TreeNode examNode = p.readValueAsTree();
//            Long id = ((LongNode) examNode.get("id")).asLong();
//            return null;
//        }
//
//    }

}
