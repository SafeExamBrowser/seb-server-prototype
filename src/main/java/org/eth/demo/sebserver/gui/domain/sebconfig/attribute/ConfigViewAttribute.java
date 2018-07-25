/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig.attribute;

import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigViewAttribute {

    private static final Logger log = LoggerFactory.getLogger(ConfigViewAttribute.class);

    public final String name;
    public final String type;
    public final String parentAttributeName;
    public final String resources;
    public final String dependencies;

    public final String view;
    public final String group;
    public final int xpos;
    public final int ypos;

    @JsonCreator
    public ConfigViewAttribute(
            @JsonProperty("name") final String name,
            @JsonProperty("type") final String type,
            @JsonProperty("parentAttributeName") final String parentAttributeName,
            @JsonProperty("resources") final String resources,
            @JsonProperty("dependencies") final String dependencies,
            @JsonProperty("view") final String view,
            @JsonProperty("group") final String group,
            @JsonProperty("xpos") final int xpos,
            @JsonProperty("ypos") final int ypos) {

        this.name = name;
        this.type = type;
        this.parentAttributeName = parentAttributeName;
        this.resources = resources;
        this.dependencies = dependencies;
        this.view = view;
        this.group = group;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
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

    public FieldType getFieldType() {
        try {
            return FieldType.valueOf(this.type);
        } catch (final Exception e) {
            log.error("Unknown FieldType: {}", this.type);
            return FieldType.UNKNOWN;
        }
    }

    // TODO we should support default values from back-end (DB)
    public String getDefaultValue() {
        final FieldType fieldType = getFieldType();
        switch (fieldType) {
            case CHECKBOX:
            case CHECK_FIELD: {
                return String.valueOf(Boolean.FALSE);
            }
            case DECIMAL:
            case INTEGER: {
                return "";
            }
            case TEXT_AREA:
            case TEXT_FIELD: {
                return "--";
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.parentAttributeName == null) ? 0 : this.parentAttributeName.hashCode());
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
        final ConfigViewAttribute other = (ConfigViewAttribute) obj;
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
        return true;
    }

    @Override
    public String toString() {
        return "ConfigViewAttribute [name=" + this.name + ", type=" + this.type + ", parentAttributeName="
                + this.parentAttributeName
                + ", resources=" + this.resources + ", dependencies=" + this.dependencies + ", view=" + this.view
                + ", group=" + this.group
                + ", xpos=" + this.xpos + ", ypos=" + this.ypos + "]";
    }

}
