/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import java.util.Collection;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.exam.Exam;

public interface ExamDao {

    // TODO It should also be possible to use a Join here...
    // NOTE Joins are a bit more complicated to implement and cannot be generated directly.
    //      A join example is implements within getAll using ExamIndicatorJoinMapper
    //      We should decide in case if it makes sense to use join. usually if a there
    //      are a lot of rows to fetch we better implement and use a join to perform better.
    Exam byId(Long id);

    Collection<Exam> getAll();

    Collection<Exam> getAll(Predicate<Exam> predicate);

    Exam save(Exam model);

    boolean delete(Long id);

}