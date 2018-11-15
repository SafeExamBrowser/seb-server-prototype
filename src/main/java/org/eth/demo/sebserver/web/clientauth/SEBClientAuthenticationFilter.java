/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import static org.eth.demo.sebserver.web.clientauth.SEBClientConnectionController.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection.ConnectionStatus;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.web.WebSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBClientAuthenticationFilter extends AbstractClientAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(SEBClientAuthenticationFilter.class);

    public SEBClientAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher,
            final ExamConnectionService examConnectionService) {

        super(defaultAuthenticationEventPublisher,
                examConnectionService);
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        try {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;

            if (httpRequest.getRequestURI().startsWith("/ws")) {

                log.debug("SEB-Client Web-Socket connection authentication step");

                doFilterWebSocketConnection(httpRequest);
                chain.doFilter(request, response);
                return;
            }
        } catch (final Exception e) {
            log.error("Unexpected Error during authentication: ", e);
            this.defaultAuthenticationEventPublisher.publishAuthenticationFailure(
                    new InternalAuthenticationServiceException("Unexpected Error during authentication", e),
                    null);
            return;
        }

        super.doFilter(request, response, chain);
    }

    private void doFilterWebSocketConnection(final HttpServletRequest httpRequest) {
        final String connectionToken = (httpRequest.getParameterMap().containsKey(CONNECTION_TOKEN_KEY_NAME))
                ? httpRequest.getParameter(CONNECTION_TOKEN_KEY_NAME)
                : httpRequest.getHeader(CONNECTION_TOKEN_KEY_NAME);

        if (StringUtils.isBlank(connectionToken)) {
            log.error("Missing connectionToken on SEB-Client WebSocket connect");
            throw new IllegalArgumentException("Missing connectionToken on SEB-Client WebSocket connect");
        }

        final ClientConnectionRecord connection = this.examConnectionService.getConnectionByToken(
                connectionToken,
                ConnectionStatus.AUTHENTICATED)
                .onError(t -> {
                    log.error("Unable to find SEB-Client connection in status: {} for token: {}",
                            ConnectionStatus.AUTHENTICATED.name(), connectionToken);
                    throw new BadClientCredentialsException();
                });

        SecurityContextHolder
                .getContext()
                .setAuthentication(ClientConnectionAuth.sebWebSocketAuthOf(
                        connection,
                        getBooleanAttr(httpRequest, VDI_FLAG_KEY_NAME),
                        getBooleanAttr(httpRequest, VIRTUAL_CLIENT_FLAG_KEY_NAME)));
    }

    @Override
    protected ClientConnectionAuth auth(
            final String username,
            final String password,
            final HttpServletRequest httpRequest) {

        log.debug("Apply filter SEBClientAuthenticationFilter");

        final SebLmsSetupRecord matching = this.examConnectionService.getLMSSetup(username, password, false)
                .onError(t -> {
                    log.error("Unable to find matching LMS-Setup for SEB-Client name: {}", username, t);
                    throw new BadClientCredentialsException();
                });

        log.debug("Found match: {}", matching);

        return ClientConnectionAuth.sebAuthOf(
                matching,
                httpRequest.getRemoteAddr(),
                getBooleanAttr(httpRequest, VDI_FLAG_KEY_NAME),
                getBooleanAttr(httpRequest, VIRTUAL_CLIENT_FLAG_KEY_NAME));
    }

    @Override
    protected Role getRole() {
        return Role.SEB_CLIENT;
    }

    private boolean getBooleanAttr(final HttpServletRequest httpRequest, final String name) {
        final Object flag = httpRequest.getAttribute(name);
        return (flag != null && BooleanUtils.toBoolean(String.valueOf(flag)));
    }

    @Override
    protected boolean filterMatch(final HttpServletRequest httpRequest) {
        return WebSecurityConfig.SEB_CLIENT_ENDPOINTS.matches(httpRequest);
    }

}
