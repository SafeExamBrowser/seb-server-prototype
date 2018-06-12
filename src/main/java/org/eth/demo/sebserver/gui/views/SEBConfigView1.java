/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.GUISpringConfig;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.util.TypedMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Lazy
@Component
public class SEBConfigView1 implements ViewComposer {

    public static final int VIEW_GRID_CELL_WIDTH = 100 / 6;
    public static final int VIEW_GRID_CELL_HEIGHT = 100 / 20;

    private final RestTemplate restTemplate;

    public SEBConfigView1(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean validateAttributes(final TypedMap attributes) {
        return true;
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent, final TypedMap attributes) {
        final Collection<GUIViewAttribute> configAttributes = getConfigAttributes();

        final RowLayout parentLayout = new RowLayout();
        parentLayout.wrap = false;
        parentLayout.pack = false;
        parentLayout.justify = true;
        parentLayout.type = SWT.HORIZONTAL;
        parentLayout.center = true;
        parent.setLayout(parentLayout);

        final Group view = new Group(parent, SWT.NONE);
        view.setLayoutData(new RowData(800, 500));
        view.setText("Demo Config View");
        view.setBounds(100, 0, 800, 500);
        final FormLayout layout = new FormLayout();
        layout.marginTop = 20;
        view.setLayout(layout);

        for (final GUIViewAttribute configAttr : configAttributes) {
            if (StringUtils.isNotBlank(configAttr.parentAttributeName)) {
                continue;
            }

            final Label attrName = new Label(view, SWT.RIGHT);
            attrName.setText(configAttr.name);
            attrName.setLayoutData(onLeftUpperorientation(configAttr.xpos, configAttr.ypos));

            final Label attr = new Label(view, SWT.LEFT);
            attr.setText(configAttr.name + "_editor");
            attr.setLayoutData(onLeftUpperorientation(configAttr.xpos + 1, configAttr.ypos));
        }
    }

    private Collection<GUIViewAttribute> getConfigAttributes() {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(GUISpringConfig.ROOT_LOCATION + "/sebconfig/view1");

        final ResponseEntity<List<GUIViewAttribute>> request = this.restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GUIViewAttribute>>() {
                });

        return request.getBody();
    }

    private FormData onLeftUpperorientation(final int x, final int y) {
        final FormData result = new FormData();
        result.top = new FormAttachment(y * VIEW_GRID_CELL_HEIGHT);
        result.left = new FormAttachment(x * VIEW_GRID_CELL_WIDTH, 10);
        result.right = new FormAttachment((x + 1) * VIEW_GRID_CELL_WIDTH);
        result.width = 150;
        result.height = 20;

        return result;
    }

}
