/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.activity;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.page.TODOTemplate;
import org.eth.demo.sebserver.gui.service.page.TemplateComposer;
import org.eth.demo.sebserver.gui.service.page.action.ActionPane;
import org.eth.demo.sebserver.gui.service.page.content.ExamsListPage;
import org.eth.demo.sebserver.gui.service.page.content.InstitutionForm;
import org.eth.demo.sebserver.gui.service.page.content.InstitutionsForm;
import org.eth.demo.sebserver.gui.service.page.content.RunningExamPage;
import org.eth.demo.sebserver.gui.service.page.content.SEBConfigurationPage;

public class ActivitySelection {

    public static final Consumer<TreeItem> EMPTY_FUNCTION = ti -> {
    };
    public static final Consumer<TreeItem> COLLAPSE_NONE_EMPTY = ti -> {
        ti.removeAll();
        ti.setItemCount(1);
    };

    public enum Activity {
        NONE(TODOTemplate.class, TODOTemplate.class),
        INSTITUTIONS(InstitutionsForm.class, ActionPane.class),
        INSTITUTION(InstitutionForm.class, ActionPane.class, AttributeKeys.INSTITUTION_ID),

        USERS(TODOTemplate.class, TODOTemplate.class),

        EXAMS(ExamsListPage.class, ActionPane.class),
        SEB_CONFIGS(TODOTemplate.class, ActionPane.class),
        SEB_CONFIG(SEBConfigurationPage.class, ActionPane.class),
        SEB_CONFIG_TEMPLATES(TODOTemplate.class, ActionPane.class),
        MONITORING(TODOTemplate.class, TODOTemplate.class),
        RUNNING_EXAMS(TODOTemplate.class, TODOTemplate.class),
        RUNNING_EXAM(RunningExamPage.class, ActionPane.class, AttributeKeys.EXAM_ID),
        LOGS(TODOTemplate.class, TODOTemplate.class),
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

        public final ActivitySelection createSelection() {
            return new ActivitySelection(this);
        }

        public final ActivitySelection createSelection(final IdAndName idAndName) {
            return new ActivitySelection(this, idAndName);
        }
    }

    private static final String ATTR_ACTIVITY_SELECTION = "ACTIVITY_SELECTION";

    public final Activity activity;
    public final IdAndName idAndName;
    Consumer<TreeItem> expandFunction = EMPTY_FUNCTION;

    ActivitySelection(final Activity activity) {
        this(activity, null);
    }

    ActivitySelection(final Activity activity, final IdAndName idAndName) {
        this.activity = activity;
        this.idAndName = idAndName;
        this.expandFunction = EMPTY_FUNCTION;
    }

    public ActivitySelection withExpandFunction(final Consumer<TreeItem> expandFunction) {
        if (expandFunction == null) {
            this.expandFunction = EMPTY_FUNCTION;
        }
        this.expandFunction = expandFunction;
        return this;
    }

    public String getObjectIdentifier() {
        if (this.idAndName == null) {
            return null;
        }

        return this.idAndName.id;
    }

    public void processExpand(final TreeItem item) {
        this.expandFunction.accept(item);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activity == null) ? 0 : this.activity.hashCode());
        result = prime * result + ((this.idAndName == null) ? 0 : this.idAndName.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ActivitySelection other = (ActivitySelection) obj;
        if (this.activity != other.activity)
            return false;
        if (this.idAndName == null) {
            if (other.idAndName != null)
                return false;
        } else if (!this.idAndName.equals(other.idAndName))
            return false;
        return true;
    }

    public static ActivitySelection get(final TreeItem item) {
        return (ActivitySelection) item.getData(ATTR_ACTIVITY_SELECTION);
    }

    public static void set(final TreeItem item, final ActivitySelection selection) {
        item.setData(ATTR_ACTIVITY_SELECTION, selection);
    }

}
