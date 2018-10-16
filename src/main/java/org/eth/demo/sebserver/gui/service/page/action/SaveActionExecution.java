/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveActionExecution implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SaveActionExecution.class);

    private final PageContext pageContext;
    private final ActionDefinition actionDefinition;
    private final Runnable actionExecution;

    public SaveActionExecution(
            final PageContext pageContext,
            final ActionDefinition actionDefinition,
            final Runnable actionExecution) {

        this.pageContext = pageContext;
        this.actionDefinition = actionDefinition;
        this.actionExecution = actionExecution;
    }

    @Override
    public void run() {
        try {
            this.actionExecution.run();
        } catch (final Throwable t) {
            log.error("Failed to execute action: {}", this.actionDefinition, t);
            this.pageContext.notifyError("action.error.unexpected.message", t);
        }
    }

}
