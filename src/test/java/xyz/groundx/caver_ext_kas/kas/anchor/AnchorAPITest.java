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

package xyz.groundx.caver_ext_kas.kas.anchor;

import com.squareup.okhttp.Call;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.kas.KAS;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.anchor.model.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AnchorAPITest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    static String operatorID;
    static CaverExtKAS caver;
    static String txHash = "";
    static String payloadId = "";

    @BeforeClass
    public static void init() throws ApiException {
        Config.init();
        caver = Config.getCaver();
        operatorID = Config.getOperatorAddress();

        AnchorTransactions txList = caver.kas.anchor.getAnchoringTransactionList(operatorID);
        txHash = txHash.equals("") ? txList.getItems().get(0).getTransactionHash() : txHash;
        payloadId = payloadId.equals("") ? txList.getItems().get(0).getPayloadId() : payloadId;

        caver.kas.anchor.getDataAnchoringTransactionApi().getApiClient().setDebugging(true);
    }

    @Test
    public void enableAPITest() {
        assertNotNull(caver.kas.getAnchor());
    }

    @Test
    public void sendAnchoringDataTest() throws ApiException {
        Random random = new Random();

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockStatus res = caver.kas.anchor.sendAnchoringData(operatorID, payload);
        assertNotNull(res);
    }

    @Test
    public void sendAnchoringDataWithNoIdTest() throws ApiException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Payload must have an 'id' of String type.");

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockStatus res = caver.kas.anchor.sendAnchoringData(operatorID, payload);
    }

    @Test
    public void sendAnchoringDataWithIdTypeTest() throws ApiException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Payload id must be String type.");

        Random random = new Random();
        String id = Integer.toString(random.nextInt());

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("id", 1000);
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockStatus res = caver.kas.anchor.sendAnchoringData(operatorID, payload);
    }

    @Test
    public void getOperatorsTest() throws ApiException {
        Operators res = caver.kas.getAnchor().getOperatorList();
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithSizeTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);

        Operators res = caver.kas.anchor.getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithCursorTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        Operators res = caver.kas.getAnchor().getOperatorList(anchorQueryParams);

        anchorQueryParams.setCursor(res.getCursor());
        res = caver.kas.anchor.getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithFromDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-25");

        Operators res = caver.kas.anchor.getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithToDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToTimestamp("2020-11-30");

        Operators res = caver.kas.anchor.getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize(3l);
        anchorQueryParams.setFromTimestamp("2020-10-17");
        anchorQueryParams.setToTimestamp("2020-10-30");

        Operators res = caver.kas.anchor.getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorTest() throws ApiException {
        Operator res = caver.kas.getAnchor().getOperator(operatorID);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsTest() throws ApiException {
        AnchorTransactions res = caver.kas.anchor.getAnchoringTransactionList(operatorID);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithSize() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        AnchorTransactions res = caver.kas.anchor.getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithCursor() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        AnchorTransactions res = caver.kas.anchor.getAnchoringTransactionList(operatorID, anchorQueryParams);

        String cursor = res.getCursor();
        anchorQueryParams.setSize((long)3);
        anchorQueryParams.setCursor(cursor);

        res = caver.kas.anchor.getAnchoringTransactionList(operatorID, anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithFromDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-20 15:00:00");
        AnchorTransactions res = caver.kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithToDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToTimestamp("2020-11-30 15:00:00");
        AnchorTransactions res = caver.kas.anchor.getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-10-26 15:00:00");
        anchorQueryParams.setToTimestamp("2020-10-28 18:00:00");
        AnchorTransactions res = caver.kas.anchor.getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionByTxHash() throws ApiException {
        AnchorTransactionDetail res = caver.kas.anchor.getAnchoringTransactionByTxHash(operatorID, txHash);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionByPayloadId() throws ApiException {
        AnchorTransactionDetail res = caver.kas.anchor.getAnchoringTransactionByPayloadId(operatorID, payloadId);

        assertNotNull(res);
    }

    @Test
    public void sendAnchoringDataAsyncTest() {
        Random random = new Random();

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);

        CompletableFuture<AnchorBlockStatus> future = new CompletableFuture();

        try {
            Call res = caver.kas.anchor.sendAnchoringDataAsync(operatorID, payload, new ApiCallback<AnchorBlockStatus>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorBlockStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getOperatorsAsyncTest() {
        CompletableFuture<Operators> future = new CompletableFuture();

        try {
            Call res = caver.kas.anchor.getOperatorListAsync(new ApiCallback<Operators>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Operators result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getOperatorAsyncTest() {
        CompletableFuture<Operator> future = new CompletableFuture();

        try {
            Call res = caver.kas.anchor.getOperatorAsync(operatorID, new ApiCallback<Operator>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Operator result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionsAsyncTest() {
        CompletableFuture<AnchorTransactions> completableFuture = new CompletableFuture();

        try {
            caver.kas.anchor.getAnchoringTransactionListAsync(operatorID, new ApiCallback<AnchorTransactions>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorTransactions result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(completableFuture.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionByTxHashAsyncTest() {
        CompletableFuture<AnchorTransactionDetail> completableFuture = new CompletableFuture();
        try {
            caver.kas.anchor.getAnchoringTransactionByTxHashAsync(operatorID, txHash, new ApiCallback<AnchorTransactionDetail>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorTransactionDetail result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(completableFuture.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAnchoringTransactionByPayloadIdAsyncTest() {
        CompletableFuture<AnchorTransactionDetail> completableFuture = new CompletableFuture<>();

        try {
            caver.kas.anchor.getAnchoringTransactionByPayloadIdAsync(operatorID, payloadId, new ApiCallback<AnchorTransactionDetail>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorTransactionDetail result, int statusCode, Map<String, List<String>> responseHeaders) {
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
                assertNotNull(completableFuture.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}