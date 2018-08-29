/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.composer.views.RunningExamView;
import org.eth.demo.sebserver.gui.domain.exam.ExamStatus;
import org.eth.demo.sebserver.gui.domain.exam.ExamTableRow;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.POSTExamStateChange;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ExamTablePopupMenu implements PopupMenuComposer {

    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;
    private final POSTExamStateChange examStateChange;

    public ExamTablePopupMenu(
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder,
            final POSTExamStateChange examStateChange) {

        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
        this.examStateChange = examStateChange;
    }

    @Override
    public final void onMenuEvent(final Event event) {
        final Table table = (Table) event.widget;
        final TableItem[] selection = table.getSelection();
        if (selection != null && selection.length > 0) {
            final Rectangle bounds = getRealBounds(selection[0]);
            final Menu menu = table.getMenu();
            if (menu.getItemCount() > 0) {
                for (final MenuItem mItem : menu.getItems()) {
                    mItem.dispose();
                }
            }
            if (bounds.contains(event.x, event.y)) {
                composeExamMenu(menu, selection[0]);
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
        final ExamTableRow exam = (ExamTableRow) item.getData(TableBuilder.TABLE_ROW_DATA);
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
            tItem.setData(TableBuilder.TABLE_ROW_DATA, newExam);
        });
    }

    private final void addViewRunningExamAction(final Menu menu, final String examId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("View");
        item.addListener(SWT.Selection, event -> {

            @SuppressWarnings("unchecked")
            final Supplier<Composite> rootCompositeSupplier =
                    (Supplier<Composite>) menu.getData(TableBuilder.ROOT_COMPOSITE_SUPPLIER);
            this.viewService
                    .createViewOn(rootCompositeSupplier.get())
                    .exam(examId)
                    .compose(RunningExamView.class);
        });
    }

}
