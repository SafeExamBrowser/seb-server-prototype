/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.sebconfig;

import java.util.Collection;

import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.Attribute;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeOfView;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.TableAttributeValue;

public interface ConfigAttributeDao {

    Collection<Attribute> getAll();

    Attribute byId(Long id);

    Collection<AttributeOfView> getAttributesOfView(String viewName);

    Collection<AttributeValue> getValuesOfView(Long configId, String viewName);

    Collection<AttributeValue> getValuesOfConfig(Long configId);

    Collection<ConfigurationValueRecord> getValueRecordsOfConfig(Long configId);

    void saveValues(Collection<ConfigurationValueRecord> values);

    Long saveValue(AttributeValue value);

    void saveTableValue(TableAttributeValue value);

}