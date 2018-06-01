/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.event;

import org.eth.demo.sebserver.SEBServer;
import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.service.ClientConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Approach 1 to handle/save client events internally
 *
 * This saves a client event to persistence. With the @Async annotation spring executes every call in an own thread from
 * a ThreadPool. This gives some performance-boost but if there are a lot of clients connected, firing events at small
 * intervals like 100ms, this is blocking to much because every event is saved within its own SQL commit and also
 * transactional.
 *
 * An advantage of this approach is minimal data loss on server fail. **/
@Lazy
@Component(SEBServer.EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE)
public class SingleEventSaveStrategy implements EventHandlingStrategy {

    private static final Logger log = LoggerFactory.getLogger(SingleEventSaveStrategy.class);

    private final ClientConnectionService clientConnectionService;
    private final ClientEventRecordMapper clientEventRecordMapper;

    public SingleEventSaveStrategy(
            final ClientConnectionService clientConnectionService,
            final ClientEventRecordMapper clientEventRecordMapper) {

        this.clientConnectionService = clientConnectionService;
        this.clientEventRecordMapper = clientEventRecordMapper;
    }

    @Transactional
    @Async
    @Override
    public void accept(final ClientEvent event) {
        try {
            if (!this.clientConnectionService.checkActiveConnection(event.clientId)) {
                throw new IllegalStateException("Client with UUID: " + event.clientId + " is not registered");
            }

            final int saved = this.clientConnectionService
                    .getClientConnection(event.clientId)
                    .map(cc -> this.clientEventRecordMapper.insert(event.toRecord(
                            cc.examId,
                            cc.clientId)))
                    .orElse(0);

            if (saved < 1) {
                System.out.println("######################## missing connection: " + event.clientId);
            } else {
//              System.out.println("************************* saveClientEvent on Thread: " + Thread.currentThread());
            }
        } catch (final Exception e) {
            log.error("Unexpected error while trying to save client event: {}, {} : ", event.clientId, event, e);
        }
    }

}
