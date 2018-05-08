package org.eth.demo.sebserver.service.dao;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DaoSpringConfig {

    @Lazy
    @Bean
    public ExamDao examDao(
            final ExamRecordMapper examMapper,
            final IndicatorRecordMapper indicatorMapper,
            final ExamIndicatorJoinMapper examIndicatorJoinMapper) {

        return new ExamDaoImpl(examMapper, indicatorMapper, examIndicatorJoinMapper);
    }

}
