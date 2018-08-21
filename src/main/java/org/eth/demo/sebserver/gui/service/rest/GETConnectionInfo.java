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

import org.eth.demo.sebserver.gui.domain.exam.ConnectionRow;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETConnectionInfo implements SEBServerAPICall<List<ConnectionRow>> {

    private final RestCallBuilder restCallBuilder;

    public GETConnectionInfo(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Result<List<ConnectionRow>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);

        try {
            return Result.of(
                    restTemplate.exchange(
                            this.restCallBuilder
                                    .withPath("runningexam/indicatorValues/" + examId),
                            HttpMethod.GET,
                            this.restCallBuilder.httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            new ParameterizedTypeReference<List<ConnectionRow>>() {
                            })
                            .getBody());
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }
}
