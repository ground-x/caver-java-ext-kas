package com.klaytn.caver.ext.kas.anchor.v1;

import anchor.v1.ApiException;
import com.klaytn.caver.ext.kas.KAS;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AnchorAPIV1Test {
    String operatorID = "0x6491bC445d302a28becc041Cda9a40BebD8a5A19";
    String krn = "krn:1001:anchor:test:operator-pool:rp1";
    String basPath = "https://anchor-anchor-api.dev.klaytn.com";
    String accessKey = "72ec21002ad2de341fd8e15801b5f80035ac8f5c";
    String secretAccessKey = "932424d7ca203ec601528155097e3d527722ec3e";

    @Test
    public void sendAnchoringDataTest() throws ApiException {
        KAS kas = new KAS();
        kas.enableAnchorAPI(basPath, "1001", accessKey, secretAccessKey);

        kas.getAnchorAPI().getAnchorApi().getApiClient().setDebugging(true);

        Map payload = new HashMap();
        payload.put("field", "1");
        payload.put("filed2", 2);
        kas.getAnchorAPI().sendAnchoringData(operatorID, payload);
    }

    @Test
    public void getAnchoringTransactionsTest() throws ApiException {
//        AnchorQueryParams anchorQueryParams = new AnchorQueryParams();
//        AccessOptions accessOptions = new AccessOptions(
//                "72ec21002ad2de341fd8e15801b5f80035ac8f5c",
//                "932424d7ca203ec601528155097e3d527722ec3e",
//                Arrays.asList(krn)
//        );
//        AnchorAPIV1.getAnchoringTransactions(operatorID, anchorQueryParams, accessOptions);
//
//        Gson gson = new Gson();
    }
}