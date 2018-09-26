/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import java.util.concurrent.Executor;

import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.eth.demo.sebserver.service.exam.run.EventHandlingStrategy;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ServiceSpringConfig implements AsyncConfigurer {

    @Autowired
    private ExamDao examDao;
    @Autowired
    private ClientConnectionRecordMapper clientConnectionRecordMapper;
    @Autowired
    private ExamConnectionService examConnectionService;

    @Value("${sebserver.indicator.caching}")
    private boolean indicatorValueCachingEnabled;
    @Value("${sebserver.client.event-handling-strategy}")
    private String eventHandlingStrategyBeanName;

    @Lazy
    @Bean
    public ExamSessionService examSessionService(final ApplicationContext applicationContext) {
        final EventHandlingStrategy eventHandlingStrategy = applicationContext
                .getBean(this.eventHandlingStrategyBeanName, EventHandlingStrategy.class);

        eventHandlingStrategy.enable(true);
        return new ExamSessionService(
                this.examConnectionService,
                this.examDao,
                this.clientConnectionRecordMapper,
                eventHandlingStrategy);
    }

    @Override
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
