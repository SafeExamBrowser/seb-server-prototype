/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import org.eclipse.swt.widgets.Event;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigTablePopupMenu implements PopupMenuComposer {

    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public SEBConfigTablePopupMenu(
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public void onMenuEvent(final Event event) {
        // TODO Auto-generated method stub

    }

}
