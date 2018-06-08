/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import java.math.BigDecimal;
import java.util.UUID;

import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ClientEvent {

    public static enum EventType {
        UNKNOWN(-1), PING(1), ERROR(2);

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

    public final UUID clientId;
    public final EventType type;
    public final Long timestamp;
    public final BigDecimal numValue;
    public final String text;

    @JsonCreator
    ClientEvent(@JsonProperty("clientId") final UUID clientId,
            @JsonProperty("type") final Integer type,
            @JsonProperty("timestamp") final Long timestamp,
            @JsonProperty("numValue") final BigDecimal numValue,
            @JsonProperty("text") final String text) {

        this.clientId = clientId;
        this.type = (type != null) ? EventType.byId(type) : EventType.UNKNOWN;
        this.timestamp = (timestamp != null) ? timestamp : System.currentTimeMillis();
        this.numValue = numValue;
        this.text = text;
    }

    public UUID getClientId() {
        return this.clientId;
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

    public ClientEventRecord toRecord(final Long examId, final Long clientId) {
        return new ClientEventRecord(null, examId, clientId, this.type.id, this.timestamp, this.numValue,
                this.text);
    }

}
