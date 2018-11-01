/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.form;

import org.eth.demo.sebserver.gui.service.i18n.PolyglotPageService;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class PageFormService {

    private final WidgetFactory widgetFactory;
    private final PolyglotPageService polyglotPageService;

    public PageFormService(final WidgetFactory widgetFactory, final PolyglotPageService polyglotPageService) {
        this.widgetFactory = widgetFactory;
        this.polyglotPageService = polyglotPageService;
    }

    public FormBuilder getBuilder(final PageContext composerCtx, final int rows) {
        return new FormBuilder(this.widgetFactory, this.polyglotPageService, composerCtx, rows);
    }

    public WidgetFactory getWidgetFactory() {
        return this.widgetFactory;
    }

    public PolyglotPageService getPolyglotPageService() {
        return this.polyglotPageService;
    }

}
