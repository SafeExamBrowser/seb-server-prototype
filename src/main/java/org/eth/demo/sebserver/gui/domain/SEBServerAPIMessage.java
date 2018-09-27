/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SEBServerAPIMessage {

    public final String messageCode;
    public final String systemMessage;

    @JsonCreator
    public SEBServerAPIMessage(
            @JsonProperty(value = "messageCode", required = true) final String messageCode,
            @JsonProperty(value = "systemMessage", required = true) final String systemMessage) {

        this.messageCode = messageCode;
        this.systemMessage = systemMessage;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String getSystemMessage() {
        return this.systemMessage;
    }

    @Override
    public String toString() {
        return "APIMessage [messageCode=" + this.messageCode + ", systemMessage=" + this.systemMessage + "]";
    }

}
