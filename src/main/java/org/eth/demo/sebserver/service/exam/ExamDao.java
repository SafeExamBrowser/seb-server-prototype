/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.exam.Exam;

public interface ExamDao {

    Exam importExam(Long lmsSetupId, String externalUuid);

    Exam byId(Long id);

    Optional<Exam> runningExam(Long id);

    boolean isRunning(Long id);

    Collection<Exam> getAll();

    Collection<Exam> getAll(Predicate<Exam> predicate);

    boolean remove(Long id);

}