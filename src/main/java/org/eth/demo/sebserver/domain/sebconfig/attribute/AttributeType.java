/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig.attribute;

//@formatter:off
public enum AttributeType {
    STRING,             // DB Type - normal value column = VARCHAR
    BOOLEAN,            // DB Type - normal value column = VARCHAR
    INTEGER,            // DB Type - normal value column = VARCHAR
    DECIMAL,            // DB Type - normal value column = VARCHAR
    TEXT,               // DB Type - separate table TEXT (~64 KB)
    BASE64_BINARY,      // DB Type - separate table MEDIUMTEXT (~16 MB)?

    LIST_OF_TEXT,
    LIST_OF_BOOLEAN,
    LIST_OF_INTEGER,
    LIST_OF_DECIMAL,

    COMPLEX_ATTRIBUTE,
    ;
}
//@formatter:on
