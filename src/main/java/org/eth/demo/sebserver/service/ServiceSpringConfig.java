/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import java.util.concurrent.Executor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.eth.demo.sebserver.batis.ClientEventExtentionMapper;
import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.service.dao.ExamDao;
import org.eth.demo.sebserver.service.dao.ExamDaoImpl;
import org.eth.demo.sebserver.service.event.AsyncBatchEventSaveStrategy;
import org.eth.demo.sebserver.service.event.EventHandlingStrategy;
import org.eth.demo.sebserver.service.event.SingleEventStoreStrategy;
import org.eth.demo.sebserver.service.indicator.ClientConnectionFactory;
import org.eth.demo.sebserver.service.indicator.ClientIndicator;
import org.eth.demo.sebserver.service.indicator.ErrorCountIndicator;
import org.eth.demo.sebserver.service.indicator.PingIntervalIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ServiceSpringConfig implements AsyncConfigurer {

    public static final String PROPERTY_NAME_INDICATOR_CACHING = "sebserver.indicator.caching";
    public static final String PROPERTY_NAME_EVENT_CONSUMER_STRATEGY = "sebserver.event.consumerstrategy";
    public static final String EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE = "SINGLE_EVENT_STORE_STRATEGY";
    public static final String EVENT_CONSUMER_STRATEGY_ASYNC_BATCH_STORE = "ASYNC_BATCH_STORE_STRATEGY";

    @Autowired
    private ClientEventRecordMapper clientEventRecordMapper;
    @Autowired
    private ExamRecordMapper examRecordMapper;
    @Autowired
    private IndicatorRecordMapper indicatorRecordMapper;
    @Autowired
    private ExamIndicatorJoinMapper examIndicatorJoinMapper;
    @Autowired
    private ClientEventExtentionMapper clientEventExtentionMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private Environment environment;

    @Lazy
    @Bean
    public ExamDao examDao() {
        return new ExamDaoImpl(this.examRecordMapper,
                this.indicatorRecordMapper,
                this.examIndicatorJoinMapper);
    }

    @Lazy
    @Bean
    public ExamStateService examStateService() {
        return new ExamStateService(this.examRecordMapper, examDao());
    }

    @Lazy
    @Bean
    public ExamSessionService examSessionService(final ApplicationContext context) {
        final String property = this.environment.getProperty(PROPERTY_NAME_EVENT_CONSUMER_STRATEGY);
        final EventHandlingStrategy eventHandlingStrategy = context.getBean(property, EventHandlingStrategy.class);

        return new ExamSessionService(
                examStateService(),
                clientConnectionService(context),
                eventHandlingStrategy);
    }

    @Lazy
    @Bean(ErrorCountIndicator.BEAN_NAME)
    @Scope("prototype")
    public ClientIndicator errorCountIndicator() {
        return new ErrorCountIndicator(this.clientEventRecordMapper);
    }

    @Lazy
    @Bean(PingIntervalIndicator.BEAN_NAME)
    @Scope("prototype")
    public ClientIndicator pingIntervalIndicator() {
        return new PingIntervalIndicator(this.clientEventExtentionMapper);
    }

    @Lazy
    @Bean
    public ClientConnectionFactory clientConnectionFactory(final ApplicationContext context) {
        return new ClientConnectionFactory(context);
    }

    @Lazy
    @Bean
    public ClientConnectionService clientConnectionService(final ApplicationContext context) {
        return new ClientConnectionService(
                clientConnectionFactory(context),
                examStateService());
    }

    @Lazy
    @Bean(name = EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE)
    public SingleEventStoreStrategy singleEventStoreStrategy(final ApplicationContext context) {
        return new SingleEventStoreStrategy(
                clientConnectionService(context),
                this.clientEventRecordMapper);
    }

    @Lazy
    @Bean(name = EVENT_CONSUMER_STRATEGY_ASYNC_BATCH_STORE)
    public AsyncBatchEventSaveStrategy ayncBatchEventSaveService(final ApplicationContext context) {
        return new AsyncBatchEventSaveStrategy(
                clientConnectionService(context),
                this.sqlSessionFactory,
                getAsyncExecutor());
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
