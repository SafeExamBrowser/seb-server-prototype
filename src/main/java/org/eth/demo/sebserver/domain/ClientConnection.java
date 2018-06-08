/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.eth.demo.sebserver.service.indicator.ClientIndicator;

public class ClientConnection {

    public final Long examId;
    public final Long clientId;
    public final UUID clientUUID;
    private final Map<IndicatorDefinition, ClientIndicator> indicatorMapping = new LinkedHashMap<>();

    public ClientConnection(final Long examId, final Long clientId, final UUID clientUUID) {
        this.examId = examId;
        this.clientId = clientId;
        this.clientUUID = clientUUID;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public UUID getClientUuid() {
        return this.clientUUID;
    }

    public ClientConnection add(final IndicatorDefinition indicator, final ClientIndicator clientIndicator) {
        this.indicatorMapping.put(indicator, clientIndicator);
        return this;
    }

    public Stream<ClientIndicator> valuesStream() {
        return this.indicatorMapping.values().stream();
    }

    public Stream<ClientIndicator> indicators() {
        return this.indicatorMapping.values().stream();
    }

}
