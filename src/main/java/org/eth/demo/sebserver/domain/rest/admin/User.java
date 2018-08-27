/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.model.RoleRecord;
import org.eth.demo.sebserver.batis.gen.model.UserRecord;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class User implements UserDetails {

    private static final long serialVersionUID = -2490638288935417827L;

    public final Long id;
    public final Long institutionId;
    public final String name;
    public final String username;
    public final String password;
    public final String email;
    public final DateTime creationDate;
    public final Boolean active;
    public final Collection<Role> roles;

    @JsonCreator
    public User(
            @JsonProperty("id") final Long id,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("name") final String name,
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("email") final String email,
            @JsonProperty("creationDate") final DateTime creationDate,
            @JsonProperty("active") final Boolean active,
            @JsonProperty("roles") final Collection<Role> roles) {

        this.id = id;
        this.institutionId = institutionId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() {
        return this.id;
    }

    public Long getInstitutionId() {
        return this.institutionId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public DateTime getCreationDate() {
        return this.creationDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // TODO??
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @Override
    public String toString() {
        return "User [id=" + this.id + ", institutionId=" + this.institutionId + ", name=" + this.name + ", username="
                + this.username
                + ", password=" + this.password + ", email=" + this.email + ", creationDate=" + this.creationDate
                + ", active="
                + this.active + ", roles=" + this.roles + "]";
    }

    public static User fromRecord(final UserRecord record, final List<RoleRecord> roles) {
        return new User(
                record.getId(),
                record.getInstitutionId(),
                record.getName(),
                record.getUserName(),
                record.getPassword(),
                record.getEmail(),
                record.getCreationDate(),
                record.getActive(),
                roles.stream()
                        .map(Role::fromRecord)
                        .collect(Collectors.toList()));
    }

}
