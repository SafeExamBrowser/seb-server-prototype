/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class POSTConfigValue implements SEBServerAPICall<String> {

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;

    public POSTConfigValue(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String doAPICall(final Map<String, String> attributes) {
        final String saveType = getAttribute(attributes, AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE);
        final String attributeValue = getAttribute(attributes, AttributeKeys.CONFIG_ATTRIBUTE_VALUE);

        // TODO here we will get a validation error response if the back-end validation failed
        return this.restTemplate.postForObject(
                this.restCallBuilder
                        .withPath("sebconfig/" + saveType),
                this.restCallBuilder
                        .httpEntity()
                        .withContentTypeJson()
                        .withAuth(attributes)
                        .withBody(attributeValue)
                        .build(),
                String.class);
    }

}
