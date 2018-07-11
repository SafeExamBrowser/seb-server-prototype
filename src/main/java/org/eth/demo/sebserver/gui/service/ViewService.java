/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    private static final Logger log = LoggerFactory.getLogger(ViewService.class);

    private Map<String, ViewComposer> viewComposer;

    @Autowired
    public void init(final Collection<ViewComposer> viewComposer) {
        this.viewComposer = viewComposer.stream()
                .collect(Collectors.toMap(
                        composer -> composer.getName(),
                        Function.identity()));
    }

    public void composeView(final Composite parent, final Class<? extends ViewComposer> composerType) {
        composeView(parent, composerType.getName(), Collections.emptyMap());
    }

    public void composeView(final Composite parent, final String composerName) {
        composeView(parent, composerName, Collections.emptyMap());
    }

    public void composeView(
            final Composite parent,
            final Class<? extends ViewComposer> composerType,
            final Map<String, String> attributes) {

        composeView(parent, composerType.getName(), attributes);
    }

    public void composeView(
            final Composite parent,
            final String composerName,
            final Map<String, String> attributes) {

        if (!this.viewComposer.containsKey(composerName)) {
            throw new IllegalArgumentException(
                    "No ViewComposer with name: " + composerName + " found. Check Spring confiuration and beans");
        }

        final ViewComposer composer = this.viewComposer.get(composerName);

        if (composer.validateAttributes(attributes)) {
            clearView(parent);
            composer.composeView(this, parent, attributes);
            parent.layout();
        } else {
            log.error(
                    "Invalid or missing mandatory attributes to handle compose request of ViewComposer: {} attributes: ",
                    composerName,
                    attributes);
        }
    }

    public void centringView(final Composite parent) {
        final RowLayout parentLayout = new RowLayout();
        parentLayout.wrap = false;
        parentLayout.pack = false;
        parentLayout.justify = true;
        parentLayout.type = SWT.HORIZONTAL;
        parentLayout.center = true;
        parent.setLayout(parentLayout);
    }

    private void clearView(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

}
