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
import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.eth.demo.sebserver.gui.domain.admin.UserRole;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.MainPage.MainPageState;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.page.action.ActionEventListener;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection.Activity;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelectionEvent;
import org.eth.demo.sebserver.gui.service.page.event.PageEventListener;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.institution.GetInstitutionInfo;
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
    public void compose(final PageContext composerCtx) {
        final UserInfo userInfo = this.authorizationContextHolder
                .getAuthorizationContext()
                .getLoggedInUser();

        final Label activities = this.widgetFactory.labelLocalized(
                composerCtx.parent, "h3", new LocTextKey("org.sebserver.activities"));
        activities.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        final Tree navigation = this.widgetFactory.treeLocalized(composerCtx.parent, SWT.SINGLE | SWT.FULL_SELECTION);
        navigation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Map<String, String> insitutionInfo = this.restServices
                .sebServerCall(GetInstitutionInfo.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                });

        if (userInfo.hasRole(UserRole.SEB_SERVER_ADMIN)) {
            // institutions (list) as root
            final TreeItem institutions = this.widgetFactory.treeItemLocalized(
                    navigation,
                    new LocTextKey("org.sebserver.activities.inst"));
            ActivitySelection.set(institutions, Activity.INSTITUTIONS.selection());

            for (final Map.Entry<String, String> inst : insitutionInfo.entrySet()) {
                createInstitutionItem(institutions, inst.getKey(), inst.getValue());
            }
        } else {
            final Entry<String, String> inst = insitutionInfo.entrySet().iterator().next();
            createInstitutionItem(navigation, inst.getKey(), inst.getValue());
        }

        final TreeItem user = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.user");
        ActivitySelection.set(user, Activity.USERS.selection());

        final TreeItem configs = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.sebconfig");
        ActivitySelection.set(configs, Activity.SEB_CONFIGS.selection());

        final TreeItem exams = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.exam");
        ActivitySelection.set(exams, Activity.EXAMS.selection());

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
            composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
        });

        navigation.setData(
                PageEventListener.LISTENER_ATTRIBUTE_KEY,
                createActionEventListener(navigation, composerCtx));

        applySelection(navigation, composerCtx);
    }

    private ActionEventListener createActionEventListener(
            final Tree navigation,
            final PageContext composerCtx) {

        // TODO create handler mapping for action types instead of if else...
        return new ActionEventListener() {

            @Override
            public void notify(final ActionEvent event) {
                if (event.actionDefinition == ActionDefinition.INSTITUTION_NEW) {
                    final IdAndName idAndName = (IdAndName) event.source;
                    final TreeItem institutions = findItemByActivity(navigation.getItems(), Activity.INSTITUTIONS);
                    final TreeItem newInstItem = createInstitutionItem(institutions, idAndName);
                    MainPageState.get().activitySelection = ActivitySelection.get(newInstItem);
                    navigation.select(newInstItem);

                } else if (event.actionDefinition == ActionDefinition.INSTITUTION_MODIFY) {

                } else if (event.actionDefinition == ActionDefinition.INSTITUTION_DELETE) {

                }
            }
        };
    }

    private TreeItem createInstitutionItem(final TreeItem parent, final IdAndName idAndName) {
        return createInstitutionItem(parent, idAndName.id, idAndName.name);
    }

    private TreeItem createInstitutionItem(final Tree parent, final String id, final String name) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(id, name, institution);
        return institution;
    }

    private TreeItem createInstitutionItem(final TreeItem parent, final String id, final String name) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(id, name, institution);
        return institution;
    }

    private void createInstitutionItem(final String id, final String name, final TreeItem institution) {
        institution.setText(name);
        ActivitySelection.set(institution, Activity.INSTITUTION.selection()
                .with(id));

//        final TreeItem lmsSetup = this.widgetFactory.treeItemLocalized(
//                institution,
//                "org.sebserver.activities.lms");
//        ActivitySelection.set(lmsSetup, Activity.LMS_SETUP.selection());

    }

    private void applySelection(final Tree navigation, final PageContext composerCtx) {
        final MainPageState mainPageState = MainPageState.get();
        if (mainPageState.activitySelection == null) {
            mainPageState.activitySelection = Activity.NONE.selection();
        }

        final TreeItem itemToPreSelect = findSelectedItem(navigation.getItems(), mainPageState);
        if (itemToPreSelect != null) {
            navigation.select(itemToPreSelect);
            expand(itemToPreSelect.getParentItem());
            composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
        }
    }

    private TreeItem findItemByActivity(final TreeItem[] items, final Activity activity) {
        if (items == null) {
            return null;
        }

        for (final TreeItem item : items) {
            final ActivitySelection activitySelection = ActivitySelection.get(item);
            if (activitySelection != null && activitySelection.activity == activity) {
                return item;
            }

            return findItemByActivity(item.getItems(), activity);
        }

        return null;
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
