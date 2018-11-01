/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.eth.demo.sebserver.service.JSONMapper;
import org.eth.demo.sebserver.web.MVCWithOAut2Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

/** This is an example for a REST API integration test with full-stack back-end, with h2 in memory database initialized
 * with test profile and running on h2 platform (uses schema-h2.sql and data-h2.sql).
 *
 * NOTE: this test starts the Spring Boot application with an in memory database and all components. The REST endpoints
 * are secured within OAuth2 and as configured in WebSecurityConfig.class.
 *
 * @author anhefti */
public class ExamControllerTest extends MVCWithOAut2Test {

    @Autowired
    private JSONMapper jsonMapper;

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

            // [{"id":1,"institutionId":1,"lmsSetupId":1,"external_uuid":"Demo Exam 1","name":"Demo Exam 1","description":"Demo Exam","status":"READY","startTime":"2020-01-01T09:00:00.000Z","endTime":"2021-01-01T09:00:00.000Z","enrollmentURL":"mock","type":"MANAGED","owner":"internalUser1","supporter":[],"indicators":[{"name":"Demo Indicator","type":"DemoIndicator1","threshold1":20.0000,"threshold2":40.0000,"threshold3":60.0000},{"name":"Error Count","type":"errorCountIndicator","threshold1":1.0000,"threshold2":2.0000,"threshold3":3.0000}],"sebConfigMapping":[]},{"id":2,"institutionId":1,"lmsSetupId":1,"external_uuid":"Demo Exam 2","name":"Demo Exam 2","description":"Demo Exam","status":"READY","startTime":"2020-01-01T09:00:00.000Z","endTime":"2021-01-01T09:00:00.000Z","enrollmentURL":"mock","type":"BYOD","owner":"internalUser1","supporter":[],"indicators":[],"sebConfigMapping":[]},{"id":3,"institutionId":1,"lmsSetupId":1,"external_uuid":"Demo Exam 3","name":"Demo Exam 3","description":"Demo Exam","status":"FINISHED","startTime":"2018-07-30T09:00:00.000Z","endTime":"2018-08-01T00:00:00.000Z","enrollmentURL":"mock","type":"MANAGED","owner":"internalUser2","supporter":[],"indicators":[{"name":"Error Count","type":"errorCountIndicator","threshold1":1.0000,"threshold2":2.0000,"threshold3":3.0000},{"name":"Ping","type":"pingIntervalIndicator","threshold1":1000.0000,"threshold2":2000.0000,"threshold3":5000.0000}],"sebConfigMapping":[]},{"id":4,"institutionId":1,"lmsSetupId":1,"external_uuid":"Demo Exam 4","name":"Demo Exam 4","description":"Demo Exam","status":"RUNNING","startTime":"2018-01-01T00:00:00.000Z","endTime":"2019-01-01T00:00:00.000Z","enrollmentURL":"mock","type":"BYOD","owner":"internalUser2","supporter":[],"indicators":[{"name":"Error Count","type":"errorCountIndicator","threshold1":1.0000,"threshold2":2.0000,"threshold3":3.0000},{"name":"Ping","type":"pingIntervalIndicator","threshold1":1000.0000,"threshold2":2000.0000,"threshold3":5000.0000}],"sebConfigMapping":[]}]
            final JsonNode readTree = this.jsonMapper.readTree(contentAsString);
            assertTrue(readTree.isArray());
            assertTrue(readTree.has(0));
            final JsonNode jsonUserNode = readTree.get(0);
            assertTrue(jsonUserNode.isObject());
            final JsonNode id = jsonUserNode.get("id");
            assertTrue(id.intValue() == 1);
            final JsonNode lmsSetupId = jsonUserNode.get("lmsSetupId");
            assertTrue(lmsSetupId.intValue() == 1);
            final JsonNode external_uuid = jsonUserNode.get("external_uuid");
            assertTrue(external_uuid.asText().equals("Demo Exam 1"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
