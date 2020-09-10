package com.klaytn.caver.ext.kas.wallet;

import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.wallet.keyring.SignatureData;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.wallet.api.*;
import io.swagger.client.api.wallet.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class WalletAPI {
    AccountApi accountApi;
    BasicTransactionApi basicTransactionApi;
    FeeDelegatedTransactionPaidByKasApi feeDelegatedTransactionPaidByKasApi;
    FeeDelegatedTransactionPaidByUserApi feeDelegatedTransactionPaidByUserApi;
    MultisigTransactionManagementApi multisigTransactionManagementApi;

    String chainId;

    public WalletAPI(String chainId, ApiClient walletApiClient) {
        setChainId(chainId);
        setAccountApi(new AccountApi(walletApiClient));
        setBasicTransactionApi(new BasicTransactionApi(walletApiClient));
        setFeeDelegatedTransactionPaidByKasApi(new FeeDelegatedTransactionPaidByKasApi(walletApiClient));
        setFeeDelegatedTransactionPaidByUserApi(new FeeDelegatedTransactionPaidByUserApi(walletApiClient));
        setMultisigTransactionManagementApi(new MultisigTransactionManagementApi(walletApiClient));
    }

    public Account createAccount() throws ApiException {
        return getAccountApi().createAccount(chainId);
    }

    public Accounts getAccountList() throws ApiException {
        return getAccountList(new WalletQueryOptions());
    }

    public Accounts getAccountList(WalletQueryOptions options) throws ApiException {
        return getAccountApi().retrieveAccounts(chainId, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp());
    }

    public Account getAccount(String address) throws ApiException {
        return getAccountApi().retrieveAccount(chainId, address);
    }

    public AccountStatus deleteAccount(String address) throws ApiException {
        return getAccountApi().deleteAccount(chainId, address);
    }

    public AccountSummary disableAccount(String address) throws ApiException {
        return getAccountApi().deactivateAccount(chainId, address);
    }

    public AccountSummary enableAccount(String address) throws ApiException {
        return getAccountApi().activateAccount(chainId, address);
    }

    public Signature signTransaction(String address, String transactionId) throws ApiException {
        return getAccountApi().signTransactionIDResponse(chainId, address, transactionId);
    }

    public MultisigAccount updateAccountToMultiSig(String address, AccountKeyWeightedMultiSig weightedMultiSig) throws ApiException {
        MultisigAccountUpdateRequest body = new MultisigAccountUpdateRequest();
        body.setThreshold(weightedMultiSig.getThreshold().longValue());
        body.setWeightedKeys(convertMultiSigKey(weightedMultiSig));

        return updateAccountToMultiSig(address, body);
    }

    public MultisigAccount updateAccountToMultiSig(String address, MultisigAccountUpdateRequest request) throws ApiException {
        return getAccountApi().multisigAccountUpdate(chainId, address, request);
    }

    public TransactionResult requestLegacyTransaction(LegacyTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().legacyTransaction(chainId, request);
    }

    public TransactionResult requestValueTransfer(ValueTransferTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().valueTransferTransaction(chainId, request);
    }

    public TransactionResult requestSmartContractDeploy(ContractDeployTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().contractDeployTransaction(chainId, request);
    }

    public TransactionResult requestSmartContractExecution(ContractExecutionTransactionRequest request, boolean submit) throws ApiException {
        return getBasicTransactionApi().contractExecutionTransaction(chainId, request);
    }

    public TransactionResult requestCancel(CancelTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().cancelTransaction(chainId, request);
    }

    public TransactionResult requestChainDataAnchoring(AnchorTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().anchorTransaction(chainId, request);
    }

    public TransactionResult requestRawTransaction(ProcessRLPRequest request) throws ApiException {
        return getBasicTransactionApi().processRLP(chainId, request);
    }

    //TODO: It need to check that account update format.
    public TransactionResult requestAccountUpdate(AccountUpdateTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().accountUpdateTransaction(chainId, request);
    }

    public TransactionReceipt getTransaction(String transactionHash) throws ApiException {
        return getBasicTransactionApi().transactionReceipt(chainId, transactionHash);
    }

    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FDValueTransferTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByGlobalFeePayer(FDContractDeployTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByGlobalFeePayer(FDContractExecutionTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByGlobalFeePayer(FDCancelTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDCancelTransactionResponse(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByGlobalFeePayer(FDAnchorTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestRawTransactionPaidByGlobalFeePayer(FDProcessRLPRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLP(chainId, request);
    }

    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByGlobalFeePayer(FDAccountUpdateTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDAccountUpdateTransactionResponse(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByUser(FDUserValueTransferTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByUser(FDUserContractDeployTransactionRequest request, boolean submit) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByUser(FDUserContractDeployTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByUser(FDUserContractExecutionTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByUser(FDUserCancelTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransaction(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByUser(FDUserAnchorTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestRawTransactionPaidByUser(FDUserProcessRLPRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDProcessRLP(chainId, request);
    }

    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByUser(FDUserAccountUpdateTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDAccountUpdateTransaction(chainId, request);
    }

    public MultisigTransactions getMultiSigTransactions(String address) throws ApiException {
        return getMultiSigTransactions(address, new WalletQueryOptions());
    }

    public MultisigTransactions getMultiSigTransactions(String address, WalletQueryOptions options) throws ApiException {
        return getMultisigTransactionManagementApi().retrieveMultisigTransactions(chainId, address, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp());
    }

    public MultisigTransactionStatus signMultiSigTransaction(String address, String transactionId) throws ApiException {
        return getMultisigTransactionManagementApi().signPendingTransaction(chainId, address, transactionId);
    }

    public MultisigTransactionStatus appendSignatures(String transactionId, List<SignatureData> sigs) throws ApiException {
        SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
        List<Signature> data = sigs.stream().map(signatureData -> {
            Signature signature = new Signature();
            signature.setR(signatureData.getR());
            signature.setV(signatureData.getV());
            signature.setS(signatureData.getS());

            return signature;
        }).collect(Collectors.toList());
        request.setSignatures(data);
        return getMultisigTransactionManagementApi().signPendingTransactionBySig(chainId, transactionId, request);
    }

    public AccountApi getAccountApi() {
        return accountApi;
    }

    public BasicTransactionApi getBasicTransactionApi() {
        return basicTransactionApi;
    }

    public FeeDelegatedTransactionPaidByKasApi getFeeDelegatedTransactionPaidByKasApi() {
        return feeDelegatedTransactionPaidByKasApi;
    }

    public FeeDelegatedTransactionPaidByUserApi getFeeDelegatedTransactionPaidByUserApi() {
        return feeDelegatedTransactionPaidByUserApi;
    }

    public MultisigTransactionManagementApi getMultisigTransactionManagementApi() {
        return multisigTransactionManagementApi;
    }

    public String getChainId() {
        return chainId;
    }

    public void setAccountApi(AccountApi accountApi) {
        this.accountApi = accountApi;
    }

    public void setBasicTransactionApi(BasicTransactionApi basicTransactionApi) {
        this.basicTransactionApi = basicTransactionApi;
    }

    public void setFeeDelegatedTransactionPaidByKasApi(FeeDelegatedTransactionPaidByKasApi feeDelegatedTransactionPaidByKasApi) {
        this.feeDelegatedTransactionPaidByKasApi = feeDelegatedTransactionPaidByKasApi;
    }

    public void setFeeDelegatedTransactionPaidByUserApi(FeeDelegatedTransactionPaidByUserApi feeDelegatedTransactionPaidByUserApi) {
        this.feeDelegatedTransactionPaidByUserApi = feeDelegatedTransactionPaidByUserApi;
    }

    public void setMultisigTransactionManagementApi(MultisigTransactionManagementApi multisigTransactionManagementApi) {
        this.multisigTransactionManagementApi = multisigTransactionManagementApi;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    private List<MultisigKey> convertMultiSigKey(AccountKeyWeightedMultiSig weightedMultiSig) {
        return weightedMultiSig.getWeightedPublicKeys().stream()
                .map(weightedPublicKey -> {
                    MultisigKey multisigKey = new MultisigKey();
                    multisigKey.setPublicKey(weightedPublicKey.getPublicKey());
                    multisigKey.setWeight(weightedPublicKey.getWeight().longValue());

                    return multisigKey;
                }).collect(Collectors.toList());
    }
}
