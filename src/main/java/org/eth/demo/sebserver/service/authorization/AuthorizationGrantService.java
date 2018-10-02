/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.authorization;

import java.security.Principal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.eth.demo.sebserver.domain.rest.admin.Role;
import org.eth.demo.sebserver.domain.rest.admin.User;
import org.eth.demo.sebserver.service.admin.UserFacade;
import org.eth.demo.sebserver.service.authorization.RoleTypeGrant.RoleTypeKey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class AuthorizationGrantService {

    public enum GrantType implements Predicate<RoleTypeGrant> {
        READ_ONLY(grant -> grant.read),
        MODIFY(grant -> grant.modify),
        WRITE(grant -> grant.write);

        private final Predicate<RoleTypeGrant> typeCheck;

        private GrantType(final Predicate<RoleTypeGrant> typeCheck) {
            this.typeCheck = typeCheck;
        }

        @Override
        public boolean test(final RoleTypeGrant grant) {
            return this.typeCheck.test(grant);
        }
    }

    public enum GrantEntityType {
        INSTITUTION,
        SEB_LMS_SETUP,
        USER,
        EXAM,
        SEB_CONFIG
    }

    private final Map<RoleTypeGrant.RoleTypeKey, RoleTypeGrant> generalGrantRules = new HashMap<>();
    private final Map<GrantEntityType, AuthorizationGrantRule> exceptionalRules =
            new EnumMap<>(GrantEntityType.class);

    public AuthorizationGrantService(final ApplicationContext appContext) {
        //@formatter:off

        // Global privileges

        // r - read-only
        // m - modify -> save but no creation nor deletion
        // w - write  -> with creation and deletion
        // i - institutionOnly -> sees only entities of same institution
        // mo - modifyOnlyOwner -> like modify but only if the user is the owner
        // wo - writeOnlyOwner -> like write but only of the user is the owner

        //  r      m      w      i      mo     wo
        add(true,  true,  true,  false, false, false, GrantEntityType.INSTITUTION,Role.SEB_SERVER_ADMIN);
        add(true,  true,  false, true,  true,  false, GrantEntityType.INSTITUTION,Role.INSTITUTIONAL_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.INSTITUTION,Role.EXAM_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.INSTITUTION,Role.EXAM_SUPPORTER);

        //  r      m      w      i      mo     wo
        add(true,  false, false, false, false, false, GrantEntityType.SEB_LMS_SETUP,Role.SEB_SERVER_ADMIN);
        add(true,  true,  true,  true,  false, false, GrantEntityType.SEB_LMS_SETUP,Role.INSTITUTIONAL_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.SEB_LMS_SETUP,Role.EXAM_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.SEB_LMS_SETUP,Role.EXAM_SUPPORTER);

        //  r      m      w      i      mo     wo
        add(true,  false, false, false, false, false, GrantEntityType.EXAM,Role.SEB_SERVER_ADMIN);
        add(true,  true,  true,  true,  false, false, GrantEntityType.EXAM,Role.INSTITUTIONAL_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.EXAM,Role.EXAM_ADMIN);
        add(true,  false, false, true,  false, false, GrantEntityType.EXAM,Role.EXAM_SUPPORTER);

        //@formatter:on

        final Map<String, AuthorizationGrantRule> exceptions = appContext.getBeansOfType(AuthorizationGrantRule.class);
        if (exceptions != null) {
            exceptions.values().stream()
                    .forEach(r -> this.exceptionalRules.put(r.type(), r));
        }
    }

    public boolean hasTypeGrant(final GrantEntityType entityType, final GrantType type, final Principal principal) {
        final User user = UserFacade.extractFromPrincipal(principal);
        for (final Role role : user.getRoles()) {
            final RoleTypeGrant roleTypeGrant = this.generalGrantRules.get(new RoleTypeKey(entityType, role));
            if (roleTypeGrant != null && type.test(roleTypeGrant)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasGrant(final GrantEntity entity, final GrantType type, final Principal principal) {
        return hasGrant(entity, type, UserFacade.extractFromPrincipal(principal));
    }

    public boolean hasGrant(final GrantEntity entity, final GrantType type, final User user) {
        final AuthorizationGrantRule authorizationGrantRule = getGrantRule(entity.grantEntityType());
        if (authorizationGrantRule == null) {
            return false;
        }

        switch (type) {
            case READ_ONLY:
                return authorizationGrantRule.hasReadGrant(entity, user);
            case MODIFY:
                return authorizationGrantRule.hasModifyGrant(entity, user);
            case WRITE:
                return authorizationGrantRule.hasWriteGrant(entity, user);
            default:
                return false;
        }
    }

    public <T extends GrantEntity> Predicate<T> getGrantFilter(
            final GrantEntityType entityType,
            final GrantType type,
            final Principal principal) {

        return getGrantFilter(entityType, type, UserFacade.extractFromPrincipal(principal));
    }

    public <T extends GrantEntity> Predicate<T> getGrantFilter(
            final GrantEntityType entityType,
            final GrantType type,
            final User user) {

        final AuthorizationGrantRule authorizationGrantRule = getGrantRule(entityType);
        if (authorizationGrantRule == null)
            return t -> false;

        switch (type) {
            case READ_ONLY:
                return t -> authorizationGrantRule.hasReadGrant(t, user);
            case MODIFY:
                return t -> authorizationGrantRule.hasModifyGrant(t, user);
            case WRITE:
                return t -> authorizationGrantRule.hasWriteGrant(t, user);
            default:
                return t -> false;
        }
    }

    private AuthorizationGrantRule getGrantRule(final GrantEntityType type) {
        return this.exceptionalRules.computeIfAbsent(type, entityType -> new GeneralGrantRule(entityType));
    }

    private void add(
            final boolean read,
            final boolean modify,
            final boolean write,
            final boolean institutionOnly,
            final boolean modifyOwnerOnly,
            final boolean writeOwnerOnly,
            final GrantEntityType type,
            final Role role) {

        final RoleTypeGrant roleTypeGrant =
                new RoleTypeGrant(read, modify, write, institutionOnly, modifyOwnerOnly, writeOwnerOnly, type, role);
        this.generalGrantRules.put(roleTypeGrant.roleTypeKey, roleTypeGrant);
    }

    private final class GeneralGrantRule implements AuthorizationGrantRule {

        private final GrantEntityType type;
        private final Map<Role, RoleTypeGrant> grants;

        public GeneralGrantRule(final GrantEntityType type) {
            this.type = type;
            this.grants = new EnumMap<>(Role.class);
            for (final Role role : Role.values()) {
                this.grants.put(role,
                        AuthorizationGrantService.this.generalGrantRules.get(new RoleTypeKey(type, role)));
            }
        }

        @Override
        public GrantEntityType type() {
            return this.type;
        }

        @Override
        public boolean hasReadGrant(final GrantEntity entity, final User user) {
            for (final Role role : user.roles) {
                final RoleTypeGrant roleTypeGrant = this.grants.get(role);
                if (roleTypeGrant.read) {
                    if (roleTypeGrant.institutionOnly
                            && user.institutionId.longValue() == entity.getInstitutionId().longValue()) {
                        return true;
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean hasModifyGrant(final GrantEntity entity, final User user) {
            for (final Role role : user.roles) {
                final RoleTypeGrant roleTypeGrant = this.grants.get(role);
                if (roleTypeGrant.modify) {
                    if (roleTypeGrant.institutionOnly
                            && user.institutionId.longValue() == entity.getInstitutionId().longValue()) {
                        return true;
                    } else {
                        return true;
                    }
                }
                if (roleTypeGrant.modifyOwnerOnly && user.id.longValue() == entity.getOwnerId()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean hasWriteGrant(final GrantEntity entity, final User user) {
            for (final Role role : user.roles) {
                final RoleTypeGrant roleTypeGrant = this.grants.get(role);
                if (roleTypeGrant.write) {
                    if (roleTypeGrant.institutionOnly
                            && user.institutionId.longValue() == entity.getInstitutionId().longValue()) {
                        return true;
                    } else {
                        return true;
                    }
                }
                if (roleTypeGrant.writeOwnerOnly && user.id.longValue() == entity.getOwnerId()) {
                    return true;
                }
            }
            return false;
        }
    }
}
