package com.klaytn.caver.ext.kas.anchor.v1;

import com.klaytn.caver.ext.kas.KAS;
import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.api.anchor.v1.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AnchorAPIV1Test {
    static String operatorID = "0x0Ea563A80f5ea22C174030416E7fCdbeD920D5EB";
    static String basPath = "https://anchor-api.dev.klaytn.com";
    static String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";
    static KAS kas;

    @BeforeClass
    public static void init() {
        kas = new KAS();
        kas.enableAnchorAPI(basPath, "1001", accessKey, secretAccessKey);
        kas.getAnchorAPI().getDataAnchoringTransactionApi().getApiClient().setDebugging(true);
    }

    @Test
    public void sendAnchoringDataTest() throws ApiException {
        Random random = new Random();

        Map payload = new HashMap();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockResponse res = kas.getAnchorAPI().sendAnchoringData(operatorID, payload);

        assertEquals("succeed", res.getResult().getStatus());
    }

    @Test
    public void sendAnchoringDataAsyncTest() {
        Random random = new Random();

        Map payload = new HashMap();
        payload.put("id", "1444418315");
        payload.put("field", "1");
        payload.put("filed2", 4);

        CompletableFuture<AnchorBlockResponse> future = new CompletableFuture();

        try {
            Call res = kas.getAnchorAPI().sendAnchoringDataAsync(operatorID, payload, new ApiCallback<AnchorBlockResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    System.out.println(statusCode);
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorBlockResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                    System.out.println(result.getResult().getStatus());
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertEquals("succeed", future.get().getResult().getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void convertTimestamp(String date) {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");

        try {
            datePattern.parse(date);
        } catch (DateTimeParseException e) {
            try {
                dateTimePattern.parse(date);
            } catch (DateTimeParseException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Test
    public void localDateTimeTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//        formatter.parse("2020-8-26 00:00:00.100");

        String dateTime = LocalDateTime.now().format(formatter);
        System.out.println(dateTime);

        formatter.parse("2020-08-27 00:00:00.000");

        LocalDateTime time = LocalDateTime.parse("2020-08-27 10:27:30.077", formatter);
        System.out.println(time);
    }

    @Test
    public void getOperatorsTest() throws ApiException {
        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators();
        assertEquals((long)0, operatorList.getCode().longValue());
    }

    @Test
    public void getOperatorTest() throws ApiException {
        GetOperatorResponse operator = kas.getAnchorAPI().getOperator(operatorID);
        assertEquals((long)0, operator.getCode().longValue());
    }

    @Test
    public void getAnchoringTransactionsTest() throws ApiException {
        RetirieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID);
        assertEquals(0, txList.getCode().longValue());
    }

    @Test
    public void getAnchoringTransactionByTxHash() throws ApiException {
        String txHash = "0x74ee2fcf41b7bee3cb6ff0e9d5facb877cf9da178236d5ccc371318cbf09d6de";
        GetAnchorBlockByTxResponse txbytxHash = kas.getAnchorAPI().getAnchoringTransactionByTxHash(operatorID, txHash);

        assertEquals(0, txbytxHash.getCode().longValue());
    }

    @Test
    public void getAnchoringTransactionByPayloadId() throws ApiException {
        String payloadId = "0xca0577c2f7f2537d499357c2bd08c72a808d7bb718a336b69fd37f640c73ba6d";
        GetAnchorBlockByPayloadIDResponse res = kas.getAnchorAPI().getAnchoringTransactionByPayloadId(operatorID, payloadId);

        assertEquals(0, res.getCode().longValue());
    }
}