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
import org.eth.demo.sebserver.service.sebconfig.SEBConfigDao;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sebconfig")
public class SEBConfigController {

    private final SEBConfigDao sebConfigDao;

    public SEBConfigController(final SEBConfigDao sebConfigDao) {
        this.sebConfigDao = sebConfigDao;
    }

    @RequestMapping(value = "attributes/{viewName}", method = RequestMethod.GET)
    final Collection<ViewAttribute> getAttributesOfView(@PathVariable final String viewName) {
        return this.sebConfigDao.getAttributes(viewName);
    }

    @RequestMapping(value = "values/{configId}", method = RequestMethod.GET)
    final Collection<AttributeValue> getValues(
            @RequestHeader(value = "attributeNames") final String attributeNames,
            @PathVariable final Long configId) {

        final List<String> attrNames = Arrays.asList(StringUtils.split(attributeNames, ","));
        if (attrNames == null || attrNames.isEmpty()) {
            return Collections.emptyList();
        }

        return this.sebConfigDao.getValues(configId, attrNames);
    }

    // TODO here we should add a validation error response
    @RequestMapping(value = "saveValue", method = RequestMethod.POST)
    final void saveValue(@RequestBody final AttributeValue value) {
        this.sebConfigDao.saveValue(value);
    }

    // TODO here we should add a validation error response
    @RequestMapping(value = "saveTable", method = RequestMethod.POST)
    final void saveValue(@RequestBody final TableValue value) {
        this.sebConfigDao.saveTableValue(value);
    }

}
