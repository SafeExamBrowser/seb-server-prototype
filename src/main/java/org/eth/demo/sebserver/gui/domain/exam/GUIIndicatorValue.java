/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GUIIndicatorValue {

    public final UUID clientUUID;
    public final String type;
    public final Float value;

    public GUIIndicatorValue(
            @JsonProperty("clientUUID") final UUID clientUUID,
            @JsonProperty("type") final String type,
            @JsonProperty("value") final Float value) {

        this.clientUUID = clientUUID;
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public Float getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.clientUUID == null) ? 0 : this.clientUUID.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GUIIndicatorValue other = (GUIIndicatorValue) obj;
        if (this.clientUUID == null) {
            if (other.clientUUID != null)
                return false;
        } else if (!this.clientUUID.equals(other.clientUUID))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        } else if (!this.type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GUIIndicatorValue [clientUUID=" + this.clientUUID
                + ", type=" + this.type
                + ", value=" + this.value + "]";
    }

}
