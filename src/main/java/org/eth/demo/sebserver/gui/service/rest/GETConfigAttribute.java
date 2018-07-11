/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class GETConfigAttribute implements RestCall<Map<String, GUIViewAttribute>> {

    private static final Logger log = LoggerFactory.getLogger(GETConfigAttribute.class);

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;

    public GETConfigAttribute(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Map<String, GUIViewAttribute> doRequest(final Map<String, String> attributes) {
        final String viewName = getAttribute(attributes, AttributeKeys.CONFIG_VIEW_NAME);

        try {
            final ResponseEntity<List<GUIViewAttribute>> request = this.restTemplate.exchange(
                    this.restCallBuilder
                            .withPath("sebconfig/attributes/" + viewName),
                    HttpMethod.GET,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .withHeaderCopy(HttpHeaders.AUTHORIZATION)
                            .build(),
                    new ParameterizedTypeReference<List<GUIViewAttribute>>() {
                    });

            final Map<String, GUIViewAttribute> result = request.getBody()
                    .stream()
                    .collect(Collectors.toMap(
                            a -> getFQName(a, request.getBody()),
                            a -> a));

            return result;
        } catch (final Exception e) {
            log.error("Error while trying to get attributes of view: {} ", viewName, e);
            return Collections.emptyMap();
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
