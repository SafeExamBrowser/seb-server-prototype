/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.exam;

import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamTableRow {

    public final String id;
    public final String owner;
    public final String name;
    public final String status;
    public final DateTime startTime;

    @JsonCreator
    public ExamTableRow(
            @JsonProperty("id") final String id,
            @JsonProperty("owner") final String owner,
            @JsonProperty("name") final String name,
            @JsonProperty("status") final String status,
            @JsonProperty("startTime") final DateTime startTime) {

        this.id = id;
        this.owner = owner;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
    }

    public String getId() {
        return this.id;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public ExamStatus getExamStatus() {
        return ExamStatus.valueOf(this.status);
    }

    public DateTime getStartTime() {
        return this.startTime;
    }

    /** Use this to check if the current logged in user is the owner of the Exam represented by this ExamTableRow. NOTE:
     * This extracts the current user from the RWT's UISession and therefore is only working in a context where a valid
     * RWT UISession is available
     *
     * @return */
    public boolean isOwner(final AuthorizationContextHolder authorizationContextHolder) {
        return authorizationContextHolder
                .getAuthorizationContext()
                .getLoggedInUser().id.equals(this.owner);
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
        final ExamTableRow other = (ExamTableRow) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExamTableRow [id=" + this.id + ", owner=" + this.owner + ", name=" + this.name + ", status="
                + this.status
                + ", startTime=" + this.startTime + "]";
    }

}
