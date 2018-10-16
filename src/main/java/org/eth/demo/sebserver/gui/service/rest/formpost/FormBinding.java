/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.formpost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class FormBinding {

    protected final ObjectMapper mapper;
    protected final ObjectNode objectRoot;

    protected FormBinding() {
        this.mapper = new ObjectMapper();
        this.objectRoot = this.mapper.createObjectNode();
    }

    public String getJson() throws Exception {
        flush();
        return this.mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this.objectRoot);
    }

    public abstract void flush() throws Exception;

}
