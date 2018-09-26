/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.security.Principal;
import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.service.admin.InstitutionDao;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institution")
public class InstitutionController {

    private final InstitutionDao institutionDao;
    private final AuthorizationGrantService authorizationGrantService;

    public InstitutionController(
            final InstitutionDao institutionDao,
            final AuthorizationGrantService authorizationGrantService) {

        this.institutionDao = institutionDao;
        this.authorizationGrantService = authorizationGrantService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public final Collection<Institution> exams(final Principal principal) {
        return this.institutionDao.all(
                this.authorizationGrantService.getReadGrantFilter(Institution.class, principal));
    }

}
