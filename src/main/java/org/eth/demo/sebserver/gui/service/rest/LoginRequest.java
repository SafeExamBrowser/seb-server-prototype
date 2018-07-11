/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Lazy
@Component
public class LoginRequest implements RestCall<String> {

    private final RestCallBuilder restCallBuilder;
    private final RestTemplate restTemplate;
    private final String uri;
    private final HttpHeaders headers;

    public LoginRequest(final RestCallBuilder restCallBuilder) {
        this.restCallBuilder = restCallBuilder;
        this.restTemplate = new RestTemplate();

        this.uri = restCallBuilder.withPath("doLogin");
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/x-www-form-urlencoded");
    }

    @Override
    public String doRequest(final Map<String, String> attributes) {
        final String userName = getAttribute(attributes, AttributeKeys.USER_NAME);
        final String password = getAttribute(attributes, AttributeKeys.PASSWORD);

        final ResponseEntity<String> loginResponse = this.restTemplate.exchange(
                this.uri,
                HttpMethod.POST,
                this.restCallBuilder
                        .<String> httpEntity()
                        .withHeaders(this.headers)
                        .withBody("username=" + userName + "&password=" + password)
                        .build(),
                String.class);

        return loginResponse.getHeaders().getFirst("Set-Cookie");
    }

}
