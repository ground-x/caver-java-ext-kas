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

import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.methods.response.AccountKey;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractFeeDelegatedWithRatioTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.wallet.IWallet;
import com.klaytn.caver.wallet.keyring.SignatureData;
import xyz.groundx.caver_ext_kas.kas.wallet.Wallet;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representing a wallet class that uses KAS Wallet API.<br>
 * KASWallet used as a member of CaverExtKAS named wallet.<br>
 * If you called sign() to sign Caver's Transaction(ValueTransfer, SmartContractExecution..), It is signed using KAS Wallet API internally.
 */
public class KASWallet implements IWallet {
    /**
     * The WalletAPI instance to use KAS Wallet API.
     */
    Wallet walletAPI;

    /**
     * Creates a wallet instance that uses the KAS Wallet API. <br>
     * KASWallet used as a member of CaverExtKAS named wallet.
     * @param walletAPI An WalletAPI instance to use KAS Wallet API.
     */
    public KASWallet(Wallet walletAPI) {
        this.walletAPI = walletAPI;
    }

    /**
     * Generates accounts in KAS Wallet service.
     * @param num The number of accounts to generate.
     * @return List
     */
    @Override
    public List<String> generate(int num) throws ApiException {
        List<String> addressList = new ArrayList<>();
        for(int i=0; i < num; i++) {
            Account account = this.walletAPI.createAccount();
            addressList.add(account.getAddress());
        }
        return addressList;
    }

    /**
     * Get an account corresponding to the given address in KAS wallet service.
     * @param address An address to get account in KAS Wallet service.
     * @return Account
     * @throws ApiException
     */
    public Account getAccount(String address) throws ApiException {
        return this.walletAPI.getAccount(address);
    }

    /**
     * Removes an account in KAS Wallet API service.
     * @param address An address of account to remove.
     * @return boolean
     */
    @Override
    public boolean remove(String address) {
        try {
            AccountStatus status = this.walletAPI.deleteAccount(address);
            return status.getStatus().equals("deleted");
        } catch (ApiException e) {
            return false;
        }
    }

