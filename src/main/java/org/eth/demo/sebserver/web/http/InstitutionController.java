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
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.eth.demo.sebserver.domain.rest.admin.Institution;
import org.eth.demo.sebserver.service.admin.InstitutionDao;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantEntityType;
import org.eth.demo.sebserver.service.authorization.AuthorizationGrantService.GrantType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @RequestMapping(value = "/names", method = RequestMethod.GET)
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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public final Collection<Institution> all(final Principal principal) {
        return this.institutionDao.all(
                this.authorizationGrantService.getGrantFilter(
                        GrantEntityType.INSTITUTION,
                        GrantType.READ_ONLY,
                        principal));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public final Institution createNewInstitution(
            @RequestHeader(value = "institutionName", required = false) final String name,
            final Principal principal) {

        String _name = name;
        System.out.println("name = " + name);
        if (StringUtils.isBlank(_name)) {
            _name = "New Institution (" + UUID.randomUUID() + ")";
        }
        return this.institutionDao.createNew(_name);
    }

    @RequestMapping(value = "/get/{instId}", method = RequestMethod.GET)
    public final Institution getInstitution(
            @PathVariable(required = true) final Long instId,
            final Principal principal) {

        return this.institutionDao.byId(instId);
    }

    @RequestMapping(value = "/delete/{instId}", method = RequestMethod.POST)
    public final ResponseEntity<Object> deleteInstitution(
            @PathVariable(required = true) final Long instId,
            final Principal principal) {

        if (this.institutionDao.delete(instId)) {
            return ResponseEntity.ok(String.valueOf(instId));
        } else {
            // TODO deactivate
            return ResponseEntity.ok(String.valueOf(instId));
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public final ResponseEntity<Object> save(
            @Valid @RequestBody final Institution institution,
            final Principal principal) {

        if (!this.authorizationGrantService.hasGrant(institution, GrantType.MODIFY, principal)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // TODO some additional validation!?

        this.institutionDao.save(institution);
        return ResponseEntity.ok(institution);
    }

}
