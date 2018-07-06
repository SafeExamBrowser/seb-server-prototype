/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.util.TypedMap;
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
        composeView(parent, composerType.getName(), TypedMap.EMPTY_MAP);
    }

    public void composeView(final Composite parent, final String composerName) {
        composeView(parent, composerName, TypedMap.EMPTY_MAP);
    }

    public void composeView(
            final Composite parent,
            final Class<? extends ViewComposer> composerType,
            final TypedMap attributes) {

        composeView(parent, composerType.getName(), attributes);
    }

    public void composeView(
            final Composite parent,
            final String composerName,
            final TypedMap attributes) {

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

    private void clearView(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

}
