/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.domain.exam.ExamTableRow;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionPublishEvent;
import org.eth.demo.sebserver.gui.service.page.table.TableBuilder;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.exam.GetExams;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ExamsListPage implements TemplateComposer {

    private final I18nSupport i18nSupport;
    private final RestServices restServices;
    private final WidgetFactory widgetFactory;

    public ExamsListPage(final I18nSupport i18nSupport, final RestServices restServices,
            final WidgetFactory widgetFactory) {
        this.i18nSupport = i18nSupport;
        this.restServices = restServices;
        this.widgetFactory = widgetFactory;
    }

    @Override
    public void compose(final PageContext composerCtx) {

        final Composite content = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout contentLayout = new GridLayout();
        contentLayout.marginLeft = 10;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label labelLocalized = this.widgetFactory.labelLocalized(
                content, "h2", new LocTextKey("org.sebserver.activities.exams"));
        labelLocalized.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));

        createExamsTable(content);

        // publish possible actions for this page
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.EXAM_IMPORT,
                () -> {
                }));
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.EXAM_EDIT,
                () -> {
                }));
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.EXAM_DELETE,
                () -> {
                }));
    }

    private void createExamsTable(final Composite parent) {
        // get all exams for the current logged in user from the SEBServer Web-Service API
        final Collection<ExamTableRow> exams = this.restServices
                .sebServerCall(GetExams.class)
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        new TableBuilder<>(exams)
                .withColumn("Name", "The name of the Exam", 200, row -> row.name)
                .withColumn("Status", "The status of the Exam", 200, row -> row.status)
                .withColumn("Start Time", "The start time of the Exam (UTC)", 250,
                        row -> this.i18nSupport.formatDisplayDate(row.startTime))
                .withEmptyNote("No Exams Available")
                .compose(parent);

    }

}
