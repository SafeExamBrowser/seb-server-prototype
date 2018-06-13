/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

import static org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationValueType.*;

//@formatter:off
public enum AttributeType {
    LABEL(NONE),
    TEXT_FIELD(TEXT),
    TEXT_AREA(TEXT),
    CHECKBOX(TEXT),
    CHECK_FIELD(TEXT),
    INTEGER(TEXT),
    DECIMAL(TEXT),
    SINGLE_SELECTION(TEXT),
    MULTI_SELECTION(LIST),
    RADIO_SELECTION(TEXT),
    FILE_UPLOAD(BASE64_BINARY),

    TABLE(COMPOSITE_LIST),
    ;

    public final ConfigurationValueType configurationValueType;

    private AttributeType(final ConfigurationValueType configurationValueType) {
        this.configurationValueType = configurationValueType;
    }
}
//@formatter:on
