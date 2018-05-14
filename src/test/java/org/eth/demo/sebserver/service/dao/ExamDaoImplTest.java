/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.testing.TestWithLogging;
import org.junit.Test;
import org.mockito.Mockito;

import ch.qos.logback.classic.spi.LoggingEvent;

public class ExamDaoImplTest extends TestWithLogging {

    @Test
    public void testById_NoExamData() {
        final TestHelper tester = new TestHelper();

        when(tester.examMapperMock.selectByPrimaryKey(1L)).thenReturn(null);

        final Exam exam = tester.candidate.byId(1L);
        assertNotNull(exam);
    }

    @Test
    public void testById_UnexpectedException() {
        final TestHelper tester = new TestHelper();

        Mockito.when(tester.examMapperMock.selectByPrimaryKey(1L))
                .thenThrow(new RuntimeException("UnexpectedException"));

        final Exam exam = tester.candidate.byId(1L);
        assertNotNull(exam);

        verify(this.mockAppender).doAppend(this.captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = this.captorLoggingEvent.getValue();
        assertNotNull(loggingEvent);
    }

    private static class TestHelper {

        final ExamRecordMapper examMapperMock = mock(ExamRecordMapper.class);
        final IndicatorRecordMapper indicatorMapperMock = mock(IndicatorRecordMapper.class);
        final ExamIndicatorJoinMapper examIndicatorJoinMapperMock = mock(ExamIndicatorJoinMapper.class);

        final ExamDaoImpl candidate;

        TestHelper() {
            super();
            this.candidate = new ExamDaoImpl(
                    this.examMapperMock,
                    this.indicatorMapperMock,
                    this.examIndicatorJoinMapperMock);
        }
    }

}
