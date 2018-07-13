/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eth.demo.sebserver.gui.domain.sebconfig.GUIAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUITableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.rest.POSTConfigValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViewValueChangeListener implements ValueChangeListener {

    private final POSTConfigValue saveConfigAttributeValue;

    public ViewValueChangeListener(
            final POSTConfigValue saveConfigAttributeValue) {

        this.saveConfigAttributeValue = saveConfigAttributeValue;
    }

    @Override
    public void valueChanged(
            final String configId,
            final GUIViewAttribute attribute,
            final String value,
            final int listIndex) {

        final GUIAttributeValue valueObj = new GUIAttributeValue(
                configId,
                attribute.name,
                attribute.parentAttributeName,
                listIndex,
                value);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonValue = mapper.writeValueAsString(valueObj);
            this.saveConfigAttributeValue
                    .with()
                    .singleAttribute()
                    .attributeValue(jsonValue)
                    .doAPICall();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to POST attribute value to back-end: ", e);
        }

    }

    @Override
    public void tableChanged(final GUITableValue tableValue) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonValue = mapper.writeValueAsString(tableValue);
            this.saveConfigAttributeValue
                    .with()
                    .tableAttribute()
                    .attributeValue(jsonValue)
                    .doAPICall();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to POST attribute value to back-end: ", e);
        }
    }

}
