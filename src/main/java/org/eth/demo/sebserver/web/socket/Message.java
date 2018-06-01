/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    public enum Type {
        CONNECT, UPDATE_CONFIG, ERROR
    }

    public final Type type;
    public final String content;

    public Message(@JsonProperty("type") final Type type, @JsonProperty("content") final String content) {
        super();
        this.type = type;
        this.content = content;
    }

}
