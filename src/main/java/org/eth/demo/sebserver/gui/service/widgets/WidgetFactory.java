/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.widgets;

import java.io.InputStream;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
import org.eth.demo.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WidgetFactory {

    private static final Logger log = LoggerFactory.getLogger(WidgetFactory.class);

    public enum IconButtonType {
        MAXIMIZE("maximize.png"),
        MINIMIZE("minimize.png"),
        SAVE_ACTION("saveAction.png"),
        NEW_ACTION("newAction.png"),
        DELETE_ACTION("deleteAction.png"),
        ;

        private String fileName;
        private ImageData image = null;

        private IconButtonType(final String fileName) {
            this.fileName = fileName;
        }

        public Image getImage(final Device device) {
            if (this.image == null) {
                try {
                    final InputStream resourceAsStream =
                            WidgetFactory.class.getResourceAsStream("/static/images/" + this.fileName);
                    this.image = new ImageData(resourceAsStream);
                } catch (final Exception e) {
                    log.error("Failed to load resource image: {}", this.fileName, e);
                }
            }

            return new Image(device, this.image);
        }

    }

    private final PolyglotPageService polyglotPageService;

    public WidgetFactory(final PolyglotPageService polyglotPageService) {
        this.polyglotPageService = polyglotPageService;
    }

    public Button buttonLocalized(final Composite parent, final String locTextKey) {
        final Button button = new Button(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(button, new LocTextKey(locTextKey));
        return button;
    }

    public Button buttonLocalized(final Composite parent, final LocTextKey locTextKey) {
        final Button button = new Button(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(button, locTextKey);
        return button;
    }

    public Button buttonLocalized(final Composite parent, final String style, final String locTextKey) {
        final Button button = new Button(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(button, new LocTextKey(locTextKey));
        button.setData(RWT.CUSTOM_VARIANT, style);
        return button;
    }

    public Label label(final Composite parent, final String text) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        return label;
    }

    public Label labelLocalized(final Composite parent, final String locTextKey) {
        final Label label = new Label(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(label, new LocTextKey(locTextKey));
        return label;
    }

    public Label labelLocalized(final Composite parent, final LocTextKey locTextKey) {
        final Label label = new Label(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(label, locTextKey);
        return label;
    }

    public Label labelLocalized(final Composite parent, final String style, final LocTextKey locTextKey) {
        final Label label = new Label(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(label, locTextKey);
        label.setData(RWT.CUSTOM_VARIANT, style);
        return label;
    }

    public Label labelLocalized(
            final Composite parent,
            final LocTextKey locTextKey,
            final LocTextKey locToolTextKey) {

        final Label label = new Label(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(label, locTextKey, locToolTextKey);
        return label;
    }

    public Label labelLocalized(
            final Composite parent,
            final String style,
            final LocTextKey locTextKey,
            final LocTextKey locToolTextKey) {

        final Label label = new Label(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(label, locTextKey, locToolTextKey);
        label.setData(RWT.CUSTOM_VARIANT, style);
        return label;
    }

    public Tree treeLocalized(final Composite parent, final int style) {
        final Tree tree = new Tree(parent, SWT.SINGLE | SWT.FULL_SELECTION);
        this.polyglotPageService.injectI18n(tree);
        return tree;
    }

    public TreeItem treeItemLocalized(final Tree parent, final String locTextKey) {
        final TreeItem item = new TreeItem(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(item, new LocTextKey(locTextKey));
        return item;
    }

    public TreeItem treeItemLocalized(final Tree parent, final LocTextKey locTextKey) {
        final TreeItem item = new TreeItem(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(item, locTextKey);
        return item;
    }

    public TreeItem treeItemLocalized(final TreeItem parent, final String locTextKey) {
        final TreeItem item = new TreeItem(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(item, new LocTextKey(locTextKey));
        return item;
    }

    public TreeItem treeItemLocalized(final TreeItem parent, final LocTextKey locTextKey) {
        final TreeItem item = new TreeItem(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(item, locTextKey);
        return item;
    }

    public Table tableLocalized(final Composite parent) {
        final Table table = new Table(parent, SWT.NONE);
        this.polyglotPageService.injectI18n(table);
        return table;
    }

    public TableColumn tableColumnLocalized(
            final Table table,
            final LocTextKey locTextKey) {

        return tableColumnLocalized(table, locTextKey, null);
    }

    public TableColumn tableColumnLocalized(
            final Table table,
            final LocTextKey locTextKey,
            final LocTextKey locToolTextKey) {

        final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        this.polyglotPageService.injectI18n(tableColumn, locTextKey, locToolTextKey);
        return tableColumn;
    }

    public Label labelSeparator(final Composite parent) {
        final Label label = new Label(parent, SWT.SEPARATOR);
        return label;
    }

    public Label imageButton(
            final IconButtonType type,
            final Composite parent,
            final LocTextKey toolTip,
            final Listener listener) {

        final Label imageButton = labelLocalized(parent, (LocTextKey) null, toolTip);
        imageButton.setData(RWT.CUSTOM_VARIANT, "imageButton");
        imageButton.setImage(type.getImage(parent.getDisplay()));
        if (listener != null) {
            imageButton.addListener(SWT.MouseDown, listener);
        }
        return imageButton;
    }

    public Label formLabelLocalized(final Composite parent, final String locTextKey) {
        final Label label = labelLocalized(parent, locTextKey);
        final GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
        label.setLayoutData(gridData);
        return label;
    }

    public Label formValueLabel(final Composite parent, final String value, final int span) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText(value);
        final GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, span, 1);
        label.setLayoutData(gridData);
        return label;
    }

    public Text formTextInput(final Composite parent, final String value) {
        return formTextInput(parent, value, 1, 1);
    }

    public Text formTextInput(final Composite parent, final String value, final int hspan, final int vspan) {
        final Text textInput = new Text(parent, SWT.LEFT | SWT.BORDER);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, hspan, vspan);
        gridData.heightHint = 15;
        textInput.setLayoutData(gridData);
        textInput.setText(value);
        return textInput;
    }

    public Combo formSingleSelectionLocalized(
            final Composite parent,
            final String selection,
            final List<Tuple<String>> items) {

        return formSingleSelectionLocalized(parent, selection, items, 1, 1);
    }

    public Combo formSingleSelectionLocalized(
            final Composite parent,
            final String selection,
            final List<Tuple<String>> items,
            final int hspan, final int vspan) {

        final SingleSelection combo = singleSelectionLocalized(parent, items);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, hspan, vspan);
        gridData.heightHint = 25;
        combo.setLayoutData(gridData);
        combo.select(selection);
        return combo;
    }

    public void formEmpty(final Composite parent) {
        formEmpty(parent, 1, 1);
    }

    public void formEmpty(final Composite parent, final int hspan, final int vspan) {
        final Label empty = new Label(parent, SWT.LEFT);
        empty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, hspan, vspan));
        empty.setText("");
    }

    public SingleSelection singleSelectionLocalized(
            final Composite parent,
            final List<Tuple<String>> items) {

        final SingleSelection combo = new SingleSelection(parent, items);
        this.polyglotPageService.injectI18n(combo, combo.valueMapping);
        return combo;
    }

}
