/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.indicator;

import java.util.UUID;

import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorDefinition;
import org.eth.demo.sebserver.service.ServiceSpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ClientConnectionFactory extends ProxyFactoryBean {

    private static final long serialVersionUID = -5479046722015368430L;

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionFactory.class);

    private final ApplicationContext applicationContext;
    private final boolean alwaysCompute;

    public ClientConnectionFactory(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.alwaysCompute = !applicationContext
                .getEnvironment()
                .getProperty(ServiceSpringConfig.PROPERTY_NAME_INDICATOR_CACHING, Boolean.class);
    }

    public ClientConnection create(final Long examId, final Long clientId, final UUID uuid, final Exam exam) {
        final ClientConnection result = new ClientConnection(examId, clientId, uuid);

        for (final IndicatorDefinition indicatorDef : exam.getIndicators()) {
            try {
                final ClientIndicator indicator =
                        this.applicationContext.getBean(indicatorDef.type, ClientIndicator.class);
                indicator.init(examId, clientId, uuid, this.alwaysCompute);
                result.add(indicatorDef, indicator);
            } catch (final Exception e) {
                log.warn("No Indicator with type: {} found as registered bean. Ignore this one.", indicatorDef.type, e);
            }
        }

        return result;
    }

    @Override
    public Object getObject() throws BeansException {
        // TODO Auto-generated method stub
        return super.getObject();
    }

}
