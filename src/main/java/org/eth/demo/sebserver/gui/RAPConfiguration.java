/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RAPConfiguration implements ApplicationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RAPConfiguration.class);

    @Override
    public void configure(final Application application) {
        application.addEntryPoint("/gui", RAPSpringEntryPointFactory, null);
        application.addStyleSheet(RWT.DEFAULT_THEME_ID, "static/css/sebserver.css");
    }

    private static final EntryPointFactory RAPSpringEntryPointFactory = new EntryPointFactory() {

        @Override
        public EntryPoint create() {
            return new AbstractEntryPoint() {

                private static final long serialVersionUID = -1299125117752916270L;

                @Override
                protected void createContents(final Composite parent) {
                    final HttpSession httpSession = RWT
                            .getUISession(parent.getDisplay())
                            .getHttpSession();

                    log.debug("Create new GUI entrypoint. HttpSession: " + httpSession);
                    if (httpSession == null) {
                        log.error("HttpSession not available from RWT.getUISession().getHttpSession()");
                        throw new IllegalStateException(
                                "HttpSession not available from RWT.getUISession().getHttpSession()");
                    }

                    final WebApplicationContext webApplicationContext = getWebApplicationContext(httpSession);
                    final ViewService viewService = webApplicationContext
                            .getBean(ViewService.class);

                    if (isAuthenticated(httpSession, webApplicationContext)) {
                        viewService.composeView(parent, ViewService.MAIN_PAGE);
                    } else {
                        viewService.composeView(parent, ViewService.LOGIN_PAGE);
                    }
                }
            };
        }

        private boolean isAuthenticated(
                final HttpSession httpSession,
                final WebApplicationContext webApplicationContext) {

            final AuthorizationContextHolder authorizationContextHolder = webApplicationContext
                    .getBean(AuthorizationContextHolder.class);
            final SEBServerAuthorizationContext authorizationContext = authorizationContextHolder
                    .getAuthorizationContext(httpSession);
            return authorizationContext.isValid() && authorizationContext.isLoggedIn();
        }

        private WebApplicationContext getWebApplicationContext(final HttpSession httpSession) {
            try {
                final ServletContext servletContext = httpSession.getServletContext();

                log.debug("Initialize Spring-Context on Servlet-Context: " + servletContext);

                final WebApplicationContext cc = WebApplicationContextUtils.getRequiredWebApplicationContext(
                        servletContext);

                if (cc == null) {
                    log.error("Failed to initialize Spring-Context on Servlet-Context: " + servletContext);
                    throw new RuntimeException(
                            "Failed to initialize Spring-Context on Servlet-Context: " + servletContext);
                }
                return cc;
            } catch (final Exception e) {
                log.error("Failed to initialize Spring-Context on HttpSession: " + httpSession);
                throw new RuntimeException("Failed to initialize Spring-Context on HttpSession: " + httpSession);
            }
        }

    };
}
