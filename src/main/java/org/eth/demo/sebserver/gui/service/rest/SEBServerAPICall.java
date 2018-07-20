/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
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

    public class APICallBuilder<T> {

        private final SEBServerAPICall<T> call;
        private RestTemplate restTemplate;
        protected final Map<String, String> attributes = new HashMap<>();

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

        public APICallBuilder<T> attribute(final String name, final String value) {
            attributes.put(name, value);
            return this;
        }

        public APICallBuilder<T> exam(final String examId) {
            attributes.put(AttributeKeys.EXAM_ID, examId);
            return this;
        }

        public APICallBuilder<T> toState(final String stateId) {
            attributes.put(AttributeKeys.STATE_ID, stateId);
            return this;
        }

        public APICallBuilder<T> config(final String configId) {
            attributes.put(AttributeKeys.CONFIG_ID, configId);
            return this;
        }

        public APICallBuilder<T> configViewName(final String name) {
            attributes.put(AttributeKeys.CONFIG_VIEW_NAME, name);
            return this;
        }

        public APICallBuilder<T> configAttributeNames(final String configAttrs) {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_NAMES, configAttrs);
            return this;
        }

        public APICallBuilder<T> singleAttribute() {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveValue");
            return this;
        }

        public APICallBuilder<T> tableAttribute() {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveTable");
            return this;
        }

        public APICallBuilder<T> attributeValue(final String value) {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_VALUE, value);
            return this;
        }

        public APICallBuilder<T> authHeader(final String authHeader) {
            attributes.put(AttributeKeys.AUTHORIZATION_HEADER, authHeader);
            return this;
        }

        public APICallBuilder<T> username(final String username) {
            attributes.put(AttributeKeys.USER_NAME, username);
            return this;
        }

        public APICallBuilder<T> password(final String password) {
            attributes.put(AttributeKeys.PASSWORD, password);
            return this;
        }

        public final void clear() {
            attributes.clear();
        }

        public final Response<T> doAPICall() {
            return call.doAPICall(restTemplate, attributes);
        }
    }
}
