/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;

public abstract class ControlFieldAdapter<T extends Control> implements InputField {

    protected final ConfigViewAttribute attribute;
    protected final T control;

    public ControlFieldAdapter(final ConfigViewAttribute attribute, final T control) {
        this.attribute = attribute;
        this.control = control;
    }

    @Override
    public final FieldType getType() {
        return this.attribute.getFieldType();
    }

    @Override
    public final ConfigViewAttribute getAttribute() {
        return this.attribute;
    }

    @Override
    public final String getName() {
        return this.attribute.name;
    }

    @Override
    public final Control getControl() {
        return this.control;
    }

//    public void setValueChangeListener(final ValueChangeListener listener) {
//        this.control.addListener(
//                SWT.Verify,
//                event -> listener.valueChanged(this.attribute, getValue().asString(), 0));
//    }

}
