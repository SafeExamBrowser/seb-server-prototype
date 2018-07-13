/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.views.ExamOverview;
import org.eth.demo.sebserver.gui.views.SEBConfigView1;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RAPConfiguration implements ApplicationConfiguration {

    @Override
    public void configure(final Application application) {
        //  application.addEntryPoint("/login", of(LoginView.class), null);
        application.addEntryPoint("/examview", of(ExamOverview.class), null);
        application.addEntryPoint("/sebconfig", of(SEBConfigView1.class), null);

        // NOTE: add more views here

    }

    // RAP - Spring - Integration below
    private static final SpringAwareEntryPointFactory of(final Class<? extends ViewComposer> startPageComposer) {
        return new SpringAwareEntryPointFactory(startPageComposer);
    }

    private static final class SpringAwareEntryPointFactory implements EntryPointFactory {

        private final Class<? extends ViewComposer> startPageComposer;

        public SpringAwareEntryPointFactory(final Class<? extends ViewComposer> startPageComposer) {
            this.startPageComposer = startPageComposer;
        }

        @Override
        public EntryPoint create() {
            final WebApplicationContext cc = WebApplicationContextUtils.getRequiredWebApplicationContext(
                    RWT.getUISession().getHttpSession().getServletContext());

            final ViewService viewService = cc.getBean(ViewService.class);
            if (viewService == null) {
                throw new RuntimeException("Failed to initialize Spring-Context for EntryPoint");
            }

            return new AbstractEntryPoint() {

                private static final long serialVersionUID = -1299125117752916270L;

                @Override
                protected void createContents(final Composite parent) {
                    viewService.composeView(parent,
                            SpringAwareEntryPointFactory.this.startPageComposer);
                }
            };
        }
    }

}
