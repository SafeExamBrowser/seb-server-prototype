/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

import org.eth.demo.sebserver.gui.domain.IdAndName;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.rest.RestServices;
import org.eth.demo.sebserver.gui.service.rest.institution.DeleteInstitution;
import org.eth.demo.sebserver.gui.service.rest.institution.NewInstitution;

public interface InstitutionActions {

    /** Use this higher-order function to create a new Institution action Runnable.
     *
     * @return */
    static Runnable newInstitution(final PageContext composerCtx, final RestServices restServices) {
        return () -> {
            final IdAndName newInstitutionId = restServices
                    .sebServerAPICall(NewInstitution.class)
                    .doAPICall()
                    .onErrorThrow("Unexpected Error");
            composerCtx.notify(new ActionEvent(ActionDefinition.INSTITUTION_NEW, newInstitutionId));
        };
    }

    /** Use this higher-order function to create a delete Institution action Runnable.
     *
     * @return */
    static Runnable deleteInstitution(final PageContext composerCtx, final RestServices restServices,
            final String instId) {
        return () -> {
            restServices
                    .sebServerAPICall(DeleteInstitution.class)
                    .attribute(AttributeKeys.INSTITUTION_ID, instId)
                    .doAPICall()
                    .onErrorThrow("Unexpected Error");
            composerCtx.notify(new ActionEvent(ActionDefinition.INSTITUTION_DELETE, instId));
        };
    }

}
