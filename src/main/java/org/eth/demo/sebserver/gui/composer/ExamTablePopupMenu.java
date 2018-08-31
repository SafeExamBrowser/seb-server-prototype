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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
public class ExamTablePopupMenu extends AbstractTableRowMenuPopup<ExamTableRow> {

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
    protected final void composeMenu(final Menu menu, final TableItem item, final ExamTableRow rowData) {
        final ExamStatus examStatus = rowData.getExamStatus();
        if (examStatus == null) {
            menu.setVisible(false);
            return;
        }

        switch (examStatus) {
            case IN_PROGRESS: {
                addViewExamAction(menu, rowData.id);
                if (rowData.isOwner(this.authorizationContextHolder)) {
                    addEditAction(menu, rowData.id);
                    addStateChangeAction("Set Ready", menu, item, rowData.id, ExamStatus.READY);
                }
                break;
            }
            case READY: {
                addViewExamAction(menu, rowData.id);
                if (rowData.isOwner(this.authorizationContextHolder)) {
                    addStateChangeAction("Back To Edit", menu, item, rowData.id, ExamStatus.IN_PROGRESS);
                    addStateChangeAction("Run", menu, item, rowData.id, ExamStatus.RUNNING);
                }
                break;
            }
            case RUNNING: {
                addViewRunningExamAction(menu, rowData.id);
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
