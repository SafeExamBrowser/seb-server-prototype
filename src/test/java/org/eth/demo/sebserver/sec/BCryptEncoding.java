/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.sec;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptEncoding {

    @Test
    public void encrypt() {
        final BCryptPasswordEncoder clientEncoder = new BCryptPasswordEncoder(4);
        final BCryptPasswordEncoder userEncoder = new BCryptPasswordEncoder(8);

        assertEquals("$2a$04$L1ZxeoTjH62dhKz1Disq5ePnfD9HlJbnMdq0MILWitZ6p.9La0JKa",
                userEncoder.encode("test"));
    }

}
