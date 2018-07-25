/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.sebconfig;

import java.util.Collection;
import java.util.List;

import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.TableValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.ViewAttribute;

public interface SEBConfigDao {

    Collection<ViewAttribute> getAttributes(String viewName);

    Collection<AttributeValue> getValues(Long configId, List<String> attributeNames);

    AttributeValue attributeValueFromRecord(ConfigurationValueRecord record);

    Long saveValue(AttributeValue value);

    void saveTableValue(TableValue value);

}