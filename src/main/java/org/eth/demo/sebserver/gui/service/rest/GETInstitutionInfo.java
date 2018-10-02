/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETInstitutionInfo implements SEBServerAPICall<Map<String, String>> {

    private final RestCallBuilder restCallBuilder;
    private final String uri;

    public GETInstitutionInfo(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath("institution/info");
    }

    @Override
    public Result<Map<String, String>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        try {
            return Result.of(
                    restTemplate.exchange(
                            this.uri,
                            HttpMethod.GET,
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            new ParameterizedTypeReference<Map<String, String>>() {
                            })
                            .getBody());
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }

}
