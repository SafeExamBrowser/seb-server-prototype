/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.indicator;

import java.math.BigDecimal;

public interface ClientIndicator {

    String getBeanName();

    String getDisplayName();

    BigDecimal computeValue(Long examId, Long clientId, Long timestamp);

}
