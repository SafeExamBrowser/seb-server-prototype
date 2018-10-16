/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.institution;

import java.util.Map;

import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class NewInstitution implements SEBServerAPICall<IdAndName> {

    private final RestCallBuilder restCallBuilder;
    private final String uri;

    public NewInstitution(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath("institution/create");
    }

    @Override
    public Result<IdAndName> doAPICall(final RestTemplate restTemplate, final Map<String, String> attributes) {
        try {
            return Result.of(restTemplate.postForObject(
                    this.uri,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .build(),
                    IdAndName.class));
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }
}
