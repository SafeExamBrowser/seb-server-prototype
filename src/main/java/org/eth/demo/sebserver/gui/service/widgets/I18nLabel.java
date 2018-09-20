/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.i18n.Polyglot;

public class I18nLabel extends Label implements Polyglot {

    private static final long serialVersionUID = -8714647617039392129L;

    private LocTextKey locTextKey;
    private LocTextKey locToolTipKey = null;

    public I18nLabel(final Composite parent, final I18nSupport i18nSupport, final LocTextKey locTextKey) {
        super(parent, SWT.NONE);
        this.locTextKey = locTextKey;
        updateLocale(i18nSupport);
    }

    public void setLocTextKey(final LocTextKey locTextKey, final I18nSupport i18nSupport) {
        this.locTextKey = locTextKey;
        this.updateLocale(i18nSupport);
    }

    public void setLocToolTipKey(final LocTextKey locToolTipKey, final I18nSupport i18nSupport) {
        this.locToolTipKey = locToolTipKey;
        this.updateLocale(i18nSupport);
    }

    @Override
    public void updateLocale(final I18nSupport i18nSupport) {
        if (this.locTextKey != null) {
            super.setText(i18nSupport.getText(this.locTextKey));
        }
        if (this.locToolTipKey != null) {
            super.setToolTipText(i18nSupport.getText(this.locToolTipKey));
        }
    }

}
