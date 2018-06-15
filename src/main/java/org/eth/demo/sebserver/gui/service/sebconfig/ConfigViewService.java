/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.GUISpringConfig;
import org.eth.demo.sebserver.gui.domain.sebconfig.Cell;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
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

    public ConfigViewService(final RestTemplate restTemplate,
            final Collection<InputComponentBuilder> builders) {

        this.restTemplate = restTemplate;
        this.builderTypeMapping = new EnumMap<>(FieldType.class);
        for (final InputComponentBuilder builder : builders) {
            for (final FieldType type : builder.supportedTypes()) {
                this.builderTypeMapping.put(type, builder);
            }
        }
    }

    public ViewContext createViewContext(
            final String name,
            final int xpos,
            final int ypos,
            final int width,
            final int height,
            final int columns,
            final int rows) {

        final ValueChangeListener valueChangeListener = new ValueChangeListener() {
            @Override
            public void valueChanged(final GUIViewAttribute attribute, final String value, final int listIndex) {
                System.out.println("****************** value entered: " + value + " attribute: " + attribute.name
                        + " listIndex: " + listIndex);
            }
        };

        return new ViewContext(name, xpos, ypos, width, height, columns, rows,
                loadConfigAttributes(name), valueChangeListener);
    }

    public ViewContext createComponents(final Composite parent, final ViewContext viewContext) {
        final Map<String, List<GUIViewAttribute>> groups = new LinkedHashMap<>();
        for (final GUIViewAttribute attribute : viewContext.getAttributes()) {

            // ignore nested attributes
            if (StringUtils.isNotBlank(attribute.parentAttributeName)) {
                continue;
            }

            // check and handle builder availability for specified type
            if (!this.builderTypeMapping.containsKey(attribute.getFieldType())) {
                final Label errorLabel = getErrorLabel(parent, "No Builder for type: " + attribute.type);
                errorLabel.setLayoutData(Cell.createFormData(viewContext.getCell(attribute.xpos, attribute.ypos)));
                continue;
            }

            // collect attributes that belongs to a group for later processing
            if (StringUtils.isNotBlank(attribute.group)) {
                groups.computeIfAbsent(
                        attribute.group,
                        a -> new ArrayList<>()).add(attribute);
                continue;
            }

            // TODO the span information should also come within the orientation form back-end
            Cell cell = viewContext.getCell(attribute.xpos, attribute.ypos);
            if (FieldType.TABLE == attribute.getFieldType()) {
                cell = cell.span(3, 6);
            }

            createSingleInputComponent(
                    parent,
                    attribute,
                    viewContext,
                    cell);
        }

        if (!groups.isEmpty()) {
            for (final List<GUIViewAttribute> group : groups.values()) {
                createInputComponentGroup(parent, group, viewContext);
            }
        }

        return viewContext;
    }

    public ViewContext initInputFieldValues(final ViewContext viewContext) {

        // TODO get all values of the view form back-end and inject them to the appropriate input fields

        return viewContext;
    }

    private Map<String, GUIViewAttribute> loadConfigAttributes(final String viewName) {
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

    private void createInputComponentGroup(
            final Composite parent,
            final List<GUIViewAttribute> groupAttrs,
            final ViewContext viewContext) {

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
                viewContext.getCellRelativeWidth() * groupBounds.width,
                viewContext.getCellRelativeHeight() * (groupBounds.height + Cell.GROUP_CELL_HEIGHT_ADJUSTMENT),
                viewContext.getCellPixelWidth() * groupBounds.width,
                viewContext.getCellPixelHeight() * (groupBounds.height + Cell.GROUP_CELL_HEIGHT_ADJUSTMENT))));

        final int cellWidth = 100 / groupBounds.width;
        final int cellHeight = 100 / groupBounds.height;

        for (final GUIViewAttribute attr : groupAttrs) {
            createSingleInputComponent(
                    group,
                    attr,
                    viewContext,
                    new Cell(
                            attr.xpos - groupBounds.x,
                            attr.ypos - groupBounds.y,
                            cellWidth,
                            cellHeight,
                            viewContext.getCellPixelWidth(),
                            viewContext.getCellPixelHeight()));
        }
    }

    private void createSingleInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final ViewContext viewContext,
            final Cell cell) {

        final InputComponentBuilder inputComponentBuilder = this.builderTypeMapping.get(attribute.getFieldType());
        final InputField inputField = inputComponentBuilder.createInputComponent(parent, attribute, viewContext);

        createTitleLabel(parent, inputField, cell, attribute.name);
        inputField.getControl().setLayoutData(Cell.createFormData(cell));
        inputField.getControl().setToolTipText(attribute.name);
        viewContext.registerInputField(inputField);
    }

    private void createTitleLabel(
            final Composite parent,
            final InputField inputField,
            final Cell cell,
            final String title) {

        final FieldType fieldType = inputField.getType();
        final Label label = new Label(parent, SWT.NONE);
        label.setText(" " + title + "   ");
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