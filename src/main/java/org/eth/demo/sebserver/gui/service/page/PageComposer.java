/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;

public abstract class PageComposer implements TemplateComposer {

    public static final String ATTR_PAGE_SKELETON_COMPOSER_NAME = "PAGE_SKELETON_COMPOSER_NAME";

    private final String pageContentComposerName;

    public PageComposer(final String pageContentComposerName) {
        this.pageContentComposerName = pageContentComposerName;
    }

    @Override
    public void compose(final PageContext composerCtx) {
        final String skeletonComposer = composerCtx.attributes.getOrDefault(
                ATTR_PAGE_SKELETON_COMPOSER_NAME,
                DefaultPageSkeleton.class.getName());

        composerCtx.composerService.compose(
                skeletonComposer,
                composerCtx.withAttr(
                        DefaultPageSkeleton.ATTR_CONTENT_COMPOSER_NAME,
                        this.pageContentComposerName));
    }

    protected abstract void composePageContent(final PageContext composerCtx);

}
