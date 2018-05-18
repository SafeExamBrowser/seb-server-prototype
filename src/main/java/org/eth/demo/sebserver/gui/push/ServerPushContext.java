/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.push;

import java.util.function.Predicate;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eth.demo.sebserver.util.TypedMap;

/** ServerPushContext defines the state of a server push session.
 *
 * @author anhefti */
public final class ServerPushContext {

    private final Widget anchor;
    private final Predicate<ServerPushContext> runAgain;
    private final TypedMap dataMapping = new TypedMap();

    public ServerPushContext(final Widget anchor) {
        this(anchor, context -> false);
    }

    public ServerPushContext(
            final Widget anchor,
            final Predicate<ServerPushContext> runAgain) {

        this.anchor = anchor;
        this.runAgain = runAgain;
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

    public TypedMap getDataMapping() {
        return this.dataMapping;
    }

}
