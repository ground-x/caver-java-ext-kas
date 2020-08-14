package com.klaytn.caver.ext.kas.anchor.v1;

import anchor.v1.ApiClient;
import anchor.v1.ApiException;
import anchor.v1.api.AnchorApi;
import anchor.v1.model.*;

import java.util.Map;

public class AnchorAPIV1 {
    AnchorApi anchorApi;
    String url;
    String authorization;
    String chainId;

    public AnchorAPIV1(String url, String authorization, String chainId) {
        this.url = url;
        this.authorization = authorization;
        this.chainId = chainId;

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        this.anchorApi = new AnchorApi(apiClient);
    }

    public V1AnchorResponse sendAnchoringData(String operatorId, Map payload) throws ApiException {
        V1AnchorRequest anchorRequest = new V1AnchorRequest();
        anchorRequest.setOperator(operatorId);
        anchorRequest.setPayload(payload);

        return getAnchorApi().anchorBlock(getAuthorization(), "", anchorRequest);
    }

    public V1OperatorTxResponse getAnchoringTransaction(String operatorId, AnchorQueryParams queryParams) throws ApiException {
        if(queryParams != null) {
            return anchorApi.retrieveBlock(operatorId, getAuthorization(), null, queryParams.getSize(), queryParams.getFromDate(), queryParams.toDate, queryParams.getCursor());
        } else {
            return anchorApi.retrieveBlock(operatorId, getAuthorization(), null, 0, 0, 0, null);
        }
    }

    public V1OperatorTxResponse1 getAnchoringTransactionByTxHash(String operatorId, String txHash) throws ApiException {
        return anchorApi.retrieveanchoredtransaction(operatorId, txHash, getAuthorization(), null);
    }

    public V1OperatorPayloadResponse getAnchoringTransactionByPayloadId(String operatorId, String payloadId) throws ApiException {
        return anchorApi.getRetrieveanchoredtransaction(operatorId, payloadId, getAuthorization(), null);
    }

    public V1OperatorResponse getOperators(AnchorQueryParams queryParams) throws ApiException {
        if(queryParams != null) {
            return anchorApi.retrieveregisteredservicechainoperators(getAuthorization(), null, queryParams.getSize(), queryParams.getFromDate(), queryParams.toDate, queryParams.getCursor());
        } else {
            return anchorApi.retrieveregisteredservicechainoperators(getAuthorization(), null, 0, 0, 0, null);
        }
    }

    public V1OperatorResponse1 getOperator(String operatorId) throws ApiException {
        return anchorApi.retrieveOperator(getAuthorization(), null, operatorId);
    }

    public AnchorApi getAnchorApi() {
        return anchorApi;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getChainId() {
        return chainId;
    }

    public void setAnchorApi(AnchorApi anchorApi) {
        this.anchorApi = anchorApi;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}
