/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam;

import java.security.Principal;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.service.admin.UserFacade;

public final class UserPrivilegeExamFilter implements Predicate<Exam> {

    private final User user;
    private final boolean isAdmin;

    public UserPrivilegeExamFilter(final User user) {
        this.user = user;
        this.isAdmin = this.user
                .getAuthorities()
                .stream()
                .anyMatch(ga -> Role.UserRole.EXAM_ADMIN.name().equals(ga.getAuthority()));
    }

    @Override
    public boolean test(final Exam exam) {
        if (this.isAdmin) {
            return true;
        } else {
            return exam.ownerId.longValue() == this.user.getId().longValue();
        }
    }

    public static final UserPrivilegeExamFilter of(final Principal principal) {
        return new UserPrivilegeExamFilter(UserFacade.extractFromPrincipal(principal));
    }
}