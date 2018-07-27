/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.admin;

import org.eth.demo.sebserver.domain.rest.admin.User;

public interface UserFacade {

    /** Use this to get the current User within a request-response thread cycle.
     *
     * @return the Current user
     * @throws IllegalStateException if no Authentication was found
     * @throws IllegalArgumentException if fromPrincipal is not able to extract the User of the Authentication
     *             instance */
    User getCurrentUser();

}
