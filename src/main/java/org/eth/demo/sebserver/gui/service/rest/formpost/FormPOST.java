/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest.formpost;

import java.util.ArrayList;
import java.util.Map;

import org.eth.demo.sebserver.gui.service.rest.RestCallBuilder;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall;
import org.eth.demo.sebserver.gui.service.rest.validation.FieldValidationError;
import org.eth.demo.util.Result;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public abstract class FormPOST<T> implements SEBServerAPICall<FormPostResponse<T>> {

    public static final String JSON_FORM_POST = "JSON_FORM_POST";

    private final RestCallBuilder restCallBuilder;
    private final String uri;

    protected FormPOST(final RestCallBuilder restCallBuilder, final String path) {
        this.restCallBuilder = restCallBuilder;
        this.uri = restCallBuilder.withPath(path);
    }

    @Override
    public Result<FormPostResponse<T>> doAPICall(
            final RestTemplate restTemplate,
            final Map<String, String> attributes) {

        final String json = getAttribute(attributes, JSON_FORM_POST);

        try {
            final T response = restTemplate.exchange(
                    this.uri,
                    HttpMethod.POST,
                    this.restCallBuilder
                            .httpEntity()
                            .withContentTypeJson()
                            .withBody(json)
                            .build(),
                    type())
                    .getBody();
            return Result.of(new FormPostResponse<>(response));

        } catch (final Throwable t) {
            if (t instanceof HttpClientErrorException) {
                return Result.of(createValidationErrors(((HttpClientErrorException) t).getResponseBodyAsString()));
            }
            return Result.ofError(t);
        }
    }

    protected abstract Class<T> type();

    private FormPostResponse<T> createValidationErrors(final String response) {
        final ArrayList<FieldValidationError> result = new ArrayList<>();
        // TODO extract validation errors and if possible the objectId
        return new FormPostResponse<>(null, result);
    }

}
