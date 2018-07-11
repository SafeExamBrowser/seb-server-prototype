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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.springframework.http.HttpHeaders;

public interface RestCall<T> {

    String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    T doRequest(Map<String, String> attributes);

    default T doRequest() {
        return doRequest(Collections.emptyMap());
    }

    default String getAttribute(final Map<String, String> attrs, final String name) {
        if (!attrs.containsKey(name)) {
            throw new IllegalArgumentException("Missing attribute: " + name + " in attributes: " + attrs);
        }
        return attrs.get(name);
    }

    default RequestCallBuilder<T> with() {
        return new RequestCallBuilder<>(this);
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

    public class RequestCallBuilder<T> {

        private final RestCall<T> call;
        protected final Map<String, String> attributes = new HashMap<>();

        RequestCallBuilder(final RestCall<T> call) {
            this.call = call;
        }

        public RequestCallBuilder<T> attribute(final String name, final String value) {
            attributes.put(name, value);
            return this;
        }

        public RequestCallBuilder<T> exam(final String examId) {
            attributes.put(AttributeKeys.EXAM_ID, examId);
            return this;
        }

        public RequestCallBuilder<T> toState(final String stateId) {
            attributes.put(AttributeKeys.STATE_ID, stateId);
            return this;
        }

        public RequestCallBuilder<T> config(final String configId) {
            attributes.put(AttributeKeys.CONFIG_ID, configId);
            return this;
        }

        public RequestCallBuilder<T> configViewName(final String name) {
            attributes.put(AttributeKeys.CONFIG_VIEW_NAME, name);
            return this;
        }

        public RequestCallBuilder<T> configAttributeNames(final String configAttrs) {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_NAMES, configAttrs);
            return this;
        }

        public RequestCallBuilder<T> singleAttribute() {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveValue");
            return this;
        }

        public RequestCallBuilder<T> tableAttribute() {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_SAVE_TYPE, "saveTable");
            return this;
        }

        public RequestCallBuilder<T> attributeValue(final String value) {
            attributes.put(AttributeKeys.CONFIG_ATTRIBUTE_VALUE, value);
            return this;
        }

        public final void clear() {
            attributes.clear();
        }

        public final T doRequest() {
            return call.doRequest(attributes);
        }

    }

}
