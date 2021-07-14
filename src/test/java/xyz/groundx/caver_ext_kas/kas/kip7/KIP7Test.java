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

package xyz.groundx.caver_ext_kas.kas.kip7;

import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.utils.Utils;
import com.squareup.okhttp.Call;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class KIP7Test {
    public static CaverExtKAS caver;
    public static String account;
    public static String testContractAlias;
    public static String testContractAddress;
    public static String deployerAddress;

    public static class Kip7TransactionStatusCallback implements ApiCallback<Kip7TransactionStatusResponse> {
        CompletableFuture<Kip7TransactionStatusResponse> future = new CompletableFuture<>();

        @Override
        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
            future.completeExceptionally(e);
        }

        @Override
        public void onSuccess(Kip7TransactionStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
            future.complete(result);
        }

        @Override
        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

        }

        @Override
        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

        }

        public CompletableFuture<Kip7TransactionStatusResponse> getFuture() {
            return future;
        }

        public void setFuture(CompletableFuture<Kip7TransactionStatusResponse> future) {
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

    @BeforeClass
    public static void init() throws TransactionException, IOException, InterruptedException, ApiException {
        Config.init();
        caver = Config.getCaver();
        caver.kas.kip7.getApiClient().setDebugging(true);
        account = caver.kas.wallet.createAccount().getAddress();
        prepareKIP7Contract();
    }

    public static  TransactionReceipt.TransactionReceiptData getReceipt(CaverExtKAS caver, String txHash) throws TransactionException, IOException {
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        return receiptProcessor.waitForTransactionReceipt(txHash);
    }

    public static void prepareKIP7Contract() throws ApiException, TransactionException, IOException, InterruptedException {
        BigInteger initial_supply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
        testContractAlias = "kk-" + new Date().getTime();
        Kip7TransactionStatusResponse response = caver.kas.kip7.deploy("TEST-KIP7", "TKIP7", 18, initial_supply, testContractAlias);

        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, response.getTransactionHash());
        Thread.sleep(5000);

        Kip7DeployerResponse deployerRes = caver.kas.kip7.getDeployer();
        deployerAddress = deployerRes.getAddress();
        testContractAddress = receiptData.getContractAddress();

        BigInteger transferAmount = BigInteger.valueOf(100).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
        caver.kas.kip7.transfer(testContractAlias, deployerAddress, account, transferAmount);
        Thread.sleep(3000);
    }

    public static boolean isPaused() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        KIP7 kip7 = new KIP7(caver, testContractAddress);
        return kip7.paused();
    }

    @Test
    public void getDeployer() throws ApiException {
        Kip7DeployerResponse deployerResponse = caver.kas.kip7.getDeployer();
        assertNotNull(deployerResponse);
        assertTrue(Utils.isAddress(deployerResponse.getAddress()));
    }

    @Test
    public void getDeployerAsync() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Kip7DeployerResponse> future = new CompletableFuture<>();
        Call result = caver.kas.kip7.getDeployerAsync(new ApiCallback<Kip7DeployerResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip7DeployerResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
            assertTrue(Utils.isAddress(future.get().getAddress()));
        }
        Thread.sleep(5000);
    }

    @Test
    public void deploy() throws ApiException {
        String testAlias = "test-contract" + new Date().getTime();
        String name = "TEST_KIP7";
        String symbol = "TKIP7";
        int decimals = 18;
        BigInteger initial_supply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initial_supply, testAlias);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());
    }

    @Test
    public void deployAsync() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String testAlias = "test-contract" + new Date().getTime();
        String name = "TEST_KIP7";
        String symbol = "TKIP7";
        int decimals = 18;
        BigInteger initial_supply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18

        Call call = caver.kas.kip7.deployAsync(name, symbol, decimals, initial_supply, testAlias, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void deploy_withHexStringSupply() throws ApiException, InterruptedException {
        String testAlias = "test-contract" + new Date().getTime();
        String name = "TEST_KIP7";
        String symbol = "TKIP7";
        int decimals = 18;
        String initial_supply = "0x152d02c7e14af6800000";

        Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initial_supply, testAlias);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void deployAsync_withHexStringSupply() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String testAlias = "test-contract" + new Date().getTime();
        String name = "TEST_KIP7";
        String symbol = "TKIP7";
        int decimals = 18;
        String initial_supply = "0x152d02c7e14af6800000";

        Call call = caver.kas.kip7.deployAsync(name, symbol, decimals, initial_supply, testAlias, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void getContractList() throws ApiException {
        Kip7ContractListResponse response = caver.kas.kip7.getContractList();
        assertNotNull(response);
    }

    @Test
    public void getContract() throws ApiException {
        Kip7ContractMetadataResponse response = caver.kas.kip7.getContract(testContractAlias);
        assertNotNull(response);
    }

    @Test
    public void getContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip7ContractMetadataResponse> future = new CompletableFuture<>();

        caver.kas.kip7.getContractAsync(testContractAlias, new ApiCallback<Kip7ContractMetadataResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip7ContractMetadataResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getContractListWithSizeOptions() throws ApiException {
        KIP7QueryOptions options = new KIP7QueryOptions();
        options.setSize(1);

        Kip7ContractListResponse response = caver.kas.kip7.getContractList(options);
        assertNotNull(response);
    }

    @Test
    public void getContractListAsync() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Kip7ContractListResponse> future = new CompletableFuture<>();

        ApiCallback<Kip7ContractListResponse> callback = new ApiCallback<Kip7ContractListResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip7ContractListResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.kip7.getContractListAsync(callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void allowance() throws ApiException {
        Kip7TokenBalanceResponse response = caver.kas.kip7.allowance(testContractAlias, deployerAddress, account);
        assertNotNull(response);
    }

    @Test
    public void allowanceAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<Kip7TokenBalanceResponse> future = new CompletableFuture<>();

        caver.kas.kip7.allowanceAsync(testContractAlias, deployerAddress, account, new ApiCallback<Kip7TokenBalanceResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip7TokenBalanceResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void balance() throws ApiException {
        Kip7TokenBalanceResponse response = caver.kas.kip7.balance(testContractAlias, deployerAddress);
        assertNotNull(response);
    }

    @Test
    public void balanceAsync() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Kip7TokenBalanceResponse> future = new CompletableFuture<>();

        caver.kas.kip7.balanceAsync(testContractAlias, deployerAddress, new ApiCallback<Kip7TokenBalanceResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Kip7TokenBalanceResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void approve() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, owner, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void approveAsync() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.approveAsync(contractAlias, owner, spender, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void approve_withHexStringAmount() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, owner, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void approveAsync_withHexStringAmount() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.approveAsync(contractAlias, owner, spender, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void approveWithOutOwner() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void approveWithOutOwnerAsync() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();
        String contractAlias = testContractAlias;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.approveAsync(contractAlias, spender, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void approveWithOutOwner_withHexStringAmount() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    public void approveWithOutOwnerAsync_withHexStringAmount() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();
        String contractAlias = testContractAlias;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.approveAsync(contractAlias, spender, amount, callback);
        callback.checkResponse();

        Thread.sleep(5000);
    }

    @Test
    public void transfer() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, response.getTransactionHash());
        assertNotNull(receiptData);

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.transferAsync(contractAlias, owner, spender, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void transferWithHexStringAmount() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, response.getTransactionHash());
        assertNotNull(receiptData);

        Thread.sleep(5000);
    }

    @Test
    public void transferAsync_WithHexStringAmount() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.transferAsync(contractAlias, owner, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void transferWithOutOwner() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, to, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, response.getTransactionHash());
        assertNotNull(receiptData);

        Thread.sleep(5000);
    }

    @Test
    public void transferWithOutOwnerAsync() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.transferAsync(contractAlias, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void transferWithOutOwner_WithHexStringAmount() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, spender, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, response.getTransactionHash());
        assertNotNull(receiptData);

        Thread.sleep(5000);
    }

    @Test
    public void transferWithOutOwnerAsync_WithHexStringAmount() throws ApiException, ExecutionException, InterruptedException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.transferAsync(contractAlias, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void transferFrom() throws TransactionException, IOException, ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        Kip7TransactionStatusResponse transferRes = caver.kas.kip7.transferFrom(contractAlias, spender, owner, to, amount);
        assertNotNull(transferRes);
        assertNotNull(transferRes.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void transferFromAsync() throws TransactionException, IOException, InterruptedException, ApiException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        caver.kas.kip7.transferFromAsync(contractAlias, spender, owner, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void transferFrom_withHexStringAmount() throws TransactionException, IOException, InterruptedException, ApiException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        Kip7TransactionStatusResponse transferRes = caver.kas.kip7.transferFrom(contractAlias, spender, owner, to, amount);
        assertNotNull(transferRes);
        assertNotNull(transferRes.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void transferFromAsync_withHexStringAmount() throws TransactionException, IOException, InterruptedException, ApiException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        caver.kas.kip7.transferFromAsync(contractAlias, spender, owner, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void mint() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount);
        assertNotNull(response);

        Thread.sleep(3000);
    }

    @Test
    public void mintAsync() throws ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String to = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.mintAsync(contractAlias, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void mint_withHexStringAmount() throws InterruptedException, ApiException {
        String contractAlias = testContractAlias;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount);
        assertNotNull(response);

        Thread.sleep(3000);
    }

    @Test
    public void mintAsync_withHexStringAmount() throws InterruptedException, ApiException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String to = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.mintAsync(contractAlias, to, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burnWithOutFrom() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnAsyncWithOutFrom() throws ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.burnAsync(contractAlias, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burnWithOutFrom_withHexStringAmount() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnAsyncWithOutFrom_withHexStringAmount() throws ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.burnAsync(contractAlias, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burn() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String from = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, from, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnAsync() throws ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String from = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        caver.kas.kip7.burnAsync(contractAlias, from, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burn_withHexStringAmount() throws ApiException, InterruptedException {
        String contractAlias = testContractAlias;
        String from = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, from, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnAsync_withHexStringAmount() throws ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String from = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        caver.kas.kip7.burnAsync(contractAlias, from, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burnFrom() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        Kip7TransactionStatusResponse response = caver.kas.kip7.burnFrom(contractAlias, spender, owner, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnFromAsync() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        caver.kas.kip7.burnFromAsync(contractAlias, spender, owner, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void burnFromWithHexString() throws ApiException, TransactionException, IOException, InterruptedException {
        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        Kip7TransactionStatusResponse response = caver.kas.kip7.burnFrom(contractAlias, spender, owner, amount);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
    }

    @Test
    public void burnFromAsync_WithHexString() throws ApiException, TransactionException, IOException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        String contractAlias = testContractAlias;
        String owner = deployerAddress;
        String spender = account;
        String amount = "0x8ac7230489e80000"; // 10 * 10^18

        Kip7TransactionStatusResponse approveRes = caver.kas.kip7.approve(contractAlias, spender, owner, amount);
        getReceipt(caver, approveRes.getTransactionHash());
        Thread.sleep(3000);

        caver.kas.kip7.burnFromAsync(contractAlias, spender, owner, amount, callback);
        callback.checkResponse();

        Thread.sleep(3000);
    }

    @Test
    public void pause() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ApiException, InterruptedException {
        if(isPaused()) {
            caver.kas.kip7.unpause(testContractAlias);
            Thread.sleep(3000);
        }

        Kip7TransactionStatusResponse response = caver.kas.kip7.pause(testContractAlias);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
        assertTrue(isPaused());

        caver.kas.kip7.unpause(testContractAlias);
        Thread.sleep(3000);
    }

    @Test
    public void pauseAsync() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ApiException, InterruptedException, ExecutionException {
        if(isPaused()) {
            caver.kas.kip7.unpause(testContractAlias);
            Thread.sleep(3000);
        }

        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();
        caver.kas.kip7.pauseAsync(testContractAlias, callback);
        Thread.sleep(3000);
        callback.checkResponse();
        assertTrue(isPaused());

        caver.kas.kip7.unpause(testContractAlias);
        Thread.sleep(3000);
    }

    @Test
    public void unpause() throws ApiException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        if(!isPaused()) {
            caver.kas.kip7.pause(testContractAlias);
            Thread.sleep(3000);
        }

        Kip7TransactionStatusResponse response = caver.kas.kip7.unpause(testContractAlias);
        assertNotNull(response);
        assertNotNull(response.getTransactionHash());

        Thread.sleep(3000);
        assertTrue(!isPaused());
    }

    @Test
    public void unpauseAsync() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ApiException, InterruptedException, ExecutionException {
        Kip7TransactionStatusCallback callback = new Kip7TransactionStatusCallback();

        if(!isPaused()) {
            caver.kas.kip7.pause(testContractAlias);
            Thread.sleep(3000);
        }

        caver.kas.kip7.unpauseAsync(testContractAlias, callback);
        Thread.sleep(3000);
        callback.checkResponse();
    }

}
