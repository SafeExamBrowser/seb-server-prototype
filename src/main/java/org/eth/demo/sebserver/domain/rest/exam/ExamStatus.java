/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

public enum ExamStatus {
    NONE(-1, "NO STATUS"), IN_PROGRESS(0, "In Progress"), READY(1, "Ready To Run"), RUNNING(2,
            "Running"), FINISHED(3, "Finished");

    public final int id;
    public final String displayName;

    private ExamStatus(final int id, final String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static ExamStatus byId(final int id) {
        for (final ExamStatus examStatus : ExamStatus.values()) {
            if (examStatus.id == id) {
                return examStatus;
            }
        }

        return NONE;
    }

    public static String getDisplayName(final int id) {
        return byId(id).displayName;
    }
}