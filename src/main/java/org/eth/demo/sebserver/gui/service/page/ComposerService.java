/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eth.demo.sebserver.gui.service.page.event.PageComponentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class ComposerService {

    private static final Logger log = LoggerFactory.getLogger(ComposerService.class);

    private final Map<String, TemplateComposer> composer;

    public ComposerService(final Collection<TemplateComposer> composer) {
        this.composer = composer.stream()
                .collect(Collectors.toMap(
                        comp -> comp.getName(),
                        Function.identity()));
    }

    public final void composePage(
            final Class<? extends TemplateComposer> composerType,
            final Composite root) {

        compose(composerType.getName(), new ComposerServiceContext(this, root, root, Collections.emptyMap()));
    }

    public final void composePage(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Map<String, String> attributes) {

        compose(composerType.getName(), new ComposerServiceContext(this, root, root, attributes));
    }

    public final void composePage(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final PageAttr... attributes) {

        final Map<String, String> attributesMap = new HashMap<>();
        if (attributes != null) {
            for (final PageAttr attr : attributes) {
                attributesMap.put(attr.name, attr.value);
            }
        }
        compose(composerType.getName(), new ComposerServiceContext(this, root, root, attributesMap));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Composite parent) {

        compose(composerType.getName(), new ComposerServiceContext(this, root, parent, Collections.emptyMap()));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final Composite root,
            final Composite parent,
            final Map<String, String> attributes) {

        compose(composerType.getName(), new ComposerServiceContext(this, root, parent, attributes));
    }

    public final void compose(
            final Class<? extends TemplateComposer> composerType,
            final ComposerServiceContext context) {

        compose(composerType.getName(), context);
    }

    public final void compose(
            final String name,
            final ComposerServiceContext ctx) {

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

    public static final class ComposerServiceContext {

        public final ComposerService composerService;
        public final Composite root;
        public final Composite parent;
        public final Map<String, String> attributes;

        private ComposerServiceContext(
                final ComposerService composerService,
                final Composite root,
                final Composite parent,
                final Map<String, String> attributes) {

            this.composerService = composerService;
            this.root = root;
            this.parent = parent;
            this.attributes = Collections.unmodifiableMap(attributes);
        }

        public ComposerServiceContext of(final Composite parent) {
            return new ComposerServiceContext(this.composerService, this.root, parent, this.attributes);
        }

        public ComposerServiceContext of(final Composite parent, final Map<String, String> attributes) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.putAll(attributes);
            return new ComposerServiceContext(this.composerService, this.root, parent, attrs);
        }

        public ComposerServiceContext of(final Map<String, String> attributes) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.putAll(attributes);
            return new ComposerServiceContext(this.composerService, this.root, this.parent, attrs);
        }

        public ComposerServiceContext withAttr(final String key, final String value) {
            final Map<String, String> attrs = new HashMap<>();
            attrs.putAll(this.attributes);
            attrs.put(key, value);
            return new ComposerServiceContext(this.composerService, this.root, this.parent, attrs);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public <T> void notify(final T t) {
            final Class<?> typeClass = t.getClass();
            PageTreeTraversal.traversePageTree(
                    this.root,
                    c -> c instanceof PageComponentListener && ((PageComponentListener) c).type() == typeClass,
                    c -> ((PageComponentListener<T>) c).notify(t));
        }

    }

}
