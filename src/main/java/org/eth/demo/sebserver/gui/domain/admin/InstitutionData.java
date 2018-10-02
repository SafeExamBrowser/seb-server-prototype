/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eth.demo.util.Tuple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InstitutionData {

    public static final List<Tuple<String>> AUTH_TYPE_SELECTION;

    public final String name;
    public final String authType;
    public final Collection<String> authTypeSelection;
    public final Collection<String> lmsTypeSelection;
    public final Collection<LMSSetup> lmsSetup;

    static {
        final List<Tuple<String>> mapping = new ArrayList<>();
        mapping.add(new Tuple<>("INTERNAL", "org.sebserver.form.institution.authType.internal"));
        mapping.add(new Tuple<>("EXTERNAL_LDAP", "org.sebserver.form.institution.authType.ldap"));
        AUTH_TYPE_SELECTION = Collections.unmodifiableList(mapping);
    }

    @JsonCreator
    public InstitutionData(
            @JsonProperty(value = "name") final String name,
            @JsonProperty(value = "authType") final String authType,
            @JsonProperty(value = "authTypeSelection") final Collection<String> authTypeSelection,
            @JsonProperty(value = "lmsTypeSelection") final Collection<String> lmsTypeSelection,
            @JsonProperty(value = "lmsSetup") final Collection<LMSSetup> lmsSetup) {

        this.name = name;
        this.authType = authType;
        this.authTypeSelection = authTypeSelection;
        this.lmsTypeSelection = lmsTypeSelection;
        this.lmsSetup = lmsSetup;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthType() {
        return this.authType;
    }

    public Collection<LMSSetup> getLmsSetup() {
        return this.lmsSetup;
    }

    public final static class LMSSetup {

        public final String lmsType;
        public final String lmsAuthName;
        public final String lmsAuthSecret;
        public final String lmsApiUrl;
        public final String sebAuthName;
        public final String sebAuthSecret;

        public LMSSetup(
                @JsonProperty(value = "lmsType") final String lmsType,
                @JsonProperty(value = "lmsAuthName") final String lmsAuthName,
                @JsonProperty(value = "lmsAuthSecret") final String lmsAuthSecret,
                @JsonProperty(value = "lmsApiUrl") final String lmsApiUrl,
                @JsonProperty(value = "sebAuthName") final String sebAuthName,
                @JsonProperty(value = "sebAuthSecret") final String sebAuthSecret) {

            this.lmsType = lmsType;
            this.lmsAuthName = lmsAuthName;
            this.lmsAuthSecret = lmsAuthSecret;
            this.lmsApiUrl = lmsApiUrl;
            this.sebAuthName = sebAuthName;
            this.sebAuthSecret = sebAuthSecret;
        }
    }

}
