/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

public class SEBClientAuth {

    public final String connectionToken;
    public final String clientAddress;

    public SEBClientAuth(
            final String connectionToken,
            final String clientAddress) {

        this.connectionToken = connectionToken;
        this.clientAddress = clientAddress;
    }

    public String getConnectionToken() {
        return this.connectionToken;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public Long getInstitutionId() {
        // TODO extract the institution id from connectionToken
        return 1l;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.connectionToken == null) ? 0 : this.connectionToken.hashCode());
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
        final SEBClientAuth other = (SEBClientAuth) obj;
        if (this.connectionToken == null) {
            if (other.connectionToken != null)
                return false;
        } else if (!this.connectionToken.equals(other.connectionToken))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SEBClientAuth [connectionToken=" + this.connectionToken + ", clientAddress=" + this.clientAddress + "]";
    }

}
