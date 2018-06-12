/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;

public class RAPConfiguration implements ApplicationConfiguration {

    @Override
    public void configure(final Application application) {
        application.addEntryPoint("/examview", ExamViewEntryPoint.class, null);
        application.addEntryPoint("/sebconfig", SEBConfigEntryPoint.class, null);
    }

}
