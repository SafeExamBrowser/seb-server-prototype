/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eth.demo.sebserver.gui.domain.admin.InstitutionData;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.util.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GETInstitutionData implements SEBServerAPICall<InstitutionData> {

    private final RestCallBuilder restCallBuilder;
    private final String uri;

    public GETInstitutionData(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath("institution/");
    }

    @Override
    public Result<InstitutionData> doAPICall(final RestTemplate restTemplate, final Map<String, String> attributes) {
        final String instId = getAttribute(attributes, AttributeKeys.INSTITUTION_ID);

        try {
            return Result.of(
                    restTemplate.exchange(
                            this.uri + instId,
                            HttpMethod.GET,
                            this.restCallBuilder
                                    .httpEntity()
                                    .withContentTypeJson()
                                    .build(),
                            new ParameterizedTypeReference<InstitutionData>() {
                            })
                            .getBody());
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }

}
