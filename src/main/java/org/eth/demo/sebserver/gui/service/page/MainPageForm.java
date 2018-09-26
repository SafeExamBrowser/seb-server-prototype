/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.widgets.I18nLabel;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory.IconButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class MainPageForm implements TemplateComposer {

    private static final Logger log = LoggerFactory.getLogger(MainPageForm.class);

    public static final String ATTR_MAIN_PAGE_STATE = "MAIN_PAGE_STATE";

    private static final int ACTIVITY_PANE_WEIGHT = 15;
    private static final int OBJECTS_PANE_WEIGHT = 65;
    private static final int SELECTION_PANE_WEIGHT = 20;
    private static final int[] DEFAULT_SASH_WEIGHTS = new int[] {
            ACTIVITY_PANE_WEIGHT,
            OBJECTS_PANE_WEIGHT,
            SELECTION_PANE_WEIGHT
    };
    private static final int[] OPENED_SASH_WEIGHTS = new int[] { 0, 100, 0 };

    private final WidgetFactory widgetFactory;
    private final I18nSupport i18nSupport;

    public MainPageForm(final WidgetFactory widgetFactory, final I18nSupport i18nSupport) {
        this.widgetFactory = widgetFactory;
        this.i18nSupport = i18nSupport;
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {

        // Initialize new PageState if there is none before page compose
        final MainPageState mainPageState = (MainPageState) RWT.getUISession().getAttribute(ATTR_MAIN_PAGE_STATE);
        if (mainPageState == null) {
            RWT.getUISession().setAttribute(ATTR_MAIN_PAGE_STATE, new MainPageState());
        }

        final Composite parent = composerCtx.parent;
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final SashForm mainSash = new SashForm(parent, SWT.HORIZONTAL);
        final GridLayout gridLayout = new GridLayout();

        mainSash.setLayout(gridLayout);
        mainSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite nav = new Composite(mainSash, SWT.NONE);
        nav.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        nav.setLayout(new GridLayout());

        final Composite content = new Composite(mainSash, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout contentOuterlayout = new GridLayout();
        contentOuterlayout.marginHeight = 0;
        contentOuterlayout.marginWidth = 0;
        content.setLayout(contentOuterlayout);

        final Label toggleView = this.widgetFactory.imageButton(
                IconButtonType.MAXIMIZE,
                content,
                "org.sebserver.mainpage.maximize.tooltip",
                event -> {
                    final I18nLabel ib = (I18nLabel) event.widget;
                    if ((Boolean) ib.getData("fullScreen")) {
                        mainSash.setWeights(DEFAULT_SASH_WEIGHTS);
                        ib.setData("fullScreen", false);
                        ib.setImage(WidgetFactory.IconButtonType.MAXIMIZE.getImage(ib.getDisplay()));
                        ib.setLocToolTipKey(new LocTextKey("org.sebserver.mainpage.maximize.tooltip"),
                                this.i18nSupport);
                    } else {
                        mainSash.setWeights(OPENED_SASH_WEIGHTS);
                        ib.setData("fullScreen", true);
                        ib.setImage(WidgetFactory.IconButtonType.MINIMIZE.getImage(ib.getDisplay()));
                        ib.setLocToolTipKey(new LocTextKey("org.sebserver.mainpage.minimize.tooltip"),
                                this.i18nSupport);
                    }
                });
        final GridData gridData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
        toggleView.setLayoutData(gridData);
        toggleView.setData("fullScreen", false);

        final Composite contentObjects = new Composite(content, SWT.NONE);
        contentObjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout contentObjectslayout = new GridLayout();
        contentObjectslayout.marginHeight = 0;
        contentObjectslayout.marginWidth = 0;
        contentObjects.setLayout(contentObjectslayout);

        final Composite selectionPane = new Composite(mainSash, SWT.NONE);
        selectionPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout selectionPaneGrid = new GridLayout();
        selectionPane.setLayout(selectionPaneGrid);
        selectionPane.setData(RWT.CUSTOM_VARIANT, "selectionPane");

        final ActivityListener activityListener = new ActivityListener(contentObjects, selectionPane);
        composerCtx.composerService.compose(
                ActivitiesPane.class,
                composerCtx.of(nav, activityListener));
        activityListener.notifySelection(composerCtx);

        mainSash.setWeights(DEFAULT_SASH_WEIGHTS);
    }

    public final static class MainPageState {

        ActivitySelection activitySelection = ActivitySelection.NONE;

        static MainPageState get() {
            try {
                return (MainPageState) RWT.getUISession().getAttribute(ATTR_MAIN_PAGE_STATE);
            } catch (final Exception e) {
                log.error("Unexpected error while trying to get MainPageState from user-session");
            }

            return null;
        }
    }

}
