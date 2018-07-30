/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.List;
import java.util.Map;

import org.eth.demo.sebserver.gui.domain.exam.GUIIndicatorValue;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETIndicatorValues implements SEBServerAPICall<List<GUIIndicatorValue>> {

    private final RestCallBuilder restCallBuilder;

    public GETIndicatorValues(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Response<List<GUIIndicatorValue>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);

        try {
            return new Response<>(
                    restTemplate.exchange(
                            this.restCallBuilder
                                    .withPath("exam/indicatorValues/" + examId),
                            HttpMethod.GET,
                            this.restCallBuilder.httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            new ParameterizedTypeReference<List<GUIIndicatorValue>>() {
                            })
                            .getBody());
        } catch (final Throwable t) {
            return new Response<>(t);
        }
    }
}
