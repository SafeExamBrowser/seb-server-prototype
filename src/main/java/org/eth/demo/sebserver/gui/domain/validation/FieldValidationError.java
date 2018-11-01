/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldValidationError {

    public final String messageCode;
    public final String domainName;
    public final String fieldName;
    public final String errorType;
    public final String[] attributes;

    @JsonCreator
    public FieldValidationError(
            @JsonProperty("messageCode") final String messageCode,
            @JsonProperty("attributes") final String[] attributes) {

        this.messageCode = messageCode;
        this.attributes = attributes;

        this.domainName = (attributes != null && attributes.length > 0) ? attributes[0] : null;
        this.fieldName = (attributes != null && attributes.length > 1) ? attributes[1] : null;
        this.errorType = (attributes != null && attributes.length > 2) ? attributes[2] : null;
    }

}
