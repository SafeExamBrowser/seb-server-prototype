/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.eth.demo.sebserver.web.MVCWithOAut2Test;
import org.junit.Test;

/** This is an example for a REST API integration test with full-stack back-end, with h2 in memory database initialized
 * with test profile and running on h2 platform (uses schema-h2.sql and data-h2.sql).
 *
 * NOTE: this test starts the Spring Boot application with an in memory database and all components. The REST endpoints
 * are secured within OAuth2 and as configured in WebSecurityConfig.class.
 *
 * @author anhefti */
public class ExamControllerTest extends MVCWithOAut2Test {

    @Test
    public void getExams_givenNoToken_thenUnauthorized() throws Exception {
        this.mockMvc.perform(get("/exam"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getExams_givenToken_thenGetExamsForUser() {
        try {
            final String accessToken = obtainAccessToken("user1", "user1");
            final String contentAsString = this.mockMvc.perform(get("/exam")
                    .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertEquals("", contentAsString);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
