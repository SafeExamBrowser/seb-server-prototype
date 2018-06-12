/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.sebconfig.ViewAttribute;
import org.eth.demo.sebserver.service.dao.ConfigViewDaoImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sebconfig")
public class SEBConfigurationController {

    private final ConfigViewDaoImpl configViewDaoImpl;

    public SEBConfigurationController(final ConfigViewDaoImpl configViewDaoImpl) {
        this.configViewDaoImpl = configViewDaoImpl;
    }

    @RequestMapping(value = "/{viewName}", method = RequestMethod.GET)
    final Collection<ViewAttribute> exam(@PathVariable final String viewName) {
        return this.configViewDaoImpl.getAttributes(viewName);
    }

}
