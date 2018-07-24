/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class DeleteTokenEndpoint {

    private final ConsumerTokenServices tokenServices;

    public DeleteTokenEndpoint(final ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void logout(final HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            final String tokenId = authHeader.substring("Bearer".length() + 1);
            this.tokenServices.revokeToken(tokenId);
        }
    }

}
