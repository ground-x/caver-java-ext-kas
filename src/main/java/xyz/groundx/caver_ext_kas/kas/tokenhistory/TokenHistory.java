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
import xyz.groundx.caver_ext_kas.kas.utils.KASUtils;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.*;

import java.util.Arrays;
import java.util.List;

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
     * Creates an TokenHistoryAPI instance.
     * @param chainId A Klaytn network chain id.
     * @param client The Api client for connection with KAS.
     */
    public TokenHistory(String chainId, ApiClient client) {
        this.chainId = chainId;
        tokenApi = new TokenApi(client);
        tokenContractApi = new TokenContractApi(client);
        tokenHistoryApi = new TokenHistoryApi(client);
        tokenOwnershipApi = new TokenOwnershipApi(client);
    }

    /**
     * Gets transfer history list.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
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
     * @param presets Preset IDs to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistory(List<Integer> presets, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenHistoryApi.getTransfers(chainId, KASUtils.parameterToString(presets), options.getKind(), options.getRange(), options.getSize(), options.getCursor());
    }

    /**
     * Gets transfer history list asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer
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
     * @param presets Preset ID to be used for search. Preset ID can be checked in KAS console.
     * @param options Filters required when retrieving data. `kind`, `range`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(List<Integer> presets, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return this.tokenHistoryApi.getTransfersAsync(chainId, KASUtils.parameterToString(presets), options.getKind(), options.getRange(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Gets token history list with a specific transaction hash.<br>
     * GET /v2/transfer/tx/{transaction-hash}
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
     * @param address The EOA address used to search for token transfer history.
     * @param options Filters required when retrieving data. `kind`, `caFilter`, `range`, `size`, and `cursor`.
     * @return PageableTransfers
     * @throws ApiException
     */
    public PageableTransfers getTransferHistoryByAccount(String address, TokenHistoryQueryOptions options) throws ApiException {
        return tokenHistoryApi.getTransfersByEoa(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor());
    }

    /**
     * Gets token history list with a specific EOA asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/transfer/account/{address}
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
     * @param address The EOA address used to search for token transfer history.
     * @param options Filters required when retrieving data. `kind`, `caFilter`, `range`, `size`, and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAccountAsync(String address, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return tokenHistoryApi.getTransfersByEoaAsync(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieve information of all labeled FT contracts.<br>
     * It will send a request without filter options.<br>
     * GET /v2/contract/ft
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
     * Retrieve FT(Fungible Token) information owned EOA passed as a address parameter.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor, ca-filters), use getFTSummaryByAddress(String, TokenHistoryQueryOptions).<br>
     * GET /v2/account/token/{address}/ft
     * @param address The EOA Address.
     * @return PageableAccountFT
     * @throws ApiException
     */
    public PageableAccountFT getFTSummaryByAddress(String address) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTSummaryByAddress(address, options);
    }

    /**
     * Retrieve FT(Fungible Token) information owned EOA passed as a address parameter.<br>
     * You can set a search options(size, cursor, ca-filters) by using TokenHistoryQueryOptions.<br>
     * GET /v2/account/token/{address}/ft
     * @param address The EOA Address.
     * @param options Filters required when retrieving data. `size`, `cursor` and `ca-filters`
     * @return PageableAccountFT
     * @throws ApiException
     */
    public PageableAccountFT getFTSummaryByAddress(String address, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenOwnershipApi.getFtSummaryByEoaAddress(chainId, address, options.getSize(), options.getCursor(), options.getCaFilter());
    }

    /**
     * Retrieve FT(Fungible Token) information owned EOA passed as a address parameter asynchronously.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor, ca-filters), use getFTSummaryByAddressAsync(String, TokenHistoryQueryOptions).<br>
     * GET /v2/account/token/{address}/ft
     * @param address The EOA Address.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFTSummaryByAddressAsync(String address, ApiCallback<PageableAccountFT> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTSummaryByAddressAsync(address, options, callback);
    }

    /**
     * Retrieve FT(Fungible Token) information owned EOA passed as a address parameter asynchronously.<br>
     * You can set a search options(size, cursor, ca-filters) by using TokenHistoryQueryOptions.<br>
     * GET /v2/account/token/{address}/ft
     * @param address The EOA Address.
     * @param options Filters required when retrieving data. `size`, `cursor` and `ca-filters`
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFTSummaryByAddressAsync(String address, TokenHistoryQueryOptions options, ApiCallback<PageableAccountFT> callback) throws ApiException {
        return this.tokenOwnershipApi.getFtSummaryByEoaAddressAsync(chainId, address, options.getSize(), options.getCursor(), options.getCaFilter(), callback);
    }

    /**
     * Retrieve MTs(Multiple Token) that are owned by the passed as ownerAddress.<br>
     * It will send a request without filter options.<br>
     * If you want to execute this function with search options(size, cursor), use getMTListByOwner(String, TokenHistoryQueryOptions).<br>
     * GET /v2/contract/mt/{mt-address}/owner/{owner-address}
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
     * @param mtAddress The MT contract address.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMTContractAsync(String mtAddress, ApiCallback<MtContractDetail> callback) throws ApiException {
        return this.tokenContractApi.getMtContractDetailAsync(chainId, mtAddress, callback);
    }
}
