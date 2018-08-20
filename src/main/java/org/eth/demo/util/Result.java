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

public class Result<T> {
    public final T value;
    public final Throwable error;

    public Result(final T value) {
        this.value = value;
        this.error = null;
    }

    public Result(final Throwable error) {
        this.value = null;
        this.error = error;
    }

    public T orElse(final T other) {
        return this.value != null ? this.value : other;
    }

    public T orElse(final Supplier<T> supplier) {
        return this.value != null ? this.value : supplier.get();
    }

    public <U> Result<U> map(final Function<T, U> mapf) {
        if (this.error == null) {
            return Result.of(mapf.apply(this.value));
        } else {
            return Result.ofError(this.error);
        }
    }

    public T onError(final Function<Throwable, T> errorHandler) {
        return this.error != null ? errorHandler.apply(this.error) : this.value;
    }

    public static final <T> Result<T> of(final T value) {
        return new Result<>(value);
    }

    public static final <T> Result<T> ofError(final Throwable error) {
        return new Result<>(error);
    }
}
