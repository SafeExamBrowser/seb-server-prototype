/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eth.demo.sebserver.SEBServer;
import org.eth.demo.sebserver.gui.domain.exam.GUIExam;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public final class GETExams implements SEBServerAPICall<Collection<GUIExam>> {

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;
    private final String uri;

    public GETExams(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();
        this.uri = restCallBuilder.withPath("exam");
    }

    @Override
    public Collection<GUIExam> doAPICall(final Map<String, String> attributes) {
        return this.restTemplate.exchange(
                this.uri,
                HttpMethod.GET,
                this.restCallBuilder
                        .httpEntity()
                        .withHeader(
                                HttpHeaders.CONTENT_TYPE,
                                SEBServer.Constants.CONTENT_TYPE_APPLICATION_JSON)
                        .withAuth(attributes)
                        .build(),
                new ParameterizedTypeReference<List<GUIExam>>() {
                }).getBody();
    }

}
