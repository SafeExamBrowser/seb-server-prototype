/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;

public abstract class ControlFieldAdapter<T extends Control> implements InputField {

    protected final GUIViewAttribute attribute;
    protected final T control;

    public ControlFieldAdapter(final GUIViewAttribute attribute, final T control) {
        this.attribute = attribute;
        this.control = control;
    }

    @Override
    public final FieldType getType() {
        return this.attribute.getFieldType();
    }

    @Override
    public final GUIViewAttribute getAttribute() {
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

    @Override
    public void setValueListener(final BiConsumer<String, GUIViewAttribute> valueListener) {
        this.control.addListener(
                SWT.Verify,
                event -> valueListener.accept(
                        String.valueOf(event.data),
                        this.attribute));
    }

}
