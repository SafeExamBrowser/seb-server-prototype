/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eth.demo.sebserver.gui.views.AttributeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Lazy
@Component
public class RestCallBuilder {

    private static final Logger log = LoggerFactory.getLogger(RestCallBuilder.class);

    @Value("${server.address}")
    private String webServerAdress;
    @Value("${server.port}")
    private String webServerPort;
    @Value("${sebserver.webservice.protocol}")
    private String webProtocol;

    public String withPath(final String path) {
        return UriComponentsBuilder
                .fromHttpUrl(this.webProtocol + "://" + this.webServerAdress)
                .port(this.webServerPort)
                .path(path)
                .toUriString();
    }

    public <T> HttpEntityBuilder<T> httpEntity() {
        return new HttpEntityBuilder<>();
    }

    public final class HttpEntityBuilder<T> {
        private final HttpHeaders httpHeaders = new HttpHeaders();
        private T body = null;

        public HttpEntityBuilder<T> withHeaders(final HttpHeaders headers) {
            this.httpHeaders.addAll(headers);
            return this;
        }

        public HttpEntityBuilder<T> withContentTypeJson() {
            this.httpHeaders.set(HttpHeaders.CONTENT_TYPE, SEBServerAPICall.CONTENT_TYPE_APPLICATION_JSON);
            return this;
        }

        public HttpEntityBuilder<T> withAuth(final Map<String, String> attributes) {
            if (attributes.containsKey(AttributeKeys.AUTHORIZATION_HEADER)) {
                this.httpHeaders.set(
                        HttpHeaders.AUTHORIZATION,
                        attributes.get(AttributeKeys.AUTHORIZATION_HEADER));
            } else {
                try {
                    this.httpHeaders.set(
                            HttpHeaders.AUTHORIZATION,
                            RWT.getRequest().getHeader(HttpHeaders.AUTHORIZATION));
                } catch (final Exception e) {
                    log.error("Error while trying to get AUTHORIZATION_HEADER from RWT context request: ", e);
                }
            }

            return this;
        }

        public HttpEntityBuilder<T> withHeader(final String name, final String value) {
            this.httpHeaders.set(name, value);
            return this;
        }

        public HttpEntityBuilder<T> withBody(final T body) {
            this.body = body;
            return this;
        }

        public HttpEntity<T> build() {
            if (this.body != null) {
                return new HttpEntity<>(this.body, this.httpHeaders);
            } else {
                return new HttpEntity<>(this.httpHeaders);
            }
        }

    }
}
