/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig.attribute;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableAttributeValue {

    public final Long configId;
    public final String attributeName;
    public final List<String> columns;
    public final List<String> values;

    @JsonCreator
    public TableAttributeValue(
            @JsonProperty("configId") final Long configId,
            @JsonProperty("attributeName") final String attributeName,
            @JsonProperty("columns") final List<String> columns,
            @JsonProperty("values") final List<String> values) {

        this.configId = configId;
        this.attributeName = attributeName;
        this.columns = Collections.unmodifiableList(columns);
        this.values = Collections.unmodifiableList(values);
    }

    public Long getConfigId() {
        return this.configId;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public List<String> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return "GUITableValue [configId=" + this.configId + ", attributeName=" + this.attributeName + ", columns="
                + this.columns
                + ", values=" + this.values + "]";
    }

}
