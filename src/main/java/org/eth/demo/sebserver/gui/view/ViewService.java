/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    private final ApplicationContext applicationContext;

    public ViewService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void composeView(final Composite parent, final Class<? extends ViewComposer> composerType) {
        clearView(parent);
        final ViewComposer composer = this.applicationContext.getBean(composerType);
        composer.composeView(this, parent);
        parent.layout();
    }

    private void clearView(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

}
