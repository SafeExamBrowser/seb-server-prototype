/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.form;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormPostResponse;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormBuilder {

    private static final Logger log = LoggerFactory.getLogger(FormBuilder.class);

    private final WidgetFactory widgetFactory;
    public final PageContext composerCtx;
    public final Composite formParent;
    public final Form form;

    public FormBuilder(
            final WidgetFactory widgetFactory,
            final PageContext composerCtx,
            final int rows) {

        this.widgetFactory = widgetFactory;
        this.composerCtx = composerCtx;
        this.form = new Form();

        this.formParent = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout layout = new GridLayout(rows, true);
        layout.horizontalSpacing = 10;
        layout.verticalSpacing = 10;
        layout.marginLeft = 10;
        layout.marginTop = 10;
        this.formParent.setLayout(layout);
        this.formParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    public FormBuilder setControl(final TabItem instTab) {
        instTab.setControl(this.formParent);
        return this;
    }

    public FormBuilder addEmptyCell() {
        return addEmptyCell(1);
    }

    public FormBuilder addEmptyCell(final int span) {
        this.widgetFactory.formEmpty(this.formParent, span, 1);
        return this;
    }

    public FormBuilder putStaticValue(final String name, final String value) {
        try {
            this.form.putStatic(name, value);
        } catch (final Exception e) {
            log.error("Failed to put static field value to json object: ", e);
        }
        return this;
    }

    public FormBuilder addTextField(
            final String name,
            final String label,
            final String value) {

        return addTextField(name, label, value, 1, null);
    }

    public FormBuilder addTextField(
            final String name,
            final String label,
            final String value,
            final int span) {

        return addTextField(name, label, value, span, null);
    }

    public FormBuilder addTextField(
            final String name,
            final String label,
            final String value,
            final int span,
            final String group) {

        this.widgetFactory.formLabelLocalized(this.formParent, label);
        this.form.putField(name, this.widgetFactory.formTextInput(this.formParent, value, span, 1));
        if (StringUtils.isNoneBlank(group)) {
            this.form.addToGroup(group, name);
        }
        return this;
    }

    public FormBuilder addSingleSelection(
            final String name,
            final String label,
            final String value,
            final List<Tuple<String>> items,
            final Consumer<Form> selectionListener) {

        return addSingleSelection(name, label, value, items, selectionListener, 1, null);
    }

    public FormBuilder addSingleSelection(
            final String name,
            final String label,
            final String value,
            final List<Tuple<String>> items,
            final Consumer<Form> selectionListener,
            final int span) {

        return addSingleSelection(name, label, value, items, selectionListener, span, null);
    }

    public FormBuilder addSingleSelection(
            final String name,
            final String label,
            final String value,
            final List<Tuple<String>> items,
            final Consumer<Form> selectionListener,
            final int span,
            final String group) {

        this.widgetFactory.formLabelLocalized(this.formParent, label);
        final Combo selection = this.widgetFactory.formComboLocalized(this.formParent, value, items, span, 1);
        if (selectionListener != null) {
            selection.addListener(SWT.Selection, e -> {
                selectionListener.accept(this.form);
            });
        }
        if (StringUtils.isNoneBlank(group)) {
            this.form.addToGroup(group, name);
        }
        return this;
    }

    public FormHandle buildFor(final APICallBuilder<FormPostResponse> post) {
        return new FormHandle(this.composerCtx, this.form, post);
    }

}
