/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.rap.rwt.widgets.DialogCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.page.event.PageEventListener;
import org.eth.demo.sebserver.gui.service.widgets.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class ComposerService {

    private static final Logger log = LoggerFactory.getLogger(ComposerService.class);

    private final I18nSupport i18nSupport;
    private final Map<String, TemplateComposer> composer;

    public ComposerService(
            final I18nSupport i18nSupport,
            final Collection<TemplateComposer> composer) {

        this.i18nSupport = i18nSupport;
        this.composer = composer.stream()
                .collect(Collectors.toMap(
                        comp -> comp.getName(),
                        Function.identity()));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root) {

        compose(composerType.getName(), new PageContext(this.i18nSupport, this, root, root, Collections.emptyMap()));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Map<String, String> attributes) {

        compose(composerType.getName(), new PageContext(this.i18nSupport, this, root, root, attributes));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final PageAttr... attributes) {

        final Map<String, String> attributesMap = new HashMap<>();
        if (attributes != null) {
            for (final PageAttr attr : attributes) {
                attributesMap.put(attr.name, attr.value);
            }
        }
        compose(composerType.getName(), new PageContext(this.i18nSupport, this, root, root, attributesMap));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Composite parent) {

        compose(composerType.getName(), new PageContext(this.i18nSupport, this, root, parent, Collections.emptyMap()));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Composite parent,
            final Map<String, String> attributes) {

        compose(composerType.getName(), new PageContext(this.i18nSupport, this, root, parent, attributes));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final PageContext context) {

        compose(composerType.getName(), context);
    }

    public final void compose(
            final String name,
            final PageContext ctx) {

        if (!this.composer.containsKey(name)) {
            log.error("No TemplateComposer with name: " + name + " found. Check Spring confiuration and beans");
            return;
        }

        final TemplateComposer composer = this.composer.get(name);

        if (composer.validateAttributes(ctx.attributes)) {
            clear(ctx.parent);
            composer.compose(ctx);
            ctx.parent.layout();
        } else {
            log.error(
                    "Invalid or missing mandatory attributes to handle compose request of ViewComposer: {} attributes: ",
                    name,
                    ctx.attributes);
        }
    }

    final void composePageContent(
            final String name,
            final PageContext ctx) {

        final PageComposer composer = (PageComposer) this.composer.get(name);

        if (composer.validateAttributes(ctx.attributes)) {
            clear(ctx.parent);
            composer.composePageContent(ctx);
            ctx.parent.layout();
        } else {
            log.error(
                    "Invalid or missing mandatory attributes to handle compose request of ViewComposer: {} attributes: ",
                    name,
                    ctx.attributes);
        }
    }

    private void clear(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

    public static final class PageAttr {

        public final String name;
        public final String value;

        public PageAttr(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }

    public static final class PageContext {

        private static final Comparator<PageEventListener<?>> LISTENER_COMPARATOR =
                new Comparator<PageEventListener<?>>() {
                    @Override
                    public int compare(final PageEventListener<?> o1, final PageEventListener<?> o2) {
                        final int x = o1.priority();
                        final int y = o2.priority();
                        return (x < y) ? -1 : ((x == y) ? 0 : 1);
                    }
                };

        private final I18nSupport i18nSupport;
        public final ComposerService composerService;
        public final Composite root;
        public final Composite parent;
        public final Map<String, String> attributes;

        private PageContext(
                final I18nSupport i18nSupport,
                final ComposerService composerService,
                final Composite root,
                final Composite parent,
                final Map<String, String> attributes) {

            this.i18nSupport = i18nSupport;
            this.composerService = composerService;
            this.root = root;
            this.parent = parent;
            this.attributes = Collections.unmodifiableMap(attributes);
        }

        public PageContext of(final Composite parent) {
            return new PageContext(this.i18nSupport, this.composerService, this.root, parent, this.attributes);
        }

        public PageContext of(final Composite parent, final Map<String, String> attributes) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.putAll(attributes);
            return new PageContext(this.i18nSupport, this.composerService, this.root, parent, attrs);
        }

        public PageContext of(final Map<String, String> attributes) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.putAll(attributes);
            return new PageContext(this.i18nSupport, this.composerService, this.root, this.parent, attrs);
        }

        public PageContext withAttr(final String key, final String value) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.put(key, value);
            return new PageContext(this.i18nSupport, this.composerService, this.root, this.parent, attrs);
        }

        @SuppressWarnings("unchecked")
        public <T> void notify(final T t) {
            final Class<?> typeClass = t.getClass();
            final List<PageEventListener<T>> listeners = new ArrayList<>();
            PageTreeTraversal.traversePageTree(
                    this.root,
                    c -> {
                        final PageEventListener<?> listener =
                                (PageEventListener<?>) c.getData(PageEventListener.LISTENER_ATTRIBUTE_KEY);
                        return listener != null && listener.match(typeClass);
                    },
                    c -> listeners.add(((PageEventListener<T>) c.getData(PageEventListener.LISTENER_ATTRIBUTE_KEY))));

            if (listeners.isEmpty()) {
                return;
            }

            listeners.stream()
                    .sorted(LISTENER_COMPARATOR)
                    .forEach(listener -> listener.notify(t));
        }

        @SuppressWarnings("serial")
        public void applyConfirmDialog(final String confirmMessage, final Runnable onOK) {
            final Message messageBox = new Message(
                    this.root.getShell(),
                    this.i18nSupport.getText("org.sebserver.dialog.confirm.title"),
                    this.i18nSupport.getText(confirmMessage),
                    SWT.OK | SWT.CANCEL);
            messageBox.open(new DialogCallback() {
                @Override
                public void dialogClosed(final int returnCode) {
                    if (returnCode == SWT.OK) {
                        try {
                            onOK.run();
                        } catch (final Throwable t) {
                            log.error(
                                    "Unexpected on confirm callback execution. This should not happen, plase secure the given onOK Runnable",
                                    t);
                        }
                    }
                }
            });
        }

//        public void applyValidationErrorDialog(final Collection<FieldValidationError> validationErrors) {
//            final Message messageBox = new Message(
//                    this.root.getShell(),
//                    this.i18nSupport.getText("org.sebserver.dialog.validationErrors.title"),
//                    this.i18nSupport.getText(confirmMessage),
//                    SWT.OK);
//        }

        public void notifyError(final String string, final Throwable t) {
            // TODO show a error pop-up to inform the user about unexpected error
        }

    }

}
