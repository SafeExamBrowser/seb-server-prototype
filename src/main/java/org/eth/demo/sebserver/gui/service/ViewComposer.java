/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service;

import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.util.TypedMap;

public interface ViewComposer {

    /** This is used to indicate if a set of attributes are valid to process the specified ViewComposers compose method.
     *
     * @param attributes the given attributes to test
     * @return true if the given attributes has all the information the composer needs to compose the view */
    boolean validateAttributes(TypedMap attributes);

    void composeView(ViewService viewService, Composite parent, TypedMap attributes);

}
