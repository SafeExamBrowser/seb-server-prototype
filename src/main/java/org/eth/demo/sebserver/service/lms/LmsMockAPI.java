/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.lms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.domain.rest.admin.LmsSetup;

public class LmsMockAPI implements LmsConnectionTemplate {

    private final LmsSetup setup;
    private final Collection<CourseData> couses;

    public LmsMockAPI(final LmsSetup setup) {
        this.setup = setup;
        this.couses = new ArrayList<>();
        this.couses.add(new CourseData(
                "Demo Exam 1", "Demo Exam 1", "Demo Exam",
                "2020-01-01 09:00:00",
                "2021-01-01 09:00:00",
                "mock"));
        this.couses.add(new CourseData(
                "Demo Exam 2", "Demo Exam 2", "Demo Exam",
                "2020-01-01 09:00:00",
                "2021-01-01 09:00:00",
                "mock"));
        this.couses.add(new CourseData(
                "Demo Exam 3", "Demo Exam 3", "Demo Exam",
                "2018-07-30 09:00:00",
                "2018-08-01 00:00:00",
                "mock"));
        this.couses.add(new CourseData(
                "Demo Exam 4", "Demo Exam 4", "Demo Exam",
                "2018-01-01 00:00:00", "2020-01-01 00:00:00",
                "mock"));
    }

    @Override
    public Collection<String> courseNames() {
        return this.couses.stream()
                .map(cd -> cd.name)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CourseData> courses() {
        return Collections.unmodifiableCollection(this.couses);
    }

    @Override
    public CourseData course(final String uuid) {
        return this.couses.stream()
                .filter(cd -> cd.uuid.equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Course data with uuid: " + uuid + " not found"));
    }

    @Override
    public LmsSetup lmsSetup() {
        return this.setup;
    }

}
