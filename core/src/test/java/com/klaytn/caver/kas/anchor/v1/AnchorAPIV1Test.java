package com.klaytn.caver.kas.anchor.v1;

import anchor.v1.ApiException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AnchorAPIV1Test {
    @Test
    public void sendAnchoringDataTest() throws ApiException {
        String operatorId = "0x6491bC445d302a28becc041Cda9a40BebD8a5A19";
        AccessOptions accessOptions = new AccessOptions(
                "72ec21002ad2de341fd8e15801b5f80035ac8f5c",
                "932424d7ca203ec601528155097e3d527722ec3e",
                Arrays.asList("krn:1001:anchor:test:operator-pool:rp1")
                );
        AnchorAPIV1.sendAnchoringData(operatorId, null, accessOptions);
    }
}