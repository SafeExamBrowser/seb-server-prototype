/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import org.eth.demo.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Lazy
@Component
public class RestCallBuilder {

    private final String webServerAdress;
    private final String webServerPort;
    private final String webProtocol;

    @Autowired
    public RestCallBuilder(
            @Value("${server.address}") final String webServerAdress,
            @Value("${server.port}") final String webServerPort,
            @Value("${sebserver.webservice.protocol}") final String webProtocol) {

        this.webServerAdress = webServerAdress;
        this.webServerPort = webServerPort;
        this.webProtocol = webProtocol;
    }

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
            this.httpHeaders.set(
                    HttpHeaders.CONTENT_TYPE,
                    Const.CONTENT_TYPE_APPLICATION_JSON);
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
