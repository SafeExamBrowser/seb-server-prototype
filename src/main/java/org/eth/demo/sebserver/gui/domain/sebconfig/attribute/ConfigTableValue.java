/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig.attribute;

import java.util.Collections;
import java.util.List;

public class ConfigTableValue {

    public final String configId;
    public final String attributeName;
    public final List<String> columns;
    public final List<String> values;

    public ConfigTableValue(
            final String configId,
            final String attributeName,
            final List<String> columns,
            final List<String> values) {

        this.configId = configId;
        this.attributeName = attributeName;
        this.columns = Collections.unmodifiableList(columns);
        this.values = Collections.unmodifiableList(values);
    }

    public String getConfigId() {
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
        return "ConfigTableValue [configId=" + this.configId + ", attributeName=" + this.attributeName + ", columns="
                + this.columns
                + ", values=" + this.values + "]";
    }

}
