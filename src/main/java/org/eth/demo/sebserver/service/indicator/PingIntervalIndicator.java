/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.indicator;

import java.math.BigDecimal;

public final class PingIntervalIndicator implements ClientIndicator {

    public static final String BEAN_NAME = "pingIntervalIndicator";
    private static final String DISPLAY_NAME = "Last Ping";

    @Override
    public final BigDecimal computeValue(final Long examId, final Long clientId, final Long timestamp) {
        final Long time = (timestamp != null) ? timestamp : System.currentTimeMillis();

        // TODO

        return null;
    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

}
