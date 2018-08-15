/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import static org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport.sebClientname;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.Role.UserRole;
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection.ConnectionStatus;
import org.eth.demo.sebserver.web.clientauth.ClientAuth.ClientAuthentication;
import org.eth.demo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SEBClientAuthenticationFilter extends AbstractClientAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(SEBClientAuthenticationFilter.class);

    // TODO create and use Dao here and also inject by constructor
    @Autowired
    private ClientConnectionRecordMapper clientConnectionRecordMapper;

    public SEBClientAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher,
            final SebLmsSetupRecordMapper sebLmsSetupRecordMapper,
            final PasswordEncoder clientPasswordEncoder) {

        super(defaultAuthenticationEventPublisher,
                sebLmsSetupRecordMapper,
                clientPasswordEncoder);
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        try {
            // TODO: For now WebSocket connection attempt is just railroaded.
            //       Later the WebSocket connection must also have an access token within and a check with LMS must be done here
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (httpRequest.getRequestURI().startsWith("/ws")) {

                log.debug("************************ SEB-Client Web-Socket connection authentication step");

                final String connectionToken = httpRequest.getHeader(
                        SEBClientConnectionController.CONNECTION_TOKEN_KEY_NAME);

                if (StringUtils.isBlank(connectionToken)) {
                    //throw new ConnectException("Missing connectionToken within request header");
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(new ClientAuthentication(
                                    "[NONE]",
                                    new ClientAuth(
                                            null,
                                            connectionToken,
                                            httpRequest.getRemoteAddr()),
                                    Role.UserRole.LMS_CLIENT));

                    chain.doFilter(request, response);
                    return;
                }

                final ClientConnectionRecord connection = Utils.getSingle(this.clientConnectionRecordMapper
                        .selectByExample()
                        .where(ClientConnectionRecordDynamicSqlSupport.status,
                                isEqualTo(ConnectionStatus.AUTHENTICATED.name()))
                        .and(ClientConnectionRecordDynamicSqlSupport.connectionToken,
                                isEqualTo(connectionToken))
                        .build()
                        .execute());

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new ClientAuthentication(
                                connection.getUserIdentifier(),
                                new ClientAuth(
                                        null,
                                        connectionToken,
                                        httpRequest.getRemoteAddr()),
                                Role.UserRole.LMS_CLIENT));

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

    @Override
    protected ClientAuth auth(
            final String username,
            final String password,
            final HttpServletRequest httpRequest) {

        log.debug("Apply filter SEBClientAuthenticationFilter");

        final List<SebLmsSetupRecord> setups = this.sebLmsSetupRecordMapper.selectByExample()
                .where(sebClientname, isEqualTo(username))
                .build()
                .execute();

        log.debug("Found SebLmsSetupRecord matches: {}", setups);

        final SebLmsSetupRecord matching = Utils.getSingle(setups.stream()
                .filter(record -> this.clientPasswordEncoder.matches(password, record.getSebClientsecret()))
                .collect(Collectors.toList()));

        log.debug("Found match: {}", matching);

        return new ClientAuth(
                matching.getInstitutionId(),
                matching.getSebClientname(),
                httpRequest.getRemoteAddr(),
                matching.getLmsUrl());
    }

    @Override
    protected UserRole getRole() {
        return UserRole.SEB_CLIENT;
    }

}
