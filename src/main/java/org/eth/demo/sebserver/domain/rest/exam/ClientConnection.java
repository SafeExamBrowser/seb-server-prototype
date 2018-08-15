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
        CONNECTION_REQUESTED, AUTHENTICATED, WEB_SOCKET_ESTABLISHED, ABORTED, FINISHED
    }

    public final Long id;
    public final Long examId;
    public final ConnectionStatus status;
    public final String userIdentifier;
    public final String clientAddress;

    @JsonCreator
    public ClientConnection(
            @JsonProperty("id") final Long id,
            @JsonProperty("examId") final Long examId,
            @JsonProperty("status") final ConnectionStatus status,
            @JsonProperty("userIdentifier") final String userIdentifier,
            @JsonProperty("clientAddress") final String clientAddress) {

        this.id = id;
        this.examId = examId;
        this.status = status;
        this.userIdentifier = userIdentifier;
        this.clientAddress = clientAddress;
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

    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public static final ClientConnection fromRecord(final ClientConnectionRecord record) {

        return new ClientConnection(
                record.getId(),
                record.getExamId(),
                ClientConnection.ConnectionStatus.valueOf(record.getStatus()),
                record.getUserIdentifier(),
                record.getClientAddress());
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
        return "ClientConnection [id=" + this.id + ", examId=" + this.examId + ", status=" + this.status
                + ", userIdentifier="
                + this.userIdentifier + ", clientAddress=" + this.clientAddress + "]";
    }

}
