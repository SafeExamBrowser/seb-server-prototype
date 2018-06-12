/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.util.TypedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    private static final Logger log = LoggerFactory.getLogger(ViewService.class);

    private final ApplicationContext applicationContext;

    public ViewService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void composeView(final Composite parent, final Class<? extends ViewComposer> composerType) {
        composeView(parent, composerType, TypedMap.EMPTY_MAP);
    }

    public void composeView(
            final Composite parent,
            final Class<? extends ViewComposer> composerType,
            final TypedMap attributes) {

        final ViewComposer composer = this.applicationContext.getBean(composerType);

        if (composer.validateAttributes(attributes)) {
            clearView(parent);
            composer.composeView(this, parent, attributes);
            parent.layout();
        } else {
            log.error(
                    "Invalid or missing mandatory attributes to handle compose request of ViewComposer: {} attributes: ",
                    composerType.getName(),
                    attributes);
        }
    }

    private void clearView(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

}
