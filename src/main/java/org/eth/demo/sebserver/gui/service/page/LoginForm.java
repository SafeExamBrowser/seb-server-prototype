/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.page.ComposerService.ComposerServiceContext;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageAttr;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.eth.demo.sebserver.gui.service.widgets.Message;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginForm implements TemplateComposer {

    private static final Logger log = LoggerFactory.getLogger(LoginForm.class);

    private final AuthorizationContextHolder authorizationContextHolder;
    private final WidgetFactory widgetFactory;

    public LoginForm(
            final AuthorizationContextHolder authorizationContextHolder,
            final WidgetFactory widgetFactory) {

        this.authorizationContextHolder = authorizationContextHolder;
        this.widgetFactory = widgetFactory;
    }

    @Override
    public void compose(final ComposerServiceContext composerCtx) {
        final Composite parent = composerCtx.parent;
        final Map<String, String> attributes = composerCtx.attributes;

        if (attributes.containsKey(AttributeKeys.LGOUT_SUCCESS)) {
            final MessageBox logoutSuccess = new Message(
                    composerCtx.root.getShell(),
                    this.widgetFactory.i18nSupport.getText("org.sebserver.logout"),
                    this.widgetFactory.i18nSupport.getText("org.sebserver.logout.success.message"),
                    SWT.ICON_INFORMATION);
            logoutSuccess.open(null);
        }

        final Composite loginGroup = new Composite(parent, SWT.NONE);
        final GridLayout rowLayout = new GridLayout();
        rowLayout.marginWidth = 20;
        rowLayout.marginRight = 100;
        loginGroup.setLayout(rowLayout);
        loginGroup.setData(RWT.CUSTOM_VARIANT, "login");

        final Label name = this.widgetFactory.labelLocalized(loginGroup, "org.sebserver.login.username");
        name.setLayoutData(new GridData(300, -1));
        name.setAlignment(SWT.BOTTOM);
        final Text loginName = new Text(loginGroup, SWT.LEFT | SWT.BORDER);
        loginName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
        gridData.verticalIndent = 10;
        final Label pwd = this.widgetFactory.labelLocalized(loginGroup, "org.sebserver.login.pwd");
        pwd.setLayoutData(gridData);
        final Text loginPassword = new Text(loginGroup, SWT.LEFT | SWT.PASSWORD | SWT.BORDER);
        loginPassword.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

        final Button button = this.widgetFactory.buttonLocalized(loginGroup, "org.sebserver.login.login");
        gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
        gridData.verticalIndent = 10;
        button.setLayoutData(gridData);

        final SEBServerAuthorizationContext authorizationContext = this.authorizationContextHolder
                .getAuthorizationContext(RWT.getUISession().getHttpSession());

        button.addListener(SWT.Selection, event -> {
            final String username = loginName.getText();
            try {

                final boolean loggedIn = authorizationContext.login(
                        username,
                        loginPassword.getText());

                if (loggedIn) {
                    // view main page (TODO: or users-entry-page?)
                    composerCtx.composerService.composePage(
                            MainPage.class,
                            composerCtx.root,
                            composerCtx.attributes);
                } else {
                    loginError(composerCtx, "org.sebserver.login.failed.message");
                }
            } catch (final Exception e) {
                log.error("Unexpected error while trying to login with user: {}", username, e);
                loginError(composerCtx, "Unexpected Error. Please call an Administrator");
            }
        });
        loginName.addListener(SWT.KeyDown, event -> {
            if (event.character == '\n' || event.character == '\r') {
                loginPassword.setFocus();
            }
        });
        loginPassword.addListener(SWT.KeyDown, event -> {
            if (event.character == '\n' || event.character == '\r') {
                button.setFocus();
            }
        });
    }

    private void loginError(
            final ComposerServiceContext composerCtx,
            final String message) {

        final MessageBox error = new Message(
                composerCtx.root.getShell(),
                this.widgetFactory.i18nSupport.getText("org.sebserver.login.failed.title"),
                this.widgetFactory.i18nSupport.getText(message, message),
                SWT.ERROR);
        error.open(null);

        // just to be sure we leave a clean and proper authorizationContext
        try {
            this.authorizationContextHolder
                    .getAuthorizationContext()
                    .logout();
        } catch (final Exception e) {
            log.info("Cleanup logout failed: {}", e.getMessage());
        }

        // redirect to login page
        composerCtx.composerService.composePage(
                LoginPage.class,
                composerCtx.root,
                new PageAttr(AttributeKeys.AUTHORIZATION_FAILURE, message));
    }

}
