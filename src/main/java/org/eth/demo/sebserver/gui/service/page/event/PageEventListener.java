/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.event;

public interface PageEventListener<T> {

    String LISTENER_ATTRIBUTE_KEY = "PageEventListener";

    boolean match(Class<?> type);

    default int priority() {
        return 1;
    }

    void notify(T event);

}
