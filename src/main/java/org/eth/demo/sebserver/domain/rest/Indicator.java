/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import java.math.BigDecimal;

import org.eth.demo.sebserver.batis.gen.model.IndicatorRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Indicator {

    public final String type;
    public final BigDecimal threshold1;
    public final BigDecimal threshold2;
    public final BigDecimal threshold3;

    @JsonCreator
    public Indicator(@JsonProperty("type") final String type,
            @JsonProperty("threshold1") final BigDecimal threshold1,
            @JsonProperty("threshold2") final BigDecimal threshold2,
            @JsonProperty("threshold3") final BigDecimal threshold3) {

        this.type = type;
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
        this.threshold3 = threshold3;
    }

    @Override
    public String toString() {
        return "Indicator [type=" + this.type
                + ", threshold1=" + this.threshold1
                + ", threshold2=" + this.threshold2
                + ", threshold3=" + this.threshold3 + "]";
    }

    public final IndicatorRecord toRecord(final Long examId) {
        return new IndicatorRecord(
                null,
                examId,
                this.type,
                this.threshold1,
                this.threshold2,
                this.threshold3);
    }

    public static final Indicator fromRecord(final IndicatorRecord record) {
        return new Indicator(
                record.getType(),
                record.getThreshold1(),
                record.getThreshold2(),
                record.getThreshold3());
    }

}
