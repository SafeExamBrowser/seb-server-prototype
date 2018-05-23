/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GUIExam {

    public final Long id;
    public final String name;
    public final Integer status;
    public final String statusName;
    private final Collection<GUIIndicatorDef> indicators;

    @JsonCreator
    public GUIExam(@JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final Integer status,
            @JsonProperty("statusName") final String statusName,
            @JsonProperty("indicators") final Collection<GUIIndicatorDef> indicators) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.statusName = statusName;
        this.indicators = (indicators != null) ? new ArrayList<>(indicators) : new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Collection<GUIIndicatorDef> getIndicators() {
        return this.indicators;
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
        final GUIExam other = (GUIExam) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GUIExam [id=" + this.id
                + ", name=" + this.name
                + ", status=" + this.status
                + ", statusName=" + this.statusName
                + ", indicators=" + this.indicators + "]";
    }

}
