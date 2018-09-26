/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.lms;

import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.SebLmsSetup;
import org.eth.demo.sebserver.domain.rest.admin.SebLmsSetup.LMSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LmsAPIConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(LmsAPIConnectionFactory.class);

    private final SebLmsSetupRecordMapper sebLmsSetupRecordMapper;

    public LmsAPIConnectionFactory(final SebLmsSetupRecordMapper sebLmsSetupRecordMapper) {
        this.sebLmsSetupRecordMapper = sebLmsSetupRecordMapper;
    }

    public LmsConnectionTemplate getLmsAPIConnection(final Long sebLmsSetupId) {
        final SebLmsSetupRecord sebLmsSetup = this.sebLmsSetupRecordMapper.selectByPrimaryKey(sebLmsSetupId);
        if (sebLmsSetup == null) {
            log.error("No SebLmsSetup found for id: {}", sebLmsSetupId);
            return null;
        }

        final LMSType type = LMSType.valueOf(sebLmsSetup.getLmsType());

        switch (type) {
            case MOODLE: {
                // TODO
                throw new UnsupportedOperationException("TODO");
            }
            case OPEN_EDX: {
                // TODO
                throw new UnsupportedOperationException("TODO");
            }
            case OPEN_OLAT: {
                // TODO
                throw new UnsupportedOperationException("TODO");
            }
            default: {
                return new LmsMockAPI(SebLmsSetup.of(sebLmsSetup));
            }
        }
    }

}
