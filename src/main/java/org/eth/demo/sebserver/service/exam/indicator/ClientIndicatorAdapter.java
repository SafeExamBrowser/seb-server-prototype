/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.indicator;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class ClientIndicatorAdapter implements ClientIndicator {

    protected Long examId;
    protected Long clientId;
    protected UUID clientUUID;
    protected boolean cachingEnabled;

    protected BigDecimal currentValue = null;

    @Override
    public void init(final Long examId, final Long clientId, final UUID clientUUID, final boolean cachingEnabled) {
        this.examId = examId;
        this.clientId = clientId;
        this.clientUUID = clientUUID;
        this.cachingEnabled = cachingEnabled;
    }

    @Override
    public Long examId() {
        return this.examId;
    }

    @Override
    public Long clientId() {
        return this.clientId;
    }

    @Override
    public UUID clientUUID() {
        return this.clientUUID;
    }

    @Override
    public BigDecimal getCurrentValue() {
        if (this.currentValue == null || !this.cachingEnabled) {
            this.currentValue = computeValueAt(System.currentTimeMillis());
        }

        return this.currentValue;
    }

}
