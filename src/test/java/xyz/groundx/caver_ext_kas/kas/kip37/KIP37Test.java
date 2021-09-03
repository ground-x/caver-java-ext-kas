/*
 * Copyright 2021 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.kas.kip37;

import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.Kip37DeployResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.Kip37DeployerResponse;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class KIP37Test {
    public static CaverExtKAS caver;
    public static String account;
    public static String testContractAlias;
    public static String testContractAddress;
    public static String deployerAddress;

    public static TransactionReceipt.TransactionReceiptData getReceipt(CaverExtKAS caver, String txHash) throws TransactionException, IOException {
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        return receiptProcessor.waitForTransactionReceipt(txHash);
    }

    public static void prepareKIP37Contract(CaverExtKAS caver) throws ApiException, TransactionException, IOException, InterruptedException {
        String uri = "https://token-cdn-domain/{id}.json";
        testContractAlias = "kk-" + new Date().getTime();

        Kip37DeployResponse deployResponse = caver.kas.kip37.deploy(uri, testContractAlias);
        TransactionReceipt.TransactionReceiptData receiptData = getReceipt(caver, deployResponse.getTransactionHash());
        Thread.sleep(5000);

        Kip37DeployerResponse deployerResponse = caver.kas.kip37.getDeployer();
        deployerAddress = deployerResponse.getAddress();
        testContractAddress = receiptData.getContractAddress();
    }

    @BeforeClass
    public static void init() {
        Config.init();
        caver = Config.getCaver();
        caver.kas.kip37.getApiClient().setDebugging(true);
    }

    @Test
    public void deployTest() {

    }
}