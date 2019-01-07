/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.exam.ConnectionRow;
import org.eth.demo.sebserver.gui.domain.exam.Indicator;
import org.eth.demo.sebserver.gui.domain.exam.RunningExam;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.push.ServerPushContext;
import org.eth.demo.sebserver.gui.service.push.ServerPushService;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.exam.GetConnectionInfo;
import org.eth.demo.sebserver.gui.service.rest.exam.GetRunningExamDetails;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class RunningExamPage implements TemplateComposer {

    private final ServerPushService serverPushService;
    private final RestServices restServices;
    private final WidgetFactory widgetFactory;

    public RunningExamPage(
            final RestServices restServices,
            final ServerPushService serverPushService,
            final WidgetFactory widgetFactory) {

        this.serverPushService = serverPushService;
        this.restServices = restServices;
        this.widgetFactory = widgetFactory;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return StringUtils.isNotBlank(attributes.get(AttributeKeys.EXAM_ID));
    }

    @Override
    public void compose(final PageContext composerCtx) {

        final String examId = composerCtx.attributes.get(AttributeKeys.EXAM_ID);
        final RunningExam exam = this.restServices
                .sebServerAPICall(GetRunningExamDetails.class)
                .exam(examId)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        final Display display = composerCtx.parent.getDisplay();

        final Composite content = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout contentLayout = new GridLayout();
        contentLayout.marginLeft = 10;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label title = this.widgetFactory.labelLocalized(
                content, "h2", new LocTextKey("org.sebserver.runningexam.page.title", exam.name));
        title.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));

        final Composite group = new Composite(content, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        group.setLayout(gridLayout);

//        final Label nameT = new Label(group, SWT.NULL);
//        nameT.setText("Name: ");
//        final Label name = new Label(group, SWT.NULL);
//        name.setText(exam.name);
//        final GridData gridData = new GridData();
//        gridData.horizontalAlignment = SWT.FILL;
//        gridData.horizontalSpan = 3;
//        name.setLayoutData(gridData);

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

        this.widgetFactory.labelLocalized(content, "Connected Clients:");

        final Composite tablePane = new Composite(content, SWT.NONE);
//        final ScrolledComposite tablePane = new ScrolledComposite(content, SWT.V_SCROLL);
//        tablePane.setExpandVertical(false);
//        tablePane.setAlwaysShowScrollBars(true);
        tablePane.setLayout(new GridLayout());
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 100;
        tablePane.setLayoutData(gridData);

        final ClientTable clientTable = new ClientTable(display, tablePane, exam);
//        tablePane.setContent(clientTable.table);

        this.serverPushService.runServerPush(
                new ServerPushContext(
                        content,
                        runAgainContext -> true),
                dataPoll(
                        this.restServices.sebServerAPICall(GetConnectionInfo.class),
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

        private int tableWidth = 0;

        ClientTable(final Display display, final Composite tableRoot, final RunningExam exam) {
            this.exam = exam;
            this.table = new Table(tableRoot, SWT.NONE);
            this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            this.table.setLayout(new GridLayout());

            final TableColumn t1c = new TableColumn(this.table, SWT.NONE);
            t1c.setText("Identifier");
            t1c.setAlignment(SWT.LEFT);
            final TableColumn t2c = new TableColumn(this.table, SWT.NONE);
            t2c.setText("Status");
            final TableColumn t3c = new TableColumn(this.table, SWT.NONE);
            t3c.setText("Address");
            for (final Indicator indDef : exam.getIndicators()) {
                final TableColumn tc = new TableColumn(this.table, SWT.NONE);
                tc.setText(indDef.name);
            }

            this.table.setHeaderVisible(true);
            this.table.setLinesVisible(true);

            this.color1 = new Color(display, new RGB(0, 255, 0), 100);
            this.color2 = new Color(display, new RGB(249, 166, 2), 100);
            this.color3 = new Color(display, new RGB(255, 0, 0), 100);

            this.tableMapping = new HashMap<>();
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

            adaptTableWidth();
        }

        private void adaptTableWidth() {
            final Rectangle area = this.table.getParent().getClientArea();
            if (this.tableWidth != area.width) {
                final int columnWidth = area.width / ClientTable.this.table.getColumnCount();
                for (final TableColumn column : ClientTable.this.table.getColumns()) {
                    column.setWidth(columnWidth);
                }
                ClientTable.this.table.layout(true, true);
                ClientTable.this.table.pack();
                this.tableWidth = area.width;
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
