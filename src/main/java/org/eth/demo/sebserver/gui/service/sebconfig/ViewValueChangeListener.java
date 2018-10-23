/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigTableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.PostConfigValue;
import org.eth.demo.sebserver.service.JSONMapper;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ViewValueChangeListener implements ValueChangeListener {

    private final PostConfigValue saveConfigAttributeValue;
    private final RestTemplate restTemplate;
    private final JSONMapper jsonMapper;

    public ViewValueChangeListener(
            final PostConfigValue saveConfigAttributeValue,
            final RestTemplate restTemplate,
            final JSONMapper jsonMapper) {

        this.saveConfigAttributeValue = saveConfigAttributeValue;
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
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
            final String jsonValue = this.jsonMapper.writeValueAsString(valueObj);
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
            final String jsonValue = this.jsonMapper.writeValueAsString(tableValue);
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
