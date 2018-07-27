/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    public final Long id;
    public final Long nodeId;
    public final String version;
    public final DateTime versionDate;
    public final Boolean followup;

    @JsonCreator
    public Configuration(
            @JsonProperty("id") final Long id,
            @JsonProperty("nodeId") final Long nodeId,
            @JsonProperty("version") final String version,
            @JsonProperty("versionDate") final DateTime versionDate,
            @JsonProperty("followup") final Boolean followup) {

        this.id = id;
        this.nodeId = nodeId;
        this.version = version;
        this.versionDate = versionDate;
        this.followup = followup;
    }

    public Long getId() {
        return this.id;
    }

    public Long getNodeId() {
        return this.nodeId;
    }

    public String getVersion() {
        return this.version;
    }

    public DateTime getVersionDate() {
        return this.versionDate;
    }

    public Boolean getFollowup() {
        return this.followup;
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
        final Configuration other = (Configuration) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Configuration [id=" + this.id + ", nodeId=" + this.nodeId + ", version=" + this.version
                + ", versionDate="
                + this.versionDate + ", followup=" + this.followup + "]";
    }

}
