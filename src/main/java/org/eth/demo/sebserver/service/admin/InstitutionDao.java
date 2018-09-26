/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.admin;

import java.util.Collection;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.domain.rest.admin.SebLmsSetup;
import org.eth.demo.sebserver.domain.rest.admin.User;

public interface InstitutionDao {

    Institution byId(Long id);

    Institution ofUser(User user);

    Collection<Institution> all();

    Collection<Institution> all(Predicate<Institution> filter);

    Institution save(Institution data);

    boolean delete(Long id);

    SebLmsSetup save(SebLmsSetup setup);

    boolean delete(SebLmsSetup setup);

}
