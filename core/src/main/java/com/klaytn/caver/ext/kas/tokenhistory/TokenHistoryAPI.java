package com.klaytn.caver.ext.kas.tokenhistory;

import com.klaytn.caver.ext.kas.utils.KASUtils;
import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.tokenhistory.api.TokenApi;
import io.swagger.client.api.tokenhistory.api.TokenContractApi;
import io.swagger.client.api.tokenhistory.api.TokenHistoryApi;
import io.swagger.client.api.tokenhistory.api.TokenOwnershipApi;
import io.swagger.client.api.tokenhistory.model.*;

import java.util.Arrays;
import java.util.List;


public class TokenHistoryAPI {
    TokenApi tokenApi;
    TokenContractApi tokenContractApi;
    TokenHistoryApi tokenHistoryApi;
    TokenOwnershipApi tokenOwnershipApi;
    String chainId;

    public TokenHistoryAPI(String chainId, ApiClient client) {
        this.chainId = chainId;
        tokenApi = new TokenApi(client);
        tokenContractApi = new TokenContractApi(client);
        tokenHistoryApi = new TokenHistoryApi(client);
        tokenOwnershipApi = new TokenOwnershipApi(client);
    }

    public PageableTransfers getTransferHistory(int preset) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistory(preset, options);
    }

    public PageableTransfers getTransferHistory(List<Integer> presets) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistory(presets, options);
    }

    public PageableTransfers getTransferHistory(int preset, TokenHistoryQueryOptions options) throws ApiException {
        return getTransferHistory(Arrays.asList(preset), options);
    }

    public PageableTransfers getTransferHistory(List<Integer> presets, TokenHistoryQueryOptions options) throws ApiException {
        return this.tokenHistoryApi.getTransfers(chainId, KASUtils.parameterToString(presets), options.getKind(), options.getRange(), options.getSize(), options.getCursor());
    }

    public Call getTransferHistoryAsync(int preset, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAsync(preset, options, callback);
    }

    public Call getTransferHistoryAsync(List<Integer> presets, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAsync(presets, options, callback);
    }

    public Call getTransferHistoryAsync(int preset, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return getTransferHistoryAsync(Arrays.asList(preset), options, callback);
    }

    public Call getTransferHistoryAsync(List<Integer> presets, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return this.tokenHistoryApi.getTransfersAsync(chainId, KASUtils.parameterToString(presets), options.getKind(), options.getRange(), options.getSize(), options.getCursor(), callback);
    }

    public Transfers getTransferHistoryByTxHash(String txHash) throws ApiException {
        return tokenHistoryApi.getTransfersByTxHash(chainId, txHash);
    }

    public Call getTransferHistoryByTxHashAsync(String txHash, ApiCallback<Transfers> callback) throws ApiException {
        return tokenHistoryApi.getTransfersByTxHashAsync(chainId, txHash, callback);
    }

    public PageableTransfers getTransferHistoryByAccount(String address) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryByAccount(address, options);
    }

    public PageableTransfers getTransferHistoryByAccount(String address, TokenHistoryQueryOptions options) throws ApiException {
        return tokenHistoryApi.getTransfersByEoa(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor());
    }

    public Call getTransferHistoryAccountAsync(String address, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAccountAsync(address, options, callback);
    }

    public Call getTransferHistoryAccountAsync(String address, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return tokenHistoryApi.getTransfersByEoaAsync(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor(), callback);
    }

    public PageableFtContractDetails getFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTContractList(options);
    }

    public PageableFtContractDetails getFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListofFtContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    public Call getFTContractListAsync(ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFTContractListAsync(options, callback);
    }

    public Call getFTContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        return tokenContractApi.getListofFtContractsAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    public FtContractDetail getFTContract(String ftAddress) throws ApiException {
        return tokenContractApi.getFtContractDetail(chainId, ftAddress);
    }

    public Call getFTContractAsync(String ftAddress, ApiCallback<FtContractDetail> callback) throws ApiException {
        return tokenContractApi.getFtContractDetailAsync(chainId, ftAddress, callback);
    }

    public PageableNftContractDetails getNFTContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTContractList(options);
    }

    public PageableNftContractDetails getNFTContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenContractApi.getListOfNftContracts(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    public Call getNFTContractListAsync(ApiCallback<PageableNftContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTContractListAsync(options, callback);
    }

    public Call getNFTContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableNftContractDetails> callback) throws ApiException {
        return tokenContractApi.getListOfNftContractsAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    public NftContractDetail getNFTContract(String nftAddress) throws ApiException {
        return tokenContractApi.getNftContractDetail(chainId, nftAddress);
    }

    public Call getNFTContractAsync(String nftAddress, ApiCallback<NftContractDetail> callback) throws ApiException {
        return tokenContractApi.getNftContractDetailAsync(chainId, nftAddress, callback);
    }

    public PageableNfts getNFTList(String nftAddress) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTList(nftAddress, options);
    }

    public PageableNfts getNFTList(String nftAddress, TokenHistoryQueryOptions options) throws ApiException {
        return tokenApi.getNftsByContractAddress(chainId, nftAddress, options.getSize(), options.getCursor());
    }

    public Call getNFTListAsync(String nftAddress, ApiCallback<PageableNfts> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListAsync(nftAddress, options, callback);
    }

    public Call getNFTListAsync(String nftAddress, TokenHistoryQueryOptions options, ApiCallback<PageableNfts> callback) throws ApiException {
        return tokenApi.getNftsByContractAddressAsync(chainId, nftAddress, options.getSize(), options.getCursor(), callback);
    }

    public PageableNfts getNFTListByOwner(String nftAddress, String ownerAddress) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListByOwner(nftAddress, ownerAddress, options);
    }

    public PageableNfts getNFTListByOwner(String nftAddress, String ownerAddress, TokenHistoryQueryOptions options) throws ApiException {
        return tokenApi.getNftsByOwnerAddress(chainId, nftAddress, ownerAddress, options.getSize(), options.getCursor());
    }

    public Call getNFTListByOwnerAsync(String nftAddress, String ownerAddress, ApiCallback<PageableNfts> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTListByOwnerAsync(nftAddress, ownerAddress, options, callback);
    }

    public Call getNFTListByOwnerAsync(String nftAddress, String ownerAddress, TokenHistoryQueryOptions options, ApiCallback<PageableNfts> callback) throws ApiException {
        return tokenApi.getNftsByOwnerAddressAsync(chainId, nftAddress, ownerAddress, options.getSize(), options.getCursor(), callback);
    }

    public Nft getNFT(String nftAddress, String tokenId) throws ApiException {
        return tokenApi.getNftById(chainId, nftAddress, tokenId);
    }

    public Call getNFTAsync(String nftAddress, String tokenId, ApiCallback<Nft> callback) throws ApiException {
        return tokenApi.getNftByIdAsync(chainId, nftAddress, tokenId, callback);
    }

    public PageableNftOwnershipChanges getNFTOwnershipHistory(String nftAddress, String tokenId) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTOwnershipHistory(nftAddress, tokenId, options);
    }

    public PageableNftOwnershipChanges getNFTOwnershipHistory(String nftAddress, String tokenId, TokenHistoryQueryOptions options) throws ApiException {
        return tokenOwnershipApi.getListOfNftOwnershipChanges(chainId, nftAddress, tokenId, options.getSize(), options.getCursor());
    }

    public Call getNFTOwnershipHistoryAsync(String nftAddress, String tokenId, ApiCallback<PageableNftOwnershipChanges> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getNFTOwnershipHistoryAsync(nftAddress, tokenId, options, callback);
    }

    public Call getNFTOwnershipHistoryAsync(String nftAddress, String tokenId, TokenHistoryQueryOptions options, ApiCallback<PageableNftOwnershipChanges> callback) throws ApiException {
        return tokenOwnershipApi.getListOfNftOwnershipChangesAsync(chainId, nftAddress, tokenId, options.getSize(), options.getCursor(), callback);
    }
}
