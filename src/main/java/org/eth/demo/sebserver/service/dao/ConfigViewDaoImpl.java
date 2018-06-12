/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper;
import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper.JoinRecord;
import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeType;
import org.eth.demo.sebserver.domain.rest.sebconfig.ViewAttribute;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class ConfigViewDaoImpl {

    private final AttributeOrientationJoinMapper attributeOrientationJoinMapper;

    public ConfigViewDaoImpl(final AttributeOrientationJoinMapper attributeOrientationJoinMapper) {
        this.attributeOrientationJoinMapper = attributeOrientationJoinMapper;
    }

    @Transactional(readOnly = true)
    public Collection<ViewAttribute> getAttributes(final String viewName) {
        final Collection<JoinRecord> allOfView = this.attributeOrientationJoinMapper.selectOfView(viewName);
        final Map<Long, String> mapping = allOfView
                .stream()
                .collect(Collectors.toMap(r -> r.id, r -> r.name));

        return allOfView
                .stream()
                .map(r -> new ViewAttribute(
                        r.name,
                        AttributeType.valueOf(r.type),
                        mapping.get(r.parentId),
                        r.resources,
                        r.dependencies,
                        r.view,
                        r.group,
                        r.xPosition,
                        r.yPosition))
                .collect(Collectors.toList());
    }

//    public Collection<AttributeValue> getValues() {
//
//    }

}
