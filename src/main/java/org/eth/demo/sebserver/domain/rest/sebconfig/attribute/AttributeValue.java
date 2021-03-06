/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig.attribute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class AttributeValue {

    public final Long configId;
    public final String attributeName;
    // TODO use parentId instead of name and remove the name resolving code in ConfigAttributeDaoImpl (adapt GUI)
    public final String parentAttributeName;
    public final Integer listIndex;
    public final String value;

    public AttributeValue(
            final Long configId,
            final String attributeName,
            final String value) {

        this(configId, attributeName, attributeName, 0, value);
    }

    @JsonCreator
    public AttributeValue(
            @JsonProperty("configId") final Long configId,
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

    public Long getConfigId() {
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
    public String toString() {
        return "SEBConfigValue [attributeName=" + this.attributeName + ", parentAttributeName="
                + this.parentAttributeName + ", listIndex=" + this.listIndex + ", value=" + this.value + "]";
    }

}
