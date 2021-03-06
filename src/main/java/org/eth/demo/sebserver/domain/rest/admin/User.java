/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.domain.rest.admin;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

    public final String uuid;
    public final Long institutionId;
    public final String name;
    public final String username;
    public final String password;
    public final String email;
    public final DateTime creationDate;
    public final Boolean active;
    public final Locale locale;
    public final EnumSet<Role> roles;
    public final boolean isExternal;

    @JsonCreator
    public User(
            @JsonProperty("uuid") final String uuid,
            @JsonProperty("institutionId") final Long institutionId,
            @JsonProperty("name") final String name,
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("email") final String email,
            @JsonProperty("creationDate") final DateTime creationDate,
            @JsonProperty("active") final Boolean active,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("roles") final Set<String> roles) {

        this.uuid = uuid;
        this.institutionId = institutionId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
        this.locale = locale;
        this.roles = (roles != null)
                ? EnumSet.copyOf(roles.stream().map(r -> Role.valueOf(r)).collect(Collectors.toList()))
                : EnumSet.noneOf(Role.class);
        this.isExternal = false;
    }

    /** Use this to create a User after successful external authentication
     *
     * @param uuid The uuid of the user --> provided by the external authentication and authorization provider
     * @param name The real name of the user --> provided by the external authentication and authorization provider
     * @param username The username of the user --> provided by the external authentication and authorization provider
     * @param email The email address of the user --> provided by the external authentication and authorization provider
     *            or null
     * @param creationDate The creation date of the user --> provided by the external authentication and authorization
     *            provider or null
     * @param locale The preferred locale of the user --> provided by the external authentication and authorization
     *            provider or null
     * @param roles The roles of the user --> provided by the external authentication and authorization provider */
    public User(
            final String uuid,
            final String name,
            final String username,
            final String email,
            final DateTime creationDate,
            final Locale locale,
            final Role... roles) {

        this.uuid = uuid;
        this.institutionId = -1L;
        this.name = name;
        this.username = username;
        this.password = null;
        this.email = email;
        this.creationDate = creationDate;
        this.active = true;
        this.locale = locale;
        this.roles = (roles != null)
                ? EnumSet.copyOf(Arrays.asList(roles))
                : EnumSet.noneOf(Role.class);
        this.isExternal = true;
    }

    public String getUuid() {
        return this.uuid;
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

    public Locale getLocale() {
        return this.locale;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public boolean containsAny(final Role... roles) {
        if (roles == null) {
            return false;
        }

        for (final Role role : roles) {
            if (this.roles.contains(role)) {
                return true;
            }
        }

        return false;
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
        return "User [uuid=" + this.uuid + ", institutionId=" + this.institutionId + ", name=" + this.name
                + ", username=" + this.username
                + ", password=" + this.password + ", email=" + this.email + ", creationDate=" + this.creationDate
                + ", active="
                + this.active + ", locale=" + this.locale + ", roles=" + this.roles + ", isExternal=" + this.isExternal
                + "]";
    }

    public static User fromRecord(final UserRecord record, final List<RoleRecord> roles) {
        return new User(
                record.getUuid(),
                record.getInstitutionId(),
                record.getName(),
                record.getUserName(),
                record.getPassword(),
                record.getEmail(),
                record.getCreationDate(),
                record.getActive(),
                new Locale(record.getLocale()),
                roles.stream()
                        .map(r -> r.getRoleName())
                        .collect(Collectors.toSet()));
    }

}
