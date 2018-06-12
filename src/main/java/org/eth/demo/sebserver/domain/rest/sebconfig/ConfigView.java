/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

import java.util.ArrayList;
import java.util.List;

public final class ConfigView {

    public final String name;
    public final List<ViewAttribute> attributes;
    public final List<AttributeValue> values;

    private ConfigView(final String name, final List<ViewAttribute> attributes, final List<AttributeValue> values) {
        this.name = name;
        this.attributes = attributes;
        this.values = values;
    }

    public String getName() {
        return this.name;
    }

    public List<ViewAttribute> getAttributes() {
        return this.attributes;
    }

    public List<AttributeValue> getValues() {
        return this.values;
    }

    public static final Builder create(final String name) {
        return new Builder(name);
    }

    public static final class Builder {

        private final ConfigView tmpView;

        private Builder(final String name) {
            this.tmpView = new ConfigView(name, new ArrayList<>(), new ArrayList<>());
        }

    }

}
