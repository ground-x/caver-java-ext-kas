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

package xyz.groundx.caver_ext_kas.kas.wallet;

import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7ConstantData;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdate;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.squareup.okhttp.Call;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.kas.wallet.accountkey.*;
import xyz.groundx.caver_ext_kas.kas.wallet.migration.MigrationAccount;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WalletAPITest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public static CaverExtKAS caver;
    static String userFeePayer;


    static String baseAccount = "";
    static String toAccount = "";
    static String multiSigAddress = "";

    static Account multiSigAccount;

    static String ftContractAddress = "";


    static String txHash;
    static String krn;

    @BeforeClass
    public static void init() throws IOException, TransactionException, ApiException {
        Config.init();
        caver = Config.getCaver();
        caver.kas.wallet.getApiClient().setDebugging(true);
        userFeePayer = Config.getFeePayerAddress();

        baseAccount = baseAccount.equals("") ? makeAccount().getAddress() : baseAccount;
        toAccount = toAccount.equals("") ? makeAccount().getAddress() : toAccount;
        multiSigAddress = multiSigAddress.equals("") ? createMultiSig().getAddress() : multiSigAddress;
        multiSigAccount = caver.kas.wallet.getAccount(multiSigAddress);

        krn = multiSigAccount.getKrn();
        //Send balance to baseAccount
        com.klaytn.caver.methods.response.TransactionReceipt.TransactionReceiptData receiptData = Config.sendValue(baseAccount);
        txHash = receiptData.getTransactionHash();
//        java.lang.RuntimeException: insufficient funds of the sender for value

        //Send balance to multiSig address
        Config.sendValue(multiSigAddress);

        //Send balance to userFeePayer
        BigInteger balance = Config.getBalance(userFeePayer);
        BigInteger milliKLAY = new BigInteger(Utils.convertToPeb("999", Utils.KlayUnit.mKLAY));
        if(balance.compareTo(milliKLAY) <= 0) {
            Config.sendValue(userFeePayer);
        }

        ftContractAddress = ftContractAddress.equals("") ? deployKIP7() : ftContractAddress;
    }

    public static Account makeAccount() throws ApiException{
        return caver.kas.wallet.createAccount();
    }

    public static Account createMultiSig() {
        try {
            Account baseAccount = makeAccount();
            Account multiSigAccount = makeAccount();
            Account multiSigAccount2 = makeAccount();

            Config.sendValue(baseAccount.getAddress());

            List<Account> accountList = Arrays.asList(baseAccount, multiSigAccount, multiSigAccount2);
            List<MultisigKey> multiSigKeys = accountList.stream().map(account -> {
                MultisigKey multisigKey = new MultisigKey();
                multisigKey.setWeight((long)2);
                multisigKey.setPublicKey(account.getPublicKey());

                return multisigKey;
            }).collect(Collectors.toList());

            MultisigAccountUpdateRequest request = new MultisigAccountUpdateRequest();
            request.setThreshold((long)3);
            request.setWeightedKeys(multiSigKeys);

            MultisigAccount account = caver.kas.wallet.updateToMultiSigAccount(baseAccount.getAddress(), request);
            Thread.sleep(3000);
            return baseAccount;
        } catch (ApiException | TransactionException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String deployKIP7() {
        try {
            ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
            String input = encodeContractDeploy();

            request.setFrom(baseAccount);
            request.setInput(Utils.addHexPrefix(input));
            request.setGas(5500000L);
            request.submit(true);

            TransactionResult transactionResult = caver.kas.wallet.requestSmartContractDeploy(request);

            TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            return processor.waitForTransactionReceipt(transactionResult.getTransactionHash()).getContractAddress();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return null;
    }


    private static String encodeContractDeploy() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        KIP7 kip7 = new KIP7(caver);
        BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
        String input = ABI.encodeContractDeploy(kip7.getConstructor(), KIP7ConstantData.BINARY, Arrays.asList("TEST", "TES", 18, initialSupply));

        return input;
    }

    private static String encodeKIP7Transfer(String address) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        KIP7 kip7 = new KIP7(caver, address);
        BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
        String input = kip7.getMethod("transfer").encodeABI(Arrays.asList(toAccount, transferAmount));

        return input;
    }

    private static void sendValueTransferForMultiSig(String fromAddress, String toAddress) throws ApiException {
        ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
        request.setFrom(fromAddress);
        request.setTo(toAddress);
        request.setValue("0x1");
        request.setSubmit(true);

        caver.kas.wallet.requestValueTransfer(request);
    }

    private static KeyTypeLegacy createLegacyKeyType() {
        return new KeyTypeLegacy();
    }

    private static KeyTypeFail createFailKeyType() {
        return new KeyTypeFail();
    }

    private static KeyTypePublic createPublicKeyType() throws ApiException {
        Account account = makeAccount();

        KeyTypePublic keyType = new KeyTypePublic();
        keyType.setKey(account.getPublicKey());

        return keyType;
    }

    private static MultisigKey createMultiSig(long weight, String publicKey) {
        MultisigKey key = new MultisigKey();
        key.setWeight(weight);
        key.setPublicKey(publicKey);

        return key;
    }

    private static KeyTypeMultiSig createWeightedMultiSigKeyType(Account account) throws ApiException {
        Account account1 = makeAccount();
        Account account2 = makeAccount();

        MultisigUpdateKey multisigUpdateKey = new MultisigUpdateKey();
        multisigUpdateKey.setThreshold((long)2);
        multisigUpdateKey.setWeightedKeys(
                Arrays.asList(createMultiSig(2, account.getPublicKey()),
                            createMultiSig(1, account1.getPublicKey()),
                            createMultiSig(1, account2.getPublicKey()))
        );

        KeyTypeMultiSig keyTypeMultiSig = new KeyTypeMultiSig(multisigUpdateKey);

        return keyTypeMultiSig;
    }

    private static KeyTypeRoleBased createRoleBasedKeyType(Account account) throws ApiException {
        KeyTypePublic txKey = createPublicKeyType();
        KeyTypePublic accountUpdateKey = createPublicKeyType();
        KeyTypeMultiSig fdKey = createWeightedMultiSigKeyType(account);

        OneOfRoleBasedUpdateKeyTypeKeyItems[] keys = new OneOfRoleBasedUpdateKeyTypeKeyItems[] {txKey, accountUpdateKey, fdKey};
        KeyTypeRoleBased roleBasedUpdateKeyType = new KeyTypeRoleBased(Arrays.asList(keys));

        return roleBasedUpdateKeyType;
    }

    @Before
    public void beforeSleep() throws InterruptedException {
        Thread.sleep(5000);
    }

    @Test
    public void makeUncompressedFormat_LegacyKeyType() {
        KeyTypeLegacy type = new KeyTypeLegacy();

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) type);
        assertEquals(KeyTypeLegacy.KEY_TYPE, type.getKeyType().longValue());
    }

    @Test
    public void makeUncompressedFormat_FailKeyType() {
        KeyTypeFail type = new KeyTypeFail();

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) type);
        assertEquals(KeyTypeFail.KEY_TYPE, type.getKeyType().longValue());
    }

    @Test
    public void makeUncompressedFormat_PublicKeyType() {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        String actual = "0x24002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        KeyTypePublic type = new KeyTypePublic();
        type.setKey(actual);

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) type);
        assertEquals(expected, type.getKey());
    }

    @Test
    public void makeUncompressedFormat_alreadyPubKeyTag() {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        KeyTypePublic type = new KeyTypePublic();
        type.setKey(expected);

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) type);
        assertEquals(expected, type.getKey());
    }

    @Test
    public void makeUncompressedFormat_WeightedSigKey() {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        String actual = "0x24002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";

        MultisigKey key1 = new MultisigKey();
        key1.setPublicKey(expected);

        MultisigKey key2 = new MultisigKey();
        key2.setPublicKey(actual);

        MultisigUpdateKey multisigUpdateKey = new MultisigUpdateKey();
        multisigUpdateKey.setWeightedKeys(Arrays.asList(key1, key2));

        KeyTypeMultiSig multiSig = new KeyTypeMultiSig();
        multiSig.setKey(multisigUpdateKey);

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) multiSig);
        assertEquals(expected, multiSig.getKey().getWeightedKeys().get(0).getPublicKey());
        assertEquals(expected, multiSig.getKey().getWeightedKeys().get(1).getPublicKey());
    }

    @Test
    public void makeUncompressedFormat_roleBasedKey() throws ApiException {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        String actual = "0x24002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";

        Account account = makeAccount();
        KeyTypeRoleBased roleBased = createRoleBasedKeyType(account);

        List<OneOfRoleBasedUpdateKeyTypeKeyItems> keyList = roleBased.getKey();

        ((KeyTypePublic)keyList.get(0)).setKey(actual);
        ((KeyTypePublic)keyList.get(1)).setKey(expected);
        MultisigUpdateKey multiSig = ((KeyTypeMultiSig)keyList.get(2)).getKey();
        List<MultisigKey> multisigKeyList = multiSig.getWeightedKeys();

        for(int i=0; i<multisigKeyList.size(); i++) {
            multisigKeyList.get(i).setPublicKey(actual);
        }

        caver.kas.wallet.makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey) roleBased);

        assertEquals(expected,((KeyTypePublic)keyList.get(0)).getKey());
        assertEquals(expected,((KeyTypePublic)keyList.get(1)).getKey());

        for(int i=0; i<multisigKeyList.size(); i++) {
            assertEquals(expected, multisigKeyList.get(i).getPublicKey());
        }
    }


    @Test
    public void createAccount() {
        try {
            Account account = caver.kas.wallet.createAccount();
            assertNotNull(account);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void createAccountAsync() {
        try {
            CompletableFuture<Account> future = new CompletableFuture<>();
            Call call = caver.kas.wallet.createAccountAsync(new ApiCallback<Account>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Account result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountList() {
        try {
            Accounts accounts = caver.kas.wallet.getAccountList();
            assertNotNull(accounts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountListWithOption() {
        try {
            WalletQueryOptions options = new WalletQueryOptions();
            options.setSize(5l);
            options.setFromTimestamp("2021-01-01 00:00:00");
            options.setToTimestamp(new Date().getTime() / 1000);
            Accounts accounts = caver.kas.wallet.getAccountList(options);
            assertNotNull(accounts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountListAsync() {
        try {
            CompletableFuture<Accounts> future = new CompletableFuture<>();
            Call call = caver.kas.wallet.getAccountListAsync(new ApiCallback<Accounts>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Accounts result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccount() {
        try {
            Account expected = makeAccount();
            Account actual = caver.kas.wallet.getAccount(expected.getAddress());
            assertEquals(expected.getAddress(), actual.getAddress());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountAsync() {
        CompletableFuture<Account> future = new CompletableFuture<Account>();
        try {
            Account expected = makeAccount();
            Call call = caver.kas.wallet.getAccountAsync(expected.getAddress(), new ApiCallback<Account>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Account result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void deleteAccount() {
        try {
            Account expected = makeAccount();
            AccountStatus accountStatus = caver.kas.wallet.deleteAccount(expected.getAddress());

            assertEquals("deleted", accountStatus.getStatus());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void deleteAccountAsync() {
        CompletableFuture<AccountStatus> future = new CompletableFuture<>();

        try {
            Account expected = makeAccount();
            caver.kas.wallet.deleteAccountAsync(expected.getAddress(), new ApiCallback<AccountStatus>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void disableAccount() {
        try {
            Account expected = makeAccount();
            AccountSummary status = caver.kas.wallet.disableAccount(expected.getAddress());
            assertEquals(expected.getAddress(), status.getAddress());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void disableAccountAsync() {
        CompletableFuture<AccountSummary> future = new CompletableFuture<>();

        try {
            Account expected = makeAccount();
            caver.kas.wallet.disableAccountAsync(expected.getAddress(), new ApiCallback<AccountSummary>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountSummary result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void enableAccount() {
        try {
            Account expected = makeAccount();
            caver.kas.wallet.disableAccount(expected.getAddress());
            AccountSummary enableSummary = caver.kas.wallet.enableAccount(expected.getAddress());

            assertEquals(expected.getAddress(), enableSummary.getAddress());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void enableAccountAsync() {
        CompletableFuture<AccountSummary> future = new CompletableFuture<>();

        try {
            Account expected = makeAccount();
            caver.kas.wallet.disableAccount(expected.getAddress());

            caver.kas.wallet.enableAccountAsync(expected.getAddress(), new ApiCallback<AccountSummary>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountSummary result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void updateAccountToMultiSig() {
        try {
            Account baseAccount = makeAccount();
            Account multiSigAccount = makeAccount();
            Account multiSigAccount2 = makeAccount();

            Config.sendValue(baseAccount.getAddress());

            List<Account> accountList = Arrays.asList(baseAccount, multiSigAccount, multiSigAccount2);
            List<MultisigKey> multiSigKeys = accountList.stream().map(account -> {
                MultisigKey multisigKey = new MultisigKey();
                multisigKey.setWeight((long)2);
                multisigKey.setPublicKey(account.getPublicKey());

                return multisigKey;
            }).collect(Collectors.toList());

            MultisigAccountUpdateRequest request = new MultisigAccountUpdateRequest();
            request.setThreshold((long)3);
            request.setWeightedKeys(multiSigKeys);

            MultisigAccount account = caver.kas.wallet.updateToMultiSigAccount(baseAccount.getAddress(), request);
            assertEquals((long)3, account.getThreshold().longValue());
        } catch (ApiException | TransactionException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void updateAccountToMultiSigAsync() {
        CompletableFuture<MultisigAccount> future = new CompletableFuture<>();

        try {
            Account baseAccount = makeAccount();
            Account multiSigAccount = makeAccount();
            Account multiSigAccount2 = makeAccount();

            Config.sendValue(baseAccount.getAddress());

            List<Account> accountList = Arrays.asList(baseAccount, multiSigAccount, multiSigAccount2);
            List<MultisigKey> multiSigKeys = accountList.stream().map(account -> {
                MultisigKey multisigKey = new MultisigKey();
                multisigKey.setWeight((long)2);
                multisigKey.setPublicKey(account.getPublicKey());

                return multisigKey;
            }).collect(Collectors.toList());

            MultisigAccountUpdateRequest request = new MultisigAccountUpdateRequest();
            request.setThreshold((long)3);
            request.setWeightedKeys(multiSigKeys);

            caver.kas.wallet.updateToMultiSigAccountAsync(baseAccount.getAddress(), request, new ApiCallback<MultisigAccount>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(MultisigAccount result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void getAccountByPublicKey() {
        try {
            Account account = makeAccount();
            AccountsByPubkey accounts = caver.kas.wallet.getAccountListByPublicKey(account.getPublicKey());
            assertNotNull(accounts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountByPublicKeyAsync() {
        CompletableFuture<AccountsByPubkey> future = new CompletableFuture<>();

        try {
            Account account = makeAccount();
            AccountsByPubkey accounts = caver.kas.wallet.getAccountListByPublicKey(account.getPublicKey());
            caver.kas.wallet.getAccountListByPublicKeyAsync(account.getPublicKey(), new ApiCallback<AccountsByPubkey>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountsByPubkey result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestLegacyTransaction() throws ApiException {
        LegacyTransactionRequest request = new LegacyTransactionRequest();
        request.setFrom(baseAccount);
        request.setTo(toAccount);
        request.setValue("0x1");
        request.setSubmit(true);

        TransactionResult result = caver.kas.wallet.requestLegacyTransaction(request);
        assertNotNull(result);
    }

    @Test
    public void requestLegacyTransactionAsync() {
        CompletableFuture<TransactionResult> future = new CompletableFuture<>();

        try {
            LegacyTransactionRequest request = new LegacyTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setSubmit(true);

            caver.kas.wallet.requestLegacyTransactionAsync(request, new ApiCallback<TransactionResult>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(TransactionResult result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestValueTransfer() {
        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setSubmit(true);

            TransactionResult transactionResult = caver.kas.wallet.requestValueTransfer(request);
            assertNotNull(transactionResult);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestValueTransferAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setSubmit(true);

            caver.kas.wallet.requestValueTransferAsync(request, callBack);

            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestValueTransferWithMemo() {
        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setMemo("aaaaaa");
            request.setSubmit(true);

            TransactionResult transactionResult = caver.kas.wallet.requestValueTransfer(request);
            assertNotNull(transactionResult);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestValueTransferWithMemoAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setMemo("aaaaaa");
            request.setSubmit(true);

            caver.kas.wallet.requestValueTransferAsync(request, callBack);

            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestSmartContractDeploy() {
        try {
            ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
            String input = encodeContractDeploy();

            request.setFrom(baseAccount);
            request.setInput(Utils.addHexPrefix(input));
            request.setGas(5500000L);
            request.submit(true);

            TransactionResult transactionResult = caver.kas.wallet.requestSmartContractDeploy(request);
            assertNotNull(transactionResult);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestSmartContractDeployAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
            String input = encodeContractDeploy();

            request.setFrom(baseAccount);
            request.setInput(Utils.addHexPrefix(input));
            request.setGas(1500000L);
            request.submit(true);

            caver.kas.wallet.requestSmartContractDeployAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestSmartContractExecution() {
        ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
        try {
            String input = encodeKIP7Transfer(ftContractAddress);

            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setInput(input);
            request.setSubmit(true);

            TransactionResult result = caver.kas.wallet.requestSmartContractExecution(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestSmartContractExecutionAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
        try {
            String input = encodeKIP7Transfer(ftContractAddress);

            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setInput(input);
            request.setSubmit(true);

            caver.kas.wallet.requestSmartContractExecutionAsync(request, callBack);

            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestCancel() {
        try {
            CancelTransactionRequest request = new CancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setNonce(1l);

            TransactionResult result = caver.kas.wallet.requestCancel(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestCancelAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            CancelTransactionRequest request = new CancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setNonce(1l);

            caver.kas.wallet.requestCancelAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void requestChainDataAnchoring() {
        try {
            AnchorTransactionRequest request = new AnchorTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput("0x0111");
            request.setSubmit(true);

            TransactionResult result = caver.kas.wallet.requestChainDataAnchoring(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestChainDataAnchoringAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();
        try {
            AnchorTransactionRequest request = new AnchorTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput("0x0111");
            request.setSubmit(true);

            caver.kas.wallet.requestChainDataAnchoringAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransaction() {
        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            TransactionResult transactionResult = caver.kas.wallet.requestValueTransfer(request);

            ProcessRLPRequest requestRLP = new ProcessRLPRequest();
            requestRLP.setRlp(transactionResult.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.wallet.requestRawTransaction(requestRLP);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransactionAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();
        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            TransactionResult transactionResult = caver.kas.wallet.requestValueTransfer(request);

            ProcessRLPRequest requestRLP = new ProcessRLPRequest();
            requestRLP.setRlp(transactionResult.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.wallet.requestRawTransactionAsync(requestRLP, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateFail() {
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeFail keyTypeFail = new KeyTypeFail();

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(keyTypeFail);
            request.setSubmit(true);

            TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateFailAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeFail keyTypeFail = new KeyTypeFail();

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(keyTypeFail);
            request.setSubmit(true);

            caver.kas.wallet.requestAccountUpdateAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyLegacy() throws TransactionException, IOException, ApiException, InterruptedException {
        expectedException.expect(ApiException.class);

        Account account = makeAccount();
        Config.sendValue(account.getAddress());
        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setSubmit(true);

        TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
        assertNotNull(result);

        Thread.sleep(5000);

        AccountUpdateTransactionRequest requestLegacy = new AccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        requestLegacy.setSubmit(true);

        TransactionResult result1 = caver.kas.wallet.requestAccountUpdate(requestLegacy);

    }

    @Test
    public void requestAccountUpdateToAccountKeyLegacyAsync() throws ApiException, ExecutionException, InterruptedException, TransactionException, IOException {
        expectedException.expect(ExecutionException.class);

        BasicTxCallBack callBack = new BasicTxCallBack();

        Account account = makeAccount();
        Config.sendValue(account.getAddress());
        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setSubmit(true);

        TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
        assertNotNull(result);

        Thread.sleep(5000);

        AccountUpdateTransactionRequest requestLegacy = new AccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        requestLegacy.setSubmit(true);

        caver.kas.wallet.requestAccountUpdateAsync(requestLegacy, callBack);
        callBack.checkResponse();
    }

    @Test
    public void requestAccountUpdateToAccountKeyPublic() {
        BasicTxCallBack callBack = new BasicTxCallBack();
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());
            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            caver.kas.wallet.requestAccountUpdateAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyMultiSig() {
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyMultiSigAsync() {
        BasicTxCallBack callback = new BasicTxCallBack();
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            caver.kas.wallet.requestAccountUpdateAsync(request, callback);
            callback.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyRoleBased() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setGas(250000l);
            request.setSubmit(true);

            TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyRoleBasedAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setGas(250000l);
            request.setSubmit(true);

            caver.kas.wallet.requestAccountUpdateAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransaction() {
        try {
            TransactionReceipt receipt = caver.kas.wallet.getTransaction(txHash);
            assertNotNull(receipt);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransactionAsync() {
        CompletableFuture<TransactionReceipt> future = new CompletableFuture<>();

        try {
            caver.kas.wallet.getTransactionAsync(txHash, new ApiCallback<TransactionReceipt>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(TransactionReceipt result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDValueTransferPaidByGlobalFeePayer() {
        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(baseAccount);
        request.setTo(toAccount);
        request.setValue("0x1");
        request.setSubmit(true);

        try {
            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDValueTransferPaidByGlobalFeePayerAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(baseAccount);
        request.setTo(toAccount);
        request.setValue("0x1");
        request.setSubmit(true);

        try {
            caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractDeployPaidByGlobalFeePayer() {
        try {
            FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput(Utils.addHexPrefix(encodeContractDeploy()));
            request.setGas(1500000L);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDSmartContractDeployPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractDeployPaidByGlobalFeePayerAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput(Utils.addHexPrefix(encodeContractDeploy()));
            request.setGas(1500000L);
            request.setSubmit(true);

            caver.kas.wallet.requestFDSmartContractDeployPaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractExecutionPaidByGlobalFeePayer() {
        try {
            FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setInput(Utils.addHexPrefix(encodeKIP7Transfer(ftContractAddress)));
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDSmartContractExecutionPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractExecutionPaidByGlobalFeePayerAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setInput(Utils.addHexPrefix(encodeKIP7Transfer(ftContractAddress)));
            request.setSubmit(true);

            caver.kas.wallet.requestFDSmartContractExecutionPaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDCancelPaidByGlobalFeePayer() {
        FDTxCallBack callBack = new FDTxCallBack();
        try {
            FDCancelTransactionRequest request = new FDCancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setNonce(1l);

            caver.kas.wallet.requestFDCancelPaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDChainDataAnchoringPaidByGlobalFeePayer() {
        try {
            FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput("0x1111");
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDChainDataAnchoringPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDChainDataAnchoringPaidByGlobalFeePayerAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
            request.setFrom(baseAccount);
            request.setInput("0x1111");
            request.setSubmit(true);

            caver.kas.wallet.requestFDChainDataAnchoringPaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransactionPaidByGlobalFeePayer() {
        try {
            FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayer(request);

            FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
            requestRLP.setRlp(result.getRlp());
            requestRLP.setSubmit(true);

            FDTransactionResult result1 = caver.kas.wallet.requestFDRawTransactionPaidByGlobalFeePayer(requestRLP);
            assertNotNull(result1);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransactionPaidByGlobalFeePayerAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayer(request);

            FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
            requestRLP.setRlp(result.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.wallet.requestFDRawTransactionPaidByGlobalFeePayerAsync(requestRLP, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToLegacyType() throws ApiException, InterruptedException {
        expectedException.expect(ApiException.class);

        Account account = makeAccount();

        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setSubmit(true);

        FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
        assertNotNull(result);

        Thread.sleep(5000);

        FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        requestLegacy.setSubmit(true);

        FDTransactionResult result1 = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
        assertNotNull(result1);
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToLegacyTypeAsync() throws ApiException, InterruptedException, ExecutionException {
        expectedException.expect(ExecutionException.class);

        FDTxCallBack callBack = new FDTxCallBack();

        Account account = makeAccount();

        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setSubmit(true);

        FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
        assertNotNull(result);

        Thread.sleep(5000);

        FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        requestLegacy.setSubmit(true);

        caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(requestLegacy, callBack);
        callBack.checkResponse();
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToPublicType() {
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToPublicTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToFailType() {
        try {
            Account account = makeAccount();

            KeyTypeFail keyTypeFail = new KeyTypeFail();

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(keyTypeFail);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToFailTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeFail keyTypeFail = new KeyTypeFail();

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(keyTypeFail);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToMultiSigType() {
        try {
            Account account = makeAccount();

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToMultiSigTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToRoleBasedType() {
        try {
            Account account = makeAccount();

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setGas(500000l);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToRoleBasedTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setGas(500000l);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDValueTransferPaidByUser() {
        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(baseAccount);
        request.setTo(toAccount);
        request.setFeePayer(userFeePayer);
        request.setValue("0x1");
        request.setSubmit(true);

        try {
            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByUser(request);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDValueTransferPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(baseAccount);
        request.setTo(toAccount);
        request.setFeePayer(userFeePayer);
        request.setValue("0x1");
        request.setSubmit(true);

        try {
            caver.kas.wallet.requestFDValueTransferPaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractDeployPaidByUser() {
        try {
            FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
            request.setFrom(baseAccount);
            request.setFeePayer(userFeePayer);
            request.setInput(Utils.addHexPrefix(encodeContractDeploy()));
            request.setGas(1500000L);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDSmartContractDeployPaidByUser(request);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractDeployPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
            request.setFrom(baseAccount);
            request.setFeePayer(userFeePayer);
            request.setInput(Utils.addHexPrefix(encodeContractDeploy()));
            request.setGas(1500000L);
            request.setSubmit(true);

            caver.kas.wallet.requestFDSmartContractDeployPaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractExecutionPaidByUser() {
        try {
            FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setFeePayer(userFeePayer);
            request.setInput(encodeKIP7Transfer(ftContractAddress));
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDSmartContractExecutionPaidByUser(request);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDSmartContractExecutionPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(ftContractAddress);
            request.setFeePayer(userFeePayer);
            request.setInput(encodeKIP7Transfer(ftContractAddress));
            request.setSubmit(true);

            caver.kas.wallet.requestFDSmartContractExecutionPaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDCancelPaidByUser() {
        try {
            FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setFeePayer(userFeePayer);
            request.setNonce((long)1);


            FDTransactionResult result = caver.kas.wallet.requestFDCancelPaidByUser(request);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDCancelPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setFeePayer(userFeePayer);
            request.setNonce((long)1);


            caver.kas.wallet.requestFDCancelPaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDChainDataAnchoringPaidByUser() {
        FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
        request.setFrom(baseAccount);
        request.setFeePayer(userFeePayer);
        request.setInput("0x123456");
        request.setSubmit(true);

        try {
            FDTransactionResult result = caver.kas.wallet.requestFDChainDataAnchoringPaidByUser(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDChainDataAnchoringPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
        request.setFrom(baseAccount);
        request.setFeePayer(userFeePayer);
        request.setInput("0x123456");
        request.setSubmit(true);

        try {
            caver.kas.wallet.requestFDChainDataAnchoringPaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransactionPaidByUser() {
        try {
            FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setFeePayer(userFeePayer);
            request.setValue("0x1");

            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByUser(request);

            FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
            processRLPRequest.setRlp(result.getRlp());
            processRLPRequest.setFeePayer(userFeePayer);
            processRLPRequest.setSubmit(true);
            FDTransactionResult resultRLP = caver.kas.wallet.requestFDRawTransactionPaidByUser(processRLPRequest);
            assertNotNull(resultRLP);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestRawTransactionPaidByUserAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setFeePayer(userFeePayer);
            request.setValue("0x1");

            FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByUser(request);

            FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
            processRLPRequest.setRlp(result.getRlp());
            processRLPRequest.setFeePayer(userFeePayer);
            processRLPRequest.setSubmit(true);

            caver.kas.wallet.requestFDRawTransactionPaidByUserAsync(processRLPRequest, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void requestFDAccountUpdatePaidByUserToLegacyType() throws ApiException, InterruptedException {
        expectedException.expect(ApiException.class);

        Account account = makeAccount();

        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setFeePayer(userFeePayer);
        request.setSubmit(true);

        FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
        assertNotNull(result);

        Thread.sleep(5000);

        FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        request.setFeePayer(userFeePayer);
        requestLegacy.setSubmit(true);

        FDTransactionResult result1 = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
        assertNotNull(result1);
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToLegacyTypeAsync() throws ApiException, ExecutionException, InterruptedException {
        expectedException.expect(ExecutionException.class);

        FDTxCallBack callBack = new FDTxCallBack();

        Account account = makeAccount();

        KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

        FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
        request.setFrom(account.getAddress());
        request.setAccountKey(updateKeyType);
        request.setFeePayer(userFeePayer);
        request.setSubmit(true);

        FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
        assertNotNull(result);

        Thread.sleep(5000);

        FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
        requestLegacy.setFrom(account.getAddress());
        requestLegacy.setAccountKey(new KeyTypeLegacy());
        request.setFeePayer(userFeePayer);
        requestLegacy.setSubmit(true);

        caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(requestLegacy, callBack);
        callBack.checkResponse();
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToPublicType() {
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToPublicTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToFailType() {
        try {
            Account account = makeAccount();

            KeyTypeFail updateKeyType = new KeyTypeFail();

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToFailTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeFail updateKeyType = new KeyTypeFail();

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToMultiSigType() {
        try {
            Account account = makeAccount();

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToMultiSigTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeMultiSig updateKeyType = createWeightedMultiSigKeyType(account);

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToRoleBasedType() {
        try {
            Account account = makeAccount();

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setGas(500000l);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToRoleBasedTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setGas(500000l);
            request.setSubmit(true);

            caver.kas.wallet.requestFDAccountUpdatePaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean hasMultiSigTx(String address) {
        try {
            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(address);
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    @Test
    public void getMultiSigTransactions() {
        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }

            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAccount.getAddress());
            assertNotNull(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void getMultiSigTransactionsAsync() {
        CompletableFuture<MultisigTransactions> future = new CompletableFuture<>();

        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }

            caver.kas.wallet.getMultiSigTransactionListAsync(multiSigAccount.getAddress(), new ApiCallback<MultisigTransactions>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(MultisigTransactions result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void signMultiSigTransaction() {
        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }

            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            MultisigTransactionStatus status = caver.kas.wallet.signMultiSigTransaction(transactions.getItems().get(0).getMultiSigKeys().get(1).getAddress(), transactions.getItems().get(0).getTransactionId());
            assertNotNull(status);
        } catch (ApiException | InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void signMultiSigTransactionAsync() {
        CompletableFuture<MultisigTransactionStatus> future = new CompletableFuture<>();

        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }

            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            caver.kas.wallet.signMultiSigTransactionAsync(transactions.getItems().get(0).getMultiSigKeys().get(1).getAddress(), transactions.getItems().get(0).getTransactionId(), new ApiCallback<MultisigTransactionStatus>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(MultisigTransactionStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void signTransaction() {
        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }
            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            Signature signature = caver.kas.wallet.signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactions.getItems().get(0).getTransactionId());
            assertNotNull(signature);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void signTransactionAsync() {
        CompletableFuture<Signature> future = new CompletableFuture<>();

        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }
            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            caver.kas.wallet.signTransactionAsync(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactions.getItems().get(0).getTransactionId(), new ApiCallback<Signature>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(Signature result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void appendSignatures() {
        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
            }


            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            String transactionID = transactions.getItems().get(0).getTransactionId();


            Signature signature = caver.kas.wallet.signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactionID);
            SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
            request.setSignatures(Arrays.asList(signature));

            MultisigTransactionStatus transactionStatus = caver.kas.wallet.appendSignatures(transactionID, request);
            assertNotNull(transactionStatus);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void appendSignaturesAsync() {
        CompletableFuture<MultisigTransactionStatus> future = new CompletableFuture<>();

        try {
            if(!hasMultiSigTx(multiSigAddress)) {
                sendValueTransferForMultiSig(multiSigAddress, toAccount);
                Thread.sleep(3000);
            }


            MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
            String transactionID = transactions.getItems().get(0).getTransactionId();


            Signature signature = caver.kas.wallet.signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactionID);
            SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
            request.setSignatures(Arrays.asList(signature));

            caver.kas.wallet.appendSignaturesAsync(transactionID, request, new ApiCallback<MultisigTransactionStatus>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(MultisigTransactionStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountCount() {
        try {
            AccountCountByAccountID countByAccountID = caver.kas.wallet.getAccountCount();
            assertNotNull(countByAccountID);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountCountAsync() {
        CompletableFuture<AccountCountByAccountID> future = new CompletableFuture<>();

        try {
            Call res = caver.kas.wallet.getAccountCountAsync(new ApiCallback<AccountCountByAccountID>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountCountByAccountID result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountCountByKRN() {
        try {
            AccountCountByKRN accountCountByKRN = caver.kas.wallet.getAccountCountByKRN();
            assertNotNull(accountCountByKRN);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

//    @Test
//    public void getAccountCountByKRN_WithKRN() {
//        try {
//            AccountCountByKRN accountCountByKRN = caver.kas.wallet.getAccountCountByKRN(krn);
//            assertNotNull(accountCountByKRN);
//        } catch (ApiException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

    @Test
    public void getAccountCountByKRNAsync() {
        CompletableFuture<AccountCountByKRN> future = new CompletableFuture<>();
        try {
            Call res = caver.kas.wallet.getAccountCountByKRNAsync(new ApiCallback<AccountCountByKRN>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onSuccess(AccountCountByKRN result, int statusCode, Map<String, List<String>> responseHeaders) {
                    future.complete(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });

            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

//    @Test
//    public void getAccountCountByKRNAsync_WithKRN() {
//        CompletableFuture<AccountCountByKRN> future = new CompletableFuture<>();
//
//        try {
//            Call res = caver.kas.wallet.getAccountCountByKRNAsync(krn, new ApiCallback<AccountCountByKRN>() {
//                @Override
//                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
//                    future.completeExceptionally(e);
//                }
//
//                @Override
//                public void onSuccess(AccountCountByKRN result, int statusCode, Map<String, List<String>> responseHeaders) {
//                    future.complete(result);
//                }
//
//                @Override
//                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
//
//                }
//
//                @Override
//                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
//
//                }
//            });
//
//            if(future.isCompletedExceptionally()) {
//                fail();
//            } else {
//                assertNotNull(future.get());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

    public static class BasicTxCallBack implements ApiCallback<TransactionResult> {
        CompletableFuture<TransactionResult> future = new CompletableFuture<>();

        @Override
        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
            future.completeExceptionally(e);
        }

        @Override
        public void onSuccess(TransactionResult result, int statusCode, Map<String, List<String>> responseHeaders) {
            future.complete(result);
        }

        @Override
        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

        }

        @Override
        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

        }

        public CompletableFuture<TransactionResult> getFuture() {
            return future;
        }

        public void setFuture(CompletableFuture<TransactionResult> future) {
            this.future = future;
        }

        public void checkResponse() throws ExecutionException, InterruptedException {
            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        }
    }

    public static class FDTxCallBack implements ApiCallback<FDTransactionResult> {
        CompletableFuture<FDTransactionResult> future = new CompletableFuture<>();

        @Override
        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
            future.completeExceptionally(e);
        }

        @Override
        public void onSuccess(FDTransactionResult result, int statusCode, Map<String, List<String>> responseHeaders) {
            future.complete(result);
        }

        @Override
        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

        }

        @Override
        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

        }

        public CompletableFuture<FDTransactionResult> getFuture() {
            return future;
        }

        public void setFuture(CompletableFuture<FDTransactionResult> future) {
            this.future = future;
        }

        public void checkResponse() throws ExecutionException, InterruptedException {
            if(future.isCompletedExceptionally()) {
                fail();
            } else {
                assertNotNull(future.get());
            }
        }
    }

    @Test
    public void callContract() throws ApiException {
        CallArgument argument = new CallArgument();
        argument.setType("address");
        argument.setValue(baseAccount);
        ContractCallResponse response = caver.kas.wallet.callContract(ftContractAddress, "balanceOf", Collections.singletonList(argument));

        assertNotNull(response);
    }

    @Test
    public void callContractWithNoArgument() throws ApiException {
        String contractAddress = "0x96961fcbf250211ec4cab63de8ed1a722dff89a6";
        ContractCallResponse response = caver.kas.wallet.callContract(contractAddress, "totalSupply");

        assertNotNull(response);
    }

    @Test
    public void callContractWithMultiArgument() throws ApiException {
        String contractAddress = ftContractAddress;
        String toAccount = caver.kas.wallet.getAccountList().getItems().get(0).getAddress();

        CallArgument argument1 = new CallArgument();
        argument1.setType("address");
        argument1.setValue(toAccount);

        CallArgument argument2 = new CallArgument();
        argument2.setType("uint256");
        argument2.setValue(BigInteger.valueOf(1000000000));

        SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));

        ContractCallResponse response = caver.kas.wallet.callContract(contractAddress, "transfer", Arrays.asList(argument1, argument2),  sendOptions);
        assertNotNull(response);
    }

    @Test
    public void callContractAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<ContractCallResponse> future = new CompletableFuture<ContractCallResponse>();

        CallArgument argument = new CallArgument();
        argument.setType("address");
        argument.setValue(baseAccount);

        Call result = caver.kas.wallet.callContractAsync(ftContractAddress, "balanceOf", Collections.singletonList(argument), new ApiCallback<ContractCallResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(ContractCallResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void createKeys() throws ApiException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(2);
        assertNotNull(response);
        assertEquals(2, response.getItems().size());
    }

    @Test
    public void createKeysAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<KeyCreationResponse> future = new CompletableFuture<>();
        Call result = caver.kas.wallet.createKeysAsync(2, new ApiCallback<KeyCreationResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(KeyCreationResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getKey() throws ApiException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        Key key = caver.kas.wallet.getKey(response.getItems().get(0).getKeyId());
        assertNotNull(key);
    }

    @Test
    public void getKeyAsync() throws ApiException, ExecutionException, InterruptedException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        CompletableFuture<Key> future = new CompletableFuture<>();
        Call result = caver.kas.wallet.getKeyAsync(response.getItems().get(0).getKeyId(), new ApiCallback<Key>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Key result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void signMessage() throws ApiException {
        String data = "0x1122334455667788112233445566778811223344556677881122334455667788";
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        KeySignDataResponse keySignDataResponse = caver.kas.wallet.signMessage(response.getItems().get(0).getKeyId(), data);
        assertNotNull(keySignDataResponse);
    }

//    @Test
//    public void signMessageWithKRN() throws ApiException {
//        String data = "0x1122334455667788112233445566778811223344556677881122334455667788";
//
//        KeyCreationResponse response = caver.kas.wallet.createKeys(1);
//        KeySignDataResponse keySignDataResponse = caver.kas.wallet.signMessage(response.getItems().get(0).getKeyId(), data, response.getItems().get(0).getKrn());
//
//        assertNotNull(keySignDataResponse);
//    }

    @Test
    public void signMessageAsync() throws ApiException, ExecutionException, InterruptedException {
        String data = "0x1122334455667788112233445566778811223344556677881122334455667788";
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        CompletableFuture<KeySignDataResponse> future = new CompletableFuture<>();
        Call call = caver.kas.wallet.signMessageAsync(response.getItems().get(0).getKeyId(), data, new ApiCallback<KeySignDataResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(KeySignDataResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    private FeeDelegatedAccountUpdate createAccountUpdateTx(SingleKeyring keyring, String newKey) throws IOException {
        com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyPublic(keyring.getAddress(), newKey);

        FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(keyring.getAddress())
                .setGas(BigInteger.valueOf(250000))
                .setAccount(account)
                .build();

        tx.sign(keyring);

        return tx;
    }

    @Test
    public void registerAccounts() throws ApiException, IOException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        SingleKeyring keyring = KeyringFactory.generate();
        com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());

        AccountRegistration registration = new AccountRegistration();
        registration.setAddress(account.getAddress());
        registration.setKeyId(response.getItems().get(0).getKeyId());
        registration.setRlp(createAccountUpdateTx(keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());

        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.add(registration);

        RegistrationStatusResponse result = caver.kas.wallet.registerAccounts(request);
        assertNotNull(result);
    }

    @Test
    public void registerAccountsWithList() throws ApiException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        SingleKeyring keyring = KeyringFactory.generate();
        com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());

        AccountRegistration registration = new AccountRegistration();
        registration.setAddress(account.getAddress());
        registration.setKeyId(response.getItems().get(0).getKeyId());

        RegistrationStatusResponse result = caver.kas.wallet.registerAccounts(Collections.singletonList(registration));
        assertNotNull(result);
        assertEquals("ok", result.getStatus());
    }

    @Test
    public void registerAccountsAsync() throws ApiException, ExecutionException, InterruptedException, IOException {
        CompletableFuture<RegistrationStatusResponse> future = new CompletableFuture<>();

        KeyCreationResponse response = caver.kas.wallet.createKeys(1);

        SingleKeyring keyring = KeyringFactory.generate();
        com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());

        AccountRegistration registration = new AccountRegistration();
        registration.setAddress(account.getAddress());
        registration.setKeyId(response.getItems().get(0).getKeyId());
        registration.setRlp(createAccountUpdateTx(keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());

        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.add(registration);

        Call result = caver.kas.wallet.registerAccountsAsync(request, new ApiCallback<RegistrationStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(RegistrationStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
            assertEquals("ok", future.get().getStatus());
        }
    }

    @Test
    public void migrateSingleKeyAccount() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
        SingleKeyring singleKeyring = KeyringFactory.generate();

        MigrationAccount migrationAccount = new MigrationAccount(
                singleKeyring.getAddress(),
                singleKeyring.getKey().getPrivateKey()
        );
        accountsToBeMigrated.add(migrationAccount);

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating an account having single key should be succeeded.", "ok", response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateSingleKeyAccountUsingValidNonce() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
        SingleKeyring singleKeyring = KeyringFactory.generate();

        MigrationAccount migrationAccount = new MigrationAccount(
                singleKeyring.getAddress(),
                singleKeyring.getKey().getPrivateKey(),
                "0x0"
        );

        accountsToBeMigrated.add(migrationAccount);

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating an account having single key should be succeeded.", "ok", response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateSingleKeyAccount_AllFailed() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        MigrationAccount accountNotYetDecoupled = new MigrationAccount(
                KeyringFactory.generate().getAddress(),
                PrivateKey.generate().getPrivateKey()
        );
        accountsToBeMigrated.add(accountNotYetDecoupled);

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "An account with wrong private key should be failed to be migrated.",
                    "all failed",
                    response.getStatus()
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateMultipleSingleKeyAccounts() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        for (int i=0; i<3; i++) {
            SingleKeyring singleKeyring = KeyringFactory.generate();

            MigrationAccount migrationAccount = new MigrationAccount(
                    singleKeyring.getAddress(),
                    singleKeyring.getKey().getPrivateKey()
            );
            accountsToBeMigrated.add(migrationAccount);
        }

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating multiple accounts having single key should be succeeded.", "ok", response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateMultipleSingleKeyAccountsUsingValidNonce() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
        SingleKeyring keyring1 = KeyringFactory.generate();
        SingleKeyring keyring2 = KeyringFactory.generate();

        // You can set the nonce value to either a hexadecimal string or a BigInteger.
        accountsToBeMigrated.add(
                new MigrationAccount(
                        keyring1.getAddress(),
                        keyring1.getKey().getPrivateKey(),
                        "0x0"
                )
        );
        accountsToBeMigrated.add(
                new MigrationAccount(
                        keyring2.getAddress(),
                        keyring2.getKey().getPrivateKey(),
                        BigInteger.valueOf(0)
                )
        );

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals("Migrating an account having single key should be succeeded.", "ok", response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateMultipleSingleKeyAccounts_PartiallyFailed() {
        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        for (int i=0; i<3; i++) {
            SingleKeyring singleKeyring = KeyringFactory.generate();

            MigrationAccount migrationAccount = new MigrationAccount(
                    singleKeyring.getAddress(),
                    singleKeyring.getKey().getPrivateKey()
            );

            accountsToBeMigrated.add(0, migrationAccount);
        }

        // Newly created account have a AccountKeyLegacy which means coupled-key in default.
        // Below will be failed to be migrated because it does not use coupled-key.
        MigrationAccount accountNotYetDecoupled = new MigrationAccount(
                KeyringFactory.generate().getAddress(),
                PrivateKey.generate().getPrivateKey()
        );
        accountsToBeMigrated.add(accountNotYetDecoupled);

        try {
            RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "If there are accounts with the wrong private key, the migration should fail, but the correct accounts should succeed.",
                    "partially failed",
                    response.getStatus()
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrate_throwException_withoutInitializingNodeAPI() throws ApiException, NoSuchFieldException, IOException {
        // This test case is assuming that the user does not directly launch the KAS service on the local host.
        // Since it is unlikely that the KAS service will be run on the local host and tested, this test case will be kept as it is.
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("You should initialize Node API with working url(e.g. https://node-api.klaytnapi.com/v1/klaytn) first.");

        CaverExtKAS caverExtKAS = new CaverExtKAS();
        caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);

        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        SingleKeyring singleKeyring = KeyringFactory.generate();

        MigrationAccount migrationAccount = new MigrationAccount(
                singleKeyring.getAddress(),
                singleKeyring.getKey().getPrivateKey()
        );

        accountsToBeMigrated.add(migrationAccount);

        caverExtKAS.kas.wallet.migrateAccounts(accountsToBeMigrated);
    }

    @Test
    public void migrateWithInitializingAPIsManuallyWalletFirst() {
        CaverExtKAS caverExtKAS = new CaverExtKAS();
        caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);
        caverExtKAS.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API);

        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
        for (int i=0; i<2; i++) {
            SingleKeyring singleKeyring = KeyringFactory.generate();

            MigrationAccount migrationAccount = new MigrationAccount(
                    singleKeyring.getAddress(),
                    singleKeyring.getKey().getPrivateKey()
            );
            accountsToBeMigrated.add(migrationAccount);
        }

        try {
            RegistrationStatusResponse response = caverExtKAS.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "Migrating multiple accounts with a single key should succeed after manually initializing each API.",
                    "ok",
                    response.getStatus()
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateWithInitializingAPIsManuallyNodeFirst() {
        CaverExtKAS caverExtKAS = new CaverExtKAS();
        caverExtKAS.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API);
        caverExtKAS.initWalletAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_WALLET_API);

        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
        for (int i=0; i<2; i++) {
            SingleKeyring singleKeyring = KeyringFactory.generate();

            MigrationAccount migrationAccount = new MigrationAccount(
                    singleKeyring.getAddress(),
                    singleKeyring.getKey().getPrivateKey()
            );
            accountsToBeMigrated.add(migrationAccount);
        }

        try {
            RegistrationStatusResponse response = caverExtKAS.kas.wallet.migrateAccounts(accountsToBeMigrated);
            assertEquals(
                    "Migrating multiple accounts with a single key should succeed after manually initializing each API.",
                    "ok",
                    response.getStatus()
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateAccount_throwException_emptyAddress() throws IOException, NoSuchFieldException, ApiException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Address of migrationAccount must not be empty.");

        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        SingleKeyring singleKeyring = KeyringFactory.generate();

        MigrationAccount migrationAccount = new MigrationAccount(
                "",
                singleKeyring.getKey().getPrivateKey()
        );

        accountsToBeMigrated.add(0, migrationAccount);

        caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
    }

    @Test
    public void migrateAccount_throwException_nullKey() throws IOException, NoSuchFieldException, ApiException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("MigrationAccountKey of migrationAccount must not be empty.");

        ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();

        SingleKeyring singleKeyring = KeyringFactory.generate();

        MigrationAccount migrationAccount = new MigrationAccount(
                singleKeyring.getAddress(),
                singleKeyring.getKey().getPrivateKey()
        );
        migrationAccount.setMigrationAccountKey(null);

        accountsToBeMigrated.add(0, migrationAccount);
        caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
    }

    @Test
    public void migrateMultipleKeyAccount() {
        RegistrationStatusResponse mockResponse = new RegistrationStatusResponse();
        mockResponse.setStatus("ok");

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
            when(wallet.migrateAccounts(accountsToBeMigrated)).thenReturn(mockResponse);
            RegistrationStatusResponse actualResponse = wallet.migrateAccounts(accountsToBeMigrated);

            verify(wallet, times(1)).migrateAccounts(accountsToBeMigrated);
            assertEquals("A status of response should be ok", mockResponse.getStatus(), actualResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void migrateRoleBasedKeyAccount() {
        RegistrationStatusResponse mockResponse = new RegistrationStatusResponse();
        mockResponse.setStatus("ok");

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
            when(wallet.migrateAccounts(accountsToBeMigrated)).thenReturn(mockResponse);
            RegistrationStatusResponse actualResponse = wallet.migrateAccounts(accountsToBeMigrated);

            verify(wallet, times(1)).migrateAccounts(accountsToBeMigrated);
            assertEquals("A status of response should be ok", mockResponse.getStatus(), actualResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void createFeePayer() throws ApiException {
        Account res = caver.kas.wallet.createFeePayer();
        assertNotNull(res);
    }

    @Test
    public void createFeePayerAsync() throws ExecutionException, InterruptedException, ApiException {
        CompletableFuture<Account> future = new CompletableFuture<>();

        caver.kas.wallet.createFeePayerAsync(new ApiCallback<Account>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Account result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void deleteFeePayer() throws ApiException {
        Account feePayer = caver.kas.wallet.createFeePayer();

        AccountStatus status = caver.kas.wallet.deleteFeePayer(feePayer.getAddress());
        assertNotNull(status);
        assertEquals("deleted", status.getStatus());
    }

    @Test
    public void deleteFeePayerAsync() throws ExecutionException, InterruptedException, ApiException {
        Account feePayer = caver.kas.wallet.createFeePayer();

        CompletableFuture<AccountStatus> future = new CompletableFuture<>();
        caver.kas.wallet.deleteFeePayerAsync(feePayer.getAddress(), new ApiCallback<AccountStatus>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(AccountStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getFeePayer() throws ApiException {
        Account account = caver.kas.wallet.createFeePayer();
        Account feePayer = caver.kas.wallet.getFeePayer(account.getAddress());

        assertNotNull(feePayer);
    }

    @Test
    public void getFeePayerAsync() throws ApiException, ExecutionException, InterruptedException {
        Account account = caver.kas.wallet.createFeePayer();

        CompletableFuture<Account> future = new CompletableFuture<>();
        caver.kas.wallet.getFeePayerAsync(account.getAddress(), new ApiCallback<Account>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Account result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getFeePayerList() throws ApiException {
        Account account = caver.kas.wallet.createFeePayer();

        Accounts accounts = caver.kas.wallet.getFeePayerList();
        assertNotNull(accounts);
    }

    @Test
    public void getFeePayerListWithOptions() throws ApiException {
        Account account = caver.kas.wallet.createFeePayer();

        WalletQueryOptions options = new WalletQueryOptions();
        options.setSize(1L);

        Accounts accounts = caver.kas.wallet.getFeePayerList(options);
        assertNotNull(accounts);
    }

    @Test
    public void getFeePayerListAsync() throws ApiException, ExecutionException, InterruptedException {
        Account account = caver.kas.wallet.createFeePayer();
        CompletableFuture<Accounts> future = new CompletableFuture<>();

        caver.kas.wallet.getFeePayerListAsync(new ApiCallback<Accounts>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(Accounts result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void deleteKey() throws ApiException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);
        Key key = response.getItems().get(0);

        KeyStatus status = caver.kas.wallet.deleteKey(key.getKeyId());
        assertNotNull(status);
        assertEquals("deleted", status.getStatus());
    }

    @Test
    public void deleteKeyAsync() throws ApiException, ExecutionException, InterruptedException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(1);
        Key key = response.getItems().get(0);

        CompletableFuture<KeyStatus> future = new CompletableFuture<>();

        caver.kas.wallet.deleteKeyAsync(key.getKeyId(), new ApiCallback<KeyStatus>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(KeyStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        });

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void getKeyListByKRN() throws ApiException, InterruptedException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(3);
        String krn = response.getItems().get(0).getKrn();

        KeyList keyList = caver.kas.wallet.getKeyListByKRN(krn);
        assertNotNull(keyList);
    }

    @Test
    public void getKeyListByKRNWithOptions() throws ApiException, InterruptedException {
        KeyCreationResponse response = caver.kas.wallet.createKeys(3);
        String krn = response.getItems().get(0).getKrn();

        WalletQueryOptions options = new WalletQueryOptions();
        options.setSize(1L);

        KeyList keyList = caver.kas.wallet.getKeyListByKRN(krn, options);
        assertNotNull(keyList);
    }

    @Test
    public void getKeyListByKRNAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<KeyList> future = new CompletableFuture<>();
        ApiCallback<KeyList> callback = new ApiCallback<KeyList>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(KeyList result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        KeyCreationResponse response = caver.kas.wallet.createKeys(3);
        String krn = response.getItems().get(0).getKrn();
        caver.kas.wallet.getKeyListByKRNAsync(krn, callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Ignore
    public void getFDTransactionList() throws ApiException {
        //It doesn't work in baobab
        FDTransactionWithCurrencyResultList response = caver.kas.wallet.getFDTransactionList();
        assertNotNull(response);
    }

    @Ignore
    public void getFDTransactionListWithFrom() throws ApiException {
        //It doesn't work in baobab
        FDTransactionWithCurrencyResultList response = caver.kas.wallet.getFDTransactionList(baseAccount);
        assertNotNull(response);
    }

    @Ignore
    public void getFDTransactionListAsync() throws ApiException, ExecutionException, InterruptedException {
        //It doesn't work in baobab
        CompletableFuture<FDTransactionWithCurrencyResultList> future = new CompletableFuture<>();
        ApiCallback<FDTransactionWithCurrencyResultList> callback = new ApiCallback<FDTransactionWithCurrencyResultList>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(FDTransactionWithCurrencyResultList result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.wallet.getFDTransactionListAsync(callback);

        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }


    @Ignore
    public void getFDTransaction() throws ApiException {
        FDTransactionWithCurrencyResult response = caver.kas.wallet.getFDTransaction(txHash);
        assertNotNull(response);
    }

    @Ignore
    public void getFDTransactionAsync() throws ApiException, ExecutionException, InterruptedException {
        CompletableFuture<FDTransactionWithCurrencyResult> future = new CompletableFuture<>();
        ApiCallback<FDTransactionWithCurrencyResult> callback = new ApiCallback<FDTransactionWithCurrencyResult>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(FDTransactionWithCurrencyResult result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

            }
        };

        caver.kas.wallet.getFDTransactionAsync(txHash, callback);
        if(future.isCompletedExceptionally()) {
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void createFeePayerWithoutAccountUpdate() throws ApiException {
        Account account = caver.kas.wallet.createFeePayer(true);
        assertNotNull(account);
    }
}