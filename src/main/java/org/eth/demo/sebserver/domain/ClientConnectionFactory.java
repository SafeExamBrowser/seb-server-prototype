/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain;

import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorDefinition;
import org.eth.demo.sebserver.service.indicator.ClientIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class ClientConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionFactory.class);

    private final ApplicationContext applicationContext;

    public ClientConnectionFactory(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ClientConnection create(final Long examId, final Long clientId, final UUID uuid, final Exam exam) {
        final ClientConnection result = new ClientConnection(examId, clientId, uuid);

        for (final IndicatorDefinition indicator : exam.getIndicators()) {
            try {
                result.add(indicator, this.applicationContext.getBean(indicator.type, ClientIndicator.class));
            } catch (final Exception e) {
                log.warn("No Indicator with type: {} found as registered bean. Ignore this one.", indicator.type);
            }
        }

        return result;
    }

}
