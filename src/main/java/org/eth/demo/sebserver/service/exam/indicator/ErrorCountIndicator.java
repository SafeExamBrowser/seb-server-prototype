/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.indicator;

import static org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordDynamicSqlSupport.clientEventRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;

import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent.EventType;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component(ErrorCountIndicator.BEAN_NAME)
@Scope("prototype")
public class ErrorCountIndicator extends ClientIndicatorAdapter {

    public static final String BEAN_NAME = "errorCountIndicator";
    private static final String DISPLAY_NAME = "Error Logs";

    private final ClientEventRecordMapper clientEventRecordMapper;

    public ErrorCountIndicator(final ClientEventRecordMapper clientEventRecordMapper) {
        this.clientEventRecordMapper = clientEventRecordMapper;
    }

    @Override
    public String getType() {
        return BEAN_NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    @Transactional(readOnly = true)
    public Double computeValueAt(final Long timestamp) {
        final Long time = (timestamp != null) ? timestamp : System.currentTimeMillis();

        final Long errors = this.clientEventRecordMapper.countByExample()
                .where(clientEventRecord.connectionId, isEqualTo(this.connectionId))
                .and(clientEventRecord.type, isEqualTo(ClientEvent.EventType.ERROR.id))
                .and(clientEventRecord.timestamp, isLessThan(time))
                .build()
                .execute();

        return errors.doubleValue();
    }

    @Override
    public void notifyClientEvent(final ClientEvent event) {
        if (event.type == EventType.ERROR) {
            this.currentValue = getCurrentValue().doubleValue() + 1.0d;
        }
    }

}
