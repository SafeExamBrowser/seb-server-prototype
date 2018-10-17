/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageAttr;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.MainPage.MainPageState;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class DefaultPageSkeleton implements TemplateComposer {

    public static final String ATTR_CONTENT_COMPOSER_NAME = "CONTENT_COMPOSER_NAME";

    private final WidgetFactory widgetFactory;
    private final PolyglotPageService polyglotPageService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public DefaultPageSkeleton(
            final WidgetFactory widgetFactory,
            final PolyglotPageService polyglotPageService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.widgetFactory = widgetFactory;
        this.polyglotPageService = polyglotPageService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return attributes.containsKey(ATTR_CONTENT_COMPOSER_NAME);
    }

    @Override
    public void compose(final PageContext composerCtx) {

        final GridLayout skeletonLayout = new GridLayout();
        skeletonLayout.marginBottom = 0;
        skeletonLayout.marginLeft = 0;
        skeletonLayout.marginRight = 0;
        skeletonLayout.marginTop = 0;
        skeletonLayout.marginHeight = 0;
        skeletonLayout.marginWidth = 0;
        skeletonLayout.verticalSpacing = 0;
        skeletonLayout.horizontalSpacing = 0;
        composerCtx.parent.setLayout(skeletonLayout);

        composeHeader(composerCtx);
        composeLogoBar(composerCtx);
        composeContent(composerCtx);
        composeFooter(composerCtx);

        this.polyglotPageService.setDefaultPageLocale(composerCtx.root);
    }

    private void composeHeader(final PageContext composerCtx) {
        final Composite header = new Composite(composerCtx.parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginRight = 50;
        gridLayout.marginLeft = 50;
        header.setLayout(gridLayout);
        final GridData headerCell = new GridData(SWT.FILL, SWT.TOP, true, false);
        headerCell.minimumHeight = 40;
        headerCell.heightHint = 40;
        header.setLayoutData(headerCell);
        header.setData(RWT.CUSTOM_VARIANT, "header");

        final Composite headerRight = new Composite(header, SWT.NONE);
        headerRight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
        final GridLayout headerRightGrid = new GridLayout(2, false);
        headerRightGrid.marginHeight = 0;
        headerRightGrid.marginWidth = 0;
        headerRightGrid.horizontalSpacing = 20;
        headerRight.setLayout(headerRightGrid);
        headerRight.setData(RWT.CUSTOM_VARIANT, "header");

        if (this.authorizationContextHolder.getAuthorizationContext().isLoggedIn()) {
            final Label username = new Label(headerRight, SWT.NONE);
            username.setData(RWT.CUSTOM_VARIANT, "header");
            username.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
            username.setText(this.authorizationContextHolder.getAuthorizationContext().getLoggedInUser().username);

            final Button logout = this.widgetFactory.buttonLocalized(headerRight, "org.sebserver.logout");
            logout.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true));
            logout.setData(RWT.CUSTOM_VARIANT, "header");
            logout.addListener(SWT.Selection, event -> {
                final boolean logoutSuccessful = this.authorizationContextHolder
                        .getAuthorizationContext()
                        .logout();

                if (!logoutSuccessful) {
                    // TODO error handling
                }

                MainPageState.clear();

                composerCtx.composerService.compose(
                        LoginPage.class,
                        composerCtx.root,
                        new PageAttr(AttributeKeys.LGOUT_SUCCESS, "true"));
            });
        }
    }

    private void composeLogoBar(final PageContext composerCtx) {
        final Composite logoBar = new Composite(composerCtx.parent, SWT.NONE);
        final GridData logoBarCell = new GridData(SWT.FILL, SWT.TOP, false, false);
        logoBarCell.minimumHeight = 80;
        logoBarCell.heightHint = 80;
        logoBar.setLayoutData(logoBarCell);
        logoBar.setData(RWT.CUSTOM_VARIANT, "logo");
        final GridLayout logoBarLayout = new GridLayout(2, false);
        logoBarLayout.horizontalSpacing = 0;
        logoBarLayout.marginHeight = 0;
        logoBar.setLayout(logoBarLayout);

        final Composite logo = new Composite(logoBar, SWT.NONE);
        final GridData logoCell = new GridData(SWT.LEFT, SWT.CENTER, true, true);
        logoCell.minimumHeight = 80;
        logoCell.heightHint = 80;
        logoCell.minimumWidth = 400;
        logoCell.horizontalIndent = 50;
        logo.setLayoutData(logoCell);
        logo.setData(RWT.CUSTOM_VARIANT, "bgLogo");

        final Composite langSupport = new Composite(logoBar, SWT.NONE);
        final GridData langSupportCell = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        langSupportCell.heightHint = 20;
        logoCell.horizontalIndent = 50;
        langSupport.setLayoutData(langSupportCell);
        langSupport.setData(RWT.CUSTOM_VARIANT, "logo");
        final RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
        rowLayout.spacing = 7;
        rowLayout.marginRight = 70;
        langSupport.setLayout(rowLayout);

        this.polyglotPageService.createLanguageSelector(composerCtx.of(langSupport));
//        for (final Locale locale : this.i18nSupport.supportedLanguages()) {
//            final LanguageSelection languageSelection = new LanguageSelection(langSupport, locale);
//            languageSelection.updateLocale(this.i18nSupport);
//            languageSelection.addListener(SWT.MouseDown, event -> {
//                this.polyglotPageService.setPageLocale(composerCtx.root, languageSelection.locale);
//
//            });
//        }
    }

    private void composeContent(final PageContext composerCtx) {
        final Composite contentBackground = new Composite(composerCtx.parent, SWT.NONE);
        contentBackground.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentBackground.setData(RWT.CUSTOM_VARIANT, "bgContent");
        final GridLayout innerGrid = new GridLayout();
        innerGrid.marginLeft = 50;
        innerGrid.marginRight = 50;
        innerGrid.marginHeight = 0;
        innerGrid.marginWidth = 0;

        contentBackground.setLayout(innerGrid);

        final Composite content = new Composite(contentBackground, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setData(RWT.CUSTOM_VARIANT, "content");
        final GridLayout contentGrid = new GridLayout();
        contentGrid.marginHeight = 0;
        contentGrid.marginWidth = 0;
        content.setLayout(contentGrid);

        final Composite contentInner = new Composite(content, SWT.NONE);
        contentInner.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        contentInner.setLayout(gridLayout);

        final String contentComposerName = composerCtx.attributes.get(ATTR_CONTENT_COMPOSER_NAME);
        composerCtx.composerService
                .composePageContent(contentComposerName, composerCtx.of(contentInner));
    }

    private void composeFooter(final PageContext composerCtx) {
        final Composite footerBar = new Composite(composerCtx.parent, SWT.NONE);
        final GridData footerCell = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
        footerCell.minimumHeight = 30;
        footerCell.heightHint = 30;
        footerBar.setLayoutData(footerCell);
        footerBar.setData(RWT.CUSTOM_VARIANT, "bgFooter");
        final GridLayout innerBarGrid = new GridLayout();
        innerBarGrid.marginHeight = 0;
        innerBarGrid.marginWidth = 0;
        innerBarGrid.marginLeft = 50;
        innerBarGrid.marginRight = 50;
        footerBar.setLayout(innerBarGrid);

        final Composite footer = new Composite(footerBar, SWT.NONE);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        footer.setLayoutData(gridData);
        final GridLayout footerGrid = new GridLayout(2, false);
        footerGrid.marginHeight = 0;
        footerGrid.marginWidth = 0;
        footerGrid.horizontalSpacing = 0;
        footer.setLayout(footerGrid);
        footer.setData(RWT.CUSTOM_VARIANT, "footer");

        final Composite footerLeft = new Composite(footer, SWT.NONE);
        footerLeft.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
        footerLeft.setData(RWT.CUSTOM_VARIANT, "footer");
        RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
        rowLayout.marginLeft = 20;
        rowLayout.spacing = 20;
        footerLeft.setLayout(rowLayout);

        final Composite footerRight = new Composite(footer, SWT.NONE);
        footerRight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
        footerRight.setData(RWT.CUSTOM_VARIANT, "footer");
        rowLayout = new RowLayout(SWT.HORIZONTAL);
        rowLayout.marginRight = 20;
        footerRight.setLayout(rowLayout);

        this.widgetFactory.labelLocalized(footerLeft, "footer", new LocTextKey("org.sebserver.overall.imprint"));
        this.widgetFactory.labelLocalized(footerLeft, "footer", new LocTextKey("org.sebserver.overall.disclaimer"));
        //this.widgetFactory.labelLocalized(footerRight, "footer", "org.sebserver.overall.copyright");
    }

//    private final class LanguageSelection extends Label implements Polyglot {
//
//        private static final long serialVersionUID = 8110167162843383940L;
//        private final Locale locale;
//
//        public LanguageSelection(final Composite parent, final Locale locale) {
//            super(parent, SWT.NONE);
//            this.locale = locale;
//            super.setData(RWT.CUSTOM_VARIANT, "header");
//            super.setText("|  " + locale.getLanguage().toUpperCase());
//        }
//
//        @Override
//        public void updateLocale(final I18nSupport i18nSupport) {
//            super.setVisible(
//                    !i18nSupport.getCurrentLocale()
//                            .getLanguage()
//                            .equals(this.locale.getLanguage()));
//        }
//    }

}
