/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig;

import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GUIViewAttribute {

    private static final Logger log = LoggerFactory.getLogger(GUIViewAttribute.class);

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
    public GUIViewAttribute(
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

    @Override
    public String toString() {
        return "GUIViewAttribute [name=" + this.name + ", type=" + this.type + ", parentAttributeName="
                + this.parentAttributeName
                + ", resources=" + this.resources + ", dependencies=" + this.dependencies + ", view=" + this.view
                + ", group=" + this.group
                + ", xpos=" + this.xpos + ", ypos=" + this.ypos + "]";
    }

}
