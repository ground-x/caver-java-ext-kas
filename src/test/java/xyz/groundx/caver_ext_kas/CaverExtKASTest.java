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
        assertNotNull(caver.kas.wallet);
        assertNotNull(caver.kas.tokenHistory);
        assertNotNull(caver.kas.anchor);
        assertNotNull(caver.kas.kip17);
        assertNotNull(caver.kas.kip7);

        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        caver = new CaverExtKAS(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);
        assertTrue(caver.currentProvider instanceof HttpService);
        assertNotNull(caver.kas.wallet);
        assertNotNull(caver.kas.tokenHistory);
        assertNotNull(caver.kas.anchor);
        assertNotNull(caver.kas.kip17);
        assertNotNull(caver.kas.kip7);
    }

    @Test
    public void initKASAPIWithOptions() throws IOException {
        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        CaverExtKAS caver = new CaverExtKAS();
        caver.initKASAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), options);

        assertTrue(caver.currentProvider instanceof HttpService);
        assertNotNull(caver.kas.wallet);
        assertNotNull(caver.kas.tokenHistory);
        assertNotNull(caver.kas.anchor);
        assertNotNull(caver.kas.kip17);
        assertNotNull(caver.kas.kip7);
    }
}
