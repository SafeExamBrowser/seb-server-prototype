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
import org.eth.demo.sebserver.SEBServer;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ServiceSpringConfig implements AsyncConfigurer {

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
    private ApplicationContext applicationContext;

    @Value("${" + SEBServer.PROPERTY_NAME_INDICATOR_CACHING + "}")
    private boolean indicatorValueCachingEnabled;
    @Value("${" + SEBServer.PROPERTY_NAME_EVENT_CONSUMER_STRATEGY + "}")
    private String eventHandlingStrategyBeanName;

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
    public ExamSessionService examSessionService() {
        final EventHandlingStrategy eventHandlingStrategy = this.applicationContext
                .getBean(this.eventHandlingStrategyBeanName, EventHandlingStrategy.class);

        return new ExamSessionService(
                examStateService(),
                clientConnectionService(),
                eventHandlingStrategy,
                this.indicatorValueCachingEnabled);
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
    public ClientConnectionFactory clientConnectionFactory() {
        return new ClientConnectionFactory(this.applicationContext);
    }

    @Lazy
    @Bean
    public ClientConnectionService clientConnectionService() {
        return new ClientConnectionService(
                clientConnectionFactory(),
                examStateService());
    }

    @Lazy
    @Bean(name = SEBServer.EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE)
    public SingleEventStoreStrategy singleEventStoreStrategy() {
        return new SingleEventStoreStrategy(
                clientConnectionService(),
                this.clientEventRecordMapper);
    }

    @Lazy
    @Bean(name = SEBServer.EVENT_CONSUMER_STRATEGY_ASYNC_BATCH_STORE)
    public AsyncBatchEventSaveStrategy ayncBatchEventSaveService() {
        return new AsyncBatchEventSaveStrategy(
                clientConnectionService(),
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