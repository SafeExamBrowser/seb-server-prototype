/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.validation;

public class FieldValidationError {

    public final String errorCode;
    public final String message;
    public final String fieldName;
    public final String detail;

    public FieldValidationError(
            final String errorCode,
            final String message,
            final String fieldName,
            final String detail) {

        this.errorCode = errorCode;
        this.message = message;
        this.fieldName = fieldName;
        this.detail = detail;
    }

}
