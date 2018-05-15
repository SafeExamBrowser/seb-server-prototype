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
import org.springframework.context.ApplicationContext;

public class ClientConnectionFactory {

    private final ApplicationContext applicationContext;

    public ClientConnectionFactory(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ClientConnection create(final Long examId, final Long clientId, final UUID uuid, final Exam exam) {
        final ClientConnection result = new ClientConnection(examId, clientId, uuid);

        for (final IndicatorDefinition indicator : exam.getIndicators()) {
            result.add(indicator, this.applicationContext.getBean(indicator.type, ClientIndicator.class));
        }

        return result;
    }

}
