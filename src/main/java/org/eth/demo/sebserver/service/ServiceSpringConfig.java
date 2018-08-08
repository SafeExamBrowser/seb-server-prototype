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
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.eth.demo.sebserver.service.exam.run.ClientConnectionFactory;
import org.eth.demo.sebserver.service.exam.run.ClientConnectionService;
import org.eth.demo.sebserver.service.exam.run.EventHandlingStrategy;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.eth.demo.sebserver.service.exam.run.NewExamSessionService;
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
    private ClientConnectionService clientConnectionService;
    @Autowired
    private ExamStateService examStateService;
    @Autowired
    private ClientConnectionFactory clientConnectionFactory;
    @Autowired
    private ClientConnectionRecordMapper clientConnectionRecordMapper;

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
                this.examStateService,
                this.clientConnectionService,
                eventHandlingStrategy,
                this.indicatorValueCachingEnabled);
    }

    @Lazy
    @Bean
    public NewExamSessionService newExamSessionService(final ApplicationContext applicationContext) {
        final EventHandlingStrategy eventHandlingStrategy = applicationContext
                .getBean(this.eventHandlingStrategyBeanName, EventHandlingStrategy.class);

        eventHandlingStrategy.enable(true);
        return new NewExamSessionService(
                this.examStateService,
                this.clientConnectionFactory,
                this.clientConnectionRecordMapper,
                eventHandlingStrategy,
                this.indicatorValueCachingEnabled);
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
