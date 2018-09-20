/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.Polyglot;

public class I18nButton extends Button implements Polyglot {

    private static final long serialVersionUID = 7872020604698010433L;

    private final String locTextKey;
    private final Object[] args = null;

    public I18nButton(final Composite parent, final I18nSupport i18nSupport, final String locTextKey) {
        super(parent, SWT.NONE);
        this.locTextKey = locTextKey;
        updateLocale(i18nSupport);
    }

    @Override
    public void updateLocale(final I18nSupport i18nSupport) {
        super.setText(i18nSupport.getText(this.locTextKey, this.args));
    }

}
