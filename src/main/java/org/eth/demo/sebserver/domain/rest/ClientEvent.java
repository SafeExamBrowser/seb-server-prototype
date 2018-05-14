/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import java.math.BigDecimal;

import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ClientEvent {

    public final int type;
    public final Long timestamp;
    public final BigDecimal numValue;
    public final String text;

    @JsonCreator
    ClientEvent(@JsonProperty("type") final int type,
            @JsonProperty("timestamp") final Long timestamp,
            @JsonProperty("numValue") final BigDecimal numValue,
            @JsonProperty("text") final String text) {

        super();
        this.type = type;
        this.timestamp = timestamp;
        this.numValue = numValue;
        this.text = text;
    }

    public int getType() {
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

    public ClientEventRecord toRecord(final Long examId, final Long clientUUID) {
        return new ClientEventRecord(null, examId, clientUUID, this.type, this.timestamp, this.numValue,
                this.text);
    }

}
