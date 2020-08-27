package com.klaytn.caver.ext.kas.tokenhistory.v2;

import com.klaytn.caver.ext.kas.KAS;
import io.swagger.client.ApiException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenHistoryAPITest {
    public static KAS kas;
    public static String baseUrl = "https://th-api.dev.klaytn.com";

    static String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";
    static String chainId = "1001";

    @BeforeClass
    public static void init() {
        kas = new KAS();
        kas.enableTokenHistoryAPI(baseUrl, chainId, accessKey, secretAccessKey);
        kas.getTokenHistoryAPI().tokenHistoryApiClient.getApiClient().setDebugging(true);
    }

    @Test
    public void getTransferHistory() throws ApiException {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
//        options.setPreset(65);
        kas.getTokenHistoryAPI().getTransferHistory(options);
    }
}