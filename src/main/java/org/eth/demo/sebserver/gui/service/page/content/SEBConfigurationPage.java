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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.sebconfig.ConfigViewService;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigurationPage implements TemplateComposer {

    private final ConfigViewService configViewService;
    private final WidgetFactory widgetFactory;

    public SEBConfigurationPage(final ConfigViewService configViewService, final WidgetFactory widgetFactory) {
        super();
        this.configViewService = configViewService;
        this.widgetFactory = widgetFactory;
    }

    @Override
    public void compose(final PageContext composerCtx) {
        final String configId = "1"; // TODO: composerCtx.attributes.get(AttributeKeys.CONFIG_ID);

        final Composite content = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout contentLayout = new GridLayout();
        contentLayout.marginLeft = 10;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label labelLocalized = this.widgetFactory.labelLocalized(
                content, "h2", new LocTextKey("org.sebserver.domain.sebconfig.title"));
        labelLocalized.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, false));

        final TabFolder tabs = new TabFolder(content, SWT.NONE);
        tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TabItem page1Tap = new TabItem(tabs, SWT.NONE);
        page1Tap.setText("Page 1");
        final TabItem page2Tap = new TabItem(tabs, SWT.NONE);
        page2Tap.setText("Page 2");
        final TabItem page3Tap = new TabItem(tabs, SWT.NONE);
        page3Tap.setText("Page 3");

//        final Composite contentPage1 = new Composite(tabs, SWT.NONE);
//        // TODO use GridLayout instead of FormLayout
//        final GridLayout formLayout = new GridLayout();
//        formLayout.marginTop = 10;
//        contentPage1.setLayout(formLayout);
//        contentPage1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final ViewContext viewContext = this.configViewService.createViewContext("view1", configId, 4, 20);
        final Composite viewGrid = this.configViewService.createViewGrid(tabs, viewContext);
        this.configViewService.initInputFieldValues(viewContext);

        page1Tap.setControl(viewGrid);

        // NTOE: just for testing
        final Composite contentPage2 = new Composite(tabs, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(4, true);
        gridLayout.horizontalSpacing = 10;
        gridLayout.verticalSpacing = 10;
        gridLayout.marginTop = 10;
        contentPage2.setLayout(gridLayout);
        contentPage2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label label1 = this.widgetFactory.label(contentPage2, "label1");
        label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Label label2 = this.widgetFactory.label(contentPage2, "label2");
        label2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Group group = new Group(contentPage2, SWT.NONE);
        group.setText("Group");
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 5));

        this.widgetFactory.formEmpty(contentPage2);

        final Label label3 = this.widgetFactory.label(contentPage2, "label3");
        label3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Label label4 = this.widgetFactory.label(contentPage2, "label4");
        label4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Label label5 = this.widgetFactory.label(contentPage2, "label5");
        label5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        page2Tap.setControl(contentPage2);
    }

}
