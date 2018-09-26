/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.widgets;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.i18n.Polyglot;

public class I18nTreeItem extends TreeItem implements Polyglot {

    private static final long serialVersionUID = 5312491223570972666L;

    private final LocTextKey locTextKey;

    public I18nTreeItem(
            final Tree parent,
            final int style,
            final I18nSupport i18nSupport,
            final LocTextKey locTextKey) {

        super(parent, style);
        this.locTextKey = locTextKey;
        updateLocale(i18nSupport);
    }

    @Override
    public void updateLocale(final I18nSupport i18nSupport) {
        if (this.locTextKey != null) {
            super.setText(i18nSupport.getText(this.locTextKey));
        }
    }

}
