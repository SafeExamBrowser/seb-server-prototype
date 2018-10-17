/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.exam;

import java.util.Map;

import org.eth.demo.sebserver.gui.domain.exam.RunningExam;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETRunningExamDetails implements SEBServerAPICall<RunningExam> {

    private final RestCallBuilder restCallBuilder;

    public GETRunningExamDetails(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public APICallBuilder<RunningExam> with(final RestTemplate restTemplate) {
        return new APICallBuilder<>(this, restTemplate);
    }

    @Override
    public Result<RunningExam> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String examId = getAttribute(attributes, AttributeKeys.EXAM_ID);

        try {
            return Result.of(
                    restTemplate.exchange(
                            this.restCallBuilder
                                    .withPath("exam/" + examId),
                            HttpMethod.GET,
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            RunningExam.class)
                            .getBody());
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }
}
