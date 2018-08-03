/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import java.util.UUID;

import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.eth.demo.sebserver.service.exam.indicator.ClientIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ClientConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionFactory.class);

    private final ApplicationContext applicationContext;
    private final boolean enableCaching;

    @Autowired
    public ClientConnectionFactory(
            final ApplicationContext applicationContext,
            @Value("${sebserver.indicator.caching}") final boolean enableCaching) {

        this.applicationContext = applicationContext;
        this.enableCaching = enableCaching;
    }

    public ClientConnection create(final Long examId, final Long clientId, final UUID uuid, final Exam exam) {
        final ClientConnection result = new ClientConnection(examId, clientId, uuid);

        for (final IndicatorDefinition indicatorDef : exam.getIndicators()) {
            try {
                final ClientIndicator indicator =
                        this.applicationContext.getBean(indicatorDef.type, ClientIndicator.class);
                indicator.init(examId, clientId, uuid, this.enableCaching);
                result.add(indicatorDef, indicator);
            } catch (final Exception e) {
                log.warn("No Indicator with type: {} found as registered bean. Ignore this one.", indicatorDef.type, e);
            }
        }

        return result;
    }

}