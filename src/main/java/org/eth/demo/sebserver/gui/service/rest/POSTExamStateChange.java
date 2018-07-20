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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public final class POSTExamStateChange implements SEBServerAPICall<GUIExam> {

    private final RestCallBuilder restCallBuilder;

    public POSTExamStateChange(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Response<GUIExam> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);
        final String toState = getAttribute(attributes, AttributeKeys.STATE_ID);

        try {
            return new Response<>(
                    restTemplate.postForObject(
                            this.restCallBuilder
                                    .withPath("exam/statechange/" + examId + "/" + toState),
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            GUIExam.class));
        } catch (final Throwable t) {
            return new Response<>(t);
        }
    }

}
