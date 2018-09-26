/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import javax.servlet.http.HttpServletRequest;

import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class LMSClientAuthenticationFilter extends AbstractClientAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LMSClientAuthenticationFilter.class);

    public static final String CONNECTION_TOKEN_PARAM_NAME = "connectionToken";

    public LMSClientAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher,
            final ExamConnectionService examConnectionService) {

        super(defaultAuthenticationEventPublisher,
                examConnectionService);
    }

    @Override
    protected ClientConnectionAuth auth(
            final String username,
            final String password,
            final HttpServletRequest httpRequest) {

        log.debug("Apply filter LMSClientAuthenticationFilter");

        final SebLmsSetupRecord matching = this.examConnectionService.getLMSSetup(username, password, true)
                .onError(t -> {
                    log.error("Unable to find matching LMS-Setup for LMS-Client name: {}", username, t);
                    throw new BadClientCredentialsException();
                });

        log.debug("Found match: {}", matching);

        return ClientConnectionAuth.lmsAuthOf(
                matching,
                httpRequest.getRemoteAddr());
    }

    @Override
    protected Role getRole() {
        return Role.LMS_CLIENT;
    }

}
