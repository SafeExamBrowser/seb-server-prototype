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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
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
    private final PolyglotPageService polyglotPageService;
    public final PageContext composerCtx;
    public final Composite formParent;
    public final Form form;

    private boolean readonly = false;

    FormBuilder(
            final WidgetFactory widgetFactory,
            final PolyglotPageService polyglotPageService,
            final PageContext composerCtx,
            final int rows) {

        this.widgetFactory = widgetFactory;
        this.polyglotPageService = polyglotPageService;
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

    public FormBuilder readonly(final boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormBuilder setVisible(final boolean visible, final String group) {
        this.form.setVisible(visible, group);
        return this;
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

        final Label lab = this.widgetFactory.formLabelLocalized(this.formParent, label);
        if (this.readonly) {
            this.form.putField(name, lab, this.widgetFactory.formValueLabel(this.formParent, value, span));
        } else {
            this.form.putField(name, lab, this.widgetFactory.formTextInput(this.formParent, value, span, 1));
        }
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

        final Label lab = this.widgetFactory.formLabelLocalized(this.formParent, label);
        if (this.readonly) {
            this.form.putField(name, lab, this.widgetFactory.formValueLabel(this.formParent, value, 2));
        } else {
            final Combo selection =
                    this.widgetFactory.formSingleSelectionLocalized(this.formParent, value, items, span, 1);
            this.form.putField(name, lab, selection);
            if (selectionListener != null) {
                selection.addListener(SWT.Selection, e -> {
                    selectionListener.accept(this.form);
                });
            }
        }
        if (StringUtils.isNoneBlank(group)) {
            this.form.addToGroup(group, name);
        }
        return this;
    }

    public <T> FormHandle<T> buildFor(final APICallBuilder<FormPostResponse<T>> post) {
        return new FormHandle<>(this.composerCtx, this.form, post, this.polyglotPageService.getI18nSupport());
    }

}
