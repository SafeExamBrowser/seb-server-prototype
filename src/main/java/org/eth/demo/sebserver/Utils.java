/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver;

import java.util.Collection;

public interface Utils {

    public abstract class Collections {

        public static final <T> T getFirst(final Collection<T> collection) {
            if (collection == null || collection.isEmpty()) {
                return null;
            }

            return collection.iterator().next();
        }

        public static final <T> T getSingle(final Collection<T> collection) {
            if (collection == null || collection.isEmpty() || collection.size() > 1) {
                throw new IllegalArgumentException("The collection is null, empty or contains more then one objects");
            }

            return collection.iterator().next();
        }
    }

}
