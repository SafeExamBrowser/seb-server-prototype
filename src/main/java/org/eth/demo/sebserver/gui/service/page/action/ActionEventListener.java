/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.swt.widgets.Widget;
import org.eth.demo.sebserver.gui.service.page.event.PageEventListener;

public abstract class ActionEventListener implements PageEventListener<ActionEvent> {

    @Override
    public boolean match(final Class<?> type) {
        return type == ActionEvent.class;
    }

    public static final ActionEventListener of(final Consumer<ActionEvent> eventConsumer) {
        return new ActionEventListener() {
            @Override
            public void notify(final ActionEvent event) {
                eventConsumer.accept(event);
            }
        };
    }

    public static final ActionEventListener of(
            final Predicate<ActionEvent> predicate,
            final Consumer<ActionEvent> eventConsumer) {

        return new ActionEventListener() {
            @Override
            public void notify(final ActionEvent event) {
                if (predicate.test(event)) {
                    eventConsumer.accept(event);
                }
            }
        };
    }

    public static final ActionEventListener of(
            final ActionDefinition actionDefinition,
            final Consumer<ActionEvent> eventConsumer) {

        return new ActionEventListener() {
            @Override
            public void notify(final ActionEvent event) {
                if (event.actionDefinition == actionDefinition) {
                    eventConsumer.accept(event);
                }
            }
        };
    }

    public static final void injectListener(
            final Widget widget,
            final ActionDefinition actionDefinition,
            final Consumer<ActionEvent> eventConsumer) {

        widget.setData(
                PageEventListener.LISTENER_ATTRIBUTE_KEY,
                of(actionDefinition, eventConsumer));
    }

}
