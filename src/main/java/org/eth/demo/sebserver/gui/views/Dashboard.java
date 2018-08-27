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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eth.demo.sebserver.gui.I18nSupport;
import org.eth.demo.sebserver.gui.domain.exam.ExamStatus;
import org.eth.demo.sebserver.gui.domain.exam.ExamTableRow;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.GETConfigs;
import org.eth.demo.sebserver.gui.service.rest.GETExams;
import org.eth.demo.sebserver.gui.service.rest.POSTExamStateChange;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Dashboard implements ViewComposer {

    private static final String ITEM_DATA_EXAM = "ITEM_DATA_EXAM";
    private static final String ROOT_COMPOSITE_SUPPLIER = "ROOT_COMPOSITE_SUPPLIER";

    private final GETExams examsRequest;
    private final GETConfigs configsRequest;
    private final POSTExamStateChange examStateChange;
    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;
    private final I18nSupport i18nSupport;

    public Dashboard(
            final RestServices restServices,
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder,
            final I18nSupport i18nSupport) {

        this.examsRequest = restServices.getSEBServerAPICall(GETExams.class);
        this.configsRequest = restServices.getSEBServerAPICall(GETConfigs.class);
        this.examStateChange = restServices.getSEBServerAPICall(POSTExamStateChange.class);
        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
        this.i18nSupport = i18nSupport;
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

        parent.setLayout(new GridLayout());

        final Composite group = new Composite(parent, SWT.NONE);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        composeTopToolbar(viewService, parent, group);

        final ExpandBar expandBar = new ExpandBar(group, SWT.BORDER | SWT.V_SCROLL);
        expandBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        expandBar.setSpacing(8);

        final ExpandItem configs = new ExpandItem(expandBar, SWT.NONE, 0);
        configs.setText("Configurations");
        final Composite configTableGroup = new Composite(expandBar, SWT.NONE);
        configTableGroup.setLayout(new GridLayout());
        configs.setControl(configTableGroup);
        final Label todo = new Label(configTableGroup, SWT.NONE);
        todo.setText("TODO");
        configs.setHeight(configTableGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        configs.setExpanded(true);

        final ExpandItem exams = new ExpandItem(expandBar, SWT.NONE, 1);
        exams.setText("Exams");
        final Composite examTableGroup = new Composite(expandBar, SWT.NONE);
        examTableGroup.setLayout(new GridLayout());
        createExamsTable(parent, examTableGroup);
        exams.setControl(examTableGroup);
        exams.setHeight(examTableGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        exams.setExpanded(true);

        final ToolBar startusBar = new ToolBar(group, SWT.HORIZONTAL | SWT.RIGHT);
        final GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData2.heightHint = 30;
        startusBar.setLayoutData(gridData2);
        startusBar.setLayout(new GridLayout(3, true));

        final Label statusLabel = new Label(startusBar, SWT.NONE | SWT.NO_BACKGROUND);
        statusLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true));
        statusLabel.setBackground(new Color(parent.getDisplay(), new RGBA(255, 255, 255, 0)));
        statusLabel.setText("  You are logged in as : " + authorizationContext.getLoggedInUser().getName());
    }

    private void composeTopToolbar(
            final ViewService viewService,
            final Composite parent,
            final Composite group) {

        final ToolBar toolBar = new ToolBar(group, SWT.HORIZONTAL | SWT.RIGHT);
        final GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.heightHint = 50;
        toolBar.setLayoutData(gridData);
        toolBar.setLayout(new GridLayout(3, false));

        final Label titleLabel = new Label(toolBar, SWT.NONE | SWT.NO_BACKGROUND);
        titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        titleLabel.setBackground(new Color(group.getDisplay(), new RGBA(255, 255, 255, 0)));
        titleLabel.setText("  Dashboard : ");

        final ToolItem sep = new ToolItem(toolBar, SWT.SEPARATOR);
        sep.setWidth(150);

        final Menu newMenu = new Menu(group.getShell(), SWT.POP_UP);
        final MenuItem config = new MenuItem(newMenu, SWT.PUSH);
        config.setText("Configuration");
        config.addListener(SWT.Selection, event -> {
            System.out.println("TODO: new Configuration View");
        });
        final MenuItem exam = new MenuItem(newMenu, SWT.PUSH);
        exam.setText("Exam");
        exam.addListener(SWT.Selection, event -> {
            System.out.println("TODO: new Exam View");
        });

        final ToolItem newItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        newItem.setText("New");
        newItem.addListener(SWT.Selection, event -> {
            final Rectangle rect = newItem.getBounds();
            Point pt = new Point(rect.x, rect.y + rect.height);
            pt = toolBar.toDisplay(pt);
            newMenu.setLocation(pt.x, pt.y);
            newMenu.setVisible(true);
        });

        final Menu adminMenu = new Menu(group.getShell(), SWT.POP_UP);
        final MenuItem user = new MenuItem(adminMenu, SWT.PUSH);
        user.setText("User");
        user.addListener(SWT.Selection, event -> {
            System.out.println("TODO: go to user administration");
        });
        final MenuItem institutionMenuItem = new MenuItem(adminMenu, SWT.PUSH);
        institutionMenuItem.setText("Institution");
        institutionMenuItem.addListener(SWT.Selection, event -> {
            System.out.println("TODO: go to institution administration");
        });

        final ToolItem adminItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        adminItem.setText("  Administration");
        adminItem.addListener(SWT.Selection, event -> {
            final Rectangle rect = adminItem.getBounds();
            Point pt = new Point(rect.x, rect.y + rect.height);
            pt = toolBar.toDisplay(pt);
            adminMenu.setLocation(pt.x, pt.y);
            adminMenu.setVisible(true);
        });

        final Button logout = new Button(toolBar, SWT.NONE);
        logout.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true));
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
    }

