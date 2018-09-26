/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import org.eth.demo.sebserver.domain.rest.admin.User;

public interface AuthorizationGrantRule<T> {

    Class<T> type();

    boolean hasReadGrant(T t, User user);

    boolean hasWriteGrant(T t, User user);

}
