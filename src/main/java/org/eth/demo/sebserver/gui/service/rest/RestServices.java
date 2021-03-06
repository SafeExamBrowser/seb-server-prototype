/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.util.Result;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class RestServices {

    private final Map<String, SEBServerAPICall<?>> sebServerAPICalls = new HashMap<>();

    private final AuthorizationContextHolder authorizationContextHolder;

    public RestServices(
            final Collection<SEBServerAPICall<?>> apiCalls,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.authorizationContextHolder = authorizationContextHolder;
        for (final SEBServerAPICall<?> apiCall : apiCalls) {
            this.sebServerAPICalls.put(apiCall.getClass().getName(), apiCall);
        }
    }

    public final <V, T extends SEBServerAPICall<V>> Result<V> sebServerCall(final Class<T> type) {
        return sebServerAPICall(type).doAPICall();
    }

    public <V, T extends SEBServerAPICall<V>> APICallBuilder<V> sebServerAPICall(final Class<T> type) {
        if (this.sebServerAPICalls.containsKey(type.getName())) {
            return type
                    .cast(this.sebServerAPICalls.get(type.getName()))
                    .with(this.authorizationContextHolder);
        }

        throw new IllegalArgumentException("Unknown SEBServerAPICall of type: " + type.getName());
    }

}
