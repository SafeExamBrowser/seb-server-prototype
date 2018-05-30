/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.push;

import java.util.function.Predicate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eth.demo.sebserver.util.TypedMap;
import org.eth.demo.sebserver.util.TypedMap.TypedKey;
import org.springframework.web.client.RestTemplate;

/** ServerPushContext defines the state of a server push session.
 *
 * @author anhefti */
public final class ServerPushContext {

    private final Composite anchor;
    private final Predicate<ServerPushContext> runAgain;
    private final TypedMap dataMapping;
    private final RestTemplate restTemplate;

    public ServerPushContext(final Composite anchor, final RestTemplate restTemplate) {
        this(anchor, context -> false, restTemplate);

    }

    public ServerPushContext(
            final Composite anchor,
            final Predicate<ServerPushContext> runAgain,
            final RestTemplate restTemplate) {

        this.anchor = anchor;
        this.runAgain = runAgain;
        this.dataMapping = new TypedMap();
        this.restTemplate = restTemplate;
    }

    public boolean runAgain() {
        return this.runAgain.test(this);
    }

    public boolean isDisposed() {
        return this.anchor.isDisposed();
    }

    public Display getDisplay() {
        return this.anchor.getDisplay();
    }

    public <T> T getData(final TypedKey<T> key) {
        return this.dataMapping.get(key);
    }

    public <T> ServerPushContext setData(final TypedKey<T> key, final T value) {
        this.dataMapping.put(key, value);
        return this;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public void layout() {
        this.anchor.pack();
        this.anchor.layout();
        this.anchor.getParent().layout(true, true);
    }

}
