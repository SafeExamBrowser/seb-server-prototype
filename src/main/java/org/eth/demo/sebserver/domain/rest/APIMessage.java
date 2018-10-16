/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

public interface APIMessage {

    public String getMessageCode();

    public String getMessage();

    public String getDetail();

    //@formatter:off
    enum ErrorMessages {
        UNEXPECTED_ERROR("1001", "Unexpected server-side error happened")
        ;

        public final String messageCode;
        public final String systemMessage;
        private ErrorMessages(final String messageCode, final String systemMessage) {
            this.messageCode = messageCode;
            this.systemMessage = systemMessage;
        }
        public APIMessage of(final String detail) {
            return new APIMessage() {
                @Override public String getMessageCode() { return messageCode; }
                @Override public String getMessage() { return systemMessage; }
                @Override public String getDetail() { return detail; }
            };
        }
    }

    enum ValidationMessages {
        SIMPLE_ENTITY_FIELD("2001", "Field validation error")
        ;

        public final String messageCode;
        public final String systemMessage;
        private ValidationMessages(final String messageCode, final String systemMessage) {
            this.messageCode = messageCode;
            this.systemMessage = systemMessage;
        }
        public APIMessage of(final String detail) {
            return new APIMessage() {
                @Override public String getMessageCode() { return messageCode; }
                @Override public String getMessage() { return systemMessage; }
                @Override public String getDetail() { return detail; }
            };
        }
    }

    //@formatter:on

    public static APIMessage unexpectedError(final Throwable t) {
        return ErrorMessages.UNEXPECTED_ERROR.of(t.getMessage());
    }

}
