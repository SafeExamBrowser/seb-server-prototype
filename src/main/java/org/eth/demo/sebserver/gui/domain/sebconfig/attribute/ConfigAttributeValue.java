/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig.attribute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ConfigAttributeValue {

    public final String configId;
    public final String attributeName;
    public final String parentAttributeName;
    public final Integer listIndex;
    public final String value;

    @JsonCreator
    public ConfigAttributeValue(
            @JsonProperty("configId") final String configId,
            @JsonProperty("attributeName") final String attributeName,
            @JsonProperty("parentAttributeName") final String parentAttributeName,
            @JsonProperty("listIndex") final Integer listIndex,
            @JsonProperty("value") final String value) {

        this.configId = configId;
        this.attributeName = attributeName;
        this.parentAttributeName = parentAttributeName;
        this.listIndex = listIndex;
        this.value = value;
    }

    public String getConfigId() {
        return this.configId;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getParentAttributeName() {
        return this.parentAttributeName;
    }

    public int getListIndex() {
        return this.listIndex;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.attributeName == null) ? 0 : this.attributeName.hashCode());
        result = prime * result + ((this.configId == null) ? 0 : this.configId.hashCode());
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
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
        final ConfigAttributeValue other = (ConfigAttributeValue) obj;
        if (this.attributeName == null) {
            if (other.attributeName != null)
                return false;
        } else if (!this.attributeName.equals(other.attributeName))
            return false;
        if (this.configId == null) {
            if (other.configId != null)
                return false;
        } else if (!this.configId.equals(other.configId))
            return false;
        if (this.value == null) {
            if (other.value != null)
                return false;
        } else if (!this.value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConfigAttributeValue [attributeName=" + this.attributeName + ", parentAttributeName="
                + this.parentAttributeName + ", listIndex=" + this.listIndex + ", value=" + this.value + "]";
    }

}
