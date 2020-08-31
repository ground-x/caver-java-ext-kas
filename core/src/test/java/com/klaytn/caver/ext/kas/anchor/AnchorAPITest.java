package com.klaytn.caver.ext.kas.anchor;

import com.klaytn.caver.ext.kas.KAS;
import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.api.anchor.v1.model.*;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

public class AnchorAPITest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
    public void enableAPITest() {
        KAS kas = new KAS();
        kas.enableAnchorAPI(basPath, "1001", accessKey, secretAccessKey);

        assertNotNull(kas.getAnchorAPI());
    }

    @Test
    public void sendAnchoringDataTest() throws ApiException {
        Random random = new Random();

        Map payload = new HashMap();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockResponse res = kas.getAnchorAPI().sendAnchoringData(operatorID, payload);

        assertEquals(0, res.getCode().intValue());
    }

    @Test
    public void sendAnchoringDataWithNoIdTest() throws ApiException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Payload must have an 'id' of String type.");

        Map payload = new HashMap();
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockResponse res = kas.getAnchorAPI().sendAnchoringData(operatorID, payload);
    }

    @Test
    public void sendAnchoringDataWithIdTypeTest() throws ApiException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Payload id must be String type.");

        Random random = new Random();
        String id = Integer.toString(random.nextInt());

        Map payload = new HashMap();
        payload.put("id", 1000);
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockResponse res = kas.getAnchorAPI().sendAnchoringData(operatorID, payload);
    }

    @Test
    public void getOperatorsTest() throws ApiException {
        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators();
        assertEquals((long)0, operatorList.getCode().longValue());
    }

    @Test
    public void getOperatorsWithSizeTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize(3);
        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);

        assertEquals(0, operatorList.getCode().intValue());
//        assertEquals(3, operatorList.getResult().getOperators().size());
    }

    @Test
    public void getOperatorsWithCursorTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize(3);
        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);

        anchorQueryParams.setCursor(operatorList.getResult().getCursor());
        operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);

        assertEquals(0, operatorList.getCode().intValue());
    }

    @Test
    public void getOperatorsWithFromDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromDate("2020-08-25");

        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);
        assertEquals(0, operatorList.getCode().intValue());
    }

    @Test
    public void getOperatorsWithToDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToDate("2020-08-25");

        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);
        assertEquals(0, operatorList.getCode().intValue());
    }

    @Test
    public void getOperatorsWithDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromDate("2020-08-17");
        anchorQueryParams.setToDate("2020-08-25");

        RetrieveOperatorsResponse operatorList = kas.getAnchorAPI().getOperators(anchorQueryParams);
        assertEquals(0, operatorList.getCode().intValue());
    }

    @Test
    public void getOperatorTest() throws ApiException {
        GetOperatorResponse operator = kas.getAnchorAPI().getOperator(operatorID);
        assertEquals((long)0, operator.getCode().longValue());
    }

    @Test
    public void getAnchoringTransactionsTest() throws ApiException {
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID);
        assertEquals(0, txList.getCode().longValue());
    }

    @Test
    public void getAnchoringTransactionsWithSize() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize(3);
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);

        assertEquals(0, txList.getCode().intValue());
//        assertEquals(3, txList.getResult().getTxs().size());
    }

    @Test
    public void getAnchoringTransactionsWithCursor() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize(3);
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);

        String cursor = txList.getResult().getCursor();
        anchorQueryParams.setSize(3);
        anchorQueryParams.setCursor(cursor);

        txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);
        assertEquals(0, txList.getCode().intValue());
    }

    @Test
    public void getAnchoringTransactionsWithFromDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromDate("2020-08-20 15:00:00");
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);

        assertEquals(0, txList.getCode().intValue());
//        assertEquals(3, txList.getResult().getTxs().size());
    }

    @Test
    public void getAnchoringTransactionsWithToDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToDate("2020-08-27 15:00:00");
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);

        assertEquals(0, txList.getCode().intValue());
//        assertEquals(3, txList.getResult().getTxs().size());
    }

    @Test
    public void getAnchoringTransactionsWithDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromDate("2020-08-20 15:00:00");
        anchorQueryParams.setToDate("2020-08-25 18:00:00");
        RetrieveAnchorBlockResponse txList = kas.getAnchorAPI().getAnchoringTransactions(operatorID, anchorQueryParams);

        assertEquals(0, txList.getCode().intValue());
//        assertEquals(4, txList.getResult().getTxs().size());
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

    @Test
    public void sendAnchoringDataAsyncTest() {
        Random random = new Random();

        Map payload = new HashMap();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);

        CompletableFuture<AnchorBlockResponse> future = new CompletableFuture();

        try {
            Call res = kas.getAnchorAPI().sendAnchoringDataAsync(operatorID, payload, new ApiCallback<AnchorBlockResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorBlockResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertEquals(0, future.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getOperatorsAsyncTest() {
        CompletableFuture<RetrieveOperatorsResponse> future = new CompletableFuture();

        try {
            Call res = kas.getAnchorAPI().getOperatorsAsync(new ApiCallback<RetrieveOperatorsResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(RetrieveOperatorsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertEquals(0, future.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getOperatorAsyncTest() {
        CompletableFuture<GetOperatorResponse> future = new CompletableFuture();

        try {
            Call res = kas.getAnchorAPI().getOperatorAsync(operatorID, new ApiCallback<GetOperatorResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(GetOperatorResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertEquals(0, future.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionsAsyncTest() {
        CompletableFuture<RetrieveAnchorBlockResponse> completableFuture = new CompletableFuture();

        try {
            kas.getAnchorAPI().getAnchoringTransactionsAsync(operatorID, new ApiCallback<RetrieveAnchorBlockResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(RetrieveAnchorBlockResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(completableFuture.isCompletedExceptionally()) {
                fail();
            } else {
                assertEquals(0, completableFuture.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionByTxHashAsyncTest() {
        String txHash = "0x74ee2fcf41b7bee3cb6ff0e9d5facb877cf9da178236d5ccc371318cbf09d6de";
        CompletableFuture<GetAnchorBlockByTxResponse> completableFuture = new CompletableFuture();
        try {
            kas.getAnchorAPI().getAnchoringTransactionByTxHashAsync(operatorID, txHash, new ApiCallback<GetAnchorBlockByTxResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(GetAnchorBlockByTxResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(completableFuture.isCompletedExceptionally()) {
                fail();
            } else {
                assertEquals(0, completableFuture.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionByPayloadIdAsyncTest() {
        String payloadId = "0xca0577c2f7f2537d499357c2bd08c72a808d7bb718a336b69fd37f640c73ba6d";
        CompletableFuture<GetAnchorBlockByPayloadIDResponse> completableFuture = new CompletableFuture<>();

        try {

            kas.getAnchorAPI().getAnchoringTransactionByPayloadIdAsync(operatorID, payloadId, new ApiCallback<GetAnchorBlockByPayloadIDResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(GetAnchorBlockByPayloadIDResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(completableFuture.isCompletedExceptionally()) {
                fail();
            } else {
                assertEquals(0, completableFuture.get().getCode().intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}