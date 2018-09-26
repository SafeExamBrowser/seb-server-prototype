/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

public class ActivitySelection {

    public enum Activity {
        NONE(TODOTemplate.class, TODOTemplate.class),
        ADMIN(TODOTemplate.class, TODOTemplate.class),
        USER(TODOTemplate.class, TODOTemplate.class),
        LMS_SETTUP(TODOTemplate.class, TODOTemplate.class),

        SEB_CONFIG(TODOTemplate.class, TODOTemplate.class),
        EXAM(TODOTemplate.class, TODOTemplate.class)

        ;

        final Class<? extends TemplateComposer> objectPaneComposer;
        final Class<? extends TemplateComposer> selectionPaneComposer;

        private Activity(final Class<? extends TemplateComposer> objectPaneComposer,
                final Class<? extends TemplateComposer> selectionPaneComposer) {
            this.objectPaneComposer = objectPaneComposer;
            this.selectionPaneComposer = selectionPaneComposer;
        }
    }

    public static final ActivitySelection NONE = new ActivitySelection(Activity.NONE);
    public static final ActivitySelection ADMIN = new ActivitySelection(Activity.ADMIN);

    public final Activity activity;
    public final String objectIdentifier;

    public ActivitySelection(final Activity activity) {
        this.activity = activity;
        this.objectIdentifier = null;
    }

    public ActivitySelection(final Activity activity, final String objectIdentifier) {
        this.activity = activity;
        this.objectIdentifier = objectIdentifier;
    }

}
