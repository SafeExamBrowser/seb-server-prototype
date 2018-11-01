/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;

public class APIMessage {

    public enum ErrorMessages {
        UNEXPECTED_ERROR("1000", "Unexpected server-side error"),
        FIELD_VALIDATION_ERROR("1500", "Field validation error")

        ;

        public final String messageCode;
        public final String systemMessage;

        private ErrorMessages(final String messageCode, final String systemMessage) {
            this.messageCode = messageCode;
            this.systemMessage = systemMessage;
        }

        public APIMessage of(final String detail) {
            return new APIMessage(this.messageCode, this.systemMessage, detail);
        }

        public APIMessage of(final String detail, final String... attributes) {
            return new APIMessage(this.messageCode, this.systemMessage, detail, attributes);
        }

        public APIMessage of(final Throwable error) {
            return new APIMessage(this.messageCode, this.systemMessage, error.getMessage());
        }
    }

    public final String messageCode;
    public final String systemMessage;
    public final String details;
    public final String[] attributes;

    public APIMessage(
            final String messageCode,
            final String systemMessage,
            final String details,
            final String[] attributes) {

        this.messageCode = messageCode;
        this.systemMessage = systemMessage;
        this.details = details;
        this.attributes = attributes;
    }

    public APIMessage(final String messageCode, final String systemMessage, final String details) {
        this(messageCode, systemMessage, details, null);
    }

    public APIMessage(final String messageCode, final String systemMessage) {
        this(messageCode, systemMessage, null, null);
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String getSystemMessage() {
        return this.systemMessage;
    }

    public String getDetails() {
        return this.details;
    }

    public String[] getAttributes() {
        return this.attributes;
    }

    public static final APIMessage fieldValidationError(final FieldError error) {
        final String[] args = StringUtils.split(error.getDefaultMessage(), ":");
        return ErrorMessages.FIELD_VALIDATION_ERROR.of(error.toString(), args);
    }

}
