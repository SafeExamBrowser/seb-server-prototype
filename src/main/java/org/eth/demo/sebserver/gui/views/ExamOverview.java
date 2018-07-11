/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.exam.GUIExam;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.POSTExamStateChange;
import org.eth.demo.sebserver.gui.service.rest.GETExams;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ExamOverview implements ViewComposer {

    private static final String ITEM_DATA_EXAM = "ITEM_DATA_EXAM";
    private static final String ROOT_COMPOSITE_SUPPLIER = "ROOT_COMPOSITE_SUPPLIER";

    private final GETExams examsRequest;
    private final POSTExamStateChange examStateChange;
    private final ViewService viewService;

    public ExamOverview(
            final GETExams examsRequest,
            final POSTExamStateChange examStateChange,
            final ViewService viewService) {

        this.examsRequest = examsRequest;
        this.examStateChange = examStateChange;
        this.viewService = viewService;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return true;
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent,
            final Map<String, String> attributes) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final Collection<GUIExam> exams = this.examsRequest.doRequest();

        viewService.centringView(parent);

        final Group group = new Group(parent, SWT.SHADOW_NONE);
        group.setLayout(new RowLayout());
        group.setBounds(20, 20, 500, 100);
        group.setText(" Exam Overview ");

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
        table.getColumn(1).setWidth(200);
        table.getColumn(2).setWidth(200);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (final GUIExam exam : exams) {
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
        final GUIExam exam = (GUIExam) item.getData(ITEM_DATA_EXAM);
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
            final GUIExam newExam = this.examStateChange
                    .with()
                    .exam(examId)
                    .toState(toStateId)
                    .doRequest();
            tItem.setText(2, newExam.statusName);
            tItem.setData(ITEM_DATA_EXAM, newExam);
        });
    }

    private final void addViewRunningExamAction(final Menu menu, final String examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("View");
        item.addListener(SWT.Selection, event -> {

            final Map<String, String> attributes = new HashMap<>();
            attributes.put(AttributeKeys.EXAM_ID, examId);
            @SuppressWarnings("unchecked")
            final Supplier<Composite> rootCompositeSupplier =
                    (Supplier<Composite>) menu.getData(ROOT_COMPOSITE_SUPPLIER);
            this.viewService.composeView(
                    rootCompositeSupplier.get(),
                    RunningExamView.class, attributes);
        });
    }

}
