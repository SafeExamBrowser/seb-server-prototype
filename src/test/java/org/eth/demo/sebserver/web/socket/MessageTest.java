/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eth.demo.sebserver.web.socket.Message.Type;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageTest {

    @Test
    public void testSerialization() {
        final Message msg = new Message(Type.CONNECT, 123, "content");

        try {
            final String writeValueAsString = new ObjectMapper().writeValueAsString(msg);
            assertEquals(
                    "{\"type\":\"CONNECT\",\"timestamp\":123,\"content\":\"content\"}",
                    writeValueAsString);
        } catch (final JsonProcessingException e) {
            fail("Exception: " + e.getMessage());
        }
    }

}
