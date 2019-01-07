/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.template.ImageCell;
import org.eclipse.rap.rwt.template.Template;
import org.eclipse.rap.rwt.template.TextCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.event.PageEventListener;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ActionPane implements TemplateComposer {

    private static final String ACTION_EVENT_CALL_KEY = "ACTION_EVENT_CALL";

    private final WidgetFactory widgetFactory;

    public ActionPane(final WidgetFactory widgetFactory) {
        super();
        this.widgetFactory = widgetFactory;
    }

    @Override
    public void compose(final PageContext composerCtx) {

        final Label label = new Label(composerCtx.parent, SWT.NONE);
        label.setData(RWT.CUSTOM_VARIANT, "h3");
        label.setText("Actions");
        final GridData titleLayout = new GridData(SWT.FILL, SWT.TOP, true, false);
        titleLayout.verticalIndent = 10;
        titleLayout.horizontalIndent = 10;
        label.setLayoutData(titleLayout);

        final Tree actions = this.widgetFactory.treeLocalized(composerCtx.parent, SWT.SINGLE | SWT.FULL_SELECTION);
        actions.setData(RWT.CUSTOM_VARIANT, "actions");
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        actions.setLayoutData(gridData);
        final Template template = new Template();
        final ImageCell imageCell = new ImageCell(template);
        imageCell.setLeft(0, 0).setWidth(40).setTop(0).setBottom(0, 0).setHorizontalAlignment(SWT.LEFT);
        imageCell.setBindingIndex(0);
        final TextCell textCell = new TextCell(template);
        textCell.setLeft(0, 30).setWidth(150).setTop(7).setBottom(0, 0).setHorizontalAlignment(SWT.LEFT);
        textCell.setBindingIndex(0);
        actions.setData(RWT.ROW_TEMPLATE, template);

        actions.addListener(SWT.Selection, event -> {
            final TreeItem treeItem = (TreeItem) event.item;

            final Runnable action = (Runnable) treeItem.getData(ACTION_EVENT_CALL_KEY);
            action.run();

            if (!treeItem.isDisposed()) {
                treeItem.getParent().deselectAll();
            }
        });

        actions.setData(
                PageEventListener.LISTENER_ATTRIBUTE_KEY,
                new ActionPublishEventListener() {
                    @Override
                    public void notify(final ActionPublishEvent event) {
                        final TreeItem actionItem = ActionPane.this.widgetFactory.treeItemLocalized(
                                actions,
                                event.actionDefinition.name);
                        actionItem.setImage(event.actionDefinition.icon.getImage(composerCtx.parent.getDisplay()));
                        actionItem.setData(ACTION_EVENT_CALL_KEY,
                                new SafeActionExecution(composerCtx, event, event.run));
                    }
                });

    }

}
