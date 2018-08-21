/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionRow {

    public final String userIdentifier;
    public final String status;
    public final String address;
    public final Collection<IndicatorValue> indicatorValues;

    @JsonCreator
    public ConnectionRow(
            @JsonProperty("userIdentifier") final String userIdentifier,
            @JsonProperty("status") final String status,
            @JsonProperty("address") final String address,
            @JsonProperty("indicatorValues") final Collection<IndicatorValue> indicatorValues) {

        this.userIdentifier = userIdentifier;
        this.status = status;
        this.address = address;
        this.indicatorValues = (indicatorValues != null)
                ? Collections.unmodifiableCollection(indicatorValues)
                : Collections.emptyList();
    }

    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    public String getStatus() {
        return this.status;
    }

    public String getAddress() {
        return this.address;
    }

    public Collection<IndicatorValue> getIndicatorValues() {
        return this.indicatorValues;
    }

    @Override
    public String toString() {
        return "ConnectionInfo [userIdentifier=" + this.userIdentifier + ", status=" + this.status + ", address="
                + this.address
                + ", indicatorValues=" + this.indicatorValues + "]";
    }

    public static final class IndicatorValue {

        public final String type;
        public final Double value;

        public IndicatorValue(
                @JsonProperty("type") final String type,
                @JsonProperty("value") final Double value) {

            this.type = type;
            this.value = value;
        }

        public String getType() {
            return this.type;
        }

        public Double getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "IndicatorValue [type=" + this.type + ", value=" + this.value + "]";
        }
    }

}
