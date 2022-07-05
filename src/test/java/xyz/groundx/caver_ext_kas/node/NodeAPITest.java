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

package xyz.groundx.caver_ext_kas.node;

import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static junit.framework.TestCase.*;
import static org.junit.Assert.fail;

public class NodeAPITest {
    static CaverExtKAS caver;

    static String account;

    static String kip17Contract;

    static long blockNumber;
    static String blockHash = "";
    static String transactionHash = "";

    static KeyringContainer container;

    @BeforeClass
    public static void init() throws Exception {
        Config.init();
        caver = Config.getCaver();

        account = Config.getKlayProviderKeyring().getAddress();

        TransactionReceipt.TransactionReceiptData receipt = Config.sendValue("0xf9ca77f63353285bed406215033fa572347339e3");
        blockHash = receipt.getBlockHash();
        transactionHash = receipt.getTransactionHash();
        blockNumber = Numeric.toBigInt(receipt.getBlockNumber()).longValue();

        deployContract();
    }

    public static void deployContract() throws Exception {
        kip17Contract = Config.deployKIP17(caver, account);
        System.out.println("Contract address : " + kip17Contract);
    }


    @Test
    public void accountCreatedTest() {
        try {
            Boolean created = caver.rpc.klay.accountCreated(account).send();
            if(created.hasError()) {
                throw new IOException(created.getError().getMessage());
            }
            TestCase.assertTrue(created.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void getAccountTest() {
        try {
            Account eoaAccount = caver.rpc.klay.getAccount(account).send();
            if(eoaAccount.hasError()) {
                throw new IOException(eoaAccount.getError().getMessage());
            }
            TestCase.assertEquals(IAccountType.AccType.EOA.getAccType(), eoaAccount.getResult().getAccType());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountKeyTest() {
        try {
            AccountKey res = caver.rpc.klay.getAccountKey(account).send();
            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            TestCase.assertNotNull(res);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBalanceTest() {
        try {
            Quantity res = caver.rpc.klay.getBalance(account).send();
            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            Assert.assertNotEquals(0, res.getValue().compareTo(BigInteger.ZERO));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getCodeTest() {
        try {
            Bytes res = caver.rpc.klay.getCode(kip17Contract).send();

            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            TestCase.assertNotNull(res.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransactionCountTest() throws IOException {
        Quantity response = caver.rpc.klay.getTransactionCount(
                account,
                DefaultBlockParameterName.LATEST).send();
        BigInteger result = response.getValue();
        assertNotNull(result);
    }

    @Test
    public void isContractAccountTest() {
        try {
            Boolean result = caver.rpc.klay.isContractAccount(kip17Contract).send();
            if(result.hasError()) {
                throw new IOException(result.getError().getMessage());
            }
            TestCase.assertTrue(result.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockNumberTest() {
        try {
            Quantity response = caver.rpc.klay.getBlockNumber().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockByNumberTest() {
        try {
            Block response = caver.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> block = response.getResult();
            TestCase.assertNotNull(block.getHash());

            response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            block = response.getResult();
            TestCase.assertNotNull(block.getHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockByHashTest() {
        try {
            Block responseByHash = caver.rpc.klay.getBlockByHash(blockHash, true).send();

            TestCase.assertEquals(blockHash, responseByHash.getResult().getHash());

            responseByHash = caver.rpc.klay.getBlockByHash(blockHash, false).send();

            TestCase.assertEquals(blockHash, responseByHash.getResult().getHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockReceiptsTest() {
        try {
            BlockTransactionReceipts blockReceipts = caver.rpc.klay.getBlockReceipts(blockHash).send();
            TestCase.assertEquals(blockHash, blockReceipts.getResult().get(0).getBlockHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockTransactionCountByNumberTest() {
        try {
            Block response = caver.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByNumber = caver.rpc.klay.getBlockTransactionCountByNumber(
                    new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
            BigInteger result = responseByNumber.getValue();
            TestCase.assertEquals(testBlock.getTransactions().size(), result.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockTransactionCountByHash() {
        try {
            Block response = caver.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByHash = caver.rpc.klay.getBlockTransactionCountByHash(testBlock.getHash()).send();
            BigInteger result = responseByHash.getValue();
            TestCase.assertEquals(testBlock.getTransactions().size(), result.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockWithConsensusInfoByHashTest() throws Exception {
        Block response = caver.rpc.klay.getBlockByNumber(blockNumber, true).send();
        Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

        BlockWithConsensusInfo responseInfo = caver.rpc.klay.getBlockWithConsensusInfoByHash(testBlock.getHash()).send();
        BlockWithConsensusInfo.Block result = responseInfo.getResult();
        TestCase.assertEquals(testBlock.getHash(), result.getHash());
    }

    @Test
    public void getBlockWithConsensusInfoByNumberTest() throws Exception {
        Block response = caver.rpc.klay.getBlockByNumber(blockNumber, true).send();
        Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

        BlockWithConsensusInfo responseInfo = caver.rpc.klay.getBlockWithConsensusInfoByNumber(
                new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
        BlockWithConsensusInfo.Block result = responseInfo.getResult();
        TestCase.assertEquals(testBlock.getNumber(), result.getNumber());
    }

    @Test
    public void getCommitteeTest() throws IOException {
        Addresses response = caver.rpc.klay.getCommittee(DefaultBlockParameterName.LATEST).send();
        TestCase.assertNull(response.getError());
    }

    @Test
    public void getCommitteeSizeTest() throws IOException {
        Quantity response = caver.rpc.klay.getCommitteeSize(DefaultBlockParameterName.LATEST).send();
        TestCase.assertNull(response.getError());
    }

    @Test
    public void getCouncilTest() throws IOException {
        Addresses response = caver.rpc.klay.getCouncil(DefaultBlockParameterName.LATEST).send();
        TestCase.assertNull(response.getError());
    }

    @Test
    public void getCouncilSizeTest() throws IOException {
        Quantity response = caver.rpc.klay.getCouncilSize(DefaultBlockParameterName.LATEST).send();
        TestCase.assertNull(response.getError());
    }

    @Test
    public void getStorageAtTest() throws Exception {
        Response<String> response = caver.rpc.klay.getStorageAt(
                account,
                new DefaultBlockParameterNumber(0),
                DefaultBlockParameterName.LATEST).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void isSyncingTest() throws Exception {
        KlaySyncing response = caver.rpc.klay.isSyncing().send();
        KlaySyncing.Result result = response.getResult();
        TestCase.assertFalse(result.isSyncing());
    }

    @Test
    public void callTest() {
        try {
            KIP17 kip17 = new KIP17(caver, kip17Contract);
            String encoded = kip17.getMethod("symbol").encodeABI(Collections.emptyList());

            CallObject callObject = CallObject.createCallObject(
                    account,
                    kip17Contract,
                    new BigInteger("100000000", 16),
                    new BigInteger("ae9f7bcc00", 16),
                    new BigInteger("0", 16),
                    encoded
            );

            Bytes symbol = caver.rpc.klay.call(callObject).send();
            TestCase.assertNotNull(symbol);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void estimateGasTest() throws Exception {
        KIP17 kip17 = new KIP17(caver, kip17Contract);
        String encoded = kip17.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17.getContractAddress(),
                new BigInteger("100000000", 16),
                new BigInteger("ae9f7bcc00", 16),
                new BigInteger("0", 16),
                encoded
        );
        Quantity response = caver.rpc.klay.estimateGas(callObject).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void estimateComputationCostTest() throws Exception {
        KIP17 kip17 = new KIP17(caver, kip17Contract);
        String encoded = kip17.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17.getContractAddress(),
                new BigInteger("100000000", 16),
                new BigInteger("ae9f7bcc00", 16),
                new BigInteger("0", 16),
                encoded
        );
        Quantity response = caver.rpc.klay.estimateComputationCost(callObject, DefaultBlockParameterName.LATEST).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void getTransactionByBlockHashAndIndexTest() throws Exception {
        Transaction res = caver.rpc.klay.getTransactionByBlockHashAndIndex(blockHash, 0).send();
        TestCase.assertEquals(blockHash, res.getResult().getBlockHash());
    }

    @Test
    public void getTransactionByBlockNumberAndIndexTest() throws IOException {
        Transaction res = caver.rpc.klay.getTransactionByBlockNumberAndIndex(
                blockNumber,
                0).send();
        TestCase.assertEquals(blockHash, res.getResult().getBlockHash());
    }
//
    @Test
    public void getTransactionByHashTest() throws Exception {
        Transaction response = caver.rpc.klay.getTransactionByHash(transactionHash).send();
        Transaction.TransactionData result = response.getResult();
        TestCase.assertEquals(transactionHash, result.getHash());
    }


    @Test
    public void getTransactionReceiptTest() throws Exception {
        TransactionReceipt response = caver.rpc.klay.getTransactionReceipt(transactionHash).send();
        TestCase.assertEquals(transactionHash, response.getResult().getTransactionHash());
    }

    @Test
    public void sendRawTransactionTest() throws Exception {
        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setKlaytnCall(caver.rpc.getKlay())
                .setFrom(account)
                .setTo(KeyringFactory.generate().getAddress())
                .setValue(BigInteger.valueOf(1))
                .setGas(BigInteger.valueOf(25000))
                .build();

        Config.keyringContainer.sign(account, valueTransfer);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(valueTransfer).send();
        assertNotNull(txHash);
    }


    @Test
    public void getChainIdTest() throws Exception {
        Quantity response = caver.rpc.klay.getChainID().send();
        BigInteger result = response.getValue();
        assertEquals(BigInteger.valueOf(ChainId.BAOBAB_TESTNET), result);
    }

    @Test
    public void getClientVersionTest() throws IOException {
        Bytes response = caver.rpc.klay.getClientVersion().send();
        TestCase.assertNull(response.getError());
    }

    @Test
    public void getGasPriceTest() throws Exception {
        Quantity response = caver.rpc.klay.getGasPrice().send();
        BigInteger result = response.getValue();
        assertEquals(new BigInteger("3a35294400", 16), result); // 25,000,000,000 peb = 25 Gpeb
    }

    @Test
    public void getGasPriceAtTest() throws IOException {
        Quantity response = caver.rpc.klay.getGasPriceAt().send();
        BigInteger result = response.getValue();
        assertEquals(new BigInteger("3a35294400", 16), result); // 25,000,000,000 peb = 25 Gpeb
    }


    @Test
    public void getProtocolVersionTest() throws Exception {
        String result = caver.rpc.klay.getProtocolVersion().send().getResult();
        assertEquals("0x40", result);
    }


//    @Test
//    public void getFilterChangesTest() throws Exception {
//        KlayLogs response = caver.rpc.klay.getFilterChanges(
//                "").send();
//        assertNotNull(response);
//    }

//    @Test
//    public void getFilterLogsTest() throws Exception {
//        KlayLogs response = caver.rpc.klay.getFilterLogs(
//                "0xf9081aeb4a0dc3109959748c659c8cd7").send();
//        assertNotNull(response);
//    }

    @Test
    public void getLogsTest() throws Exception {
        KlayLogFilter logFilter = new KlayLogFilter();
        KlayLogs response = caver.rpc.klay.getLogs(logFilter).send();
        assertNotNull(response);
    }

//    @Test
//    public void newBlockFilterTest() throws Exception {
//        Response<String> response = caver.rpc.klay.newBlockFilter().send();
//        String result = response.getResult();
//        assertNotNull(result);
//    }

//    @Test
//    public void newFilterTest() throws Exception {
//        KlayFilter filter = new KlayFilter(
//                DefaultBlockParameterName.EARLIEST,
//                DefaultBlockParameterName.LATEST,
//                account);
//        filter.addSingleTopic("0xd596fdad182d29130ce218f4c1590c4b5ede105bee36690727baa6592bd2bfc8");
//        Quantity response = caver.rpc.klay.newFilter(filter).send();
//        String result = response.getResult();
//        assertNotNull(result);
//    }

//    @Test
//    public void newPendingTransactionFilterTest() throws Exception {
//        Response<String> response = caver.rpc.klay.newPendingTransactionFilter().send();
//        String result = response.getResult();
//        assertNotNull(result);
//    }

//    @Test
//    public void uninstallFilterTest() throws Exception {
//        Boolean response = caver.rpc.klay.uninstallFilter("0x0").send();
//        assertNotNull(response);
//    }

    @Test
    public void getSha3Test() throws IOException {
        Bytes response = caver.rpc.klay.sha3("0x123f").send();
        String result = response.getResult();
        assertEquals("0x7fab6b214381d6479bf140c3c8967efb9babe535025500d5b1dc2d549984b90b", result);
    }

    @Test
    public void getIdTest() throws Exception {
        Bytes netVersion = caver.rpc.net.getNetworkID().send();
        TestCase.assertEquals(netVersion.getResult(), String.valueOf(ChainId.BAOBAB_TESTNET));
    }

    @Test
    public void isListeningTest() throws Exception {
        Boolean netListening = caver.rpc.net.isListening().send();
        TestCase.assertTrue(netListening.getResult());
    }

    @Test
    public void getPeerCountTest() throws Exception {
        Quantity peerCount = caver.rpc.net.getPeerCount().send();
        assertTrue(peerCount.getValue().intValue() >= 0);
    }

    @Test
    public void getPeerCountByTypeTest() throws Exception {
        KlayPeerCount klayPeerCount = caver.rpc.net.getPeerCountByType().send();
        KlayPeerCount.PeerCount peerCount = klayPeerCount.getResult();
        assertTrue(peerCount.getTotal().intValue() >= 0);
    }
}
