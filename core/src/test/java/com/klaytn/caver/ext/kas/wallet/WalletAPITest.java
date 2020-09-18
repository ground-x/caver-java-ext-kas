package com.klaytn.caver.ext.kas.wallet;

import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.ext.kas.Config;
import com.klaytn.caver.ext.kas.KAS;
import com.klaytn.caver.ext.kas.wallet.accountkey.*;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7ConstantData;
import com.klaytn.caver.utils.Utils;
import io.swagger.client.ApiException;
import io.swagger.client.api.wallet.model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class WalletAPITest {
    public static KAS kas;
    public static String baseUrl = "https://wallet-api.dev.klaytn.com";

    static String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";
    static String chainId = "1001";

    static String baseAccount = "0x81bA6c299350719B18dFAEC38ba566fBd5Cd7202";
    static String toAccount = "0x95E3Fd82eCd2b32Cae8618599971F5F47F4bC110";

    static String userFeePayer = "0x31d845Ac80A0B2a38f6267CabcF34F8fA9DcD2B7";
    static String contractAddress = "0x978e8f0a0d52b1bf498a193f4fa11f5a83c7f2f3";

    static Account multiSigAccount;
    static String multiSigAddress = "0xBF19457580DcF1ed9E586F0C74747311a0d9d070";



    @BeforeClass
    public static void init() throws IOException, TransactionException, ApiException {
        kas = new KAS();
        kas.enableWalletAPI(baseUrl, chainId, accessKey, secretAccessKey);
        kas.getWalletAPI().getBasicTransactionApi().getApiClient().setDebugging(true);

        BigInteger balance = Config.getBalance(baseAccount);
        BigInteger milliKLAY = new BigInteger(Utils.convertToPeb("100", Utils.KlayUnit.mKLAY));
        if(balance.compareTo(milliKLAY) <= 0) {
            Config.sendValue(baseAccount);
        }

        balance = Config.getBalance(userFeePayer);
        milliKLAY = new BigInteger(Utils.convertToPeb("100", Utils.KlayUnit.mKLAY));
        if(balance.compareTo(milliKLAY) <= 0) {
            Config.sendValue(userFeePayer);
        }

        multiSigAccount = kas.getWalletAPI().getAccount(multiSigAddress);

        balance = Config.getBalance(multiSigAddress);
        milliKLAY = new BigInteger(Utils.convertToPeb("100", Utils.KlayUnit.mKLAY));
        if(balance.compareTo(milliKLAY) <= 0) {
            Config.sendValue(multiSigAddress);
        }
    }

    public Account makeAccount() throws ApiException{
        return kas.getWalletAPI().createAccount();
    }

    private String encodeContractDeploy() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        KIP7 kip7 = new KIP7(kas);
        BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
        String input = ABI.encodeContractDeploy(kip7.getConstructor(), KIP7ConstantData.BINARY, Arrays.asList("KALE", "KAL", 18, initialSupply));

        return input;
    }

    private String encodeKIP7Transfer(String address) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        KIP7 kip7 = new KIP7(kas, address);
        BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
        String input = kip7.getMethod("transfer").encodeABI(Arrays.asList(toAccount, transferAmount));

        return input;
    }

    private TransactionResult sendValueTransferForMultiSig(String fromAddress, String toAddress) throws ApiException {
        ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
        request.setFrom(fromAddress);
        request.setTo(toAddress);
        request.setValue("0x1");
        request.setSubmit(true);

        return kas.getWalletAPI().requestValueTransfer(request);
    }

    private KeyTypeLegacy createLegacyKeyType() {
        return new KeyTypeLegacy();
    }

    private KeyTypeFail createFailKeyType() {
        return new KeyTypeFail();
    }

    private KeyTypePublic createPublicKeyType() throws ApiException {
        Account account = makeAccount();

        KeyTypePublic keyType = new KeyTypePublic();
        keyType.setKey(account.getPublicKey());

        return keyType;
    }

    private MultisigKey createMultiSig(long weight, String publicKey) {
        MultisigKey key = new MultisigKey();
        key.setWeight(weight);
        key.setPublicKey(publicKey);

        return key;
    }

    private KeyTypeMultiSig createWeightedMultiSigKeyType(Account account) throws ApiException {
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

    private KeyTypeRoleBased createRoleBasedKeyType(Account account) throws ApiException {
        KeyTypePublic txKey = createPublicKeyType();
        KeyTypePublic accountUpdateKey = createPublicKeyType();
        KeyTypeMultiSig fdKey = createWeightedMultiSigKeyType(account);

        AccountUpdateKey[] keys = new AccountUpdateKey[] {txKey, accountUpdateKey, fdKey};
        KeyTypeRoleBased roleBasedUpdateKeyType = new KeyTypeRoleBased(Arrays.asList(keys));

        return roleBasedUpdateKeyType;
    }

    @Test
    public void createAccount() {
        try {
            Account account = kas.getWalletAPI().createAccount();
            assertNotNull(account);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountList() {
        try {
            Accounts accounts = kas.getWalletAPI().getAccountList();
            assertNotNull(accounts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccount() {
        try {
            Account expected = makeAccount();
            Account actual = kas.getWalletAPI().getAccount(expected.getAddress());

            assertEquals(expected.getAddress(), actual.getAddress());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void deleteAccount() {
        try {
            Account expected = makeAccount();
            AccountStatus accountStatus = kas.getWalletAPI().deleteAccount(expected.getAddress());

            assertEquals("deleted", accountStatus.getStatus());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void disableAccount() {
        try {
            Account expected = makeAccount();
            AccountSummary status = kas.getWalletAPI().disableAccount(expected.getAddress());
            assertEquals(expected.getAddress(), status.getAddress());
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void enableAccount() {
        try {
            Account expected = makeAccount();
            kas.getWalletAPI().disableAccount(expected.getAddress());
            AccountSummary enableSummary = kas.getWalletAPI().enableAccount(expected.getAddress());

            assertEquals(expected.getAddress(), enableSummary.getAddress());
        } catch (ApiException e) {
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

            MultisigAccount account = kas.getWalletAPI().updateToMultiSigAccount(baseAccount.getAddress(), request);
            assertEquals((long)3, account.getThreshold().longValue());
        } catch (ApiException | TransactionException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getAccountByPublicKey() {
        String publicKey = "0x04f715a9d9e0f7a4b152d4ef8a67f4708fc1f83fe2e1984cf0f72987dbacbad324fb619fbdf30497441eddf80676403f0009f07b4195915df7220c79183e9d1f27";
        try {
            AccountsByPubkey accounts = kas.getWalletAPI().getAccountByPublicKey(publicKey);
            assertNotNull(accounts);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestLegacyTransaction() {
        try {
            LegacyTransactionRequest request = new LegacyTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");
            request.setSubmit(true);

            TransactionResult transactionResult = kas.getWalletAPI().requestLegacyTransaction(request);
            assertNotNull(transactionResult);
        } catch (ApiException e) {
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

            TransactionResult transactionResult = kas.getWalletAPI().requestValueTransfer(request);
            assertNotNull(transactionResult);
        } catch (ApiException e) {
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

            TransactionResult transactionResult = kas.getWalletAPI().requestValueTransfer(request);
            assertNotNull(transactionResult);
        } catch (ApiException e) {
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
            request.submit(true);

            TransactionResult transactionResult = kas.getWalletAPI().requestSmartContractDeploy(request);
            assertNotNull(transactionResult);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestSmartContractExecution() {
        ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
        try {
            String input = encodeKIP7Transfer(contractAddress);

            request.setFrom(baseAccount);
            request.setTo(contractAddress);
            request.setInput(input);
            request.setSubmit(true);

            TransactionResult result = kas.getWalletAPI().requestSmartContractExecution(request);
            assertNotNull(result);
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

            TransactionResult result = kas.getWalletAPI().requestCancel(request);
            assertNotNull(result);
        } catch (ApiException e) {
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

            TransactionResult result = kas.getWalletAPI().requestChainDataAnchoring(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    //TODO
    @Test
    public void requestRawTransaction() {
        try {
            ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            TransactionResult transactionResult = kas.getWalletAPI().requestValueTransfer(request);

            ProcessRLPRequest requestRLP = new ProcessRLPRequest();
            requestRLP.setRlp(transactionResult.getRlp());
            requestRLP.setSubmit(true);

            kas.getWalletAPI().requestRawTransaction(requestRLP);
        } catch (ApiException e) {
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

            TransactionResult result = kas.getWalletAPI().requestAccountUpdate(request);
            assertNotNull(result);
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

            TransactionResult result = kas.getWalletAPI().requestAccountUpdate(request);
            assertNotNull(result);

            AccountUpdateTransactionRequest requestLegacy = new AccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            TransactionResult result1 = kas.getWalletAPI().requestAccountUpdate(requestLegacy);
            assertNotNull(result1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyPublic() {
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());
            KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            TransactionResult result = kas.getWalletAPI().requestAccountUpdate(request);
            assertNotNull(result);
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

            TransactionResult result = kas.getWalletAPI().requestAccountUpdate(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestAccountUpdateToAccountKeyRoleBased() {
        try {
            Account account = makeAccount();
            Config.sendValue(account.getAddress());

            KeyTypeRoleBased updateKeyType = createRoleBasedKeyType(account);

            AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
            request.setFrom(account.getAddress());
            request.setAccountKey(updateKeyType);
            request.setSubmit(true);

            TransactionResult result = kas.getWalletAPI().requestAccountUpdate(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getTransaction() {
        String txHash = "0xc3041c76f3ad881db9ad73111927d210edca9a531d33a5180fbe50b7bcf33951";

        try {
            TransactionReceipt receipt = kas.getWalletAPI().getTransaction(txHash);
            assertNotNull(receipt);
        } catch (ApiException e) {
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
            FDTransactionResult result = kas.getWalletAPI().requestFDValueTransferPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (ApiException e) {
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
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDSmartContractDeployPaidByGlobalFeePayer(request);
            assertNotNull(result);
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
            request.setTo(contractAddress);
            request.setInput(Utils.addHexPrefix(encodeKIP7Transfer(contractAddress)));
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDSmartContractExecutionPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void requestFDCancelPaidByGlobalFeePayer() {
        try {
            FDCancelTransactionRequest request = new FDCancelTransactionRequest();
            request.setFrom(baseAccount);
            request.setNonce(1l);

            FDTransactionResult result = kas.getWalletAPI().requestFDCancelPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (ApiException e) {
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

            FDTransactionResult result = kas.getWalletAPI().requestFDChainDataAnchoringPaidByGlobalFeePayer(request);
            assertNotNull(result);
        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    //TODO
    @Test
    public void requestRawTransactionPaidByGlobalFeePayer() {
        try {
            FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
            request.setFrom(baseAccount);
            request.setTo(toAccount);
            request.setValue("0x1");

            FDTransactionResult result = kas.getWalletAPI().requestFDValueTransferPaidByGlobalFeePayer(request);

            FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
            requestRLP.setRlp(result.getRlp());
            requestRLP.setSubmit(true);

            FDTransactionResult result1 = kas.getWalletAPI().requestRawTransactionPaidByGlobalFeePayer(requestRLP);
            assertNotNull(result1);
        } catch (ApiException e) {
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            requestLegacy.setSubmit(true);

            FDTransactionResult result1 = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
            assertNotNull(result1);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
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
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(request);
            assertNotNull(result);
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
            FDTransactionResult result = kas.getWalletAPI().requestFDValueTransferPaidByUser(request);
        } catch (ApiException e) {
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
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDSmartContractDeployPaidByUser(request);
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
            request.setTo(contractAddress);
            request.setFeePayer(userFeePayer);
            request.setInput(encodeKIP7Transfer(contractAddress));
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDSmartContractExecutionPaidByUser(request);
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


            FDTransactionResult result = kas.getWalletAPI().requestFDCancelPaidByUser(request);
        } catch (ApiException e) {
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
            FDTransactionResult result = kas.getWalletAPI().requestFDChainDataAnchoringPaidByUser(request);
            assertNotNull(result);
        } catch (ApiException e) {
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

            FDTransactionResult result = kas.getWalletAPI().requestFDValueTransferPaidByUser(request);

            FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
            processRLPRequest.setRlp(result.getRlp());
            processRLPRequest.setFeePayer(userFeePayer);
            processRLPRequest.setSubmit(true);
            FDTransactionResult resultRLP = kas.getWalletAPI().requestRawTransactionPaidByUser(processRLPRequest);
            assertNotNull(resultRLP);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);

            FDAccountUpdateTransactionRequest requestLegacy = new FDAccountUpdateTransactionRequest();
            requestLegacy.setFrom(account.getAddress());
            requestLegacy.setAccountKey(new KeyTypeLegacy());
            request.setFeePayer(userFeePayer);
            requestLegacy.setSubmit(true);

            FDTransactionResult result1 = kas.getWalletAPI().requestFDAccountUpdatePaidByGlobalFeePayer(requestLegacy);
            assertNotNull(result1);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
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

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
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
            request.setSubmit(true);

            FDTransactionResult result = kas.getWalletAPI().requestFDAccountUpdatePaidByUser(request);
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean hasMultiSigTx(String address) {
        try {
            MultisigTransactions transactions = kas.getWalletAPI().getMultiSigTransactions(address);
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

            MultisigTransactions transactions = kas.getWalletAPI().getMultiSigTransactions(multiSigAccount.getAddress());
            assertNotNull(transactions);
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

            MultisigTransactions transactions = kas.getWalletAPI().getMultiSigTransactions(multiSigAddress);
            MultisigTransactionStatus status = kas.getWalletAPI().signMultiSigTransaction(transactions.getItems().get(0).getMultiSigKeys().get(1).getAddress(), transactions.getItems().get(0).getTransactionId());
            assertNotNull(status);
        } catch (ApiException e) {
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
            MultisigTransactions transactions = kas.getWalletAPI().getMultiSigTransactions(multiSigAddress);
            Signature signature = kas.getWalletAPI().signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactions.getItems().get(0).getTransactionId());
            assertNotNull(signature);
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


            MultisigTransactions transactions = kas.getWalletAPI().getMultiSigTransactions(multiSigAddress);
            String transactionID = transactions.getItems().get(0).getTransactionId();


            Signature signature = kas.getWalletAPI().signTransaction(transactions.getItems().get(0).getMultiSigKeys().get(2).getAddress(), transactionID);
            SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
            request.setSignatures(Arrays.asList(signature));

            MultisigTransactionStatus transactionStatus = kas.getWalletAPI().appendSignatures(transactionID, request);
            assertNotNull(transactionStatus);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}