/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.sebconfig;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ConfigurationNodeJoinMapper;
import org.eth.demo.sebserver.batis.ConfigurationNodeJoinMapper.ConfigNodeJoinRecord;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationNodeRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationNodeRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ConfigurationRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationNodeRecord;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationRecord;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;
import org.eth.demo.sebserver.domain.rest.sebconfig.Configuration;
import org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationNode;
import org.eth.demo.sebserver.domain.rest.sebconfig.ConfigurationNode.ConfigurationType;
import org.eth.demo.sebserver.domain.rest.sebconfig.attribute.Attribute;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfigurationDaoImpl implements ConfigurationDao {

    private final ConfigurationNodeRecordMapper configurationNodeRecordMapper;
    private final ConfigurationRecordMapper configurationRecordMapper;
    private final ConfigAttributeDao configAttributeDao;
    private final ConfigurationNodeJoinMapper configurationNodeJoinMapper;

    public ConfigurationDaoImpl(
            final ConfigurationNodeRecordMapper configurationNodeRecordMapper,
            final ConfigurationRecordMapper configurationRecordMapper,
            final ConfigAttributeDao configAttributeDao,
            final ConfigurationNodeJoinMapper configurationNodeJoinMapper) {

        this.configurationNodeRecordMapper = configurationNodeRecordMapper;
        this.configurationRecordMapper = configurationRecordMapper;
        this.configAttributeDao = configAttributeDao;
        this.configurationNodeJoinMapper = configurationNodeJoinMapper;
    }

    @Override
    @Transactional
    public ConfigurationNode createNew(final ConfigurationNode node) {
        // first create new configuration_node entry
        final ConfigurationNodeRecord nodeRecord = toRecord(node);
        this.configurationNodeRecordMapper.insert(nodeRecord);
        final Long nodeId = nodeRecord.getId();

        assert nodeId != null : "Unable to retrieve generated ConfigurationNodeRecord id";

        // create also a new configuration entry as first element in the configuration_node history
        final ConfigurationRecord confRecord = new ConfigurationRecord(
                null, nodeId, "Initial Version", DateTime.now(), false);
        this.configurationRecordMapper.insert(confRecord);
        final Long configId = confRecord.getId();

        assert configId != null : "Unable to retrieve generated ConfigurationRecord id";

        // get a full set of configuration attributes and save default values for the new configuration
        final Collection<Attribute> fullSetOfAttributes = this.configAttributeDao.getAll();
        final List<ConfigurationValueRecord> valueRecords = fullSetOfAttributes
                .stream()
                .map(attr -> new ConfigurationValueRecord(null, configId, attr.id, 0, attr.defaultValue, null))
                .collect(Collectors.toList());

        this.configAttributeDao.saveValues(valueRecords);

        return byId(nodeId);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurationNode byId(final Long id) {
        final QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<ConfigNodeJoinRecord>>>.JoinSpecificationFinisher selectByExample =
                this.configurationNodeJoinMapper.selectByExample();

        return createNode(selectByExample
                .where(ConfigurationNodeRecordDynamicSqlSupport.id, isEqualTo(id))
                .build()
                .execute());
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurationNode byName(final String name) {
        return createNode(this.configurationNodeJoinMapper
                .selectByExample()
                .where(ConfigurationNodeRecordDynamicSqlSupport.name, isEqualTo(name))
                .build()
                .execute());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ConfigurationNode> all(final Predicate<ConfigurationNode> predicate) {
        return fromJoinRecords(this.configurationNodeJoinMapper
                .selectByExample()
                .build()
                .execute())
                        .stream()
                        .filter(predicate)
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConfigurationNode save(final ConfigurationNode node) {
        if (node.id == null) {
            throw new IllegalArgumentException("ConfigurationNode must exists and have an id applied for save/update");
        }

        this.configurationNodeRecordMapper.updateByPrimaryKeySelective(toRecord(node));

        return byId(node.id);
    }

    @Override
    @Transactional
    public ConfigurationNode saveAsVersion(final Long configurationNodeId, final String versionName) {
        final Configuration currentConfig = getCurrentConfiguration(configurationNodeId);

        // close old version
        final ConfigurationRecord configRecord = new ConfigurationRecord(
                currentConfig.id,
                currentConfig.nodeId,
                currentConfig.version,
                currentConfig.versionDate,
                true);
        this.configurationRecordMapper.updateByPrimaryKey(configRecord);

        // create a new one with new values
        final ConfigurationRecord newConfigRecord = new ConfigurationRecord(
                null, configurationNodeId, versionName, DateTime.now(), false);
        this.configurationRecordMapper.insert(newConfigRecord);
        final Long newConfigId = newConfigRecord.getId();

        assert newConfigId != null : "Unable to retrieve generated ConfigurationRecord id";

        // get all values and copy over
        final List<ConfigurationValueRecord> newValueRecords = this.configAttributeDao
                .getValueRecordsOfConfig(currentConfig.id)
                .stream()
                .map(r -> new ConfigurationValueRecord(
                        null,
                        newConfigId,
                        r.getConfigurationAttributeId(),
                        r.getListIndex(),
                        r.getValue(),
                        r.getText()))
                .collect(Collectors.toList());
        this.configAttributeDao.saveValues(newValueRecords);

        return byId(configurationNodeId);
    }

    @Override
    @Transactional
    public ConfigurationNode undo(final Long configurationNodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public ConfigurationNode delete(final ConfigurationNode node) {
        // TODO Auto-generated method stub
        return null;
    }

    private static final Collection<ConfigurationNode> fromJoinRecords(
            final Collection<ConfigNodeJoinRecord> records) {

        return records.stream()
                .reduce(
                        new HashMap<Long, Collection<ConfigNodeJoinRecord>>(),
                        (map, rec) -> {
                            map.computeIfAbsent(rec.id, id -> new ArrayList<>()).add(rec);
                            return map;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        })
                .values()
                .stream()
                .map(ConfigurationDaoImpl::createNode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static final ConfigurationNode createNode(final Collection<ConfigNodeJoinRecord> joinRecords) {

        ConfigurationNode prototype = null;
        final Collection<ExamSEBConfigMapping> examMappings = new ArrayList<>();
        final List<Configuration> configHistory = new ArrayList<>();

        for (final ConfigNodeJoinRecord record : joinRecords) {
            if (prototype == null) {
                prototype = new ConfigurationNode(
                        record.id,
                        record.institutionId,
                        record.ownerId,
                        record.name,
                        ConfigurationType.valueOf(record.type),
                        null, null);
            } else {
                if (prototype.id.longValue() != record.id.longValue()) {
                    throw new IllegalArgumentException(
                            "ConfigNodeJoinRecord from different ConfigurationNode's. Expect all from same ConfigurationNode here");
                }
            }

            if (record.examMappingId != null) {
                examMappings.add(new ExamSEBConfigMapping(
                        record.examMappingId,
                        record.examId,
                        prototype.id,
                        record.clientInfo));
            }

            if (record.configVersionId != null) {
                configHistory.add(new Configuration(
                        record.configVersionId,
                        prototype.id,
                        record.version,
                        record.versionDate,
                        record.followup));
            }
        }

        if (prototype == null) {
            return null;
        }

        // Sort by date
        Collections.sort(
                configHistory,
                (c1, c2) -> c1.versionDate.compareTo(c2.versionDate));

        return new ConfigurationNode(
                prototype.id,
                prototype.institutionId,
                prototype.ownerId,
                prototype.name,
                prototype.type,
                examMappings,
                configHistory);
    }

    private Configuration getCurrentConfiguration(final Long configurationNodeId) {
        final ConfigurationNode node = byId(configurationNodeId);

        if (node.configurationHistory.isEmpty()) {
            throw new IllegalArgumentException("ConfigurationNode with id: " + configurationNodeId + " has no history");
        }

        final Configuration currentConfig = node.configurationHistory.stream()
                .filter(c -> c.followup == false)
                .findFirst()
                .orElse(null);

        if (currentConfig == null) {
            throw new IllegalArgumentException(
                    "ConfigurationNode with id: " + configurationNodeId + " has no current Configuration");
        }

        return currentConfig;
    }

    private static final ConfigurationNodeRecord toRecord(final ConfigurationNode node) {
        return new ConfigurationNodeRecord(
                node.id,
                node.institutionId,
                node.ownerId,
                node.name,
                node.type.name());
    }

    public static final ConfigurationRecord toRecord(final Configuration config) {
        return new ConfigurationRecord(config.id, config.nodeId, config.version, config.versionDate, config.followup);
    }

}
