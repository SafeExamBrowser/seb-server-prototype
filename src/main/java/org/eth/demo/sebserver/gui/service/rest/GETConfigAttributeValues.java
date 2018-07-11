/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collection;
import java.util.Map;

import org.eth.demo.sebserver.gui.domain.sebconfig.GUIAttributeValue;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETConfigAttributeValues implements RestCall<Collection<GUIAttributeValue>> {

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;

    public GETConfigAttributeValues(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Collection<GUIAttributeValue> doRequest(final Map<String, String> attributes) {
        final String configId = getAttribute(attributes, AttributeKeys.CONFIG_ID);
        final String configAttrs = getAttribute(attributes, AttributeKeys.CONFIG_ATTRIBUTE_NAMES);

        return this.restTemplate.exchange(
                this.restCallBuilder
                        .withPath("sebconfig/values/" + configId),
                HttpMethod.GET,
                this.restCallBuilder
                        .httpEntity()
                        .withContentTypeJson()
                        .withHeader("attributeNames", configAttrs)
                        .withHeaderCopy(HttpHeaders.AUTHORIZATION)
                        .build(),
                new ParameterizedTypeReference<Collection<GUIAttributeValue>>() {
                }).getBody();
    }

}
