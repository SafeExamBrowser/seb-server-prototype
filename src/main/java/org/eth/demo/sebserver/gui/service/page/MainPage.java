/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import org.springframework.stereotype.Component;

@Component
public class MainPage extends PageComposer {

    public static final String ATTR_MAIN_PAGE_STATE = "MAIN_PAGE_STATE";

    public MainPage() {
        super(MainPageForm.class.getName());
    }

}
