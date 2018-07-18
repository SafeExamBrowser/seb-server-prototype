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

@SpringBootApplication

public class SEBServer {

    public interface Constants {
        String CONTENT_TYPE_APPLICATION_JSON = "application/json";

        public static final String EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE = "SINGLE_EVENT_STORE_STRATEGY";
        public static final String EVENT_CONSUMER_STRATEGY_ASYNC_BATCH_STORE = "ASYNC_BATCH_STORE_STRATEGY";
    }

    public static void main(final String[] args) {
        SpringApplication.run(SEBServer.class, args);
    }

}
