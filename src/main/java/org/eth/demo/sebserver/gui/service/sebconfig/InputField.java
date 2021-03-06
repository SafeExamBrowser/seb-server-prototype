/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import static org.eth.demo.sebserver.gui.service.sebconfig.InputField.TitleOrientation.*;

import java.util.Collection;

import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;

public interface InputField {

    // TODO this should go to a column in orientation table
    public enum TitleOrientation {
        NONE, LEFT, RIGHT, TOP
    }

    //@formatter:off
    public enum FieldType {
        LABEL(NONE),
        TEXT_FIELD(LEFT),
        TEXT_AREA(LEFT),
        CHECKBOX(NONE),
        CHECK_FIELD(LEFT),
        INTEGER(LEFT),
        DECIMAL(LEFT),
        SINGLE_SELECTION(LEFT),
        MULTI_SELECTION(LEFT),
        RADIO_SELECTION(NONE),
        FILE_UPLOAD(RIGHT),
        TABLE(TOP),

        UNKNOWN(NONE),
        ;

        public final TitleOrientation titleOrientation;

        private FieldType(final TitleOrientation titleOrientation) {
            this.titleOrientation = titleOrientation;
        }

    }
    //@formatter:on

    FieldType getType();

    String getName();

    void initValue(Collection<ConfigAttributeValue> values);

    Control getControl();

    ConfigViewAttribute getAttribute();

    void disable();

    void enable();

}
