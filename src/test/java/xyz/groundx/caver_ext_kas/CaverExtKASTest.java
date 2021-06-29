/*
 * Copyright 2020 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
