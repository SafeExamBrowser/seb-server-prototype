/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

//@formatter:off
public enum AttributeType {
    TEXT_FIELD,
    TEXT_AREA,
    CHECKBOX,
    INTEGER,
    DECIMAL,
    SINGLE_SELECTION,
    MULTI_SELECTION,
    RADIO_SELECTION,
    BASE64_BINARY,

    COMPLEX_ATTRIBUTE,
    ;
}
//@formatter:on
