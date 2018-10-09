/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.composer.ViewComposer;
import org.eth.demo.sebserver.gui.domain.exam.ConnectionRow;
import org.eth.demo.sebserver.gui.domain.exam.Indicator;
import org.eth.demo.sebserver.gui.domain.exam.RunningExam;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.push.ServerPushContext;
import org.eth.demo.sebserver.gui.service.push.ServerPushService;
import org.eth.demo.sebserver.gui.service.rest.GETConnectionInfo;
import org.eth.demo.sebserver.gui.service.rest.GETRunningExamDetails;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class RunningExamView implements ViewComposer {

    private final ServerPushService serverPushService;
    private final RestServices restServices;

    public RunningExamView(
            final RestServices restServices,
            final ServerPushService serverPushService) {

        this.serverPushService = serverPushService;
        this.restServices = restServices;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return attributes.containsKey(AttributeKeys.EXAM_ID);
    }

    @Override
    public void composeView(
            final ViewService viewService,
            final Composite parent,
            final Map<String, String> attributes) {

        final String examId = attributes.get(AttributeKeys.EXAM_ID);
        final RunningExam exam = this.restServices
                .sebServerAPICall(GETRunningExamDetails.class)
                .exam(examId)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        final Display display = parent.getDisplay();
        viewService.centringView(parent);
        final Composite root = new Composite(parent, SWT.NONE);
        root.setLayoutData(new RowData(800, 400));

        final RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        root.setLayout(layout);

        final Label title = new Label(root, SWT.BOLD);
        title.setText("Running Exam View");
        final FontData fontData = title.getFont().getFontData()[0];
        final Font boldFont = new Font(display,
                new FontData(fontData.getName(),
                        fontData.getHeight(), SWT.BOLD));
        title.setFont(boldFont);

        final Composite group = new Composite(root, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        group.setLayout(gridLayout);
        final Label nameT = new Label(group, SWT.NULL);
        nameT.setText("Name: ");
        final Label name = new Label(group, SWT.NULL);
        name.setText(exam.name);
        final GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.horizontalSpan = 3;
        name.setLayoutData(gridData);

        for (final Indicator indDef : exam.getIndicators()) {
            final Label indName = new Label(group, SWT.NULL);
            indName.setText(indDef.type);
            final Label indT1 = new Label(group, SWT.NULL);
            indT1.setText("Threshold1: " + String.valueOf(indDef.threshold1));
            indT1.setBackground(new Color(display, new RGB(0, 255, 0), 100));
            final Label indT2 = new Label(group, SWT.NULL);
            indT2.setText("Threshold2: " + String.valueOf(indDef.threshold2));
            indT2.setBackground(new Color(display, new RGB(249, 166, 2), 100));
            final Label indT3 = new Label(group, SWT.NULL);
            indT3.setText("Threshold3: " + String.valueOf(indDef.threshold3));
            indT3.setBackground(new Color(display, new RGB(255, 0, 0), 100));
        }

        final Label clients = new Label(root, SWT.NONE);
        clients.setText("Connected Clients:");
        clients.setFont(boldFont);

        final ScrolledComposite tablePane = new ScrolledComposite(root, SWT.V_SCROLL);
        tablePane.setExpandVertical(true);
        tablePane.setAlwaysShowScrollBars(true);
        tablePane.setLayout(new RowLayout());
        tablePane.setLayoutData(new RowData(800, 200));

        final ClientTable clientTable = new ClientTable(display, tablePane, exam);
        tablePane.setContent(clientTable.table);

        final Button button = new Button(root, SWT.FLAT);
        button.setText("To Exam Overview");
        button.addListener(SWT.Selection, event -> {
            viewService.composeView(parent, Dashboard.class);
        });

        this.serverPushService.runServerPush(
                new ServerPushContext(
                        root,
                        runAgainContext -> true),
                dataPoll(
                        this.restServices.sebServerAPICall(GETConnectionInfo.class),
                        clientTable),
                context -> {
                    clientTable.updateGUI();
                    context.layout();
                });
    }

    private static final Consumer<ServerPushContext> dataPoll(
            final APICallBuilder<List<ConnectionRow>> restCallBuilder,
            final ClientTable clientTable) {

        return context -> {
            try {
                Thread.sleep(100);
            } catch (final Exception e) {
            }

            restCallBuilder.clear();
            final List<ConnectionRow> connectionInfo = restCallBuilder
                    .exam(String.valueOf(clientTable.exam.id))
                    .doAPICall()
                    .onError(t -> {
                        throw new RuntimeException(t);
                    }); // TODO error handling

            clientTable.updateValues(connectionInfo);
        };
    }

    private final static class ClientTable {

        final RunningExam exam;
        Table table;

        final Color color1;
        final Color color2;
        final Color color3;

        final Map<String, UpdatableTableItem> tableMapping;

        ClientTable(final Display display, final Composite tableRoot, final RunningExam exam) {
            this.exam = exam;
            this.table = new Table(tableRoot, SWT.NONE);
            final TableColumn t1c = new TableColumn(this.table, SWT.NONE);
            t1c.setText("Identifier");
            t1c.setWidth(200);
            final TableColumn t2c = new TableColumn(this.table, SWT.NONE);
            t2c.setText("Status");
            t2c.setWidth(150);
            final TableColumn t3c = new TableColumn(this.table, SWT.NONE);
            t3c.setText("Address");
            t3c.setWidth(150);
            for (final Indicator indDef : exam.getIndicators()) {
                final TableColumn tc = new TableColumn(this.table, SWT.NONE);
                tc.setText(indDef.name);
                tc.setWidth(100);
            }

            this.table.setHeaderVisible(true);
            this.table.setLinesVisible(true);
            this.table.layout();

            this.color1 = new Color(display, new RGB(0, 255, 0), 100);
            this.color2 = new Color(display, new RGB(249, 166, 2), 100);
            this.color3 = new Color(display, new RGB(255, 0, 0), 100);

            this.tableMapping = new HashMap<>();

            this.table.pack();
            this.table.layout();
        }

        void updateValues(final List<ConnectionRow> connectionInfo) {
            for (final ConnectionRow row : connectionInfo) {
                final UpdatableTableItem tableItem = this.tableMapping.computeIfAbsent(
                        row.username,
                        (userIdentifier -> new UpdatableTableItem(this.table, row.username)));
                tableItem.push(row);
            }
        }

        void updateGUI() {
            for (final UpdatableTableItem uti : this.tableMapping.values()) {
                if (uti.tableItem == null) {
                    uti.tableItem = new TableItem(this.table, SWT.NONE);
                    uti.tableItem.setText(0, uti.username);
                    uti.tableItem.setText(1, uti.connectionRow.status);
                    uti.tableItem.setText(2, uti.connectionRow.clientAddress);
                    updateIndicatorValues(uti);
                    updateConnectionStatusColor(uti);
                } else {
                    if (!uti.connectionRow.status.equals(uti.previous_connectionRow.status)) {
                        uti.tableItem.setText(0, uti.connectionRow.username);
                        uti.tableItem.setText(1, uti.connectionRow.status);
                        updateConnectionStatusColor(uti);
                    }
                    if ("ESTABLISHED".equals(uti.connectionRow.status)) {
                        updateIndicatorValues(uti);
                    }
                }
                uti.tableItem.getDisplay();
            }
        }

        private void updateIndicatorValues(final UpdatableTableItem uti) {
            for (final ConnectionRow.IndicatorValue iv : uti.connectionRow.indicatorValues) {
                final int indicatorIndex = this.exam.getIndicatorIndex(iv.type);
                final int columnIndex = indicatorIndex + 3;
                uti.tableItem.setText(columnIndex, String.valueOf(iv.value));
                uti.tableItem.setBackground(
                        columnIndex,
                        this.getColorForValue(indicatorIndex, iv.value));
            }
        }

        private void updateConnectionStatusColor(final UpdatableTableItem uti) {
            Color color = null;
            if ("ESTABLISHED".equals(uti.connectionRow.status)) {
                color = this.color1;
            } else if ("CLOSED".equals(uti.connectionRow.status)) {
                color = this.color2;
            } else if ("ABORTED".equals(uti.connectionRow.status)) {
                color = this.color3;
            }
            if (color != null) {
                uti.tableItem.setBackground(1, color);
            }
        }

        private Color getColorForValue(final int indicatorIndex, final double value) {
            final Indicator indicator = this.exam.getIndicator(indicatorIndex);
            if (value >= indicator.threshold3) {
                return this.color3;
            } else if (value >= indicator.threshold2) {
                return this.color2;
            } else {
                return this.color1;
            }
        }
    }

    private static final class UpdatableTableItem {

        final String username;
        TableItem tableItem;
        ConnectionRow previous_connectionRow;
        ConnectionRow connectionRow;

        UpdatableTableItem(final Table parent, final String username) {
            this.tableItem = null;
            this.username = username;
        }

        public void push(final ConnectionRow connectionRow) {
            if (!this.username.equals(connectionRow.username)) {
                throw new IllegalArgumentException("UserIdentifier mismatch");
            }
            this.previous_connectionRow = this.connectionRow;
            this.connectionRow = connectionRow;
        }
    }
}
