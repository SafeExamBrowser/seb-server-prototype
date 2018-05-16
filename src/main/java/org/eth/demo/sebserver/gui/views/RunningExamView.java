/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.push.ServerPushData;
import org.eth.demo.sebserver.gui.push.ServerPushService;
import org.eth.demo.sebserver.gui.view.ViewComposer;
import org.eth.demo.sebserver.gui.view.ViewService;

public class RunningExamView implements ViewComposer {

    private final ServerPushService serverPushService;

    public RunningExamView(final ServerPushService serverPushService) {
        this.serverPushService = serverPushService;
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent) {
        final RowLayout layout = new RowLayout();
        layout.type = SWT.HORIZONTAL;
        parent.setLayout(layout);

        final Label label = new Label(parent, SWT.NONE);
        label.setText("Server Push Example");
        label.setBackground(new Color(label.getDisplay(), new RGB(0, 0, 255)));

        final Button button = new Button(parent, SWT.FLAT);
        button.setText("Back to Home");
        button.addListener(SWT.Selection, event -> {
            viewService.composeInitView(parent);
        });

        final UpdateData updateData = new UpdateData(label);

        this.serverPushService.runServerPush(
                () -> {
                    try {
                        Thread.sleep(2000);
                    } catch (final Exception e) {
                    }
                    updateData.toggle();
                    return updateData;
                },
                data -> () -> {
                    data.label.setBackground(data.currentColor);
                    data.label.setText(label.getText() + "A");
                    data.label.requestLayout();
                });
    }

    private static class UpdateData implements ServerPushData {

        final Label label;
        final Color color1;
        final Color color2;
        Color currentColor;

        UpdateData(final Label label) {
            this.label = label;
            this.color1 = new Color(label.getDisplay(), new RGB(0, 255, 0));
            this.color2 = new Color(label.getDisplay(), new RGB(0, 0, 255));
            this.currentColor = this.color1;
        }

        void toggle() {
            this.currentColor = (this.currentColor == this.color1) ? this.color2 : this.color1;
        }

        @Override
        public boolean isDisposed() {
            return this.label.isDisposed();
        }

        @Override
        public Display getDisplay() {
            return this.label.getDisplay();
        }

    }

}
