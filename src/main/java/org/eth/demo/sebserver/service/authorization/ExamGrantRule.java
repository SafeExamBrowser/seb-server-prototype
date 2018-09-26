/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ExamGrantRule implements AuthorizationGrantRule<Exam> {

    @Override
    public Class<Exam> type() {
        return Exam.class;
    }

    @Override
    public boolean hasReadGrant(final Exam exam, final User user) {
        if (user.containsAny(
                Role.INSTITUTIONAL_ADMIN,
                Role.EXAM_ADMIN,
                Role.EXAM_SUPPORTER) &&
                exam.institutionId.longValue() == user.institutionId.longValue()) {

            return true;
        }

        return false;
    }

    @Override
    public boolean hasWriteGrant(final Exam exam, final User user) {
        if (user.containsAny(
                Role.INSTITUTIONAL_ADMIN,
                Role.EXAM_ADMIN) &&
                exam.institutionId.longValue() == user.institutionId.longValue()) {

            return true;
        }

        return false;
    }

}
