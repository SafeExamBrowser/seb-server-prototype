/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GUIIndicatorDef {

    public final String type;
    public final Float threshold1;
    public final Float threshold2;
    public final Float threshold3;

    @JsonCreator
    public GUIIndicatorDef(@JsonProperty("type") final String type,
            @JsonProperty("threshold1") final Float threshold1,
            @JsonProperty("threshold2") final Float threshold2,
            @JsonProperty("threshold3") final Float threshold3) {

        this.type = type;
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
        this.threshold3 = threshold3;
    }

    @Override
    public String toString() {
        return "Indicator [type=" + this.type
                + ", threshold1=" + this.threshold1
                + ", threshold2=" + this.threshold2
                + ", threshold3=" + this.threshold3 + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        final IndicatorDefinition other = (IndicatorDefinition) obj;
        if (this.type == null) {
            if (other.type != null)
                return false;
        } else if (!this.type.equals(other.type))
            return false;
        return true;
    }

}
