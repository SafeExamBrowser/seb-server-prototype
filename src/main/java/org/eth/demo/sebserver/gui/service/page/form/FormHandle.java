/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.form;

import java.util.function.Consumer;

import org.eth.demo.sebserver.gui.domain.validation.FieldValidationError;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.i18n.LocTextKey;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;
import org.eth.demo.sebserver.gui.service.page.form.Form.FormFieldAccessor;
import org.eth.demo.sebserver.gui.service.rest.SEBServerAPICall.APICallBuilder;
import org.eth.demo.sebserver.gui.service.rest.formpost.FormPostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormHandle<T> {

    private static final Logger log = LoggerFactory.getLogger(FormHandle.class);

    public static final String FIELD_VALIDATION_LOCTEXT_PREFIX = "org.sebserver.form.validation.fieldError.";

    private final PageContext composerCtx;
    private final Form form;
    private final APICallBuilder<FormPostResponse<T>> post;
    private final I18nSupport i18nSupport;

    FormHandle(
            final PageContext composerCtx,
            final Form form,
            final APICallBuilder<FormPostResponse<T>> post,
            final I18nSupport i18nSupport) {

        this.composerCtx = composerCtx;
        this.form = form;
        this.post = post;
        this.i18nSupport = i18nSupport;
    }

    public void doAPIPost(final ActionDefinition action) {
        this.form.process(
                name -> true,
                fieldAccessor -> fieldAccessor.resetError());

        final FormPostResponse<?> response = this.post
                .withFormBinding(this.form)
                .doAPICall()
                .onError(t -> {
                    log.error("Failed to post form data to SEBServer within Rest API: ", t);
                    // TODO show error popup
                    throw new RuntimeException(t);
                });

        if (response.validationErrors.isEmpty()) {
            this.composerCtx.notify(new ActionEvent(action, response.response));
        } else {
            log.error("Validation Errors: {}", response.validationErrors);
            for (final FieldValidationError valError : response.validationErrors) {
                this.form.process(
                        name -> name.equals(valError.fieldName),
                        fieldAccessor -> showValidationError(fieldAccessor, valError));
            }

        }
    }

    private final void showValidationError(
            final FormFieldAccessor<?> fieldAccessor,
            final FieldValidationError valError) {

        fieldAccessor.setError(this.i18nSupport.getText(new LocTextKey(
                FIELD_VALIDATION_LOCTEXT_PREFIX + valError.errorType,
                (Object[]) valError.attributes)));
    }

    public FormHandle<T> process(final Consumer<Form> consumer) {
        consumer.accept(this.form);
        return this;
    }

}
