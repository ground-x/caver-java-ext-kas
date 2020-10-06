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

    static String operatorID = "0x0Ea563A80f5ea22C174030416E7fCdbeD920D5EB";
    static String basPath = "https://anchor-api.dev.klaytn.com";
    static String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";
    static CaverExtKAS caver;
    static KAS kas;

    @BeforeClass
    public static void init() {
        caver = new CaverExtKAS();
        caver.initAnchorAPI(basPath, "1001", accessKey, secretAccessKey);
        kas = caver.getKas();

        kas.getAnchor().getDataAnchoringTransactionApi().getApiClient().setDebugging(true);
    }

    @Test
    public void enableAPITest() {
        KAS kas = new KAS();
        kas.initAnchorAPI(basPath, "1001", accessKey, secretAccessKey);

        assertNotNull(kas.getAnchor());
    }

    @Test
    public void sendAnchoringDataTest() throws ApiException {
        Random random = new Random();

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("id", Integer.toString(random.nextInt()));
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockStatus res = kas.getAnchor().sendAnchoringData(operatorID, payload);
        assertNotNull(res);
    }

    @Test
    public void sendAnchoringDataWithNoIdTest() throws ApiException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Payload must have an 'id' of String type.");

        AnchorBlockPayload payload = new AnchorBlockPayload();
        payload.put("field", "1");
        payload.put("filed2", 4);
        AnchorBlockStatus res = kas.getAnchor().sendAnchoringData(operatorID, payload);
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
        AnchorBlockStatus res = kas.getAnchor().sendAnchoringData(operatorID, payload);
    }

    @Test
    public void getOperatorsTest() throws ApiException {
        Operators res = kas.getAnchor().getOperatorList();
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithSizeTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);

        Operators res = kas.getAnchor().getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithCursorTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        Operators res = kas.getAnchor().getOperatorList(anchorQueryParams);

        anchorQueryParams.setCursor(res.getCursor());
        res = kas.getAnchor().getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithFromDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-25");

        Operators res = kas.getAnchor().getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithToDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToTimestamp("2020-08-25");

        Operators res = kas.getAnchor().getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorsWithDateTest() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-17");
        anchorQueryParams.setToTimestamp("2020-08-25");

        Operators res = kas.getAnchor().getOperatorList(anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getOperatorTest() throws ApiException {
        Operator res = kas.getAnchor().getOperator(operatorID);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsTest() throws ApiException {
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithSize() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithCursor() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setSize((long)3);
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        String cursor = res.getCursor();
        anchorQueryParams.setSize((long)3);
        anchorQueryParams.setCursor(cursor);

        res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);
        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithFromDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-20 15:00:00");
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithToDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setToTimestamp("2020-08-27 15:00:00");
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionsWithDate() throws ApiException {
        AnchorQueryOptions anchorQueryParams = new AnchorQueryOptions();
        anchorQueryParams.setFromTimestamp("2020-08-20 15:00:00");
        anchorQueryParams.setToTimestamp("2020-08-25 18:00:00");
        AnchorTransactions res = kas.getAnchor().getAnchoringTransactionList(operatorID, anchorQueryParams);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionByTxHash() throws ApiException {
        String txHash = "0x74ee2fcf41b7bee3cb6ff0e9d5facb877cf9da178236d5ccc371318cbf09d6de";
        AnchorBlockTransactions res = kas.getAnchor().getAnchoringTransactionByTxHash(operatorID, txHash);

        assertNotNull(res);
    }

    @Test
    public void getAnchoringTransactionByPayloadId() throws ApiException {
        String payloadId = "0xca0577c2f7f2537d499357c2bd08c72a808d7bb718a336b69fd37f640c73ba6d";
        AnchorBlockTransactions res = kas.getAnchor().getAnchoringTransactionByPayloadId(operatorID, payloadId);

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
            Call res = kas.getAnchor().sendAnchoringDataAsync(operatorID, payload, new ApiCallback<AnchorBlockStatus>() {
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
            Call res = kas.getAnchor().getOperatorListAsync(new ApiCallback<Operators>() {
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
            Call res = kas.getAnchor().getOperatorAsync(operatorID, new ApiCallback<Operator>() {
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
            kas.getAnchor().getAnchoringTransactionListAsync(operatorID, new ApiCallback<AnchorTransactions>() {
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
        String txHash = "0x74ee2fcf41b7bee3cb6ff0e9d5facb877cf9da178236d5ccc371318cbf09d6de";
        CompletableFuture<AnchorBlockTransactions> completableFuture = new CompletableFuture();
        try {
            kas.getAnchor().getAnchoringTransactionByTxHashAsync(operatorID, txHash, new ApiCallback<AnchorBlockTransactions>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorBlockTransactions result, int statusCode, Map<String, List<String>> responseHeaders) {
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
        String payloadId = "0xca0577c2f7f2537d499357c2bd08c72a808d7bb718a336b69fd37f640c73ba6d";
        CompletableFuture<AnchorBlockTransactions> completableFuture = new CompletableFuture<>();

        try {

            kas.getAnchor().getAnchoringTransactionByPayloadIdAsync(operatorID, payloadId, new ApiCallback<AnchorBlockTransactions>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    completableFuture.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AnchorBlockTransactions result, int statusCode, Map<String, List<String>> responseHeaders) {
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