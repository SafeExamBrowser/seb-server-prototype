/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.push.ServerPushService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class GUISpringConfig {

    public static final String ROOT_LOCATION = "http://localhost:8080";

    @Lazy
    @Bean
    public ViewService viewService(final ApplicationContext context) {
        return new ViewService(context);
    }

    @Lazy
    @Bean
    public ServerPushService serverPushService() {
        return new ServerPushService();
    }

}
