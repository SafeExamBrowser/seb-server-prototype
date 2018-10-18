/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.activity;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.domain.admin.UserInfo;
import org.eth.demo.sebserver.gui.domain.admin.UserRole;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.MainPage.MainPageState;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.page.action.ActionEventListener;
import org.eth.demo.sebserver.gui.service.page.activity.ActivitySelection.Activity;
import org.eth.demo.sebserver.gui.service.page.event.PageEventListener;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.exam.GetRunningExamNames;
import org.eth.demo.sebserver.gui.service.rest.institution.GetInstitutionNames;
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
        final GridData activitiesGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        activitiesGridData.horizontalIndent = 20;
        activities.setLayoutData(activitiesGridData);

        final Tree navigation = this.widgetFactory.treeLocalized(composerCtx.parent, SWT.SINGLE | SWT.FULL_SELECTION);
        final GridData navigationGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        //navigationGridData.horizontalIndent = 20;
        navigation.setLayoutData(navigationGridData);

        final List<IdAndName> insitutionNames = this.restServices
                .sebServerCall(GetInstitutionNames.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                });

        if (userInfo.hasRole(UserRole.SEB_SERVER_ADMIN)) {
            // institutions (list) as root
            final TreeItem institutions = this.widgetFactory.treeItemLocalized(
                    navigation,
                    new LocTextKey("org.sebserver.activities.inst"));
            ActivitySelection.set(institutions, Activity.INSTITUTIONS.createSelection());

            for (final IdAndName inst : insitutionNames) {
                createInstitutionItem(institutions, inst);
            }
        } else {
            final IdAndName inst = insitutionNames.iterator().next();
            createInstitutionItem(navigation, inst);
        }

        final TreeItem user = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.user");
        ActivitySelection.set(user, Activity.USERS.createSelection());

        final TreeItem configs = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.sebconfig");
        ActivitySelection.set(configs, Activity.SEB_CONFIGS.createSelection());

        final TreeItem exams = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.exam");
        ActivitySelection.set(exams, Activity.EXAMS.createSelection());

        final TreeItem monitoring = this.widgetFactory.treeItemLocalized(
                navigation,
                "org.sebserver.activities.monitoring");
        ActivitySelection.set(monitoring, Activity.MONITORING.createSelection());

        final TreeItem runningExams = this.widgetFactory.treeItemLocalized(
                monitoring,
                "org.sebserver.activities.runningExams");
        ActivitySelection.set(runningExams, Activity.RUNNING_EXAMS.createSelection()
                .withExpandFunction(this::runningExamExpand));
        runningExams.setItemCount(1);

        final TreeItem logs = this.widgetFactory.treeItemLocalized(
                monitoring,
                "org.sebserver.activities.logs");
        ActivitySelection.set(logs, Activity.LOGS.createSelection());

        navigation.addListener(SWT.Expand, this::handleExpand);
        navigation.addListener(SWT.Selection, event -> handleSelection(composerCtx, event));

        navigation.setData(
                PageEventListener.LISTENER_ATTRIBUTE_KEY,
                createActionEventListener(navigation, composerCtx));

    }

    private void runningExamExpand(final TreeItem item) {
        item.removeAll();
        final List<IdAndName> runningExamNames = this.restServices
                .sebServerCall(GetRunningExamNames.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                });

        if (runningExamNames != null) {
            for (final IdAndName runningExamName : runningExamNames) {
                final TreeItem runningExams = this.widgetFactory.treeItemLocalized(
                        item,
                        runningExamName.name);
                ActivitySelection.set(runningExams, Activity.RUNNING_EXAM.createSelection(runningExamName));
            }
        }
    }

    private void handleExpand(final Event event) {
        final TreeItem treeItem = (TreeItem) event.item;

        System.out.println("opened: " + treeItem);

        final ActivitySelection activity = ActivitySelection.get(treeItem);
        if (activity != null) {
            activity.processExpand(treeItem);
        }
    }

    private void handleSelection(final PageContext composerCtx, final Event event) {
        final TreeItem treeItem = (TreeItem) event.item;

        System.out.println("selected: " + treeItem);

        final MainPageState mainPageState = MainPageState.get();
        final ActivitySelection activitySelection = ActivitySelection.get(treeItem);
        if (mainPageState.activitySelection == null) {
            mainPageState.activitySelection = Activity.NONE.createSelection();
        }
        if (!mainPageState.activitySelection.equals(activitySelection)) {
            mainPageState.activitySelection = activitySelection;
            composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
        }
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
                    final MainPageState mainPageState = MainPageState.get();
                    mainPageState.activitySelection = ActivitySelection.get(newInstItem);
                    navigation.select(newInstItem);
                    expand(newInstItem);
                    composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
                } else if (event.actionDefinition == ActionDefinition.INSTITUTION_MODIFY) {
                    final IdAndName idAndName = (IdAndName) event.source;
                    final TreeItem selected = findItemByActivity(
                            navigation.getItems(),
                            Activity.INSTITUTION,
                            idAndName.id);
                    selected.setText(idAndName.name);
                } else if (event.actionDefinition == ActionDefinition.INSTITUTION_DELETE) {
                    final String id = (String) event.source;
                    final TreeItem institutions = findItemByActivity(navigation.getItems(), Activity.INSTITUTIONS);
                    final TreeItem selected = findItemByActivity(navigation.getItems(), Activity.INSTITUTION, id);
                    selected.dispose();
                    final MainPageState mainPageState = MainPageState.get();
                    mainPageState.activitySelection = ActivitySelection.get(institutions);
                    navigation.select(institutions);
                    composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
                }
            }
        };
    }

    private TreeItem createInstitutionItem(final Tree parent, final IdAndName idAndName) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(idAndName, institution);
        return institution;
    }

    private TreeItem createInstitutionItem(final TreeItem parent, final IdAndName idAndName) {
        final TreeItem institution = new TreeItem(parent, SWT.NONE);
        createInstitutionItem(idAndName, institution);
        return institution;
    }

    private void createInstitutionItem(final IdAndName idAndName, final TreeItem institution) {
        institution.setText(idAndName.name);
        ActivitySelection.set(institution, Activity.INSTITUTION.createSelection(idAndName));

//        final TreeItem lmsSetup = this.widgetFactory.treeItemLocalized(
//                institution,
//                "org.sebserver.activities.lms");
//        ActivitySelection.set(lmsSetup, Activity.LMS_SETUP.selection());

    }

    private static final TreeItem findItemByActivity(
            final TreeItem[] items,
            final Activity activity,
            final String objectId) {

        if (items == null) {
            return null;
        }

        for (final TreeItem item : items) {
            final ActivitySelection activitySelection = ActivitySelection.get(item);
            final String id = activitySelection.getObjectIdentifier();
            if (activitySelection != null && activitySelection.activity == activity &&
                    ((objectId == null && id == null) || objectId.equals(id))) {
                return item;
            }

            final TreeItem _item = findItemByActivity(item.getItems(), activity, objectId);
            if (_item != null) {
                return _item;
            }
        }

        return null;
    }

    private static final TreeItem findItemByActivity(final TreeItem[] items, final Activity activity) {
        return findItemByActivity(items, activity, null);
    }

    private static final void expand(final TreeItem item) {
        if (item == null) {
            return;
        }

        item.setExpanded(true);
        expand(item.getParentItem());
    }

}
