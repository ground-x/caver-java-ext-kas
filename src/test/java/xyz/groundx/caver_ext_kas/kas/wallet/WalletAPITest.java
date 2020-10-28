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

import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import xyz.groundx.caver_ext_kas.Config;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7ConstantData;
import com.klaytn.caver.utils.Utils;
import com.squareup.okhttp.Call;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.kas.wallet.accountkey.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class WalletAPITest {
    public static CaverExtKAS caver;
    static String userFeePayer;


    static String baseAccount = "";
    static String toAccount = "";
    static String multiSigAddress = "";

    static Account multiSigAccount;

    static String ftContractAddress = "";


    static String txHash;
    static String krn = "krn:1001:wallet:173db69c-f1b8-4dd5-9ac2-ed8a0badab29:account-pool:kas_sdk_account_pool";

    @BeforeClass
    public static void init() throws IOException, TransactionException, ApiException {
        Config.init();
        caver = Config.getCaver();
        userFeePayer = Config.getFeePayerAddress();

        baseAccount = baseAccount.equals("") ? makeAccount().getAddress() : baseAccount;
        toAccount = toAccount.equals("") ? makeAccount().getAddress() : toAccount;
        multiSigAddress = multiSigAddress.equals("") ? createMultiSig().getAddress() : multiSigAddress;
        multiSigAccount = caver.kas.getWallet().getAccount(multiSigAddress);

        //Send balance to baseAccount
        com.klaytn.caver.methods.response.TransactionReceipt.TransactionReceiptData receiptData = Config.sendValue(baseAccount);
        txHash = receiptData.getTransactionHash();

        //Send balance to multiSig address
        Config.sendValue(multiSigAddress);

        //Send balance to userFeePayer
        BigInteger balance = Config.getBalance(userFeePayer);
        BigInteger milliKLAY = new BigInteger(Utils.convertToPeb("999", Utils.KlayUnit.mKLAY));
        if(balance.compareTo(milliKLAY) <= 0) {
            Config.sendValue(userFeePayer);
        }

        ftContractAddress = ftContractAddress.equals("") ? deployKIP7() : ftContractAddress;

        caver.kas.wallet.accountApi.getApiClient().setDebugging(true);
    }

    public static Account makeAccount() throws ApiException{
        return caver.kas.getWallet().createAccount();
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

            MultisigAccount account = caver.kas.getWallet().updateToMultiSigAccount(baseAccount.getAddress(), request);
            return baseAccount;
        } catch (ApiException | TransactionException | IOException e) {
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

            TransactionResult transactionResult = caver.kas.getWallet().requestSmartContractDeploy(request);

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
        String input = ABI.encodeContractDeploy(kip7.getConstructor(), KIP7ConstantData.BINARY, Arrays.asList("KALE", "KAL", 18, initialSupply));

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

        caver.kas.getWallet().requestValueTransfer(request);
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

        AccountUpdateKey[] keys = new AccountUpdateKey[] {txKey, accountUpdateKey, fdKey};
        KeyTypeRoleBased roleBasedUpdateKeyType = new KeyTypeRoleBased(Arrays.asList(keys));

        return roleBasedUpdateKeyType;
    }

    @Test
    public void makeUncompressedFormat_LegacyKeyType() {
        KeyTypeLegacy type = new KeyTypeLegacy();

        caver.kas.getWallet().makeUncompressedKeyFormat(type);
        assertEquals(KeyTypeLegacy.KEY_TYPE, type.getKeyType().longValue());
    }

    @Test
    public void makeUncompressedFormat_FailKeyType() {
        KeyTypeFail type = new KeyTypeFail();

        caver.kas.getWallet().makeUncompressedKeyFormat(type);
        assertEquals(KeyTypeFail.KEY_TYPE, type.getKeyType().longValue());
    }

    @Test
    public void makeUncompressedFormat_PublicKeyType() {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        String actual = "0x24002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        KeyTypePublic type = new KeyTypePublic();
        type.setKey(actual);

        caver.kas.getWallet().makeUncompressedKeyFormat(type);
        assertEquals(expected, type.getKey());
    }

    @Test
    public void makeUncompressedFormat_alreadyPubKeyTag() {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        KeyTypePublic type = new KeyTypePublic();
        type.setKey(expected);

        caver.kas.getWallet().makeUncompressedKeyFormat(type);
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

        caver.kas.getWallet().makeUncompressedKeyFormat(multiSig);
        assertEquals(expected, multiSig.getKey().getWeightedKeys().get(0).getPublicKey());
        assertEquals(expected, multiSig.getKey().getWeightedKeys().get(1).getPublicKey());
    }

    @Test
    public void makeUncompressedFormat_roleBasedKey() throws ApiException {
        String expected = "0x0424002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";
        String actual = "0x24002a25c6404e5087c77c7860bbc6ce661a83890bef401d59eb4062510155f6b936d2efee0889bdc35efdf9d5948acae155a530ae0d12ab6b8db0550d749366";

        Account account = makeAccount();
        KeyTypeRoleBased roleBased = createRoleBasedKeyType(account);

        List<AccountUpdateKey> keyList = roleBased.getKey();

        ((KeyTypePublic)keyList.get(0)).setKey(actual);
        ((KeyTypePublic)keyList.get(1)).setKey(expected);
        MultisigUpdateKey multiSig = ((KeyTypeMultiSig)keyList.get(2)).getKey();
        List<MultisigKey> multisigKeyList = multiSig.getWeightedKeys();

        for(int i=0; i<multisigKeyList.size(); i++) {
            multisigKeyList.get(i).setPublicKey(actual);
        }

        caver.kas.getWallet().makeUncompressedKeyFormat(roleBased);

        assertEquals(expected,((KeyTypePublic)keyList.get(0)).getKey());
        assertEquals(expected,((KeyTypePublic)keyList.get(1)).getKey());

        for(int i=0; i<multisigKeyList.size(); i++) {
            assertEquals(expected, multisigKeyList.get(i).getPublicKey());
        }
    }


    @Test
    public void createAccount() {
        try {
            Account account = caver.kas.getWallet().createAccount();
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
            Call call = caver.kas.getWallet().createAccountAsync(new ApiCallback<Account>() {
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
            Accounts accounts = caver.kas.getWallet().getAccountList();
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
            options.setFromTimestamp("2020-01-01 00:00:00");
            options.setToTimestamp("2020-12-31 00:00:00");
            Accounts accounts = caver.kas.getWallet().getAccountList(options);
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
            Call call = caver.kas.getWallet().getAccountListAsync(new ApiCallback<Accounts>() {
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
            Account actual = caver.kas.getWallet().getAccount(expected.getAddress());
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
            Call call = caver.kas.getWallet().getAccountAsync(expected.getAddress(), new ApiCallback<Account>() {
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
            AccountStatus accountStatus = caver.kas.getWallet().deleteAccount(expected.getAddress());

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
            caver.kas.getWallet().deleteAccountAsync(expected.getAddress(), new ApiCallback<AccountStatus>() {
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
            AccountSummary status = caver.kas.getWallet().disableAccount(expected.getAddress());
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
            caver.kas.getWallet().disableAccountAsync(expected.getAddress(), new ApiCallback<AccountSummary>() {
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
            caver.kas.getWallet().disableAccount(expected.getAddress());
            AccountSummary enableSummary = caver.kas.getWallet().enableAccount(expected.getAddress());

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
            caver.kas.getWallet().disableAccount(expected.getAddress());

            caver.kas.getWallet().enableAccountAsync(expected.getAddress(), new ApiCallback<AccountSummary>() {
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

            MultisigAccount account = caver.kas.getWallet().updateToMultiSigAccount(baseAccount.getAddress(), request);
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

            caver.kas.getWallet().updateToMultiSigAccountAsync(baseAccount.getAddress(), request, new ApiCallback<MultisigAccount>() {
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
            AccountsByPubkey accounts = caver.kas.getWallet().getAccountListByPublicKey(account.getPublicKey());
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
            AccountsByPubkey accounts = caver.kas.getWallet().getAccountListByPublicKey(account.getPublicKey());
            caver.kas.getWallet().getAccountListByPublicKeyAsync(account.getPublicKey(), new ApiCallback<AccountsByPubkey>() {
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
    public void requestLegacyTransaction() {
        CompletableFuture<TransactionResult> future = new CompletableFuture<>();

        try {
            LegacyTransactionRequest request = new LegacyTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setSubmit(true);

            caver.kas.getWallet().requestLegacyTransactionAsync(request, new ApiCallback<TransactionResult>() {
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

            TransactionResult transactionResult = caver.kas.getWallet().requestValueTransfer(request);
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

            caver.kas.getWallet().requestValueTransferAsync(request, callBack);

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

            TransactionResult transactionResult = caver.kas.getWallet().requestValueTransfer(request);
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

            caver.kas.getWallet().requestValueTransferAsync(request, callBack);

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

            TransactionResult transactionResult = caver.kas.getWallet().requestSmartContractDeploy(request);
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

            caver.kas.getWallet().requestSmartContractDeployAsync(request, callBack);
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

            TransactionResult result = caver.kas.getWallet().requestSmartContractExecution(request);
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

            caver.kas.getWallet().requestSmartContractExecutionAsync(request, callBack);

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

            TransactionResult result = caver.kas.getWallet().requestCancel(request);
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

            caver.kas.getWallet().requestCancelAsync(request, callBack);
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

            TransactionResult result = caver.kas.getWallet().requestChainDataAnchoring(request);
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

            caver.kas.getWallet().requestChainDataAnchoringAsync(request, callBack);
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

            TransactionResult transactionResult = caver.kas.getWallet().requestValueTransfer(request);

            ProcessRLPRequest requestRLP = new ProcessRLPRequest();
            requestRLP.setRlp(transactionResult.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.getWallet().requestRawTransaction(requestRLP);
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

            TransactionResult transactionResult = caver.kas.getWallet().requestValueTransfer(request);

            ProcessRLPRequest requestRLP = new ProcessRLPRequest();
            requestRLP.setRlp(transactionResult.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.getWallet().requestRawTransactionAsync(requestRLP, callBack);
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

            TransactionResult result = caver.kas.getWallet().requestAccountUpdate(request);
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

            caver.kas.getWallet().requestAccountUpdateAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyLegacy() {
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());
            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            TransactionResult result = caver.kas.getWallet().requestAccountUpdate(request);
            assertNotNull(result);

            AccountUpdateTransactionRequest requestLegacy = new AccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            TransactionResult result1 = caver.kas.getWallet().requestAccountUpdate(requestLegacy);
            assertNotNull(result1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyLegacyAsync() {
        BasicTxCallBack callBack = new BasicTxCallBack();

        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());
            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            TransactionResult result = caver.kas.getWallet().requestAccountUpdate(request);
            assertNotNull(result);

            AccountUpdateTransactionRequest requestLegacy = new AccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            caver.kas.getWallet().requestAccountUpdateAsync(requestLegacy, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
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

            caver.kas.getWallet().requestAccountUpdateAsync(request, callBack);
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

            TransactionResult result = caver.kas.getWallet().requestAccountUpdate(request);
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

            caver.kas.getWallet().requestAccountUpdateAsync(request, callback);
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

            TransactionResult result = caver.kas.getWallet().requestAccountUpdate(request);
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

            caver.kas.getWallet().requestAccountUpdateAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransaction() {
        try {
            TransactionReceipt receipt = caver.kas.getWallet().getTransaction(txHash);
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
            caver.kas.getWallet().getTransactionAsync(txHash, new ApiCallback<TransactionReceipt>() {
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
            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByGlobalFeePayer(request);
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
            caver.kas.getWallet().requestFDValueTransferPaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDSmartContractDeployPaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDSmartContractDeployPaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDSmartContractExecutionPaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDSmartContractExecutionPaidByGlobalFeePayerAsync(request, callBack);
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

            caver.kas.getWallet().requestFDCancelPaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDChainDataAnchoringPaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDChainDataAnchoringPaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByGlobalFeePayer(request);

            FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
            requestRLP.setRlp(result.getRlp());
            requestRLP.setSubmit(true);

            FDTransactionResult result1 = caver.kas.getWallet().requestFDRawTransactionPaidByGlobalFeePayer(requestRLP);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByGlobalFeePayer(request);

            FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
            requestRLP.setRlp(result.getRlp());
            requestRLP.setSubmit(true);

            caver.kas.getWallet().requestFDRawTransactionPaidByGlobalFeePayerAsync(requestRLP, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToLegacyType() {
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            FDTransactionResult result1 = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
            assertNotNull(result1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByGlobalFeePayerToLegacyTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(requestLegacy, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callBack);
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
            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByUser(request);
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
            caver.kas.getWallet().requestFDValueTransferPaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDSmartContractDeployPaidByUser(request);
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

            caver.kas.getWallet().requestFDSmartContractDeployPaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDSmartContractExecutionPaidByUser(request);
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

            caver.kas.getWallet().requestFDSmartContractExecutionPaidByUserAsync(request, callBack);
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


            FDTransactionResult result = caver.kas.getWallet().requestFDCancelPaidByUser(request);
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


            caver.kas.getWallet().requestFDCancelPaidByUserAsync(request, callBack);
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
            FDTransactionResult result = caver.kas.getWallet().requestFDChainDataAnchoringPaidByUser(request);
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
            caver.kas.getWallet().requestFDChainDataAnchoringPaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByUser(request);

            FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
            processRLPRequest.setRlp(result.getRlp());
            processRLPRequest.setFeePayer(userFeePayer);
            processRLPRequest.setSubmit(true);
            FDTransactionResult resultRLP = caver.kas.getWallet().requestFDRawTransactionPaidByUser(processRLPRequest);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDValueTransferPaidByUser(request);

            FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
            processRLPRequest.setRlp(result.getRlp());
            processRLPRequest.setFeePayer(userFeePayer);
            processRLPRequest.setSubmit(true);

            caver.kas.getWallet().requestFDRawTransactionPaidByUserAsync(processRLPRequest, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void requestFDAccountUpdatePaidByUserToLegacyType() {
        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            request.setFeePayer(userFeePayer);
            requestLegacy.setSubmit(true);

            FDTransactionResult result1 = caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
            assertNotNull(result1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDAccountUpdatePaidByUserToLegacyTypeAsync() {
        FDTxCallBack callBack = new FDTxCallBack();

        try {
            Account account = makeAccount();

            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setFeePayer(userFeePayer);
            request.setSubmit(true);

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            request.setFeePayer(userFeePayer);
            requestLegacy.setSubmit(true);

            caver.kas.getWallet().requestFDAccountUpdatePaidByGlobalFeePayerAsync(requestLegacy, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByUserAsync(request, callBack);
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

            FDTransactionResult result = caver.kas.getWallet().requestFDAccountUpdatePaidByUser(request);
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

            caver.kas.getWallet().requestFDAccountUpdatePaidByUserAsync(request, callBack);
            callBack.checkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean hasMultiSigTx(String address) {
        try {
            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(address);
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
            }

            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAccount.getAddress());
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
            }

            caver.kas.getWallet().getMultiSigTransactionListAsync(multiSigAccount.getAddress(), new ApiCallback<MultisigTransactions>() {
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
            }

            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            MultisigTransactionStatus status = caver.kas.getWallet().signMultiSigTransaction(transactions.getItems().get(0).getMultiSigKeys().get(1).getAddress(), transactions.getItems().get(0).getTransactionId());
            assertNotNull(status);
        } catch (ApiException e) {
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
            }

            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            caver.kas.getWallet().signMultiSigTransactionAsync(transactions.getItems().get(0).getMultiSigKeys().get(1).getAddress(), transactions.getItems().get(0).getTransactionId(), new ApiCallback<MultisigTransactionStatus>() {
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
            }
            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            Signature signature = caver.kas.getWallet().signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactions.getItems().get(0).getTransactionId());
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
            }
            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            caver.kas.getWallet().signTransactionAsync(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactions.getItems().get(0).getTransactionId(), new ApiCallback<Signature>() {
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


            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            String transactionID = transactions.getItems().get(0).getTransactionId();


            Signature signature = caver.kas.getWallet().signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactionID);
            SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
            request.setSignatures(Arrays.asList(signature));

            MultisigTransactionStatus transactionStatus = caver.kas.getWallet().appendSignatures(transactionID, request);
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
            }


            MultisigTransactions transactions = caver.kas.getWallet().getMultiSigTransactionList(multiSigAddress);
            String transactionID = transactions.getItems().get(0).getTransactionId();


            Signature signature = caver.kas.getWallet().signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactionID);
            SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
            request.setSignatures(Arrays.asList(signature));

            caver.kas.getWallet().appendSignaturesAsync(transactionID, request, new ApiCallback<MultisigTransactionStatus>() {
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
            AccountCountByAccountID countByAccountID = caver.kas.getWallet().getAccountCount();
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
            Call res = caver.kas.getWallet().getAccountCountAsync(new ApiCallback<AccountCountByAccountID>() {
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
            AccountCountByKRN accountCountByKRN = caver.kas.getWallet().getAccountCountByKRN();
            assertNotNull(accountCountByKRN);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountCountByKRN_WithKRN() {
        try {
            AccountCountByKRN accountCountByKRN = caver.kas.getWallet().getAccountCountByKRN(krn);
            assertNotNull(accountCountByKRN);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountCountByKRNAsync() {
        CompletableFuture<AccountCountByKRN> future = new CompletableFuture<>();
        try {
            Call res = caver.kas.getWallet().getAccountCountByKRNAsync(new ApiCallback<AccountCountByKRN>() {
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

    @Test
    public void getAccountCountByKRNAsync_WithKRN() {
        CompletableFuture<AccountCountByKRN> future = new CompletableFuture<>();

        try {
            Call res = caver.kas.getWallet().getAccountCountByKRNAsync(krn, new ApiCallback<AccountCountByKRN>() {
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
}