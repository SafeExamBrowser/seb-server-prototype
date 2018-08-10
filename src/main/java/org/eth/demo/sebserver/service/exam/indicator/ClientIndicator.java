/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.indicator;

import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;

public interface ClientIndicator {

    String getType();

    String getDisplayName();

    void init(Long examId, String clientIdentifier, boolean alwaysCompute);

    Long examId();

    String clientIdentifier();

    Double getCurrentValue();

    Double computeValueAt(Long timestamp);

    void notifyClientEvent(ClientEvent event);

}
