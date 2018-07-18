/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.RAPConfiguration;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class LoginView implements ViewComposer {

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
        //       we "redirect" the page-compose-call to the main page composer here
        if (alreadyAuthenticated(parent)) {
            viewService.composeView(parent, RAPConfiguration.MAIN_PAGE_COMPOSER_CLASS);
            return;
        }

        viewService.centringView(parent);

        final Group group = new Group(parent, SWT.SHADOW_NONE);
        group.setBounds(20, 20, 500, 100);
        group.setText(" Login");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);

        final Label name = new Label(group, SWT.NONE);
        name.setText("Name: ");
        final Text loginName = new Text(group, SWT.LEFT | SWT.BORDER);
        loginName.setLayoutData(new GridData(100, 10));
        final Label pwd = new Label(group, SWT.NONE);
        pwd.setText("Password: ");
        final Text loginPassword = new Text(group, SWT.LEFT | SWT.PASSWORD | SWT.BORDER);
        loginPassword.setLayoutData(new GridData(200, 10));

        final Button button = new Button(group, SWT.FLAT);
        button.setText("Login");
        button.addListener(SWT.Selection, event -> {
            try {
                final SEBServerAuthorizationContext authorizationContext = this.authorizationContextHolder
                        .getAuthorizationContext(RWT.getUISession().getHttpSession());
                final boolean loggedIn = authorizationContext.login(loginName.getText(), loginPassword.getText());

//                final String response = this.loginRequest
//                        .with()
//                        .username(loginName.getText())
//                        .password(loginPassword.getText())
//                        .doAPICall();
//
//                System.out.println("*********************** accessToken: " + response);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean alreadyAuthenticated(final Composite parent) {
        final HttpSession httpSession = RWT.getUISession(parent.getDisplay()).getHttpSession();
        final String authentication = (String) httpSession.getAttribute(AttributeKeys.AUTHORIZATION_HEADER);
        return (authentication != null && validAuthentication(authentication));
    }

    private boolean validAuthentication(final String authentication) {
        // TODO later we have also to check the token if it is a valid one and if it is still not expired
        return true;
    }

}
