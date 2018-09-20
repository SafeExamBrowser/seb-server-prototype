/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Locale;

import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.Polyglot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class PolyglotPageService {

    private final I18nSupport i18nSupport;
    private final PageTreeTraversal pageTreeTraversal;

    public PolyglotPageService(final I18nSupport i18nSupport, final PageTreeTraversal pageTreeTraversal) {
        this.i18nSupport = i18nSupport;
        this.pageTreeTraversal = pageTreeTraversal;
    }

    public void setPageLocale(final Composite root, final Locale locale) {
        this.i18nSupport.setSessionLocale(locale);
        this.pageTreeTraversal.traversePageTree(
                root,
                comp -> comp instanceof Polyglot,
                p -> ((Polyglot) p).updateLocale(this.i18nSupport));

        root.layout(true, true);
    }

}
