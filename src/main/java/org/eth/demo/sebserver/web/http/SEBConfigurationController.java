/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.security.Principal;
import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.admin.Scope;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationNode;
import org.eth.demo.sebserver.service.admin.UserFacade;
import org.eth.demo.sebserver.service.sebconfig.ConfigurationDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sebconfig")
public class SEBConfigurationController {

    private final ConfigurationDao configurationDao;

    public SEBConfigurationController(final ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public final Collection<ConfigurationNode> getAttributesOfView(
            @RequestParam(name = "scope", required = false) final String scope,
            final Principal principal) {

        final User user = UserFacade.extractFromPrincipal(principal);

        // TODO the exact scope meanings and permissions has to be defined
        final Scope usedScope = (scope != null) ? Scope.valueOf(scope) : Scope.OWNER;
        switch (usedScope) {
            case EDIT:
            case OWNER: {
                return this.configurationDao.getOwned(user);
            }
            case READ_ONLY: {
                return this.configurationDao.getAll(user);
            }
            default: {
                return this.configurationDao.getOwned(user);
            }
        }
    }

}
