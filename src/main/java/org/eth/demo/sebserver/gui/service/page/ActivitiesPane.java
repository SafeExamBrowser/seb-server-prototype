/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.eth.demo.sebserver.gui.domain.admin.UserRole;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.MainPageForm.MainPageState;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection.Activity;
import org.eth.demo.sebserver.gui.service.rest.GETInstitutionInfo;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ActivitiesPane implements TemplateComposer {

    private final WidgetFactory widgetFactory;
    private final RestServices restServices;
    private final AuthorizationContextHolder authorizationContextHolder;

    public ActivitiesPane(
            final WidgetFactory widgetFactory,
            final RestServices restServices,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.widgetFactory = widgetFactory;
        this.restServices = restServices;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {
        final UserInfo userInfo = this.authorizationContextHolder
                .getAuthorizationContext()
                .getLoggedInUser();

        final Label activities = this.widgetFactory.labelLocalized(
                composerCtx.parent, "h3", new LocTextKey("org.sebserver.activities"));
        activities.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        final Tree navigation = this.widgetFactory.treeLocalized(composerCtx.parent, SWT.SINGLE | SWT.FULL_SELECTION);
        navigation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Map<String, String> insitutionInfo = this.restServices
                .sebServerCall(GETInstitutionInfo.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                });

        if (userInfo.hasRole(UserRole.SEB_SERVER_ADMIN)) {
            // institutions (list) as root
            final TreeItem institutions = this.widgetFactory.treeItemLocalized(
                    navigation,
                    new LocTextKey("org.sebserver.activities.inst", insitutionInfo.size()));
            ActivitySelection.set(institutions, Activity.INSTITUTIONS.selection());

            for (final Map.Entry<String, String> inst : insitutionInfo.entrySet()) {
                createInstitutionItem(institutions, inst);
            }
        } else {
            final Entry<String, String> inst = insitutionInfo.entrySet().iterator().next();
            createInstitutionItem(navigation, inst);
        }

        final TreeItem exams = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.exam");
        ActivitySelection.set(exams, Activity.EXAM.selection());

        final TreeItem configs = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.sebconfig");
        ActivitySelection.set(configs, Activity.SEB_CONFIG.selection());

        final TreeItem monitoring = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.monitoring");
        ActivitySelection.set(monitoring, Activity.MONITORING.selection());

        navigation.addListener(SWT.Expand, event -> {
            final TreeItem treeItem = (TreeItem) event.item;

            System.out.println("opened: " + treeItem);

            final ActivitySelection activity = ActivitySelection.get(treeItem);
            if (activity != null) {
                activity.processExpand(treeItem);
            }
        });
        navigation.addListener(SWT.Collapse, event -> {
            final TreeItem treeItem = (TreeItem) event.item;

            System.out.println("closed: " + treeItem);

            final ActivitySelection activity = ActivitySelection.get(treeItem);
            if (activity != null) {
                activity.processCollapse(treeItem);
            }
        });
        navigation.addListener(SWT.Selection, event -> {
            final TreeItem treeItem = (TreeItem) event.item;

            System.out.println("selected: " + treeItem);

            final MainPageState mainPageState = MainPageState.get();
            mainPageState.activitySelection = ActivitySelection.get(treeItem);
            if (mainPageState.activitySelection == null) {
                mainPageState.activitySelection = Activity.NONE.selection();
            }
            composerCtx.notify(mainPageState.activitySelection);
        });

        applyPreSelection(navigation, composerCtx);
    }

    private void createInstitutionItem(final Tree parent, final Map.Entry<String, String> inst) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(inst, institution);
    }

    private void createInstitutionItem(final TreeItem parent, final Map.Entry<String, String> inst) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(inst, institution);
    }

    private void createInstitutionItem(final Map.Entry<String, String> inst, final TreeItem institution) {
        institution.setText(inst.getValue());
        ActivitySelection.set(institution, Activity.INSTITUTION.selection()
                .with(inst.getKey()));

        final TreeItem lmsSetup = this.widgetFactory.treeItemLocalized(
                institution,
                "org.sebserver.activities.lms");
        ActivitySelection.set(lmsSetup, Activity.LMS_SETUP.selection());

        final TreeItem user = this.widgetFactory.treeItemLocalized(
                institution,
                "org.sebserver.activities.user");
        ActivitySelection.set(user, Activity.USER.selection());
    }

    private void applyPreSelection(final Tree navigation, final ComposerServiceContext composerCtx) {
        final MainPageState mainPageState = MainPageState.get();
        if (mainPageState.activitySelection == null || mainPageState.activitySelection.activity == Activity.NONE) {
            return;
        }

        final TreeItem itemToPreSelect = findSelectedItem(navigation.getItems(), mainPageState);
        if (itemToPreSelect != null) {
            navigation.select(itemToPreSelect);
            expand(itemToPreSelect.getParentItem());
            composerCtx.notify(mainPageState.activitySelection);
        }
    }

    private TreeItem findSelectedItem(final TreeItem[] items, final MainPageState mainPageState) {
        if (items == null) {
            return null;
        }

        for (final TreeItem item : items) {
            final ActivitySelection activitySelection = ActivitySelection.get(item);
            if (activitySelection != null && activitySelection.equals(mainPageState.activitySelection)) {
                return item;
            }

            return findSelectedItem(item.getItems(), mainPageState);
        }

        return null;
    }

    private static final void expand(final TreeItem item) {
        if (item == null) {
            return;
        }

        item.setExpanded(true);
        expand(item.getParentItem());
    }

}
