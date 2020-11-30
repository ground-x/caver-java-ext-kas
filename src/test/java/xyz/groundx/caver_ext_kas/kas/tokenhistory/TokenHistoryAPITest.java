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

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7ConstantData;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.squareup.okhttp.Call;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.kas.KAS;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.JSON;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TokenHistoryAPITest {
    public static CaverExtKAS caver;
    public static int preset;

    public static String account;

    public static String ftAddress = "";
    public static String nftAddress = "";
    public static String tokenId = "0x2";



    @BeforeClass
    public static void init() throws Exception {
        Config.init();
        caver = Config.getCaver();
        preset = Config.getPresetID();
        account = Config.getKlayProviderKeyring().getAddress();

        ftAddress = deployKIP7(caver, account);
        nftAddress = deployKIP17(caver, account);
        mintKIP17Token(caver, nftAddress, account, BigInteger.valueOf(2));

        caver.kas.tokenHistory.tokenApi.getApiClient().setDebugging(true);
    }

    public static String deployKIP7(Caver caver, String deployer) throws Exception {
        BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
        KIP7DeployParams deployParams = new KIP7DeployParams("TEST", "TES", 18, initialSupply);

        return KIP7.deploy(caver, deployParams, deployer).getContractAddress();
    }

    public static String deployKIP17(Caver caver, String deployer) throws Exception {
        String contractName = "TEST_KIP17";
        String contractSymbol = "KIP17";

        KIP17 kip17 = new KIP17(caver);
        KIP17DeployParams deployParams = new KIP17DeployParams(contractName, contractSymbol);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(5500000));

        return KIP17.deploy(caver, deployParams, deployer).getContractAddress();
    }

    public static void mintKIP17Token(Caver caver, String contractAddress, String ownerAddress, BigInteger tokenId) throws Exception {
        KIP17 kip17 = new KIP17(caver, contractAddress);
        SendOptions sendOptions = new SendOptions(ownerAddress, BigInteger.valueOf(5500000));

        kip17.mint(ownerAddress, tokenId, sendOptions);
    }

    @Test
    public void getTransferHistory() {
        try {
            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset);
            assertNotNull(transfersData.getItems());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithPresetsList() {
        try {
            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(Arrays.asList(preset));
            assertNotNull(transfersData.getItems());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithSize() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);

            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
            assertNotNull(transfersData.getItems());
            Assert.assertEquals(3, transfersData.getItems().size());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithKind() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setKind("ft");

            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
            assertNotNull(transfersData.getItems());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithKindList() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setKind(Arrays.asList("ft", "nft"));

            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
            assertNotNull(transfersData.getItems());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithCursorTest() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)10);

            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
            String cursor = transfersData.getCursor();

            options.setCursor(cursor);
            PageableTransfers transfersData_cont = caver.kas.tokenHistory.getTransferHistory(preset, options);
            assertNotNull(transfersData_cont);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithRangeTest() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setRange("1584583000", "1584583388");

            PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryInvalidPreset() {
        try {
            caver.kas.tokenHistory.getTransferHistory(0);
        } catch (ApiException e) {
            InvalidQueryParameterValue res = new JSON().deserialize(e.getResponseBody(), InvalidQueryParameterValue.class);
            Assert.assertEquals(1040400, res.getCode().longValue());
        }
    }

    @Test
    public void getTransferHistoryByTxHash() {
        String txHash = "0x617a56786f97c76f6d0573d36a624610f1bfb0029e8e8f7afc02c7262e9224fb";

        try {
            Transfers transfers = caver.kas.tokenHistory.getTransferHistoryByTxHash(txHash);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccount() {
        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithKind() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind("klay");
        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithCaFilter() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";
        String caFilter = "0xdb71dd9e38af6cb8fdd7cfe498c2337864d5a0f6";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setCaFilter(caFilter);
        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithRange() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange("1584573000", "1584583388");

        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithSize() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange("1584573000", "1584583388");

        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithCursor() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize((long)3);

        try {
            PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            options.setCursor(transfers.getCursor());

            PageableTransfers transfersContd = caver.kas.tokenHistory.getTransferHistoryByAccount(account, options);
            assertNotNull(transfersContd);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractList() {
        try {
            PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithStatus() {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus("completed");
        try {
            PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList(options);
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithType() {
        try {
            PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithSize() {
        try {
            PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithCursor() {
        try {
            PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContract() {
        try {
            FtContractDetail contract = caver.kas.tokenHistory.getFTContract(ftAddress);
            assertNotNull(contract);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractList() {
        try {
            PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractListWithStatus() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setStatus("processing");
            PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractListWithType() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setType("KIP-17");
            PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractListWithSize() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);
            PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
            assertNotNull(details);
            Assert.assertEquals(3, details.getItems().size());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractListWithCursor() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);
            PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
            assertNotNull(details);

            options.setCursor(details.getCursor());
            PageableNftContractDetails detailWithCursor = caver.kas.tokenHistory.getNFTContractList(options);
            assertNotNull(detailWithCursor);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContract() {
        try {
            NftContractDetail detail = caver.kas.tokenHistory.getNFTContract(nftAddress);
            assertNotNull(detail);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTList() {
        try {
            PageableNfts nfts = caver.kas.tokenHistory.getNFTList(nftAddress);
            assertNotNull(nfts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListWithSize() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);

            PageableNfts nfts = caver.kas.tokenHistory.getNFTList(nftAddress, options);
            assertNotNull(nfts);
            Assert.assertTrue(nfts.getItems().size() <=3);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListWithCursor() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);

            PageableNfts nfts = caver.kas.tokenHistory.getNFTList(nftAddress, options);
            assertNotNull(nfts);

            options.setCursor(nfts.getCursor());

            PageableNfts nftsWithCursor = caver.kas.tokenHistory.getNFTList(nftAddress, options);
            assertNotNull(nftsWithCursor);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListByOwner() {
        String owner = "0x88ab3cdbf31f856de69be569564b751a97ddf5d8";

        try {
            PageableNfts nfts = caver.kas.tokenHistory.getNFTListByOwner(nftAddress, owner);
            assertNotNull(nfts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFT() {
        try {
            Nft nft = caver.kas.tokenHistory.getNFT(nftAddress, tokenId);
            assertNotNull(nft);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistory() {
        try {
            PageableNftOwnershipChanges ownershipChanges = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId);
            assertNotNull(ownershipChanges);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistoryWithSize() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)1);
            PageableNftOwnershipChanges ownershipChanges = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId, options);
            assertNotNull(ownershipChanges);
            Assert.assertEquals(1, ownershipChanges.getItems().size());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistoryWithCursor() {
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)1);

            PageableNftOwnershipChanges ownershipChanges = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId, options);
            assertNotNull(ownershipChanges);

            options.setCursor(ownershipChanges.getCursor());

            PageableNftOwnershipChanges ownershipChangesWithCursor = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId, options);
            assertNotNull(ownershipChangesWithCursor);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryAsync() {
        CompletableFuture<PageableTransfers> future = new CompletableFuture<>();

        try {
            Call response = caver.kas.tokenHistory.getTransferHistoryAsync(preset, new ApiCallback<PageableTransfers>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableTransfers result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getTransferHistoryByHashAsync() {
        String txHash = "0x617a56786f97c76f6d0573d36a624610f1bfb0029e8e8f7afc02c7262e9224fb";
        CompletableFuture<Transfers> future = new CompletableFuture<>();

        try {
            Call res = caver.kas.tokenHistory.getTransferHistoryByTxHashAsync(txHash, new ApiCallback<Transfers>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Transfers result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getTransferHistoryByAccountAsync() {
        CompletableFuture<PageableTransfers> future = new CompletableFuture<>();
        try {
            Call call = caver.kas.tokenHistory.getTransferHistoryAccountAsync(account, new ApiCallback<PageableTransfers>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableTransfers result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getFTContractListAsync() {
        CompletableFuture<PageableFtContractDetails> future = new CompletableFuture();
        try {
            Call call = caver.kas.tokenHistory.getFTContractListAsync(new ApiCallback<PageableFtContractDetails>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableFtContractDetails result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getFTContractAsync() {
        CompletableFuture<FtContractDetail> future = new CompletableFuture<>();
        try {
            Call call = caver.kas.tokenHistory.getFTContractAsync(ftAddress, new ApiCallback<FtContractDetail>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(FtContractDetail result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getNFTContractListAsync() {
        CompletableFuture<PageableNftContractDetails> future = new CompletableFuture();

        try {
            Call call = caver.kas.tokenHistory.getNFTContractListAsync(new ApiCallback<PageableNftContractDetails>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableNftContractDetails result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getNFTContractTest() {
        CompletableFuture<NftContractDetail> future = new CompletableFuture<>();
        try {
            Call call = caver.kas.tokenHistory.getNFTContractAsync(nftAddress, new ApiCallback<NftContractDetail>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(NftContractDetail result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getNFTListAsync() {
        CompletableFuture<PageableNfts> future = new CompletableFuture<>();
        try {
            Call call = caver.kas.tokenHistory.getNFTListAsync(nftAddress, new ApiCallback<PageableNfts>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableNfts result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getNFTAsync() {
        CompletableFuture<Nft> future = new CompletableFuture<>();

        try {
            Call call = caver.kas.tokenHistory.getNFTAsync(nftAddress, tokenId, new ApiCallback<Nft>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Nft result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getNFTOwnershipHistoryAsync() {
        CompletableFuture<PageableNftOwnershipChanges> future = new CompletableFuture<>();

        try {
            Call call = caver.kas.tokenHistory.getNFTOwnershipHistoryAsync(nftAddress, tokenId, new ApiCallback<PageableNftOwnershipChanges>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(PageableNftOwnershipChanges result, int statusCode, Map<String, List<String>> responseHeaders) {
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
}