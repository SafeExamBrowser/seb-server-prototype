/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.sebconfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GetConfigAttributes implements SEBServerAPICall<Map<String, ConfigViewAttribute>> {

    private final RestCallBuilder restCallBuilder;

    public GetConfigAttributes(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Result<Map<String, ConfigViewAttribute>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String viewName = getAttribute(attributes, AttributeKeys.CONFIG_VIEW_NAME);

        try {
            final ResponseEntity<List<ConfigViewAttribute>> request = restTemplate.exchange(
                    this.restCallBuilder
                            .withPath("sebconfig/attributes/" + viewName),
                    HttpMethod.GET,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .build(),
                    new ParameterizedTypeReference<List<ConfigViewAttribute>>() {
                    });

            final Map<String, ConfigViewAttribute> result = request.getBody()
                    .stream()
                    .collect(Collectors.toMap(
                            a -> getFQName(a, request.getBody()),
                            a -> a));

            return Result.of(result);
        } catch (final Exception e) {
            return Result.ofError(e);
        }
    }

    private String getFQName(final ConfigViewAttribute a, final Collection<ConfigViewAttribute> attrs) {
        if (a.parentAttributeName == null) {
            return a.name;
        }

        final Optional<ConfigViewAttribute> parentAttr = attrs.stream()
                .filter(attr -> attr.name.equals(a.parentAttributeName))
                .findFirst();

        if (parentAttr.isPresent()) {
            return parentAttr.get().name + "." + a.name;
        } else {
            throw new IllegalArgumentException("No Attribute found with name: " + a.parentAttributeName);
        }
    }
}
