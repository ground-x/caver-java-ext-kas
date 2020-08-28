package com.klaytn.caver.ext.kas.tokenhistory;

import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.tokenhistory.v2.api.TokenHistoryApi;
import io.swagger.client.api.tokenhistory.v2.model.FtContractDetail;
import io.swagger.client.api.tokenhistory.v2.model.PageableFtContractDetails;
import io.swagger.client.api.tokenhistory.v2.model.PageableTransfers;

public class TokenHistoryAPIv2 {
    TokenHistoryApi tokenHistoryApiClient;
    String chainId;

    public TokenHistoryAPIv2(String chainId, ApiClient client) {
        this.chainId = chainId;
        tokenHistoryApiClient = new TokenHistoryApi(client);
    }

    public PageableTransfers getTransferHistory() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistory(options);
    }

    public PageableTransfers getTransferHistory(TokenHistoryQueryOptions options) throws ApiException {
        return tokenHistoryApiClient.transfer(chainId, null, options.getKind(), options.getRange(), options.getSize(), options.getCursor(), options.getPreset());
    }

    public Call getTransferHistoryAsync(ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAsync(options, callback);
    }

    public Call getTransferHistoryAsync(TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback ) throws ApiException {
        return tokenHistoryApiClient.transferAsync(chainId, null, options.getKind(), options.getRange(), options.getSize(), options.getCursor(), options.getPreset(), callback);
    }

    public PageableTransfers getTransferHistoryByTxHash(String txHash) throws ApiException {
        return tokenHistoryApiClient.txhashTransfer(chainId, txHash);
    }

    public Call getTransferHistoryByTxHashAsync(String txHash, ApiCallback callback) throws ApiException {
        return tokenHistoryApiClient.txhashTransferAsync(chainId, txHash, callback);
    }

    public PageableTransfers getTransferHistoryByAccount(String address) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryByAccount(address, options);
    }

    public PageableTransfers getTransferHistoryByAccount(String address, TokenHistoryQueryOptions options) throws ApiException {
        return tokenHistoryApiClient.addressTransfer(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor());
    }

    public Call getTransferHistoryAccountAsync(String address, ApiCallback<PageableTransfers> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getTransferHistoryAccountAsync(address, options, callback);
    }

    public Call getTransferHistoryAccountAsync(String address, TokenHistoryQueryOptions options, ApiCallback<PageableTransfers> callback) throws ApiException {
        return tokenHistoryApiClient.addressTransferAsync(chainId, address, options.getKind(), options.getCaFilter(), options.getRange(), options.getSize(), options.getCursor(), callback);
    }

    public PageableFtContractDetails getFtContractList() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFtContractList(options);
    }

    public PageableFtContractDetails getFtContractList(TokenHistoryQueryOptions options) throws ApiException {
        return tokenHistoryApiClient.fT(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor());
    }

    public Call getFtContractListAsync(ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        return getFtContractListAsync(options, callback);
    }

    public Call getFtContractListAsync(TokenHistoryQueryOptions options, ApiCallback<PageableFtContractDetails> callback) throws ApiException {
        return tokenHistoryApiClient.fTAsync(chainId, options.getStatus(), options.getType(), options.getSize(), options.getCursor(), callback);
    }

    public FtContractDetail getFTContract(String ftAddress) throws ApiException {
        return tokenHistoryApiClient.fT_0(chainId, ftAddress);
    }

    public Call getFTContractAsync(String ftAddress, ApiCallback<FtContractDetail> callback) throws ApiException {
        return tokenHistoryApiClient.fT_0Async(chainId, ftAddress, callback);
    }




}
