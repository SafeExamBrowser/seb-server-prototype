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

public final class ListAttribute extends AbstractConfigAttribute {

    public final List<String> values;

    public ListAttribute(final String name, final AttributeType type, final List<String> values) {
        super(name, type);
        this.values = (values != null) ? Collections.unmodifiableList(values) : Collections.emptyList();
    }

    @Override
    public <T> T getValueAs(final Class<T> type) {
        return type.cast(this.values);
    }

}
