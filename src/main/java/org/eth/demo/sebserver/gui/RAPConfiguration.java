/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.page.ComposerService;
import org.eth.demo.sebserver.gui.service.page.LoginPage;
import org.eth.demo.sebserver.gui.service.page.MainPage;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.eth.demo.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RAPConfiguration implements ApplicationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RAPConfiguration.class);

    @Override
    public void configure(final Application application) {
        final Map<String, String> properties = new HashMap<>();
        properties.put(WebClient.PAGE_TITLE, "SEB Server");
        properties.put(WebClient.BODY_HTML, "<big>Loading Application<big>");
//        properties.put(WebClient.FAVICON, "icons/favicon.png");
//        properties.put(WebClient.THEME_ID, "MyCustomTheme");
        application.addEntryPoint("/gui", RAPSpringEntryPointFactory, properties);

        try {
            // TODO get file path from properties
            application.addStyleSheet(RWT.DEFAULT_THEME_ID, "static/css/sebserver.css");
        } catch (final Exception e) {
            log.error("Error during CSS parsing. Please check the custom CSS files for errors.", e);
        }
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

                    final ComposerService composerService = webApplicationContext
                            .getBean(ComposerService.class);

                    if (isAuthenticated(httpSession, webApplicationContext)) {
                        composerService.compose(MainPage.class, parent);
                    } else {
                        composerService.compose(LoginPage.class, parent);
                    }
                }
            };
        }

        private boolean isAuthenticated(
                final HttpSession httpSession,
                final WebApplicationContext webApplicationContext) {

            // NOTE: if the user comes from a specified institutional login url (redirect from server) the institutionId is get from
            //       request and put to the session attributes. The institutionId can later be used for institution specific login page
            //       look and feel as well as for sending the institutionId within the login credentials to give the authorization service
            //       some restriction to search the user. This is especially useful if the user is external registered and verified
            //       with LDAP or AAI SAML
            final StartupParameters reqParams = RWT.getClient().getService(StartupParameters.class);
            final String institutionId = reqParams.getParameter(Const.INSTITUTION_ID);
            if (StringUtils.isNotBlank(institutionId)) {
                httpSession.setAttribute(Const.INSTITUTION_ID, institutionId);
            } else {
                httpSession.removeAttribute(Const.INSTITUTION_ID);
            }

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
