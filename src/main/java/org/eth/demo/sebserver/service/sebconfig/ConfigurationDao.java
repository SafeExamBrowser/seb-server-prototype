/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.sebconfig;

import java.util.Collection;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationNode;

public interface ConfigurationDao {

    ConfigurationNode createNew(ConfigurationNode node);

    ConfigurationNode byId(Long id);

    ConfigurationNode byName(String name);

    /** Use this all ConfigurationNode owned by specified User
     *
     * @param user
     * @return */
    Collection<ConfigurationNode> getOwned(User user);

    /** use this to get all ConfigurationNode from the Institution of the specified User
     *
     * @param user
     * @return */
    Collection<ConfigurationNode> getAll(User user);

    /** Use this to get all ConfigurationNode of all institutions filtered by predicate
     *
     * @param predicate
     * @return */
    Collection<ConfigurationNode> getAll(Predicate<ConfigurationNode> predicate);

    ConfigurationNode save(ConfigurationNode node);

    ConfigurationNode saveAsVersion(Long configurationNodeId, String versionName);

    ConfigurationNode undo(Long configurationNodeId);

    ConfigurationNode delete(ConfigurationNode node);

}
