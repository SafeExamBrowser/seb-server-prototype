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
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeOfView;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.TableAttributeValue;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantType;
import org.eth.demo.sebserver.service.sebconfig.ConfigAttributeDao;
import org.eth.demo.sebserver.service.sebconfig.ConfigurationDao;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sebconfig")
public class SEBConfigurationController {

    private final ConfigurationDao configurationDao;
    private final ConfigAttributeDao sebConfigattributeDao;
    private final AuthorizationGrantService authorizationGrantService;

    public SEBConfigurationController(
            final ConfigurationDao configurationDao,
            final ConfigAttributeDao sebConfigattributeDao,
            final AuthorizationGrantService authorizationGrantService) {

        this.configurationDao = configurationDao;
        this.sebConfigattributeDao = sebConfigattributeDao;
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

    @RequestMapping(value = "attributes/{viewName}", method = RequestMethod.GET)
    public final Collection<AttributeOfView> getAttributesOfView(@PathVariable final String viewName) {
        return this.sebConfigattributeDao.getAttributesOfView(viewName);
    }

    @RequestMapping(value = "values/{configId}/{viewName}", method = RequestMethod.GET)
    public final Collection<AttributeValue> getValuesOfView(
            @PathVariable(required = true) final Long configId,
            @PathVariable(required = true) final String viewName) {

        return this.sebConfigattributeDao.getValuesOfView(configId, viewName);
    }

    // TODO add validation error response
    @RequestMapping(value = "saveValue", method = RequestMethod.POST)
    public final void saveValue(@RequestBody final AttributeValue value) {
        this.sebConfigattributeDao.saveValue(value);
    }

    // TODO add validation error response
    @RequestMapping(value = "saveTable", method = RequestMethod.POST)
    public final void saveValue(@RequestBody final TableAttributeValue value) {
        this.sebConfigattributeDao.saveTableValue(value);
    }

}
