/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import static org.eth.demo.sebserver.batis.gen.mapper.RoleRecordDynamicSqlSupport.userId;
import static org.eth.demo.sebserver.batis.gen.mapper.UserRecordDynamicSqlSupport.active;
import static org.eth.demo.sebserver.batis.gen.mapper.UserRecordDynamicSqlSupport.userName;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.mapper.RoleRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.UserRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.RoleRecord;
import org.eth.demo.sebserver.batis.gen.model.UserRecord;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class UserDaoImpl implements UserDao {

    private final UserRecordMapper userRecordMapper;
    private final RoleRecordMapper roleRecordMapper;

    public UserDaoImpl(
            final UserRecordMapper userRecordMapper,
            final RoleRecordMapper roleRecordMapper) {

        this.userRecordMapper = userRecordMapper;
        this.roleRecordMapper = roleRecordMapper;
    }

    @Override
    public User byId(final Long id) {
        return toDomainModel(
                String.valueOf(id),
                this.userRecordMapper.selectByPrimaryKey(id));
    }

    @Override
    public User byUserName(final String name) {
        return toDomainModel(
                name,
                this.userRecordMapper
                        .selectByExample()
                        .where(userName, isEqualTo(name))
                        .build()
                        .execute()
                        .stream()
                        .findFirst()
                        .orElse(null));
    }

    @Override
    public Collection<User> allActive() {
        final List<UserRecord> records = this.userRecordMapper
                .selectByExample()
                .where(active, isEqualTo(Boolean.TRUE))
                .build()
                .execute();

        if (records == null) {
            Collections.emptyList();
        }

        return records.stream()
                .map(record -> User.fromRecord(record, getRoles(record)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> all(final Predicate<User> predicate) {
        final List<UserRecord> records = this.userRecordMapper
                .selectByExample()
                .build()
                .execute();

        if (records == null) {
            Collections.emptyList();
        }

        return records.stream()
                .map(record -> User.fromRecord(record, getRoles(record)))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private User toDomainModel(final String nameId, final UserRecord record) {
        if (record == null) {
            throw new ResourceNotFoundException("User", String.valueOf(nameId));
        }

        return User.fromRecord(record, getRoles(record));
    }

    private List<RoleRecord> getRoles(final UserRecord record) {
        final List<RoleRecord> roles = this.roleRecordMapper.selectByExample()
                .where(userId, isEqualTo(record.getId()))
                .build()
                .execute();
        return roles;
    }

}
