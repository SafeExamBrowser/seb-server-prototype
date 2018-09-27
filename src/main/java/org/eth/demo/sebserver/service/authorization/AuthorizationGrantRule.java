/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;

public interface AuthorizationGrantRule {

    GrantEntityType type();

    boolean hasReadGrant(GrantEntity entity, User user);

    boolean hasModifyGrant(GrantEntity entity, User user);

    boolean hasWriteGrant(GrantEntity entity, User user);

}
