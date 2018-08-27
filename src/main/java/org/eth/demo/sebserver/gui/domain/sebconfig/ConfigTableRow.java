/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigTableRow {

    public final Long id;
    public final Long institutionId;
    public final Long ownerId;
    public final String name;
    public final String type;
    public final String latestVersion;

    @JsonCreator
    public ConfigTableRow(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("ownerId") final Long ownerId,
            @JsonProperty("name") final String name,
            @JsonProperty("type") final String type,
            @JsonProperty("latestVersion") final String latestVersion) {

        this.id = id;
        this.institutionId = institutionId;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.latestVersion = latestVersion;
    }

    public Long getId() {
        return this.id;
    }

    public Long getInstitutionId() {
        return this.institutionId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getLatestVersion() {
        return this.latestVersion;
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
        final ConfigTableRow other = (ConfigTableRow) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConfigTableRow [id=" + this.id + ", institutionId=" + this.institutionId + ", ownerId=" + this.ownerId
                + ", name="
                + this.name + ", type=" + this.type + ", latestVersion=" + this.latestVersion + "]";
    }

}
