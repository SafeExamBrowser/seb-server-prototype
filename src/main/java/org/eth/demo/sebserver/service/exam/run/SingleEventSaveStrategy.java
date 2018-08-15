/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
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
@Component(EventHandlingStrategy.EVENT_CONSUMER_STRATEGY_SINGLE_EVENT_STORE)
public class SingleEventSaveStrategy implements EventHandlingStrategy {

    private static final Logger log = LoggerFactory.getLogger(SingleEventSaveStrategy.class);

    private final ClientEventRecordMapper clientEventRecordMapper;
    private ClientConnectionDelegate clientConnectionDelegate;

    private boolean enabled = false;

    public SingleEventSaveStrategy(final ClientEventRecordMapper clientEventRecordMapper) {
        this.clientEventRecordMapper = clientEventRecordMapper;
    }

    @Override
    public void setClientConnectionDelegate(final ClientConnectionDelegate clientConnectionDelegate) {
        this.clientConnectionDelegate = clientConnectionDelegate;
    }

    @Override
    public void enable(final boolean enable) {
        this.enabled = enable;
    }

    @Transactional
    @Async
    @Override
    public void accept(final ClientEvent event) {
        if (!this.enabled) {
            log.warn("Received ClientEvent on none enabled SingleEventSaveStrategy. ClientEvent is ignored");
            return;
        }

        try {
            final int saved = this.clientConnectionDelegate
                    .getClientConnection(event.userIdentifier)
                    .map(cc -> this.clientEventRecordMapper.insert(event.toRecord(cc.clientConnection.examId)))
                    .orElse(0);

            if (saved < 1) {
                log.error("Missing client connection for event store {}", event.userIdentifier);
                throw new IllegalStateException("Missing client connection for event store " + event.userIdentifier);
            }
        } catch (final Exception e) {
            log.error("Unexpected error while trying to save client event: {}, {} : ",
                    event.userIdentifier,
                    event,
                    e);
        }
    }

}
