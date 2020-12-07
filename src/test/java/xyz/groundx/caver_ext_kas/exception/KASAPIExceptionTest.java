package xyz.groundx.caver_ext_kas.exception;

import org.junit.BeforeClass;
import org.junit.Test;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;

import static org.junit.Assert.*;

public class KASAPIExceptionTest {
    static CaverExtKAS caver;

    @BeforeClass
    public static void init() throws ApiException {
        Config.init();
        caver = Config.getCaver();
        caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
    }

    @Test
    public void getAccountFail() {
        try {
            caver.wallet.getAccount("0xb2Fd3a28efC3226638B7f92D9b48C370588c49F2");
        } catch (KASAPIException e) {
            assertEquals(400, e.getCode());
            assertEquals("Bad Request", e.getMessage());
            assertEquals(1061010, e.getResponseBody().getCode());
            assertEquals("data don't exist", e.getResponseBody().getMessage());
        }

    }
}