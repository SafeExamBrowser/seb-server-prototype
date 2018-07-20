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
import java.util.Optional;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETConfigAttribute implements SEBServerAPICall<Map<String, GUIViewAttribute>> {

    private final RestCallBuilder restCallBuilder;

    public GETConfigAttribute(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
    }

    @Override
    public Response<Map<String, GUIViewAttribute>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String viewName = getAttribute(attributes, AttributeKeys.CONFIG_VIEW_NAME);

        try {
            final ResponseEntity<List<GUIViewAttribute>> request = restTemplate.exchange(
                    this.restCallBuilder
                            .withPath("sebconfig/attributes/" + viewName),
                    HttpMethod.GET,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .build(),
                    new ParameterizedTypeReference<List<GUIViewAttribute>>() {
                    });

            final Map<String, GUIViewAttribute> result = request.getBody()
                    .stream()
                    .collect(Collectors.toMap(
                            a -> getFQName(a, request.getBody()),
                            a -> a));

            return new Response<>(result);
        } catch (final Exception e) {
            return new Response<>(e);
        }
    }

    private String getFQName(final GUIViewAttribute a, final Collection<GUIViewAttribute> attrs) {
        if (a.parentAttributeName == null) {
            return a.name;
        }

        final Optional<GUIViewAttribute> parentAttr = attrs.stream()
                .filter(attr -> attr.name.equals(a.parentAttributeName))
                .findFirst();

        if (parentAttr.isPresent()) {
            return parentAttr.get().name + "." + a.name;
        } else {
            throw new IllegalArgumentException("No Attribute found with name: " + a.parentAttributeName);
        }
    }
}
