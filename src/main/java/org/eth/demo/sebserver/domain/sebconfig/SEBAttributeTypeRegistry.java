/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.sebconfig;

import static org.eth.demo.sebserver.domain.sebconfig.attribute.AttributeType.BASE64_BINARY;
import static org.eth.demo.sebserver.domain.sebconfig.attribute.AttributeType.COMPLEX_ATTRIBUTE;
import static org.eth.demo.sebserver.domain.sebconfig.attribute.AttributeType.INTEGER;
import static org.eth.demo.sebserver.domain.sebconfig.attribute.AttributeType.TEXT;

import org.eth.demo.sebserver.domain.sebconfig.attribute.AttributeType;
import org.eth.demo.sebserver.domain.sebconfig.attribute.Orientation;

// NOTE This mapping should contain all known SEB-Configuration attributes and  define the type and orientation.
//      This information should later go to the DB within tables "attribute_type" and "attribute_orientation"
//@formatter:off
public enum SEBAttributeTypeRegistry {
    additionalDictionaries(COMPLEX_ATTRIBUTE),
    dictionaryData(BASE64_BINARY, additionalDictionaries),
    dictionaryFormat(INTEGER, additionalDictionaries),
    localeName(TEXT, additionalDictionaries)

    ;

    public final AttributeType type;
    public final SEBAttributeTypeRegistry parent;
    public final Orientation orientation;

    private SEBAttributeTypeRegistry(final AttributeType type) {
        this(type, null, Orientation.NO_ORIENTATION);
    }

    private SEBAttributeTypeRegistry(final AttributeType type, final SEBAttributeTypeRegistry parent) {
        this(type, parent, Orientation.NO_ORIENTATION);
    }

    private SEBAttributeTypeRegistry(final AttributeType type, final SEBAttributeTypeRegistry parent, final Orientation orientation) {
        this.type = type;
        this.parent = parent;
        this.orientation = orientation;
    }


}
//@formatter:on
