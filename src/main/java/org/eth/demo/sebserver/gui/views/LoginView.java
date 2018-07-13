/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.service.ViewComposer;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.LoginRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class LoginView implements ViewComposer {

    private final LoginRequest loginRequest;

    private final Map<String, String> attrs = new HashMap<>();

    public LoginView(final LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    @Override
    public boolean validateAttributes(final Map<String, String> attributes) {
        return true;
    }

    @Override
    public void composeView(final ViewService viewService, final Composite parent,
            final Map<String, String> attributes) {
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
            System.out.println("login name: " + loginName.getText() + " password: " + loginPassword.getText());
            this.attrs.clear();
            this.attrs.put(AttributeKeys.USER_NAME, loginName.getText());
            this.attrs.put(AttributeKeys.PASSWORD, loginPassword.getText());
            final String sessionId = this.loginRequest.doAPICall(this.attrs);

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("********************** authentication_: " + authentication);

//            final HttpServletResponse response = RWT.getResponse();
//            response.setStatus(HttpServletResponse.SC_OK);
//            try {
//                response.getWriter()
//                        .write("{\"head\": {\"redirect\": \"http://localhost:8080/examview\", \"Set-Cookie\": \""
//                                + sessionId + "\"}}");
//                response.getWriter().flush();
//                response.getWriter().close();
//            } catch (final IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        });
    }

}
