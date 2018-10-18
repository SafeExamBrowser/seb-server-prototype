/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.institution;

import org.eth.demo.sebserver.gui.service.rest.GetNames;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class GetInstitutionNames extends GetNames {

    public GetInstitutionNames(final RestCallBuilder restCallBuilder) {
        super(restCallBuilder, "institution/names");
    }

}
