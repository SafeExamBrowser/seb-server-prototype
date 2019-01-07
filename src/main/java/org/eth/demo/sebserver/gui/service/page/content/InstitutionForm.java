/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import java.util.Arrays;
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
import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEventListener;
import org.eth.demo.sebserver.gui.service.page.action.ActionPublishEvent;
import org.eth.demo.sebserver.gui.service.page.action.InstitutionActions;
import org.eth.demo.sebserver.gui.service.page.form.Form;
import org.eth.demo.sebserver.gui.service.page.form.FormHandle;
import org.eth.demo.sebserver.gui.service.page.form.PageFormService;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.institution.GetInstitutionData;
import org.eth.demo.sebserver.gui.service.rest.institution.InstitutionFormPost;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.util.Tuple;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class InstitutionForm implements TemplateComposer {

    private static final String LDAP_GROUP_NAME = "LDAP_GROUP";

    private final RestServices restServices;
    private final PageFormService pageFormService;

    public InstitutionForm(
            final RestServices restServices,
            final PageFormService pageFormService) {

        this.restServices = restServices;
        this.pageFormService = pageFormService;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return StringUtils.isNoneBlank(attributes.get(AttributeKeys.INSTITUTION_ID));
    }

    @Override
    public void compose(final PageContext composerCtx) {
        final PolyglotPageService polyglotPageService = this.pageFormService.getPolyglotPageService();
        final WidgetFactory widgetFactory = this.pageFormService.getWidgetFactory();

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

        final Label pageTitle = widgetFactory.labelLocalized(
                content, "h2", new LocTextKey(
                        "org.sebserver.activities.instName",
                        institutionData.name));
        pageTitle.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));
        ActionEventListener.injectListener(
                pageTitle,
                ActionDefinition.INSTITUTION_MODIFY,
                event -> {
                    final IdAndName idAndName = (IdAndName) event.source;
                    polyglotPageService.injectI18n(pageTitle, new LocTextKey(
                            "org.sebserver.activities.instName",
                            idAndName.name));
                });

        final TabFolder tabs = new TabFolder(content, SWT.NONE);
        tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TabItem instTab = new TabItem(tabs, SWT.NONE);
        instTab.setText("General");
        final TabItem lmsSetupTab = new TabItem(tabs, SWT.NONE);
        lmsSetupTab.setText("LMS Setup(s)");

        // The Institution form
        //@formatter:off
        final FormHandle<IdAndName> formHandle = this.pageFormService.getBuilder(composerCtx.of(tabs), 4)
                .readonly(false)
                .putStaticValue("id", instId)
                .addTextField("name", "org.sebserver.form.institution.name", institutionData.name, 2)
                .addEmptyCell()
                .addSingleSelection(
                        "authType",
                        "org.sebserver.form.institution.authType",
                        institutionData.authType,
                        InstitutionData.AUTH_TYPE_SELECTION,
                        InstitutionForm::processAuthTypeSelection,
                        2)
                .addEmptyCell()
                .addTextField("ldapUrl", "org.sebserver.form.institution.authType.ldap.url", "", 2, LDAP_GROUP_NAME)
                .addEmptyCell()
                .addTextField("ldapUserDN", "org.sebserver.form.institution.authType.ldap.userDN", "", 2, LDAP_GROUP_NAME)
                .addEmptyCell()
                .addTextField("ldapUserSearch", "org.sebserver.form.institution.authType.ldap.userSearch", "", 2,LDAP_GROUP_NAME)
                .addEmptyCell()
                .addTextField("ldapGroupSearch", "org.sebserver.form.institution.authType.ldap.groupSearch", "", 2, LDAP_GROUP_NAME)
                .addEmptyCell()
                .addTextField("ldapGroupFilter", "org.sebserver.form.institution.authType.ldap.groupFilter", "", 2,LDAP_GROUP_NAME)
                .setControl(instTab)
                .buildFor(this.restServices.sebServerAPICall(InstitutionFormPost.class))
                .process(InstitutionForm::processAuthTypeSelection);
        //@formatter:on

        this.pageFormService.getBuilder(composerCtx.of(tabs), 4)
                .readonly(false)

                .addSingleSelection(
                        "LMSType",
                        "LMS Type",
                        "1",
                        Arrays.asList(new Tuple<>("1", "Moodle"), new Tuple<>("2", "Open edX")),
                        InstitutionForm::processLMSTypeSelection,
                        2)
                .addEmptyCell()
                .addTextField("adress", "Server API URL", "https://www.lms.com/api/", 2)
                .addEmptyCell()
                .addTextField("username", "API Credential Name", "", 2)
                .addEmptyCell()
                .addTextField("secret", "API Credential Secret", "", 2)
                .addEmptyCell()
                .addTextField("token", "API Access Token", "", 2)
                .addEmptyCell()
                .addTextField("cert", "API Certificate", "", 2)
                .addEmptyCell()
                .setControl(lmsSetupTab)
                .buildFor(this.restServices.sebServerAPICall(InstitutionFormPost.class));

        tabs.setSelection(0);

//        final Composite setup = new Composite(tabs, SWT.NONE);
//        final GridLayout setupLayout = new GridLayout(4, true);
//        setupLayout.horizontalSpacing = 10;
//        setupLayout.verticalSpacing = 10;
//        setupLayout.marginLeft = 10;
//        setup.setLayout(setupLayout);
//        setup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//
//        lmsSetupTab.setControl(setup);

        // publish possible actions for this page
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_NEW,
                InstitutionActions.newInstitution(composerCtx, this.restServices),
                "actions.new.institution.confirm"));

        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_MODIFY,
                () -> formHandle.doAPIPost(ActionDefinition.INSTITUTION_MODIFY)));

        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.INSTITUTION_DELETE,
                InstitutionActions.deleteInstitution(composerCtx, this.restServices, instId),
                "actions.delete.institution.confirm"));

        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.LMS_SETUP_NEW,
                () -> {
                }));
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.LMS_SETUP_TEST,
                () -> {
                }));
    }

    static final void processLMSTypeSelection(final Form form) {

    }

    static final void processAuthTypeSelection(final Form form) {
        final String value = form.getValue("authType");
        if ("INTERNAL".equals(value)) {
            form.setVisible(false, LDAP_GROUP_NAME);
        } else if ("EXTERNAL_LDAP".equals(value)) {
            form.setVisible(true, LDAP_GROUP_NAME);
        }
    }

}
