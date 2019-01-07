/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionPublishEvent;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigurationForm implements TemplateComposer {

    private final RestServices restServices;
    private final WidgetFactory widgetFactory;

    public SEBConfigurationForm(final RestServices restServices, final WidgetFactory widgetFactory) {
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
                content, "h2", new LocTextKey(
                        "org.sebserver.sebconfig.info.title"));
        labelLocalized.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));

        this.widgetFactory.labelLocalized(content, "h3", new LocTextKey("org.sebserver.sebconfig.info"));

        // publish possible actions for this page
        composerCtx.notify(new ActionPublishEvent(
                ActionDefinition.SEB_CONFIG_NEW,
                () -> {
                }));
    }

}
