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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.widgets.I18nLabel;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory.IconButtonType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class MainPageForm implements TemplateComposer {

    private final WidgetFactory widgetFactory;
    private final I18nSupport i18nSupport;

    public MainPageForm(final WidgetFactory widgetFactory, final I18nSupport i18nSupport) {
        this.widgetFactory = widgetFactory;
        this.i18nSupport = i18nSupport;
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
        nav.setLayout(new GridLayout());

        final Label activities = new Label(nav, SWT.NONE);
        activities.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        activities.setText("Activities");
        activities.setData(RWT.CUSTOM_VARIANT, "h3");

        final Tree navigation = new Tree(nav, SWT.SINGLE | SWT.FULL_SELECTION);
        navigation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TreeItem item = new TreeItem(navigation, SWT.NONE);
        item.setText("Item1");
        final TreeItem item1 = new TreeItem(item, SWT.NONE);
        item1.setText("Item2");

        final Composite content = new Composite(mainSash, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new GridLayout());

        final Label toggleView = this.widgetFactory.imageButton(
                IconButtonType.MAXIMIZE,
                content,
                "org.sebserver.mainpage.maximize.tooltip",
                event -> {
                    final I18nLabel ib = (I18nLabel) event.widget;
                    if ((Boolean) ib.getData("fullScreen")) {
                        mainSash.setWeights(new int[] { 15, 70, 15 });
                        ib.setData("fullScreen", false);
                        ib.setImage(WidgetFactory.IconButtonType.MAXIMIZE.getImage(ib.getDisplay()));
                        ib.setLocToolTipKey(new LocTextKey("org.sebserver.mainpage.maximize.tooltip"),
                                this.i18nSupport);
                    } else {
                        mainSash.setWeights(new int[] { 0, 100, 0 });
                        ib.setData("fullScreen", true);
                        ib.setImage(WidgetFactory.IconButtonType.MINIMIZE.getImage(ib.getDisplay()));
                        ib.setLocToolTipKey(new LocTextKey("org.sebserver.mainpage.minimize.tooltip"),
                                this.i18nSupport);
                    }
                });
        final GridData gridData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
        toggleView.setLayoutData(gridData);
        toggleView.setData("fullScreen", false);

        final Label two = new Label(content, SWT.NONE);
        two.setLayoutData(new GridData(SWT.TOP, SWT.FILL, true, true));
        two.setText("TWO");

        final Label tree = new Label(mainSash, SWT.NONE);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tree.setText("THREE");

        mainSash.setWeights(new int[] { 15, 70, 15 });
    }

}
