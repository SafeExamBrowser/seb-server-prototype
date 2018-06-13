/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.domain.sebconfig.ViewContext;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.sebconfig.ConfigViewService;
import org.eth.demo.sebserver.util.TypedMap;
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
    public boolean validateAttributes(final TypedMap attributes) {
        return true;
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent, final TypedMap attributes) {

        final ViewContext view = new ViewContext("view1", 100, 0, 800, 500, 4, 20);
        final Map<String, GUIViewAttribute> configAttributes = this.configViewService.loadConfigAttributes(view.name);

        final RowLayout parentLayout = new RowLayout();
        parentLayout.wrap = false;
        parentLayout.pack = false;
        parentLayout.justify = true;
        parentLayout.type = SWT.HORIZONTAL;
        parentLayout.center = true;
        parent.setLayout(parentLayout);

        final Group viewGroup = new Group(parent, SWT.NONE);
        viewGroup.setLayoutData(new RowData(view.width, view.height));
        viewGroup.setText("Demo Config View");
        viewGroup.setBounds(view.getViewBounds());

        final FormLayout layout = new FormLayout();
        layout.marginTop = 20;
        //   layout.marginLeft = 20;
        viewGroup.setLayout(layout);

        final Map<String, List<GUIViewAttribute>> groups = new LinkedHashMap<>();
        for (final GUIViewAttribute configAttr : configAttributes.values()) {
            if (StringUtils.isNotBlank(configAttr.parentAttributeName)) {
                continue;
            }
            if (StringUtils.isNotBlank(configAttr.group)) {
                groups.computeIfAbsent(
                        configAttr.group,
                        a -> new ArrayList<>()).add(configAttr);
                continue;
            }

            this.configViewService.createInputComponent(viewGroup, configAttr, view);
        }

        if (!groups.isEmpty()) {
            for (final List<GUIViewAttribute> group : groups.values()) {
                this.configViewService.createInputComponentGroup(viewGroup, group, view);
            }
        }

    }

}
