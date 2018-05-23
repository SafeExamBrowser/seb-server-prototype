/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import org.eth.demo.sebserver.gui.GUISpringConfig;
import org.eth.demo.sebserver.gui.domain.GUIExam;
import org.eth.demo.sebserver.gui.domain.GUIIndicatorDef;
import org.eth.demo.sebserver.gui.domain.GUIIndicatorValue;
import org.eth.demo.sebserver.gui.domain.IndicatorValueMapping;
import org.eth.demo.sebserver.gui.push.ServerPushContext;
import org.eth.demo.sebserver.gui.push.ServerPushService;
import org.eth.demo.sebserver.gui.view.ViewComposer;
import org.eth.demo.sebserver.gui.view.ViewService;
import org.eth.demo.sebserver.util.TypedMap;
import org.eth.demo.sebserver.util.TypedMap.TypedKey;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public final class RunningExamView implements ViewComposer {

    public static final TypedKey<Long> EXAM_ID = new TypedKey<>("EXAM_ID", Long.class);

    private static final TypedKey<Table> CLIENT_TABLE = new TypedKey<>("CLIENT_TABLE", Table.class);
    private static final TypedKey<GUIExam> EXAM_DATA = new TypedKey<>("EXAM_DATA", GUIExam.class);
    private static final TypedKey<IndicatorValueMapping> INDICATOR_VALUES =
            new TypedKey<>("INDICATOR_VALUES", IndicatorValueMapping.class);

    private final ServerPushService serverPushService;

    public RunningExamView(final ServerPushService serverPushService) {
        this.serverPushService = serverPushService;
    }

    @Override
    public boolean validateAttributes(final TypedMap attributes) {
        return attributes.containsKey(EXAM_ID);
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent, final TypedMap attributes) {
        final Long examId = attributes.get(EXAM_ID);
        final GUIExam exam = requestExamData(examId);
        final Display display = parent.getDisplay();

        final RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        parent.setLayout(layout);

        final Label title = new Label(parent, SWT.BOLD);
        title.setText("Running Exam View");
        final FontData fontData = title.getFont().getFontData()[0];
        final Font boldFont = new Font(display,
                new FontData(fontData.getName(),
                        fontData.getHeight(), SWT.BOLD));
        title.setFont(boldFont);

        final Composite group = new Composite(parent, SWT.SHADOW_NONE);
        group.setBounds(20, 20, 200, 400);

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

        for (final GUIIndicatorDef indDef : exam.getIndicators()) {
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

        final Label clients = new Label(parent, SWT.NONE);
        clients.setText("Connected Clients:");
        clients.setFont(boldFont);

        final Table table = new Table(parent, SWT.NULL);
        final TableColumn t1c = new TableColumn(table, SWT.LEFT);
        t1c.setText("UUID");
        t1c.setWidth(200);
        for (final GUIIndicatorDef indDef : exam.getIndicators()) {
            final TableColumn tc = new TableColumn(table, SWT.LEFT);
            tc.setText(indDef.type);
            tc.setWidth(300);
        }

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.layout();

        final Button button = new Button(parent, SWT.FLAT);
        button.setText("Back to Home");
        button.addListener(SWT.Selection, event -> {
            viewService.composeView(parent, ExamOverview.class);
        });

        final ServerPushContext context = new ServerPushContext(title, runAgainContext -> true)
                .setData(EXAM_DATA, exam)
                .setData(CLIENT_TABLE, table);
        this.serverPushService.runServerPush(
                context,
                RunningExamView::pollData,
                RunningExamView::update);
    }

    private GUIExam requestExamData(final Long examId) {
        final RestTemplate restTemplate = new RestTemplate();
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(GUISpringConfig.ROOT_LOCATION + "/exam/" + examId);
        return restTemplate.getForObject(builder.toUriString(), GUIExam.class);
    }

    private final static void pollData(final ServerPushContext context) {
        try {
            Thread.sleep(500);
        } catch (final Exception e) {
        }

        final GUIExam exam = context.getData(EXAM_DATA);
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(GUISpringConfig.ROOT_LOCATION + "/exam/indicatorValues/" + exam.id);

        @SuppressWarnings("unchecked")
        final Collection<GUIIndicatorValue> indicatorValues =
                context.getRestTemplate().getForObject(builder.toUriString(), Collection.class);
        if (!indicatorValues.isEmpty()) {
            System.out.println("********************** indicatorValues: " + indicatorValues);
        }
        final IndicatorValueMapping indicatorValueMapping = new IndicatorValueMapping();
        for (final GUIIndicatorValue value : indicatorValues) {
            final Map<String, Float> computeIfAbsent = indicatorValueMapping.computeIfAbsent(
                    value.clientUUID,
                    uuid -> new LinkedHashMap<>());
            computeIfAbsent.put(value.type, value.value);
        }
        context.setData(INDICATOR_VALUES, indicatorValueMapping);

    }

    private final static void update(final ServerPushContext context) {
        final Table table = context.getData(CLIENT_TABLE);
        final IndicatorValueMapping indicatorValueMapping = context.getData(INDICATOR_VALUES);
        if (indicatorValueMapping == null || table == null) {
            return;
        }

        final Set<UUID> newClients = new HashSet<>(indicatorValueMapping.keySet());
        for (final TableItem item : table.getItems()) {
            final UUID uuid = UUID.fromString(item.getText(0));
            if (!indicatorValueMapping.containsKey(uuid)) {
                // client has no data anymore
                item.dispose();
                newClients.remove(uuid);
                continue;
            }
            final Map<String, Float> map = indicatorValueMapping.get(uuid);
            int i = 1;
            for (final Float value : map.values()) {
                item.setText(i, String.valueOf(value));
                i++;
            }

            newClients.remove(uuid);
        }

        if (!newClients.isEmpty()) {
            for (final UUID uuid : newClients) {
                final TableItem item = new TableItem(table, SWT.LEFT);
                item.setText(0, uuid.toString());
                final Map<String, Float> map = indicatorValueMapping.get(uuid);
                int i = 1;
                for (final Float value : map.values()) {
                    item.setText(i, String.valueOf(value));
                    i++;
                }
            }
        }
    }

}
