/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.service.dao.ExamDao;
import org.eth.demo.sebserver.service.dao.ExamDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ServiceSpringConfig {

    @Autowired
    private ClientEventRecordMapper clientEventRecordMapper;
    @Autowired
    private ExamRecordMapper examRecordMapper;
    @Autowired
    private IndicatorRecordMapper indicatorRecordMapper;
    @Autowired
    private ExamIndicatorJoinMapper examIndicatorJoinMapper;

    @Lazy
    @Bean
    public ExamDao examDao() {
        return new ExamDaoImpl(this.examRecordMapper,
                this.indicatorRecordMapper,
                this.examIndicatorJoinMapper);
    }

    @Lazy
    @Bean
    public ExamSessionService examSessionService() {
        return new ExamSessionService(this.clientEventRecordMapper,
                this.examRecordMapper,
                this.examDao());
    }

}
