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

import org.apache.commons.lang3.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionRow {

    public final String username;
    public final String status;
    public final boolean vdi;
    public final String clientAddress;
    public final String virtualClientAddress;
    public final Collection<IndicatorValue> indicatorValues;

    @JsonCreator
    public ConnectionRow(
            @JsonProperty("username") final String username,
            @JsonProperty("status") final String status,
            @JsonProperty("vdi") final Boolean vdi,
            @JsonProperty("clientAddress") final String clientAddress,
            @JsonProperty("virtualClientAddress") final String virtualClientAddress,
            @JsonProperty("indicatorValues") final Collection<IndicatorValue> indicatorValues) {

        this.username = username;
        this.status = status;
        this.vdi = BooleanUtils.isTrue(vdi);
        this.clientAddress = clientAddress;
        this.virtualClientAddress = virtualClientAddress;
        this.indicatorValues = (indicatorValues != null)
                ? Collections.unmodifiableCollection(indicatorValues)
                : Collections.emptyList();
    }

    public String getStatus() {
        return this.status;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isVdi() {
        return this.vdi;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public String getVirtualClientAddress() {
        return this.virtualClientAddress;
    }

    public Collection<IndicatorValue> getIndicatorValues() {
        return this.indicatorValues;
    }

    @Override
    public String toString() {
        return "ConnectionRow [username=" + this.username + ", status=" + this.status + ", vdi=" + this.vdi
                + ", clientAddress="
                + this.clientAddress + ", virtualClientAddress=" + this.virtualClientAddress + ", indicatorValues="
                + this.indicatorValues + "]";
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
