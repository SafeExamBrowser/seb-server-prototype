/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.lms;

import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.admin.LmsSetup;

public interface LmsConnectionTemplate {

    LmsSetup lmsSetup();

    Collection<String> courseNames();

    Collection<CourseData> courses();

    CourseData course(String uuid);

}
