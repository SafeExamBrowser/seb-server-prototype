/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunningExam {

    public final Long id;
    public final String name;
    public final Integer status;
    public final String statusName;
    private final List<String> indicatorTypeList;
    private final List<Indicator> indicatorList;
    private final Map<String, Indicator> indicators;

    @JsonCreator
    public RunningExam(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final Integer status,
            @JsonProperty("statusName") final String statusName,
            @JsonProperty("indicators") final Collection<Indicator> indicatorList) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.statusName = statusName;

        this.indicatorTypeList = new ArrayList<>();
        this.indicatorList = new ArrayList<>();
        this.indicators = new HashMap<>();
        if (indicatorList != null) {
            indicatorList.stream()
                    .forEach(i -> {
                        this.indicatorTypeList.add(i.type);
                        this.indicatorList.add(i);
                        this.indicators.put(i.type, i);
                    });
        }
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

    public Collection<Indicator> getIndicators() {
        return this.indicatorList;
    }

    public Indicator getIndicator(final String type) {
        return this.indicators.get(type);
    }

    public int getIndicatorIndex(final String indicatorType) {
        return this.indicatorTypeList.indexOf(indicatorType);
    }

    public Indicator getIndicator(final int indicatorIndex) {
        return this.indicatorList.get(indicatorIndex);
    }

    public int getNumberOfIndicators() {
        return this.indicatorTypeList.size();
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
        final RunningExam other = (RunningExam) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExamInfo [id=" + this.id
                + ", name=" + this.name
                + ", status=" + this.status
                + ", statusName=" + this.statusName
                + ", indicators=" + this.indicators + "]";
    }

}
