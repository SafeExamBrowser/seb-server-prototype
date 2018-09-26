/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class InstitutionGrantRule implements AuthorizationGrantRule<Institution> {

    @Override
    public Class<Institution> type() {
        return Institution.class;
    }

    @Override
    public boolean hasReadGrant(final Institution t, final User user) {
        if (user.roles.contains(Role.SEB_SERVER_ADMIN)) {
            return true;
        }

        return user.institutionId.longValue() == t.id;
    }

    @Override
    public boolean hasWriteGrant(final Institution t, final User user) {
        if (user.roles.contains(Role.SEB_SERVER_ADMIN)) {
            return true;
        }

        return false;
    }

}
