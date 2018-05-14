/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.service.ExamSessionService;
import org.eth.demo.sebserver.service.dao.ExamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamDao examDao;
    @Autowired
    private ExamSessionService examSessionService;

    @RequestMapping(method = RequestMethod.GET)
    final Collection<Exam> exams() {
        return this.examDao.getAll();
    }

    @RequestMapping(value = "/{examId}", method = RequestMethod.GET)
    final Exam exam(@PathVariable final Long examId) {
        return this.examDao.byId(examId);
    }

    @RequestMapping(value = "/delete/{examId}", method = RequestMethod.POST)
    final void deleteExam(@PathVariable final Long examId) {
        this.examDao.delete(examId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    final Exam saveExam(@RequestBody final Exam exam) {
        return this.examDao.save(exam);
    }

    @RequestMapping(value = "/start/{examId}", method = RequestMethod.POST)
    final void startExam(@PathVariable final Long examId) {
        this.examSessionService.startExam(examId);
    }

    @RequestMapping(value = "/end/{examId}", method = RequestMethod.POST)
    final void endExam(@PathVariable final Long examId) {
        this.examSessionService.endExam(examId);
    }

}
