/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eth.demo.sebserver.gui.domain.sebconfig.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.ConfigTableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.rest.POSTConfigValue;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViewValueChangeListener implements ValueChangeListener {

    private final POSTConfigValue saveConfigAttributeValue;
    private final RestTemplate restTemplate;

    public ViewValueChangeListener(
            final POSTConfigValue saveConfigAttributeValue,
            final RestTemplate restTemplate) {

        this.saveConfigAttributeValue = saveConfigAttributeValue;
        this.restTemplate = restTemplate;
    }

    @Override
    public void valueChanged(
            final String configId,
            final ConfigViewAttribute attribute,
            final String value,
            final int listIndex) {

        final ConfigAttributeValue valueObj = new ConfigAttributeValue(
                configId,
                attribute.name,
                attribute.parentAttributeName,
                listIndex,
                value);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonValue = mapper.writeValueAsString(valueObj);
            this.saveConfigAttributeValue
                    .with(this.restTemplate)
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
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonValue = mapper.writeValueAsString(tableValue);
            this.saveConfigAttributeValue
                    .with(this.restTemplate)
                    .tableAttribute()
                    .attributeValue(jsonValue)
                    .doAPICall();
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to POST attribute value to back-end: ", e);
        }
    }

}
