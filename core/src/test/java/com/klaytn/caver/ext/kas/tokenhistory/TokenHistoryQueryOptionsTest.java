package com.klaytn.caver.ext.kas.tokenhistory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TokenHistoryQueryOptionsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void kindTest() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();

        options.setKind("ft");
        assertEquals("ft", options.getKind());

        options.setKind(Arrays.asList("ft", "nft"));
        assertEquals("ft,nft", options.getKind());

        options.setKind(Arrays.asList("ft", "nft", "klay"));
        assertEquals("ft,nft,klay", options.getKind());
    }

    @Test
    public void kindCountExceedException() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The 'kind' option must have up to 3 items. ['klay', 'ft', 'nft']");
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind(Arrays.asList("ft", "klay", "nft", "ft"));
    }

    @Test
    public void invalidKindValueException() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The kind option must have one of the following: ['klay', 'ft', 'nft']");

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind("invalid");
    }

    @Test
    public void rangeBlockNumber() {
        String blockNumber = "0x12";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange(blockNumber);

        assertEquals("0x12", blockNumber);
    }

    @Test
    public void rangeInvalidBlockNumber() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Unsupported parameters");

        String blockNumber = "0xinvalid";
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange(blockNumber);
    }

    @Test
    public void rangeTimestampTest() {
        String expectedTimeStamp = "1599145200";
        String dateFormat = "2020-09-04";
        String dateTimeFormat = "2020-09-04 00:00:00";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange(dateFormat);
        assertEquals(expectedTimeStamp, options.getRange());

        options.setRange(dateTimeFormat);
        assertEquals(expectedTimeStamp, options.getRange());

        options.setRange(expectedTimeStamp);
        assertEquals(expectedTimeStamp, options.getRange());
    }

    @Test
    public void rangeFromToTest() {
        String expectedString = "1599145200,1599231600";
        String[] actualDataSet_timestamp = new String[] {"1599145200", "1599231600"};
        String[] actualDataSet_date = new String[] {"2020-09-04", "2020-09-05"};
        String[] actualDataSet_dateTime = new String[] {"2020-09-04 00:00:00", "2020-09-05 00:00:00"};

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange(actualDataSet_timestamp[0], actualDataSet_timestamp[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_date[0], actualDataSet_date[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_dateTime[0], actualDataSet_dateTime[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_timestamp[0], actualDataSet_date[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_date[0], actualDataSet_dateTime[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_date[0], actualDataSet_dateTime[1]);
        assertEquals(expectedString, options.getRange());
    }

    @Test
    public void setRangeWithDifferentFormat() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The range parameter('from', 'to') must have same type(block number(hex) / timestamp(decimal))");

        String from = "1599145200";
        String to = "0x15";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange(from, to);
    }
}