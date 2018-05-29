/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.eclipse.rap.rwt.engine.RWTServlet;
import org.eclipse.rap.rwt.engine.RWTServletContextListener;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RAPSpringConfig {

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(final ServletContext servletContext) throws ServletException {
                servletContext.setInitParameter(
                        "org.eclipse.rap.applicationConfiguration",
                        RAPConfiguration.class.getName());
            }
        };
    }

    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> listenerRegistrationBean() {
        final ServletListenerRegistrationBean<ServletContextListener> bean =
                new ServletListenerRegistrationBean<>();
        bean.setListener(new RWTServletContextListener());
        return bean;
    }

    @Bean
    public ServletRegistrationBean<RWTServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>(new RWTServlet(), "/examview");
    }

    @Bean
    public RAPSpringContext rapSpringContext() {
        return new RAPSpringContext();
    }

    // NOTE: This is not best practice but I have found no other way to get an ApplicationContext
    //       In a RAP EntryPoint so far. There may be a better way to integrate Spring dependency
    //       injection into a RAP/SWT context but the information base on the Internet is verly low on
    //       this suspect.
    public static class RAPSpringContext implements ApplicationContextAware {

        private static ApplicationContext context;

        RAPSpringContext() {
        }

        @Override
        public void setApplicationContext(final ApplicationContext context) throws BeansException {
            RAPSpringContext.context = context;
        }

        public static ApplicationContext getApplicationContext() {
            return context;

        }
    }

}