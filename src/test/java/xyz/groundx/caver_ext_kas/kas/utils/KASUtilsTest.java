/*
 * Copyright 2020 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.groundx.caver_ext_kas.kas.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class KASUtilsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    String getExpectedTimestampData(String date) {
        long timestamp = Timestamp.valueOf(date).getTime();
        return Long.toString(timestamp / 1000);
    }

    @Test
    public void convertDateTest() {
        String testDate1 = "2020-08-01 00:00:00";
        String testDate2 = "2020-08-01";
        String testDate3 = "2020-08-01 00:00:00:111";

        assertEquals(getExpectedTimestampData(testDate1), KASUtils.convertDateToTimestamp(testDate1));
        assertEquals(getExpectedTimestampData(testDate1), KASUtils.convertDateToTimestamp(testDate2));
        assertEquals(getExpectedTimestampData(testDate1), KASUtils.convertDateToTimestamp(testDate3));
    }

    @Test
    public void convertDateInvalidFormat() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Unsupported parameters");

        String invalidDate = "0xaabbccee";
        KASUtils.convertDateToTimestamp(invalidDate);
    }
}