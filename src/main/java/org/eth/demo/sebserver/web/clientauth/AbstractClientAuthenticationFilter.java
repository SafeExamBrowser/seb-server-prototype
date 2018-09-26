/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public abstract class AbstractClientAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(AbstractClientAuthenticationFilter.class);

    protected final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher;
    protected final ExamConnectionService examConnectionService;

    protected AbstractClientAuthenticationFilter(
            final DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher,
            final ExamConnectionService examConnectionService) {

        this.defaultAuthenticationEventPublisher = defaultAuthenticationEventPublisher;
        this.examConnectionService = examConnectionService;
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        try {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final ClientConnectionAuth clientAuth = verifyClientFromCredentials(httpRequest);
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(clientAuth);
        } catch (final Exception e) {
            log.error("Unexpected error while trying to verify client-user from credientials", e);
            this.defaultAuthenticationEventPublisher.publishAuthenticationFailure(
                    new InternalAuthenticationServiceException("Unexpected Error during authentication", e),
                    null);
        }

        chain.doFilter(request, response);
    }

    private ClientConnectionAuth verifyClientFromCredentials(final HttpServletRequest httpRequest)
            throws UnsupportedEncodingException {

        // First try to get the SEB-Client credentials from header AUTHORIZATION
        final String credentials = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(credentials)) {
            return getFromBasicAuth(httpRequest, credentials);
        }

        // Second try to extract credentials form parameter
        final String username = httpRequest.getParameter("username");
        if (StringUtils.isNotBlank(username)) {
            final String password = httpRequest.getParameter("password");
            return auth(username, password, httpRequest);
        }

        throw new BadCredentialsException("Unable to extract client credentials");
    }

    private ClientConnectionAuth getFromBasicAuth(
            final HttpServletRequest httpRequest,
            final String credentials) throws UnsupportedEncodingException {

        final String clientCredentials = credentials.startsWith("Basic")
                ? credentials.substring(6)
                : credentials;
        String username = null;
        String password = null;

        final byte[] decoded = Base64.getDecoder().decode(clientCredentials.getBytes("UTF-8"));

        final String token = new String(decoded, "UTF-8");
        final String[] tokens = StringUtils.split(token, ":");

        username = tokens[0];
        password = tokens[1];

        return auth(username, password, httpRequest);
    }

    protected abstract ClientConnectionAuth auth(
            String username,
            String password,
            HttpServletRequest httpRequest);

    protected abstract Role getRole();

}
