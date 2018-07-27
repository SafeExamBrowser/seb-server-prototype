/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig.attribute;

public class Attribute {

    public final Long id;
    public final Long parentId;
    public final String name;
    public final AttributeType type;
    public final String resources;
    public final String dependencies;
    public final String defaultValue;

    public Attribute(
            final Long id,
            final Long parentId,
            final String name,
            final AttributeType type,
            final String resources,
            final String dependencies,
            final String defaultValue) {

        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.resources = resources;
        this.dependencies = dependencies;
        this.defaultValue = defaultValue;
    }

    public Long getId() {
        return this.id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public String getName() {
        return this.name;
    }

    public AttributeType getType() {
        return this.type;
    }

    public String getResources() {
        return this.resources;
    }

    public String getDependencies() {
        return this.dependencies;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        final Attribute other = (Attribute) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Attribute [id=" + this.id + ", parentId=" + this.parentId + ", name=" + this.name + ", type="
                + this.type + ", resources="
                + this.resources + ", dependencies=" + this.dependencies + ", defaultValue=" + this.defaultValue + "]";
    }

}
