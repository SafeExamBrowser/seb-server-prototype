/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.event;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.page.ActionPane;
import org.eth.demo.sebserver.gui.service.page.TODOTemplate;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.content.InstitutionForm;

public class ActivitySelection {

    public static final Consumer<TreeItem> EMPTY_FUNCTION = ti -> {
    };
    public static final Consumer<TreeItem> COLLAPSE_NONE_EMPTY = ti -> {
        ti.removeAll();
        ti.setItemCount(1);
    };

    public enum Activity {
        NONE(TODOTemplate.class, TODOTemplate.class),
        INSTITUTIONS(TODOTemplate.class, TODOTemplate.class),
        INSTITUTION(InstitutionForm.class, ActionPane.class, AttributeKeys.INSTITUTION_ID),

        USER(TODOTemplate.class, TODOTemplate.class),
        LMS_SETUP(TODOTemplate.class, TODOTemplate.class),

        EXAMS(TODOTemplate.class, TODOTemplate.class),
        EXAM(TODOTemplate.class, TODOTemplate.class),
        SEB_CONFIG(TODOTemplate.class, TODOTemplate.class),
        MONITORING(TODOTemplate.class, TODOTemplate.class),

        ;

        public final Class<? extends TemplateComposer> objectPaneComposer;
        public final Class<? extends TemplateComposer> selectionPaneComposer;
        public final String objectIdentifierAttribute;

        private Activity(
                final Class<? extends TemplateComposer> objectPaneComposer,
                final Class<? extends TemplateComposer> selectionPaneComposer) {

            this.objectPaneComposer = objectPaneComposer;
            this.selectionPaneComposer = selectionPaneComposer;
            this.objectIdentifierAttribute = null;
        }

        private Activity(
                final Class<? extends TemplateComposer> objectPaneComposer,
                final Class<? extends TemplateComposer> selectionPaneComposer,
                final String objectIdentifierAttribute) {

            this.objectPaneComposer = objectPaneComposer;
            this.selectionPaneComposer = selectionPaneComposer;
            this.objectIdentifierAttribute = objectIdentifierAttribute;
        }

        public final ActivitySelection selection() {
            return new ActivitySelection(this);
        }
    }

    private static final String ATTR_ACTIVITY_SELECTION = "ACTIVITY_SELECTION";

    public final Activity activity;
    String objectIdentifier;
    Consumer<TreeItem> expandFunction = EMPTY_FUNCTION;
    Consumer<TreeItem> collapseFunction = EMPTY_FUNCTION;

    ActivitySelection(final Activity activity) {
        this.activity = activity;
        this.expandFunction = EMPTY_FUNCTION;
        this.collapseFunction = EMPTY_FUNCTION;
    }

    public ActivitySelection withExpandFunction(final Consumer<TreeItem> expandFunction) {
        if (expandFunction == null) {
            this.expandFunction = EMPTY_FUNCTION;
        }
        this.expandFunction = expandFunction;
        return this;
    }

    public ActivitySelection withCollapseFunction(final Consumer<TreeItem> collapseFunction) {
        if (collapseFunction == null) {
            this.collapseFunction = EMPTY_FUNCTION;
        }
        this.collapseFunction = collapseFunction;
        return this;
    }

    public ActivitySelection with(final String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
        return this;
    }

    public static ActivitySelection get(final TreeItem item) {
        return (ActivitySelection) item.getData(ATTR_ACTIVITY_SELECTION);
    }

    public static void set(final TreeItem item, final ActivitySelection selection) {
        item.setData(ATTR_ACTIVITY_SELECTION, selection);
    }

    public String getObjectIdentifier() {
        return this.objectIdentifier;
    }

    public void processExpand(final TreeItem item) {
        this.expandFunction.accept(item);
    }

    public void processCollapse(final TreeItem item) {
        this.collapseFunction.accept(item);
    }
}
