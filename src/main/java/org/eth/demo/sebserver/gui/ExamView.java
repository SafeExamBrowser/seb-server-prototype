/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ExamView extends AbstractEntryPoint {

    private static final long serialVersionUID = -1106183608032715804L;

    @Override
    protected void createContents(final Composite parent) {
        final RowLayout layout = new RowLayout();
        layout.type = SWT.HORIZONTAL;
        parent.setLayout(layout);

        final Label label = new Label(parent, SWT.NONE);
        label.setText("Hello RAP World");
        label.setBackground(new Color(label.getDisplay(), new RGB(0, 0, 255)));

    }

}
