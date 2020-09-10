package com.klaytn.caver.ext.kas.wallet;

import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.wallet.keyring.SignatureData;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.wallet.api.*;
import io.swagger.client.api.wallet.model.*;
import org.web3j.utils.Numeric;

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

        return getAccountApi().multisigAccountUpdate(chainId, address, body);
    }

    public TransactionResult requestLegacyTransaction(LegacyTransaction tx, boolean submit) throws ApiException {
        LegacyTransactionRequest request = new LegacyTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setTo(tx.getTo());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setValue(tx.getValue());
        request.setSubmit(submit);

        return getBasicTransactionApi().legacyTransaction(chainId, request);
    }

    public TransactionResult requestValueTransfer(ValueTransfer tx, boolean submit) throws ApiException {
        ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setTo(tx.getTo());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setValue(tx.getValue());
        request.setSubmit(submit);

        return getBasicTransactionApi().valueTransferTransaction(chainId, request);
    }

    public TransactionResult requestValueTransfer(ValueTransferMemo tx, boolean submit) throws ApiException {
        ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setTo(tx.getTo());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setValue(tx.getValue());
        request.setMemo(tx.getInput());
        request.setSubmit(submit);

        return getBasicTransactionApi().valueTransferTransaction(chainId, request);
    }

    public TransactionResult requestSmartContractDeploy(SmartContractDeploy tx, boolean submit) throws ApiException {
        ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setValue(tx.getValue());
        request.setInput(tx.getInput());
        request.setSubmit(submit);

        return getBasicTransactionApi().contractDeployTransaction(chainId, request);
    }

    public TransactionResult requestSmartContractExecution(SmartContractExecution tx, boolean submit) throws ApiException {
        ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getBasicTransactionApi().contractExecutionTransaction(chainId, request);
    }

    public TransactionResult requestCancel(Cancel tx, String txHash, boolean submit) throws ApiException {
        CancelTransactionRequest request = new CancelTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setTransactionHash(txHash);
        request.setSubmit(submit);

        return getBasicTransactionApi().cancelTransaction(chainId, request);
    }

    public TransactionResult requestChainDataAnchoring(ChainDataAnchoring tx, boolean submit) throws ApiException {
        AnchorTransactionRequest request = new AnchorTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getBasicTransactionApi().anchorTransaction(chainId, request);
    }

    public TransactionResult requestRawTransaction(String rawTransaction, boolean submit) throws ApiException {
        ProcessRLPRequest request = new ProcessRLPRequest();
        request.setRlp(rawTransaction);
        request.setSubmit(submit);

        return getBasicTransactionApi().processRLP(chainId, request);
    }

    //TODO: It need to check that account update format.
    public TransactionResult requestAccountUpdate(AccountUpdate tx, boolean submit) {
        AccountKey accountKey = new AccountKey();
//        accountKey.setKeyType(tx.getAccount().getAccountKey());

        AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
        request.setFrom(tx.getFrom());
//        request.setAccountKey();
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return null;
    }

    public TransactionReceipt getTransaction(String transactionHash) throws ApiException {
        return getBasicTransactionApi().transactionReceipt(chainId, transactionHash);
    }

    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FeeDelegatedValueTransfer tx, boolean submit) throws ApiException {
        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FeeDelegatedValueTransferMemo tx, boolean submit) throws ApiException {
        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setMemo(tx.getInput());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FeeDelegatedValueTransferWithRatio tx, boolean submit) throws ApiException {
        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FeeDelegatedValueTransferMemoWithRatio tx, boolean submit) throws ApiException {
        FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setMemo(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByGlobalFeePayer(FeeDelegatedSmartContractDeploy tx, boolean submit) throws ApiException {
        FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByGlobalFeePayer(FeeDelegatedSmartContractDeployWithRatio tx, boolean submit) throws ApiException {
        FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setInput(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByGlobalFeePayer(FeeDelegatedSmartContractExecution tx, boolean submit) throws ApiException {
        FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByGlobalFeePayer(FeeDelegatedSmartContractExecutionWithRatio tx, boolean submit) throws ApiException {
        FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setInput(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByGlobalFeePayer(FeeDelegatedCancel tx, String txHash, boolean submit) throws ApiException {
        FDCancelTransactionRequest request = new FDCancelTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setTransactionHash(txHash);
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDCancelTransactionResponse(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByGlobalFeePayer(FeeDelegatedCancelWithRatio tx, String txHash, boolean submit) throws ApiException {
        FDCancelTransactionRequest request = new FDCancelTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setTransactionHash(txHash);
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDCancelTransactionResponse(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByGlobalFeePayer(FeeDelegatedChainDataAnchoring tx, boolean submit) throws ApiException {
        FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByGlobalFeePayer(FeeDelegatedChainDataAnchoringWithRatio tx, boolean submit) throws ApiException {
        FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestRawTransactionPaidByGlobalFeePayer(String rawTransaction, boolean submit) throws ApiException {
        FDProcessRLPRequest request = new FDProcessRLPRequest();
        request.setRlp(rawTransaction);
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLP(chainId, request);
    }

    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByGlobalFeePayer(FeeDelegatedAccountUpdate tx, boolean submit) {
        FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();

        return null;
    }

    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByGlobalFeePayer(FeeDelegatedAccountUpdateWithRatio tx, boolean submit) {
        FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();

        return null;
    }

    public FDTransactionResult requestFDValueTransferPaidByUser(FeeDelegatedValueTransfer tx, boolean submit) throws ApiException {
        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByUser(FeeDelegatedValueTransferMemo tx, boolean submit) throws ApiException {
        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setMemo(tx.getInput());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByUser(FeeDelegatedValueTransferWithRatio tx, boolean submit) throws ApiException {
        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDValueTransferPaidByUser(FeeDelegatedValueTransferMemoWithRatio tx, boolean submit) throws ApiException {
        FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setMemo(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByUser(FeeDelegatedSmartContractDeploy tx, boolean submit) throws ApiException {
        FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractDeployPaidByUser(FeeDelegatedSmartContractDeployWithRatio tx, boolean submit) throws ApiException {
        FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setInput(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByUser(FeeDelegatedSmartContractExecution tx, boolean submit) throws ApiException {
        FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDSmartContractExecutionPaidByUser(FeeDelegatedSmartContractExecutionWithRatio tx, boolean submit) throws ApiException {
        FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setValue(tx.getValue());
        request.setTo(tx.getTo());
        request.setInput(tx.getInput());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDContractExecutionTransaction(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByUser(FeeDelegatedCancel tx, String txHash, boolean submit) throws ApiException {
        FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setTransactionHash(txHash);
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransaction(chainId, request);
    }

    public FDTransactionResult requestFDCancelPaidByUser(FeeDelegatedCancelWithRatio tx, String txHash, boolean submit) throws ApiException {
        FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setTransactionHash(txHash);
        request.setSubmit(submit);
        request.setFeePayer(tx.getFeePayer());

        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransaction(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByUser(FeeDelegatedChainDataAnchoring tx, boolean submit) throws ApiException {
        FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByUserApi().uFDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestFDChainDataAnchoringPaidByUser(FeeDelegatedChainDataAnchoringWithRatio tx, boolean submit) throws ApiException {
        FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
        request.setFrom(tx.getFrom());
        request.setInput(tx.getInput());
        request.setNonce(Numeric.toBigInt(tx.getNonce()).longValue());
        request.setGasLimit(Numeric.toBigInt(tx.getGas()).longValue());
        request.setFeeRatio(Numeric.toBigInt(tx.getFeeRatio()).longValue());
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByUserApi().uFDAnchorTransaction(chainId, request);
    }

    public FDTransactionResult requestRawTransactionPaidByUser(String rawTransaction, boolean submit) throws ApiException {
        FDProcessRLPRequest request = new FDProcessRLPRequest();
        request.setRlp(rawTransaction);
        request.setSubmit(submit);

        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLP(chainId, request);
    }



    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByUser(FeeDelegatedAccountUpdate tx, boolean submit) {
        FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();

        return null;
    }

    //TODO: It need to check that account update format.
    public FDTransactionResult requestFDAccountUpdatePaidByUser(FeeDelegatedAccountUpdateWithRatio tx, boolean submit) {
        FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();

        return null;
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
