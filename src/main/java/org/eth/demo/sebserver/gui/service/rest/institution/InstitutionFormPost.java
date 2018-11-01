/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.institution;

import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormPOST;
import org.eth.demo.sebserver.service.JSONMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class InstitutionFormPost extends FormPOST<IdAndName> {

    public InstitutionFormPost(final RestCallBuilder restCallBuilder, final JSONMapper jsonMapper) {
        super(restCallBuilder, jsonMapper, "institution/save");
    }

    @Override
    protected Class<IdAndName> type() {
        return IdAndName.class;
    }
}
