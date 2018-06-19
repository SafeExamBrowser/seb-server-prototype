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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViewValueChangeListener implements ValueChangeListener {

    private static final Logger log = LoggerFactory.getLogger(ViewValueChangeListener.class);

    private final RestTemplate restTemplate;
    private final ViewContext viewContext;

    public ViewValueChangeListener(final RestTemplate restTemplate, final ViewContext viewContext) {
        this.restTemplate = restTemplate;
        this.viewContext = viewContext;
    }

    @Override
    public void valueChanged(final GUIViewAttribute attribute, final String value, final int listIndex) {
        System.out.println("****************** value entered: " + value + " attribute: " + attribute.name
                + " listIndex: " + listIndex);

        final String url = ConfigViewService.VALUE_LOCATION + "save";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        final GUIAttributeValue valueObj = new GUIAttributeValue(
                this.viewContext.configurationId,
                attribute.name,
                attribute.parentAttributeName,
                listIndex,
                value);

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String json = mapper.writeValueAsString(valueObj);
            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(url);
            final HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);

            // TODO here we will get a validation error response if the back-end validation failed
            final String response = this.restTemplate.postForObject(
                    builder.toUriString(),
                    httpEntity,
                    String.class);
        } catch (final JsonProcessingException e) {
            log.error("Failed to send value to back-end: ", e);
        }
    }

    @Override
    public void tableChanged(final GUITableValue tableValue) {
        final String url = ConfigViewService.VALUE_LOCATION + "savetable";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String json = mapper.writeValueAsString(tableValue);
            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(url);
            final HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);

            // TODO here we will get a validation error response if the back-end validation failed
            final String response = this.restTemplate.postForObject(
                    builder.toUriString(),
                    httpEntity,
                    String.class);
        } catch (final JsonProcessingException e) {
            log.error("Failed to send value to back-end: ", e);
        }

    }

}
