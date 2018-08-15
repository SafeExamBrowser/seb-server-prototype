/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import java.math.BigDecimal;

import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ClientEvent {

    public static enum EventType {
        UNKNOWN(-1), PING(1), LOG(2), ERROR(3);

        public final int id;

        private EventType(final int id) {
            this.id = id;
        }

        public static EventType byId(final int id) {
            for (final EventType status : EventType.values()) {
                if (status.id == id) {
                    return status;
                }
            }

            return UNKNOWN;
        }
    }

    public final String userIdentifier;
    public final EventType type;
    public final Long timestamp;
    public final BigDecimal numValue;
    public final String text;

    @JsonCreator
    ClientEvent(
            @JsonProperty("userIdentifier") final String userIdentifier,
            @JsonProperty("type") final Integer type,
            @JsonProperty("timestamp") final Long timestamp,
            @JsonProperty("numValue") final BigDecimal numValue,
            @JsonProperty("text") final String text) {

        this.userIdentifier = userIdentifier;
        this.type = (type != null) ? EventType.byId(type) : EventType.UNKNOWN;
        this.timestamp = (timestamp != null) ? timestamp : System.currentTimeMillis();
        this.numValue = numValue;
        this.text = text;
    }

    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    public EventType getType() {
        return this.type;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public BigDecimal getNumValue() {
        return this.numValue;
    }

    public String getText() {
        return this.text;
    }

    public ClientEventRecord toRecord(final Long examId) {
        return new ClientEventRecord(
                null,
                this.userIdentifier,
                this.type.id,
                this.timestamp,
                this.numValue,
                this.text);
    }

}
