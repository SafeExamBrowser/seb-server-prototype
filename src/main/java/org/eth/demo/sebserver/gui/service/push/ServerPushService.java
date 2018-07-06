/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.push;

import java.util.function.Consumer;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class ServerPushService {

    private static final Logger log = LoggerFactory.getLogger(ServerPushService.class);

    public void runServerPush(
            final ServerPushContext context,
            final Consumer<ServerPushContext> business,
            final Consumer<ServerPushContext> update) {

        final ServerPushSession pushSession = new ServerPushSession();

        pushSession.start();
        final Thread bgThread = new Thread(() -> {
            while (!context.isDisposed() && context.runAgain()) {

                log.trace("Call business on Server Push Session on: {}", Thread.currentThread().getName());

                business.accept(context);

                if (!context.isDisposed()) {

                    log.trace("Call update on Server Push Session on: {}", Thread.currentThread().getName());

                    context.getDisplay().asyncExec(() -> {
                        try {
                            update.accept(context);
                        } catch (final Exception e) {
                            log.warn(
                                    "Failed to update on Server Push Session {}. It seems that the UISession is not available anymore. This may source from a connection interruption",
                                    Thread.currentThread().getName(), e);
                        }
                    });
                }
            }

            log.info("Stop Server Push Session on: {}", Thread.currentThread().getName());
            try {
                pushSession.stop();
            } catch (final Exception e) {
                log.warn(
                        "Failed to stop Server Push Session on: {}. It seems that the UISession is not available anymore. This may source from a connection interruption",
                        Thread.currentThread().getName(), e);
            }

        });

        log.info("Start new Server Push Session on: {}", bgThread.getName());

        bgThread.setDaemon(true);
        bgThread.start();
    }
}
