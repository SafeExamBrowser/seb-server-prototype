/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest;

import java.math.BigDecimal;
import java.util.UUID;

public class IndicatorInfo {

    public final UUID clientUUID;
    public final String name;
    public final BigDecimal value;
    public final IndicatorDefinition indicator;

    public IndicatorInfo(final UUID clientUUID, final String name, final BigDecimal value,
            final IndicatorDefinition indicator) {
        super();
        this.clientUUID = clientUUID;
        this.name = name;
        this.value = value;
        this.indicator = indicator;
    }

    public UUID getClientUUID() {
        return this.clientUUID;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public IndicatorDefinition getIndicator() {
        return this.indicator;
    }

}
