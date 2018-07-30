/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.util;

import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionalBounds<T> implements Function<Supplier<T>, Result<T>> {

    private static final Logger log = LoggerFactory.getLogger(TransactionalBounds.class);

    private final PlatformTransactionManager transactionManager;

    public TransactionalBounds(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Result<T> apply(final Supplier<T> runInTransaction) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        return transactionTemplate.execute(new TransactionCallback<Result<T>>() {

            @Override
            public Result<T> doInTransaction(final TransactionStatus status) {
                try {
                    return Result.of(runInTransaction.get());
                } catch (final Throwable t) {
                    log.error("Unexpected error while run in transaction. Apply rollback and return error Result", t);
                    TransactionalBounds.this.transactionManager.rollback(status);
                    return Result.ofError(t);
                }
            }
        });
    }

    public final Result<T> runInTransaction(final Supplier<T> runInTransaction) {
        return new TransactionalBounds<T>(this.transactionManager).apply(runInTransaction);
    }

    public final Result<T> runInTransaction(final Runnable runnable) {
        return runInTransaction(() -> {
            runnable.run();
            return null;
        });
    }

}
