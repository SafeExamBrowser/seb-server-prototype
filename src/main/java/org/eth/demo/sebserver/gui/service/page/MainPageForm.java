/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelection.Activity;
import org.eth.demo.sebserver.gui.service.page.event.ActivitySelectionListener;
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

    private static final int ACTIVITY_PANE_WEIGHT = 20;
    private static final int OBJECTS_PANE_WEIGHT = 65;
    private static final int SELECTION_PANE_WEIGHT = 15;
    private static final int[] DEFAULT_SASH_WEIGHTS = new int[] {
            ACTIVITY_PANE_WEIGHT,
            OBJECTS_PANE_WEIGHT,
            SELECTION_PANE_WEIGHT
    };
    private static final int[] OPENED_SASH_WEIGHTS = new int[] { 0, 100, 0 };

    private final WidgetFactory widgetFactory;
    private final PolyglotPageService polyglotPageService;

    public MainPageForm(
            final WidgetFactory widgetFactory,
            final PolyglotPageService polyglotPageService) {

        this.widgetFactory = widgetFactory;
        this.polyglotPageService = polyglotPageService;
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {
        final Composite parent = composerCtx.parent;
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final SashForm mainSash = new SashForm(parent, SWT.HORIZONTAL);
        final GridLayout gridLayout = new GridLayout();

        mainSash.setLayout(gridLayout);
        mainSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite nav = new Composite(mainSash, SWT.NONE);
        nav.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout navLayout = new GridLayout();
        navLayout.marginHeight = 20;
        navLayout.marginLeft = 10;
        nav.setLayout(navLayout);

        final Composite content = new Composite(mainSash, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout contentOuterlayout = new GridLayout();
        contentOuterlayout.marginHeight = 0;
        contentOuterlayout.marginWidth = 0;
        content.setLayout(contentOuterlayout);

        final Label toggleView = this.widgetFactory.imageButton(
                IconButtonType.MAXIMIZE,
                content,
                new LocTextKey("org.sebserver.mainpage.maximize.tooltip"),
                event -> {
                    final Label ib = (Label) event.widget;
                    if ((Boolean) ib.getData("fullScreen")) {
                        mainSash.setWeights(DEFAULT_SASH_WEIGHTS);
                        ib.setData("fullScreen", false);
                        ib.setImage(WidgetFactory.IconButtonType.MAXIMIZE.getImage(ib.getDisplay()));
                        this.polyglotPageService.injectI18n(
                                ib,
                                null,
                                new LocTextKey("org.sebserver.mainpage.maximize.tooltip"));
                    } else {
                        mainSash.setWeights(OPENED_SASH_WEIGHTS);
                        ib.setData("fullScreen", true);
                        ib.setImage(WidgetFactory.IconButtonType.MINIMIZE.getImage(ib.getDisplay()));
                        this.polyglotPageService.injectI18n(
                                ib,
                                null,
                                new LocTextKey("org.sebserver.mainpage.minimize.tooltip"));
                    }
                });
        final GridData gridData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
        toggleView.setLayoutData(gridData);
        toggleView.setData("fullScreen", false);

        final Composite contentObjects = new ContentPaneComposite(content, composerCtx);
        contentObjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout contentObjectslayout = new GridLayout();
        contentObjectslayout.marginHeight = 0;
        contentObjectslayout.marginWidth = 0;
        contentObjects.setLayout(contentObjectslayout);

        final Composite selectionPane = new SelectionPaneComposite(mainSash, composerCtx);
        selectionPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout selectionPaneGrid = new GridLayout();
        selectionPane.setLayout(selectionPaneGrid);
        selectionPane.setData(RWT.CUSTOM_VARIANT, "selectionPane");

//        final ActivityListener activityListener = new ActivityListener(contentObjects, selectionPane);
        composerCtx.composerService.compose(
                ActivitiesPane.class,
                composerCtx.of(nav));

        mainSash.setWeights(DEFAULT_SASH_WEIGHTS);
    }

    public final static class MainPageState {

        ActivitySelection activitySelection = Activity.NONE.selection();

        private MainPageState() {
        }

        static MainPageState get() {
            try {
                final HttpSession httpSession = RWT
                        .getUISession()
                        .getHttpSession();

                MainPageState mainPageState = (MainPageState) httpSession.getAttribute(ATTR_MAIN_PAGE_STATE);
                if (mainPageState == null) {
                    mainPageState = new MainPageState();
                    httpSession.setAttribute(ATTR_MAIN_PAGE_STATE, mainPageState);
                }

                return mainPageState;
            } catch (final Exception e) {
                log.error("Unexpected error while trying to get MainPageState from user-session");
            }

            return null;
        }
    }

    public static final class ContentPaneComposite extends Composite implements ActivitySelectionListener {

        private final ComposerServiceContext composerCtx;

        ContentPaneComposite(final Composite parent, final ComposerServiceContext composerCtx) {
            super(parent, SWT.NONE);
            this.composerCtx = composerCtx;
        }

        private static final long serialVersionUID = -7003168413727710543L;

        @Override
        public void notify(final ActivitySelection activitySelection) {
            final Map<String, String> attrs = new HashMap<>(this.composerCtx.attributes);
            attrs.put(activitySelection.activity.objectIdentifierAttribute, activitySelection.getObjectIdentifier());
            this.composerCtx.composerService.compose(
                    activitySelection.activity.objectPaneComposer,
                    this.composerCtx.of(this, attrs));
        }
    }

    public static final class SelectionPaneComposite extends Composite implements ActivitySelectionListener {

        private final ComposerServiceContext composerCtx;

        SelectionPaneComposite(final Composite parent, final ComposerServiceContext composerCtx) {
            super(parent, SWT.NONE);
            this.composerCtx = composerCtx;
        }

        private static final long serialVersionUID = -7003168413727710543L;

        @Override
        public void notify(final ActivitySelection activitySelection) {
            final Map<String, String> attrs = new HashMap<>(this.composerCtx.attributes);
            attrs.put(activitySelection.activity.objectIdentifierAttribute, activitySelection.getObjectIdentifier());
            this.composerCtx.composerService.compose(
                    activitySelection.activity.selectionPaneComposer,
                    this.composerCtx.of(this, attrs));
        }
    }

}
