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
import java.util.HashMap;
import java.util.Map;

import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.service.admin.InstitutionDao;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantType;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public final Map<Long, String> info(final Principal principal) {
        return this.institutionDao.all(
                this.authorizationGrantService.getGrantFilter(
                        GrantEntityType.INSTITUTION,
                        GrantType.READ_ONLY,
                        principal))
                .stream()
                .reduce(new HashMap<Long, String>(),
                        (acc, inst) -> {
                            acc.put(inst.id, inst.name);
                            return acc;
                        },
                        (acc1, acc2) -> {
                            acc1.putAll(acc2);
                            return acc1;
                        });
    }

    @RequestMapping(method = RequestMethod.GET)
    public final Collection<Institution> all(final Principal principal) {
        return this.institutionDao.all(
                this.authorizationGrantService.getGrantFilter(
                        GrantEntityType.INSTITUTION,
                        GrantType.READ_ONLY,
                        principal));
    }

    @RequestMapping(value = "/{instId}", method = RequestMethod.GET)
    public final Institution institution(
            @PathVariable(required = true) final Long instId,
            final Principal principal) {

        return this.institutionDao.byId(instId);
    }

//    @RequestMapping(value = "/self", method = RequestMethod.GET)
//    public final Institution usersInstitution(final Principal principal) {
//        return this.institutionDao.all(
//                this.authorizationGrantService.getReadGrantFilter(Institution.class, principal));
//    }

}
