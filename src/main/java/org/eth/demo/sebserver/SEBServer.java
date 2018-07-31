/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SpringBootApplication
public class SEBServer {

    public static void main(final String[] args) {
        org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
        // first of all register the JodaModule to Jackson
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        SpringApplication.run(SEBServer.class, args);
    }

}
