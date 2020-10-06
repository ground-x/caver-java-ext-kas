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

import static org.junit.Assert.assertEquals;

public class KASUtilsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void convertDateTest() {
        String testDate1 = "2020-08-01 09:00:00";
        String testDate2 = "2020-08-01";
        String testDate3 = "2020-08-01 09:00:00:111";
        String testDate4 = "1596240000";

        assertEquals("1596240000", KASUtils.convertDateToTimestamp(testDate1));
        assertEquals("1596207600", KASUtils.convertDateToTimestamp(testDate2));
        assertEquals("1596240000", KASUtils.convertDateToTimestamp(testDate3));
        assertEquals("1596240000", KASUtils.convertDateToTimestamp(testDate4));
    }

    @Test
    public void convertDateInvalidFormat() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Unsupported parameters");

        String invalidDate = "0xaabbccee";
        KASUtils.convertDateToTimestamp(invalidDate);
    }
}