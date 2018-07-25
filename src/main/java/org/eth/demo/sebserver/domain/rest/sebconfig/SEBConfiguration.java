/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.sebconfig;

import java.util.Collection;
import java.util.Collections;

import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;

public final class SEBConfiguration {

    public final Long id;
    public final String name;
    public final Long ownerId;
    public final Collection<ExamSEBConfigMapping> examMappings;

    public SEBConfiguration(final Long id, final String name, final Long ownerId,
            final Collection<ExamSEBConfigMapping> examMappings) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.examMappings = (examMappings != null)
                ? Collections.unmodifiableCollection(examMappings)
                : Collections.emptyList();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Collection<ExamSEBConfigMapping> getExamMappings() {
        return this.examMappings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SEBConfiguration other = (SEBConfiguration) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SEBConfiguration [id=" + this.id + ", name=" + this.name + ", ownerId=" + this.ownerId
                + ", examMappings="
                + this.examMappings + "]";
    }

}
