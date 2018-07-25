/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.sebconfig;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordDynamicSqlSupport.name;
import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper;
import org.eth.demo.sebserver.batis.AttributeOrientationJoinMapper.JoinRecord;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.OrientationRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationAttributeRecord;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.Attribute;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeType;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.AttributeValue;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.TableAttributeValue;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class ConfigAttributeDaoImpl implements ConfigAttributeDao {

    private final AttributeOrientationJoinMapper attributeOrientationJoinMapper;
    private final ConfigurationAttributeRecordMapper configurationAttributeRecordMapper;
    private final ConfigurationValueRecordMapper configurationValueRecordMapper;

    public ConfigAttributeDaoImpl(
            final AttributeOrientationJoinMapper attributeOrientationJoinMapper,
            final ConfigurationAttributeRecordMapper configurationAttributeRecordMapper,
            final ConfigurationValueRecordMapper configurationValueRecordMapper) {

        this.attributeOrientationJoinMapper = attributeOrientationJoinMapper;
        this.configurationAttributeRecordMapper = configurationAttributeRecordMapper;
        this.configurationValueRecordMapper = configurationValueRecordMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.SEBConfigDao#getAttributes(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Attribute> getAttributes(final String viewName) {
        final Collection<JoinRecord> allOfView = this.attributeOrientationJoinMapper.selectOfView(viewName);
        final Map<Long, String> mapping = allOfView
                .stream()
                .collect(Collectors.toMap(r -> r.id, r -> r.name));

        return allOfView
                .stream()
                .map(r -> new Attribute(
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

    @Override
    public Collection<AttributeValue> getValuesOfView(final Long configId, final String viewName) {
        return this.configurationValueRecordMapper.selectByExample()

                .leftJoin(ConfigurationAttributeRecordDynamicSqlSupport.configurationAttributeRecord)
                .on(ConfigurationValueRecordDynamicSqlSupport.configurationAttributeId,
                        equalTo(ConfigurationAttributeRecordDynamicSqlSupport.id))

                .leftJoin(OrientationRecordDynamicSqlSupport.orientationRecord)
                .on(ConfigurationAttributeRecordDynamicSqlSupport.id,
                        equalTo(OrientationRecordDynamicSqlSupport.configAttributeId))

                .where(configurationId, SqlBuilder.isEqualTo(configId))
                .and(OrientationRecordDynamicSqlSupport.view, isEqualTo(viewName))

                .build()
                .execute()
                .stream()
                .map(this::attributeValueFromRecord)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AttributeValue> getValuesOfConfig(final Long configId) {
        return this.configurationValueRecordMapper.selectByExample()
                .where(configurationId, SqlBuilder.isEqualTo(configId))
                .build()
                .execute()
                .stream()
                .map(this::attributeValueFromRecord)
                .collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.SEBConfigDao#getValues(java.lang.Long, java.util.List)
     */
    @Deprecated
    @Override
    @Transactional(readOnly = true)
    public Collection<AttributeValue> getValues(final Long configId, final List<String> attributeNames) {

        final List<String> tableAttributeNames = attributeNames.stream()
                .filter(a -> a.contains("."))
                .collect(Collectors.toList());
        final List<String> singleAttributeNames = new ArrayList<>(attributeNames);
        singleAttributeNames.removeAll(tableAttributeNames);

        // TODO do this with a join!?

        final List<Long> attributeIds = this.configurationAttributeRecordMapper.selectByExample()
                .where(name, SqlBuilder.isIn(singleAttributeNames))
                .build()
                .execute()
                .stream()
                .map(a -> a.getId())
                .collect(Collectors.toList());

        attributeIds.addAll(
                tableAttributeNames.stream()
                        .map(name -> {
                            final String[] names = StringUtils.split(name, ".");
                            return getAttribute(names[1], names[0]).getId();
                        })
                        .collect(Collectors.toList()));

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
        final ConfigurationAttributeRecord attribute = this.configurationAttributeRecordMapper
                .selectByPrimaryKey(record.getConfigurationAttributeId());
        final Long parentId = attribute.getParentId();
        final String parentName = (parentId != null) ? getAttributeName(parentId) : null;

        return new AttributeValue(
                record.getConfigurationId(),
                attribute.getName(),
                parentName,
                record.getListIndex(),
                record.getValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.SEBConfigDao#saveValue(org.eth.demo.sebserver.domain.rest.sebconfig.
     * AttributeValue)
     */
    @Override
    @Transactional
    public Long saveValue(final AttributeValue value) {
        final ConfigurationAttributeRecord attribute = getAttribute(value.attributeName, value.parentAttributeName);
        final AttributeType type = AttributeType.valueOf(attribute.getType());

        final List<ConfigurationValueRecord> records = this.configurationValueRecordMapper.selectByExample()
                .where(configurationId, isEqualTo(value.configId))
                .and(configurationAttributeId, isEqualTo(attribute.getId()))
                .and(listIndex, isEqualTo(value.listIndex))
                .build()
                .execute();

        if (records != null && records.size() > 1) {
            throw new IllegalArgumentException("To many ConfigurationValueRecord found for value: " + value);
        }

        final boolean insert = (records == null || records.isEmpty());
        final ConfigurationValueRecord valueRecord = new ConfigurationValueRecord(
                (insert) ? null : records.get(0).getId(),
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

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.SEBConfigDao#saveTableValue(org.eth.demo.sebserver.domain.rest.sebconfig.
     * TableValue)
     */
    @Override
    @Transactional
    public void saveTableValue(final TableAttributeValue value) {
        final ConfigurationAttributeRecord attribute = getAttribute(value.attributeName, null);
        final List<ConfigurationAttributeRecord> columnAttributes =
                this.configurationAttributeRecordMapper.selectByExample()
                        .where(ConfigurationAttributeRecordDynamicSqlSupport.parentId, isEqualTo(attribute.getId()))
                        .build()
                        .execute();
        final List<Long> columnAttributeIds = columnAttributes.stream()
                .map(a -> a.getId())
                .collect(Collectors.toList());

        final int columns = columnAttributeIds.size();

        // first delete all old values of this table
        this.configurationValueRecordMapper.deleteByExample()
                .where(configurationId, isEqualTo(value.configId))
                .and(configurationAttributeId, SqlBuilder.isIn(columnAttributeIds))
                .build()
                .execute();

        // then add the new values
        int columnIndex = 0;
        int rowIndex = 0;
        for (final String val : value.values) {
            final ConfigurationAttributeRecord columnAttr = columnAttributes.get(columnIndex);
            final AttributeType type = AttributeType.valueOf(columnAttr.getType());
            final ConfigurationValueRecord valueRecord = new ConfigurationValueRecord(
                    null,
                    value.configId,
                    columnAttr.getId(),
                    rowIndex,
                    (type == AttributeType.FILE_UPLOAD) ? null : val,
                    (type == AttributeType.FILE_UPLOAD) ? val : null);

            this.configurationValueRecordMapper.insert(valueRecord);

            columnIndex++;
            if (columnIndex >= columns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private String getAttributeName(final Long attrId) {
        return this.configurationAttributeRecordMapper.selectByPrimaryKey(attrId)
                .getName();
    }

    // TODO make this with join and caching
    private ConfigurationAttributeRecord getAttribute(final String name, final String parentName) {

        final Long parentId;
        if (StringUtils.isNotBlank(parentName)) {
            final List<ConfigurationAttributeRecord> parents = this.configurationAttributeRecordMapper.selectByExample()
                    .where(ConfigurationAttributeRecordDynamicSqlSupport.name, isEqualTo(parentName))
                    .build()
                    .execute();

            if (parents == null || parents.isEmpty()) {
                throw new IllegalArgumentException("No Configuration Attribute found for name: " + parentName);
            } else if (parents.size() > 1) {
                throw new IllegalArgumentException("To many Configuration Attribute found for name: " + parentName);
            }

            parentId = parents.get(0).getId();
        } else {
            parentId = null;
        }

        final List<ConfigurationAttributeRecord> attrs = this.configurationAttributeRecordMapper.selectByExample()
                .where(ConfigurationAttributeRecordDynamicSqlSupport.name, isEqualTo(name))
                .and(ConfigurationAttributeRecordDynamicSqlSupport.parentId,
                        (parentId == null) ? SqlBuilder.isNull() : isEqualTo(parentId))
                .build()
                .execute();

        if (attrs == null || attrs.isEmpty()) {
            throw new IllegalArgumentException(
                    "No Configuration Attribute found for name: " + name + " and parentName: " + parentName);
        } else if (attrs.size() > 1) {
            throw new IllegalArgumentException(
                    "To many Configuration Attribute found for name: " + name + " and parentName: " + parentName);
        } else {
            return attrs.get(0);
        }
    }

}
