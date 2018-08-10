/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.util;

import java.util.Collection;

public abstract class Utils {

    /** Use this to extract a single element from a Collection. This also checks if there is only a single element
     * within the Collection.
     * 
     * @param collection the Collection to extract the single and only element
     * @return The single element
     * @throws IllegalArgumentException if the collection is null, or empty or has more then one element */
    public static final <T> T getSingle(final Collection<T> collection) {
        if (collection == null || collection.isEmpty() || collection.size() > 1) {
            throw new IllegalArgumentException(
                    "Collection has no or more then one element. Expected is exaclty one: " + collection);
        }

        return collection.iterator().next();
    }

}