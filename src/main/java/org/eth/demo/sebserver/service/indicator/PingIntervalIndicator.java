/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.indicator;

import static org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordDynamicSqlSupport.clientEventRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;

import java.math.BigDecimal;

import org.eth.demo.sebserver.batis.ClientEventExtentionMapper;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.ClientEvent.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PingIntervalIndicator extends ClientIndicatorAdapter {

    public static final String BEAN_NAME = "pingIntervalIndicator";
    private static final String DISPLAY_NAME = "Last Ping";

    private ClientEventExtentionMapper clientEventExtentionMapper;

    public PingIntervalIndicator(final ClientEventExtentionMapper clientEventExtentionMapper) {
        this.clientEventExtentionMapper = clientEventExtentionMapper;
    }

    @Autowired
    public void setClientEventExtentionMapper(final ClientEventExtentionMapper clientEventExtentionMapper) {
        this.clientEventExtentionMapper = clientEventExtentionMapper;
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
    public BigDecimal computeValueAt(final Long timestamp) {
        final long time = (timestamp != null) ? timestamp : System.currentTimeMillis();

        final Long lastPing = this.clientEventExtentionMapper.maxByExample(clientEventRecord.timestamp)
                .where(clientEventRecord.examId, isEqualTo(this.examId))
                .and(clientEventRecord.clientId, isEqualTo(this.clientId))
                .and(clientEventRecord.type, isEqualTo(ClientEvent.EventType.PING.id))
                .and(clientEventRecord.timestamp, isLessThan(time))
                .build()
                .execute();

        if (lastPing == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(time - lastPing);
    }

    @Override
    public void notifyClientEvent(final ClientEvent event) {
        if (event.type == EventType.PING) {
            this.currentValue = new BigDecimal(System.currentTimeMillis() - event.timestamp);
        }
    }

}