    /**
     * Check if the account corresponding to the address exists in KAS Wallet service.
     * @param address The address of account to find.
     * @return boolean
     */
    @Override
    public boolean isExisted(String address) {
        try {
            Account account = getAccount(address);
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    /**
     * Enable account in KAS Wallet service.
     * @param address The address of account to enable.
     * @return AccountSummary
     * @throws ApiException
     */
    public AccountSummary enableAccount(String address) throws ApiException {
        return this.walletAPI.enableAccount(address);
    }

    /**
     * Disable account in KAS Wallet service.
     * @param address The address of account to disable.
     * @return AccountSummary
     * @throws ApiException
     */
    public AccountSummary disableAccount(String address) throws ApiException {
        return this.walletAPI.disableAccount(address);
    }

    /**
     * Sign a transaction instance using an account in KAS Wallet service.
     * @param address An address of account to sign.
     * @param transaction A transaction instance to sign.
     * @return AbstractTransaction
     * @throws IOException
     * @throws ApiException
     */
    @Override
    public AbstractTransaction sign(String address, AbstractTransaction transaction) throws IOException, ApiException {
        if(transaction.getFrom().equals("0x")) {
            transaction.setFrom(address);
        }

        if(!transaction.getFrom().toLowerCase().equals(address.toLowerCase())) {
            throw new IllegalArgumentException("From address are not matched");
        }

        if(isWeightedMultiSigType(transaction, address)) {
            throw new IllegalArgumentException("Not supported: Using multiple keys in an account is currently not supported.");
        }

        List<SignatureData> signatureData = makeSignature(transaction);
        transaction.appendSignatures(signatureData);

        return transaction;
    }

    /**
     * Sign a transaction with the global fee payer using an account in KAS Wallet service.
     * @param feeDelegatedTransaction A fee delegated transaction instance.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     * @throws ApiException
     */
    public AbstractFeeDelegatedTransaction signAsGlobalFeePayer(AbstractFeeDelegatedTransaction feeDelegatedTransaction) throws IOException, ApiException {
        if(!feeDelegatedTransaction.getFeePayer().equals("0x")) {
            throw new RuntimeException("To sign the transaction using the global fee payer, feePayer cannot be defined in the transaction.");
        }

        feeDelegatedTransaction.fillTransaction();
        String rlp = feeDelegatedTransaction.getRLPEncoding();

        FDProcessRLPRequest rlpRequest = new FDProcessRLPRequest();
        rlpRequest.setRlp(rlp);
        rlpRequest.setSubmit(false);

        if(feeDelegatedTransaction instanceof AbstractFeeDelegatedWithRatioTransaction) {
            rlpRequest.setFeeRatio(((AbstractFeeDelegatedWithRatioTransaction) feeDelegatedTransaction).getFeeRatioInteger().longValue());
        }

        FDTransactionResult result = this.walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(rlpRequest);

        AbstractFeeDelegatedTransaction tx = (AbstractFeeDelegatedTransaction)TransactionDecoder.decode(result.getRlp());
        feeDelegatedTransaction.setFeePayer(tx.getFeePayer());
        feeDelegatedTransaction.appendFeePayerSignatures(tx.getFeePayerSignatures());

        return feeDelegatedTransaction;
    }

    /**
     * Sign a transaction as a fee payer using an account in KAS Wallet service.
     * @param feePayerAddress An address of account to sign as a fee payer in KAS Wallet service
     * @param feeDelegatedTransaction A fee delegated transaction instance.
     * @return AbstractFeeDelegatedTransaction
     * @throws ApiException
     * @throws IOException
     */
    @Override
    public AbstractFeeDelegatedTransaction signAsFeePayer(String feePayerAddress, AbstractFeeDelegatedTransaction feeDelegatedTransaction) throws ApiException, IOException {
        if(feePayerAddress == null) {
            return signAsGlobalFeePayer(feeDelegatedTransaction);
        }

        if(feeDelegatedTransaction.getFeePayer().equals("0x")) {
            feeDelegatedTransaction.setFeePayer(feePayerAddress);
        }

        if(!feeDelegatedTransaction.getFeePayer().toLowerCase().equals(feePayerAddress.toLowerCase())) {
            throw new IllegalArgumentException("Fee payer address are not matched");
        }

        if(isWeightedMultiSigType(feeDelegatedTransaction, feePayerAddress)) {
            throw new IllegalArgumentException("Not supported: Using multiple keys in an account is currently not supported.");
        }

        feeDelegatedTransaction.fillTransaction();
        String rlp = feeDelegatedTransaction.getRLPEncoding();

        FDUserProcessRLPRequest rlpRequest = new FDUserProcessRLPRequest();
        rlpRequest.setRlp(rlp);
        rlpRequest.setFeePayer(feeDelegatedTransaction.getFeePayer());
        rlpRequest.setSubmit(false);

        if(feeDelegatedTransaction instanceof AbstractFeeDelegatedWithRatioTransaction) {
            rlpRequest.setFeeRatio(((AbstractFeeDelegatedWithRatioTransaction) feeDelegatedTransaction).getFeeRatioInteger().longValue());
        }

        FDTransactionResult result = this.walletAPI.requestFDRawTransactionPaidByUser(rlpRequest);

        AbstractFeeDelegatedTransaction tx = (AbstractFeeDelegatedTransaction)TransactionDecoder.decode(result.getRlp());
        feeDelegatedTransaction.appendFeePayerSignatures(tx.getFeePayerSignatures());

        return feeDelegatedTransaction;
    }

    /**
     * Getter function for walletAPI
     * @return Wallet
     */
    public Wallet getWalletAPI() {
        return walletAPI;
    }

    /**
     * Setter function for walletAPI
     * @param walletAPI The WalletAPI instance to use KAS Wallet API.
     */
    public void setWalletAPI(Wallet walletAPI) {
        this.walletAPI = walletAPI;
    }


    private boolean isWeightedMultiSigType(AbstractTransaction transaction, String address) throws IOException {
        AccountKey res = transaction.getKlaytnCall().getAccountKey(address).send();
        if(res == null) {
            return true;
        }

        AccountKey.AccountKeyData accountKeyData = res.getResult();
        String type = accountKeyData.getType();

        if(type.equals(AccountKeyWeightedMultiSig.getType())) {
            return true;
        }

        if(type.equals(AccountKeyRoleBased.getType())) {
            AccountKeyRoleBased roleBased = (AccountKeyRoleBased) accountKeyData.getAccountKey();

            if(transaction instanceof AccountUpdate) {
                if(roleBased.getRoleAccountUpdateKey() instanceof AccountKeyWeightedMultiSig) {
                    return true;
                }
            } else {
                if(roleBased.getRoleTransactionKey() instanceof  AccountKeyWeightedMultiSig) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWeightedMultiSigType(AbstractFeeDelegatedTransaction fdTransaction, String address) throws IOException {
        AccountKey res = fdTransaction.getKlaytnCall().getAccountKey(address).send();
        if(res == null) {
            return true;
        }
        AccountKey.AccountKeyData accountKeyData = res.getResult();

        String type = accountKeyData.getType();

        if(type.equals(AccountKeyWeightedMultiSig.getType())) {
            return true;
        }

        if(type.equals(AccountKeyRoleBased.getType())) {
            AccountKeyRoleBased roleBased = (AccountKeyRoleBased) accountKeyData.getAccountKey();
            if(roleBased.getRoleFeePayerKey() instanceof AccountKeyWeightedMultiSig) {
                return true;
            }
        }

        return false;
    }

    private List<SignatureData> makeSignature(AbstractTransaction transaction) throws ApiException, IOException {
        if(transaction instanceof AbstractFeeDelegatedTransaction) {
            return makeSignature((AbstractFeeDelegatedTransaction)transaction);
        }

        transaction.fillTransaction();

        ProcessRLPRequest request = new ProcessRLPRequest();
        if(transaction instanceof LegacyTransaction) {
            request.setFrom(transaction.getFrom());
        }

        request.setRlp(transaction.getRLPEncoding());
        request.setSubmit(false);

        TransactionResult result = this.walletAPI.requestRawTransaction(request);
        return convertSignatureData(result.getSignatures());
    }

    
    private List<SignatureData> makeSignature(AbstractFeeDelegatedTransaction transaction) throws ApiException, IOException {
        transaction.fillTransaction();

        FDProcessRLPRequest request = new FDProcessRLPRequest();

        if (transaction instanceof AbstractFeeDelegatedWithRatioTransaction) {
            request.setFeeRatio(((AbstractFeeDelegatedWithRatioTransaction) transaction).getFeeRatioInteger().longValue());
        }

        request.setRlp(transaction.getRLPEncoding());
        request.setSubmit(false);

        FDTransactionResult result = this.walletAPI.requestFDRawTransactionPaidByGlobalFeePayer(request);
        return convertSignatureData(result.getSignatures());
    }

    private List<SignatureData> convertSignatureData(List<Signature> signatureList) {
        return signatureList.stream().map(signature -> new SignatureData(signature.getV(), signature.getR(), signature.getS()))
                .collect(Collectors.toList());
    }
}
