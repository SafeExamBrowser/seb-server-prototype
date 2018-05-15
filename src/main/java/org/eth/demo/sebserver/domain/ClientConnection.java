/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.domain.rest.IndicatorDefinition;
import org.eth.demo.sebserver.domain.rest.IndicatorInfo;
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

    ClientConnection add(final IndicatorDefinition indicator, final ClientIndicator clientIndicator) {
        this.indicatorMapping.put(indicator, clientIndicator);
        return this;
    }

    public Collection<IndicatorInfo> getIndicatorInfo() {
        return this.indicatorMapping
                .entrySet()
                .stream()
                .map(this::createIndicatorInfo)
                .collect(Collectors.toList());
    }

    private final IndicatorInfo createIndicatorInfo(final Map.Entry<IndicatorDefinition, ClientIndicator> entry) {
        return new IndicatorInfo(
                this.clientUUID,
                entry.getValue().getDisplayName(),
                entry.getValue().computeValue(this.examId, this.clientId, null),
                entry.getKey());
    }

}
