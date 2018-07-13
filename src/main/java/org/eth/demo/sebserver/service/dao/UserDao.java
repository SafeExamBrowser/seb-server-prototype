/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import java.util.Collection;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.User;

public interface UserDao {

    User byId(Long id);

    User byUserName(String name);

    Collection<User> allActive();

    Collection<User> all(Predicate<User> predicate);

}
