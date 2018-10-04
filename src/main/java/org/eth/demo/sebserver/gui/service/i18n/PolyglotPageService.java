/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.i18n;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.PageTreeTraversal;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** Service that supports page language change on the fly */
@Lazy
@Component
public final class PolyglotPageService {

    public static final String POLYGLOT_WIDGET_FUNCTION_KEY = "POLYGLOT_WIDGET_FUNCTION";
    private static final String POLYGLOT_TREE_ITEM_TEXT_DATA_KEY = "POLYGLOT_TREE_ITEM_TEXT_DATA";

    private final I18nSupport i18nSupport;

    public PolyglotPageService(final I18nSupport i18nSupport) {
        this.i18nSupport = i18nSupport;
    }

    public void setDefaultPageLocale(final Composite root) {
        setPageLocale(root, this.i18nSupport.getCurrentLocale());
    }

    @SuppressWarnings("unchecked")
    public void setPageLocale(final Composite root, final Locale locale) {
        this.i18nSupport.setSessionLocale(locale);
        PageTreeTraversal.traversePageTree(
                root,
                comp -> comp.getData(POLYGLOT_WIDGET_FUNCTION_KEY) != null,
                comp -> ((Consumer<Control>) comp.getData(POLYGLOT_WIDGET_FUNCTION_KEY)).accept(comp));

        root.layout(true, true);
    }

    public void injectI18n(final Label label, final LocTextKey locTextKey) {
        final Consumer<Label> labelFunction = labelFunction(locTextKey, null, this.i18nSupport);
        label.setData(POLYGLOT_WIDGET_FUNCTION_KEY, labelFunction);
        labelFunction.accept(label);
    }

    public void injectI18n(final Label label, final LocTextKey locTextKey, final LocTextKey locToolTipKey) {
        final Consumer<Label> labelFunction = labelFunction(locTextKey, locToolTipKey, this.i18nSupport);
        label.setData(POLYGLOT_WIDGET_FUNCTION_KEY, labelFunction);
        labelFunction.accept(label);
    }

    public void injectI18n(final Button button, final LocTextKey locTextKey) {
        final Consumer<Button> buttonFunction = buttonFunction(locTextKey, null, this.i18nSupport);
        button.setData(POLYGLOT_WIDGET_FUNCTION_KEY, buttonFunction);
        buttonFunction.accept(button);
    }

    public void injectI18n(final Button button, final LocTextKey locTextKey, final LocTextKey locToolTipKey) {
        final Consumer<Button> buttonFunction = buttonFunction(locTextKey, locToolTipKey, this.i18nSupport);
        button.setData(POLYGLOT_WIDGET_FUNCTION_KEY, buttonFunction);
        buttonFunction.accept(button);
    }

    public void injectI18n(final Tree tree) {
        tree.setData(POLYGLOT_WIDGET_FUNCTION_KEY, treeFunction());
    }

    public void injectI18n(final TreeItem treeItem, final LocTextKey locTextKey) {
        treeItem.setData(POLYGLOT_TREE_ITEM_TEXT_DATA_KEY, locTextKey);
    }

    public void injectI18n(final Combo combo, final List<String> items) {
        final Consumer<Combo> comboFunction = comboFunction(items, this.i18nSupport);
        combo.setData(POLYGLOT_WIDGET_FUNCTION_KEY, comboFunction);
        comboFunction.accept(combo);
    }

    public void createLanguageSelector(final ComposerServiceContext composerCtx) {
        for (final Locale locale : this.i18nSupport.supportedLanguages()) {
            final Label languageSelection = new Label(composerCtx.parent, SWT.NONE);
            languageSelection.setData(POLYGLOT_WIDGET_FUNCTION_KEY,
                    langSelectionLabelFunction(locale, this.i18nSupport));
            languageSelection.setData(RWT.CUSTOM_VARIANT, "header");
            languageSelection.setText("|  " + locale.getLanguage().toUpperCase());
            //languageSelection.updateLocale(this.i18nSupport);
            languageSelection.addListener(SWT.MouseDown, event -> {
                setPageLocale(composerCtx.root, locale);
            });
        }
    }

    private Consumer<Tree> treeFunction() {
        return tree -> updateLocale(tree.getItems(), this.i18nSupport);
    }

    private static final Consumer<Label> langSelectionLabelFunction(
            final Locale locale,
            final I18nSupport i18nSupport) {

        return label -> label.setVisible(
                !i18nSupport.getCurrentLocale()
                        .getLanguage()
                        .equals(locale.getLanguage()));
    }

    private static final Consumer<Label> labelFunction(
            final LocTextKey locTextKey,
            final LocTextKey locToolTipKey,
            final I18nSupport i18nSupport) {

        return label -> {
            if (locTextKey != null) {
                label.setText(i18nSupport.getText(locTextKey));
            }
            if (locToolTipKey != null) {
                label.setToolTipText(i18nSupport.getText(locToolTipKey));
            }
        };
    }

    private static final Consumer<Combo> comboFunction(
            final List<String> items,
            final I18nSupport i18nSupport) {

        return combo -> {
            int i = 0;
            final Iterator<String> iterator = items.iterator();
            while (iterator.hasNext()) {
                combo.setItem(i, i18nSupport.getText(iterator.next()));
                i++;
            }
        };
    }

    private static final Consumer<Button> buttonFunction(
            final LocTextKey locTextKey,
            final LocTextKey locToolTipKey,
            final I18nSupport i18nSupport) {

        return button -> {
            if (locTextKey != null) {
                button.setText(i18nSupport.getText(locTextKey));
            }
            if (locToolTipKey != null) {
                button.setToolTipText(i18nSupport.getText(locToolTipKey));
            }
        };
    }

    private static final void updateLocale(
            final TreeItem[] items,
            final I18nSupport i18nSupport) {

        if (items == null) {
            return;
        }

        for (final TreeItem childItem : items) {
            final LocTextKey locTextKey = (LocTextKey) childItem.getData(POLYGLOT_TREE_ITEM_TEXT_DATA_KEY);
            if (locTextKey != null) {
                childItem.setText(i18nSupport.getText(locTextKey));
            }
            updateLocale(childItem.getItems(), i18nSupport);
        }
    }

}
