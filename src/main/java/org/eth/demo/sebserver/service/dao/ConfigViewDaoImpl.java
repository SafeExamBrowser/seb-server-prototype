/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordDynamicSqlSupport.*;
import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport.*;
import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport.id;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper;
import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper.JoinRecord;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationAttributeRecord;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeType;
import org.eth.demo.sebserver.domain.rest.sebconfig.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.ViewAttribute;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class ConfigViewDaoImpl {

    private final AttributeOrientationJoinMapper attributeOrientationJoinMapper;
    private final ConfigurationAttributeRecordMapper configurationAttributeRecordMapper;
    private final ConfigurationValueRecordMapper configurationValueRecordMapper;

    // TODO Try to get rid of this mappings. Try to use mybatis built in caches for attributes and always make a request
    private final Map<String, ConfigurationAttributeRecord> attributeFQNameMapping;
    private final Map<Long, String> attributeIdFQNameMapping;

    public ConfigViewDaoImpl(
            final AttributeOrientationJoinMapper attributeOrientationJoinMapper,
            final ConfigurationAttributeRecordMapper configurationAttributeRecordMapper,
            final ConfigurationValueRecordMapper configurationValueRecordMapper) {

        this.attributeOrientationJoinMapper = attributeOrientationJoinMapper;
        this.configurationAttributeRecordMapper = configurationAttributeRecordMapper;
        this.configurationValueRecordMapper = configurationValueRecordMapper;
        this.attributeFQNameMapping = new HashMap<>();
        this.attributeIdFQNameMapping = new HashMap<>();
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

    @Transactional(readOnly = true)
    public Collection<AttributeValue> getValues(final Long configId, final List<String> attributeNames) {

        // TODO do this with a join

        final List<Long> attributeIds = this.configurationAttributeRecordMapper.selectByExample()
                .where(name, SqlBuilder.isIn(attributeNames))
                .build()
                .execute()
                .stream()
                .map(a -> a.getId())
                .collect(Collectors.toList());

        return this.configurationValueRecordMapper.selectByExample()
                .where(configurationId, SqlBuilder.isEqualTo(configId))
                .and(configurationAttributeId, SqlBuilder.isIn(attributeIds))
                .build()
                .execute()
                .stream()
                .map(this::attributeValueFromRecord)
                .collect(Collectors.toList());

    }

    public AttributeValue attributeValueFromRecord(final ConfigurationValueRecord record) {
        return new AttributeValue(
                record.getConfigurationId(),
                getAttributeName(record.getConfigurationAttributeId()),
                getAttributeFQName(record.getConfigurationAttributeId()),
                record.getListIndex(),
                record.getValue());
    }

    @Transactional
    public Long saveValue(final AttributeValue value) {
        final ConfigurationAttributeRecord attribute = attributeByFQName(value.fullyQualifiedAttributeName);
        final ConfigurationAttributeRecord attRecord =
                this.configurationAttributeRecordMapper.selectByPrimaryKey(attribute.getId());
        final AttributeType type = AttributeType.valueOf(attRecord.getType());

        ConfigurationValueRecord valueRecord = SelectDSL.selectWithMapper(
                this.configurationValueRecordMapper::selectOne,
                id, configurationId, configurationAttributeId, listIndex,
                ConfigurationValueRecordDynamicSqlSupport.value, text)
                .from(configurationValueRecord)
                .where(configurationId, isEqualTo(value.configId))
                .build()
                .execute();

        final boolean insert = valueRecord == null;
        valueRecord = new ConfigurationValueRecord(
                (insert) ? null : valueRecord.getId(),
                value.configId,
                attribute.getId(),
                value.listIndex,
                (type == AttributeType.FILE_UPLOAD) ? null : value.value,
                (type == AttributeType.FILE_UPLOAD) ? value.value : null);

        if (insert) {
            this.configurationValueRecordMapper.insert(valueRecord);
        } else {
            this.configurationValueRecordMapper.updateByPrimaryKey(valueRecord);
        }
        return valueRecord.getId();
    }

    private String getAttributeName(final Long attrId) {
        return this.configurationAttributeRecordMapper.selectByPrimaryKey(attrId)
                .getName();
    }

    private String getAttributeFQName(final Long attrId) {
        if (!this.attributeIdFQNameMapping.containsKey(attrId)) {
            final ConfigurationAttributeRecord attribute =
                    this.configurationAttributeRecordMapper.selectByPrimaryKey(attrId);
            final String fqName = (attribute.getParentId() == null) ? attribute.getName()
                    : getAttributeFQName(attribute.getParentId()) + attribute.getName();
            this.attributeIdFQNameMapping.put(attrId, fqName);
        }

        return this.attributeIdFQNameMapping.get(attrId);
    }

    private ConfigurationAttributeRecord attributeByFQName(final String attributeFQName) {
        if (this.attributeFQNameMapping.containsKey(attributeFQName)) {
            return this.attributeFQNameMapping.get(attributeFQName);
        }

        final String[] nameSpace = StringUtils.split(attributeFQName, ".");
        if (nameSpace.length >= 1) {
            ConfigurationAttributeRecord attr = getAttribute(nameSpace[0], null);
            this.attributeFQNameMapping.put(nameSpace[0], attr);
            this.attributeIdFQNameMapping.put(attr.getId(), nameSpace[0]);
            String fqName = nameSpace[0];
            for (int i = 1; i < nameSpace.length; i++) {
                attr = getAttribute(nameSpace[i], attr.getId());
                fqName = fqName + "." + nameSpace[i];
                this.attributeFQNameMapping.put(fqName, attr);
                this.attributeIdFQNameMapping.put(attr.getId(), fqName);
            }
        }

        return this.attributeFQNameMapping.get(attributeFQName);
    }

    private ConfigurationAttributeRecord getAttribute(final String attributeName, final Long pId) {
        if (!this.attributeFQNameMapping.containsKey(attributeName)) {
            final ConfigurationAttributeRecord attr =
                    SelectDSL.selectWithMapper(this.configurationAttributeRecordMapper::selectOne, id)
                            .from(configurationAttributeRecord)
                            .where(name, SqlBuilder.isEqualTo(attributeName))
                            .and(parentId, (pId == null) ? SqlBuilder.isNull() : SqlBuilder.isEqualTo(pId))
                            .build()
                            .execute();

            if (attr != null) {
                this.attributeFQNameMapping.put(attributeName, attr);
                this.attributeIdFQNameMapping.put(attr.getId(), attributeName);
            }
        }

        return this.attributeFQNameMapping.get(attributeName);
    }

}
