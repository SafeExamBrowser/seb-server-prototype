/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.GUISpringConfig;
import org.eth.demo.sebserver.gui.domain.sebconfig.Cell;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.domain.sebconfig.ViewContext;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ConfigViewService {

    private final RestTemplate restTemplate;
    private final Map<FieldType, InputComponentBuilder> builderTypeMapping;

    private final Map<String, Map<String, GUIViewAttribute>> attributeCache;

    public ConfigViewService(final RestTemplate restTemplate,
            final Collection<InputComponentBuilder> builders) {

        this.restTemplate = restTemplate;
        this.builderTypeMapping = new EnumMap<>(FieldType.class);
        for (final InputComponentBuilder builder : builders) {
            for (final FieldType type : builder.supportedTypes()) {
                this.builderTypeMapping.put(type, builder);
            }
        }

        this.attributeCache = new HashMap<>();
    }

    public Map<String, GUIViewAttribute> loadConfigAttributes(final String viewName) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                GUISpringConfig.ROOT_LOCATION + "/sebconfig/" + viewName);

        final ResponseEntity<List<GUIViewAttribute>> request = this.restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GUIViewAttribute>>() {
                });

        return request.getBody().stream()
                .collect(Collectors.toMap(
                        a -> a.name,
                        a -> a));
    }

    public Map<String, GUIViewAttribute> getConfigAttributes(final String viewName, final boolean reload) {
        return this.attributeCache.computeIfAbsent(
                viewName,
                this::loadConfigAttributes);
    }

    public Map<String, GUIViewAttribute> getConfigAttributes(final ViewContext view) {
        return getConfigAttributes(view.name, false);
    }

    public GUIViewAttribute getConfigAttribute(final String viewName, final String attrName) {
        return getConfigAttributes(viewName, false).get(attrName);
    }

    public Collection<GUIViewAttribute> getChildAttributes(final String viewName, final String parentName) {
        return getConfigAttributes(viewName, false)
                .values()
                .stream()
                .filter(a -> parentName.equals(a.parentAttributeName))
                .collect(Collectors.toList());
    }

    public void createInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final ViewContext view) {

        createInputComponent(parent, attribute, view.getCell(attribute.xpos, attribute.ypos));
    }

    public void createInputComponentGroup(
            final Composite parent,
            final List<GUIViewAttribute> groupAttrs,
            final ViewContext view) {

        if (groupAttrs == null || groupAttrs.isEmpty()) {
            return;
        }

        final GUIViewAttribute firstAttr = groupAttrs.get(0);
        final Rectangle groupBounds = groupAttrs.stream()
                .map(a -> new Rectangle(a.xpos, a.ypos, 1, 1))
                .reduce(new Rectangle(firstAttr.xpos, firstAttr.ypos, 0, 0), Rectangle::union);

        System.out.println("****************** group: " + firstAttr.group + " bounds: " + groupBounds);

        final Group group = new Group(parent, SWT.NONE);
        final FormLayout layout = new FormLayout();
        group.setLayout(layout);
        group.setText(firstAttr.group);
        group.setLayoutData(Cell.createFormData(new Cell(
                groupBounds.x,
                groupBounds.y,
                view.getCellRelativeWidth() * groupBounds.width,
                view.getCellRelativeHeight() * (groupBounds.height + Cell.GROUP_CELL_HEIGHT_ADJUSTMENT),
                view.getCellPixelWidth() * groupBounds.width,
                view.getCellPixelHeight() * (groupBounds.height + Cell.GROUP_CELL_HEIGHT_ADJUSTMENT))));

        final int cellWidth = 100 / groupBounds.width;
        final int cellHeight = 100 / groupBounds.height;

        for (final GUIViewAttribute attr : groupAttrs) {
            createInputComponent(
                    group,
                    attr,
                    new Cell(
                            attr.xpos - groupBounds.x,
                            attr.ypos - groupBounds.y,
                            cellWidth,
                            cellHeight,
                            view.getCellPixelWidth(),
                            view.getCellPixelHeight()));
        }
    }

    private void createInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final Cell cell) {

        final InputComponentBuilder inputComponentBuilder = this.builderTypeMapping.get(attribute.getFieldType());
        if (inputComponentBuilder == null) {
            final Label errorLabel = getErrorLabel(parent, "No Builder for type: " + attribute.type);
            errorLabel.setLayoutData(Cell.createFormData(cell));
            return;
        }

        final InputField inputField = inputComponentBuilder.createInputComponent(parent, attribute);
        createTitleLabel(parent, inputField, cell, attribute.name);
        inputField.getControl().setLayoutData(Cell.createFormData(cell));
        inputField.getControl().setToolTipText(attribute.name);
        inputField.setValueListener((value, attr) -> {
            // TODO send value to back-end
            System.out.println("****************** value entered: " + value + " attribute: " + attr.name);
        });
    }

    private void createTitleLabel(
            final Composite parent,
            final InputField inputField,
            final Cell cell,
            final String title) {

        final FieldType fieldType = inputField.getType();
        final Label label = new Label(parent, SWT.NONE);
        label.setText(" " + title + " ");
        switch (fieldType.titleOrientation) {
            case LEFT: {
                if (cell.column > 0) {
                    label.setAlignment(SWT.RIGHT);
                    label.setLayoutData(Cell.createFormData(cell.copyOf(cell.column - 1, cell.row)));
                }
                break;
            }
            case TOP: {
                if (cell.row > 0) {
                    label.setAlignment(SWT.BOTTOM);
                    label.setLayoutData(Cell.createFormData(cell.copyOf(cell.column, cell.row - 1)));
                }
                break;
            }
            case RIGHT: {
                label.setAlignment(SWT.LEFT);
                label.setLayoutData(Cell.createFormData(cell.copyOf(cell.column + 1, cell.row)));
            }
            default: {
                label.dispose();
            }
        }
    }

    private Label getErrorLabel(final Composite parent, final String errorText) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText(errorText);
        return label;
    }

}
