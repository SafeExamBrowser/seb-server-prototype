/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.MainPageForm.MainPageState;

final class ActivityListener {

    private final Composite objectPaneParent;
    private final Composite selectionPaneParent;

    public ActivityListener(final Composite objectPaneParent, final Composite selectionPaneParent) {
        this.objectPaneParent = objectPaneParent;
        this.selectionPaneParent = selectionPaneParent;
    }

    public final void notifySelection(final ComposerServiceContext composerCtx) {
        final MainPageState mainPageState = MainPageState.get();
        composerCtx.composerService.compose(
                mainPageState.activitySelection.activity.objectPaneComposer,
                composerCtx.of(this.objectPaneParent));
        composerCtx.composerService.compose(
                mainPageState.activitySelection.activity.selectionPaneComposer,
                composerCtx.of(this.selectionPaneParent));
    }
}