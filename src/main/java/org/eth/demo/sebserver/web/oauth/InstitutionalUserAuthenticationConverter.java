/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.JSONMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import com.fasterxml.jackson.core.JsonProcessingException;

/** NOTE: This is as an example of a specialized authentication token converter for external user authentication
 * mechanisms
 *
 * In the case we have an successfully authenticated external-user. an external user means one that was not
 * authenticated by the internal JDBC-based authentication provider but within a specialized ExternalAuthProvider like
 * LDAP. In this case, the user-principal of that is converted to JSON format and put to the attributes map with the
 * attribute "USER" and also an "EXTERNAL" indicator is put to the attributes map. The attributes map is later used to
 * put all the attributes in the JwToken that is been created and stored for further token-based authentication
 * mechanisms within OAuth2.
 *
 * @author anhefti */
public class InstitutionalUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    private static final Logger log = LoggerFactory.getLogger(InstitutionalUserAuthenticationConverter.class);

    private final JSONMapper jsonMapper;

    public InstitutionalUserAuthenticationConverter(final JSONMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Map<String, ?> convertUserAuthentication(final Authentication authentication) {
        final Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            final User user = (User) principal;
            if (user.isExternal) {
                final Map<String, Object> response = new LinkedHashMap<>();
                response.put("EXTERNAL", true);
                try {
                    final String writeValueAsString = this.jsonMapper.writeValueAsString(user);
                    response.put("USER", writeValueAsString);
                    return response;
                } catch (final JsonProcessingException e) {
                    log.error("Unexpected error while tring to convert external user authentication: ", e);
                }
            }
        }

        return super.convertUserAuthentication(authentication);
    }

    @Override
    public Authentication extractAuthentication(final Map<String, ?> map) {
        if (map.containsKey("EXTERNAL")) {
            final String userJSON = (String) map.get("USER");
            try {
                final User user = this.jsonMapper.readValue(userJSON, User.class);
                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());
            } catch (final IOException e) {
                log.error("Unexpected error while tring to convert external user authentication: ", e);
            }
        }

        return super.extractAuthentication(map);
    }

}
