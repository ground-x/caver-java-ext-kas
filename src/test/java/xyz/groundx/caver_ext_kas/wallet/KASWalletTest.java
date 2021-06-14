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

import com.klaytn.caver.account.AccountKeyLegacy;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.methods.response.AccountKey;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.exception.KASAPIException;
import xyz.groundx.caver_ext_kas.kas.wallet.Wallet;
import xyz.groundx.caver_ext_kas.kas.wallet.migration.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KASWalletTest {

    public static class generateTest {
        @Test
        public void generate() {
            Wallet wallet = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(wallet);

            try {
                when(wallet.createAccount()).thenReturn(new Account());

                List<String> addressList = kasWallet.generate(3);
                verify(wallet, times(3)).createAccount();
                assertEquals(3, addressList.size());
            } catch (KASAPIException | ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class accountMigrationTest {
        static CaverExtKAS caver;
        static RegistrationStatusResponse response;

        @BeforeClass
        public static void init() {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);
            caver.kas.wallet.getKeyApi().getApiClient().setDebugging(true);

            response = new RegistrationStatusResponse();
            response.setStatus("ok");
        }

        @Test
        public void migrateMultipleKeyAccount() throws NoSuchFieldException {
            Wallet wallet = mock(Wallet.class);
            ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

            String address = KeyringFactory.generate().getAddress();
            String[] multisigPrivateKeys = KeyringFactory.generateMultipleKeys(3);

            MigrationAccount accountWithMultisigPrivateKeys = new MigrationAccount(
                    address,
                    multisigPrivateKeys
            );

            accountsToBeMigrated.add(accountWithMultisigPrivateKeys);

            try {
                when(wallet.migrateAccounts(accountsToBeMigrated)).thenReturn(response);
                RegistrationStatusResponse actualResponse = wallet.migrateAccounts(accountsToBeMigrated);

                verify(wallet, times(1)).migrateAccounts(accountsToBeMigrated);
                assertEquals("A status of response should be ok", response.getStatus(), actualResponse.getStatus());
            } catch (KASAPIException | IOException | ApiException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void migrateRoleBasedKeyAccount() throws NoSuchFieldException {
            Wallet wallet = mock(Wallet.class);
            ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

            String address = KeyringFactory.generate().getAddress();
            List<String[]> roleBasedPrivateKeys = KeyringFactory.generateRoleBasedKeys(new int[]{4, 5, 6}, "entropy");

            MigrationAccount accountWithRoleBasedPrivateKeys = new MigrationAccount(
                    address,
                    roleBasedPrivateKeys
            );
            accountsToBeMigrated.add(accountWithRoleBasedPrivateKeys);

            try {
                when(wallet.migrateAccounts(accountsToBeMigrated)).thenReturn(response);
                RegistrationStatusResponse actualResponse = wallet.migrateAccounts(accountsToBeMigrated);

                verify(wallet, times(1)).migrateAccounts(accountsToBeMigrated);
                assertEquals("A status of response should be ok", response.getStatus(), actualResponse.getStatus());
            } catch (KASAPIException | IOException | ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class getAccountTest {
        static Account account;
        static CaverExtKAS caver;

        @BeforeClass
        public static void init() throws ApiException {
            Config.init();
            caver = Config.getCaver();
            caver.kas.wallet.getAccountApi().getApiClient().setDebugging(true);

            account = caver.kas.wallet.createAccount();
        }

        @Test
        public void getAccount() {
            Wallet wallet = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(wallet);
            try {
                when(wallet.getAccount(account.getAddress())).thenReturn(account);
                Account actual = kasWallet.getAccount(account.getAddress());

                verify(wallet, times(1)).getAccount(account.getAddress());
                assertNotNull(actual);
            } catch (KASAPIException | ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class isExistedTest {
        @Test
        public void isExisted() throws ApiException {
            String address = "0xb2Fd3a28efC3226638B7f92D9b48C370588c49F2";
            Wallet wallet = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(wallet);

            Account account = new Account();
            account.setAddress(address);

            when(wallet.getAccount(address)).thenReturn(account);

            kasWallet.isExisted(address);
            verify(wallet, times(1)).getAccount(address);
            assertTrue(kasWallet.isExisted(address));
        }

        @Test
        public void isExistedFail() {
            String address = "0xa809284C83b901eD106Aba4Ccda14628Af128e14";

            try {
                Wallet wallet = mock(Wallet.class);
                KASWallet kasWallet = new KASWallet(wallet);

                when(wallet.getAccount(address)).thenThrow(new ApiException());

                assertFalse(kasWallet.isExisted(address));
                verify(wallet, times(1)).getAccount(address);
            } catch (ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class removeTest {
        @Test
        public void remove() {
            String address = "0xb2Fd3a28efC3226638B7f92D9b48C370588c49F2";

            AccountStatus accountStatus = new AccountStatus();
            accountStatus.setStatus("deleted");

            try {
                Wallet wallet = mock(Wallet.class);
                KASWallet kasWallet = new KASWallet(wallet);
                when(wallet.deleteAccount(address)).thenReturn(accountStatus);

                assertTrue(kasWallet.remove(address));
                verify(wallet, times(1)).deleteAccount(address);
            } catch (ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class disableAccountTest {
        @Test
        public void disableAccount() {
            String address = "0x695D16Caccf08FfBE9680ED8C4e1950d74ceD8CE";

            AccountSummary accountSummary = new AccountSummary();
            accountSummary.setAddress(address);
            accountSummary.setKrn("krn:1001:wallet:d5c346f5-bb80-4f45-9093-57e25205cdc8:account-pool:pool");
            accountSummary.setUpdatedAt(1606900622l);

            try {
                Wallet wallet = mock(Wallet.class);
                KASWallet kasWallet = new KASWallet(wallet);

                when(wallet.disableAccount(address)).thenReturn(accountSummary);
                AccountSummary summary = kasWallet.disableAccount(address);

                assertNotNull(summary);
                verify(wallet, times(1)).disableAccount(address);
            } catch (ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class enableAccountTest {
        @Test
        public void enableAccount() {
            String address = "0x695D16Caccf08FfBE9680ED8C4e1950d74ceD8CE";

            AccountSummary accountSummary = new AccountSummary();
            accountSummary.setAddress(address);
            accountSummary.setKrn("krn:1001:wallet:d5c346f5-bb80-4f45-9093-57e25205cdc8:account-pool:pool");
            accountSummary.setUpdatedAt(1606900622l);

            try {
                Wallet wallet = mock(Wallet.class);
                KASWallet kasWallet = new KASWallet(wallet);

                when(wallet.enableAccount(address)).thenReturn(accountSummary);
                AccountSummary summary = kasWallet.enableAccount(address);

                assertNotNull(summary);
                verify(wallet, times(1)).enableAccount(address);
            } catch (ApiException e) {
                e.printStackTrace();
                fail();
            }
        }
    }


    public static class signTest {
        static String from = "0x758473e68179c446437b74ca8a74b58706792806";
        static String to = "0x76c6b1f34562ed7a843786e1d7f57d0d7948a6f1";
        static String gasPrice = "0x5d21dba00";
        static BigInteger gas = BigInteger.valueOf(25000);
        static BigInteger nonce = BigInteger.valueOf(1);
        static BigInteger chainId = BigInteger.valueOf(1001);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @BeforeClass
        public static void init() {
            Config.init();
        }

        public static ValueTransfer makeValueTransfer() {
            CaverExtKAS caverExtKAS = Config.getCaver();

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caverExtKAS.rpc.klay)
                    .setFrom(from)
                    .setTo(to)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setValue("0x1")
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .build();

            return valueTransfer;
        }

        public static FeeDelegatedValueTransfer makeFDValueTransfer() {
            CaverExtKAS caverExtKAS = Config.getCaver();

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setValue("0x1")
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .build();

            return feeDelegatedValueTransfer;
        }

        public static TransactionResult makeTransactionResult() {
            Signature signature = new Signature();
            signature.setV("0x7f6");
            signature.setR("0x137491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd2");
            signature.setS("0x3289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5a");

            TransactionResult result = new TransactionResult();
            result.setFrom(from);
            result.setTo(to);
            result.setValue("0x1");
            result.setGasPrice(gasPrice);
            result.setGas(gas.longValue());
            result.setNonce(nonce.longValue());
            result.setTypeInt(8l);
            result.setRlp("0x08f87e018505d21dba008261a89476c6b1f34562ed7a843786e1d7f57d0d7948a6f10194418dad870aaaad954f245b3d0c4c13ff6a6dc201f847f8458207f6a0137491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd2a03289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5a");
            result.setSignatures(Arrays.asList(signature));

            return result;
        }

        public static FDTransactionResult makeFDTransactionResult() {
            Signature signature = new Signature();
            signature.setV("0x7f6");
            signature.setR("0x137491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd2");
            signature.setS("0x3289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5a");

            FDTransactionResult result = new FDTransactionResult();
            result.setFrom(from);
            result.setTo(to);
            result.setValue("0x1");
            result.setGasPrice(gasPrice);
            result.setGas(gas.longValue());
            result.setNonce(nonce.longValue());
            result.setTypeInt(8l);
            result.setRlp("0x08f87e018505d21dba008261a89476c6b1f34562ed7a843786e1d7f57d0d7948a6f10194418dad870aaaad954f245b3d0c4c13ff6a6dc201f847f8458207f6a0137491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd2a03289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5a");
            result.setSignatures(Arrays.asList(signature));

            return result;
        }

        @Test
        public void signBasicTx() throws IOException, ApiException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            ValueTransfer valueTransfer = makeValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            ValueTransfer spyTx = spy(valueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestRawTransaction(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(1, spyTx.getSignatures().size());
        }

        @Test
        public void signBasicTxWithAppendSignature() throws IOException, ApiException {
            SignatureData signatureData = new SignatureData("0x4f8",
                    "0x237491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd5",
                    "0x4289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5b");

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            ValueTransfer valueTransfer = makeValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            valueTransfer.appendSignatures(signatureData);

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            ValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestRawTransaction(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(2, spyTx.getSignatures().size());
        }

        @Test
        public void signLegacyTxWithAppendSignature_thrownException() throws IOException, ApiException {
            expectedException.expectMessage("Signatures already defined.");
            expectedException.expect(RuntimeException.class);

            SignatureData signatureData = new SignatureData("0x4f8",
                    "0x237491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd5",
                    "0x4289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5b");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setValue("0x1")
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .build();

            legacyTransaction.appendSignatures(signatureData);

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            legacyTransaction.setKlaytnCall(klay);

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            LegacyTransaction spyTx = spy(legacyTransaction);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, legacyTransaction);

        }

        @Test
        public void signFDTx() throws IOException, ApiException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = makeFDValueTransfer();
            feeDelegatedValueTransfer.setKlaytnCall(klay);

            FeeDelegatedValueTransfer spyTx = spy(feeDelegatedValueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByGlobalFeePayer(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(1, spyTx.getSignatures().size());
        }

        @Test
        public void signFDTxWithAppendSignatures() throws ApiException, IOException {
            SignatureData signatureData = new SignatureData("0x4f8",
                    "0x237491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd5",
                    "0x4289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5b");

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = makeFDValueTransfer();
            feeDelegatedValueTransfer.setKlaytnCall(klay);
            feeDelegatedValueTransfer.appendSignatures(signatureData);

            FeeDelegatedValueTransfer spyTx = spy(feeDelegatedValueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByGlobalFeePayer(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(2, spyTx.getSignatures().size());
        }

        @Test
        public void signWithDuplicatedSignature() throws IOException, ApiException {
            SignatureData signatureData = new SignatureData("0x7f6",
                    "0x137491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd2",
                    "0x3289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5a");

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            ValueTransfer valueTransfer = makeValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            valueTransfer.appendSignatures(signatureData);

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            ValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestRawTransaction(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(1, spyTx.getSignatures().size());
        }

        @Test
        public void signWithMultiSignKey_throwException() throws ApiException, IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Not supported: Using multiple keys in an account is currently not supported.");

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyWeightedMultiSig.getType(), null));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            ValueTransfer valueTransfer = makeValueTransfer();
            valueTransfer.setKlaytnCall(klay);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            ValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);
        }

        @Test
        public void signWithMultiSigKeyInRoleKey_throwException() throws ApiException, IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Not supported: Using multiple keys in an account is currently not supported.");

            String encodedKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyRoleBased roleBased = new AccountKeyRoleBased(Arrays.asList(AccountKeyWeightedMultiSig.decode(encodedKey), new AccountKeyLegacy(), new AccountKeyLegacy()));

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyRoleBased.getType(), roleBased));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            ValueTransfer valueTransfer = makeValueTransfer();
            valueTransfer.setKlaytnCall(klay);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            ValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);
        }

        @Test
        public void signAccountUpdateTxWithRoleKey() throws IOException, ApiException {
            String encodedKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";
            AccountKeyRoleBased roleBased = new AccountKeyRoleBased(Arrays.asList(AccountKeyWeightedMultiSig.decode(encodedKey), new AccountKeyLegacy(), new AccountKeyLegacy()));

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyRoleBased.getType(), roleBased));

            AccountUpdate accountUpdate = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(from))
                    .setGas(BigInteger.valueOf(50000))
                    .setNonce(BigInteger.ONE)
                    .setGasPrice("0x5d21dba00")
                    .setChainId(BigInteger.valueOf(1001))
                    .build();

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            accountUpdate.setKlaytnCall(klay);

            AccountUpdate spyTx = spy(accountUpdate);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestRawTransaction(any());
            verify(spyTx).appendSignatures(anyList());
            assertEquals(1, spyTx.getSignatures().size());
        }

        @Test
        public void signAccountUpdateTxWithMultiSigKeyinRoleKey() throws IOException, ApiException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Not supported: Using multiple keys in an account is currently not supported.");

            String encodedKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";
            AccountKeyRoleBased roleBased = new AccountKeyRoleBased(Arrays.asList(new AccountKeyLegacy(), AccountKeyWeightedMultiSig.decode(encodedKey), new AccountKeyLegacy()));

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyRoleBased.getType(), roleBased));

            AccountUpdate accountUpdate = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setAccount(com.klaytn.caver.account.Account.createWithAccountKeyLegacy(from))
                    .setGas(BigInteger.valueOf(50000))
                    .setNonce(BigInteger.ONE)
                    .setGasPrice("0x5d21dba00")
                    .setChainId(BigInteger.valueOf(1001))
                    .build();

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            accountUpdate.setKlaytnCall(klay);

            AccountUpdate spyTx = spy(accountUpdate);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestRawTransaction(any())).thenReturn(makeTransactionResult());

            kasWallet.sign(from, spyTx);
        }
    }

    public static class signAsFeePayerTest {
        static String from = "0x418dad870aaaad954f245b3d0c4c13ff6a6dc201";
        static String to = "0x76c6b1f34562ed7a843786e1d7f57d0d7948a6f1";
        static String feePayer = "0x44ee3906a7a2007762e9d706df6e4ef63fa1eda8";
        static String value = "0x1";
        static BigInteger gas = BigInteger.valueOf(25000);
        static BigInteger nonce = BigInteger.ONE;
        static String gasPrice = "0x5d21dba00";
        static BigInteger chainId = BigInteger.valueOf(1001);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public static FeeDelegatedValueTransfer makeFDValueTransfer() {
            SignatureData signatureData = new SignatureData("0x07f6",
                    "0xef617aa7de05d4e807bbc5b9c67ecf05ef067ca6a01aeff6cc81b1f4548216c5",
                    "0x23510f58c1dd82c583cd47302aac1d077c97926ba9bcaa9ca4ad0bc809cf0890");

            FeeDelegatedValueTransfer transfer = new FeeDelegatedValueTransfer.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setValue(value)
                    .setFeePayer(feePayer)
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .setSignatures(signatureData)
                    .build();

            return transfer;
        }

        public static FDTransactionResult makeFDTransactionResult() {
            Signature signature = new Signature();
            signature.setV("0x7f6");
            signature.setR("0xef617aa7de05d4e807bbc5b9c67ecf05ef067ca6a01aeff6cc81b1f4548216c5");
            signature.setS("0x23510f58c1dd82c583cd47302aac1d077c97926ba9bcaa9ca4ad0bc809cf0890");

            FDTransactionResult result = new FDTransactionResult();
            result.setFrom(from);
            result.setTo(to);
            result.setValue(value);
            result.setGasPrice(gasPrice);
            result.setGas(gas.longValue());
            result.setNonce(nonce.longValue());
            result.setFeePayer(feePayer);
            result.setTypeInt(9l);
            result.setRlp("0x09f8dc038505d21dba0082c3509476c6b1f34562ed7a843786e1d7f57d0d7948a6f10194758473e68179c446437b74ca8a74b58706792806f847f8458207f6a0ef617aa7de05d4e807bbc5b9c67ecf05ef067ca6a01aeff6cc81b1f4548216c5a023510f58c1dd82c583cd47302aac1d077c97926ba9bcaa9ca4ad0bc809cf08909444ee3906a7a2007762e9d706df6e4ef63fa1eda8f847f8458207f6a0637426d1221cf48837634c86fb31c96dc2c5f2f847cbdd8cff959308f2331cf0a02299f5f184fa1b9ba545ba6fa90474695b6e4860255f8c3837ec31495f0e0f39");
            result.setSignatures(Arrays.asList(signature));
            result.setTransactionHash("0x2a7c62262e4ff747afe429992f4f4dd48dc085ccc93c4993c8dd798593fa2d57");

            return result;
        }

        @Test
        public void signFDTx() throws ApiException, IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByUser(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsFeePayer(feePayer, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByUser(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(1, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void signFDTxWithAppendSignature() throws IOException, ApiException {
            SignatureData signatureData = new SignatureData("0x4f8",
                    "0x237491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd5",
                    "0x4289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5b");

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            valueTransfer.appendFeePayerSignatures(signatureData);
            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByUser(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsFeePayer(feePayer, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByUser(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(2, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void signFDTxWithDuplicatedSignature() throws IOException, ApiException {
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = (FeeDelegatedValueTransfer)TransactionDecoder.decode(makeFDTransactionResult().getRlp());
            SignatureData signatureData = feeDelegatedValueTransfer.getFeePayerSignatures().get(0);

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.appendFeePayerSignatures(signatureData);

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            valueTransfer.setKlaytnCall(klay);


            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByUser(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsFeePayer(feePayer, spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByUser(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(1, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void signWithMultiSigKey_throwException() throws ApiException, IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Not supported: Using multiple keys in an account is currently not supported.");

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyWeightedMultiSig.getType(), null));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setKlaytnCall(klay);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByUser(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsFeePayer(feePayer, spyTx);
        }

        @Test
        public void signWithMultiSigKeyInRoleKey_throwException() throws ApiException, IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Not supported: Using multiple keys in an account is currently not supported.");

            String encodedKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyRoleBased roleBased = new AccountKeyRoleBased(Arrays.asList(new AccountKeyLegacy(), new AccountKeyLegacy(), AccountKeyWeightedMultiSig.decode(encodedKey)));

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyRoleBased.getType(), roleBased));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setKlaytnCall(klay);

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByUser(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsFeePayer(feePayer, spyTx);
        }
    }

    public static class signAsGlobalFeePayerTest {
        static String from = "0xac3bd4b108f56ffcec6339fda14f649be01819c8";
        static String to = "0x76c6b1f34562ed7a843786e1d7f57d0d7948a6f1";
        static String feePayer = "0x1b71a63903e35371e2fc41c6012effb99b9a2c0f";
        static String gasPrice = "0x5d21dba00";
        static BigInteger gas = BigInteger.valueOf(50000);
        static BigInteger nonce = BigInteger.valueOf(3);
        static BigInteger chainId = BigInteger.valueOf(1001);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public static FeeDelegatedValueTransfer makeFDValueTransfer() {
            SignatureData signatureData = new SignatureData("0x07f5",
                    "0x7b253fdb79561ba2d24ee39a0ba0a6edf0a2df60ebeae6713015288a0c0cfb20",
                    "0x150c054bb93919b4fb3bed927b5dfb7162a54c29a6da52c9ad60ce6e2b62ef25");

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setValue("0x1")
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .setSignatures(signatureData)
                    .build();

            return feeDelegatedValueTransfer;
        }

        public static FDTransactionResult makeFDTransactionResult() {
            Signature signature = new Signature();
            signature.setV("0x07f5");
            signature.setR("0x7b253fdb79561ba2d24ee39a0ba0a6edf0a2df60ebeae6713015288a0c0cfb20");
            signature.setS("0x150c054bb93919b4fb3bed927b5dfb7162a54c29a6da52c9ad60ce6e2b62ef25");

            FDTransactionResult result = new FDTransactionResult();
            result.setFrom(from);
            result.setTo(to);
            result.setValue("0x1");
            result.setGasPrice(gasPrice);
            result.setGas(gas.longValue());
            result.setNonce(nonce.longValue());
            result.setFeePayer(feePayer);
            result.setTypeInt(9l);
            result.setRlp("0x09f8dc808505d21dba0082c3509476c6b1f34562ed7a843786e1d7f57d0d7948a6f10194ac3bd4b108f56ffcec6339fda14f649be01819c8f847f8458207f5a07b253fdb79561ba2d24ee39a0ba0a6edf0a2df60ebeae6713015288a0c0cfb20a0150c054bb93919b4fb3bed927b5dfb7162a54c29a6da52c9ad60ce6e2b62ef25941b71a63903e35371e2fc41c6012effb99b9a2c0ff847f8458207f6a03be553ff9d261860fbb0c4b2c2d6ad7dd8093a35ff4ee7ba7cccd9f88841e289a0559530699189bccbf9684af9e66e7609aa6a09253f98f1bfe626856f089a9414");
            result.setSignatures(Arrays.asList(signature));

            return result;
        }

        @Test
        public void signFDTx() throws IOException, ApiException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setKlaytnCall(klay);
            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);

            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsGlobalFeePayer(spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByGlobalFeePayer(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(1, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void signFDTxWithAppendSignature() throws IOException, ApiException {
            SignatureData signatureData = new SignatureData("0x4f8",
                    "0x237491673d0014cca219705291f3ee7350295ef549069e639b5e9d0d8014ffd5",
                    "0x4289ed52548303f7f2f5fbb85e88ba7f373026178d30105f9738c71ae07b4a5b");

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setFeePayer(feePayer);
            valueTransfer.appendFeePayerSignatures(signatureData);
            valueTransfer.setFeePayer("0x");

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            valueTransfer.setKlaytnCall(klay);


            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsGlobalFeePayer(spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByGlobalFeePayer(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(2, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void signDuplicatedSignature() throws IOException, ApiException {
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = (FeeDelegatedValueTransfer)TransactionDecoder.decode(makeFDTransactionResult().getRlp());

            SignatureData signatureData = feeDelegatedValueTransfer.getFeePayerSignatures().get(0);

            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setFeePayer(feePayer);
            valueTransfer.appendFeePayerSignatures(signatureData);
            valueTransfer.setFeePayer("0x");

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            valueTransfer.setKlaytnCall(klay);


            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);

            FeeDelegatedValueTransfer spyTx = spy(valueTransfer);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsGlobalFeePayer(spyTx);

            verify(spyTx).fillTransaction();
            verify(spyTx).getRLPEncoding();
            verify(walletAPI).requestFDRawTransactionPaidByGlobalFeePayer(any());
            verify(spyTx).appendFeePayerSignatures(anyList());
            assertEquals(1, spyTx.getFeePayerSignatures().size());
        }

        @Test
        public void NotMatchedFeePayer_throwException() throws IOException, ApiException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid fee payer: The address of the fee payer defined in the transaction does not match the address of the global fee payer. To sign with a global fee payer, you must define the global fee payer's address in the feePayer field, or the feePayer field must not be defined.");
            FeeDelegatedValueTransfer valueTransfer = makeFDValueTransfer();
            valueTransfer.setFeePayer(to);

            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Wallet walletAPI = mock(Wallet.class);
            KASWallet kasWallet = new KASWallet(walletAPI);
            
            when(walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(any())).thenReturn(makeFDTransactionResult());

            kasWallet.signAsGlobalFeePayer(valueTransfer);
        }
    }
}