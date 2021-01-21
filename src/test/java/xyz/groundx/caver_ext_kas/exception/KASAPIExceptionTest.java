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

package xyz.groundx.caver_ext_kas.exception;

import org.junit.BeforeClass;
import org.junit.Test;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class KASAPIExceptionTest {
    static CaverExtKAS caver;

    @BeforeClass
    public static void init() {
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