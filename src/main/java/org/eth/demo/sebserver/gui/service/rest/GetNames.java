/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.util.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public abstract class GetNames implements SEBServerAPICall<List<IdAndName>> {

    protected final RestCallBuilder restCallBuilder;
    protected final String uri;

    protected GetNames(final RestCallBuilder restCallBuilder, final String uripath) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath(uripath);
    }

    @Override
    public Result<List<IdAndName>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        try {

            final Map<String, String> mapping = restTemplate.exchange(
                    this.uri,
                    HttpMethod.GET,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .build(),
                    new ParameterizedTypeReference<Map<String, String>>() {
                    }).getBody();

            if (mapping == null || mapping.isEmpty()) {
                return Result.of(Collections.emptyList());
            } else {
                return Result.of(mapping.entrySet()
                        .stream()
                        .reduce(
                                new ArrayList<IdAndName>(),
                                (acc, inst) -> {
                                    acc.add(new IdAndName(inst.getKey(), inst.getValue()));
                                    return acc;
                                },
                                (acc1, acc2) -> {
                                    acc1.addAll(acc2);
                                    return acc1;
                                }));
            }
        } catch (final Throwable t) {
            return Result.ofError(t);
        }
    }

}
