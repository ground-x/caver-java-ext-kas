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
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.kas.utils.KASUtils;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Representing an wrapping class tha connects Token history APi.
 */
public class TokenHistory {
    /**
     * Token API rest-client object.
     */
    TokenApi tokenApi;

    /**
     * Token contract API rest-client object.
     */
    TokenContractApi tokenContractApi;

    /**
     * Token history API rest-client object.
     */
    TokenHistoryApi tokenHistoryApi;

    /**
     * Token ownership API rest-client object.
     */
    TokenOwnershipApi tokenOwnershipApi;

    /**
     * Klaytn network id.
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * Creates an TokenHistoryAPI instance.
     * @param chainId A Klaytn network chain id.
     * @param client The Api client for connection with KAS.
     */
    public TokenHistory(String chainId, ApiClient client) {
        setChainId(chainId);
        setApiClient(client);
    }

    /**
     * Gets transfer history list.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * int preset = 1;
     * PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset);
     * }
     * </pre>
     *
     * @param preset Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistory(int preset) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistory(preset, options);
    }

    /**
     * Gets transfer history list.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * List presets = Arrays.asList(151, 153);
     * PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(presets);
     * }
     * </pre>
     *
     * @param presets Preset IDs to be used for search. Preset ID can be checked in KAS console.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistory(List<Integer> presets) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistory(presets, options);
    }

    /**
     * Gets transfer history list.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * int preset = 1;
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     *
     * PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(preset, options);
     * }
     * </pre>
     *
     * @param preset Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistory(int preset, TokenHistoryQueryOptions options) throws ApiException {
        return getTransferHistory(Arrays.asList(preset), options);
    }

    /**
     * Gets transfer history list.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * List presets = Arrays.asList(151, 153);
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     *
     * PageableTransfers transfersData = caver.kas.tokenHistory.getTransferHistory(presets, options);
     * }
     * </pre>
     *
     * @param presets Preset IDs to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistory(List<Integer> presets, TokenHistoryQueryOptions options) throws ApiException {
        String isExclude_zero_klay = Optional.ofNullable(options.getExcludeZeroKlay()).map(value -> Boolean.toString(value)).orElseGet(()-> null);

        return this.tokenHistoryApi.getTransfers(chainId, KASUtils.parameterToString(presets), options.getKind(), options.getRange(), options.getSize(), options.getCursor(), isExclude_zero_klay);
    }

    /**
     * Gets transfer history list asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * int preset = 1;
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAsync(preset, callback);
     * }
     * </pre>
     *
     * @param preset Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(int preset, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAsync(preset, options, callback);
    }

    /**
     * Gets transfer history list asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * List presets = Arrays.asList(151, 153);
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAsync(presets, callback);
     * }
     * </pre>
     *
     * @param presets Preset IDs to be used for search. Preset ID can be checked in KAS console.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(List<Integer> presets, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAsync(presets, options, callback);
    }

    /**
     * Gets transfer history list asynchronously.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * int preset = 1;
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAsync(preset, options, callback);
     * }
     * </pre>
     *
     * @param preset Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(int preset, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return getTransferHistoryAsync(Arrays.asList(preset), options, callback);
    }

    /**
     * Gets transfer history list asynchronously.<br>
     * GET /v2/transfer
     *
     * <pre>Example:
     * {@code
     * List presets = Arrays.asList(151, 153);
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAsync(presets, options, callback);
     * }
     * </pre>
     *
     * @param presets Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(List<Integer> presets, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        String isExclude_zero_klay = Optional.ofNullable(options.getExcludeZeroKlay()).map(value -> Boolean.toString(value)).orElseGet(()-> null);

        return this.tokenHistoryApi.getTransfersAsync(
                chainId,
                KASUtils.parameterToString(presets),
                options.getKind(),
                options.getRange(),
                options.getSize(),
                options.getCursor(),
                isExclude_zero_klay,
                callback
        );
    }

    /**
     * Gets token history list with a specific transaction hash.<br>
     * GET /v2/transfer/tx/{transaction-hash}
     *
     * <pre>Example:
     * {@code
     * String txHash = "0x{txHash}";
     * Transfers transfers = caver.kas.tokenHistory.getTransferHistoryByTxHash(txHash);
     * }
     * </pre>
     *
     * @param txHash A transaction hash to get token history
     * @return Transfers
     * @throws ApiException
     */
    public Transfers getTransferHistoryByTxHash(String txHash) throws ApiException {
        return tokenHistoryApi.getTransfersByTxHash(chainId, txHash);
    }

