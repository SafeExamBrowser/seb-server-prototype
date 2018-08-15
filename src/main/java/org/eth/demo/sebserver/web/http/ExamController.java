/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.security.Principal;
import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.service.admin.UserFacade;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.eth.demo.sebserver.service.exam.UserPrivilegeExamFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamDao examDao;
    private final ExamStateService examStateService;

    public ExamController(final ExamDao examDao, final ExamStateService examStateService) {
        this.examDao = examDao;
        this.examStateService = examStateService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public final Collection<Exam> exams(final Principal principal) {
        return this.examDao.getAll(UserPrivilegeExamFilter.of(principal));

    }

    @RequestMapping(value = "/{examId}", method = RequestMethod.GET)
    public final Exam exam(final Principal principal, @PathVariable final Long examId) {
        final Exam exam = this.examDao.byId(examId);
        if (exam != null) {
            // TODO if there should be institutions (tenants), get current User and check institution
            @SuppressWarnings("unused")
            final User currentUser = UserFacade.extractFromPrincipal(principal);
            // currentUser.institution == exam.owner.institution
        }
        return exam;
    }

    @RequestMapping(value = "/delete/{examId}", method = RequestMethod.DELETE)
    public final void deleteExam(@PathVariable final Long examId) {
        this.examDao.delete(examId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public final Exam saveExam(@RequestBody final Exam exam) {
        return this.examDao.save(exam);
    }

    @RequestMapping(value = "/statechange/{examId}/{stateName}", method = RequestMethod.POST)
    final Exam processStateChange(@PathVariable final Long examId, @PathVariable final String stateName) {
        return this.examStateService.processStateChange(examId, Exam.ExamStatus.valueOf(stateName));
    }

}
