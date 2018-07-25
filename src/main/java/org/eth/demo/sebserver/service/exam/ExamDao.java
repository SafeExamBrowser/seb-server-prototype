/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam;

import java.util.Collection;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.exam.Exam;

public interface ExamDao {

    Exam byId(Long id);

    Collection<Exam> getAll();

    Collection<Exam> getAll(Predicate<Exam> predicate);

    Exam save(Exam model);

    boolean delete(Long id);

}