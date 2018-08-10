/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GUIIndicatorValue {

    public final String clientIdentifier;
    public final String type;
    public final Double value;

    public GUIIndicatorValue(
            @JsonProperty("clientIdentifier") final String clientIdentifier,
            @JsonProperty("type") final String type,
            @JsonProperty("value") final Double value) {

        this.clientIdentifier = clientIdentifier;
        this.type = type;
        this.value = value;
    }

    public String getClientIdentifier() {
        return this.clientIdentifier;
    }

    public String getType() {
        return this.type;
    }

    public Double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "GUIIndicatorValue [clientIdentifier=" + this.clientIdentifier + ", type=" + this.type + ", value="
                + this.value + "]";
    }

}
