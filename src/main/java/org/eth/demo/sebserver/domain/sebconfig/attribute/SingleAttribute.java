/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig.attribute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SingleAttribute extends AbstractConfigAttribute {

    public final String value;

    @JsonCreator
    public SingleAttribute(
            @JsonProperty("name") final String name,
            @JsonProperty("type") final AttributeType type,
            @JsonProperty("orientation") final Orientation orientation,
            @JsonProperty("value") final String value) {

        super(name, type, orientation);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public <T> T getValueAs(final Class<T> type) {
        if (this.value == null) {
            return null;
        }

        return type.cast(this.value);
    }
}
