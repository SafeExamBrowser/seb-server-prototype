/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.indicator;

import java.math.BigDecimal;
import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.springframework.transaction.annotation.Transactional;

public interface ClientIndicator {

    String getType();

    String getDisplayName();

    void init(Long examId, Long clientId, UUID clientUUID, boolean alwaysCompute);

    Long examId();

    Long clientId();

    UUID clientUUID();

    BigDecimal getCurrentValue();

    @Transactional(readOnly = true)
    BigDecimal computeValueAt(Long timestamp);

    void notifyClientEvent(ClientEvent event);

}
