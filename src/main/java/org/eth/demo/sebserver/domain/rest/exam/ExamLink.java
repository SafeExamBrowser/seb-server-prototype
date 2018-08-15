/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

public class ExamLink {

    public final String examId;
    public final String examName;
    public final String examDescription;

    public ExamLink(final Exam exam) {

        this.examId = String.valueOf(exam.id);
        this.examName = exam.name;
        this.examDescription = exam.name; // TODO add description to Exam
    }

    public String getExamId() {
        return this.examId;
    }

    public String getExamName() {
        return this.examName;
    }

    public String getExamDescription() {
        return this.examDescription;
    }
}
