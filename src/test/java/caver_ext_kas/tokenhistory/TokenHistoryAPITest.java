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

package caver_ext_kas.tokenhistory;

import org.junit.Assert;
import xyz.groundx.caver_ext_kas.kas.KAS;
import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.JSON;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

public class TokenHistoryAPITest {
    public static KAS kas;
    public static String baseUrl = "https://th-api.dev.klaytn.com";

    static String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";
    static String chainId = "1001";

    @BeforeClass
    public static void init() {
        kas = new KAS();
        kas.initTokenHistoryAPI(baseUrl, chainId, accessKey, secretAccessKey);
        kas.getTokenHistoryAPI().tokenHistoryApi.getApiClient().setDebugging(true);
    }

    @Test
    public void getTransferHistory() {
        try {
            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82);
            assertNotNull(transfersData.getItems());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryWithPresetsList() {
        try {
            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(Arrays.asList(82,83));
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

            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82, options);
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

            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82, options);
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

            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82, options);
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

            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82, options);
            String cursor = transfersData.getCursor();

            options.setCursor(cursor);
            PageableTransfers transfersData_cont = kas.getTokenHistoryAPI().getTransferHistory(82, options);
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

            PageableTransfers transfersData = kas.getTokenHistoryAPI().getTransferHistory(82, options);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryInvalidPreset() {
        try {
            kas.getTokenHistoryAPI().getTransferHistory(0);
        } catch (ApiException e) {
            InvalidQueryParameterValue res = new JSON().deserialize(e.getResponseBody(), InvalidQueryParameterValue.class);
            Assert.assertEquals(1040400, res.getCode().longValue());
        }
    }

    @Test
    public void getTransferHistoryByTxHash() {
        String txHash = "0x617a56786f97c76f6d0573d36a624610f1bfb0029e8e8f7afc02c7262e9224fb";

        try {
            Transfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByTxHash(txHash);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccount() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";
        try {
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithKind() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setKind("nft");
        try {
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
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
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithRange() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange("1584573000", "1584583388");

        try {
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithSize() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setRange("1584573000", "1584583388");

        try {
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
            assertNotNull(transfers);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransferHistoryByAccountWithCursor() {
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";

        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setSize((long)3);

        try {
            PageableTransfers transfers = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
            options.setCursor(transfers.getCursor());

            PageableTransfers transfersContd = kas.getTokenHistoryAPI().getTransferHistoryByAccount(account, options);
            assertNotNull(transfersContd);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractList() {
        try {
            PageableFtContractDetails details = kas.getTokenHistoryAPI().getFTContractList();
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
            PageableFtContractDetails details = kas.getTokenHistoryAPI().getFTContractList(options);
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithType() {
        try {
            PageableFtContractDetails details = kas.getTokenHistoryAPI().getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithSize() {
        try {
            PageableFtContractDetails details = kas.getTokenHistoryAPI().getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContractListWithCursor() {
        try {
            PageableFtContractDetails details = kas.getTokenHistoryAPI().getFTContractList();
            assertNotNull(details);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getFTContract() {
        String address = "0xa35fc8998eee155ec1a9a693f83c7d6c5a3ef927";
        try {
            FtContractDetail contract = kas.getTokenHistoryAPI().getFTContract(address);
            assertNotNull(contract);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContractList() {
        try {
            PageableNftContractDetails details = kas.getTokenHistoryAPI().getNFTContractList();
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
            PageableNftContractDetails details = kas.getTokenHistoryAPI().getNFTContractList(options);
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
            PageableNftContractDetails details = kas.getTokenHistoryAPI().getNFTContractList(options);
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
            PageableNftContractDetails details = kas.getTokenHistoryAPI().getNFTContractList(options);
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
            PageableNftContractDetails details = kas.getTokenHistoryAPI().getNFTContractList(options);
            assertNotNull(details);

            options.setCursor(details.getCursor());
            PageableNftContractDetails detailWithCursor = kas.getTokenHistoryAPI().getNFTContractList(options);
            assertNotNull(detailWithCursor);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTContract() {
        String addr = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        try {
            NftContractDetail detail = kas.getTokenHistoryAPI().getNFTContract(addr);
            assertNotNull(detail);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTList() {
        String contractAddress = "0xb50ebdb5026a1df752c69d8a6ce7140c99a426db";
        try {
            PageableNfts nfts = kas.getTokenHistoryAPI().getNFTList(contractAddress);
            assertNotNull(nfts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListWithSize() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);

            PageableNfts nfts = kas.getTokenHistoryAPI().getNFTList(contractAddress, options);
            assertNotNull(nfts);
            Assert.assertEquals(3, nfts.getItems().size());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListWithCursor() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)3);

            PageableNfts nfts = kas.getTokenHistoryAPI().getNFTList(contractAddress, options);
            assertNotNull(nfts);

            options.setCursor(nfts.getCursor());

            PageableNfts nftsWithCursor = kas.getTokenHistoryAPI().getNFTList(contractAddress, options);
            assertNotNull(nftsWithCursor);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTListByOwner() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String owner = "0x88ab3cdbf31f856de69be569564b751a97ddf5d8";

        try {
            PageableNfts nfts = kas.getTokenHistoryAPI().getNFTListByOwner(contractAddress, owner);
            assertNotNull(nfts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFT() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        try {
            Nft nft = kas.getTokenHistoryAPI().getNFT(contractAddress, tokenId);
            assertNotNull(nft);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistory() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        try {
            PageableNftOwnershipChanges ownershipChanges = kas.getTokenHistoryAPI().getNFTOwnershipHistory(contractAddress, tokenId);
            assertNotNull(ownershipChanges);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistoryWithSize() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)1);
            PageableNftOwnershipChanges ownershipChanges = kas.getTokenHistoryAPI().getNFTOwnershipHistory(contractAddress, tokenId, options);
            assertNotNull(ownershipChanges);
            Assert.assertEquals(1, ownershipChanges.getItems().size());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getNFTOwnershipHistoryWithCursor() {
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        try {
            TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
            options.setSize((long)1);

            PageableNftOwnershipChanges ownershipChanges = kas.getTokenHistoryAPI().getNFTOwnershipHistory(contractAddress, tokenId, options);
            assertNotNull(ownershipChanges);

            options.setCursor(ownershipChanges.getCursor());

            PageableNftOwnershipChanges ownershipChangesWithCursor = kas.getTokenHistoryAPI().getNFTOwnershipHistory(contractAddress, tokenId, options);
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
            Call response = kas.getTokenHistoryAPI().getTransferHistoryAsync(82, new ApiCallback<PageableTransfers>() {
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
            Call res = kas.getTokenHistoryAPI().getTransferHistoryByTxHashAsync(txHash, new ApiCallback<Transfers>() {
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
        String account = "0x74f630bfcf6f3e8d7523b39de821b876446adbd4";
        CompletableFuture<PageableTransfers> future = new CompletableFuture<>();
        try {
            Call call = kas.getTokenHistoryAPI().getTransferHistoryAccountAsync(account, new ApiCallback<PageableTransfers>() {
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
            Call call = kas.getTokenHistoryAPI().getFTContractListAsync(new ApiCallback<PageableFtContractDetails>() {
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
        String address = "0xa35fc8998eee155ec1a9a693f83c7d6c5a3ef927";
        CompletableFuture<FtContractDetail> future = new CompletableFuture<>();
        try {
            Call call = kas.getTokenHistoryAPI().getFTContractAsync(address, new ApiCallback<FtContractDetail>() {
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
            Call call = kas.getTokenHistoryAPI().getNFTContractListAsync(new ApiCallback<PageableNftContractDetails>() {
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
        String addr = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        CompletableFuture<NftContractDetail> future = new CompletableFuture<>();
        try {
            Call call = kas.getTokenHistoryAPI().getNFTContractAsync(addr, new ApiCallback<NftContractDetail>() {
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
        String contractAddress = "0xb50ebdb5026a1df752c69d8a6ce7140c99a426db";
        CompletableFuture<PageableNfts> future = new CompletableFuture<>();
        try {
            Call call = kas.getTokenHistoryAPI().getNFTListAsync(contractAddress, new ApiCallback<PageableNfts>() {
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
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        CompletableFuture<Nft> future = new CompletableFuture<>();

        try {
            Call call = kas.getTokenHistoryAPI().getNFTAsync(contractAddress, tokenId, new ApiCallback<Nft>() {
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
        String contractAddress = "0xbbe63781168c9e67e7a8b112425aa84c479f39aa";
        String tokenId = "0x7b";

        CompletableFuture<PageableNftOwnershipChanges> future = new CompletableFuture<>();

        try {
            Call call = kas.getTokenHistoryAPI().getNFTOwnershipHistoryAsync(contractAddress, tokenId, new ApiCallback<PageableNftOwnershipChanges>() {
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