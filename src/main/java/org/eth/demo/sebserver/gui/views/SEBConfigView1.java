/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Enumeration;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.sebconfig.ConfigViewService;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigView1 implements ViewComposer {

    private final ConfigViewService configViewService;

    public SEBConfigView1(final ConfigViewService configViewService) {
        this.configViewService = configViewService;
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

        final Enumeration<String> attributeNames = RWT.getRequest().getSession().getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            System.out.println("########" + attributeNames.nextElement());
        }

        final ViewContext viewContext = this.configViewService.createViewContext("view1", "1", 100, 0, 800, 500, 4, 20);

        viewService.centringView(parent);

        final Group viewGroup = new Group(parent, SWT.NONE);
        viewGroup.setLayoutData(new RowData(viewContext.width, viewContext.height));
        viewGroup.setText("Demo Config View");
        viewGroup.setBounds(viewContext.getViewBounds());

        final FormLayout layout = new FormLayout();
        layout.marginTop = 20;
        //   layout.marginLeft = 20;
        viewGroup.setLayout(layout);

        this.configViewService.createComponents(viewGroup, viewContext);
        this.configViewService.initInputFieldValues(viewContext);
    }

}
