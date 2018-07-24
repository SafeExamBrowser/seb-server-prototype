/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.exam.RunningExam;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.GETExams;
import org.eth.demo.sebserver.gui.service.rest.POSTExamStateChange;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ExamOverview implements ViewComposer {

    private static final String ITEM_DATA_EXAM = "ITEM_DATA_EXAM";
    private static final String ROOT_COMPOSITE_SUPPLIER = "ROOT_COMPOSITE_SUPPLIER";

    private final GETExams examsRequest;
    private final POSTExamStateChange examStateChange;
    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public ExamOverview(
            final GETExams examsRequest,
            final POSTExamStateChange examStateChange,
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.examsRequest = examsRequest;
        this.examStateChange = examStateChange;
        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return true;
    }

    @Override
    public void composeView(
            final ViewService viewService,
            final Composite parent,
            final Map<String, String> attributes) {

        final SEBServerAuthorizationContext authorizationContext = this.authorizationContextHolder
                .getAuthorizationContext();

        viewService.centringView(parent);

        final Group group = new Group(parent, SWT.SHADOW_NONE);
        group.setLayout(new RowLayout(SWT.VERTICAL));
        group.setLayoutData(new RowData(800, 500));
        group.setText(" Dashboard ");

        final Composite titleGroup = new Composite(group, SWT.SHADOW_NONE);
        titleGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
        titleGroup.setLayoutData(new RowData(800, 50));

        final Label wellcome = new Label(titleGroup, SWT.NULL);
        wellcome.setText("Wellcome: " + authorizationContext.getLoggedInUser().getName());
        wellcome.setLayoutData(new RowData(550, 50));

        final Button admin = new Button(titleGroup, SWT.NONE);
        admin.setText("Administration");
        admin.addListener(SWT.Selection, event -> {
            System.out.println("***** TODO Admin Section");
        });
        admin.setVisible(authorizationContext.hasRole("ADMIN_USER"));

        final Button logout = new Button(titleGroup, SWT.NONE);
        logout.setText("Logout");
        logout.addListener(SWT.Selection, event -> {
            final boolean logoutSuccessful = this.authorizationContextHolder
                    .getAuthorizationContext()
                    .logout();

            if (!logoutSuccessful) {
                // TODO error handling
            }

            viewService
                    .createViewOn(parent)
                    .attribute(AttributeKeys.LGOUT_SUCCESS, "true")
                    .compose(ViewService.LOGIN_PAGE);
        });

        final Label examTableTitle = new Label(group, SWT.NULL);
        examTableTitle.setText("Exams: ");

        createExamsTable(parent, group);
    }

    private void createExamsTable(final Composite parent, final Group group) {
        // get all exams for the current logged in user from the SEBServer Web-Service API
        final Collection<RunningExam> exams = this.examsRequest
                .with(this.authorizationContextHolder)
                .doAPICall()
                .orElse(t -> t.printStackTrace()); // TODO error handling

        final Table table = new Table(group, SWT.NULL);
        final Menu menu = new Menu(table);
        menu.setData(
                ROOT_COMPOSITE_SUPPLIER,
                (Supplier<Composite>) () -> parent);
        table.setMenu(menu);
        table.addListener(
                SWT.MenuDetect,
                this::tableRowMenuEvent);

        new TableColumn(table, SWT.LEFT).setText("Identifier");
        new TableColumn(table, SWT.LEFT).setText("Name");
        new TableColumn(table, SWT.LEFT).setText("Status");
        table.getColumn(0).setWidth(100);
        table.getColumn(1).setWidth(500);
        table.getColumn(2).setWidth(180);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (final RunningExam exam : exams) {
            final TableItem item = new TableItem(table, SWT.RIGHT);
            item.setText(0, String.valueOf(exam.id));
            item.setText(1, exam.name);
            item.setText(2, exam.statusName);
            item.setData(ITEM_DATA_EXAM, exam);
            item.addListener(SWT.Selection, event -> {
                System.out.println("table item selection: ");
            });
        }

        table.layout();
    }

    private final void tableRowMenuEvent(final Event event) {
        final Table table = (Table) event.widget;
        final TableItem[] selection = table.getSelection();
        if (selection != null && selection.length > 0) {
            final Rectangle bounds = getRealBounds(selection[0]);
            if (bounds.contains(event.x, event.y)) {
                composeExamMenu(table.getMenu(), selection[0]);
            } else {
                table.deselectAll();
            }
        }
    }

    private final Rectangle getRealBounds(final TableItem item) {
        Rectangle result = item.getBounds();
        for (int i = 0; i < item.getParent().getColumnCount(); i++) {
            result = result.union(item.getBounds(i));
        }
        Composite parent = item.getParent();
        while (parent != null) {
            result.x += parent.getBounds().x;
            result.y += parent.getBounds().y;
            parent = parent.getParent();
        }
        return result;
    }

    private final void composeExamMenu(final Menu menu, final TableItem item) {
        final RunningExam exam = (RunningExam) item.getData(ITEM_DATA_EXAM);
        if (menu.getItemCount() > 0) {
            for (final MenuItem mItem : menu.getItems()) {
                mItem.dispose();
            }
        }

        switch (exam.status.intValue()) {
            case 0: {
                addEditAction(menu, exam.id);
                addStateChangeAction("Set Ready", menu, item, String.valueOf(exam.id), "1");
                break;
            }
            case 1: {
                addStateChangeAction("Back To Edit", menu, item, String.valueOf(exam.id), "0");
                addStateChangeAction("Run", menu, item, String.valueOf(exam.id), "2");
                break;
            }
            case 2: {
                addViewRunningExamAction(menu, String.valueOf(exam.id));
                break;
            }
            default: {
            }
        }
    }

    private final void addEditAction(final Menu menu, final Long examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("Edit");
        item.addListener(SWT.Selection, event -> {
            System.out.println("******************* edit " + examId);
        });
    }

    private final void addStateChangeAction(
            final String name,
            final Menu menu,
            final TableItem tItem,
            final String examId,
            final String toStateId) {

        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText(name);
        item.addListener(SWT.Selection, event -> {

            final RunningExam newExam = this.examStateChange
                    .with(this.authorizationContextHolder)
                    .exam(examId)
                    .toState(toStateId)
                    .doAPICall()
                    .orElse(t -> t.printStackTrace()); // TODO error handling

            tItem.setText(2, newExam.statusName);
            tItem.setData(ITEM_DATA_EXAM, newExam);
        });
    }

    private final void addViewRunningExamAction(final Menu menu, final String examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("View");
        item.addListener(SWT.Selection, event -> {

            @SuppressWarnings("unchecked")
            final Supplier<Composite> rootCompositeSupplier =
                    (Supplier<Composite>) menu.getData(ROOT_COMPOSITE_SUPPLIER);
            this.viewService
                    .createViewOn(rootCompositeSupplier.get())
                    .exam(examId)
                    .compose(RunningExamView.class);
        });
    }

}
