/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class TypedMap {

    private final Map<String, Object> mapping = new LinkedHashMap<>();

    public <T> TypedMap put(final TypedKey<T> key, final T value) {
        this.mapping.put(key.name, value);
        return this;
    }

    public <T> T get(final TypedKey<T> key) {
        if (!this.mapping.containsKey(key.name)) {
            return null;
        }

        return key.type.cast(this.mapping.get(key.name));
    }

    @Override
    public String toString() {
        return "TypedMap [mapping=" + this.mapping + "]";
    }

    public static final class TypedKey<T> {
        private final String name;
        private final Class<T> type;

        public TypedKey(final String name, final Class<T> type) {
            super();
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public Class<T> getType() {
            return this.type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
            result = prime * result + ((this.type == null) ? 0 : this.type.getName().hashCode());
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
            final TypedKey<?> other = (TypedKey<?>) obj;
            if (this.name == null) {
                if (other.name != null)
                    return false;
            } else if (!this.name.equals(other.name))
                return false;
            if (this.type == null) {
                if (other.type != null)
                    return false;
            } else if (!this.type.getName().equals(other.type.getName()))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "TypedKey [name=" + this.name + ", type=" + this.type + "]";
        }
    }

}
