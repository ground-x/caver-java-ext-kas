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

import com.squareup.okhttp.Call;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.JSON;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Accounts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class TokenHistoryAPITest {
    public static CaverExtKAS caver;
    public static int preset;

    public static String account;

    public static String ftAddress = "";
    public static String nftAddress = "";
    public static String mtAddress = "";
    public static String tokenId = "0x2";



    @BeforeClass
    public static void init() throws Exception {
        Config.init();
        caver = Config.getCaver();
        preset = Config.getPresetID();
        account = Config.getKlayProviderKeyring().getAddress();

        caver.kas.tokenHistory.getApiClient().setConnectTimeout(10000);
        caver.kas.tokenHistory.getApiClient().setDebugging(true);

        ftAddress = Config.deployKIP7(caver, account);
        nftAddress = Config.deployKIP17(caver, account);
        mtAddress = Config.deployKIP37(caver, account);

        Accounts accounts = caver.kas.wallet.getAccountList();

        Config.mintKIP17Token(caver, nftAddress, account, BigInteger.valueOf(2));

        Config.createTokenKIP37(mtAddress, account, BigInteger.ONE);
        Config.createTokenKIP37(mtAddress, account, BigInteger.valueOf(2));
        Config.mintBatchKIP37(mtAddress, account, accounts.getItems().get(0).getAddress());
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
            new Date().getTime();
            options.setRange("1584583000", "1584900000");

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

    @Test
    public void getMTListByOwner() throws ApiException {
        PageableMtTokensWithBalance result = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account);
        assertNotNull(result);
    }

    @Test
    public void getMTListByOwnerWithSize() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);

        PageableMtTokensWithBalance result = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account, options);
        assertEquals(1, result.getItems().size());
        assertNotNull(result);
    }

    @Test
    public void getMTListByOwnerWithCursor() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);

        PageableMtTokensWithBalance pageableMtTokensWithBalance = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account, options);
        options.setCursor(pageableMtTokensWithBalance.getCursor());

        PageableMtTokensWithBalance result = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account, options);
        assertEquals(1, result.getItems().size());
        assertNotNull(result);
    }

    @Test
    public void getMTListByOwnerAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<PageableMtTokensWithBalance> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTListByOwnerAsync(mtAddress, account, new ApiCallback<PageableMtTokensWithBalance>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(PageableMtTokensWithBalance result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTWithNumber() throws ApiException {
        MtToken token = caver.kas.tokenHistory.getMT(mtAddress, account, BigInteger.valueOf(1));
        assertNotNull(token);
    }

    @Test
    public void getMT() throws ApiException {
        MtToken token = caver.kas.tokenHistory.getMT(mtAddress, account, "0x1");
        assertNotNull(token);
    }

    @Test
    public void getMTAsyncWithNumber() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<MtToken> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTAsync(mtAddress, account, BigInteger.valueOf(1), new ApiCallback<MtToken>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(MtToken result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<MtToken> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTAsync(mtAddress, account, "0x1", new ApiCallback<MtToken>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(MtToken result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTOwnerListByTokenId() throws ApiException {
        PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, "0x1");
        assertNotNull(pageableMtTokens);
    }

    @Test
    public void getMTOwnerListByTokenIdWithNumber() throws ApiException {
        PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, BigInteger.valueOf(1));
        assertNotNull(pageableMtTokens);
    }

    @Test
    public void getMTOwnerListByTokenIdWithSize() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);
        PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, "0x1", options);
        assertEquals(1, pageableMtTokens.getItems().size());
        assertNotNull(pageableMtTokens);
    }

    @Test
    public void getMTOwnerListByTokenIdWithCursor() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);
        PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, "0x1", options);
        options.setCursor(pageableMtTokens.getCursor());

        PageableMtTokens result = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, "0x1", options);
        assertNotNull(result);
    }

    @Test
    public void getMTOwnerListByTokenIdAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<PageableMtTokens> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, "0x1", new ApiCallback<PageableMtTokens>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(PageableMtTokens result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTOwnerListByTokenIdAsyncWithNumber() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<PageableMtTokens> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, BigInteger.valueOf(1), new ApiCallback<PageableMtTokens>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(PageableMtTokens result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTContractList() throws ApiException {
        PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList();
        assertNotNull(result);
    }

    @Test
    public void getMTContractListWithSize() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);

        PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList(options);
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
    }

    @Test
    public void getMTContractListWithCursor() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize(1L);

        PageableMtContractDetails cursorResult = caver.kas.tokenHistory.getMTContractList(options);
        options.setCursor(cursorResult.getCursor());

        PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList(options);
        assertNotNull(result);

    }

    @Test
    public void getMTContractListWithType() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP37);

        PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList(options);
        assertNotNull(result);
    }

    @Test
    public void getMTContractListWithStatus() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.COMPLETED);

        PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList(options);
        assertNotNull(result);
    }

    @Test
    public void getMTContractListAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<PageableMtContractDetails> future = new CompletableFuture<>();

        Call call = caver.kas.tokenHistory.getMTContractListAsync(new ApiCallback<PageableMtContractDetails>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(PageableMtContractDetails result, int statusCode, Map<String, List<String>> responseHeaders) {
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
    public void getMTContract() throws ApiException {
        MtContractDetail detail = caver.kas.tokenHistory.getMTContract(mtAddress);
        assertNotNull(detail);
    }

    @Test
    public void getMTContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<MtContractDetail> future = new CompletableFuture<>();

        Call result = caver.kas.tokenHistory.getMTContractAsync(mtAddress, new ApiCallback<MtContractDetail>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(MtContractDetail result, int statusCode, Map<String, List<String>> responseHeaders) {
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
