/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig.attribute;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ComplexAttribute extends AbstractConfigAttribute {

    public final String parentAttributeName;
    public final List<Map<String, ConfigAttribute>> values;

    public ComplexAttribute(
            final String name,
            final String parentAttributeName,
            final AttributeType type,
            final List<Map<String, ConfigAttribute>> values) {

        super(name, type);
        this.parentAttributeName = parentAttributeName;
        this.values = Collections.unmodifiableList(values);
    }

    public <T> T getValueAs(final String name, final int index, final Class<T> type) {
        return this.values.get(index).get(name).getValueAs(type);
    }

    @Override
    public <T> T getValueAs(final Class<T> type) {
        return type.cast(this.values);
    }

}
