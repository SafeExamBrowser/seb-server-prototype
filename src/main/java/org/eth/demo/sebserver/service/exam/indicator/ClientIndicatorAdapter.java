/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.indicator;

public abstract class ClientIndicatorAdapter implements ClientIndicator {

    protected Long examId;
    protected Long connectionId;
    protected boolean cachingEnabled;

    protected Double currentValue = null;

    @Override
    public void init(
            final Long examId,
            final Long connectionId,
            final boolean cachingEnabled) {

        this.examId = examId;
        this.connectionId = connectionId;
        this.cachingEnabled = cachingEnabled;
    }

    @Override
    public Long examId() {
        return this.examId;
    }

    @Override
    public Long connectionId() {
        return this.connectionId;
    }

    @Override
    public Double getCurrentValue() {
        if (this.currentValue == null || !this.cachingEnabled) {
            this.currentValue = computeValueAt(System.currentTimeMillis());
        }

        return this.currentValue;
    }

}
