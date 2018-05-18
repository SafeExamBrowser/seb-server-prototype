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
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.push.ServerPushContext;
import org.eth.demo.sebserver.gui.push.ServerPushService;
import org.eth.demo.sebserver.gui.view.ViewComposer;
import org.eth.demo.sebserver.gui.view.ViewService;
import org.eth.demo.sebserver.util.TypedMap.TypedKey;

public class RunningExamView implements ViewComposer {

    private static final TypedKey<Color> CURRENT_COLOR = new TypedKey<>("CURRENT_COLOR", Color.class);
    private static final TypedKey<Label> LABEL = new TypedKey<>("LABEL", Label.class);

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
            viewService.composeView(parent, ExamOverview.class);
        });

        final ServerPushContext context = new ServerPushContext(label, runAgainContext -> true);
        final Color color1 = new Color(label.getDisplay(), new RGB(0, 255, 0));
        final Color color2 = new Color(label.getDisplay(), new RGB(0, 0, 255));
        context.getDataMapping().put(CURRENT_COLOR, color1);
        context.getDataMapping().put(LABEL, label);

        this.serverPushService.runServerPush(
                context,
                ctx -> {
                    try {
                        Thread.sleep(2000);
                    } catch (final Exception e) {
                    }
                    final Color color = ctx.getDataMapping().get(CURRENT_COLOR);
                    ctx.getDataMapping().put(
                            CURRENT_COLOR,
                            (color == color1) ? color2 : color1);
                },
                ctx -> () -> {
                    final Label l = ctx.getDataMapping().get(LABEL);
                    l.setBackground(ctx.getDataMapping().get(CURRENT_COLOR));
                    l.setText(label.getText() + "A");
                    l.requestLayout();
                });
    }
}
