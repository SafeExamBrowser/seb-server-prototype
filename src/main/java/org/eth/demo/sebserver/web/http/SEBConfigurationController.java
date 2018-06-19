/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.TableValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.ViewAttribute;
import org.eth.demo.sebserver.service.dao.ConfigViewDaoImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @RequestMapping(value = "attributes/{viewName}", method = RequestMethod.GET)
    final Collection<ViewAttribute> getAttributesOfView(@PathVariable final String viewName) {
        return this.configViewDaoImpl.getAttributes(viewName);
    }

    @RequestMapping(value = "values/{configId}", method = RequestMethod.GET)
    final Collection<AttributeValue> getValues(
            @RequestHeader(value = "attributeNames") final String attributeNames,
            @PathVariable final Long configId) {

        final List<String> attrNames = Arrays.asList(StringUtils.split(attributeNames, ","));
        if (attrNames == null || attrNames.isEmpty()) {
            return Collections.emptyList();
        }

        return this.configViewDaoImpl.getValues(configId, attrNames);
    }

    // TODO here we should add a validation error response
    @RequestMapping(value = "values/save", method = RequestMethod.POST)
    final void saveValue(@RequestBody final AttributeValue value) {
        this.configViewDaoImpl.saveValue(value);
    }

    // TODO here we should add a validation error response
    @RequestMapping(value = "values/savetable", method = RequestMethod.POST)
    final void saveValue(@RequestBody final TableValue value) {
        this.configViewDaoImpl.saveTableValue(value);
    }

}
