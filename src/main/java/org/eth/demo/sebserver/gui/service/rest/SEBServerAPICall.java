/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eth.demo.sebserver.gui.service.AttributeMapBuilder;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormBinding;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormPOST;
import org.eth.demo.util.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public interface SEBServerAPICall<T> {

    Result<T> doAPICall(
            RestTemplate restTemplate,
            Map<String, String> attributes);

    default Result<T> doAPICall(final RestTemplate restTemplate) {
        return doAPICall(restTemplate, Collections.emptyMap());
    }

    default String getAttribute(final Map<String, String> attrs, final String name) {
        if (!attrs.containsKey(name)) {
            throw new IllegalArgumentException("Missing attribute: " + name + " in attributes: " + attrs);
        }
        return attrs.get(name);
    }

    default APICallBuilder<T> with(final AuthorizationContextHolder authorizationContextHolder) {
        return with(authorizationContextHolder.getAuthorizationContext());
    }

    default APICallBuilder<T> with(final SEBServerAuthorizationContext authorizationContext) {
        return with(authorizationContext.getRestTemplate());
    }

    default APICallBuilder<T> with(final RestTemplate restTemplate) {
        return new APICallBuilder<>(this, restTemplate);
    }

    default HttpHeaders getHeadersFromCurrentRequest() {
        final HttpServletRequest request = RWT.getRequest();

        return Collections.list(request.getHeaderNames())
                .stream()
                .reduce(new HttpHeaders(),
                        (headers, name) -> {
                            headers.addAll(name, Collections.list(request.getHeaders(name)));
                            return headers;
                        },
                        (headers1, headers2) -> {
                            headers1.addAll(headers2);
                            return headers1;
                        });
    }

    public class APICallBuilder<T> extends AttributeMapBuilder<APICallBuilder<T>> {

        private final SEBServerAPICall<T> call;
        private RestTemplate restTemplate;
        private FormBinding formBinding;

        APICallBuilder(
                final SEBServerAPICall<T> call,
                final RestTemplate restTemplate) {

            this.call = call;
            this.restTemplate = restTemplate;
        }

        public APICallBuilder<T> withFormBinding(final FormBinding formBinding) {
            this.formBinding = formBinding;
            return this;
        }

        public RestTemplate getRestTemplate() {
            return restTemplate;
        }

        public void setRestTemplate(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public final Result<T> doAPICall() {
            if (formBinding != null) {
                try {
                    this.attributes.put(FormPOST.JSON_FORM_POST, formBinding.getJson());
                } catch (final Exception e) {
                    return Result.ofError(e);
                }
            }
            return call.doAPICall(restTemplate, attributes);
        }
    }
}
