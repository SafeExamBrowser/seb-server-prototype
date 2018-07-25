/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ExamSEBConfigMapping {

    public final Long id;
    public final Long configurationId;
    public final String clientInfo;

    public ExamSEBConfigMapping(
            @JsonProperty("id") final Long id,
            @JsonProperty("configurationId") final Long configurationId,
            @JsonProperty("clientInfo") final String clientInfo) {

        this.id = id;
        this.configurationId = configurationId;
        this.clientInfo = clientInfo;
    }

    public Long getId() {
        return this.id;
    }

    public Long getConfigurationId() {
        return this.configurationId;
    }

    public String getClientInfo() {
        return this.clientInfo;
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
        final ExamSEBConfigMapping other = (ExamSEBConfigMapping) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExamSEBConfigMapping [id=" + this.id + ", configurationId=" + this.configurationId + ", clientInfo="
                + this.clientInfo
                + "]";
    }

}
