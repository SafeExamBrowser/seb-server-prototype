/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class GUIAttributeValue {

    public final Long configId;
    public final String attributeName;
    public final String fullyQualifiedAttributeName;
    public final Integer listIndex;
    public final String value;

    @JsonCreator
    public GUIAttributeValue(
            @JsonProperty("configId") final Long configId,
            @JsonProperty("attributeName") final String attributeName,
            @JsonProperty("fullyQualifiedAttributeName") final String fullyQualifiedAttributeName,
            @JsonProperty("listIndex") final Integer listIndex,
            @JsonProperty("value") final String value) {

        this.configId = configId;
        this.attributeName = attributeName;
        this.fullyQualifiedAttributeName = fullyQualifiedAttributeName;
        this.listIndex = listIndex;
        this.value = value;
    }

    public Long getConfigId() {
        return this.configId;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getFullyQualifiedAttributeName() {
        return this.fullyQualifiedAttributeName;
    }

    public int getListIndex() {
        return this.listIndex;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "SEBConfigValue [attributeName=" + this.attributeName + ", fullyQualifiedAttributeName="
                + this.fullyQualifiedAttributeName + ", listIndex=" + this.listIndex + ", value=" + this.value + "]";
    }

}
