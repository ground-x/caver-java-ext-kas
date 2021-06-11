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

package xyz.groundx.caver_ext_kas.wallet;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.exception.KASAPIException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Account;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.AccountSummary;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.RegistrationStatusResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class KASWalletIntegrationTest {

    public static class generateTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Test
        public void generateTest() {
            List<String> addressList = caver.wallet.generate(3);
            assertEquals(3, addressList.size());

            for (int i = 0; i < addressList.size(); i++) {
                caver.wallet.remove(addressList.get(i));
            }
        }
    }

    public static class accountMigrationTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
            caver.kas.wallet.getKeyApi().getApiClient().setDebugging(true);
        }

        @Test
        public void migrateSingleKeyAccount() throws ApiException, IOException {
            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();
            SingleKeyring accountToBeMigrated = KeyringFactory.generate();
            accountsToBeMigrated.add(accountToBeMigrated);

            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating an account having single key should be succeeded.", "ok", response.getStatus());
        }

        @Test
        public void migrateSingleKeyAccount_ThrowException_AllFailed() throws ApiException, IOException {
            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();

            // Newly created account have a AccountKeyLegacy which means coupled-key in default.
            // Below will be failed to be migrated because it does not use coupled-key.
            SingleKeyring accountNotYetDecoupled = KeyringFactory.create(
                    KeyringFactory.generate().getAddress(),
                    PrivateKey.generate().getPrivateKey()
            );
            accountsToBeMigrated.add(accountNotYetDecoupled);

            try {
                caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            } catch (KASAPIException e) {
                assertEquals(
                        "An account with wrong private key should be failed to be migrated.",
                        "all failed",
                        e.getMessage()
                );
            }
        }

        @Test
        public void migrateMultipleSingleKeyAccounts() throws ApiException, IOException {
            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();
            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());

            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating multiple accounts having single key should be succeeded.", "ok", response.getStatus());
        }

        @Test
        public void migrateMultipleSingleKeyAccounts_ThrowException_PartiallyFailed() throws ApiException, IOException {
            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();

            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());

            // Newly created account have a AccountKeyLegacy which means coupled-key in default.
            // Below will be failed to be migrated because it does not use coupled-key.
            SingleKeyring accountNotYetDecoupled = KeyringFactory.create(
                    KeyringFactory.generate().getAddress(),
                    PrivateKey.generate().getPrivateKey()
            );
            accountsToBeMigrated.add(accountNotYetDecoupled);

            try {
                caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            } catch (KASAPIException e) {
                assertEquals(
                        "If there are accounts with the wrong private key, the migration should fail, but the correct accounts should succeed.",
                        "partially failed",
                        e.getMessage()
                );
            }
        }

        @Test
        public void migrate_throwException_withoutInitializingNodeAPI() throws ApiException {
            CaverExtKAS caverExtKAS = new CaverExtKAS();
            caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);

            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();
            accountsToBeMigrated.add(KeyringFactory.generate());

            try {
                // Without initializing Node API, endpoint url of caverExtKAS.rpc is Caver.DEFAULT_URL which is "localhost".
                caverExtKAS.wallet.walletAPI.migrateAccounts(accountsToBeMigrated);
            } catch (IOException e) {
                assertEquals(
                        "Using account migration feature without init node api should be failed.",
                        "Connection refused (Connection refused)",
                        e.getCause().getMessage()
                );
            }
        }

        @Test
        public void migrateWithInitializingAPIsManuallyWalletFirst() throws ApiException, IOException {
            CaverExtKAS caverExtKAS = new CaverExtKAS();
            caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);
            caverExtKAS.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API);

            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();
            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());

            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "Migrating multiple accounts with a single key should succeed after manually initializing each API.",
                    "ok",
                    response.getStatus()
            );
        }

        @Test
        public void migrateWithInitializingAPIsManuallyNodeFirst() throws ApiException, IOException {
            CaverExtKAS caverExtKAS = new CaverExtKAS();
            caverExtKAS.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API);
            caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);

            ArrayList<AbstractKeyring> accountsToBeMigrated = new ArrayList<>();
            accountsToBeMigrated.add(KeyringFactory.generate());
            accountsToBeMigrated.add(KeyringFactory.generate());

            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "Migrating multiple accounts with a single key should succeed after manually initializing each API.",
                    "ok",
                    response.getStatus()
            );
        }
    }

    public static class getAccountTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Test
        public void getAccountTest() {
            List<String> addressList = caver.wallet.generate(1);

            Account account = caver.wallet.getAccount(addressList.get(0));
            assertNotNull(account);
        }

        @Test
        public void getAccountThrow_ExceptionTest() {
            try {
                String unKnownAddress = "0x785ba1146cc1bed97b9c8d73e9293cc3b6bc3691";
                Account account = caver.wallet.getAccount(unKnownAddress);
            } catch (KASAPIException e) {
                assertEquals(400, e.getCode());
                assertEquals("Bad Request", e.getMessage());
                assertEquals(1061010, e.getResponseBody().getCode());
                assertEquals("data don't exist", e.getResponseBody().getMessage());
            }
        }
    }

    public static class removeTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Test
        public void removeTest() {
            List<String> addressList = caver.wallet.generate(1);

            boolean isRemoved = caver.wallet.remove(addressList.get(0));
            assertTrue(isRemoved);
        }
    }

    public static class isExistedTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Test
        public void isExistedTest() {
            List<String> addressList = caver.wallet.generate(1);
            boolean isExisted = caver.wallet.isExisted(addressList.get(0));

            assertTrue(isExisted);

            caver.wallet.remove(addressList.get(0));
        }

        @Test
        public void isExistedFailTest() {
            String unKnownAddress = "0x785ba1146cc1bed97b9c8d73e9293cc3b6bc3691";
            boolean isExisted = caver.wallet.isExisted(unKnownAddress);

            assertFalse(isExisted);
        }
    }

    public static class enableAccountTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void enableAccountTest() {
            try {
                List<String> addressList = caver.wallet.generate(1);
                caver.wallet.disableAccount(addressList.get(0));

                AccountSummary summary = caver.wallet.enableAccount(addressList.get(0));
                assertEquals(addressList.get(0), summary.getAddress());
            } catch (KASAPIException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void enableAccount_ThrowExceptionTest() {
            expectedException.expect(KASAPIException.class);

            List<String> addressList = caver.wallet.generate(1);
            caver.wallet.enableAccount(addressList.get(0));
        }
    }

    public static class disableAccountTest {
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void disableAccountTest() {
            List<String> addressList = caver.wallet.generate(1);
            AccountSummary accountSummary = caver.wallet.disableAccount(addressList.get(0));
            assertEquals(addressList.get(0), accountSummary.getAddress());
        }

        @Test
        public void disableAccount_ThrowExceptionTest() {
            expectedException.expect(KASAPIException.class);
            List<String> addressList = caver.wallet.generate(1);
            caver.wallet.remove(addressList.get(0));
            caver.wallet.disableAccount(addressList.get(0));
        }
    }

    public static class signTest {
        static CaverExtKAS caver;
        static String baseAccount;
        static String toAccount;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            List<String> addressList =  caver.wallet.generate(2);
            baseAccount = addressList.get(0);
            toAccount = addressList.get(1);

            Config.sendValue(baseAccount);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public SignatureData getSignatureData() {
            return new SignatureData(
                    "0x07f5",
                    "0xb99eefa471f4ff2a6be78c9f66d512a286084c73c07bfd81e8c4c056b31e003b",
                    "0x1b1e3b2b0e74fe1ce44e94c7485cc5e24f852cb0daf52c85a128a77bdd579310"
            );
        }

        @Test
        public void legacyTx() {
            LegacyTransaction tx = new LegacyTransaction.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .build();
            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void legacyTx_ThrowException_alreadyExistedSignature() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined.TxTypeLegacyTransaction cannot include more than one signature.");

            SignatureData signatureData = getSignatureData();

            LegacyTransaction tx = new LegacyTransaction.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void valueTransferTx() {
            ValueTransfer tx = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void valueTransferTx_appendSignature() {
            SignatureData signatureData = getSignatureData();

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void valueTransferMemoTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            ValueTransferMemo tx = new ValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void valueTransferMemoTx_appendSignature() {
            SignatureData signatureData = getSignatureData();
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            ValueTransferMemo tx = new ValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void smartContractDeploy() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                SmartContractDeploy tx = new SmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void smartContractDeploy_appendSignature() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);
                SignatureData signatureData = getSignatureData();

                SmartContractDeploy tx = new SmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(signatureData)
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void smartContractExecution() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                SmartContractExecution tx = new SmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void smartContractExecution_appendSignature() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                SmartContractExecution tx = new SmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void accountUpdate() {
            try {
                AccountUpdate tx = new AccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void accountUpdate_appendSignature() {
            try {
                AccountUpdate tx = new AccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void cancel() {
            try {
                Cancel tx = new Cancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void cancel_appendSignature() {
            try {
                Cancel tx = new Cancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void chainDataAnchoring() {
            try {
                ChainDataAnchoring tx = new ChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void chainDataAnchoring_appendSignature() {
            try {
                ChainDataAnchoring tx = new ChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferTx() {
            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferTx_appendSignature() {
            SignatureData signatureData = getSignatureData();

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemo tx = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoTx_appendSignature() {
            SignatureData signatureData = getSignatureData();
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemo tx = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeploy() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeploy tx = new FeeDelegatedSmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeploy_appendSignature() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);
                SignatureData signatureData = getSignatureData();

                FeeDelegatedSmartContractDeploy tx = new FeeDelegatedSmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(signatureData)
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecution() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecution tx = new FeeDelegatedSmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecution_appendSignature() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecution tx = new FeeDelegatedSmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdate() {
            try {
                FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdate_appendSignature() {
            try {
                FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancel() {
            try {
                FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancel_appendSignature() {
            try {
                FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoring() {
            try {
                FeeDelegatedChainDataAnchoring tx = new FeeDelegatedChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoring_appendSignature() {
            try {
                FeeDelegatedChainDataAnchoring tx = new FeeDelegatedChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferWithRatioTx() {
            FeeDelegatedValueTransferWithRatio tx = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferWithRatioTx_appendSignature() {
            SignatureData signatureData = getSignatureData();

            FeeDelegatedValueTransferWithRatio tx = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoWithRatioTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemoWithRatio tx = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoWithRatioTx_appendSignature() {
            SignatureData signatureData = getSignatureData();
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemoWithRatio tx = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setSignatures(signatureData)
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeployWithRatio() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeployWithRatio tx = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeployWithRatio_appendSignature() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);
                SignatureData signatureData = getSignatureData();

                FeeDelegatedSmartContractDeployWithRatio tx = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(signatureData)
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecutionWithRatio() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecutionWithRatio_appendSignature() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

//        @Test
//        public void feeDelegatedAccountUpdateWithRatio() {
//            try {
//                FeeDelegatedAccountUpdateWithRatio tx = new FeeDelegatedAccountUpdateWithRatio.Builder()
//                        .setKlaytnCall(caver.rpc.klay)
//                        .setFrom(baseAccount)
//                        .setGas(BigInteger.valueOf(50000))
//                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
//                        .setFeeRatio(BigInteger.valueOf(1))
//                        .build();
//
//                caver.wallet.sign(baseAccount, tx);
//                assertEquals(1, tx.getSignatures().size());
//            } catch (IOException e) {
//                e.printStackTrace();
//                fail();
//            }
//        }

//        @Test
//        public void feeDelegatedAccountUpdateWithRatio_appendSignature() {
//            try {
//                FeeDelegatedAccountUpdateWithRatio tx = new FeeDelegatedAccountUpdateWithRatio.Builder()
//                        .setKlaytnCall(caver.rpc.klay)
//                        .setFrom(baseAccount)
//                        .setGas(BigInteger.valueOf(50000))
//                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
//                        .setSignatures(getSignatureData())
//                        .setFeeRatio(BigInteger.valueOf(1))
//                        .build();
//
//                caver.wallet.sign(baseAccount, tx);
//                assertEquals(2, tx.getSignatures().size());
//            } catch (IOException e) {
//                e.printStackTrace();
//                fail();
//            }
//        }

        @Test
        public void feeDelegatedCancelWithRatio() {
            try {
                FeeDelegatedCancelWithRatio tx = new FeeDelegatedCancelWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancelWithRatio_appendSignature() {
            try {
                FeeDelegatedCancelWithRatio tx = new FeeDelegatedCancelWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoringWithRatio() {
            try {
                FeeDelegatedChainDataAnchoringWithRatio tx = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(1, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoringWithRatio_appendSignature() {
            try {
                FeeDelegatedChainDataAnchoringWithRatio tx = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeeRatio(BigInteger.valueOf(1))
                        .setSignatures(getSignatureData())
                        .build();

                caver.wallet.sign(baseAccount, tx);
                assertEquals(2, tx.getSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class signAsFeePayerTest {
        static CaverExtKAS caver;
        static String baseAccount;
        static String toAccount;
        static String userFeePayer;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();

            List<String> addressList =  caver.wallet.generate(2);
            baseAccount = addressList.get(0);
            toAccount = addressList.get(1);
            userFeePayer = Config.getFeePayerAddress();

            Config.sendValue(baseAccount);
            Config.sendValue(userFeePayer);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        public SignatureData getSignatureData() {
            return new SignatureData(
                    "0x07f5",
                    "0xb99eefa471f4ff2a6be78c9f66d512a286084c73c07bfd81e8c4c056b31e003b",
                    "0x1b1e3b2b0e74fe1ce44e94c7485cc5e24f852cb0daf52c85a128a77bdd579310"
            );
        }

        @Test
        public void feeDelegatedValueTransferTx() {
            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures().get(0)));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferTx_appendSignature() {
            SignatureData signatureData = getSignatureData();

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setFeePayer(userFeePayer)
                    .setFeePayerSignatures(signatureData)
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemo tx = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoTx_appendSignature() {
            SignatureData signatureData = getSignatureData();
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemo tx = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setFeePayer(userFeePayer)
                    .setFeePayerSignatures(signatureData)
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeploy() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeploy tx = new FeeDelegatedSmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeploy_appendSignature() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);
                SignatureData signatureData = getSignatureData();

                FeeDelegatedSmartContractDeploy tx = new FeeDelegatedSmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(signatureData)
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);
                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecution() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecution tx = new FeeDelegatedSmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecution_appendSignature() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecution tx = new FeeDelegatedSmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setFeePayer(userFeePayer)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeePayerSignatures(getSignatureData())
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdate() {
            try {
                FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdate_appendSignature() {
            try {
                FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancel() {
            try {
                FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancel_appendSignature() {
            try {
                FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoring() {
            try {
                FeeDelegatedChainDataAnchoring tx = new FeeDelegatedChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoring_appendSignature() {
            try {
                FeeDelegatedChainDataAnchoring tx = new FeeDelegatedChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeePayer(userFeePayer)
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeePayerSignatures(getSignatureData())
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferWithRatioTx() {
            FeeDelegatedValueTransferWithRatio tx = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferWithRatioTx_appendSignature() {
            SignatureData signatureData = getSignatureData();

            FeeDelegatedValueTransferWithRatio tx = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setFeePayer(userFeePayer)
                    .setFeeRatio(BigInteger.valueOf(1))
                    .setFeePayerSignatures(getSignatureData())
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoWithRatioTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemoWithRatio tx = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoWithRatioTx_appendSignature() {
            SignatureData signatureData = getSignatureData();
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemoWithRatio tx = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setFeePayer(userFeePayer)
                    .setFeePayerSignatures(getSignatureData())
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeployWithRatio() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeployWithRatio tx = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeployWithRatio_appendSignature() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);
                SignatureData signatureData = getSignatureData();

                FeeDelegatedSmartContractDeployWithRatio tx = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecutionWithRatio() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecutionWithRatio_appendSignature() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdateWithRatio() {
            try {
                FeeDelegatedAccountUpdateWithRatio tx = new FeeDelegatedAccountUpdateWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdateWithRatio_appendSignature() {
            try {
                FeeDelegatedAccountUpdateWithRatio tx = new FeeDelegatedAccountUpdateWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancelWithRatio() {
            try {
                FeeDelegatedCancelWithRatio tx = new FeeDelegatedCancelWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancelWithRatio_appendSignature() {
            try {
                FeeDelegatedCancelWithRatio tx = new FeeDelegatedCancelWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setFeePayer(userFeePayer)
                        .setFeePayerSignatures(getSignatureData())
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoringWithRatio() {
            try {
                FeeDelegatedChainDataAnchoringWithRatio tx = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoringWithRatio_appendSignature() {
            try {
                FeeDelegatedChainDataAnchoringWithRatio tx = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeePayer(userFeePayer)
                        .setFeeRatio(BigInteger.valueOf(1))
                        .setFeePayerSignatures(getSignatureData())
                        .build();

                caver.wallet.signAsFeePayer(userFeePayer, tx);

                assertEquals(userFeePayer, tx.getFeePayer());

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(2, tx.getFeePayerSignatures().size());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class signAsGlobalFeePayerTest {
        static CaverExtKAS caver;
        static String baseAccount;
        static String toAccount;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();

            List<String> addressList =  caver.wallet.generate(2);
            baseAccount = addressList.get(0);
            toAccount = addressList.get(1);

            Config.sendValue(baseAccount);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
        }

        public SignatureData getSignatureData() {
            return new SignatureData(
                    "0x07f5",
                    "0xb99eefa471f4ff2a6be78c9f66d512a286084c73c07bfd81e8c4c056b31e003b",
                    "0x1b1e3b2b0e74fe1ce44e94c7485cc5e24f852cb0daf52c85a128a77bdd579310"
            );
        }

        @Test
        public void feeDelegatedValueTransferTx() {
            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures().get(0)));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemo tx = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .build();

            try {
                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeploy() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeploy tx = new FeeDelegatedSmartContractDeploy.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecution() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecution tx = new FeeDelegatedSmartContractExecution.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdate() {
            try {
                FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancel() {
            try {
                FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoring() {
            try {
                FeeDelegatedChainDataAnchoring tx = new FeeDelegatedChainDataAnchoring.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferWithRatioTx() {
            FeeDelegatedValueTransferWithRatio tx = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedValueTransferMemoWithRatioTx() {
            String input = Numeric.toHexString("hello".getBytes(StandardCharsets.UTF_8));

            FeeDelegatedValueTransferMemoWithRatio tx = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(baseAccount)
                    .setTo(toAccount)
                    .setValue("0x1")
                    .setInput(input)
                    .setGas(BigInteger.valueOf(50000))
                    .setFeeRatio(BigInteger.valueOf(1))
                    .build();

            try {
                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (KASAPIException  | IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractDeployWithRatio() {
            try {
                String input = ContractSampleData.encodeConstructor(caver);

                FeeDelegatedSmartContractDeployWithRatio tx = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));

            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedSmartContractExecutionWithRatio() {
            try {
                String input = ContractSampleData.encodeABI(caver);
                FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setTo(toAccount)
                        .setInput(input)
                        .setGas(BigInteger.valueOf(50000))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedAccountUpdateWithRatio() {
            try {
                FeeDelegatedAccountUpdateWithRatio tx = new FeeDelegatedAccountUpdateWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(baseAccount))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));

                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedCancelWithRatio() {
            try {
                FeeDelegatedCancelWithRatio tx = new FeeDelegatedCancelWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setNonce(BigInteger.valueOf(1))
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));
                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void feeDelegatedChainDataAnchoringWithRatio() {
            try {
                FeeDelegatedChainDataAnchoringWithRatio tx = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                        .setKlaytnCall(caver.rpc.klay)
                        .setFrom(baseAccount)
                        .setGas(BigInteger.valueOf(50000))
                        .setInput("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405")
                        .setFeeRatio(BigInteger.valueOf(1))
                        .build();

                caver.wallet.signAsGlobalFeePayer(tx);

                assertTrue(Utils.isAddress(tx.getFeePayer()));
                assertEquals(1, tx.getSignatures().size());
                assertTrue(Utils.isEmptySig(tx.getSignatures().get(0)));
                assertEquals(1, tx.getFeePayerSignatures().size());
                assertFalse(Utils.isEmptySig(tx.getFeePayerSignatures()));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class ContractTest {
        static CaverExtKAS caver;
        static String baseAccount;
        static String toAccount;
        static Contract sampleContract;

        @BeforeClass
        public static void init() throws IOException, TransactionException {
            Config.init();
            caver = Config.getCaver();
            List<String> addressList =  caver.wallet.generate(2);
            baseAccount = addressList.get(0);
            toAccount = addressList.get(1);

            Config.sendValue(baseAccount);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);

            sampleContract = deploySampleContract(caver, baseAccount);
            ContractSampleData.storeStringData(sampleContract, baseAccount, "STR", "TEST");
            ContractSampleData.storeUintData(sampleContract, baseAccount, "UINT", 2020);
        }

        public static Contract deploySampleContract(Caver caver, String deployer) {
            try {
                Contract contract = new Contract(caver, ContractSampleData.ABI);
                ContractDeployParams deployParams = new ContractDeployParams(ContractSampleData.BINARY, "KVC");
                SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(5000000));

                contract.deploy(deployParams, sendOptions);
                return contract;
            } catch (KASAPIException | IOException | ReflectiveOperationException | TransactionException e) {
                e.printStackTrace();
                fail();
            }
            return null;
        }

        @Test
        public void deploy() {
            Contract contract = deploySampleContract(caver, baseAccount);
            assertNotNull(contract.getContractAddress());
        }

        @Test
        public void send_setString() {
            try {
                SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(500000));
                TransactionReceipt.TransactionReceiptData receiptData = sampleContract.send(sendOptions, ContractSampleData.FUNC_SET_STRING, "STR", "value");

                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }

            } catch (ReflectiveOperationException | IOException | TransactionException e) {
                e.printStackTrace();
                fail();
            } catch (KASAPIException e) {
                e.printStackTrace();
                fail(e.getResponseBody().getCode() + " " + e.getResponseBody().getMessage());
            }
        }

        @Test
        public void send_setUint() {
            try {
                SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(500000));
                TransactionReceipt.TransactionReceiptData receiptData = sampleContract.send(sendOptions, ContractSampleData.FUNC_SET_UINT, "INT", 8);

                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }

            } catch (ReflectiveOperationException | IOException | TransactionException e) {
                e.printStackTrace();
                fail();
            } catch (KASAPIException e) {
                e.printStackTrace();
                fail(e.getResponseBody().getCode() + " " + e.getResponseBody().getMessage());
            }
        }

        @Test
        public void call_getString() {
            try {
                List<Type> result = sampleContract.call(ContractSampleData.FUNC_GET_STRING, "STR");
                assertEquals("TEST", ((Utf8String) result.get(0)).getValue());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void call_getUint() {
            try {
                List<Type> result = sampleContract.call(ContractSampleData.FUNC_GET_UINT, "UINT");
                assertEquals(2020, ((Uint256) result.get(0)).getValue().intValue());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class KIP7Test {
        static CaverExtKAS caver;
        static String baseAccount;
        static String toAccount;

        static KIP7 kip7;

        public static final String CONTRACT_NAME = "Kale";
        public static final String CONTRACT_SYMBOL = "KALE";
        public static final int CONTRACT_DECIMALS = 18;
        public static final BigInteger CONTRACT_INITIAL_SUPPLY = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 100000 * 10^18

        @BeforeClass
        public static void init() throws Exception {
            Config.init();
            caver = Config.getCaver();
            List<String> addressList =  caver.wallet.generate(2);
            baseAccount = addressList.get(0);
            toAccount = addressList.get(1);

            Config.sendValue(baseAccount);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);

            kip7 = KIP7.deploy(caver, baseAccount, CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);
        }

        @Test
        public void deploy() throws Exception {
            KIP7 contract = KIP7.deploy(caver, baseAccount, CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);
            assertNotNull(contract.getContractAddress());
        }

        @Test
        public void getSymbol() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            assertEquals(CONTRACT_SYMBOL, kip7.symbol());
        }

        @Test
        public void transfer() throws Exception {
            SendOptions sendOptions = new SendOptions(baseAccount, (String)null);
            BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18
            TransactionReceipt.TransactionReceiptData receiptData = kip7.transfer(toAccount, transferAmount, sendOptions);
            assertEquals("0x1", receiptData.getStatus());
        }
    }

    public static class KIP17Test {
        public static final String CONTRACT_NAME = "NFT";
        public static final String CONTRACT_SYMBOL = "NFT_KALE";
        static CaverExtKAS caver;
        static String baseAccount;
        static KIP17 kip17;

        @BeforeClass
        public static void init() throws Exception {
            Config.init();
            caver = Config.getCaver();
            List<String> addressList =  caver.wallet.generate(1);
            baseAccount = addressList.get(0);

            Config.sendValue(baseAccount);
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);

            kip17 = KIP17.deploy(caver, baseAccount, CONTRACT_NAME, CONTRACT_SYMBOL);
        }

        @Test
        public void deploy() throws Exception {
            KIP17 kip17 = KIP17.deploy(caver, baseAccount, CONTRACT_NAME, CONTRACT_SYMBOL);
            assertNotNull(kip17.getContractAddress());
        }

        @Test
        public void symbol() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            assertEquals(CONTRACT_SYMBOL, kip17.symbol());
        }

        @Test
        public void mint() throws Exception {
            TransactionReceipt.TransactionReceiptData receiptData = kip17.mint(baseAccount, BigInteger.ZERO, new SendOptions(baseAccount, (String)null));
            assertEquals("0x1", receiptData.getStatus());
        }
    }
}
