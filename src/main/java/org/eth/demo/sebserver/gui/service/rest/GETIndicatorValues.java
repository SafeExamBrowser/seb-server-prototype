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
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETIndicatorValues implements SEBServerAPICall<List<GUIIndicatorValue>> {

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;
    private final RequestCallBuilder<List<GUIIndicatorValue>> builder;

    public GETIndicatorValues(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();
        this.builder = new RequestCallBuilder<>(this);
    }

    @Override
    public RequestCallBuilder<List<GUIIndicatorValue>> with() {
        this.builder.clear();
        return this.builder;
    }

    @Override
    public List<GUIIndicatorValue> doAPICall(final Map<String, String> attributes) {
        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);

        return this.restTemplate.exchange(
                this.restCallBuilder
                        .withPath("exam/indicatorValues/" + examId),
                HttpMethod.GET,
                this.restCallBuilder.httpEntity()
                        .withContentTypeJson()
                        .withAuth(attributes)
                        .build(),
                new ParameterizedTypeReference<List<GUIIndicatorValue>>() {
                }).getBody();
    }

}
