/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import org.eth.demo.sebserver.service.dao.UserDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBServerUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    public SEBServerUserDetailService(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final org.eth.demo.sebserver.domain.rest.admin.User byUserName = this.userDao.byUserName(username);
        if (byUserName == null) {
            throw new UsernameNotFoundException("No User with name: " + username + " found");
        }
        return byUserName;
    }

}
