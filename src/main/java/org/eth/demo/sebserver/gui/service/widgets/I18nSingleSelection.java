/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.widgets;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.Polyglot;
import org.eth.demo.util.Tuple;

public class I18nSingleSelection extends SingleSelection implements Polyglot {

    private static final long serialVersionUID = -6139779734462001909L;

    public I18nSingleSelection(
            final Composite parent,
            final List<Tuple<String>> mapping,
            final I18nSupport i18nSupport) {

        super(parent, mapping);
        updateLocale(i18nSupport);
    }

    @Override
    public void updateLocale(final I18nSupport i18nSupport) {
        int i = 0;
        final Iterator<String> iterator = this.valueMapping.iterator();
        while (iterator.hasNext()) {
            super.setItem(i, i18nSupport.getText(iterator.next()));
            i++;
        }
    }

}
