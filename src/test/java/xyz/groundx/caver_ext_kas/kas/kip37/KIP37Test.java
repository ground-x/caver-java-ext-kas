/*
 * Copyright 2021 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.kas.kip37;

import com.klaytn.caver.kct.kip37.KIP37;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Account;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class KIP37Test {
    public static CaverExtKAS caver;
    public static Account userFeePayer;
    public static String testContractAlias;
    public static String testContractAddress;
    public static String deployerAddress;

    public static final BigInteger CREATED_TOKEN_ID_1 = BigInteger.valueOf(110000);
    public static final BigInteger CREATED_TOKEN_ID_2 = BigInteger.valueOf(210000);

    public static class Kip37TransactionStatusCallback implements ApiCallback<Kip37TransactionStatusResponse> {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();

        @Override
        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
            future.completeExceptionally(e);
        }

        @Override
        public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
            future.complete(result);
        }

        @Override
        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

        }

        @Override
        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

        }

        public CompletableFuture<Kip37TransactionStatusResponse> getFuture() {
            return future;
        }

        public void setFuture(CompletableFuture<Kip37TransactionStatusResponse> future) {
            this.future = future;
        }

        public void checkResponse() throws ExecutionException, InterruptedException {
            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        }
    }

    public static TransactionReceipt.TransactionReceiptData getReceipt(CaverExtKAS caver, String txHash) throws TransactionException, IOException {
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        return receiptProcessor.waitForTransactionReceipt(txHash);
    }

    public static void prepareKIP37Contract(CaverExtKAS caver) throws ApiException, TransactionException, IOException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, testContractAlias);
        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, deployResponse.getTransactionHash());
        Thread.sleep(5000);

        Kip37DeployerResponse deployerResponse = caver.kas.kip37.getDeployer();
        deployerAddress = deployerResponse.getAddress();
        testContractAddress = receiptData.getContractAddress();

        createToken(testContractAddress, CREATED_TOKEN_ID_1, BigInteger.valueOf(30000),"https://token-cdn-domain/{id}.json");
        createToken(testContractAddress, CREATED_TOKEN_ID_2, BigInteger.valueOf(30000),"https://token-cdn-domain/{id}.json");

        userFeePayer = caver.kas.wallet.createFeePayer();
        Config.sendValue(userFeePayer.getAddress());
        Config.sendValue(deployerAddress);
    }

    public static boolean isPausedContract(String contractAddress) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        KIP37 kip37 = caver.kct.kip37.create(contractAddress);
        return kip37.paused();
    }

    public static boolean isPausedToken(String contractAddress, String tokenId) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        KIP37 kip37 = caver.kct.kip37.create(contractAddress);
        return kip37.paused(tokenId);
    }

    public static TransactionReceipt.TransactionReceiptData createToken(String contractAddress, BigInteger tokenId, BigInteger amount, String uri) throws TransactionException, IOException, ApiException {
        Kip37TransactionStatusResponse response = caver.kas.kip37.create(contractAddress, tokenId, amount, uri);
        return getReceipt(caver, response.getTransactionHash());
    }

    private Kip37FeePayerOption setFeePayerOptions(int index) {
        Kip37FeePayerOption option = new Kip37FeePayerOption();
        if(index == 0) { // use global fee payer
            option.setEnableGlobalFeePayer(true);
        } else if(index == 1) { // use user fee payer
            option.setEnableGlobalFeePayer(false);
            Kip37FeePayerOptionUserFeePayer userOptions = new Kip37FeePayerOptionUserFeePayer();
            userOptions.setAddress(userFeePayer.getAddress());
            userOptions.setKrn(userFeePayer.getKrn());

            option.setUserFeePayer(userOptions);
        } else if(index == 2) { // use both global fee payer and user fee payer.
            option.setEnableGlobalFeePayer(true);
            Kip37FeePayerOptionUserFeePayer userOptions = new Kip37FeePayerOptionUserFeePayer();
            userOptions.setAddress(userFeePayer.getAddress());
            userOptions.setKrn(userFeePayer.getKrn());

            option.setUserFeePayer(userOptions);
        } else if(index == 3) { // use basic transaction.
            option.setEnableGlobalFeePayer(false);
        }

        return option;
    }

    @BeforeClass
    public static void init() throws TransactionException, IOException, InterruptedException, ApiException {
        Config.init();
        caver = Config.getCaver();
        caver.kas.kip37.getApiClient().setDebugging(true);

        prepareKIP37Contract(caver);
    }

    @Before
    public void beforeTest() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ApiException, InterruptedException {
        if(isPausedContract(testContractAddress)) {
            caver.kas.kip37.unpause(testContractAddress);
            Thread.sleep(5000);
        }
    }

    @Test
    public void deploy() throws ApiException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, testContractAlias);
        assertNotNull(deployResponse);
        assertTrue(deployResponse.getOptions().isEnableGlobalFeepayer());

        Thread.sleep(5000);
    }

    @Test
    public void deploy_UserFeePayer() throws ApiException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        String alias = "kk-" + new Date().getTime();
        Kip37FeePayerOption option = setFeePayerOptions(1);

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, alias, option);
        assertNotNull(deployResponse);
        assertFalse(deployResponse.getOptions().isEnableGlobalFeepayer());
        assertEquals(userFeePayer.getAddress(), deployResponse.getOptions().getUserFeePayer().getAddress());
        assertEquals(userFeePayer.getKrn(), deployResponse.getOptions().getUserFeePayer().getKrn());

        Thread.sleep(5000);
    }

    @Test
    public void deploy_EnableAllFeePayer() throws ApiException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        Kip37FeePayerOption option = setFeePayerOptions(2);

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, testContractAlias, option);
        assertNotNull(deployResponse);
        assertTrue(deployResponse.getOptions().isEnableGlobalFeepayer());
        assertEquals(userFeePayer.getAddress(), deployResponse.getOptions().getUserFeePayer().getAddress());
        assertEquals(userFeePayer.getKrn(), deployResponse.getOptions().getUserFeePayer().getKrn());

        Thread.sleep(5000);
    }

    @Test
    public void deployWithBasicTx() throws ApiException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        Kip37FeePayerOption option = setFeePayerOptions(3);

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, testContractAlias, option);
        assertNotNull(deployResponse);
        assertFalse(deployResponse.getOptions().isEnableGlobalFeepayer());

        Thread.sleep(5000);
    }

    @Test
    public void deployAsync() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Kip37DeployResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37DeployResponse> callback = new ApiCallback<Kip37DeployResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37DeployResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        caver.kas.kip37.deployAsync(uri, testContractAlias, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void getContractList() throws ApiException {
        Kip37ContractListResponse response = caver.kas.kip37.getContractList();
        assertNotNull(response);
    }

    @Test
    public void getContractListWithOptions() throws ApiException {
        KIP37QueryOptions options = new KIP37QueryOptions();
        options.setSize(1);
        options.setStatus(KIP37QueryOptions.STATUS_TYPE.DEPLOYED);

        Kip37ContractListResponse response = caver.kas.kip37.getContractList(options);
        assertNotNull(response);
    }

    @Test
    public void importContract() throws ApiException, InterruptedException {
        String contractAddress = Config.deployKIP37(caver, Config.getKlayProviderKeyring().getAddress());
        Thread.sleep(5000);

        String alias = "kk-" + new Date().getTime();
        Kip37Contract response = caver.kas.kip37.importContract(contractAddress, "uri", alias);

        assertNotNull(response);
    }

    @Test
    public void importContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37Contract> future = new CompletableFuture<>();
        ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37Contract result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        String contractAddress = Config.deployKIP37(caver, Config.getKlayProviderKeyring().getAddress());
        Thread.sleep(5000);

        String alias = "kk-" + new Date().getTime();
        caver.kas.kip37.importContractAsync(contractAddress, "uri", alias, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getContract() throws ApiException {
        String contractAddress = testContractAddress;
        Kip37Contract response = caver.kas.kip37.getContract(contractAddress);

        assertNotNull(response);
    }

    @Test
    public void getContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37Contract> future = new CompletableFuture<>();
        ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37Contract result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip37.getContractAsync(testContractAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void updateContractOptions() throws ApiException {
        Kip37Contract response = caver.kas.kip37.updateContractOptions(testContractAddress);
        assertNotNull(response);
        assertEquals(testContractAddress, response.getAddress());
        assertTrue(response.getOptions().isEnableGlobalFeePayer());
    }

    @Test
    public void updateContractOptions_withOptions() throws ApiException {
        Kip37FeePayerOption option = setFeePayerOptions(1);
        Kip37Contract response = caver.kas.kip37.updateContractOptions(testContractAddress, option);
        assertEquals(testContractAddress, response.getAddress());
        assertFalse(response.getOptions().isEnableGlobalFeePayer());
        assertEquals(option.getUserFeePayer().getAddress(), response.getOptions().getUserFeePayer().getAddress());
        assertEquals(option.getUserFeePayer().getKrn(), response.getOptions().getUserFeePayer().getKrn());

        option = setFeePayerOptions(2);
        response = caver.kas.kip37.updateContractOptions(testContractAddress, option);
        assertEquals(testContractAddress, response.getAddress());
        assertTrue(response.getOptions().isEnableGlobalFeePayer());
        assertEquals(option.getUserFeePayer().getAddress(), response.getOptions().getUserFeePayer().getAddress());
        assertEquals(option.getUserFeePayer().getKrn(), response.getOptions().getUserFeePayer().getKrn());

        option = setFeePayerOptions(3);
        response = caver.kas.kip37.updateContractOptions(testContractAddress, option);
        assertFalse(response.getOptions().isEnableGlobalFeePayer());

        option = setFeePayerOptions(0);
        response = caver.kas.kip37.updateContractOptions(testContractAddress, option);
        assertTrue(response.getOptions().isEnableGlobalFeePayer());
    }

    @Test
    public void updateContractOptionsAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37Contract> future = new CompletableFuture<>();
        ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37Contract result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip37.updateContractOptionsAsync(testContractAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void updateContractOptionsAsync_withOptions() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37Contract> future = new CompletableFuture<>();
        ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37Contract result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        Kip37FeePayerOption option = setFeePayerOptions(1);
        caver.kas.kip37.updateContractOptionsAsync(testContractAddress, option, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        option = setFeePayerOptions(2);
        caver.kas.kip37.updateContractOptionsAsync(testContractAddress, option, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        option = setFeePayerOptions(3);
        caver.kas.kip37.updateContractOptionsAsync(testContractAddress, option, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        option = setFeePayerOptions(0);
        caver.kas.kip37.updateContractOptionsAsync(testContractAddress, option, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void setApprovalForAll() throws TransactionException, IOException, ApiException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        Kip37TransactionStatusResponse response = caver.kas.kip37.setApprovalForAll(testContractAddress, deployerAddress, account.getAddress());

        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void setApprovalForAll_WithParam() throws TransactionException, IOException, ApiException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        Kip37TransactionStatusResponse response = caver.kas.kip37.setApprovalForAll(testContractAddress, deployerAddress, account.getAddress(), true);

        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void setApprovalForAllAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        Account account = caver.kas.wallet.createAccount();
        caver.kas.kip37.setApprovalForAllAsync(testContractAddress, deployerAddress, account.getAddress(), callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void setApprovalForAllAsync_withParam() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        Account account = caver.kas.wallet.createAccount();
        caver.kas.kip37.setApprovalForAllAsync(testContractAddress, deployerAddress, account.getAddress(), true, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void pause() throws ApiException, TransactionException, IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(isPausedContract(testContractAddress)) {
            caver.kas.kip37.unpause(testContractAddress);
            Thread.sleep(5000);
        }

        Kip37TransactionStatusResponse response = caver.kas.kip37.pause(testContractAddress);
        getReceipt(caver, response.getTransactionHash());
        assertTrue(isPausedContract(testContractAddress));

        Thread.sleep(5000);
    }

    @Test
    public void pause_withParam() throws ApiException, TransactionException, IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(isPausedContract(testContractAddress)) {
            caver.kas.kip37.unpause(testContractAddress);
            Thread.sleep(5000);
        }

        Kip37TransactionStatusResponse response = caver.kas.kip37.pause(testContractAddress, deployerAddress);
        getReceipt(caver, response.getTransactionHash());

        assertTrue(isPausedContract(testContractAddress));

        Thread.sleep(5000);
    }

    @Test
    public void pauseAsync() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        if(isPausedContract(testContractAddress)) {
            caver.kas.kip37.unpause(testContractAddress);
            Thread.sleep(5000);
        }

        caver.kas.kip37.pauseAsync(testContractAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void pauseAsync_withParam() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();

        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        if(isPausedContract(testContractAddress)) {
            caver.kas.kip37.unpause(testContractAddress);
            Thread.sleep(5000);
        }

        caver.kas.kip37.pauseAsync(testContractAddress, deployerAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void unpause() throws ApiException, InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException {
        if(!isPausedContract(testContractAddress)) {
            caver.kas.kip37.pause(testContractAddress);
            Thread.sleep(5000);
        }

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpause(testContractAddress);
        getReceipt(caver, response.getTransactionHash());
        assertFalse(isPausedContract(testContractAddress));

        Thread.sleep(5000);
    }

    @Test
    public void unpause_withParam() throws ApiException, InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException {
        if(!isPausedContract(testContractAddress)) {
            caver.kas.kip37.pause(testContractAddress);
            Thread.sleep(5000);
        }

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpause(testContractAddress, deployerAddress);
        getReceipt(caver, response.getTransactionHash());
        assertFalse(isPausedContract(testContractAddress));

        Thread.sleep(5000);
    }

    @Test
    public void unpauseAsync() throws ApiException, InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException, ExecutionException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        if(!isPausedContract(testContractAddress)) {
            caver.kas.kip37.pause(testContractAddress);
            Thread.sleep(5000);
        }

        caver.kas.kip37.unpauseAsync(testContractAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void unpauseAsync_withParam() throws ApiException, InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException, ExecutionException {
        CompletableFuture<Kip37TransactionStatusResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        if(!isPausedContract(testContractAddress)) {
            caver.kas.kip37.pause(testContractAddress);
            Thread.sleep(5000);
        }

        caver.kas.kip37.unpauseAsync(testContractAddress, deployerAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }

        Thread.sleep(5000);
    }

    @Test
    public void create_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.ONE;
        BigInteger initialSupply = BigInteger.valueOf(1000);
        String uri = "https://token-cdn-domain/0x01.json";

        Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void create_HexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        String tokenId = "0x2";
        String initialSupply = Numeric.toHexStringWithPrefix(BigInteger.valueOf(1000));
        String uri = "https://token-cdn-domain/0x02.json";

        Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void create_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(3);
        BigInteger initialSupply = BigInteger.valueOf(1000);
        String uri = "https://token-cdn-domain/0x03.json";

        Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void create_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        String tokenId = "0x4";
        String initialSupply = Numeric.toHexStringWithPrefix(BigInteger.valueOf(1000));
        String uri = "https://token-cdn-domain/0x04.json";

        Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void createAsync_BigIntegerType() throws ApiException, ExecutionException, InterruptedException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger tokenId = BigInteger.valueOf(100);
        BigInteger initialSupply = BigInteger.valueOf(1000);
        String uri = "https://token-cdn-domain/0x64.json";

        caver.kas.kip37.createAsync(testContractAddress, tokenId, initialSupply, uri, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }


    @Test
    public void createAsync_hexStringType() throws ApiException, ExecutionException, InterruptedException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String tokenId = Numeric.toHexStringWithPrefix(BigInteger.valueOf(101));
        String initialSupply = Numeric.toHexStringWithPrefix(BigInteger.valueOf(1000));
        String uri = "https://token-cdn-domain/0x65.json";

        caver.kas.kip37.createAsync(testContractAddress, tokenId, initialSupply, uri, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void createAsync_BigIntegerType_withSender() throws ApiException, ExecutionException, InterruptedException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger tokenId = BigInteger.valueOf(102);
        BigInteger initialSupply = BigInteger.valueOf(1000);
        String uri = "https://token-cdn-domain/{0x66}.json";

        caver.kas.kip37.createAsync(testContractAddress, tokenId, initialSupply, uri, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void createAsync_hexStringType_withSender() throws ApiException, ExecutionException, InterruptedException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String tokenId = Numeric.toHexStringWithPrefix(BigInteger.valueOf(103));
        String initialSupply = Numeric.toHexStringWithPrefix(BigInteger.valueOf(1000));
        String uri = "https://token-cdn-domain/0x67.json";

        caver.kas.kip37.createAsync(testContractAddress, tokenId, initialSupply, uri, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void getTokenList() throws ApiException {
        Kip37TokenInfoListResponse response = caver.kas.kip37.getTokenList(testContractAddress);
        assertNotNull(response);
    }

    @Test
    public void getTokenListWithOptions() throws ApiException {
        KIP37QueryOptions options = new KIP37QueryOptions();
        options.setSize(1);

        Kip37TokenInfoListResponse response = caver.kas.kip37.getTokenList(testContractAddress);
        assertNotNull(response);
    }

    @Test
    public void getTokenListAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37TokenInfoListResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TokenInfoListResponse> callback = new ApiCallback<Kip37TokenInfoListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TokenInfoListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip37.getTokenListAsync(testContractAddress, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void burn_oneToken_BigIntegerType() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger burnAmount = BigInteger.ONE;

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, CREATED_TOKEN_ID_1, burnAmount);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_oneToken_hexStringType() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger burnAmount = BigInteger.ONE;

        String tokenId_hex = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String burnAmount_hex = Numeric.toHexStringWithPrefix(burnAmount);

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenId_hex, burnAmount_hex);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_oneToken_BigIntegerType_withFrom() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger burnAmount = BigInteger.ONE;

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, CREATED_TOKEN_ID_1, burnAmount, deployerAddress);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_oneToken_hexStringType_withFrom() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger burnAmount = BigInteger.ONE;

        String tokenId_hex = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String burnAmount_hex = Numeric.toHexStringWithPrefix(burnAmount);

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenId_hex, burnAmount_hex, deployerAddress);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_multipleToken_BigInteger() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenIdArray, burnAmountArray);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_multipleToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] burnAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenIdArray, burnAmountArray);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_multipleToken_BigInteger_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenIdArray, burnAmountArray, deployerAddress);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burn_multipleToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] burnAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusResponse response = caver.kas.kip37.burn(testContractAddress, tokenIdArray, burnAmountArray, deployerAddress);
        assertNotNull(response);

        getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_oneToken_BigIntegerType() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger burnAmount = BigInteger.ONE;

        caver.kas.kip37.burnAsync(testContractAddress, CREATED_TOKEN_ID_1, burnAmount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_oneToken_hexStringType() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger burnAmount = BigInteger.ONE;

        String tokenId_hex = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String burnAmount_hex = Numeric.toHexStringWithPrefix(burnAmount);

        caver.kas.kip37.burnAsync(testContractAddress, tokenId_hex, burnAmount_hex, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_oneToken_BigIntegerType_withFrom() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger burnAmount = BigInteger.ONE;

        caver.kas.kip37.burnAsync(testContractAddress, CREATED_TOKEN_ID_1, burnAmount, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_oneToken_hexStringType_withFrom() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger burnAmount = BigInteger.ONE;

        String tokenId_hex = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String burnAmount_hex = Numeric.toHexStringWithPrefix(burnAmount);

        caver.kas.kip37.burnAsync(testContractAddress, tokenId_hex, burnAmount_hex, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_multipleToken_BigInteger() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        caver.kas.kip37.burnAsync(testContractAddress, tokenIdArray, burnAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_multipleToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] burnAmountArray = new String[]{"0x1", "0x1"};

        caver.kas.kip37.burnAsync(testContractAddress, tokenIdArray, burnAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_multipleToken_BigInteger_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        caver.kas.kip37.burnAsync(testContractAddress, tokenIdArray, burnAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void burnAsync_multipleToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] burnAmountArray = new String[]{"0x1", "0x1"};

        caver.kas.kip37.burnAsync(testContractAddress, tokenIdArray, burnAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mint_oneToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenId, amount);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_oneToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        String tokenId = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenId, amount);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_oneToken_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenId, amount, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_oneToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        String tokenId = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenId, amount, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_multipleToken_BigIntegerType() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_multipleToken_hexStringType() throws TransactionException, IOException, ApiException, InterruptedException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] mintAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_multipleToken_BigIntegerType_withSender() throws TransactionException, IOException, ApiException, InterruptedException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mint_multipleToken_hexStringType_withSender() throws TransactionException, IOException, ApiException, InterruptedException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] mintAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusResponse response = caver.kas.kip37.mint(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, deployerAddress);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_oneToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger tokenId = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenId, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_oneToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String tokenId = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenId, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_oneToken_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        BigInteger tokenId = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenId, amount, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_oneToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        String tokenId = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenId, amount, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_multipleToken_BigIntegerType() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_multipleToken_hexStringType() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] mintAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_multipleToken_BigIntegerType_withSender() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void mintAsync_multipleToken_hexStringType_withSender() throws TransactionException, IOException, ApiException, InterruptedException, ExecutionException {
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] mintAmountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.mintAsync(testContractAddress, deployerAddress, tokenIdArray, mintAmountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void transfer_oneToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        BigInteger id = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), id, amount);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void transfer_oneToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        String id = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), id, amount);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void transfer_multipleToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] amountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), tokenIdArray, amountArray);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void transfer_multipleToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] amountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), tokenIdArray, amountArray);
        assertNotNull(response);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync_oneToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Account account = caver.kas.wallet.createAccount();
        BigInteger id = CREATED_TOKEN_ID_1;
        BigInteger amount = BigInteger.ONE;

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        caver.kas.kip37.transferAsync(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), id, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync_oneToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Account account = caver.kas.wallet.createAccount();
        String id = Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1);
        String amount = Numeric.toHexStringWithPrefix(BigInteger.ONE);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        caver.kas.kip37.transferAsync(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), id, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync_multipleToken_BigIntegerType() throws ApiException, TransactionException, IOException, ExecutionException, InterruptedException {
        Account account = caver.kas.wallet.createAccount();
        BigInteger[] tokenIdArray = new BigInteger[]{CREATED_TOKEN_ID_1, CREATED_TOKEN_ID_2};
        BigInteger[] amountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        caver.kas.kip37.transferAsync(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), tokenIdArray, amountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync_multipleToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Account account = caver.kas.wallet.createAccount();
        String[] tokenIdArray = new String[]{Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_1), Numeric.toHexStringWithPrefix(CREATED_TOKEN_ID_2)};
        String[] amountArray = new String[]{"0x1", "0x1"};

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();

        caver.kas.kip37.transferAsync(testContractAddress, deployerAddress, deployerAddress, account.getAddress(), tokenIdArray, amountArray, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void pauseToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(30000);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void pauseToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(30001);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(testContractAddress, tokenIdHex);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void pauseToken_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(30002);
        createToken(testContractAddress, BigInteger.valueOf(30002), BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(testContractAddress, tokenId, deployerAddress);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void pauseToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(30003);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(testContractAddress, tokenIdHex, deployerAddress);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void pauseTokenAsync_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(30010);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x7531.json");
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.pauseTokenAsync(testContractAddress, tokenId, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void pauseTokenAsync_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(30011);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.pauseTokenAsync(testContractAddress, tokenIdHex, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void pauseTokenAsync_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(30012);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.pauseTokenAsync(testContractAddress, tokenId, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void pauseTokenAsync_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(30013);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.pauseTokenAsync(testContractAddress, tokenIdHex, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void unpauseToken_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(50000);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);
        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(testContractAddress, tokenId);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void unpauseToken_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(50001);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(testContractAddress, tokenIdHex);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void unpauseToken_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(50011);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(testContractAddress, tokenId, deployerAddress);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void unpauseToken_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger tokenId = BigInteger.valueOf(500021);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(testContractAddress, tokenIdHex, deployerAddress);
        getReceipt(caver, response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void unpauseTokenAsync_BigIntegerType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(5001111);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.unpauseTokenAsync(testContractAddress, tokenId, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void unpauseTokenAsync_hexStringType() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(50011111);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.unpauseTokenAsync(testContractAddress, tokenIdHex, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void unpauseTokenAsync_BigIntegerType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(5001234);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.unpauseTokenAsync(testContractAddress, tokenId, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void unpauseTokenAsync_hexStringType_withSender() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        BigInteger tokenId = BigInteger.valueOf(5005613);
        String tokenIdHex = Numeric.toHexStringWithPrefix(tokenId);
        createToken(testContractAddress, tokenId, BigInteger.ONE, "https://token-cdn-domain/0x02.json");
        Thread.sleep(5000);

        caver.kas.kip37.pauseToken(testContractAddress, tokenId);
        Thread.sleep(5000);

        Kip37TransactionStatusCallback callback = new Kip37TransactionStatusCallback();
        caver.kas.kip37.unpauseTokenAsync(testContractAddress, tokenIdHex, deployerAddress, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void getTokenListByOwner() throws ApiException {
        Kip37TokenListResponse response = caver.kas.kip37.getTokenListByOwner(testContractAddress, deployerAddress);
        assertNotNull(response);
    }

    @Test
    public void getTokenListByOwnerWithOptions() throws ApiException {
        KIP37QueryOptions queryOptions = new KIP37QueryOptions();
        queryOptions.setSize(1);

        Kip37TokenListResponse response = caver.kas.kip37.getTokenListByOwner(testContractAddress, deployerAddress, queryOptions);
        assertNotNull(response);
    }

    @Test
    public void getTokenListByOwnerAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37TokenListResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37TokenListResponse> callback = new ApiCallback<Kip37TokenListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37TokenListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip37.getTokenListByOwnerAsync(testContractAddress, deployerAddress, callback);
        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getDeployer() throws ApiException {
        Kip37DeployerResponse response = caver.kas.kip37.getDeployer();
        assertNotNull(response);
    }


    @Test
    public void getDeployerAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip37DeployerResponse> future = new CompletableFuture<>();
        ApiCallback<Kip37DeployerResponse> callback = new ApiCallback<Kip37DeployerResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip37DeployerResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip37.getDeployerAsync(callback);
        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

}