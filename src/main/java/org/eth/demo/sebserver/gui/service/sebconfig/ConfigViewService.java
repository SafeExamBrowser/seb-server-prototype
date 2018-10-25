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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigTableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.GetConfigAttributeValues;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.GetConfigAttributes;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.PostConfigValue;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.service.JSONMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class ConfigViewService implements ValueChangeListener {

    final RestServices restService;
    private final Map<FieldType, InputComponentBuilder> builderTypeMapping;
    private final JSONMapper jsonMapper;

    public ConfigViewService(
            final RestServices restService,
            final Collection<InputComponentBuilder> builders,
            final JSONMapper jsonMapper) {

        this.restService = restService;
        this.builderTypeMapping = new EnumMap<>(FieldType.class);
        this.jsonMapper = jsonMapper;
        for (final InputComponentBuilder builder : builders) {
            for (final FieldType type : builder.supportedTypes()) {
                this.builderTypeMapping.put(type, builder);
            }
        }
    }

    public ViewContext createViewContext(
            final String name,
            final String configurationId,
            final int columns,
            final int rows) {

        // request the configuration attributes for the view/page from the SEBServer Web-Service API
        final Map<String, ConfigViewAttribute> attributes = this.restService.sebServerAPICall(GetConfigAttributes.class)
                .configViewName(name)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        return new ViewContext(
                name,
                configurationId,
                columns, rows,
                attributes,
                this);
    }

    public Composite createViewGrid(
            final Composite parent,
            final ViewContext viewContext) {

        final Composite composite = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(viewContext.columns, true);
        gridLayout.marginTop = 10;
        gridLayout.verticalSpacing = 5;
        gridLayout.horizontalSpacing = 10;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final ViewGridBuilder viewGridBuilder = new ViewGridBuilder(composite, viewContext);
        for (final ConfigViewAttribute attribute : viewContext.getAttributes()) {
            viewGridBuilder.add(attribute);
        }
        viewGridBuilder.compose();
        return composite;
    }

    public ViewContext initInputFieldValues(final ViewContext viewContext) {

        final Collection<ConfigAttributeValue> attributeValues = this.restService
                .sebServerAPICall(GetConfigAttributeValues.class)
                .config(viewContext.configurationId)
                .configViewName(viewContext.name)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        viewContext.setValuesToInputFields(attributeValues);
        return viewContext;
    }

    @Override
    public void valueChanged(
            final ViewContext context,
            final ConfigViewAttribute attribute,
            final String value,
            final int listIndex) {

        final ConfigAttributeValue valueObj = new ConfigAttributeValue(
                context.configurationId,
                attribute.name,
                attribute.parentAttributeName,
                listIndex,
                value);

        try {
            final String jsonValue = this.jsonMapper.writeValueAsString(valueObj);
            this.restService.sebServerAPICall(PostConfigValue.class)
                    .singleAttribute()
                    .attributeValue(jsonValue)
                    .doAPICall();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to POST attribute value to back-end: ", e);
        }

    }

    @Override
    public void tableChanged(final ConfigTableValue tableValue) {
        try {
            final String jsonValue = this.jsonMapper.writeValueAsString(tableValue);
            this.restService.sebServerAPICall(PostConfigValue.class)
                    .tableAttribute()
                    .attributeValue(jsonValue)
                    .doAPICall();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to POST attribute value to back-end: ", e);
        }
    }

    private final class ViewGridBuilder {

        private final Composite parent;
        private final ViewContext viewContext;
        private final CellFieldBuilderAdapter[][] grid;
        private final Set<String> registeredGroups;

        public ViewGridBuilder(final Composite parent, final ViewContext viewContext) {
            this.parent = parent;
            this.viewContext = viewContext;
            this.grid = new CellFieldBuilderAdapter[viewContext.rows][viewContext.columns];
            this.registeredGroups = new HashSet<>();
        }

        public ViewGridBuilder add(final ConfigViewAttribute attribute) {
            // ignore nested attributes here
            if (StringUtils.isNotBlank(attribute.parentAttributeName)) {
                return this;
            }

            // create group builder
            if (StringUtils.isNotBlank(attribute.group)) {
                if (this.registeredGroups.contains(attribute.group)) {
                    return this;
                }

                final GroupCellFieldBuilderAdapter groupBuilder = new GroupCellFieldBuilderAdapter(
                        ConfigViewService.this,
                        this.parent,
                        attribute, this.viewContext);

                fillDummy(groupBuilder.x, groupBuilder.y, groupBuilder.width, groupBuilder.height);
                this.grid[groupBuilder.y][groupBuilder.x] = groupBuilder;
                this.registeredGroups.add(attribute.group);
                return this;
            }

            // create single input field with label
            final FieldType fieldType = attribute.getFieldType();
            final InputComponentBuilder inputComponentBuilder =
                    ConfigViewService.this.builderTypeMapping.get(fieldType);
            final CellFieldBuilderAdapter inputBuilderAdapter = inputBuilderAdapter(
                    inputComponentBuilder,
                    this.parent,
                    attribute,
                    this.viewContext);

            if (attribute.width > 1 || attribute.height > 1) {
                fillDummy(attribute.xpos, attribute.ypos, attribute.width, attribute.height);
            }
            this.grid[attribute.ypos][attribute.xpos] = inputBuilderAdapter;
            int labelX = attribute.xpos;
            int labelY = attribute.ypos;

            switch (fieldType.titleOrientation) {
                case NONE: {
                    return this;
                }
                case RIGHT: {
                    labelX++;
                    break;
                }
                case TOP: {
                    labelY--;
                    break;
                }
                case LEFT:
                default: {
                    labelX--;
                }
            }
            this.grid[labelY][labelX] = labelBuilder(
                    this.parent,
                    attribute,
                    this.viewContext);

            return this;
        }

        public void compose() {
            for (int y = 0; y < this.grid.length; y++) {
                for (int x = 0; x < this.grid[y].length; x++) {
                    if (this.grid[y][x] == null) {
                        final Label empty = new Label(this.parent, SWT.LEFT);
                        empty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                        empty.setText("");
                    } else {
                        this.grid[y][x].createCell();
                    }
                }
            }
        }

        private void fillDummy(final int x, final int y, final int width, final int height) {
            final int upperBoundX = x + width;
            final int upperBoundY = y + height;
            for (int _y = y; _y < upperBoundY; _y++) {
                for (int _x = x; _x < upperBoundX; _x++) {
                    this.grid[_y][_x] = dummyBuilderAdapter();
                }
            }
        }
    }

    private static interface CellFieldBuilderAdapter {

        void createCell();
    }

    private static final CellFieldBuilderAdapter dummyBuilderAdapter() {
        return new CellFieldBuilderAdapter() {
            @Override
            public void createCell() {
            }
        };
    }

    private static final CellFieldBuilderAdapter inputBuilderAdapter(
            final InputComponentBuilder inputFieldBuilder,
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        return new CellFieldBuilderAdapter() {
            @Override
            public void createCell() {

                final InputField inputField = inputFieldBuilder.createInputComponent(parent, attribute, viewContext);
                final GridData gridData = new GridData(
                        SWT.FILL, SWT.FILL,
                        true, false,
                        attribute.width, attribute.height);
                inputField.getControl().setLayoutData(gridData);
                inputField.getControl().setToolTipText(attribute.name);
                viewContext.registerInputField(inputField);
            }
        };
    }

    private static final CellFieldBuilderAdapter labelBuilder(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        return new CellFieldBuilderAdapter() {
            @Override
            public void createCell() {

                final Label label = new Label(parent, SWT.NONE);
                label.setText(attribute.name);
                label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                switch (attribute.getFieldType().titleOrientation) {
                    case LEFT: {
                        label.setAlignment(SWT.RIGHT);
                        return;
                    }
                    case TOP: {
                        label.setAlignment(SWT.BOTTOM);
                        return;
                    }
                    case RIGHT:
                    default: {
                        label.setAlignment(SWT.LEFT);
                    }
                }
            }
        };
    }

    private static final class GroupCellFieldBuilderAdapter implements CellFieldBuilderAdapter {

        final Composite parent;
        final ConfigViewService service;
        final List<ConfigViewAttribute> attributesOfGroup;
        final ConfigViewAttribute attribute;
        final ViewContext viewContext;

        int x = 0;
        final int y = 0;
        int width = 1;
        int height = 1;

        GroupCellFieldBuilderAdapter(
                final ConfigViewService service,
                final Composite parent,
                final ConfigViewAttribute attribute,
                final ViewContext viewContext) {

            this.service = service;
            this.parent = parent;
            this.attribute = attribute;
            this.viewContext = viewContext;
            this.attributesOfGroup = viewContext.getAttributesOfGroup(attribute.group);
            for (final ConfigViewAttribute attr : this.attributesOfGroup) {
                this.x = (this.x < attr.xpos) ? attr.xpos : this.x;
                this.x = (this.y < attr.ypos) ? attr.ypos : this.y;
                this.width = (this.width < attr.xpos + attr.width) ? attr.xpos + attr.width : this.width;
                this.height = (this.height < attr.ypos + attr.height) ? attr.ypos + attr.height : this.height;
            }

            this.width = this.width - this.x;
            this.height = this.height - this.y + 2;
        }

        @Override
        public void createCell() {
            // TODO localized group
            final Group group = new Group(this.parent, SWT.NONE);
            group.setLayout(new GridLayout(this.width, true));
            group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, this.width, this.height));
            group.setText(this.attribute.group);

            for (final ConfigViewAttribute attr : this.attributesOfGroup) {
                final InputComponentBuilder inputComponentBuilder =
                        this.service.builderTypeMapping.get(attr.getFieldType());
                createSingleInputField(group, attr, inputComponentBuilder);
            }
        }

        private void createSingleInputField(final Group group, final ConfigViewAttribute attr,
                final InputComponentBuilder inputComponentBuilder) {
            final InputField inputField =
                    inputComponentBuilder.createInputComponent(group, attr, this.viewContext);
            final GridData gridData = new GridData(
                    SWT.FILL, SWT.FILL,
                    true, false,
                    attr.width, attr.height);
            inputField.getControl().setLayoutData(gridData);
            inputField.getControl().setToolTipText(attr.name);
            this.viewContext.registerInputField(inputField);
        }
    }

}
