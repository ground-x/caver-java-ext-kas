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

import xyz.groundx.caver_ext_kas.KAS;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class NodeAPITest {
    static KAS kas;
    static final String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static final String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";

    static final String privateKey = "0xc34442bae4b74023081d8fb05003eb13f11c40d6fc79b9f30c9caa036947ffe8";
    static SingleKeyring testKeyring;
    static String account;

    static KIP17 kip17Contract;

    static long blockNumber = 19037067;
    static String blockHash = "0x9743edbd9463c725efdb2050262560b8c11a7e4ddde46b7e94165dab76af3567";
    static String transactionHash = "0xbf0ca9b24a51a089ad4a9e41607a50cfbe7fa76f658d64437e885b42af075ec2";

    @BeforeClass
    public static void config() throws Exception {
        kas = new KAS();
        testKeyring = (SingleKeyring)kas.wallet.add(KeyringFactory.createFromPrivateKey(privateKey));
        account = testKeyring.getAddress();

        kas.enableNodeAPI("https://node-api.dev.klaytn.com/v1/klaytn", "1001", accessKey, secretAccessKey);

        deployContract(kas);
    }

    public static void deployContract(KAS kas) throws Exception {
        KIP17DeployParams deployParams = new KIP17DeployParams("KIP17", "KIP17");
        kip17Contract = KIP17.deploy(kas, deployParams, testKeyring.getAddress());

        System.out.println("Contract address : " + kip17Contract.getContractAddress());
    }


    @Test
    public void accountCreatedTest() {
        try {
            Boolean created = kas.rpc.klay.accountCreated(testKeyring.getAddress()).send();
            if(created.hasError()) {
                throw new IOException(created.getError().getMessage());
            }
            assertTrue(created.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void getAccountTest() {
        try {
            Account eoaAccount = kas.rpc.klay.getAccount(account).send();
            if(eoaAccount.hasError()) {
                throw new IOException(eoaAccount.getError().getMessage());
            }
            assertEquals(IAccountType.AccType.EOA.getAccType(), eoaAccount.getResult().getAccType());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountKeyTest() {
        try {
            AccountKey res = kas.rpc.klay.getAccountKey(account).send();
            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            assertNotNull(res);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBalanceTest() {
        try {
            Quantity res = kas.rpc.klay.getBalance(account).send();
            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            assertNotEquals(0, res.getValue().compareTo(BigInteger.ZERO));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getCodeTest() {
        try {
            Bytes res = kas.rpc.klay.getCode(kip17Contract.getContractAddress()).send();

            if(res.hasError()) {
                throw new IOException(res.getError().getMessage());
            }
            assertNotNull(res.getResult());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransactionCountTest() throws IOException {
        Quantity response = kas.rpc.klay.getTransactionCount(
                account,
                DefaultBlockParameterName.LATEST).send();
        BigInteger result = response.getValue();
        assertNotNull(result);
    }

    @Test
    public void isContractAccountTest() {
        try {
            Boolean result = kas.rpc.klay.isContractAccount(kip17Contract.getContractAddress()).send();
            if(result.hasError()) {
                throw new IOException(result.getError().getMessage());
            }
            assertTrue(result.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockNumberTest() {
        try {
            Quantity response = kas.rpc.klay.getBlockNumber().send();
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
            Block response = kas.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> block = response.getResult();
            assertNotNull(block.getHash());

            response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            block = response.getResult();
            assertNotNull(block.getHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockByHashTest() {
        try {
            Block responseByHash = kas.rpc.klay.getBlockByHash(blockHash, true).send();

            assertEquals(blockHash, responseByHash.getResult().getHash());

            responseByHash = kas.rpc.klay.getBlockByHash(blockHash, false).send();

            assertEquals(blockHash, responseByHash.getResult().getHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockReceiptsTest() {
        try {
            BlockTransactionReceipts blockReceipts = kas.rpc.klay.getBlockReceipts(blockHash).send();
            assertEquals(blockHash, blockReceipts.getResult().get(0).getBlockHash());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockTransactionCountByNumberTest() {
        try {
            Block response = kas.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByNumber = kas.rpc.klay.getBlockTransactionCountByNumber(
                    new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
            BigInteger result = responseByNumber.getValue();
            assertEquals(testBlock.getTransactions().size(), result.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockTransactionCountByHash() {
        try {
            Block response = kas.rpc.klay.getBlockByNumber(blockNumber, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByHash = kas.rpc.klay.getBlockTransactionCountByHash(testBlock.getHash()).send();
            BigInteger result = responseByHash.getValue();
            assertEquals(testBlock.getTransactions().size(), result.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockWithConsensusInfoByHashTest() throws Exception {
        Block response = kas.rpc.klay.getBlockByNumber(blockNumber, true).send();
        Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

        BlockWithConsensusInfo responseInfo = kas.rpc.klay.getBlockWithConsensusInfoByHash(testBlock.getHash()).send();
        BlockWithConsensusInfo.Block result = responseInfo.getResult();
        assertEquals(testBlock.getHash(), result.getHash());
    }

    @Test
    public void getBlockWithConsensusInfoByNumberTest() throws Exception {
        Block response = kas.rpc.klay.getBlockByNumber(blockNumber, true).send();
        Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

        BlockWithConsensusInfo responseInfo = kas.rpc.klay.getBlockWithConsensusInfoByNumber(
                new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
        BlockWithConsensusInfo.Block result = responseInfo.getResult();
        assertEquals(testBlock.getNumber(), result.getNumber());
    }

    @Test
    public void getCommitteeTest() throws IOException {
        Addresses response = kas.rpc.klay.getCommittee(DefaultBlockParameterName.LATEST).send();
        assertNull(response.getError());
    }

    @Test
    public void getCommitteeSizeTest() throws IOException {
        Quantity response = kas.rpc.klay.getCommitteeSize(DefaultBlockParameterName.LATEST).send();
        assertNull(response.getError());
    }

    @Test
    public void getCouncilTest() throws IOException {
        Addresses response = kas.rpc.klay.getCouncil(DefaultBlockParameterName.LATEST).send();
        assertNull(response.getError());
    }

    @Test
    public void getCouncilSizeTest() throws IOException {
        Quantity response = kas.rpc.klay.getCouncilSize(DefaultBlockParameterName.LATEST).send();
        assertNull(response.getError());
    }

    @Test
    public void getStorageAtTest() throws Exception {
        Response<String> response = kas.rpc.klay.getStorageAt(
                account,
                new DefaultBlockParameterNumber(0),
                DefaultBlockParameterName.LATEST).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void isSyncingTest() throws Exception {
        KlaySyncing response = kas.rpc.klay.isSyncing().send();
        KlaySyncing.Result result = response.getResult();
        assertFalse(result.isSyncing());
    }

    @Test
    public void callTest() {
        try {
            String encoded = kip17Contract.getMethod("symbol").encodeABI(Collections.emptyList());

            CallObject callObject = CallObject.createCallObject(
                    account,
                    kip17Contract.getContractAddress(),
                    new BigInteger("100000000", 16),
                    new BigInteger("5d21dba00", 16),
                    new BigInteger("0", 16),
                    encoded
            );

            Bytes symbol = kas.rpc.klay.call(callObject).send();
            assertNotNull(symbol);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void estimateGasTest() throws Exception {
        String encoded = kip17Contract.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17Contract.getContractAddress(),
                new BigInteger("100000000", 16),
                new BigInteger("5d21dba00", 16),
                new BigInteger("0", 16),
                encoded
        );
        Quantity response = kas.rpc.klay.estimateGas(callObject).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void estimateComputationCostTest() throws Exception {
        String encoded = kip17Contract.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17Contract.getContractAddress(),
                new BigInteger("100000000", 16),
                new BigInteger("5d21dba00", 16),
                new BigInteger("0", 16),
                encoded
        );
        Quantity response = kas.rpc.klay.estimateComputationCost(callObject, DefaultBlockParameterName.LATEST).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void getTransactionByBlockHashAndIndexTest() throws Exception {
        Transaction res = kas.rpc.klay.getTransactionByBlockHashAndIndex(blockHash, 0).send();
        assertEquals(blockHash, res.getResult().getBlockHash());
    }

    @Test
    public void getTransactionByBlockNumberAndIndexTest() throws IOException {
        Transaction res = kas.rpc.klay.getTransactionByBlockNumberAndIndex(
                blockNumber,
                0).send();
        assertEquals(blockHash, res.getResult().getBlockHash());
    }
//
    @Test
    public void getTransactionByHashTest() throws Exception {
        Transaction response = kas.rpc.klay.getTransactionByHash(transactionHash).send();
        Transaction.TransactionData result = response.getResult();
        assertEquals(transactionHash, result.getHash());
    }


    @Test
    public void getTransactionReceiptTest() throws Exception {
        TransactionReceipt response = kas.rpc.klay.getTransactionReceipt(transactionHash).send();
        assertEquals(transactionHash, response.getResult().getTransactionHash());
    }

    @Test
    public void sendRawTransactionTest() throws Exception {
        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setKlaytnCall(kas.rpc.getKlay())
                .setFrom(account)
                .setTo(KeyringFactory.generate().getAddress())
                .setValue(BigInteger.valueOf(1))
                .setGas(BigInteger.valueOf(25000))
                .build();

        kas.wallet.sign(account, valueTransfer);

        Bytes32 txHash = kas.rpc.klay.sendRawTransaction(valueTransfer).send();
        assertNotNull(txHash);
    }


    @Test
    public void getChainIdTest() throws Exception {
        Quantity response = kas.rpc.klay.getChainID().send();
        BigInteger result = response.getValue();
        assertEquals(BigInteger.valueOf(ChainId.BAOBAB_TESTNET), result);
    }

    @Test
    public void getClientVersionTest() throws IOException {
        Bytes response = kas.rpc.klay.getClientVersion().send();
        assertNull(response.getError());
    }

    @Test
    public void getGasPriceTest() throws Exception {
        Quantity response = kas.rpc.klay.getGasPrice().send();
        BigInteger result = response.getValue();
        assertEquals(new BigInteger("5d21dba00", 16), result); // 25,000,000,000 peb = 25 Gpeb
    }

    @Test
    public void getGasPriceAtTest() throws IOException {
        Quantity response = kas.rpc.klay.getGasPriceAt().send();
        BigInteger result = response.getValue();
        assertEquals(new BigInteger("5d21dba00", 16), result); // 25,000,000,000 peb = 25 Gpeb
    }


    @Test
    public void getProtocolVersionTest() throws Exception {
        String result = kas.rpc.klay.getProtocolVersion().send().getResult();
        assertEquals("0x40", result);
    }


    @Test
    public void getFilterChangesTest() throws Exception {
        KlayLogs response = kas.rpc.klay.getFilterChanges(
                "").send();
        assertNotNull(response);
    }

    @Test
    public void getFilterLogsTest() throws Exception {
        KlayLogs response = kas.rpc.klay.getFilterLogs(
                "0xf9081aeb4a0dc3109959748c659c8cd7").send();
        assertNotNull(response);
    }

    @Test
    public void getLogsTest() throws Exception {
        KlayLogFilter filter = new KlayLogFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                account,
                "0xe2649fe9fbaa75601408fc54200e3f9b2128e8fec7cea96c9a65b9caf905c9e3");
        KlayLogs response = kas.rpc.klay.getLogs(filter).send();
        assertNotNull(response);
    }

    @Test
    public void newBlockFilterTest() throws Exception {
        Response<String> response = kas.rpc.klay.newBlockFilter().send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void newFilterTest() throws Exception {
        KlayFilter filter = new KlayFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                account);
        filter.addSingleTopic("0xd596fdad182d29130ce218f4c1590c4b5ede105bee36690727baa6592bd2bfc8");
        Quantity response = kas.rpc.klay.newFilter(filter).send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void newPendingTransactionFilterTest() throws Exception {
        Response<String> response = kas.rpc.klay.newPendingTransactionFilter().send();
        String result = response.getResult();
        assertNotNull(result);
    }

    @Test
    public void uninstallFilterTest() throws Exception {
        Boolean response = kas.rpc.klay.uninstallFilter("0x0").send();
        assertNotNull(response);
    }

    @Test
    public void getSha3Test() throws IOException {
        Bytes response = kas.rpc.klay.sha3("0x123f").send();
        String result = response.getResult();
        assertEquals("0x7fab6b214381d6479bf140c3c8967efb9babe535025500d5b1dc2d549984b90b", result);
    }

    @Test
    public void getIdTest() throws Exception {
        Bytes netVersion = kas.rpc.net.getNetworkID().send();
        assertEquals(netVersion.getResult(), String.valueOf(ChainId.BAOBAB_TESTNET));
    }

    @Test
    public void isListeningTest() throws Exception {
        Boolean netListening = kas.rpc.net.isListening().send();
        assertTrue(netListening.getResult());
    }

    @Test
    public void getPeerCountTest() throws Exception {
        Quantity peerCount = kas.rpc.net.getPeerCount().send();
        assertTrue(peerCount.getValue().intValue() >= 0);
    }

    @Test
    public void getPeerCountByTypeTest() throws Exception {
        KlayPeerCount klayPeerCount = kas.rpc.net.getPeerCountByType().send();
        KlayPeerCount.PeerCount peerCount = klayPeerCount.getResult();
        assertTrue(peerCount.getTotal().intValue() >= 0);
    }
}
