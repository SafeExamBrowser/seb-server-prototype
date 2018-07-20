/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eth.demo.sebserver.gui.domain.exam.GUIExam;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETExamDetail implements SEBServerAPICall<GUIExam> {

    private final RestCallBuilder restCallBuilder;

    public GETExamDetail(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public APICallBuilder<GUIExam> with(final RestTemplate restTemplate) {
        return new APICallBuilder<>(this, restTemplate);
    }

    @Override
    public Response<GUIExam> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);

        try {
            return new Response<>(
                    restTemplate.exchange(
                            this.restCallBuilder
                                    .withPath("exam/" + examId),
                            HttpMethod.GET,
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            GUIExam.class)
                            .getBody());
        } catch (final Throwable t) {
            return new Response<>(t);
        }
    }
}
