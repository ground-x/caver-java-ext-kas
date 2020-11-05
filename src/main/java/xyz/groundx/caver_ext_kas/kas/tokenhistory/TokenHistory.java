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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.TokenApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.TokenContractApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.TokenHistoryApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.api.TokenOwnershipApi;
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
     * Gets transfer history list.<p>
     * It will send a request without filter options.<p>
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
     * Gets transfer history list.<p>
     * It will send a request without filter options.<p>
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
     * Gets transfer history list.<p>
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
     * Gets transfer history list.<p>
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
     * Gets transfer history list asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Gets transfer history list asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Gets transfer history list asynchronously.<p>
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
     * Gets transfer history list asynchronously.<p>
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
     * Gets token history list with a specific transaction hash.<p>
     * GET /v2/transfer/tx/{transaction-hash}
     * @param txHash A transaction hash to get token history
     * @return Transfers
     * @throws ApiException
     */
    public Transfers getTransferHistoryByTxHash(String txHash) throws ApiException {
        return tokenHistoryApi.getTransfersByTxHash(chainId, txHash);
    }

    /**
     * Gets token history list with a specific transaction hash asynchronously.<p>
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
     * Gets token history list with a specific EOA.<p>
     * It will send a request without filter options.<p>
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
     * Gets token history list with a specific EOA.<p>
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
     * Gets token history list with a specific EOA asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Gets token history list with a specific EOA asynchronously.<p>
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
     * Retrieve information of all labeled FT contracts.<p>
     * It will send a request without filter options.<p>
     * GET /v2/contract/ft
     * @return PageableFtContractDetails
     * @throws ApiException
     */
    public PageableFtContractDetails getFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTContractList(options);
    }

    /**
     * Retrieve information of all labeled FT contracts.<p>
     * GET /v2/contract/ft
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @return PageableFtContractDetails
     * @throws ApiException
     */
    public PageableFtContractDetails getFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListofFtContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    /**
     * Retrieve information of all labeled FT contracts asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Retrieve information of all labeled FT contracts asynchronously.<p>
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
     * Retrieves the information of the FT contract labeled with the address of the FT contract.<p>
     * GET /v2/contract/ft/{ft-address}
     * @param ftAddress The FT contract address to retrieve contract information.
     * @return FtContractDetail
     * @throws ApiException
     */
    public FtContractDetail getFTContract(String ftAddress) throws ApiException {
        return tokenContractApi.getFtContractDetail(chainId, ftAddress);
    }

    /**
     * Retrieves the information of the FT contract labeled with the address of the FT contract asynchronously.<p>
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
     * Retrieve information of all labeled NFT contracts.<p>
     * It will send a request without filter options.<p>
     * GET /v2/contract/nft
     * @return PageableNftContractDetails
     * @throws ApiException
     */
    public PageableNftContractDetails getNFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTContractList(options);
    }

    /**
     * Retrieve information of all labeled NFT contracts.<p>
     * GET /v2/contract/nft
     * @param options Filters required when retrieving data. `status`, `type`, `size`, and `cursor`.
     * @return PageableNftContractDetails
     * @throws ApiException
     */
    public PageableNftContractDetails getNFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListOfNftContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    /**
     * Retrieve information of all labeled NFT contracts asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Retrieve information of all labeled NFT contracts asynchronously.<p>
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
     * Retrieves the information of the NFT contract labeled with the address of the NFT contract.<p>
     * GET /v2/contract/nft/{nftAddress}
     * @param nftAddress The NFT contract address to retrieve contract information.
     * @return NftContractDetail
     * @throws ApiException
     */
    public NftContractDetail getNFTContract(String nftAddress) throws ApiException {
        return tokenContractApi.getNftContractDetail(chainId, nftAddress);
    }

    /**
     * Retrieves the information of the NFT contract labeled with the address of the NFT contract asynchronously.<p>
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
     * Retrieves information of all NFTs issued by a specific NFT contract.<p>
     * It will send a request without filter options.<p>
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
     * Retrieves information of all NFTs issued by a specific NFT contract.<p>
     * @param nftAddress The NFT contract address to search issued NFTs.
     * @param options Filters required when retrieving data. `size`, and `cursor`.
     * @return PageableNfts
     * @throws ApiException
     */
    public PageableNfts getNFTList(String nftAddress, TokenHistoryQueryOptions options) throws ApiException {
        return tokenApi.getNftsByContractAddress(chainId, nftAddress, options.getSize(), options.getCursor());
    }

    /**
     * Retrieves information of all NFTs issued by a specific NFT contract asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Retrieves information of all NFTs issued by a specific NFT contract asynchronously.<p>
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
     * Among the NFTs issued from the NFT contract address,<p>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved.<p>
     * It will send a request without filter options.<p>
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
     * Among the NFTs issued from the NFT contract address,<p>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved.<p>
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
     * Among the NFTs issued from the NFT contract address.<p>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Among the NFTs issued from the NFT contract address,<p>
     * the information of the NFT owned by the EOA address received as a parameter is retrieved asynchronously.<p>
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
     * Retrieve information of a specific NFT.<p>
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
     * Retrieve information of a specific NFT asynchronously.<p>
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
     * Retrieve the record of ownership changes for a specific NFT.<p>
     * It will send a request without filter options.<p>
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
     * Retrieve the record of ownership changes for a specific NFT.<p>
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
     * Retrieve the record of ownership changes for a specific NFT asynchronously.<p>
     * It will send a request without filter options.<p>
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
     * Retrieve the record of ownership changes for a specific NFT asynchronously.<p>
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
}
