/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig.attribute;

public final class AttributeOfView {

    public final String name;
    public final AttributeType type;
    public final String parentAttributeName;
    public final String resources;
    public final String dependencies;

    public final String view;
    public final String group;
    public final int xpos;
    public final int ypos;
    public final int width;
    public final int height;

    public AttributeOfView(final String name,
            final AttributeType type,
            final String parentAttributeName,
            final String resources,
            final String dependencies,
            final String view,
            final String group,
            final int xpos,
            final int ypos,
            final int width,
            final int height) {

        this.name = name;
        this.type = type;
        this.parentAttributeName = parentAttributeName;
        this.resources = resources;
        this.dependencies = dependencies;
        this.view = view;
        this.group = group;
        this.xpos = xpos;
        this.ypos = ypos;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public AttributeType getType() {
        return this.type;
    }

    public String getParentAttributeName() {
        return this.parentAttributeName;
    }

    public String getResources() {
        return this.resources;
    }

    public String getView() {
        return this.view;
    }

    public String getGroup() {
        return this.group;
    }

    public int getXpos() {
        return this.xpos;
    }

    public int getYpos() {
        return this.ypos;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AttributeOfView other = (AttributeOfView) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (this.parentAttributeName == null) {
            if (other.parentAttributeName != null)
                return false;
        } else if (!this.parentAttributeName.equals(other.parentAttributeName))
            return false;
        if (this.type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AttributeOfView [name=" + this.name + ", type=" + this.type + ", parentAttributeName="
                + this.parentAttributeName
                + ", resources=" + this.resources + ", dependencies=" + this.dependencies + ", view=" + this.view
                + ", group=" + this.group
                + ", xpos=" + this.xpos + ", ypos=" + this.ypos + ", width=" + this.width + ", height=" + this.height
                + "]";
    }

}