    /**
     * Gets token history list with a specific transaction hash asynchronously.<br>
     * GET /v2/transfer/tx/{transaction-hash}
     *
     * <pre>Example:
     * {@code
     * String txHash = "0x{txHash}";
     *
     * ApiCallback<Transfers> callback = new ApiCallback<Transfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryByTxHashAsync(txHash, callback);
     * }
     * </pre>
     *
     * @param txHash A transaction hash to get token history
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryByTxHashAsync(String txHash, ApiCallback<Transfers> callback) throws ApiException {
        return tokenHistoryApi.getTransfersByTxHashAsync(chainId, txHash, callback);
    }

    /**
     * Gets token history list with a specific EOA.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer/account/{address}
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(address);
     * }
     * </pre>
     *
     * @param address The EOA address used to search for token transfer history.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistoryByAccount(String address) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryByAccount(address, options);
    }

    /**
     * Gets token history list with a specific EOA.<br>
     * GET /v2/transfer/account/{address}
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String contractAddress = "0x{contractAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     * options.setCaFilter(contractAddress);
     * options.setSize((long)3);
     * options.setRange("1584573000", "1584583388");
     *
     * PageableTransfers transfers = caver.kas.tokenHistory.getTransferHistoryByAccount(address, options);
     * }
     * </pre>
     *
     * @param address The EOA address used to search for token transfer history.
     * @param options Filters required when retrieving data. `kind`, `caFilter`, `range`, `size`, and `cursor`.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistoryByAccount(String address, TokenHistoryQueryOptions options) throws ApiException {
        String isExclude_zero_klay = Optional.ofNullable(options.getExcludeZeroKlay()).map(value -> Boolean.toString(value)).orElseGet(()-> null);
        String fromOnly = Optional.ofNullable(options.getFromOnly()).map(value -> Boolean.toString(value)).orElseGet(()-> null);
        String toOnly = Optional.ofNullable(options.getToOnly()).map(value -> Boolean.toString(value)).orElseGet(()-> null);

        return tokenHistoryApi.getTransfersByEoa(
                chainId,
                address,
                options.getKind(),
                options.getCaFilter(),
                options.getRange(),
                options.getSize(),
                options.getCursor(),
                isExclude_zero_klay,
                fromOnly,
                toOnly
        );
    }

    /**
     * Gets token history list with a specific EOA asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer/account/{address}
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAccountAsync(address, callback);
     * }
     * </pre>
     *
     * @param address The EOA address used to search for token transfer history.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAccountAsync(String address, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAccountAsync(address, options, callback);
    }

    /**
     * Gets token history list with a specific EOA asynchronously.<br>
     * GET /v2/transfer/account/{address}
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.KLAY);
     *
     * ApiCallback<PageableTransfers> callback = new ApiCallback<PageableTransfers>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getTransferHistoryAccountAsync(address, options, callback);
     * }
     * </pre>
     *
     * @param address The EOA address used to search for token transfer history.
     * @param options Filters required when retrieving data. `kind`, `caFilter`, `range`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAccountAsync(String address, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        String isExclude_zero_klay = Optional.ofNullable(options.getExcludeZeroKlay()).map(value -> Boolean.toString(value)).orElseGet(()-> null);
        String fromOnly = Optional.ofNullable(options.getFromOnly()).map(value -> Boolean.toString(value)).orElseGet(()-> null);
        String toOnly = Optional.ofNullable(options.getToOnly()).map(value -> Boolean.toString(value)).orElseGet(()-> null);

        return tokenHistoryApi.getTransfersByEoaAsync(chainId,
                address,
                options.getKind(),
                options.getCaFilter(),
                options.getRange(),
                options.getSize(),
                options.getCursor(),
                isExclude_zero_klay,
                fromOnly,
                toOnly,
                callback);
    }

    /**
     * Retrieve information of all labeled FT contracts.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/ft
     *
     * <pre>Example:
     * {@code
     * PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
     * }
     * </pre>
     *
     * @return PageableFtContractDetails
     * @throws ApiException
     */
    public PageableFtContractDetails getFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTContractList(options);
    }

    /**
     * Retrieve information of all labeled FT contracts.<br>
     * GET /v2/contract/ft
     *
     * <pre>Example:
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.COMPLETED);
     * options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP7);
     *
     * PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @return PageableFtContractDetails
     * @throws ApiException
     */
    public PageableFtContractDetails getFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListofFtContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    /**
     * Retrieve information of all labeled FT contracts asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/ft
     *
     * <pre>Example:
     * {@code
     * ApiCallback<PageableFtContractDetails> callback = new ApiCallback<PageableFtContractDetails>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getFTContractListAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFTContractListAsync(ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTContractListAsync(options, callback);
    }

    /**
     * Retrieve information of all labeled FT contracts asynchronously.<br>
     * GET /v2/contract/ft
     *
     * <pre>Example:
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.COMPLETED);
     * options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP7);
     *
     * ApiCallback<PageableFtContractDetails> callback = new ApiCallback<PageableFtContractDetails>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getFTContractListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFTContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        return tokenContractApi.getListofFtContractsAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieves the information of the FT contract labeled with the address of the FT contract.<br>
     * GET /v2/contract/ft/{ft-address}
     *
     * <pre>Example:
     * {@code
     * String ftAddress = "0x{ftAddress}";
     * FtContractDetail contract = caver.kas.tokenHistory.getFTContract(ftAddress);
     * }
     * </pre>
     *
     * @param ftAddress The FT contract address to retrieve contract information.
     * @return FtContractDetail
     * @throws ApiException
     */
    public FtContractDetail getFTContract(String ftAddress) throws ApiException {
        return tokenContractApi.getFtContractDetail(chainId, ftAddress);
    }

    /**
     * Retrieves the information of the FT contract labeled with the address of the FT contract asynchronously.<br>
     * GET /v2/contract/ft/{ft-address}
     *
     * <pre>Example:
     * {@code
     * String ftAddress = "0x{ftAddress}";
     *
     * ApiCallback<FtContractDetail> callback = new ApiCallback<FtContractDetail>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getFTContract(ftAddress, callback);
     * }
     * </pre>
     *
     * @param ftAddress The FT contract address to retrieve contract information.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFTContractAsync(String ftAddress, ApiCallback<FtContractDetail> callback) throws ApiException {
        return tokenContractApi.getFtContractDetailAsync(chainId, ftAddress, callback);
    }

    /**
     * Retrieve information of all labeled NFT contracts.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft
     *
     * <pre>Example:
     * {@code
     * PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList();
     * }
     * </pre>
     *
     * @return PageableNftContractDetails
     * @throws ApiException
     */
    public PageableNftContractDetails getNFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTContractList(options);
    }

    /**
     * Retrieve information of all labeled NFT contracts.<br>
     * GET /v2/contract/nft
     *
     * <pre>Example:
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.PROCESSING);
     * options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP17);
     *
     * PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @return PageableNftContractDetails
     * @throws ApiException
     */
    public PageableNftContractDetails getNFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListOfNftContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    /**
     * Retrieve information of all labeled NFT contracts asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft
     *
     * <pre>Example:
     * {@code
     * ApiCallback<PageableNftContractDetails> callback = new ApiCallback<PageableNftContractDetails>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTContractListAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTContractListAsync(ApiCallback<PageableNftContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTContractListAsync(options, callback);
    }

    /**
     * Retrieve information of all labeled NFT contracts asynchronously.<br>
     * GET /v2/contract/nft
     *
     * <pre>Example:
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setStatus(TokenHistoryQueryOptions.LABEL_STATUS.PROCESSING);
     * options.setType(TokenHistoryQueryOptions.CONTRACT_TYPE.KIP17;
     *
     * ApiCallback<PageableNftContractDetails> callback = new ApiCallback<PageableNftContractDetails>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTContractListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableNftContractDetails> callback) throws ApiException {
        return tokenContractApi.getListOfNftContractsAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieves the information of the NFT contract labeled with the address of the NFT contract.<br>
     * GET /v2/contract/nft/{nftAddress}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * NftContractDetail detail = caver.kas.tokenHistory.getNFTContract(nftAddress);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to retrieve contract information.
     * @return NftContractDetail
     * @throws ApiException
     */
    public NftContractDetail getNFTContract(String nftAddress) throws ApiException {
        return tokenContractApi.getNftContractDetail(chainId, nftAddress);
    }

    /**
     * Retrieves the information of the NFT contract labeled with the address of the NFT contract asynchronously.<br>
     * GET /v2/contract/nft/{nftAddress}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     *
     * ApiCallback<NftContractDetail> callback = new ApiCallback<NftContractDetail>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTContractAsync(nftAddress, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to retrieve contract information.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTContractAsync(String nftAddress, ApiCallback<NftContractDetail> callback) throws ApiException {
        return tokenContractApi.getNftContractDetailAsync(chainId, nftAddress, callback);
    }

    /**
     * Retrieves information of all NFTs issued by a specific NFT contract.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/token
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * PageableNfts nfts = caver.kas.tokenHistory.getNFTList(nftAddress);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to search issued NFTs.
     * @return PageableNfts
     * @throws ApiException
     */
    public PageableNfts getNFTList(String nftAddress) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTList(nftAddress, options);
    }

    /**
     * Retrieves information of all NFTs issued by a specific NFT contract.<br>
     * GET /v2/contract/nft/{nft-address}/token
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)3);
     *
     * PageableNfts nfts = caver.kas.tokenHistory.getNFTList(nftAddress, options);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to search issued NFTs.
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @return PageableNfts
     * @throws ApiException
     */
    public PageableNfts getNFTList(String nftAddress, TokenHistoryQueryOptions options) throws ApiException {
        return tokenApi.getNftsByContractAddress(chainId, nftAddress, options.getSize(), options.getCursor());
    }

    /**
     * Retrieves information of all NFTs issued by a specific NFT contract asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/token
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     *
     * ApiCallback<PageableNfts> callback = new ApiCallback<PageableNfts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTListAsync(nftAddress, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to search issued NFTs.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTListAsync(String nftAddress, ApiCallback<PageableNfts> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListAsync(nftAddress, options, callback);
    }

    /**
     * Retrieves information of all NFTs issued by a specific NFT contract asynchronously.<br>
     * GET /v2/contract/nft/{nft-address}/token
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)3);
     *
     * ApiCallback<PageableNfts> callback = new ApiCallback<PageableNfts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTListAsync(nftAddress, options, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to search issued NFTs.
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTListAsync(String nftAddress, TokenHistoryQueryOptions options, ApiCallback<PageableNfts> callback) throws ApiException {
        return tokenApi.getNftsByContractAddressAsync(chainId, nftAddress, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Among the NFTs issued from the NFT contract address,<br>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/owner/{owner-address}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * PageableNfts nfts = caver.kas.tokenHistory.getNFTListByOwner(nftAddress, ownerAddress);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched issued NFTs
     * @param ownerAddress The EOA address to be searched NFTs
     * @return PageableNfts
     * @throws ApiException
     */
    public PageableNfts getNFTListByOwner(String nftAddress, String ownerAddress) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListByOwner(nftAddress, ownerAddress, options);
    }

    /**
     * Among the NFTs issued from the NFT contract address,<br>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved.<br>
     * GET /v2/contract/nft/{nft-address}/owner/{owner-address}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)3);
     *
     * PageableNfts nfts = caver.kas.tokenHistory.getNFTListByOwner(nftAddress, ownerAddress);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched issued NFTs
     * @param ownerAddress The EOA address to be searched NFTs
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @return PageableNfts
     * @throws ApiException
     */
    public PageableNfts getNFTListByOwner(String nftAddress, String ownerAddress, TokenHistoryQueryOptions options) throws ApiException {
        return tokenApi.getNftsByOwnerAddress(chainId, nftAddress, ownerAddress, options.getSize(), options.getCursor());
    }

    /**
     * Among the NFTs issued from the NFT contract address.<br>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/owner/{owner-address}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * ApiCallback<PageableNfts> callback = new ApiCallback<PageableNfts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTListByOwnerAsync(nftAddress, ownerAddress, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched issued NFTs
     * @param ownerAddress The EOA address to be searched NFTs
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTListByOwnerAsync(String nftAddress, String ownerAddress, ApiCallback<PageableNfts> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListByOwnerAsync(nftAddress, ownerAddress, options, callback);
    }

    /**
     * Among the NFTs issued from the NFT contract address,<br>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved asynchronously.<br>
     * GET /v2/contract/nft/{nft-address}/owner/{owner-address}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)3);
     *
     * ApiCallback<PageableNfts> callback = new ApiCallback<PageableNfts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTListByOwnerAsync(nftAddress, ownerAddress, options, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched issued NFTs
     * @param ownerAddress The EOA address to be searched NFTs
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTListByOwnerAsync(String nftAddress, String ownerAddress, TokenHistoryQueryOptions options, ApiCallback<PageableNfts> callback) throws ApiException {
        return tokenApi.getNftsByOwnerAddressAsync(chainId, nftAddress, ownerAddress, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieve information of a specific NFT.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * Nft nft = caver.kas.tokenHistory.getNFT(nftAddress, tokenId);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @return Nft
     * @throws ApiException
     */
    public Nft getNFT(String nftAddress, String tokenId) throws ApiException {
        return tokenApi.getNftById(chainId, nftAddress, tokenId);
    }

    /**
     * Retrieve information of a specific NFT asynchronously.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * ApiCallback<Nft> callback = new ApiCallback<Nft>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTAsync(nftAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTAsync(String nftAddress, String tokenId, ApiCallback<Nft> callback) throws ApiException {
        return tokenApi.getNftByIdAsync(chainId, nftAddress, tokenId, callback);
    }

    /**
     * Retrieve the record of ownership changes for a specific NFT.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}/history
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * PageableNftOwnershipChanges ownershipChanges = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @return PageableNftOwnershipChanges
     * @throws ApiException
     */
    public PageableNftOwnershipChanges getNFTOwnershipHistory(String nftAddress, String tokenId) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTOwnershipHistory(nftAddress, tokenId, options);
    }

    /**
     * Retrieve the record of ownership changes for a specific NFT.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}/history
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)1);
     *
     * PageableNftOwnershipChanges ownershipChanges = caver.kas.tokenHistory.getNFTOwnershipHistory(nftAddress, tokenId, options);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @return PageableNftOwnershipChanges
     * @throws ApiException
     */
    public PageableNftOwnershipChanges getNFTOwnershipHistory(String nftAddress, String tokenId, TokenHistoryQueryOptions options) throws ApiException {
        return tokenOwnershipApi.getListOfNftOwnershipChanges(chainId, nftAddress, tokenId, options.getSize(), options.getCursor());
    }

    /**
     * Retrieve the record of ownership changes for a specific NFT asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}/history
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * ApiCallback<PageableNftOwnershipChanges> callback = new ApiCallback<PageableNftOwnershipChanges>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTOwnershipHistoryAsync(nftAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTOwnershipHistoryAsync(String nftAddress, String tokenId, ApiCallback<PageableNftOwnershipChanges> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTOwnershipHistoryAsync(nftAddress, tokenId, options, callback);
    }

    /**
     * Retrieve the record of ownership changes for a specific NFT asynchronously.<br>
     * GET /v2/contract/nft/{nft-address}/token/{token-id}/history
     *
     * <pre>Example:
     * {@code
     * String nftAddress = "0x{nftAddress}";
     * String tokenId = "0x{tokenId}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize((long)1);
     *
     * ApiCallback<PageableNftOwnershipChanges> callback = new ApiCallback<PageableNftOwnershipChanges>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.tokenHistory.getNFTOwnershipHistoryAsync(nftAddress, tokenId, options, callback);
     * }
     * </pre>
     *
     * @param nftAddress The NFT contract address to be searched.
     * @param tokenId The NFT id to be searched.
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getNFTOwnershipHistoryAsync(String nftAddress, String tokenId, TokenHistoryQueryOptions options, ApiCallback<PageableNftOwnershipChanges> callback) throws ApiException {
        return tokenOwnershipApi.getListOfNftOwnershipChangesAsync(chainId, nftAddress, tokenId, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieve MTs(Multiple Token) that are owned by the passed as ownerAddress.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTListByOwner(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}
     *
     * <pre>Example:
     * {@code
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * PageableMtTokensWithBalance result = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address to retrieve MTs.
     * @return PageableMtTokensWithBalance
     * @throws ApiException
     */
    public PageableMtTokensWithBalance getMTListByOwner(String mtAddress, String ownerAddress) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTListByOwner(mtAddress, ownerAddress, options);
    }

    /**
     * Retrieve MTs(Multiple Token) that are owned by the passed as ownerAddress.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * PageableMtTokensWithBalance result = caver.kas.tokenHistory.getMTListByOwner(mtAddress, account, options);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address to retrieve MTs.
     * @param options  Filters required when retrieving data. `size`, `cursor`.
     * @return PageableMtTokensWithBalance
     * @throws ApiException
     */
    public PageableMtTokensWithBalance getMTListByOwner(String mtAddress, String ownerAddress, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndOwnerAddress(chainId, mtAddress, ownerAddress, options.getCursor(), options.getSize());
    }

    /**
     * Retrieve MTs(Multiple Token) that are owned by the passed as ownerAddress asynchronously.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTListByOwnerAsync(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokensWithBalance> callback = new ApiCallback<PageableMtTokensWithBalance>() {
     *     ....implement callback method.
     * };
     *
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     *
     * caver.kas.tokenHistory.getMTListByOwnerAsync(mtAddress, account, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address to retrieve MTs.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTListByOwnerAsync(String mtAddress, String ownerAddress, ApiCallback<PageableMtTokensWithBalance> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTListByOwnerAsync(mtAddress, ownerAddress, options, callback);
    }

    /**
     * Retrieve MTs(Multiple Token) that are owned by the passed as ownerAddress asynchronously.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokensWithBalance> callback = new ApiCallback<PageableMtTokensWithBalance>() {
     *     ....implement callback method.
     * };
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     *
     * caver.kas.tokenHistory.getMTListByOwnerAsync(mtAddress, account, options, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address to retrieve MTs.
     * @param options  Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTListByOwnerAsync(String mtAddress, String ownerAddress, TokenHistoryQueryOptions options, ApiCallback<PageableMtTokensWithBalance> callback) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndOwnerAddressAsync(chainId, mtAddress, ownerAddress, options.getCursor(), options.getSize(), callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) corresponding to the given address and tokenID.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * MtToken token = caver.kas.tokenHistory.getMT(mtAddress, account, tokenId);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address.
     * @param tokenID The token id.
     * @return MtToken
     * @throws ApiException
     */
    public MtToken getMT(String mtAddress, String ownerAddress, BigInteger tokenID) throws ApiException {
        return getMT(mtAddress, ownerAddress, Numeric.toHexStringWithPrefix(tokenID));

    }

    /**
     * Retrieve a specific MT(Multiple Token) corresponding to the given address and tokenID.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * String tokenId = "0x1";
     *
     * MtToken token = caver.kas.tokenHistory.getMT(mtAddress, account, tokenId);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address.
     * @param tokenID The token id.
     * @return MtToken
     * @throws ApiException
     */
    public MtToken getMT(String mtAddress, String ownerAddress, String tokenID) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndOwnerAddressAndTokenId(chainId, mtAddress, ownerAddress, tokenID);
    }

    /**
     * Retrieve a specific MT(Multiple Token) corresponding to the given address and tokenID asynchronously.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<MtToken> callback = new ApiCallback<MtToken>() {
     *    ....implement callback method.
     * };
     *
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.tokenHistory.getMTAsync(mtAddress, account, tokenId, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address.
     * @param tokenID The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTAsync(String mtAddress, String ownerAddress, BigInteger tokenID, ApiCallback<MtToken> callback) throws ApiException {
        return getMTAsync(mtAddress, ownerAddress, Numeric.toHexStringWithPrefix(tokenID), callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) corresponding to the given address and tokenID asynchronously.<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<MtToken> callback = new ApiCallback<MtToken>() {
     *    ....implement callback method.
     * };
     *
     * String mtAddress = "0x{mtAddress}";
     * String account = "0x{accountAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.tokenHistory.getMTAsync(mtAddress, account, tokenId, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param ownerAddress The owner address.
     * @param tokenID The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTAsync(String mtAddress, String ownerAddress, String tokenID, ApiCallback<MtToken> callback) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndOwnerAddressAndTokenIdAsync(chainId, mtAddress, ownerAddress, tokenID, callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTOwnerByTokenId(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String mtAddress = "0x{mtAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, tokenId);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @return PageableMtTokens
     * @throws ApiException
     */
    public PageableMtTokens getMTOwnerListByTokenId(String mtAddress, BigInteger tokenId) throws ApiException {
        return getMTOwnerListByTokenId(mtAddress, Numeric.toHexStringWithPrefix(tokenId));
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTOwnerByTokenId(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String mtAddress = "0x{mtAddress}";
     * String tokenId = "0x1";
     *
     * PageableMtTokens pageableMtTokens = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, tokenId);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @return PageableMtTokens
     * @throws ApiException
     */
    public PageableMtTokens getMTOwnerListByTokenId(String mtAddress, String tokenId) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTOwnerListByTokenId(mtAddress, tokenId, options);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * PageableMtTokens response = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, tokenId, options);
     *
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return PageableMtTokens
     * @throws ApiException
     */
    public PageableMtTokens getMTOwnerListByTokenId(String mtAddress, BigInteger tokenId, TokenHistoryQueryOptions options) throws ApiException {
        return getMTOwnerListByTokenId(mtAddress, Numeric.toHexStringWithPrefix(tokenId), options);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * String tokenId = "0x1";
     *
     * PageableMtTokens response = caver.kas.tokenHistory.getMTOwnerListByTokenId(mtAddress, tokenId, options);
     *
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return PageableMtTokens
     * @throws ApiException
     */
    public PageableMtTokens getMTOwnerListByTokenId(String mtAddress, String tokenId, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndTokenId(chainId, mtAddress, tokenId, options.getCursor(), options.getSize());
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID asynchronously.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTOwnerByTokenId(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokens> callback = new ApiCallback<PageableMtTokens> () {
     *    ....implements callback method.
     * }
     *
     * String mtAddress = "0x{mtAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTOwnerListByTokenIdAsync(String mtAddress, BigInteger tokenId, ApiCallback<PageableMtTokens> callback) throws ApiException {
        return getMTOwnerListByTokenIdAsync(mtAddress, Numeric.toHexStringWithPrefix(tokenId), callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID asynchronously.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTOwnerByTokenId(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokens> callback = new ApiCallback<PageableMtTokens> () {
     *    ....implements callback method.
     * }
     *
     * String mtAddress = "0x{mtAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTOwnerListByTokenIdAsync(String mtAddress, String tokenId, ApiCallback<PageableMtTokens> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTOwnerListByTokenIdAsync(mtAddress, tokenId, options, callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID asynchronously.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokens> callback = new ApiCallback<PageableMtTokens> () {
     *    ....implements callback method.
     * }
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, tokenId, options, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTOwnerListByTokenIdAsync(String mtAddress, BigInteger tokenId, TokenHistoryQueryOptions options, ApiCallback<PageableMtTokens> callback) throws ApiException {
        return getMTOwnerListByTokenIdAsync(mtAddress, Numeric.toHexStringWithPrefix(tokenId), options, callback);
    }

    /**
     * Retrieve a specific MT(Multiple Token) owner corresponding to the given tokenID asynchronously.<br>
     * You can set a search options(size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt/{mt-address}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtTokens> callback = new ApiCallback<PageableMtTokens> () {
     *    ....implements callback method.
     * }
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String mtAddress = "0x{mtAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.tokenHistory.getMTOwnerListByTokenIdAsync(mtAddress, tokenId, options, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTOwnerListByTokenIdAsync(String mtAddress, String tokenId, TokenHistoryQueryOptions options, ApiCallback<PageableMtTokens> callback) throws ApiException {
        return this.tokenApi.getMtTokensByContractAddressAndTokenIdAsync(chainId, mtAddress, tokenId, options.getCursor(), options.getSize(), callback);
    }

    /**
     * Retrieve information of all labeled MT(Multiple Token) contracts.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(status, type, size, cursor), use getMTContractList(TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt
     *
     * <pre>Example :
     * {@code
     * PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList();
     * }
     * </pre>
     *
     * @return PageableMtContractDetails
     * @throws ApiException
     */
    public PageableMtContractDetails getMTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTContractList(options);
    }

    /**
     * Retrieve information of all labeled MT(Multiple Token) contracts.<br>
     * You can set a search options(status, type, size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt
     *
     * <pre>Example :
     * {@code
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     *
     * PageableMtContractDetails result = caver.kas.tokenHistory.getMTContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, `cursor`.
     * @return PageableMtContractDetails
     * @throws ApiException
     */
    public PageableMtContractDetails getMTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenContractApi.getListOfMtContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    /**
     * Retrieve information of all labeled MT(Multiple Token) contracts asynchronously.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(status, type, size, cursor), use getMTContractList(TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtContractDetails> callback = ApiCallback<PageableMtContractDetails>() {
     *    ....implements callback method.
     * };
     *
     * caver.kas.tokenHistory.getMTContractListAsync(callback);
     *
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTContractListAsync(ApiCallback<PageableMtContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getMTContractListAsync(options, callback);
    }

    /**
     * Retrieve information of all labeled MT(Multiple Token) contracts asynchronously.<br>
     * You can set a search options(status, type, size, cursor) by using TokenHistoryQueryOptions.<br>
     * GET /v2/contract/mt
     *
     * <pre>Example :
     * {@code
     * ApiCallback<PageableMtContractDetails> callback = ApiCallback<PageableMtContractDetails>() {
     *    ....implements callback method.
     * };
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setSize(1);
     *
     * caver.kas.tokenHistory.getMTContractListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `status`, `type`, `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableMtContractDetails> callback) throws ApiException {
        return this.tokenContractApi.getListOfMtContractsAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieves the information of the FT contract labeled with the address of the MT(Multiple Token) contract.<br>
     * GET /v2/contract/mt/{mt-address}
     *
     * <pre>Example :
     * {@code
     * String mtAddress = 0x{mtAddress};
     * MtContractDetail detail = caver.kas.tokenHistory.getMTContract(mtAddress);
     * }</pre>
     *
     * @param mtAddress The MT contract address.
     * @return MtContractDetail
     * @throws ApiException
     */
    public MtContractDetail getMTContract(String mtAddress) throws ApiException {
        return this.tokenContractApi.getMtContractDetail(chainId, mtAddress);
    }

    /**
     * Retrieves the information of the FT contract labeled with the address of the MT(Multiple Token) contract asynchronously.<br>
     * GET /v2/contract/mt/{mt-address}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<MtContractDetail> callback = new ApiCallback<MtContractDetail>() {
     *     ...implement callback method.
     * }
     *
     * String mtAddress = 0x{mtAddress};
     * caver.kas.tokenHistory.getMTContractAsync(mtAddress, callback);
     * }
     * </pre>
     *
     * @param mtAddress The MT contract address.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTContractAsync(String mtAddress, ApiCallback<MtContractDetail> callback) throws ApiException {
        return this.tokenContractApi.getMtContractDetailAsync(chainId, mtAddress, callback);
    }

    /**
     * Selecting an EOA will fetch data of all contracts of tokens by EOA.<br>
     * GET /v2/account/{address}/contract
     *
     * <pre>Example
     * {@code
     * String ownerAddress = "0x{ownerAddress}";
     * PageableContractSummary res = caver.kas.tokenHistory.getContractListByOwner(account);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @return PageableContractSummary
     * @throws ApiException
     */
    public PageableContractSummary getContractListByOwner(String ownerAddress) throws ApiException {
        return getContractListByOwner(ownerAddress, new TokenHistoryQueryOptions());
    }

    /**
     * Selecting an EOA will fetch data of all contracts of tokens by EOA.<br>
     * GET /v2/account/{address}/contract
     *
     * <pre>Example
     * {@code
     * String ownerAddress = "0x{ownerAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     * options.setSize((long)2);
     * .....
     *
     * PageableContractSummary res = caver.kas.tokenHistory.getContractListByOwner(account, options);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param options Filters required when retrieving data. `kind`, `size`, `cursor`
     * @return PageableContractSummary
     * @throws ApiException
     */
    public PageableContractSummary getContractListByOwner(String ownerAddress, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenOwnershipApi.getListOfContractByOwnerAddress(chainId, ownerAddress, options.getKind(), options.getSize(), options.getCursor());
    }

    /**
     * Selecting an EOA will fetch data of all contracts of tokens by EOA asynchronously.<br>
     * GET /v2/account/{address}/contract
     *
     * <pre>Example
     * {@code
     * ApiCallback<PageableContractSummary> callback = new ApiCallback<PageableContractSummary>() {
     *     ...implement callback method.
     * }
     *
     * String ownerAddress = "0x{ownerAddress}";
     * PageableContractSummary res = caver.kas.tokenHistory.getContractListByOwnerAsync(account, callback);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractListByOwnerAsync(String ownerAddress, ApiCallback<PageableContractSummary> callback) throws ApiException {
        return getContractListByOwnerAsync(ownerAddress, new TokenHistoryQueryOptions(), callback);
    }

    /**
     * Selecting an EOA will fetch data of all contracts of tokens by EOA asynchronously.<br>
     * GET /v2/account/{address}/contract
     *
     * <pre>Example
     * {@code
     * ApiCallback<PageableContractSummary> callback = new ApiCallback<PageableContractSummary>() {
     *     ...implement callback method.
     * }
     *
     * String ownerAddress = "0x{ownerAddress}";
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     * options.setSize((long)2);
     * .....
     *
     * PageableContractSummary res = caver.kas.tokenHistory.getContractListByOwnerAsync(account, options, callback);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param options Filters required when retrieving data. `kind`, `size`, `cursor`
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getContractListByOwnerAsync(String ownerAddress, TokenHistoryQueryOptions options, ApiCallback<PageableContractSummary> callback) throws ApiException {
        return this.tokenOwnershipApi.getListOfContractByOwnerAddressAsync(chainId, ownerAddress, options.getKind(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Selecting a EOA will get all token data by EOA.<br>
     * GET /v2/account/{address}/token
     *
     * <pre>Example
     * {@code
     * String ownerAddress = "0x{ownerAddress}";
     * PageableTokenSummary res = caver.kas.tokenHistory.getTokenListByOwner(account);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @return PageableTokenSummary
     * @throws ApiException
     */
    public PageableTokenSummary getTokenListByOwner(String ownerAddress) throws ApiException {
        return getTokenListByOwner(ownerAddress, new TokenHistoryQueryOptions());
    }

    /**
     * Selecting a EOA will get all token data by EOA.<br>
     * GET /v2/account/{address}/token
     *
     * <pre>Example
     * {@code
     *
     * TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
     * options.setKind(TokenHistoryQueryOptions.KIND.FT);
     * options.setSize((long)2);
     * .....
     *
     * String ownerAddress = "0x{ownerAddress}";
     * PageableTokenSummary res = caver.kas.tokenHistory.getTokenListByOwner(account, options);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param options Filters required when retrieving data. `kind`, `size`, `ca-filters`, `cursor`
     * @return PageableTokenSummary
     * @throws ApiException
     */
    public PageableTokenSummary getTokenListByOwner(String ownerAddress, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenOwnershipApi.getListOfTokenByOwnerAddress(chainId, ownerAddress, options.getKind(), options.getSize(), options.getCaFilter(), options.getCursor());
    }

    /**
     * Selecting a EOA will get all token data by EOA asynchronously.<br>
     * GET /v2/account/{address}/token
     *
     * <pre>Example
     * {@code
     * ApiCallback<PageableTokenSummary> callback = new ApiCallback<PageableTokenSummary>() {
     *     ...implement callback method.
     * }
     *
     * String ownerAddress = "0x{ownerAddress}";
     * PageableTokenSummary res = caver.kas.tokenHistory.getTokenListByOwnerAsync(account, callback);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String ownerAddress, ApiCallback<PageableTokenSummary> callback) throws ApiException {
        return getTokenListByOwnerAsync(ownerAddress, new TokenHistoryQueryOptions(), callback);
    }

    /**
     * Selecting a EOA will get all token data by EOA asynchronously.<br>
     * GET /v2/account/{address}/token
     *
     * <pre>Example
     * {@code
     * ApiCallback<PageableTokenSummary> callback = new ApiCallback<PageableTokenSummary>() {
     *     ...implement callback method.
     * }
     *
     * String ownerAddress = "0x{ownerAddress}";
     * PageableTokenSummary res = caver.kas.tokenHistory.getTokenListByOwnerAsync(account, callback);
     * }
     * </pre>
     *
     * @param ownerAddress The EOA to query.
     * @param options Filters required when retrieving data. `kind`, `size`, `ca-filters`, `cursor`
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String ownerAddress, TokenHistoryQueryOptions options, ApiCallback<PageableTokenSummary> callback) throws ApiException {
        return this.tokenOwnershipApi.getListOfTokenByOwnerAddressAsync(chainId, ownerAddress, options.getKind(), options.getSize(), options.getCaFilter(), options.getCursor(), callback);
    }

    /**
     * Getter function for tokenApi
     * @return TokenApi
     */
    public TokenApi getTokenApi() {
        return tokenApi;
    }

    /**
     * Getter function TokenContractApi
     * @return TokenContractApi
     */
    public TokenContractApi getTokenContractApi() {
        return tokenContractApi;
    }

    /**
     * Getter function for TokenHistoryApi
     * @return TokenHistoryApi
     */
    public TokenHistoryApi getTokenHistoryApi() {
        return tokenHistoryApi;
    }

    /**
     * Getter function for TokenOwnershipApi
     * @return TokenOwnershipApi
     */
    public TokenOwnershipApi getTokenOwnershipApi() {
        return tokenOwnershipApi;
    }

    /**
     * Getter function for chain id
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * Getter function for ApiClient
     * @return ApiClient
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Setter function for TokenApi
     * @param tokenApi Token API rest-client object.
     */
    public void setTokenApi(TokenApi tokenApi) {
        this.tokenApi = tokenApi;
    }

    /**
     * Setter function for TokenContractApi
     * @param tokenContractApi Token contract API rest-client object.
     */
    public void setTokenContractApi(TokenContractApi tokenContractApi) {
        this.tokenContractApi = tokenContractApi;
    }

    /**
     * Setter function for TokenHistoryApi
     * @param tokenHistoryApi Token history API rest-client object.
     */
    public void setTokenHistoryApi(TokenHistoryApi tokenHistoryApi) {
        this.tokenHistoryApi = tokenHistoryApi;
    }

    /**
     * Setter function for TokenOwnershipApi
     * @param tokenOwnershipApi Token owner API rest-client object.
     */
    public void setTokenOwnershipApi(TokenOwnershipApi tokenOwnershipApi) {
        this.tokenOwnershipApi = tokenOwnershipApi;
    }

    /**
     * Setter function for chain id
     * @param chainId The klaytn network chain id.
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Setter function for apiClient
     * @param apiClient The ApiClient for connecting with KAS.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setTokenApi(new TokenApi(apiClient));
        setTokenContractApi(new TokenContractApi(apiClient));
        setTokenHistoryApi(new TokenHistoryApi(apiClient));
        setTokenOwnershipApi(new TokenOwnershipApi(apiClient));
    }
}
