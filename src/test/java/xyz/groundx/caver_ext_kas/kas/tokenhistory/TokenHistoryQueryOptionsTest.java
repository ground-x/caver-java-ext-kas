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

package xyz.groundx.caver_ext_kas.kas.tokenhistory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TokenHistoryQueryOptionsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    String getExpectedTimestampData(String date) {
        long timestamp = Timestamp.valueOf(date).getTime();
        return Long.toString(timestamp / 1000);
    }

    @Test
    public void kindTest() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();

        options.setKind("ft");
        assertEquals("ft", options.getKind());

        options.setKind(Arrays.asList("ft", "nft"));
        assertEquals("ft,nft", options.getKind());

        options.setKind(Arrays.asList("ft", "nft", "klay"));
        assertEquals("ft,nft,klay", options.getKind());

        options.setKind("mt");
        assertEquals("mt", options.getKind());

        options.setKind(Arrays.asList("ft", "nft", "klay", "mt"));
        assertEquals("ft,nft,klay,mt", options.getKind());
    }

    @Test
    public void kindCountExceedException() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The 'kind' option must have up to " + TokenHistoryQueryOptions.KIND.values().length + "items. [" + TokenHistoryQueryOptions.KIND.getAllKind() +"]");
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind(Arrays.asList("ft", "klay", "nft", "ft", "ft"));
    }

    @Test
    public void invalidKindValueException() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The kind option must have one of the following: [" + TokenHistoryQueryOptions.KIND.getAllKind() +"]");

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
        String dateFormat = "2020-09-04";
        String dateTimeFormat = "2020-09-04 00:00:00";
        String expectedTimeStamp = getExpectedTimestampData(dateTimeFormat);

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

        String[] actualDataSet_date = new String[] {"2020-09-04", "2020-09-05"};
        String[] actualDataSet_dateTime = new String[] {"2020-09-04 00:00:00", "2020-09-05 00:00:00"};

        String expectedString = getExpectedTimestampData(actualDataSet_dateTime[0])+ "," + getExpectedTimestampData(actualDataSet_dateTime[1]);

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();


        options.setRange(actualDataSet_date[0], actualDataSet_date[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_dateTime[0], actualDataSet_dateTime[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_date[0], actualDataSet_dateTime[1]);
        assertEquals(expectedString, options.getRange());

        options.setRange(actualDataSet_dateTime[0], actualDataSet_date[1]);
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

    @Test
    public void setStatusTest() {
        String status = "failed";
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus(status);

        assertEquals(status, options.getStatus());
    }

    @Test
    public void invalidStatusTest() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The status parameter have one of the following: [" + TokenHistoryQueryOptions.LABEL_STATUS.getAllStatus() + "]");

        String status = "invalid";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus(status);

        assertEquals(status, options.getStatus());
    }

    @Test
    public void setTypeTest() {
        String[] types = new String[] {"KIP-7", "KIP-17", "KIP-37", "ERC-20", "ERC-721", "ERC-1155"};

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        for(int i=0; i<types.length; i++) {
            options.setType(types[i]);
            assertEquals(types[i], options.getType());
        }
    }

    @Test
    public void invalidTypeTest() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The type parameter have one of the following: [" + TokenHistoryQueryOptions.CONTRACT_TYPE.getAllType() + "]");

        String type = "invalid";
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setType(type);
    }

    @Test
    public void kindTestWithEnum() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind(TokenHistoryQueryOptions.KIND.MT);
        assertEquals("mt", options.getKind());

        options.setKind(TokenHistoryQueryOptions.KIND.FT);
        assertEquals("ft", options.getKind());

        options.setKind(TokenHistoryQueryOptions.KIND.NFT);
        assertEquals("nft", options.getKind());

        options.setKind(TokenHistoryQueryOptions.KIND.KLAY);
        assertEquals("klay", options.getKind());
    }

    @Test
    public void kindTestWithEnumArr() {
        TokenHistoryQueryOptions.KIND[] optionArr = {TokenHistoryQueryOptions.KIND.NFT, TokenHistoryQueryOptions.KIND.FT, TokenHistoryQueryOptions.KIND.MT};
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind(optionArr);

        assertEquals("nft,ft,mt", options.getKind());
    }

    @Test
    public void kindTestWithEnum_throwException_countExceed() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The 'kind' option must have up to " + TokenHistoryQueryOptions.KIND.values().length + "items. [" + TokenHistoryQueryOptions.KIND.getAllKind() +"]");

        TokenHistoryQueryOptions.KIND[] optionArr = {TokenHistoryQueryOptions.KIND.NFT, TokenHistoryQueryOptions.KIND.FT, TokenHistoryQueryOptions.KIND.MT, TokenHistoryQueryOptions.KIND.KLAY, TokenHistoryQueryOptions.KIND.MT};
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();

        options.setKind(optionArr);
    }

    @Test
    public void typeTestWithEnum() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP7);
        assertEquals("KIP-7", options.getType());

        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.ERC20);
        assertEquals("ERC-20", options.getType());

        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP17);
        assertEquals("KIP-17", options.getType());

        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.ERC721);
        assertEquals("ERC-721", options.getType());

        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP37);
        assertEquals("KIP-37", options.getType());

        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.ERC1155);
        assertEquals("ERC-1155", options.getType());
    }

    @Test
    public void statusTestWithEnum() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.CANCELLED);
        assertEquals("cancelled", options.getStatus());

        options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.COMPLETED);
        assertEquals("completed", options.getStatus());

        options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.PROCESSING);
        assertEquals("processing", options.getStatus());

        options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.FAILED);
        assertEquals("failed", options.getStatus());
    }

    @Test
    public void caFilterWithList() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setCaFilter(Arrays.asList("0x6679a0006575989065832e17fe4f1e4ed4923390", "0x89a8e75d92ce84076d33f68e4909c4156847dc69"));

        assertEquals("0x6679a0006575989065832e17fe4f1e4ed4923390,0x89a8e75d92ce84076d33f68e4909c4156847dc69", options.getCaFilter());
    }
}