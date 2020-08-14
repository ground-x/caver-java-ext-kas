package com.klaytn.caver.ext.kas.node;

import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.ext.kas.KAS;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class NodeAPITest {
    static KAS kas;
    static final String accessKey = "72ec21002ad2de341fd8e15801b5f80035ac8f5c";
    static final String secretAccessKey = "932424d7ca203ec601528155097e3d527722ec3e";

    static final String account = "0x69eaca9c1f4778b5615c1f9590e8a67a70dd76c5";
    static final String privateKey = "0x618f96289f403540ef6b030886dea850b5871367fbb0d9525d24f1d24336aef7";

    static KIP17 kip17Contract;
    static final String kip17ContractAddress = "0xeb18ce7952bc50650afc07ed52b879b2188c029f";

    @BeforeClass
    public static void config() throws Exception {
        kas = new KAS();
        kas.wallet.add(KeyringFactory.createFromPrivateKey(privateKey));
        kas.enableNodeAPI("https://node-api.beta.klaytn.io/v1/klaytn", "1001", accessKey, secretAccessKey);

//        deployContract(kas);
    }

    public static void deployContract(KAS kas) throws Exception{
        KIP17DeployParams deployParams = new KIP17DeployParams("KIP17", "KIP17");
        kip17Contract = KIP17.deploy(kas, deployParams, account);

        System.out.println("Contract address : " + kip17Contract.getContractAddress());
    }


    @Test
    public void accountCreatedTest() {
        try {
            Boolean created = kas.rpc.klay.accountCreated(account).send();
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
            Bytes res = kas.rpc.klay.getCode("0xc77ea4d397f90c1b3bded5ba5cb94a3c5bca6af7").send();

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
            Block response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
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
            String blockHash = "0x42e183e677f320ba11174330edaab8142b613c5e184ffa532dc648e9a3a1ce4d";
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
            String blockHash = "0x7b016db250e822c3caf7b8edffedb31f370ccc434c644a41f3709fd977b0c8b5";

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
            Block response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByNumber = kas.rpc.klay.getTransactionCountByNumber(
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
            Block response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Quantity responseByHash = kas.rpc.klay.getTransactionCountByHash(testBlock.getHash()).send();
            BigInteger result = responseByHash.getValue();
            assertEquals(testBlock.getTransactions().size(), result.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getBlockWithConsensusInfoByHashTest() throws Exception {
        Block response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
        Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

        BlockWithConsensusInfo responseInfo = kas.rpc.klay.getBlockWithConsensusInfoByHash(testBlock.getHash()).send();
        BlockWithConsensusInfo.Block result = responseInfo.getResult();
        assertEquals(testBlock.getHash(), result.getHash());
    }

    @Test
    public void getBlockWithConsensusInfoByNumberTest() throws Exception {
        Block response = kas.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
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
            KIP17 kip17 = new KIP17(kas, kip17ContractAddress);
            String encoded = kip17.getMethod("symbol").encodeABI(Collections.emptyList());

            CallObject callObject = CallObject.createCallObject(
                    account,
                    kip17ContractAddress,
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
        KIP17 kip17 = new KIP17(kas);

        String encoded = kip17.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17ContractAddress,
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
        KIP17 kip17 = new KIP17(kas);
        String encoded = kip17.getMethod("pause").encodeABI(Collections.emptyList());

        CallObject callObject = CallObject.createCallObject(
                account,
                kip17ContractAddress,
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
        String blockHash = "0x7b016db250e822c3caf7b8edffedb31f370ccc434c644a41f3709fd977b0c8b5";
        Transaction res = kas.rpc.klay.getTransactionByBlockHashAndIndex(blockHash, 0).send();
        assertEquals(blockHash, res.getResult().getBlockHash());
    }

    @Test
    public void getTransactionByBlockNumberAndIndexTest() throws IOException {
        long blockNumber = 35826799;
        String blockHash = "0x7b016db250e822c3caf7b8edffedb31f370ccc434c644a41f3709fd977b0c8b5";

        Transaction res = kas.rpc.klay.getTransactionByBlockNumberAndIndex(
                blockNumber,
                0).send();
        assertEquals(blockHash, res.getResult().getBlockHash());
    }
//
    @Test
    public void getTransactionByHashTest() throws Exception {
        String transactionHash = "0x3d98b0614f34b8769ecb462a11b311ab231a90e17125b3a37f874159069ab46a";

        Transaction response = kas.rpc.klay.getTransactionByHash(transactionHash).send();
        Transaction.TransactionData result = response.getResult();
        assertEquals(transactionHash, result.getHash());
    }


    @Test
    public void getTransactionReceiptTest() throws Exception {
        String transactionHash = "0x3d98b0614f34b8769ecb462a11b311ab231a90e17125b3a37f874159069ab46a";

        TransactionReceipt response = kas.rpc.klay.getTransactionReceipt(transactionHash).send();
        assertEquals(transactionHash, response.getResult().getTransactionHash());
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
    @Ignore
    public void getFilterChangesTest() throws Exception {
        KlayLogs response = kas.rpc.klay.getFilterChanges(
                "d5b93cf592b2050aee314767a02976c5").send();
        List<KlayLogs.LogResult> result = response.getResult();
        assertTrue("need test data", false);
    }

    @Test
    @Ignore
    public void getFilterLogsTest() throws Exception {
        KlayLogs response = kas.rpc.klay.getFilterLogs(
                "0xf9081aeb4a0dc3109959748c659c8cd7").send();
        List<KlayLogs.LogResult> result = response.getResult();
        assertTrue("need test data", false);
    }

    @Test
    @Ignore
    public void getLogsTest() throws Exception {
        KlayLogFilter filter = new KlayLogFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                account,
                "0xe2649fe9fbaa75601408fc54200e3f9b2128e8fec7cea96c9a65b9caf905c9e3");
        KlayLogs response = kas.rpc.klay.getLogs(filter).send();
        List<KlayLogs.LogResult> result = response.getResult();
        assertTrue("need test data", false);
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
    @Ignore
    public void uninstallFilterTest() throws Exception {
        Boolean response = kas.rpc.klay.uninstallFilter("0x0").send();
        java.lang.Boolean result = response.getResult();
        assertTrue("need test data", false);
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
