/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.MainPageForm.MainPageState;
import org.eth.demo.sebserver.gui.service.rest.GETConfigs;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ActivitiesPane implements TemplateComposer {

    private static final String ATTR_ACTIVITY_SELECTION = "ACTIVITY_SELECTION";

    private final WidgetFactory widgetFactory;
    private final GETConfigs configsRequest;
    private final AuthorizationContextHolder authorizationContextHolder;

    public ActivitiesPane(
            final WidgetFactory widgetFactory,
            final GETConfigs configsRequest,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.widgetFactory = widgetFactory;
        this.configsRequest = configsRequest;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {
        if (composerCtx.activityListener == null) {
            throw new IllegalStateException("No ActivityListener available");
        }

        final Label activities = new Label(composerCtx.parent, SWT.NONE);
        activities.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        activities.setText("Activities");
        activities.setData(RWT.CUSTOM_VARIANT, "h3");

        final Tree navigation = new Tree(composerCtx.parent, SWT.SINGLE | SWT.FULL_SELECTION);
        navigation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TreeItem item = this.widgetFactory.treeItemLocalized(navigation, "org.sebserver.activities.admin");
        item.setData(ATTR_ACTIVITY_SELECTION, ActivitySelection.ADMIN);
        item.setItemCount(1);

        navigation.addListener(SWT.Expand, event -> {
            final TreeItem treeItem = (TreeItem) event.item;
            System.out.println("opened: " + treeItem);
        });
        navigation.addListener(SWT.Collapse, event -> {
            final TreeItem treeItem = (TreeItem) event.item;
            System.out.println("closed: " + treeItem);
        });
        navigation.addListener(SWT.Selection, event -> {
            final TreeItem treeItem = (TreeItem) event.item;
            System.out.println("selected: " + treeItem);
            final MainPageState mainPageState = MainPageState.get();
            mainPageState.activitySelection = (ActivitySelection) treeItem.getData(ATTR_ACTIVITY_SELECTION);
            composerCtx.activityListener.notifySelection(composerCtx);
        });

        applyPreSelection(navigation, composerCtx);

//        // get all exams for the current logged in user from the SEBServer Web-Service API
//        final Collection<ConfigTableRow> configs = this.configsRequest
//                .with(this.authorizationContextHolder)
//                .doAPICall()
//                .onError(t -> {
//                    throw new RuntimeException(t);
//                }); // TODO error handling
    }

    private void applyPreSelection(final Tree navigation, final ComposerServiceContext composerCtx) {
        final MainPageState mainPageState = MainPageState.get();
        if (mainPageState.activitySelection == null || mainPageState.activitySelection == ActivitySelection.NONE) {
            return;
        }

    }

}
