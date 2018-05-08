/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "org.eth.demo.sebserver.batis")
public class BatisSpringConfig {

    @Lazy
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(final DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    @Lazy
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(final DataSource dataSource) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory(dataSource));
        return sqlSessionTemplate;
    }

    @Lazy
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(final DataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

}
