/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.exam.RunningExam;
import org.eth.demo.sebserver.gui.domain.exam.Indicator;
import org.eth.demo.sebserver.gui.domain.exam.GUIIndicatorValue;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.push.ServerPushContext;
import org.eth.demo.sebserver.gui.service.push.ServerPushService;
import org.eth.demo.sebserver.gui.service.rest.GETExamDetail;
import org.eth.demo.sebserver.gui.service.rest.GETIndicatorValues;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class RunningExamView implements ViewComposer {

    private final ServerPushService serverPushService;
    private final GETExamDetail examDetailRequest;
    private final GETIndicatorValues indicatorValuesRequest;
    private final AuthorizationContextHolder authorizationContextHolder;

    public RunningExamView(
            final ServerPushService serverPushService,
            final GETExamDetail examDetailRequest,
            final GETIndicatorValues indicatorValuesRequest,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.serverPushService = serverPushService;
        this.examDetailRequest = examDetailRequest;
        this.indicatorValuesRequest = indicatorValuesRequest;
        this.authorizationContextHolder = authorizationContextHolder;
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
        final RunningExam exam = this.examDetailRequest
                .with(this.authorizationContextHolder)
                .exam(examId)
                .doAPICall()
                .orElse(t -> t.printStackTrace()); // TODO error handling

        final Display display = parent.getDisplay();

        viewService.centringView(parent);

        final Composite root = new Composite(parent, SWT.SHADOW_NONE);

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

        final Composite group = new Composite(root, SWT.SHADOW_NONE);
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

        final ClientTable clientTable = new ClientTable(display, root, exam);

        final Button button = new Button(root, SWT.FLAT);
        button.setText("To Exam Overview");
        button.addListener(SWT.Selection, event -> {
            viewService.composeView(parent, ExamOverview.class);
        });

        this.serverPushService.runServerPush(
                new ServerPushContext(
                        root,
                        runAgainContext -> true),
                dataPoll(
                        this.indicatorValuesRequest.with(this.authorizationContextHolder),
                        clientTable),
                context -> {
                    clientTable.updateGUI();
                    context.layout();
                });
    }

    private static final Consumer<ServerPushContext> dataPoll(
            final APICallBuilder<List<GUIIndicatorValue>> restCallBuilder,
            final ClientTable clientTable) {

        return context -> {
            try {
                Thread.sleep(100);
            } catch (final Exception e) {
            }

            restCallBuilder.clear();
            final List<GUIIndicatorValue> indicatorValues = restCallBuilder
                    .exam(String.valueOf(clientTable.exam.id))
                    .doAPICall()
                    .orElse(t -> t.printStackTrace()); // TODO error handling

            clientTable.updateValues(indicatorValues);
        };
    }

    private final static class ClientTable {

        final RunningExam exam;
        Table table;

        final Color color1;
        final Color color2;
        final Color color3;

        final Set<UUID> toRemove;
        final Map<UUID, float[]> indicatorValues;
        final Map<UUID, UpdatableTableItem> tableMapping;

        ClientTable(final Display display, final Composite tableRoot, final RunningExam exam) {
            this.exam = exam;
            this.toRemove = new HashSet<>();
            this.indicatorValues = new HashMap<>();

            this.table = new Table(tableRoot, SWT.NULL);
            final TableColumn t1c = new TableColumn(this.table, SWT.NONE);
            t1c.setText("UUID");
            t1c.setWidth(300);
            for (final Indicator indDef : exam.getIndicators()) {
                final TableColumn tc = new TableColumn(this.table, SWT.NONE);
                tc.setText(indDef.type);
                tc.setWidth(200);
            }

            this.table.setHeaderVisible(true);
            this.table.setLinesVisible(true);
            this.table.layout();

            this.color1 = new Color(display, new RGB(0, 255, 0), 100);
            this.color2 = new Color(display, new RGB(249, 166, 2), 100);
            this.color3 = new Color(display, new RGB(255, 0, 0), 100);

            this.tableMapping = new HashMap<>();
        }

        void updateValues(final List<GUIIndicatorValue> indicatorValues) {
            this.toRemove.addAll(this.indicatorValues.keySet());
            for (final GUIIndicatorValue value : indicatorValues) {
                final float[] valueMapping = this.indicatorValues.computeIfAbsent(
                        value.clientUUID,
                        uuid -> new float[this.exam.getNumberOfIndicators()]);

                final int indicatorIndex = this.exam.getIndicatorIndex(value.type);
                valueMapping[indicatorIndex] = value.value;
                if (!this.tableMapping.containsKey(value.clientUUID)) {
                    this.tableMapping.put(
                            value.clientUUID,
                            new UpdatableTableItem(this.table, value.clientUUID));
                } else {
                    this.tableMapping.get(value.clientUUID).needsUpdate.set(indicatorIndex);
                }
                this.toRemove.remove(value.clientUUID);
            }

            this.indicatorValues.keySet()
                    .removeAll(this.toRemove);
        }

        void updateGUI() {
            final Iterator<UpdatableTableItem> iterator = this.tableMapping.values().iterator();
            while (iterator.hasNext()) {
                final UpdatableTableItem item = iterator.next();
                item.update(this.table);

                if (!this.indicatorValues.containsKey(item.clientId)) {
                    item.tableItem.dispose();
                    iterator.remove();
                } else {
                    final float[] values = this.indicatorValues.get(item.clientId);
                    for (int i = item.needsUpdate.nextSetBit(0); i >= 0; i = item.needsUpdate.nextSetBit(i + 1)) {
                        item.tableItem.setText(i + 1, String.valueOf(values[i]));
                        item.tableItem.setBackground(i + 1, getColorForValue(i, values[i]));
                    }

                }

                item.needsUpdate.clear();
            }

            this.table.pack();
            this.table.layout();
        }

        private Color getColorForValue(final int indicatorIndex, final float value) {
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

        final BitSet needsUpdate = new BitSet();
        final UUID clientId;
        TableItem tableItem;

        UpdatableTableItem(final Table parent, final UUID clientId) {
            this.tableItem = null;
            this.clientId = clientId;
        }

        void update(final Table parent) {
            if (this.tableItem == null) {
                this.tableItem = new TableItem(parent, SWT.NONE);
                this.tableItem.setText(0, this.clientId.toString());
                this.needsUpdate.set(0);
                this.needsUpdate.set(1);
            }
        }
    }

}
