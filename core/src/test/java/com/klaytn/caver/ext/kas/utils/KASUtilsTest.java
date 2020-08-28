package com.klaytn.caver.ext.kas.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class KASUtilsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void convertDateTest() {
        String testDate1 = "2020-08-01 09:00:00";
        String testDate2 = "2020-08-01";
        String testDate3 = "2020-08-01 09:00:00:111";

        assertEquals("1596240000", KASUtils.convertDateToTimestamp(testDate1));
        assertEquals("1596207600", KASUtils.convertDateToTimestamp(testDate2));
        assertEquals("1596240000", KASUtils.convertDateToTimestamp(testDate3));
    }

    @Test
    public void convertDateInvalidFormat() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Unsupported parameters");

        String invalidDate = "2020-08-01invalid";
        KASUtils.convertDateToTimestamp(invalidDate);
    }
}