/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer.views;

import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.composer.ViewComposer;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class LoginView implements ViewComposer {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    private final AuthorizationContextHolder authorizationContextHolder;

    public LoginView(final AuthorizationContextHolder authorizationContextHolder) {
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return true;
    }

    @Override
    public void composeView(
            final ViewService viewService,
            final Composite parent,
            final Map<String, String> attributes) {

        // NOTE: if there is already an authenticated user within the current http-session
        //       we "redirect" the page-compose-call to the main page composer
        if (alreadyAuthenticated(parent)) {
            viewService.composeView(parent, ViewService.MAIN_PAGE);
            return;
        }

        viewService.centringView(parent);

        final Group group = new Group(parent, SWT.NONE);
        group.setLayout(new RowLayout(SWT.VERTICAL | SWT.CENTER));
        group.setLayoutData(new RowData(400, 200));
        group.setText(" Login");

        final Composite messageGroup = new Composite(group, SWT.BORDER);
        messageGroup.setLayoutData(new RowData(390, 50));
        messageGroup.setLayout(new RowLayout(SWT.VERTICAL));

        if (attributes.containsKey(AttributeKeys.AUTHORIZATION_FAILURE)) {
            final String message = attributes.get(AttributeKeys.AUTHORIZATION_FAILURE);
            final Label loginFailed1 = new Label(messageGroup, SWT.BOLD);
            loginFailed1.setText("Login Failed");
            final Label messageLabel = new Label(messageGroup, SWT.BOLD);
            messageLabel.setText(message);
            messageGroup.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            messageGroup.setBackgroundMode(SWT.INHERIT_FORCE);
        } else if (attributes.containsKey(AttributeKeys.LGOUT_SUCCESS)) {
            final Label logoutSuccess = new Label(messageGroup, SWT.BOLD);
            logoutSuccess.setText("You have been successfully logged out.");
            messageGroup.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            messageGroup.setBackgroundMode(SWT.INHERIT_FORCE);
        } else {
            messageGroup.setVisible(false);
        }

        final Composite space = new Composite(group, SWT.NONE);
        space.setLayoutData(new RowData(390, 10));

        final Composite form = new Composite(group, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form.setLayout(gridLayout);
        form.setLayoutData(new RowData(400, -1));

        final GridData formCellText = new GridData(100, 20);
        final GridData formCellData = new GridData(200, 20);
        final Label name = new Label(form, SWT.NONE);
        name.setData(RWT.CUSTOM_VARIANT, "p");
        name.setText("Name: ");
        name.setLayoutData(formCellText);
        final Text loginName = new Text(form, SWT.LEFT | SWT.BORDER);
        loginName.setLayoutData(formCellData);
        final Label pwd = new Label(form, SWT.NONE);
        pwd.setText("Password: ");
        pwd.setLayoutData(formCellText);
        final Text loginPassword = new Text(form, SWT.LEFT | SWT.PASSWORD | SWT.BORDER);
        loginPassword.setLayoutData(formCellData);

        final Button button = new Button(form, SWT.FLAT);
        button.setText("Login");
        final GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalIndent = 125;
        gridData.widthHint = 150;
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
                    viewService.composeView(parent, ViewService.MAIN_PAGE);
                } else {
                    loginError(viewService, parent, "Access Denied");
                }
            } catch (final Exception e) {
                log.error("Unexpected error while trying to login with user: {}", username, e);
                loginError(viewService, parent, "Unexpected Error. Please call an Administrator");
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
            final ViewService viewService,
            final Composite parent,
            final String message) {

        // just to be sure we leave a clean and proper authorizationContext
        try {
            this.authorizationContextHolder
                    .getAuthorizationContext()
                    .logout();
        } catch (final Exception e) {
            log.info("Cleanup logout failed: {}", e.getMessage());
        }
        viewService
                .createViewOn(parent)
                .attribute(AttributeKeys.AUTHORIZATION_FAILURE, message)
                .compose(ViewService.LOGIN_PAGE);
    }

    private boolean alreadyAuthenticated(final Composite parent) {
        final SEBServerAuthorizationContext authorizationContext = this.authorizationContextHolder
                .getAuthorizationContext();
        return authorizationContext.isValid() && authorizationContext.isLoggedIn();
    }

}
