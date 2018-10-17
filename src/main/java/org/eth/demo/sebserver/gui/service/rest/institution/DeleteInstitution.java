/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.institution;

import java.util.Map;

import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class DeleteInstitution implements SEBServerAPICall<String> {

    private final RestCallBuilder restCallBuilder;
    private final String uri;

    public DeleteInstitution(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath("institution/delete/");
    }

    @Override
    public Result<String> doAPICall(final RestTemplate restTemplate, final Map<String, String> attributes) {
        final String instId = getAttribute(attributes, AttributeKeys.INSTITUTION_ID);

        try {
            return Result.of(restTemplate.postForObject(
                    this.uri + instId,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .build(),
                    String.class));
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }

}
