package com.klaytn.caver.ext.kas.anchor.v1;

import com.squareup.okhttp.Call;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.anchor.v1.api.DataAnchoringTransactionApi;
import io.swagger.client.api.anchor.v1.api.OperatorApi;
import io.swagger.client.api.anchor.v1.model.*;


import java.util.Map;

public class AnchorAPIV1 {
    DataAnchoringTransactionApi dataAnchoringTransactionApi;
    OperatorApi operatorApi;
    String chainId;

    public AnchorAPIV1(String chainId, ApiClient anchorApiClient) {
        setChainId(chainId);
        setDataAnchoringTransactionApi(new DataAnchoringTransactionApi(anchorApiClient));
        setOperatorApi(new OperatorApi(anchorApiClient));
    }

    public AnchorBlockResponse sendAnchoringData(String operatorId, Map payload) throws ApiException {
        AnchorBlockRequest anchorRequest = new AnchorBlockRequest();
        anchorRequest.setOperator(operatorId);
        anchorRequest.setPayload(payload);

        return getDataAnchoringTransactionApi().anchorBlock(getChainId(), anchorRequest);
    }

    public Call sendAnchoringDataAsync(String operatorId, Map payload, ApiCallback<AnchorBlockResponse> callback) throws ApiException {
        AnchorBlockRequest anchorRequest = new AnchorBlockRequest();
        anchorRequest.setOperator(operatorId);
        anchorRequest.setPayload(payload);

        return getDataAnchoringTransactionApi().anchorBlockAsync(getChainId(), anchorRequest, callback);
    }

    public RetirieveAnchorBlockResponse getAnchoringTransactions(String operatorId) throws ApiException {
        AnchorQueryParams params = new AnchorQueryParams();
        return getAnchoringTransactions(operatorId, params);
    }

    public RetirieveAnchorBlockResponse getAnchoringTransactions(String operatorId, AnchorQueryParams queryParams) throws ApiException {
        return dataAnchoringTransactionApi.retirieveAnchorBlock(getChainId(), operatorId, queryParams.getSize(), queryParams.getCursor() , queryParams.getFromDate(), queryParams.getToDate());
    }

    public Call getAnchoringTransactionsAsync(String operatorId, ApiCallback<RetirieveAnchorBlockResponse> callback) throws ApiException {
        AnchorQueryParams params = new AnchorQueryParams();
        return getAnchoringTransactionsAsync(operatorId, params, callback);
    }

    public Call getAnchoringTransactionsAsync(String operatorId, AnchorQueryParams queryParams, ApiCallback<RetirieveAnchorBlockResponse> callback) throws ApiException {
        return dataAnchoringTransactionApi.retirieveAnchorBlockAsync(getChainId(), operatorId, queryParams.getSize(), queryParams.getCursor() , queryParams.getFromDate(), queryParams.getToDate(), callback);
    }

    public GetAnchorBlockByTxResponse getAnchoringTransactionByTxHash(String operatorId, String txHash) throws ApiException {
        return dataAnchoringTransactionApi.getAnchorBlockByTx(getChainId(), operatorId, txHash);
    }

    public Call getAnchoringTransactionByTxHashAsync(String operatorId, String txHash, ApiCallback<GetAnchorBlockByTxResponse> callback) throws ApiException {
        return dataAnchoringTransactionApi.getAnchorBlockByTxAsync(getChainId(), operatorId, txHash, callback);
    }

    public GetAnchorBlockByPayloadIDResponse getAnchoringTransactionByPayloadId(String operatorId, String payloadId) throws ApiException {
        return dataAnchoringTransactionApi.getAnchorBlockByPayloadID(getChainId(), operatorId, payloadId);
    }

    public Call getAnchoringTransactionByPayloadIdAsync(String operatorId, String payloadId, ApiCallback<GetAnchorBlockByPayloadIDResponse> callback) throws ApiException {
        return dataAnchoringTransactionApi.getAnchorBlockByPayloadIDAsync(getChainId(), operatorId, payloadId, callback);
    }

    public RetrieveOperatorsResponse getOperators() throws ApiException {
        AnchorQueryParams queryParams = new AnchorQueryParams();
        return getOperators(queryParams);
    }

    public RetrieveOperatorsResponse getOperators(AnchorQueryParams queryParams) throws ApiException {
        return getOperatorApi().retrieveOperators(getChainId(), queryParams.getSize(), queryParams.getCursor(), queryParams.getFromDate(), queryParams.toDate);
    }

    public Call getOperatorsAsync(ApiCallback<RetrieveOperatorsResponse> callback) throws ApiException {
        AnchorQueryParams queryParams = new AnchorQueryParams();
        return getOperatorsAsync(queryParams, callback);
    }

    public Call getOperatorsAsync(AnchorQueryParams queryParams, ApiCallback<RetrieveOperatorsResponse> callback) throws ApiException {
        return getOperatorApi().retrieveOperatorsAsync(getChainId(), queryParams.getSize(), queryParams.getCursor(), queryParams.getFromDate(), queryParams.getToDate(), callback);
    }

    public GetOperatorResponse getOperator(String operatorId) throws ApiException {
        return getOperatorApi().getOperator(chainId, operatorId);
    }

    public Call getOperatorAsync(String operatorId, ApiCallback<GetOperatorResponse> callback) throws ApiException {
        return getOperatorApi().getOperatorAsync(chainId, operatorId, callback);
    }

    public DataAnchoringTransactionApi getDataAnchoringTransactionApi() {
        return dataAnchoringTransactionApi;
    }

    public OperatorApi getOperatorApi() {
        return operatorApi;
    }

    public String getChainId() {
        return chainId;
    }

    public void setDataAnchoringTransactionApi(DataAnchoringTransactionApi dataAnchoringTransactionApi) {
        this.dataAnchoringTransactionApi = dataAnchoringTransactionApi;
    }

    public void setOperatorApi(OperatorApi operatorApi) {
        this.operatorApi = operatorApi;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}
