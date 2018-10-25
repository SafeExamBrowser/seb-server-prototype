/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.lms;

import org.eth.demo.sebserver.domain.rest.exam.Exam.ExamStatus;
import org.eth.demo.util.Const;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public final class CourseData {

    public final String uuid; // mandatory
    public final String name; // optional
    public final String description; // optional
    public final DateTime startTime; // mandatory
    public final DateTime endTime; // mandatory
    public final String enrollmentURL; // mandatory

    public CourseData(
            final String uuid,
            final String name,
            final String description,
            final DateTime startTime,
            final DateTime endTime,
            final String enrollmentURL) {

        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrollmentURL = enrollmentURL;
    }

    public CourseData(
            final String uuid,
            final String name,
            final String description,
            final String startTime,
            final String endTime,
            final String enrollmentURL) {

        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime
                .parse(startTime, Const.DATE_TIME_PATTERN_UTC_NO_MILLIS)
                .toDateTime(DateTimeZone.UTC);
        this.endTime = LocalDateTime
                .parse(endTime, Const.DATE_TIME_PATTERN_UTC_NO_MILLIS)
                .toDateTime(DateTimeZone.UTC);
        this.enrollmentURL = enrollmentURL;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public DateTime getStartTime() {
        return this.startTime;
    }

    public DateTime getEndTime() {
        return this.endTime;
    }

    public String getEnrollmentURL() {
        return this.enrollmentURL;
    }

    public ExamStatus getStatus() {
        if (this.startTime.isAfterNow()) {
            return ExamStatus.READY;
        } else if (this.startTime.isBeforeNow() && this.endTime.isAfterNow()) {
            return ExamStatus.RUNNING;
        } else {
            return ExamStatus.FINISHED;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
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
        final CourseData other = (CourseData) obj;
        if (this.uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!this.uuid.equals(other.uuid))
            return false;
        return true;
    }

}
