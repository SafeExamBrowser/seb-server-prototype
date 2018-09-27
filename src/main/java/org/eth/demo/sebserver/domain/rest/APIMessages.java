/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

public interface APIMessages {

    public String getMessageCode();

    public String getSystemMessage();

    //@formatter:off
    enum ErrorMessages implements APIMessages {
        UNEXPECTED_ERROR("1001", "Unexpected server-side error happened")
        ;

        public final String messageCode;
        public final String systemMessage;
        private ErrorMessages(final String messageCode, final String systemMessage) {
            this.messageCode = messageCode;
            this.systemMessage = systemMessage;
        }
        @Override public String getMessageCode() { return messageCode; }
        @Override public String getSystemMessage() { return systemMessage; }
    }

    enum ValidationMessages implements APIMessages {
        ;

        public final String messageCode;
        public final String systemMessage;
        private ValidationMessages(final String messageCode, final String systemMessage) {
            this.messageCode = messageCode;
            this.systemMessage = systemMessage;
        }
        @Override public String getMessageCode() { return messageCode; }
        @Override public String getSystemMessage() { return systemMessage; }
    }

    //@formatter:on

}
