/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.push;

import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Service;

@Service
public class ServerPushService {

    public <T extends ServerPushData> void runServerPush(
            final Supplier<T> business,
            final Function<T, Runnable> update) {

        final ServerPushSession pushSession = new ServerPushSession();

        pushSession.start();
        final Thread bgThread = new Thread(() -> {
            T data = business.get();
            while (!data.isDisposed()) {
                final Display display = data.getDisplay();
                if (!display.isDisposed()) {
                    display.asyncExec(update.apply(data));
                }

                data = business.get();
            }

            pushSession.stop();
        });
        bgThread.setDaemon(true);
        bgThread.start();
    }
}
