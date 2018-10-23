/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.sebconfig;

import java.util.Map;

import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class PostConfigValue implements SEBServerAPICall<String> {

    private final RestCallBuilder restCallBuilder;

    public PostConfigValue(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Result<String> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String saveType = getAttribute(attributes, AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE);
        final String attributeValue = getAttribute(attributes, AttributeKeys.CONFIG_ATTRIBUTE_VALUE);

        try {
            // TODO here we want to get a validation error response if the back-end validation failed
            return Result.of(
                    restTemplate.postForObject(
                            this.restCallBuilder
                                    .withPath("sebconfig/" + saveType),
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .withBody(attributeValue)
                                    .build(),
                            String.class));
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }

}
