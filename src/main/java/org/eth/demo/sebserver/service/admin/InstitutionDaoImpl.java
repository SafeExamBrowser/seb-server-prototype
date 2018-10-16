/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.admin;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.mapper.InstitutionRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.InstitutionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.domain.rest.admin.Institution.AuthType;
import org.eth.demo.sebserver.domain.rest.admin.LmsSetup;
import org.eth.demo.sebserver.service.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class InstitutionDaoImpl implements InstitutionDao {

    private final InstitutionRecordMapper institutionRecordMapper;
    private final SebLmsSetupRecordMapper sebLmsSetupRecordMapper;

    public InstitutionDaoImpl(
            final InstitutionRecordMapper institutionRecordMapper,
            final SebLmsSetupRecordMapper sebLmsSetupRecordMapper) {

        this.institutionRecordMapper = institutionRecordMapper;
        this.sebLmsSetupRecordMapper = sebLmsSetupRecordMapper;
    }

    @Transactional
    @Override
    public Institution createNew(final String name) {
        final InstitutionRecord institutionRecord = new InstitutionRecord(null, name, AuthType.INTERNAL.name());
        this.institutionRecordMapper.insert(institutionRecord);
        return byId(institutionRecord.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Institution byId(final Long id) {
        return toDomainModel(
                String.valueOf(id),
                this.institutionRecordMapper.selectByPrimaryKey(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Institution> all() {
        return this.institutionRecordMapper.selectByExample()
                .build()
                .execute()
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Institution> all(final Predicate<Institution> filter) {
        return this.institutionRecordMapper.selectByExample()
                .build()
                .execute()
                .stream()
                .map(this::toDomainModel)
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Institution save(final Institution data) {
        final InstitutionRecord record = toRecord(data);
        ;
        if (data.id == null) {
            this.institutionRecordMapper.insert(record);
            data.lmsSetup.stream()
                    .map(InstitutionDaoImpl::toRecord)
                    .forEach(setupRecord -> this.sebLmsSetupRecordMapper.insert(setupRecord));
        } else {
            this.institutionRecordMapper.updateByPrimaryKey(record);

            if (!data.lmsSetup.isEmpty()) {
                this.sebLmsSetupRecordMapper.deleteByExample()
                        .where(SebLmsSetupRecordDynamicSqlSupport.institutionId, isEqualTo(record.getId()))
                        .build()
                        .execute();

                data.lmsSetup.stream()
                        .map(InstitutionDaoImpl::toRecord)
                        .forEach(setupRecord -> this.sebLmsSetupRecordMapper.insert(setupRecord));
            }
        }

        return byId(record.getId());
    }

    @Transactional
    @Override
    public boolean delete(final Long id) {
        final Institution institution = byId(id);
        if (institution == null) {
            return false;
        }

        for (final LmsSetup lmsSetup : institution.lmsSetup) {
            this.sebLmsSetupRecordMapper.deleteByPrimaryKey(lmsSetup.id);
        }

        this.institutionRecordMapper.deleteByPrimaryKey(institution.id);
        return true;
    }

    @Transactional
    @Override
    public LmsSetup save(final LmsSetup setup) {
        if (setup.institutionId == null) {
            return null;
        }

        final SebLmsSetupRecord record = toRecord(setup);
        if (setup.id == null) {
            this.sebLmsSetupRecordMapper.insert(record);
        } else {
            this.sebLmsSetupRecordMapper.updateByPrimaryKey(record);
        }

        return LmsSetup.of(record);
    }

    @Transactional
    @Override
    public boolean delete(final LmsSetup setup) {
        // TODO Auto-generated method stub
        return false;
    }

    private Institution toDomainModel(final InstitutionRecord record) {
        return toDomainModel("--", record);
    }

    private Institution toDomainModel(final String nameId, final InstitutionRecord record) {
        if (record == null) {
            throw new ResourceNotFoundException("Institution", String.valueOf(nameId));
        }

        final List<SebLmsSetupRecord> setups = this.sebLmsSetupRecordMapper.selectByExample()
                .where(SebLmsSetupRecordDynamicSqlSupport.institutionId, isEqualTo(record.getId()))
                .build()
                .execute();

        return Institution.of(record, setups);
    }

    private static InstitutionRecord toRecord(final Institution data) {
        return new InstitutionRecord(
                data.id,
                data.name,
                data.authType.name());
    }

    private static SebLmsSetupRecord toRecord(final LmsSetup data) {
        return new SebLmsSetupRecord(
                data.id,
                data.institutionId,
                data.name,
                data.lmsType.name(),
                data.lmsApiUrl,
                data.lmsAuthName,
                data.lmsAuthSecret,
                data.lmsRestApiToken,
                data.sebAuthName,
                data.sebAuthSecret);
    }

}
