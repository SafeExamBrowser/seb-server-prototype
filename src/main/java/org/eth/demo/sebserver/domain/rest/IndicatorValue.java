/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class IndicatorValue {

    public final UUID clientUUID;
    public final String type;
    @JsonSerialize(using = IndicatorValue.BigDecimalSerializer.class)
    private final BigDecimal value;

    public IndicatorValue(
            @JsonProperty("clientUUID") final UUID clientUUID,
            @JsonProperty("type") final String type,
            @JsonProperty("value") final BigDecimal value) {

        this.clientUUID = clientUUID;
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public static final class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(
                final BigDecimal value,
                final JsonGenerator gen,
                final SerializerProvider provider) throws IOException, JsonProcessingException {

            gen.writeString(value.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
        }
    }

}
