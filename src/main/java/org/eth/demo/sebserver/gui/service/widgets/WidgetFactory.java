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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WidgetFactory {

    private static final Logger log = LoggerFactory.getLogger(WidgetFactory.class);

    public enum IconButtonType {
        MAXIMIZE("maximize.png"),
        MINIMIZE("minimize.png");

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

    public final I18nSupport i18nSupport;

    public WidgetFactory(final I18nSupport i18nSupport) {
        this.i18nSupport = i18nSupport;
    }

    public Button buttonLocalized(final Composite parent, final String locTextKey) {
        return new I18nButton(parent, this.i18nSupport, new LocTextKey(locTextKey));
    }

    public Button buttonLocalized(final Composite parent, final String style, final String locTextKey) {
        final I18nButton i18nButton = new I18nButton(parent, this.i18nSupport, new LocTextKey(locTextKey));
        i18nButton.setData(RWT.CUSTOM_VARIANT, style);
        return i18nButton;
    }

    public Label labelLocalized(final Composite parent, final String locTextKey) {
        return new I18nLabel(parent, this.i18nSupport, new LocTextKey(locTextKey));
    }

    public Label labelLocalized(final Composite parent, final String style, final String locTextKey,
            final Object... args) {
        final I18nLabel i18nLabel = new I18nLabel(parent, this.i18nSupport, new LocTextKey(locTextKey, args));
        i18nLabel.setData(RWT.CUSTOM_VARIANT, style);
        return i18nLabel;
    }

    public TreeItem treeItemLocalized(final Tree parent, final String locTextKey) {
        return new I18nTreeItem(parent, SWT.NONE, this.i18nSupport, new LocTextKey(locTextKey));
    }

    public TreeItem treeItemLocalized(final Tree parent, final LocTextKey locTextKey) {
        return new I18nTreeItem(parent, SWT.NONE, this.i18nSupport, locTextKey);
    }

    public TreeItem treeItemLocalized(final TreeItem parent, final String locTextKey) {
        return new I18nTreeItem(parent, SWT.NONE, this.i18nSupport, new LocTextKey(locTextKey));
    }

    public TreeItem treeItemLocalized(final TreeItem parent, final LocTextKey locTextKey) {
        return new I18nTreeItem(parent, SWT.NONE, this.i18nSupport, locTextKey);
    }

    public Label labelSeparator(final Composite parent) {
        final Label label = new Label(parent, SWT.SEPARATOR);
        return label;
    }

    public Label imageButton(
            final IconButtonType type,
            final Composite parent,
            final String toolTip,
            final Listener listener) {

        final I18nLabel imageButton = new I18nLabel(parent, this.i18nSupport, null);
        if (toolTip != null) {
            imageButton.setLocToolTipKey(new LocTextKey(toolTip), this.i18nSupport);
        }
        imageButton.setData(RWT.CUSTOM_VARIANT, "imageButton");
        imageButton.setImage(type.getImage(parent.getDisplay()));
        if (listener != null) {
            imageButton.addListener(SWT.MouseDown, listener);
        }
        return imageButton;
    }

    public Label formLabelLocalized(final Composite parent, final String locTextKey) {
        final Label label = labelLocalized(parent, locTextKey);
        final GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        label.setLayoutData(gridData);
        return label;
    }

    public Text formTextInput(final Composite parent, final String value) {
        return formTextInput(parent, value, 1, 1);
    }

    public Text formTextInput(final Composite parent, final String value, final int hspan, final int vspan) {
        final Text textInput = new Text(parent, SWT.LEFT | SWT.BORDER);
        textInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, hspan, vspan));
        textInput.setText(value);
        return textInput;
    }

    public Combo formComboLocalized(
            final Composite parent,
            final String selection,
            final List<Tuple<String>> items) {

        return formComboLocalized(parent, selection, items, 1, 1);
    }

    public Combo formComboLocalized(
            final Composite parent,
            final String selection,
            final List<Tuple<String>> items,
            final int hspan, final int vspan) {

        final I18nSingleSelection combo = new I18nSingleSelection(parent, items, this.i18nSupport);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, hspan, vspan));
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

}