//    private Table createConfigsTable(final Composite parent, final Composite group) {
//        // get all configs for the current logged in user from the SEBServer Web-Service API
//        final Collection<ConfigTableRow> exams = this.configsRequest
//                .with(this.authorizationContextHolder)
//                .doAPICall()
//                .onError(t -> {
//                    throw new RuntimeException(t);
//                }); // TODO error handling
//
//        final Table table = createTable(parent, group);
//
//        new TableColumn(table, SWT.NONE).setText("Name");
//        new TableColumn(table, SWT.NONE).setText("Status");
//        new TableColumn(table, SWT.NONE).setText("Start Time");
//        table.getColumn(0).setToolTipText("The name of the Exam");
//        table.getColumn(1).setToolTipText("The status of the Exam");
//        table.getColumn(2).setToolTipText("The start time of the Exam (UTC)");
//
//        table.setHeaderVisible(true);
//        table.setLinesVisible(true);
//
//        for (final ExamTableRow exam : exams) {
//            final TableItem item = new TableItem(table, SWT.NONE);
//            item.setText(0, exam.name);
//            item.setText(1, exam.status);
//            item.setText(2, this.i18nSupport.formatDisplayDate(exam.startTime));
//            item.setData(ITEM_DATA_EXAM, exam);
//            item.addListener(SWT.Selection, event -> {
//                System.out.println("table item selection: ");
//            });
//        }
//        // extra fill for testing
//        for (final ExamTableRow exam : exams) {
//            final TableItem item = new TableItem(table, SWT.NONE);
//            item.setText(0, exam.name);
//            item.setText(1, exam.status);
//            item.setText(2, exam.name);
//        }
//
//        for (final TableColumn column : table.getColumns()) {
//            column.pack();
//            if (column.getWidth() < 200) {
//                column.setWidth(200);
//            }
//        }
//
//        return table;
//    }

    private Table createExamsTable(final Composite parent, final Composite group) {
        // get all exams for the current logged in user from the SEBServer Web-Service API
        final Collection<ExamTableRow> exams = this.examsRequest
                .with(this.authorizationContextHolder)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        final Table table = createTable(parent, group);

        new TableColumn(table, SWT.NONE).setText("Name");
        new TableColumn(table, SWT.NONE).setText("Status");
        new TableColumn(table, SWT.NONE).setText("Start Time");
        table.getColumn(0).setToolTipText("The name of the Exam");
        table.getColumn(1).setToolTipText("The status of the Exam");
        table.getColumn(2).setToolTipText("The start time of the Exam (UTC)");

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (final ExamTableRow exam : exams) {
            final TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, exam.name);
            item.setText(1, exam.status);
            item.setText(2, this.i18nSupport.formatDisplayDate(exam.startTime));
            item.setData(ITEM_DATA_EXAM, exam);
            item.addListener(SWT.Selection, event -> {
                System.out.println("table item selection: ");
            });
        }

        for (final TableColumn column : table.getColumns()) {
            column.pack();
            if (column.getWidth() < 200) {
                column.setWidth(200);
            }
        }

        return table;
    }

    private Table createTable(final Composite parent, final Composite group) {
        final Table table = new Table(group, SWT.NO_SCROLL);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Menu menu = new Menu(table);
        menu.setData(
                ROOT_COMPOSITE_SUPPLIER,
                (Supplier<Composite>) () -> parent);
        table.setMenu(menu);
        table.addListener(
                SWT.MenuDetect,
                this::tableRowMenuEvent);
        return table;
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
        if (menu.getItemCount() > 0) {
            for (final MenuItem mItem : menu.getItems()) {
                mItem.dispose();
            }
        }

        final ExamTableRow exam = (ExamTableRow) item.getData(ITEM_DATA_EXAM);
        if (exam == null) {
            menu.setVisible(false);
            return;
        }

        final ExamStatus examStatus = exam.getExamStatus();
        if (examStatus == null) {
            menu.setVisible(false);
            return;
        }

        switch (examStatus) {
            case IN_PROGRESS: {
                addViewExamAction(menu, exam.id);
                if (exam.isOwner(this.authorizationContextHolder)) {
                    addEditAction(menu, exam.id);
                    addStateChangeAction("Set Ready", menu, item, exam.id, ExamStatus.READY);
                }
                break;
            }
            case READY: {
                addViewExamAction(menu, exam.id);
                if (exam.isOwner(this.authorizationContextHolder)) {
                    addStateChangeAction("Back To Edit", menu, item, exam.id, ExamStatus.IN_PROGRESS);
                    addStateChangeAction("Run", menu, item, exam.id, ExamStatus.RUNNING);
                }
                break;
            }
            case RUNNING: {
                addViewRunningExamAction(menu, exam.id);
                break;
            }
            default: {
                menu.setVisible(false);
            }
        }
    }

    private final void addViewExamAction(final Menu menu, final String examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("View");
        item.addListener(SWT.Selection, event -> {
            System.out.println("**************** TODO goto exam info page");
        });
    }

    private final void addEditAction(final Menu menu, final String examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("Edit");
        item.addListener(SWT.Selection, event -> {
            System.out.println("**************** TODO goto exam edit page");
        });
    }

    private final void addStateChangeAction(
            final String name,
            final Menu menu,
            final TableItem tItem,
            final String examId,
            final ExamStatus toState) {

        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText(name);
        item.addListener(SWT.Selection, event -> {

            final ExamTableRow newExam = this.examStateChange
                    .with(this.authorizationContextHolder)
                    .exam(examId)
                    .toState(toState.name())
                    .doAPICall()
                    .onError(t -> {
                        throw new RuntimeException(t);
                    }); // TODO error handling

            tItem.setText(2, newExam.status);
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
