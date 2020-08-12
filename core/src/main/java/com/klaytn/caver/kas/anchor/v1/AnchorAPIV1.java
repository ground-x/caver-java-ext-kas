package com.klaytn.caver.kas.anchor.v1;

import anchor.v1.ApiClient;
import anchor.v1.ApiException;
import anchor.v1.api.AnchorApi;
import anchor.v1.model.Payload;
import anchor.v1.model.V1AnchorRequest;

import java.util.Map;

public class AnchorAPIV1 {

    public static void sendAnchoringData(String operatorId, Map payload, AccessOptions accessOptions) throws ApiException {
        AnchorApi api = new AnchorApi();
        api.getApiClient().setDebugging(true);

        V1AnchorRequest body = new V1AnchorRequest();
        body.setOperator(operatorId);

        Payload payload1 = new Payload();
        payload1.setBlockNumber(5);
        payload1.setCustomField("custom");

        body.setPayload(payload1);

        api.anchorBlock(accessOptions.getAuthorizationString(), accessOptions.getKrn().get(0), body);
    }
}
