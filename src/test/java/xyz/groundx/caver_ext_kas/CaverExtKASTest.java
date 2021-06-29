package xyz.groundx.caver_ext_kas;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

import java.io.IOException;

import static org.junit.Assert.*;

public class CaverExtKASTest {

    @BeforeClass
    public static void init() {
        Config.init();
    }

    @Test
    public void constructorWithOptions() throws IOException {
        CaverExtKAS caver = new CaverExtKAS(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey());
        assertTrue(caver.currentProvider instanceof HttpService);
        assertEquals(caver.kas.wallet.getApiClient().getBasePath(), Config.URL_WALLET_API);
        assertEquals(caver.kas.tokenHistory.getApiClient().getBasePath(), Config.URL_TH_API);
        assertEquals(caver.kas.anchor.getApiClient().getBasePath(), Config.URL_ANCHOR_API);
        assertEquals(caver.kas.kip17.getApiClient().getBasePath(), Config.URL_KIP17_API);
        assertEquals(caver.kas.kip7.getApiClient().getBasePath(), Config.URL_KIP7_API);

        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        caver = new CaverExtKAS(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);
        assertTrue(caver.currentProvider instanceof HttpService);
        assertEquals(caver.kas.wallet.getApiClient().getBasePath(), Config.URL_WALLET_API);
        assertEquals(caver.kas.tokenHistory.getApiClient().getBasePath(), Config.URL_TH_API);
        assertEquals(caver.kas.anchor.getApiClient().getBasePath(), Config.URL_ANCHOR_API);
        assertEquals(caver.kas.kip17.getApiClient().getBasePath(), Config.URL_KIP17_API);
        assertEquals(caver.kas.kip7.getApiClient().getBasePath(), Config.URL_KIP7_API);

        options.setUseNodeAPIWithHttp(false);
        caver = new CaverExtKAS(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);

        assertTrue(caver.currentProvider instanceof WebSocketService);
        assertEquals(caver.kas.wallet.getApiClient().getBasePath(), Config.URL_WALLET_API);
        assertEquals(caver.kas.tokenHistory.getApiClient().getBasePath(), Config.URL_TH_API);
        assertEquals(caver.kas.anchor.getApiClient().getBasePath(), Config.URL_ANCHOR_API);
        assertEquals(caver.kas.kip17.getApiClient().getBasePath(), Config.URL_KIP17_API);
        assertEquals(caver.kas.kip7.getApiClient().getBasePath(), Config.URL_KIP7_API);

        caver.currentProvider.close();
    }

    @Test
    public void initKASAPIWithOptions() throws IOException {
        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        CaverExtKAS caver = new CaverExtKAS();
        caver.initKASAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);

        assertTrue(caver.currentProvider instanceof HttpService);
        assertEquals(caver.kas.wallet.getApiClient().getBasePath(), Config.URL_WALLET_API);
        assertEquals(caver.kas.tokenHistory.getApiClient().getBasePath(), Config.URL_TH_API);
        assertEquals(caver.kas.anchor.getApiClient().getBasePath(), Config.URL_ANCHOR_API);
        assertEquals(caver.kas.kip17.getApiClient().getBasePath(), Config.URL_KIP17_API);
        assertEquals(caver.kas.kip7.getApiClient().getBasePath(), Config.URL_KIP7_API);

        options.setUseNodeAPIWithHttp(false);
        caver.initKASAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);

        assertTrue(caver.currentProvider instanceof WebSocketService);
        assertEquals(caver.kas.wallet.getApiClient().getBasePath(), Config.URL_WALLET_API);
        assertEquals(caver.kas.tokenHistory.getApiClient().getBasePath(), Config.URL_TH_API);
        assertEquals(caver.kas.anchor.getApiClient().getBasePath(), Config.URL_ANCHOR_API);
        assertEquals(caver.kas.kip17.getApiClient().getBasePath(), Config.URL_KIP17_API);
        assertEquals(caver.kas.kip7.getApiClient().getBasePath(), Config.URL_KIP7_API);

        caver.currentProvider.close();
    }
}
