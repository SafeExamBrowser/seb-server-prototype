/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import static org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport.lmsClientname;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Role.UserRole;
import org.eth.demo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LMSClientAuthenticationFilter extends AbstractClientAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LMSClientAuthenticationFilter.class);

    public static final String CONNECTION_TOKEN_PARAM_NAME = "connectionToken";

    public LMSClientAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher,
            final SebLmsSetupRecordMapper sebLmsSetupRecordMapper,
            final PasswordEncoder clientPasswordEncoder) {

        super(defaultAuthenticationEventPublisher,
                sebLmsSetupRecordMapper,
                clientPasswordEncoder);
    }

    @Override
    protected ClientAuth auth(
            final String username,
            final String password,
            final HttpServletRequest httpRequest) {

        log.debug("Apply filter LMSClientAuthenticationFilter");

        final List<SebLmsSetupRecord> setups = this.sebLmsSetupRecordMapper.selectByExample()
                .where(lmsClientname, isEqualTo(username))
                .build()
                .execute();

        log.debug("Found SebLmsSetupRecord matches: {}", setups);

        final SebLmsSetupRecord matching = Utils.getSingle(setups.stream()
                .filter(record -> this.clientPasswordEncoder.matches(password, record.getLmsClientsecret()))
                .collect(Collectors.toList()));

        log.debug("Found match: {}", matching);

        return new ClientAuth(
                matching.getInstitutionId(),
                matching.getLmsClientname(),
                httpRequest.getRemoteAddr(),
                matching.getLmsUrl());
    }

    @Override
    protected UserRole getRole() {
        return UserRole.LMS_CLIENT;
    }

}
