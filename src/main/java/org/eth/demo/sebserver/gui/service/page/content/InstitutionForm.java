/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.domain.admin.InstitutionData;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.page.action.ActionPublishEvent;
import org.eth.demo.sebserver.gui.service.page.form.FormBuilder;
import org.eth.demo.sebserver.gui.service.page.form.FormHandle;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.institution.DeleteInstitution;
import org.eth.demo.sebserver.gui.service.rest.institution.GetInstitutionData;
import org.eth.demo.sebserver.gui.service.rest.institution.InstitutionFormPost;
import org.eth.demo.sebserver.gui.service.rest.institution.NewInstitution;
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
        return StringUtils.isNoneBlank(attributes.get(AttributeKeys.INSTITUTION_ID));
    }

    @Override
    public void compose(final PageContext composerCtx) {
        final String instId = composerCtx.attributes.get(AttributeKeys.INSTITUTION_ID);

        final InstitutionData institutionData = this.restServices.sebServerAPICall(GetInstitutionData.class)
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

        final FormHandle formHandle = new FormBuilder(this.widgetFactory, composerCtx.of(tabs), 4)
                .putStaticValue("id", instId)
                .addTextField("name", "org.sebserver.form.institution.name", institutionData.name, 2)
                .addEmptyCell()
                .addSingleSelection(
                        "authType",
                        "org.sebserver.form.institution.authType",
                        institutionData.authType,
                        InstitutionData.AUTH_TYPE_SELECTION,
                        null, 2)
                .setControl(instTab)
                .buildFor(this.restServices.sebServerAPICall(InstitutionFormPost.class));

        tabs.setSelection(0);

        final Composite setup = new Composite(tabs, SWT.NONE);
        final GridLayout setupLayout = new GridLayout(4, true);
        setupLayout.horizontalSpacing = 10;
        setupLayout.verticalSpacing = 10;
        setupLayout.marginLeft = 10;
        setup.setLayout(setupLayout);
        setup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        lmsSetupTab.setControl(setup);

        // publish possible actions for this page
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_NEW,
                () -> {
                    final IdAndName newInstitutionId = this.restServices
                            .sebServerAPICall(NewInstitution.class)
                            .doAPICall()
                            .onErrorThrow("Unexpected Error");
                    composerCtx.notify(new ActionEvent(ActionDefinition.INSTITUTION_NEW, newInstitutionId));
                }));

        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_MODIFY,
                () -> formHandle.doAPIPost(ActionDefinition.INSTITUTION_MODIFY)));

        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_DELETE,
                () -> {
                    final String newInstitutionId = this.restServices
                            .sebServerAPICall(DeleteInstitution.class)
                            .doAPICall()
                            .onErrorThrow("Unexpected Error");
                    composerCtx.notify(new ActionEvent(ActionDefinition.INSTITUTION_DELETE, newInstitutionId));
                }));

//        this.widgetFactory.formLabelLocalized(institution, "org.sebserver.form.institution.name");
//        form.putField("name", this.widgetFactory.formTextInput(institution, institutionData.name, 2, 1));
//        this.widgetFactory.formEmpty(institution);
//        this.widgetFactory.formLabelLocalized(institution, "org.sebserver.form.institution.authType");
//        final Combo authSelection = this.widgetFactory.formComboLocalized(
//                institution,
//                institutionData.authType,
//                InstitutionData.AUTH_TYPE_SELECTION,
//                2, 1);
//
//        authSelection.addListener(SWT.Selection, e -> {
//            final int selectionIndex = authSelection.getSelectionIndex();
//            form.processSelection(selectionIndex);
//        });
//
//        this.widgetFactory.formEmpty(institution);
//
//        form.addInputLDAP(
//                "org.sebserver.form.institution.authType.ldap.url",
//                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
//        this.widgetFactory.formEmpty(institution);
//        form.addInputLDAP(
//                "org.sebserver.form.institution.authType.ldap.userDN",
//                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
//        this.widgetFactory.formEmpty(institution);
//        form.addInputLDAP(
//                "org.sebserver.form.institution.authType.ldap.userSearch",
//                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
//        this.widgetFactory.formEmpty(institution);
//        form.addInputLDAP(
//                "org.sebserver.form.institution.authType.ldap.groupSearch",
//                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
//        this.widgetFactory.formEmpty(institution);
//        form.addInputLDAP(
//                "org.sebserver.form.institution.authType.ldap.groupFilter",
//                () -> this.widgetFactory.formTextInput(institution, "", 2, 1));
//
//        form.processSelection(authSelection.getSelectionIndex());
    }
}
