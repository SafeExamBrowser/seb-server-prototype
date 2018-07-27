/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ConfigurationNode {

    public final Long id;
    public final Long ownerId;
    public final String name;
    public final ConfigurationType type;
    public final Collection<ExamSEBConfigMapping> examMappings;
    public final List<Configuration> configurationHistory;

    @JsonCreator
    public ConfigurationNode(
            @JsonProperty("id") final Long id,
            @JsonProperty("ownerId") final Long ownerId,
            @JsonProperty("name") final String name,
            @JsonProperty("type") final ConfigurationType type,
            @JsonProperty("examMappings") final Collection<ExamSEBConfigMapping> examMappings,
            @JsonProperty("configurationHistory") final List<Configuration> configurationHistory) {

        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.examMappings = (examMappings != null)
                ? Collections.unmodifiableCollection(examMappings)
                : Collections.emptyList();
        this.configurationHistory = (configurationHistory != null)
                ? Collections.unmodifiableList(configurationHistory)
                : Collections.emptyList();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public ConfigurationType getType() {
        return this.type;
    }

    public Collection<ExamSEBConfigMapping> getExamMappings() {
        return this.examMappings;
    }

    public List<Configuration> getConfigurationHistory() {
        return this.configurationHistory;
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
        final ConfigurationNode other = (ConfigurationNode) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConfigurationNode [id=" + this.id + ", ownerId=" + this.ownerId + ", name=" + this.name + ", type="
                + this.type
                + ", examMappings=" + this.examMappings + ", configurationHistory=" + this.configurationHistory + "]";
    }

}
