/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig.attribute;

public abstract class AbstractConfigAttribute implements ConfigAttribute {

    public final String name;
    public final AttributeType type;
    public final Orientation orientation;

    public AbstractConfigAttribute(final String name, final AttributeType type) {
        this(name, type, null);
    }

    public AbstractConfigAttribute(final String name, final AttributeType type, final Orientation orientation) {
        this.name = name;
        this.type = type;
        this.orientation = orientation;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AttributeType getType() {
        return this.type;
    }

    @Override
    public Orientation getOrientation() {
        return this.orientation;
    }

}
