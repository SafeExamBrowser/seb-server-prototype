/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.activity;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.MainPage.MainPageState;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.page.activity.ActivitySelection.Activity;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class DeleteInstitutionActionHandler implements ActivityActionHandler {

    @Override
    public ActionDefinition handlesAction() {
        return ActionDefinition.INSTITUTION_DELETE;
    }

    @Override
    public void notifyAction(final ActionEvent event, final Tree navigation, final PageContext composerCtx) {
        final String id = (String) event.source;
        final TreeItem institutions = ActivitiesPane.findItemByActivity(navigation.getItems(), Activity.INSTITUTIONS);
        final TreeItem selected = ActivitiesPane.findItemByActivity(navigation.getItems(), Activity.INSTITUTION, id);
        selected.dispose();
        final MainPageState mainPageState = MainPageState.get();
        mainPageState.activitySelection = ActivitySelection.get(institutions);
        navigation.select(institutions);
        composerCtx.notify(new ActivitySelectionEvent(mainPageState.activitySelection));
    }

}
