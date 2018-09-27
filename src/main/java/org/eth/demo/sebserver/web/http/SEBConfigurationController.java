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

import org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationNode;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantType;
import org.eth.demo.sebserver.service.sebconfig.ConfigurationDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sebconfig")
public class SEBConfigurationController {

    private final ConfigurationDao configurationDao;
    private final AuthorizationGrantService authorizationGrantService;

    public SEBConfigurationController(
            final ConfigurationDao configurationDao,
            final AuthorizationGrantService authorizationGrantService) {

        this.configurationDao = configurationDao;
        this.authorizationGrantService = authorizationGrantService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public final Collection<ConfigurationNode> getAttributesOfView(
            final Principal principal) {

        return this.configurationDao
                .all(this.authorizationGrantService.getGrantFilter(
                        GrantEntityType.SEB_CONFIG,
                        GrantType.READ_ONLY,
                        principal));
    }

}
