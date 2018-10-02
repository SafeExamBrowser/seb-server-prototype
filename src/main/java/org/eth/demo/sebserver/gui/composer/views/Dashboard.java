/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer.views;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eth.demo.sebserver.gui.composer.ExamTablePopupMenu;
import org.eth.demo.sebserver.gui.composer.SEBConfigTablePopupMenu;
import org.eth.demo.sebserver.gui.composer.StatusToolbarComposer;
import org.eth.demo.sebserver.gui.composer.TableBuilder;
import org.eth.demo.sebserver.gui.composer.TopToolbarComposer;
import org.eth.demo.sebserver.gui.composer.ViewComposer;
import org.eth.demo.sebserver.gui.domain.exam.ExamTableRow;
import org.eth.demo.sebserver.gui.domain.sebconfig.ConfigTableRow;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.rest.GETConfigs;
import org.eth.demo.sebserver.gui.service.rest.GETExams;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Dashboard implements ViewComposer {

    private final I18nSupport i18nSupport;
    private final TopToolbarComposer topToolbarComposer;
    private final ExamTablePopupMenu examTablePopupMenu;
    private final SEBConfigTablePopupMenu sebConfigTablePopupMenu;
    private final StatusToolbarComposer statusToolbarComposer;
    private final RestServices restServices;

    // TODO reduce dependencies
    public Dashboard(
            final RestServices restServices,
            final I18nSupport i18nSupport,
            final ExamTablePopupMenu examTablePopupMenu,
            final SEBConfigTablePopupMenu sebConfigTablePopupMenu,
            final TopToolbarComposer topToolbarComposer,
            final StatusToolbarComposer statusToolbarComposer) {

        this.i18nSupport = i18nSupport;
        this.topToolbarComposer = topToolbarComposer;
        this.examTablePopupMenu = examTablePopupMenu;
        this.sebConfigTablePopupMenu = sebConfigTablePopupMenu;
        this.statusToolbarComposer = statusToolbarComposer;
        this.restServices = restServices;
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

        parent.setLayout(new GridLayout());

        final Composite group = new Composite(parent, SWT.NONE);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        this.topToolbarComposer.compose(parent, group);

        final ExpandBar expandBar = new ExpandBar(group, SWT.BORDER | SWT.V_SCROLL);
        expandBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        expandBar.setSpacing(10);

        final ExpandItem configs = new ExpandItem(expandBar, SWT.NONE, 0);
        configs.setText("Configurations");
        final Composite configTableGroup = new Composite(expandBar, SWT.NONE);
        configTableGroup.setLayout(new GridLayout());
        createConfigsTable(parent, configTableGroup);
        configs.setControl(configTableGroup);
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

        this.statusToolbarComposer.compose(parent, group);
    }

    private void createConfigsTable(final Composite parent, final Composite examTableGroup) {
        // get all exams for the current logged in user from the SEBServer Web-Service API
        final Collection<ConfigTableRow> configs = this.restServices
                .sebServerCall(GETConfigs.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        new TableBuilder<>(configs)
                .withRowMenu(this.sebConfigTablePopupMenu)
                .withColumn("Name", "The name of the SEB-Configuration", 200, row -> row.name)
                .withColumn("Type", "The type of the SEB-Configuration", 200, row -> row.type)
                .withColumn("Latest Version", "The latest version of  of the SEB-Configuration", 200,
                        row -> row.latestVersion)
                .withEmptyNote("No Configurations Available")
                .compose(parent, examTableGroup);
    }

    private void createExamsTable(final Composite parent, final Composite examTableGroup) {
        // get all exams for the current logged in user from the SEBServer Web-Service API
        final Collection<ExamTableRow> exams = this.restServices
                .sebServerCall(GETExams.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        new TableBuilder<>(exams)
                .withRowMenu(this.examTablePopupMenu)
                .withColumn("Name", "The name of the Exam", 200, row -> row.name)
                .withColumn("Status", "The status of the Exam", 200, row -> row.status)
                .withColumn("Start Time", "The start time of the Exam (UTC)", 250,
                        row -> this.i18nSupport.formatDisplayDate(row.startTime))
                .withEmptyNote("No Exams Available")
                .compose(parent, examTableGroup);
    }

}
