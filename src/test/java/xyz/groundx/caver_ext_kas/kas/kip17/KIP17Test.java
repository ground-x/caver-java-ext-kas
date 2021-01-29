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

package xyz.groundx.caver_ext_kas.kas.kip17;

import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.squareup.okhttp.Call;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Accounts;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;


public class KIP17Test {
    public static CaverExtKAS caver;
    public static int preset;
    public static String account;
    public static String testContractAlias;


    public static TransactionReceipt.TransactionReceiptData mintToken(String contractAddress, String account, BigInteger tokenId) throws ApiException, IOException, TransactionException {
        Kip17TransactionStatusResponse res = caver.kas.kip17.mint(contractAddress, account, tokenId, "http://test.com");
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        return receiptProcessor.waitForTransactionReceipt(res.getTransactionHash());
    }

    public static void prepareKIP17Contract() throws ApiException, IOException, TransactionException, InterruptedException {
        testContractAlias = "kk-" + new Date().getTime();
        Kip17TransactionStatusResponse deployStatus = caver.kas.kip17.deploy("KIP17", "KCTT", testContractAlias);
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        receiptProcessor.waitForTransactionReceipt(deployStatus.getTransactionHash());

        Thread.sleep(5000);
        for(int i=0; i<2; i++) {
            mintToken(testContractAlias, account, BigInteger.valueOf(new Date().getTime()));
        }

        Thread.sleep(5000);
    }

    @BeforeClass
    public static void init() throws Exception {
        Config.init();
        caver = Config.getCaver();
        preset = Config.getPresetID();
        account = caver.kas.wallet.createAccount().getAddress();

        caver.kas.kip17.getApiClient().setDebugging(true);
        prepareKIP17Contract();


    }

    @Test
    public void deploy() throws ApiException {
        Kip17TransactionStatusResponse response = caver.kas.kip17.deploy("KIP17", "KCT17", "kk-" + new Date().getTime());
        assertNotNull(response);
    }

    @Test
    public void deployAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TransactionStatusResponse> future = new CompletableFuture<>();
        Call result = caver.kas.kip17.deployAsync("KIP17", "KCT17", "kk-" + new Date().getTime(), new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getContractList() throws ApiException {
        Kip17ContractListResponse response = caver.kas.kip17.getContractList();
        assertNotNull(response);
    }

    @Test
    public void getContractListWithSize() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        Kip17ContractListResponse response = caver.kas.kip17.getContractList(options);
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
    }

    @Test
    public void getContractListWithCursor() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        Kip17ContractListResponse response = caver.kas.kip17.getContractList(options);
        options.setCursor(response.getCursor());

