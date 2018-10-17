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

public class FormPostResponse<T> {

    public final T response;
    public final Collection<FieldValidationError> validationErrors;

    public FormPostResponse(final T response) {
        this(response, Collections.emptyList());
    }

    public FormPostResponse(final T response, final Collection<FieldValidationError> validationErrors) {
        this.response = response;
        this.validationErrors = validationErrors;
    }

}
