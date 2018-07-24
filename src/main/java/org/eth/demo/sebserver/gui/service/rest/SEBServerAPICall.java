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
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eth.demo.sebserver.gui.service.AttributeMapBuilder;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public interface SEBServerAPICall<T> {

    Response<T> doAPICall(
            RestTemplate restTemplate,
            Map<String, String> attributes);

    default Response<T> doAPICall(final RestTemplate restTemplate) {
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
        return new APICallBuilder<>(
                this,
                authorizationContext.getRestTemplate());
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

    public final class Response<T> {
        public final T t;
        public final Throwable error;

        public Response(final T t) {
            this.t = t;
            error = null;
        }

        public Response(final Throwable error) {
            t = null;
            this.error = error;
        }

        public T orElse(final Consumer<Throwable> errorHandler) {
            if (t != null) {
                return t;
            }

            errorHandler.accept(error);
            return null;
        }
    }

    public class APICallBuilder<T> extends AttributeMapBuilder<APICallBuilder<T>> {

        private final SEBServerAPICall<T> call;
        private RestTemplate restTemplate;

        APICallBuilder(
                final SEBServerAPICall<T> call,
                final RestTemplate restTemplate) {

            this.call = call;
            this.restTemplate = restTemplate;
        }

        public RestTemplate getRestTemplate() {
            return restTemplate;
        }

        public void setRestTemplate(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public final Response<T> doAPICall() {
            return call.doAPICall(restTemplate, attributes);
        }
    }
}