        response = caver.kas.kip17.getContractList(options);
        assertNotNull(response);
    }

    @Test
    public void getContractListAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17ContractListResponse> future = new CompletableFuture<>();
        Call result = caver.kas.kip17.getContractListAsync(new ApiCallback<Kip17ContractListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17ContractListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getContract() throws ApiException {
        Kip17ContractListResponse listResponse = caver.kas.kip17.getContractList();
        Kip17ContractInfoResponse infoResponse = caver.kas.kip17.getContract(listResponse.getItems().get(0).getAddress());

        assertNotNull(infoResponse);
    }

    @Test
    public void getContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17ContractInfoResponse> future = new CompletableFuture<>();
        Kip17ContractListResponse listResponse = caver.kas.kip17.getContractList();

        caver.kas.kip17.getContractAsync(listResponse.getItems().get(0).getAddress(), new ApiCallback<Kip17ContractInfoResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17ContractInfoResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void mint() throws ApiException {
        String to = account;
        String id = Numeric.toHexStringWithPrefix(BigInteger.valueOf(new Date().getTime()));
        String uri = "https://test.com";

        Kip17TransactionStatusResponse response = caver.kas.kip17.mint(testContractAlias, to, id, uri);

        assertNotNull(response);
    }

    @Test
    public void mintAsync() throws ApiException, ExecutionException, InterruptedException {
        String to = account;
        String id = Numeric.toHexStringWithPrefix(BigInteger.valueOf(new Date().getTime()));
        String uri = "https://test.com";

        CompletableFuture<Kip17TransactionStatusResponse> future = new CompletableFuture<>();
        Call result = caver.kas.kip17.mintAsync(testContractAlias, to, id, uri, new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getTokenList() throws ApiException {
        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias);
        assertNotNull(response);
    }

    @Test
    public void getTokenListWithSize() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias, options);
        assertNotNull(response);
        assertTrue(response.getItems().size() < 2);
    }

    @Test
    public void getTokenListWithCursor() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias, options);
        options.setCursor(response.getCursor());

        response = caver.kas.kip17.getTokenList(testContractAlias, options);
        assertNotNull(response);
    }

    @Test
    public void getTokenListAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TokenListResponse> future = new CompletableFuture<>();
        Call response = caver.kas.kip17.getTokenListAsync(testContractAlias, new ApiCallback<Kip17TokenListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TokenListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getToken() throws ApiException {
        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias);

        GetKip17TokenResponse tokenResponse = caver.kas.kip17.getToken(testContractAlias, response.getItems().get(0).getTokenId());
        assertNotNull(tokenResponse);
    }

    @Test
    public void getTokenAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TokenListResponse> future = new CompletableFuture<>();
        Call result = caver.kas.kip17.getTokenListAsync(testContractAlias, new ApiCallback<Kip17TokenListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TokenListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void transfer() throws ApiException, IOException, TransactionException {
        BigInteger tokenId = BigInteger.valueOf(new Date().getTime());
        mintToken(testContractAlias, account, tokenId);
        String to = caver.kas.wallet.createAccount().getAddress();

        Kip17TransactionStatusResponse response = caver.kas.kip17.transfer(testContractAlias, account, account, to, tokenId);
        assertNotNull(response);
    }

    @Test
    public void transferAsync() throws ApiException, IOException, TransactionException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TransactionStatusResponse> future = new CompletableFuture<>();

        BigInteger tokenId = BigInteger.valueOf(new Date().getTime());
        mintToken(testContractAlias, account, tokenId);
        String to = caver.kas.wallet.createAccount().getAddress();

        Call result = caver.kas.kip17.transferAsync(testContractAlias, account, account, to, tokenId, new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void burn() throws ApiException, IOException, TransactionException {
        BigInteger id = BigInteger.valueOf(new Date().getTime());
        mintToken(testContractAlias, account, id);

        Kip17TransactionStatusResponse burnResponse = caver.kas.kip17.burn(testContractAlias, account, id);
        assertNotNull(burnResponse);
    }

    @Test
    public void burnAsync() throws ApiException, IOException, TransactionException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TransactionStatusResponse> future = new CompletableFuture<>();

        BigInteger id = BigInteger.valueOf(new Date().getTime());
        mintToken(testContractAlias, account, id);

        Call result = caver.kas.kip17.burnAsync(testContractAlias, account, id, new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void approve() throws ApiException {
        String from = account;
        String to = caver.kas.wallet.createAccount().getAddress();

        Kip17TokenListResponse res = caver.kas.kip17.getTokenList(testContractAlias);
        Kip17TransactionStatusResponse response = caver.kas.kip17.approve(testContractAlias, from, to, res.getItems().get(0).getTokenId());
    }

    @Test
    public void approveAsync() throws ApiException, ExecutionException, InterruptedException {
        String from = account;
        String to = caver.kas.wallet.createAccount().getAddress();

        Kip17TokenListResponse res = caver.kas.kip17.getTokenList(testContractAlias);

        CompletableFuture<Kip17TransactionStatusResponse> completableFuture = new CompletableFuture<>();
        Call result = caver.kas.kip17.approveAsync(testContractAlias, from, to, res.getItems().get(0).getTokenId(), new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void approveAll() throws ApiException, IOException, TransactionException {
        String from = caver.kas.wallet.createAccount().getAddress();
        String to = caver.kas.wallet.createAccount().getAddress();

        Kip17TransactionStatusResponse response = caver.kas.kip17.approveAll(testContractAlias, from, to, true);
        assertNotNull(response);
    }

    @Test
    public void approveAllAsync() throws ApiException, IOException, TransactionException, ExecutionException, InterruptedException {
        CompletableFuture<Kip17TransactionStatusResponse> future = new CompletableFuture<>();

        String from = caver.kas.wallet.createAccount().getAddress();
        String to = caver.kas.wallet.createAccount().getAddress();

        Call result = caver.kas.kip17.approveAllAsync(testContractAlias, from, to, true, new ApiCallback<Kip17TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip17TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getTokenListByOwner() throws ApiException {
        GetOwnerKip17TokensResponse res = caver.kas.kip17.getTokenListByOwner(testContractAlias, account);
        assertNotNull(res);
    }

    @Test
    public void getTokenListByOwnerWithSize() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        GetOwnerKip17TokensResponse res = caver.kas.kip17.getTokenListByOwner(testContractAlias, account, options);
        assertNotNull(res);
        assertTrue(res.getItems().size() < 2);
    }

    @Test
    public void getTokenListByOwnerWithCursor() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        options.setSize(1L);

        GetOwnerKip17TokensResponse res = caver.kas.kip17.getTokenListByOwner(testContractAlias, account, options);
        options.setCursor(res.getCursor());

        res = caver.kas.kip17.getTokenListByOwner(testContractAlias, account, options);
        assertNotNull(res);
    }

    @Test
    public void getTokenListByOwnerAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<GetOwnerKip17TokensResponse> future = new CompletableFuture<>();

        Call result = caver.kas.kip17.getTokenListByOwnerAsync(testContractAlias, account, new ApiCallback<GetOwnerKip17TokensResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(GetOwnerKip17TokensResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

    @Test
    public void getTransferHistory() throws ApiException {
        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias);

        GetKip17TokenHistoryResponse tokenHistoryResponse = caver.kas.kip17.getTransferHistory(testContractAlias, response.getItems().get(0).getTokenId());
        assertNotNull(tokenHistoryResponse);
    }

    @Test
    public void getTransferHistoryAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<GetKip17TokenHistoryResponse> future = new CompletableFuture<>();

        Kip17TokenListResponse response = caver.kas.kip17.getTokenList(testContractAlias);

        Call result = caver.kas.kip17.getTransferHistoryAsync(testContractAlias, response.getItems().get(0).getTokenId(), new ApiCallback<GetKip17TokenHistoryResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(GetKip17TokenHistoryResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    }

}