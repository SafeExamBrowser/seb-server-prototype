/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.form;

import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormPostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormHandle {

    private static final Logger log = LoggerFactory.getLogger(FormHandle.class);

    private final PageContext composerCtx;
    private final Form form;
    private final APICallBuilder<FormPostResponse> post;

    public FormHandle(
            final PageContext composerCtx,
            final Form form,
            final APICallBuilder<FormPostResponse> post) {

        this.composerCtx = composerCtx;
        this.form = form;
        this.post = post;
    }

    public void doAPIPost(final ActionDefinition action) {
        final FormPostResponse response = this.post
                .withFormBinding(this.form)
                .doAPICall()
                .onError(t -> {
                    log.error("Failed to post form data to SEBServer within Rest API: ", t);
                    // TODO show error popup
                    throw new RuntimeException(t);
                });

        if (response.validationErrors.isEmpty()) {
            this.composerCtx.notify(new ActionEvent(action, response.objectId));
        } else {
            // TODO handle validation errors
        }
    }

}
