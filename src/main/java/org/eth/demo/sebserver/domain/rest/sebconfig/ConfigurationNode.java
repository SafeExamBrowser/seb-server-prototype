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
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.GrantEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ConfigurationNode implements GrantEntity {

    public enum ConfigurationType {
        TEMPLATE,
        CLIENT,
        VIRTUAL_CLIENT
    }

    public final Long id;
    public final Long institutionId;
    public final String owner;
    public final String name;
    public final String description;
    public final ConfigurationType type;
    public final String templateName;
    public final Collection<ExamSEBConfigMapping> examMappings;
    public final List<Configuration> configurationHistory;

    @JsonCreator
    public ConfigurationNode(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("owner") final String owner,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("type") final ConfigurationType type,
            @JsonProperty("templateName") final String templateName,
            @JsonProperty("examMappings") final Collection<ExamSEBConfigMapping> examMappings,
            @JsonProperty("configurationHistory") final List<Configuration> configurationHistory) {

        this.id = id;
        this.institutionId = institutionId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.type = type;
        this.templateName = templateName;
        this.examMappings = (examMappings != null)
                ? Collections.unmodifiableCollection(examMappings)
                : Collections.emptyList();
        this.configurationHistory = (configurationHistory != null)
                ? Collections.unmodifiableList(configurationHistory)
                : Collections.emptyList();
    }

    @Override
    public GrantEntityType grantEntityType() {
        return GrantEntityType.SEB_CONFIG;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public Long getInstitutionId() {
        return this.institutionId;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLatestVersion() {
        if (this.configurationHistory != null && !this.configurationHistory.isEmpty()) {
            return this.configurationHistory.get(0).version;
        } else {
            return "--";
        }
    }

    public ConfigurationType getType() {
        return this.type;
    }

    public String getTemplateName() {
        return this.templateName;
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
        return "ConfigurationNode [id=" + this.id + ", institutionId=" + this.institutionId + ", owner=" + this.owner
                + ", name="
                + this.name + ", description=" + this.description + ", type=" + this.type + ", templateName="
                + this.templateName
                + ", examMappings=" + this.examMappings + ", configurationHistory=" + this.configurationHistory + "]";
    }

}
