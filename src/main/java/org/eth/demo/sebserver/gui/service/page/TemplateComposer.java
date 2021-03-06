/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Map;

import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;

public interface TemplateComposer {

    default String getName() {
        return this.getClass().getName();
    }

    default boolean validateAttributes(final Map<String, String> attributes) {
        return true;
    }

    void compose(PageContext composerCtx);

}
