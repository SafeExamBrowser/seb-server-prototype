/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.content;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.sebconfig.ConfigViewService;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigurationPage implements TemplateComposer {

    private final ConfigViewService configViewService;

    public SEBConfigurationPage(final ConfigViewService configViewService) {
        this.configViewService = configViewService;
    }

    @Override
    public void compose(final PageContext composerCtx) {
        final String configId = "1"; // TODO: composerCtx.attributes.get(AttributeKeys.CONFIG_ID);

        final Composite content = new Composite(composerCtx.parent, SWT.NONE);
        // TODO use GridLayout instead of FormLayout
        final FormLayout contentLayout = new FormLayout();
        contentLayout.marginLeft = 10;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final ViewContext viewContext = this.configViewService.createViewContext("view1", configId, 4, 20);
        this.configViewService.createComponents(content, viewContext);
        this.configViewService.initInputFieldValues(viewContext);
    }

}
