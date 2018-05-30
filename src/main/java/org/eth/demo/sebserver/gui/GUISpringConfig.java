/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eth.demo.sebserver.gui.push.ServerPushService;
import org.eth.demo.sebserver.gui.view.ViewService;
import org.eth.demo.sebserver.gui.views.ExamOverview;
import org.eth.demo.sebserver.gui.views.RunningExamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GUISpringConfig {

    public static final String ROOT_LOCATION = "http://localhost:8080";

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Lazy
    @Bean
    public ViewService viewService(final ApplicationContext context) {
        return new ViewService(context);
    }

    @Lazy
    @Bean
    public ExamOverview examOverview() {
        return new ExamOverview(this.restTemplate);
    }

    @Lazy
    @Bean
    public RunningExamView runningExamView() {
        return new RunningExamView(
                serverPushService(),
                this.restTemplate);
    }

    @Lazy
    @Bean
    public ServerPushService serverPushService() {
        return new ServerPushService();
    }

}
