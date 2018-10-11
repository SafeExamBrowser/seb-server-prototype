/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eth.demo.sebserver.gui.domain.admin.InstitutionData;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.rest.GETInstitutionData;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class InstitutionForm implements TemplateComposer {

    private final RestServices restServices;
    private final WidgetFactory widgetFactory;

    public InstitutionForm(final RestServices restServices, final WidgetFactory widgetFactory) {
        this.restServices = restServices;
        this.widgetFactory = widgetFactory;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return attributes.containsKey(AttributeKeys.INSTITUTION_ID);
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {
        final String instId = composerCtx.attributes.get(AttributeKeys.INSTITUTION_ID);
        final InstitutionData institutionData = this.restServices.sebServerAPICall(GETInstitutionData.class)
                .attribute(AttributeKeys.INSTITUTION_ID, instId)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                });

        final Composite content = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout contentLayout = new GridLayout();
        contentLayout.marginLeft = 10;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label labelLocalized = this.widgetFactory.labelLocalized(
                content, "h2", new LocTextKey(
                        "org.sebserver.activities.instName",
                        institutionData.name));
        labelLocalized.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));

        final TabFolder tabs = new TabFolder(content, SWT.NONE);
        tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TabItem instTab = new TabItem(tabs, SWT.NONE);
        instTab.setText("General");
        final TabItem lmsSetupTab = new TabItem(tabs, SWT.NONE);
        lmsSetupTab.setText("LMS Setup(s)");

        final Composite institution = new Composite(tabs, SWT.NONE);
        final GridLayout layout = new GridLayout(4, true);
        layout.horizontalSpacing = 10;
        layout.verticalSpacing = 10;
        layout.marginLeft = 10;
        layout.marginTop = 10;
        institution.setLayout(layout);
        institution.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        instTab.setControl(institution);
        tabs.setSelection(0);

        final Composite setup = new Composite(tabs, SWT.NONE);
        final GridLayout setupLayout = new GridLayout(4, true);
        setupLayout.horizontalSpacing = 10;
        setupLayout.verticalSpacing = 10;
        setupLayout.marginLeft = 10;
        setup.setLayout(setupLayout);
        setup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        lmsSetupTab.setControl(setup);

        final Form form = new Form(institution, this.widgetFactory);

        this.widgetFactory.formLabelLocalized(institution, "org.sebserver.form.institution.name");
        this.widgetFactory.formTextInput(institution, institutionData.name, 2, 1);
        this.widgetFactory.formEmpty(institution);
        this.widgetFactory.formLabelLocalized(institution, "org.sebserver.form.institution.authType");
        final Combo authSelection = this.widgetFactory.formComboLocalized(
                institution,
                institutionData.authType,
                InstitutionData.AUTH_TYPE_SELECTION,
                2, 1);

        authSelection.addListener(SWT.Selection, e -> {
            final int selectionIndex = authSelection.getSelectionIndex();
            form.processSelection(selectionIndex);
        });

        this.widgetFactory.formEmpty(institution);

        form.addInputLDAP(
                "org.sebserver.form.institution.authType.ldap.url",
                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
        this.widgetFactory.formEmpty(institution);
        form.addInputLDAP(
                "org.sebserver.form.institution.authType.ldap.userDN",
                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
        this.widgetFactory.formEmpty(institution);
        form.addInputLDAP(
                "org.sebserver.form.institution.authType.ldap.userSearch",
                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
        this.widgetFactory.formEmpty(institution);
        form.addInputLDAP(
                "org.sebserver.form.institution.authType.ldap.groupSearch",
                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
        this.widgetFactory.formEmpty(institution);
        form.addInputLDAP(
                "org.sebserver.form.institution.authType.ldap.groupFilter",
                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));

        form.processSelection(authSelection.getSelectionIndex());
    }

    private static final class Form {

        final Collection<Control> ldapFormComponents = new ArrayList<>();
        final Collection<Control> switchedFormComponents = new ArrayList<>();
        final Composite parent;
        final WidgetFactory widgetFactory;

        public Form(final Composite parent, final WidgetFactory widgetFactory) {
            this.parent = parent;
            this.widgetFactory = widgetFactory;
        }

        public void addInputLDAP(final String label, final Supplier<Control> builder) {
            final Label urlLabel = this.widgetFactory.formLabelLocalized(this.parent, label);
            this.ldapFormComponents.add(urlLabel);
            this.switchedFormComponents.add(urlLabel);
            final Control control = builder.get();
            this.ldapFormComponents.add(control);
            this.switchedFormComponents.add(control);
        }

        public void processSelection(final int selectionIndex) {
            this.switchedFormComponents.forEach(c -> c.setVisible(false));
            if (selectionIndex == 1) {
                this.ldapFormComponents.forEach(c -> c.setVisible(true));
            }
        }
    }
}
