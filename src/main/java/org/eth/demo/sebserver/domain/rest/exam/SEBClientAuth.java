/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SEBClientAuth {

    public final String username;
    public final String authToken;
    public final String clientAddress;

    @JsonCreator
    public SEBClientAuth(
            @JsonProperty("username") final String username,
            @JsonProperty("authToken") final String authToken,
            @JsonProperty("clientAddress") final String clientAddress) {

        this.username = username;
        this.authToken = authToken;
        this.clientAddress = clientAddress;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
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
        if (this.username == null) {
            if (other.username != null)
                return false;
        } else if (!this.username.equals(other.username))
            return false;
        return true;
    }

}
