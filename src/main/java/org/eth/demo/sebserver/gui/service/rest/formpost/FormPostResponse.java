/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.formpost;

import java.util.Collection;
import java.util.Collections;

import org.eth.demo.sebserver.gui.service.rest.validation.FieldValidationError;

public class FormPostResponse {

    public final String objectId;
    public final Collection<FieldValidationError> validationErrors;

    public FormPostResponse(final String objectId) {
        this(objectId, Collections.emptyList());
    }

    public FormPostResponse(final String objectId, final Collection<FieldValidationError> validationErrors) {
        this.objectId = objectId;
        this.validationErrors = validationErrors;
    }

}
