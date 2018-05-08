/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import java.util.Collection;
import java.util.function.Predicate;

/** A general interface to all service DAO's.
 *
 * The service-DAO layer lie just on top of the data access layer that contains generated domain model classes and
 * mapper interfaces of MyBatis and is a transition layer between the data access- and the business-layer. In the
 * service-DAO layer some major data persistence aspects lime transactions and/or caching, should be covered
 *
 * The service-DAO layer should not throw any exceptions that may happen on this or lower level. Exceptions should be
 * catch and logged and if there is a return value expected, a NULL-Value should be returned that can be recognized as
 * such from the above layers.
 *
 * @author anhefti
 *
 * @param <M> The domain model type of the DAO */
public interface BaseDao<M> {

    /** Use this to get a data row by identifier (primary key) within the concrete domain model type.
     *
     * @param id the identifier (primary key) of the data row to get from persistent
     * @return Instance of domain model with the row data or a NULL-Model instance if there is no such data row */
    M byId(Long id);

    /** Use this to get all rows from the persistence store within a collection of concrete domain model.
     *
     * @return a collection of concrete domain model or an empty list if there are none or an unexpected exception
     *         happened */
    Collection<M> getAll();

    /** Use this to get all rows from the persistence store that matches the given predicate, within a collection of
     * concrete domain model.
     *
     * @param predicate the predicate
     * @return a collection of concrete domain model or an empty list if there are none or an unexpected exception
     *         happened */
    Collection<M> getAll(Predicate<M> predicate);

    /** Use this to save the data of a concrete domain model to persistence store. This checks whether the domain model
     * (M) is new (primary-key is null) or not and performs an insert or update in case
     *
     * @param model the domain model instance to save to persistence store
     * @return the saved domain model instance (with an new primary-key in the case of insert) or a NULL-Model instance
     *         on exception or failure */
    M save(M model);

    /** Use this to delete a domain model from persistence store by id (primary key)
     *
     * @param id the identifier (primary key) of the data row to get from persistent
     * @return the domain mode of the deleted row (primary-key is null) or a NULL-Model instance on exception or
     *         failure */
    M delete(Long id);

    /** Use this to check whether a specified concrete domain Model instance is the NULL-Model or not.
     *
     * @param model the domain Model instance to check against NULL
     * @return true if the specified concrete domain Model instance is the NULL-Model */
    boolean isNull(final M model);
}
