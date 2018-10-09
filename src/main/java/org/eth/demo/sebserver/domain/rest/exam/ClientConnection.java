/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.exam;

import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientConnection {

    public enum ConnectionStatus {
        CONNECTION_REQUESTED,
        AUTHENTICATED,
        ESTABLISHED,
        CLOSED,
        ABORTED,
        RELEASED
    }

    public final Long id;
    public final Long examId;
    public final ConnectionStatus status;
    public final String username;
    public final boolean vdi;
    public final String clientAddress;
    public final String virtualClientAddress;

    @JsonCreator
    public ClientConnection(
            @JsonProperty("id") final Long id,
            @JsonProperty("examId") final Long examId,
            @JsonProperty("status") final ConnectionStatus status,
            @JsonProperty("username") final String username,
            @JsonProperty("vdi") final boolean vdi,
            @JsonProperty("clientAddress") final String clientAddress,
            @JsonProperty("virtualClientAddress") final String virtualClientAddress) {

        this.id = id;
        this.examId = examId;
        this.status = status;
        this.username = username;
        this.vdi = vdi;
        this.clientAddress = clientAddress;
        this.virtualClientAddress = virtualClientAddress;
    }

    public Long getId() {
        return this.id;
    }

    public Long getExamId() {
        return this.examId;
    }

    public ConnectionStatus getStatus() {
        return this.status;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isVdi() {
        return this.vdi;
    }

    public String getVirtualClientAddress() {
        return this.virtualClientAddress;
    }

    public static final ClientConnection fromRecord(final ClientConnectionRecord record) {

        return new ClientConnection(
                record.getId(),
                record.getExamId(),
                ClientConnection.ConnectionStatus.valueOf(record.getStatus()),
                record.getUserName(),
                record.getVdi(),
                record.getClientAddress(),
                record.getVirtualClientAddress());
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
        final ClientConnection other = (ClientConnection) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ClientConnection [id=" + this.id + ", examId=" + this.examId + ", status=" + this.status + ", username="
                + this.username
                + ", vdi=" + this.vdi + ", clientAddress=" + this.clientAddress + ", virtualClientAddress="
                + this.virtualClientAddress
                + "]";
    }

}
