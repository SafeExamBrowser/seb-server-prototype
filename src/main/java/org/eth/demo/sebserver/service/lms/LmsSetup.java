/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.lms;

import org.eth.demo.sebserver.domain.rest.admin.SebLmsSetup.LMSType;

public interface LmsSetup {

    Long getId();

    Long getInstitutionId();

    LMSType getLmsType();

    String getLmsAuthName();

    String getLmsAuthSecret();

    String getLmsApiUrl();

}
