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
import org.eth.demo.sebserver.domain.rest.exam.ExamStatus;
import org.eth.demo.sebserver.service.admin.UserFacade;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestClientConnection {

    private final ExamDao examDao;

    public TestClientConnection(final ExamDao examDao) {
        this.examDao = examDao;
    }

    @RequestMapping(value = "/client-connect", method = RequestMethod.GET)
    public final Collection<Exam> test_connect(final Principal principal) {
        System.out.println("********************************* test_connect");

        // NOTE: after ClientConnectionAuthenticationFilter has successfully authenticated
        //       a client against an LMS, we should be able here to get the User instance
        //       created ad-hoc by the ClientConnectionAuthenticationFilter with all
        //       needed information (institution)
        final User client = UserFacade.extractFromPrincipal(principal);

        // TODO add also institution extracted from the User to the filter
        final Collection<Exam> runningExams = this.examDao.getAll(e -> e.status == ExamStatus.RUNNING);

        return runningExams;
    }

}
