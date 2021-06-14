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

import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.rpc.RPC;
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdate;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.squareup.okhttp.Call;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.kas.utils.KASUtils;
import xyz.groundx.caver_ext_kas.kas.wallet.accountkey.KeyTypeMultiSig;
import xyz.groundx.caver_ext_kas.kas.wallet.accountkey.KeyTypePublic;
import xyz.groundx.caver_ext_kas.kas.wallet.accountkey.KeyTypeRoleBased;
import xyz.groundx.caver_ext_kas.kas.wallet.migration.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.api.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representing an wrapping class tha connects Wallet API.
 */
public class Wallet {

    /**
     * Account API rest client object.
     */
    AccountApi accountApi;

    /**
     * Basic transaction API rest client object.
     */
    BasicTransactionApi basicTransactionApi;

    /**
     * Fee delegated transaction(fee paid by KAS) API rest client object.
     */
    FeeDelegatedTransactionPaidByKasApi feeDelegatedTransactionPaidByKasApi;

    /**
     * Fee delegated transaction(fee paid by user) API rest client object.
     */
    FeeDelegatedTransactionPaidByUserApi feeDelegatedTransactionPaidByUserApi;

    /**
     * Multiple signature transaction management API rest client object.
     */
    MultisigTransactionManagementApi multisigTransactionManagementApi;

    /**
     * Statistics API rest client object.
     */
    StatisticsApi statisticsApi;

    /**
     * Key API rest client object.
     */
    KeyApi keyApi;

    /**
     * Registration API rest client object.
     */
    RegistrationApi registrationApi;

    /**
     * Klaytn network id.
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * The RPC for using Node API.
     */
    RPC rpc;

    /**
     * Creates an WalletAPI instance.
     * @param chainId A Klaytn network chain id.
     * @param walletApiClient The Api client for connection with KAS.
     */
    public Wallet(String chainId, ApiClient walletApiClient) {
        setChainId(chainId);
        setApiClient(walletApiClient);
    }

    /**
     * Migrates Klaytn accounts to KAS Wallet API. <br>
     * This method needs a RPC instance directing endpoint url of Node API. <br>
     * Therefore, you must initialize the node api before using this function. <br>
     *
     * <pre>Example :
     * {@code
     * ArrayList<MigrationAccount> accountsToBeMigrated = new ArrayList<>();
     * accountsToBeMigrated.add(new MigrationAccount.Builder()
     *         .setAddress("0x{address}")
     *         .setMigrationAccountKey(new SinglePrivateKey("0x{privateKey}"))
     *         .build()
     * );
     * accountsToBeMigrated.add(new MigrationAccount.Builder()
     *         .setAddress("0x{address}")
     *         .setMigrationAccountKey(new SinglePrivateKey("0x{privateKey}"))
     *         .build()
     * );
     *
     * RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
     * }</pre>
     *
     * @param accounts A list of accounts to be migrated to KAS Wallet Service.
     * @return RegistrationStatusResponse
     * @throws ApiException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public RegistrationStatusResponse migrateAccounts(List<MigrationAccount> accounts) throws ApiException, IOException, IllegalArgumentException, NoSuchFieldException {
        if (this.rpc == null) {
            throw new NoSuchFieldException("Before using migrateAccounts, initNodeAPI must  be called first.");
        }

        if (this.rpc.getWeb3jService() instanceof HttpService) {
            String url = ((HttpService) this.rpc.getWeb3jService()).getUrl();
            if (!url.contains("klaytnapi")) {
                System.out.println("WARN: Endpoint URL of Node API is " + url + " which is not klaytnapi.");
                System.out.println("WARN: You should initialize Node API with working endpoint url before calling migrateAccounts.");
            }
        }

        // Need to validate whether given list of migration accounts is valid or not.
        for (int i=0; i<accounts.size(); i++) {
            if (validateMigrationAccount(accounts.get(i)) == false) {
                throw new IllegalArgumentException("Given MigrationAccount is not valid.");
            };
        }

        AccountRegistrationRequest request = new AccountRegistrationRequest();

        KeyCreationResponse keyCreationResponse = this.createKeys(accounts.size());
        List<Key> createdKeys = keyCreationResponse.getItems();

        String gasPrice = this.rpc.klay.getGasPrice().send().getResult();
        for (int i = 0; i < createdKeys.size(); i++) {
            MigrationAccount migrationAccount = accounts.get(i);
            Key key = createdKeys.get(i);

            if (migrationAccount.getNonce() == "0x") {
                String nonce = this.rpc.klay.getTransactionCount(
                        migrationAccount.getAddress(),
                        DefaultBlockParameterName.PENDING
                ).send().getResult();
                migrationAccount.setNonce(nonce);
            }

            FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                    .setChainId(BigInteger.valueOf(Integer.parseInt(chainId)))
                    .setFrom(migrationAccount.getAddress())
                    .setNonce(migrationAccount.getNonce())
                    .setAccount(
                            com.klaytn.caver.account.Account.createWithAccountKeyPublic(
                                    migrationAccount.getAddress(),
                                    key.getPublicKey()
                            )
                    )
                    .setGas(BigInteger.valueOf(1000000))
                    .setGasPrice(gasPrice)
                    .build();

            AbstractKeyring keyring = createKeyringFromMigrationAccount(migrationAccount);
            tx.sign(keyring);

            AccountRegistration accountRegistration = new AccountRegistration();
            accountRegistration.setKeyId(key.getKeyId());
            accountRegistration.setAddress(keyring.getAddress());
            accountRegistration.setRlp(tx.getRLPEncoding());
            request.add(accountRegistration);
        }

        return this.registerAccounts(request);
    }

    /**
     * Creates a Klaytn account.<br>
     * It generates Klaytn address and private key, public key pair.<br>
     * POST /v2/account
     * @return Account
     * @throws ApiException
     */
    public Account createAccount() throws ApiException {
        return getAccountApi().createAccount(chainId);
    }

    /**
     * Creates a Klaytn account asynchronously.<br>
     * It generates Klaytn address and private key, public key pair.<br>
     * POST /v2/account
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call createAccountAsync(ApiCallback<Account> callback) throws ApiException {
        return getAccountApi().createAccountAsync(chainId, callback);
    }

    /**
     * Get the list of accounts created previously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/account
     * @return Accounts
     * @throws ApiException
     */
    public Accounts getAccountList() throws ApiException {
        return getAccountList(new WalletQueryOptions());
    }

    /**
     * Get the list of accounts created previously asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/account
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getAccountListAsync(ApiCallback<Accounts> callback) throws ApiException {
        return getAccountListAsync(new WalletQueryOptions(), callback);
    }

    /**
     * Get the list of accounts created previously.<br>
     * GET /v2/account
     * @param options Filters required when retrieving data. `to-timestamp`, `from-timestamp`, `size`, `status` and `cursor`.
     * @return Accounts
     * @throws ApiException
     */
    public Accounts getAccountList(WalletQueryOptions options) throws ApiException {
        return getAccountApi().retrieveAccounts(chainId, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp(), options.getStatus());
    }

    /**
     * Get the list of accounts created previously asynchronously.<br>
     * GET /v2/account
     * @param options Filters required when retrieving data. `to-timestamp`, `from-timestamp`, `size`, `status` and `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getAccountListAsync(WalletQueryOptions options, ApiCallback<Accounts> callback) throws ApiException {
        return getAccountApi().retrieveAccountsAsync(chainId, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp(), options.getStatus(), callback);
    }

    /**
     * Get the account information passed as a parameter.<br>
     * GET /v2/account/{address}
     * @param address The address to get account information.
     * @return Account
     * @throws ApiException
     */
    public Account getAccount(String address) throws ApiException {
        return getAccountApi().retrieveAccount(chainId, address);
    }

    /**
     * Get the account information passed as a parameter asynchronously.<br>
     * GET /v2/account/{address}
     * @param address The address to get account information.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getAccountAsync(String address, ApiCallback<Account> callback) throws ApiException {
        return getAccountApi().retrieveAccountAsync(chainId, address, callback);
    }

    /**
     * Delete an account.<br>
     * DELETE /v2/account/{address}
     * @param address The address to delete.
     * @return AccountStatus
     * @throws ApiException
     */
    public AccountStatus deleteAccount(String address) throws ApiException {
        return getAccountApi().deleteAccount(chainId, address);
    }

    /**
     * Delete an account asynchronously.<br>
     * DELETE /v2/account/{address}
     * @param address The address to delete.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deleteAccountAsync(String address, ApiCallback<AccountStatus> callback) throws ApiException {
        return getAccountApi().deleteAccountAsync(chainId, address, callback);
    }

    /**
     * Disable an account.<br>
     * The disabled account will not be retrieved account information.<br>
     * PUT /v2/account/{address}/disable
     * @param address The address to disable
     * @return AccountStatus
     * @throws ApiException
     */
    public AccountSummary disableAccount(String address) throws ApiException {
        return getAccountApi().deactivateAccount(chainId, address);
    }

    /**
     * Disable an account asynchronously.<br>
     * The disabled account will not be retrieved account information.<br>
     * PUT /v2/account/{address}/disable
     * @param address The address to disable
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call disableAccountAsync(String address, ApiCallback<AccountSummary> callback) throws ApiException {
        return getAccountApi().deactivateAccountAsync(chainId, address, callback);
    }

    /**
     * Enable an account.<br>
     * PUT /v2/account/{address}/enable
     * @param address The address to enable.
     * @return AccountStatus
     * @throws ApiException
     */
    public AccountSummary enableAccount(String address) throws ApiException {
        return getAccountApi().activateAccount(chainId, address);
    }

    /**
     * Enable an account asynchronously.<br>
     * PUT /v2/account/{address}/enable
     * @param address The address to enable.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call enableAccountAsync(String address, ApiCallback<AccountSummary> callback) throws ApiException {
        return getAccountApi().activateAccountAsync(chainId, address, callback);
    }

    /**
     * Signs the transaction corresponding to the passed TransactionID with the key of the passed address.<br>
     * POST /v2/account/{address}/tx/{transaction-id}/sign
     * @param address The address to sign transaction.
     * @param transactionId The transaction id to get transaction for sign.
     * @return Signature
     * @throws ApiException
     */
    public Signature signTransaction(String address, String transactionId) throws ApiException {
        return getAccountApi().signTransactionIDResponse(chainId, address, transactionId);
    }

    /**
     * Signs the transaction corresponding to the passed TransactionID with the key of the passed address asynchronously.<br>
     * POST /v2/account/{address}/tx/{transaction-id}/sign
     * @param address The address to sign transaction.
     * @param transactionId The transaction id to get transaction for sign.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call signTransactionAsync(String address, String transactionId, ApiCallback<Signature> callback) throws ApiException {
        return getAccountApi().signTransactionIDResponseAsync(chainId, address, transactionId, callback);
    }

    /**
     * Updates an account to MultiSig Account.<br>
     * PUT /v2/account/{address}/multisig
     * @param address The address to update multisig account.
     * @param request The MultisigAccountUpdateRequest instance required for updating account.
     * @return MultisigAccount
     * @throws ApiException
     */
    public MultisigAccount updateToMultiSigAccount(String address, MultisigAccountUpdateRequest request) throws ApiException {
        return getAccountApi().multisigAccountUpdate(chainId, address, request);
    }

    /**
     * Updates an account to MultiSig Account asynchronously.<br>
     * PUT /v2/account/{address}/multisig
     * @param address The address to update multisig account.
     * @param request The MultisigAccountUpdateRequest instance required for updating account.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateToMultiSigAccountAsync(String address, MultisigAccountUpdateRequest request, ApiCallback<MultisigAccount> callback) throws ApiException {
        return getAccountApi().multisigAccountUpdateAsync(chainId, address, request, callback);
    }

    /**
     * Get list of accounts that has a public key passed as a parameter.<br>
     * GET /v2/pubkey/{public-key}/account
     * @param publicKey A public key to get list of accounts.
     * @return AccountsByPubkey
     * @throws ApiException
     */
    public AccountsByPubkey getAccountListByPublicKey(String publicKey) throws ApiException {
        return getAccountApi().retrieveAccountsByPubkey(chainId ,publicKey);
    }

    /**
     * Get list of accounts that has a public key passed as a parameter asynchronously.<br>
     * GET /v2/pubkey/{public-key}/account
     * @param publicKey A public key to get list of accounts.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getAccountListByPublicKeyAsync(String publicKey, ApiCallback<AccountsByPubkey> callback) throws ApiException {
        return getAccountApi().retrieveAccountsByPubkeyAsync(chainId ,publicKey, callback);
    }

    /**
     * Send a Legacy transaction.<br>
     * POST /v2/tx/legacy
     * @param request The LegacyTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestLegacyTransaction(LegacyTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().legacyTransaction(chainId, request);
    }

    /**
     * Send a Legacy transaction asynchronously.<br>
     * POST /v2/tx/legacy
     * @param request The LegacyTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestLegacyTransactionAsync(LegacyTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().legacyTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a ValueTransfer transaction.<br>
     * POST /v2/tx/value
     * @param request The ValueTransferTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestValueTransfer(ValueTransferTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().valueTransferTransaction(chainId, request);
    }

    /**
     * Send a ValueTransfer transaction asynchronously.<br>
     * POST /v2/tx/value
     * @param request The ValueTransferTransactionRequest instance to send a transaction.
     * @param callback THe callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestValueTransferAsync(ValueTransferTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().valueTransferTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a SmartContractDeploy transaction.<br>
     * POST /v2/tx/contract/deploy
     * @param request The ContractDeployTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestSmartContractDeploy(ContractDeployTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().contractDeployTransaction(chainId, request);
    }

    /**
     * Send a SmartContractDeploy transaction asynchronously.<br>
     * POST /v2/tx/contract/deploy
     * @param request The ContractDeployTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestSmartContractDeployAsync(ContractDeployTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().contractDeployTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a SmartContractExecution transaction.<br>
     * POST /v2/tx/contract/execute
     * @param request The ContractExecutionTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestSmartContractExecution(ContractExecutionTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().contractExecutionTransaction(chainId, request);
    }

    /**
     * Send a SmartContractExecution transaction asynchronously.<br>
     * POST /v2/tx/contract/execute
     * @param request The ContractExecutionTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestSmartContractExecutionAsync(ContractExecutionTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().contractExecutionTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a Cancel transaction.<br>
     * DELETE /v2/tx
     * @param request The CancelTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestCancel(CancelTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().cancelTransaction(chainId, request);
    }

    /**
     * Send a Cancel transaction asynchronously.<br>
     * DELETE /v2/tx
     * @param request The CancelTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestCancelAsync(CancelTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().cancelTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a ChainDataAnchoring transaction.<br>
     * POST /v2/tx/anchor
     * @param request The AnchorTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestChainDataAnchoring(AnchorTransactionRequest request) throws ApiException {
        return getBasicTransactionApi().anchorTransaction(chainId, request);
    }

    /**
     * Send a ChainDataAnchoring transaction asynchronously.<br>
     * POST /v2/tx/anchor
     * @param request The AnchorTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestChainDataAnchoringAsync(AnchorTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().anchorTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a raw transaction.<br>
     * POST /v2/tx/rlp
     * @param request The ProcessRLPRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestRawTransaction(ProcessRLPRequest request) throws ApiException {
        return getBasicTransactionApi().processRLP(chainId, request);
    }

    /**
     * Send a raw transaction asynchronously.<br>
     * POST /v2/tx/rlp
     * @param request The ProcessRLPRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestRawTransactionAsync(ProcessRLPRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        return getBasicTransactionApi().processRLPAsync(chainId, request, callback);
    }

    /**
     * Send a AccountUpdate transaction.<br>
     * POST /v2/tx/account
     * @param request The AccountUpdateTransactionRequest instance to send a transaction.
     * @return TransactionResult
     * @throws ApiException
     */
    public TransactionResult requestAccountUpdate(AccountUpdateTransactionRequest request) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getBasicTransactionApi().accountUpdateTransaction(chainId, request);
    }

    /**
     * Send a AccountUpdate transaction asynchronously.<br>
     * POST /v2/tx/account
     * @param request The AccountUpdateTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestAccountUpdateAsync(AccountUpdateTransactionRequest request, ApiCallback<TransactionResult> callback) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getBasicTransactionApi().accountUpdateTransactionAsync(chainId, request, callback);
    }

    /**
     * Get a transaction receipt.<br>
     * GET /v2/tx/{transaction-hash}
     * @param transactionHash A transaction hash to get a transaction receipt.
     * @return TransactionReceipt
     * @throws ApiException
     */
    public TransactionReceipt getTransaction(String transactionHash) throws ApiException {
        return getBasicTransactionApi().transactionReceipt(chainId, transactionHash);
    }

    /**
     * Get a transaction receipt asynchronously.<br>
     * GET /v2/tx/{transaction-hash}
     * @param transactionHash A transaction hash to get a transaction receipt.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransactionAsync(String transactionHash, ApiCallback<TransactionReceipt> callback) throws ApiException {
        return getBasicTransactionApi().transactionReceiptAsync(chainId, transactionHash, callback);
    }

    /**
     * Send a FeeDelegatedValueTransfer(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/value
     * @param request The FDValueTransferTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDValueTransferPaidByGlobalFeePayer(FDValueTransferTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedValueTransfer(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/value
     * @param request The FDValueTransferTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDValueTransferPaidByGlobalFeePayerAsync(FDValueTransferTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDValueTransferTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedSmartContractDeploy(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/contract/deploy
     * @param request The FDContractDeployTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDSmartContractDeployPaidByGlobalFeePayer(FDContractDeployTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractDeployTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedSmartContractDeploy(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/contract/deploy
     * @param request The FDContractDeployTransactionRequest instance to send a transaction.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDSmartContractDeployPaidByGlobalFeePayerAsync(FDContractDeployTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractDeployTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedSmartContractExecution(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/contract/execute
     * @param request The FDContractExecutionTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDSmartContractExecutionPaidByGlobalFeePayer(FDContractExecutionTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractExecutionTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedSmartContractExecution(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/contract/execute
     * @param request The FDContractExecutionTransactionRequest instance to send a transaction.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDSmartContractExecutionPaidByGlobalFeePayerAsync(FDContractExecutionTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDContractExecutionTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedCancelTransaction(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * DELETE /v2/tx/fd
     * @param request The FDCancelTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDCancelPaidByGlobalFeePayer(FDCancelTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDCancelTransactionResponse(chainId, request);
    }

    /**
     * Send a FeeDelegatedCancelTransaction(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * DELETE /v2/tx/fd
     * @param request The FDCancelTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDCancelPaidByGlobalFeePayerAsync(FDCancelTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDCancelTransactionResponseAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedChainDataAnchoring(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/anchor
     * @param request The FDAnchorTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDChainDataAnchoringPaidByGlobalFeePayer(FDAnchorTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDAnchorTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedChainDataAnchoring(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/anchor
     * @param request The FDAnchorTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDChainDataAnchoringPaidByGlobalFeePayerAsync(FDAnchorTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDAnchorTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegated(WithRatio) type raw transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/rlp
     * @param request The FDProcessRLPRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDRawTransactionPaidByGlobalFeePayer(FDProcessRLPRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLP(chainId, request);
    }

    /**
     * Send a FeeDelegated(WithRatio) type raw transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd/rlp
     * @param request The FDProcessRLPRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDRawTransactionPaidByGlobalFeePayerAsync(FDProcessRLPRequest request, ApiCallback callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLPAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedAccountUpdate(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * PUT /v2/tx/fd/account
     * @param request The FDAccountUpdateTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDAccountUpdatePaidByGlobalFeePayer(FDAccountUpdateTransactionRequest request) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getFeeDelegatedTransactionPaidByKasApi().fDAccountUpdateTransactionResponse(chainId, request);
    }

    /**
     * Send a FeeDelegatedAccountUpdate(WithRatio) transaction asynchronously.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * PUT /v2/tx/fd/account
     * @param request The FDAccountUpdateTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDAccountUpdatePaidByGlobalFeePayerAsync(FDAccountUpdateTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getFeeDelegatedTransactionPaidByKasApi().fDAccountUpdateTransactionResponseAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedValueTransfer(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/value
     * @param request The FDUserValueTransferTransactionRequest instance to send a transaction.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDValueTransferPaidByUser(FDUserValueTransferTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedValueTransfer(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/value
     * @param request The FDUserValueTransferTransactionRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDValueTransferPaidByUserAsync(FDUserValueTransferTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDValueTransferTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedSmartContractDeploy(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/contract/deploy
     * @param request The FDUserContractDeployTransactionRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDSmartContractDeployPaidByUser(FDUserContractDeployTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedSmartContractDeploy(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/contract/deploy
     * @param request The FDUserContractDeployTransactionRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDSmartContractDeployPaidByUserAsync(FDUserContractDeployTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractDeployTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedSmartContractExecution(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/contract/execute
     * @param request The FDUserContractExecutionTransactionRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDSmartContractExecutionPaidByUser(FDUserContractExecutionTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractExecutionTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedSmartContractExecution(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/contract/execute
     * @param request The FDUserContractExecutionTransactionRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDSmartContractExecutionPaidByUserAsync(FDUserContractExecutionTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDContractExecutionTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedCancel(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * DELETE /v2/tx/fd-user
     * @param request The FDUserCancelTransactionRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDCancelPaidByUser(FDUserCancelTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedCancel(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * DELETE /v2/tx/fd-user
     * @param request The FDUserCancelTransactionRequest instance to send a request.
     * @param callback The callback function to handle response
     * @return Call
     * @throws ApiException
     */
    public Call requestFDCancelPaidByUserAsync(FDUserCancelTransactionRequest request, ApiCallback callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedChainDataAnchoring(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/anchor
     * @param request The FDUserAnchorTransactionRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDChainDataAnchoringPaidByUser(FDUserAnchorTransactionRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDAnchorTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedChainDataAnchoring(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/anchor
     * @param request The FDUserAnchorTransactionRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDChainDataAnchoringPaidByUserAsync(FDUserAnchorTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDAnchorTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegated(WithRatio) raw transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/rlp
     * @param request The FDUserProcessRLPRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDRawTransactionPaidByUser(FDUserProcessRLPRequest request) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDProcessRLP(chainId, request);
    }

    /**
     * Send a FeeDelegated(WithRatio) raw transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/rlp
     * @param request The FDUserProcessRLPRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDRawTransactionPaidByUserAsync(FDUserProcessRLPRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDProcessRLPAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedAccountUpdate(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * PUT /v2/tx/fd-user/account
     * @param request The FDUserAccountUpdateTransactionRequest instance to send a request.
     * @return FDTransactionResult
     * @throws ApiException
     */
    public FDTransactionResult requestFDAccountUpdatePaidByUser(FDUserAccountUpdateTransactionRequest request) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getFeeDelegatedTransactionPaidByUserApi().uFDAccountUpdateTransaction(chainId, request);
    }

    /**
     * Send a FeeDelegatedAccountUpdate(WithRatio) transaction asynchronously.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * PUT /v2/tx/fd-user/account
     * @param request The FDUserAccountUpdateTransactionRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDAccountUpdatePaidByUserAsync(FDUserAccountUpdateTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        request.setAccountKey(makeUncompressedKeyFormat(request.getAccountKey()));
        return getFeeDelegatedTransactionPaidByUserApi().uFDAccountUpdateTransactionAsync(chainId, request, callback);
    }

    /**
     * Get pending transaction list.<br>
     * It will send a request without filter options.<br>
     * GET /v2/multisig/account/{address}/tx
     * @param address The sender address to get pending transaction list.
     * @return MultisigTransactions
     * @throws ApiException
     */
    public MultisigTransactions getMultiSigTransactionList(String address) throws ApiException {
        return getMultiSigTransactionList(address, new WalletQueryOptions());
    }

    /**
     * Get pending transaction list asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v2/multisig/account/{address}/tx
     * @param address The sender address to get pending transaction list.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMultiSigTransactionListAsync(String address, ApiCallback<MultisigTransactions> callback) throws ApiException {
        return getMultiSigTransactionListAsync(address, new WalletQueryOptions(), callback);
    }

    /**
     * Get pending transaction list.<br>
     * GET /v2/multisig/account/{address}/tx
     * @param address The sender address to retrieve pending transaction list.
     * @param options Filter required when retrieving data. `cursor`, `to-timestamp`, `from-timestamp`
     * @return MultisigTransactions
     * @throws ApiException
     */
    public MultisigTransactions getMultiSigTransactionList(String address, WalletQueryOptions options) throws ApiException {
        return getMultisigTransactionManagementApi().retrieveMultisigTransactions(chainId, address, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp());
    }

    /**
     * Get pending transaction list asynchronously.<br>
     * GET /v2/multisig/account/{address}/tx
     * @param address The sender address to retrieve pending transaction list.
     * @param options Filter required when retrieving data. `cursor`, `to-timestamp`, `from-timestamp`
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getMultiSigTransactionListAsync(String address, WalletQueryOptions options, ApiCallback<MultisigTransactions> callback) throws ApiException {
        return getMultisigTransactionManagementApi().retrieveMultisigTransactionsAsync(chainId, address, options.getSize(), options.getCursor(), options.getToTimestamp(), options.getFromTimestamp(), callback);
    }

    /**
     * Sign a pending transaction.<br>
     * POST /v2/multisig/account/{address}/tx/{transaction-id}/sign
     * @param address The singer address to sign.
     * @param transactionId The pending transaction id.
     * @return MultisigTransactionStatus
     * @throws ApiException
     */
    public MultisigTransactionStatus signMultiSigTransaction(String address, String transactionId) throws ApiException {
        return getMultisigTransactionManagementApi().signPendingTransaction(chainId, address, transactionId);
    }

    /**
     * Sign a pending transaction asynchronously.<br>
     * POST /v2/multisig/account/{address}/tx/{transaction-id}/sign
     * @param address The singer address to sign.
     * @param transactionId The pending transaction id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call signMultiSigTransactionAsync(String address, String transactionId, ApiCallback<MultisigTransactionStatus> callback) throws ApiException {
        return getMultisigTransactionManagementApi().signPendingTransactionAsync(chainId, address, transactionId, callback);
    }

    /**
     * Append a signature to pending transaction.<br>
     * POST /v2/multisig/tx/{transaction-id}/sign
     * @param transactionId The transaction id to append signature.
     * @param request The SignPendingTransactionBySigRequest instance to send a request.
     * @return MultisigTransactionStatus
     * @throws ApiException
     */
    public MultisigTransactionStatus appendSignatures(String transactionId, SignPendingTransactionBySigRequest request) throws ApiException {
        return getMultisigTransactionManagementApi().signPendingTransactionBySig(chainId, transactionId, request);
    }

    /**
     * Append a signature to pending transaction asynchronously.<br>
     * POST /v2/multisig/tx/{transaction-id}/sign
     * @param transactionId The transaction id to append signature.
     * @param request The SignPendingTransactionBySigRequest instance to send a request.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call appendSignaturesAsync(String transactionId, SignPendingTransactionBySigRequest request, ApiCallback<MultisigTransactionStatus> callback) throws ApiException {
        return getMultisigTransactionManagementApi().signPendingTransactionBySigAsync(chainId, transactionId, request, callback);
    }

    /**
     * Return the number of accounts in KAS.<br>
     * GET /v2/stat/count
     * @return AccountCountByAccountID
     * @throws ApiException
     */
    public AccountCountByAccountID getAccountCount() throws ApiException {
        return getStatisticsApi().getAccountCountByAccountID(getChainId());
    }

    /**
     * Return the number of accounts in KAS asynchronously.<br>
     * GET /v2/stat/count
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getAccountCountAsync(ApiCallback<AccountCountByAccountID> callback) throws ApiException {
        return getStatisticsApi().getAccountCountByAccountIDAsync(getChainId(), callback);
    }

    /**
     * Return the number of accounts by passed as KRN in KAS.<br>
     * It use default krn.<br>
     * GET /v2/stat/count/krn
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public AccountCountByKRN getAccountCountByKRN() throws ApiException {
        return getAccountCountByKRN(null);
    }

    /**
     * Return the number of accounts by passed as KRN in KAS asynchronously.<br>
     * It use default krn.<br>
     * GET /v2/stat/count/krn
     * @param callback The callback function to handle response.
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public Call getAccountCountByKRNAsync(ApiCallback<AccountCountByKRN> callback) throws ApiException {
        return getAccountCountByKRNAsync(null, callback);
    }

    /**
     * Return the number of accounts by passed as KRN in KAS.<br>
     * GET /v2/stat/count/krn
     * @param krn The krn string to search
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public AccountCountByKRN getAccountCountByKRN(String krn) throws ApiException {
        return getStatisticsApi().getAccountCountByKRN(getChainId(), krn);
    }

    /**
     * Return the number of accounts by passed as KRN in KAS asynchronously.<br>
     * GET /v2/stat/count/krn
     * @param krn The krn string to search
     * @param callback The callback function to handle response.
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public Call getAccountCountByKRNAsync(String krn, ApiCallback<AccountCountByKRN> callback) throws ApiException {
        return getStatisticsApi().getAccountCountByKRNAsync(getChainId(), krn, callback);
    }

    /**
     * Create keys in KAS. <br>
     * POST /v2/key <br>
     *
     * <pre>Example :
     * {@code
     * KeyCreationResponse response = caver.kas.wallet.createKeys(2);
     * }</pre>
     * @param numberOfKeys The number of keys to create
     * @return KeyCreationResponse
     * @throws ApiException
     */
    public KeyCreationResponse createKeys(int numberOfKeys) throws ApiException {
        KeyCreationRequest request = new KeyCreationRequest();
        request.setSize(Long.valueOf(numberOfKeys));
        return getKeyApi().keyCreation(chainId, request);
    }

    /**
     * Creates keys in KAS asynchronously. <br>
     * POST /v2/key <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<KeyCreationResponse> callback = new ApiCallback<KeyCreationResponse> callback() {
     *    ....implements callback method
     * };
     * caver.kas.wallet.createKeysAsync(2, callback);
     * }</pre>
     * @param numberOfKeys The number of keys to create
     * @param callback The callback function to handle response.
     * @return KeyCreationResponse
     * @throws ApiException
     */
    public Call createKeysAsync(int numberOfKeys, ApiCallback<KeyCreationResponse> callback) throws ApiException {
        KeyCreationRequest request = new KeyCreationRequest();
        request.setSize(Long.valueOf(numberOfKeys));
        return getKeyApi().keyCreationAsync(chainId, request, callback);
    }

    /**
     * Find a key information from KAS. <br>
     * GET /v2/key/{key-id} <br>
     *
     * <pre>Example :
     * {@code
     * String keyId = "keyId";
     * Key key = caver.kas.wallet.getKey(keyId);
     * }</pre>
     *
     * @param keyId The key id to find from KAS.
     * @return Key
     * @throws ApiException
     */
    public Key getKey(String keyId) throws ApiException {
        return getKeyApi().getKey(chainId, keyId);
    }

    /**
     * Find a key information from KAS asynchronously. <br>
     * GET /v2/key/{key-id} <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Key> callback = new ApiCallback<Key> callback() {
     *   ....implements callback method.
     * }
     *
     * String keyId = "keyId";
     * caver.kas.wallet.getKeyAsync(keyId, callback);
     *
     * }</pre>
     * @param keyId The key id to find from KAS.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getKeyAsync(String keyId, ApiCallback<Key> callback) throws ApiException {
        return getKeyApi().getKeyAsync(chainId, keyId, callback);
    }

    /**
     * Sign a message using a key create by KAS. <br>
     * The default KRN will be used. <br>
     * POST /v2/key/{key-id}/sign <br>
     *
     * <pre>Example :
     * {@code
     * String keyId = "key Id";
     * String data = "data";
     *
     * caver.kas.wallet.signMessage(keyId, data);
     * }</pre>
     *
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @return KeySignDataResponse
     * @throws ApiException
     */
    public KeySignDataResponse signMessage(String keyId, String data) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return signMessage(keyId, data, "");
    }

    /**
     * Sign a message using a key create by KAS. <br>
     * POST /v2/key/{key-id}/sign <br>
     *
     * <pre>Example :
     * {@code
     * String keyId = "key Id";
     * String data = "data";
     * String krn = "krn";
     *
     * caver.kas.wallet.signMessage(keyId, data, krn);
     * }</pre>
     *
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @param krn The krn string.
     * @return KeySignDataResponse
     * @throws ApiException
     */
    public KeySignDataResponse signMessage(String keyId, String data, String krn) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return getKeyApi().keySignData(chainId, keyId, request, krn);
    }

    /**
     * Sign a message using a key create by KAS asynchronously. <br>
     * The default KRN will be used. <br>
     * POST /v2/key/{key-id}/sign <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<KeySignDataResponse> callback = new ApiCallback<KeySignDataResponse> callback() {
     *     ....implements callback method.
     * }
     *
     * String keyId = "key Id";
     * String data = "data";
     *
     * caver.kas.wallet.signMessageAsync(keyId, data, callback);
     * }</pre>
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call signMessageAsync(String keyId, String data, ApiCallback<KeySignDataResponse> callback) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return signMessageAsync(keyId, data, null, callback);
    }

    /**
     * Sign a message using a key create by KAS asynchronously. <br>
     * The default KRN will be used. <br>
     * POST /v2/key/{key-id}/sign <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<KeySignDataResponse> callback = new ApiCallback<KeySignDataResponse> callback() {
     *     ....implements callback method.
     * }
     *
     * String keyId = "key Id";
     * String data = "data";
     * String krn = "krn"
     *
     * caver.kas.wallet.signMessageAsync(keyId, data, krn, callback);
     * }</pre>
     *
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @param krn The krn string.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call signMessageAsync(String keyId, String data, String krn, ApiCallback<KeySignDataResponse> callback) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return getKeyApi().keySignDataAsync(chainId, keyId, request, krn, callback);
    }

    /**
     * Register accounts which used before. <br>
     * POST /v2/registration/account <br>
     *
     * <pre>Example :
     * {@code
     *
     * FeeDelegatedAccountUpdate createAccountUpdateTx(Caver caver, SingleKeyring keyring, String newKey) throws IOException {
     *         com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyPublic(keyring.getAddress(), newKey);
     *
     *         FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
     *                 .setKlaytnCall(caver.rpc.klay)
     *                 .setFrom(keyring.getAddress())
     *                 .setGas(BigInteger.valueOf(250000))
     *                 .setAccount(account)
     *                 .build();
     *
     *         tx.sign(keyring);
     *
     *         return tx;
     * }
     *
     * KeyCreationResponse response = caver.kas.wallet.createKeys(1);
     *
     * SingleKeyring keyring = KeyringFactory.generate();
     *
     * AccountRegistration registration = new AccountRegistration();
     * registration.setAddress(keyring.getAddress());
     * registration.setKeyId(response.getItems().get(0).getKeyId());
     * registration.setRlp(createAccountUpdateTx(caver, keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());
     *
     * AccountRegistrationRequest request = new AccountRegistrationRequest();
     * request.add(registration);
     *
     * RegistrationStatusResponse result = caver.kas.wallet.registerAccounts(request);
     * }</pre>
     *
     * @param request The AccountRegistrationRequest instance contains account informations to be registered in KAS <br>
     *                The rlp field of AccountRegistration should be set as an encoded FeeDelegatedAccountUpdate(without set a fee payer and fee payer signature) using the key and the address to be registered in KAS. <br>
     * @return RegistrationStatusResponse
     * @throws ApiException
     */
    public RegistrationStatusResponse registerAccounts(AccountRegistrationRequest request) throws ApiException {
        return registerAccounts((List<AccountRegistration>)request);
    }

    /**
     * Register accounts which used before. <br>
     * POST /v2/registration/account <br>
     *
     * <pre>Example :
     * {@code
     *
     * FeeDelegatedAccountUpdate createAccountUpdateTx(Caver caver, SingleKeyring keyring, String newKey) throws IOException {
     *         com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyPublic(keyring.getAddress(), newKey);
     *
     *         FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
     *                 .setKlaytnCall(caver.rpc.klay)
     *                 .setFrom(keyring.getAddress())
     *                 .setGas(BigInteger.valueOf(250000))
     *                 .setAccount(account)
     *                 .build();
     *
     *         tx.sign(keyring);
     *
     *         return tx;
     * }
     *
     * KeyCreationResponse response = caver.kas.wallet.createKeys(1);
     *
     * SingleKeyring keyring = KeyringFactory.generate();
     * com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());
     *
     * AccountRegistration registration = new AccountRegistration();
     * registration.setAddress(account.getAddress());
     * registration.setKeyId(response.getItems().get(0).getKeyId());
     * registration.setRlp(createAccountUpdateTx(caver, keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());
     *
     * RegistrationStatusResponse result = caver.kas.wallet.registerAccounts(Arrays.asList(request));
     * }</pre>
     *
     * @param request The List of account information to be registered in KAS. <br>
     *                The rlp field of AccountRegistration should be set as an encoded FeeDelegatedAccountUpdate(without set a fee payer and fee payer signature) using the key and the address to be registered in KAS. <br>
     * @return RegistrationStatusResponse
     * @throws ApiException
     */
    public RegistrationStatusResponse registerAccounts(List<AccountRegistration> request) throws ApiException {
        return getRegistrationApi().registerAccount(chainId, request);
    }

    /**
     * Register accounts which used before asynchronously. <br>
     * POST /v2/registration/account <br>
     *
     * <pre>Example :
     * {@code
     *
     * FeeDelegatedAccountUpdate createAccountUpdateTx(Caver caver, SingleKeyring keyring, String newKey) throws IOException {
     *         com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyPublic(keyring.getAddress(), newKey);
     *
     *         FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
     *                 .setKlaytnCall(caver.rpc.klay)
     *                 .setFrom(keyring.getAddress())
     *                 .setGas(BigInteger.valueOf(250000))
     *                 .setAccount(account)
     *                 .build();
     *
     *         tx.sign(keyring);
     *
     *         return tx;
     * }
     *
     * ApiCallback<RegistrationStatusResponse> callback = new ApiCallback<RegistrationStatusResponse> callback() {
     *    ....implement callback methods.
     * };
     *
     * KeyCreationResponse response = caver.kas.wallet.createKeys(1);
     *
     * SingleKeyring keyring = KeyringFactory.generate();
     * com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());
     *
     * AccountRegistration registration = new AccountRegistration();
     * registration.setAddress(account.getAddress());
     * registration.setKeyId(response.getItems().get(0).getKeyId());
     * registration.setRlp(createAccountUpdateTx(caver, keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());
     *
     * AccountRegistrationRequest request = new AccountRegistrationRequest();
     * request.add(registration);
     *
     * RegistrationStatusResponse result = caver.kas.wallet.registerAccountsAsync(request, callback);
     * }</pre>
     *
     * @param request The AccountRegistrationRequest instance contains account informations to be registered in KAS. <br>
     *                The rlp field of AccountRegistration should be set as an encoded FeeDelegatedAccountUpdate(without set a fee payer and fee payer signature) using the key and the address to be registered in KAS. <br>
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call registerAccountsAsync(AccountRegistrationRequest request, ApiCallback<RegistrationStatusResponse> callback) throws ApiException {
        return registerAccountsAsync((List<AccountRegistration>)request, callback);
    }

    /**
     * Register accounts which used before asynchronously. <br>
     * POST /v2/registration/account <br>
     *
     * <pre>Example :
     * {@code
     *
     * FeeDelegatedAccountUpdate createAccountUpdateTx(Caver caver, SingleKeyring keyring, String newKey) throws IOException {
     *         com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyPublic(keyring.getAddress(), newKey);
     *
     *         FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
     *                 .setKlaytnCall(caver.rpc.klay)
     *                 .setFrom(keyring.getAddress())
     *                 .setGas(BigInteger.valueOf(250000))
     *                 .setAccount(account)
     *                 .build();
     *
     *         tx.sign(keyring);
     *
     *         return tx;
     * }
     *
     * ApiCallback<RegistrationStatusResponse> callback = new ApiCallback<RegistrationStatusResponse> callback() {
     *    ....implement callback methods.
     * };
     *
     * KeyCreationResponse response = caver.kas.wallet.createKeys(1);
     *
     * SingleKeyring keyring = KeyringFactory.generate();
     * com.klaytn.caver.account.Account account = com.klaytn.caver.account.Account.createWithAccountKeyLegacy(keyring.getAddress());
     *
     * AccountRegistration registration = new AccountRegistration();
     * registration.setAddress(account.getAddress());
     * registration.setKeyId(response.getItems().get(0).getKeyId());
     * registration.setRlp(createAccountUpdateTx(caver, keyring, response.getItems().get(0).getPublicKey()).getRLPEncoding());
     *
     * RegistrationStatusResponse result = caver.kas.wallet.registerAccountsAsync(Arrays.asList(request), callback);
     * }</pre>
     *
     * @param request The List of account information to be registered in KAS. <br>
     *                The rlp field of AccountRegistration should be set as an encoded FeeDelegatedAccountUpdate(without set a fee payer and fee payer signature) using the key and the address to be registered in KAS. <br>
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call registerAccountsAsync(List<AccountRegistration> request, ApiCallback<RegistrationStatusResponse> callback) throws ApiException {
        return getRegistrationApi().registerAccountAsync(chainId, request, callback);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction. <br>
     * POST /v2/tx/contract/call
     *
     * <pre>Example :
     * {@code
     * String kip7ContractAddress = "address";
     * ContractCallResponse response = caver.kas.wallet.callContract(kip7ContractAddress, "totalSupply");
     * }</pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @return ContractCallResponse
     * @throws ApiException
     */
    public ContractCallResponse callContract(String contractAddress, String methodName) throws ApiException {
        return callContract(contractAddress, methodName, null, new SendOptions());
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * String ftContractAddress = "0x{contractAddress}";
     * String baseAccount = "0x{accountAddress}";
     *
     * CallArgument argument = new CallArgument();
     * argument.setType("address");
     * argument.setValue(baseAccount);
     * ContractCallResponse response = caver.kas.wallet.callContract(ftContractAddress, "balanceOf", Collections.singletonList(argument));
     * }</pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param callArguments The argument required to call contract's function.
     * @return ContractCallResponse
     * @throws ApiException
     */
    public ContractCallResponse callContract(String contractAddress, String methodName, List<CallArgument> callArguments) throws ApiException {
        return callContract(contractAddress, methodName, callArguments, new SendOptions());
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * String kip7ContractAddress = "address";
     * String baseAccount = "0x{accountAddress}";
     *
     * SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));
     * ContractCallResponse response = caver.kas.wallet.callContract(kip7ContractAddress, "pause", sendOptions);
     * }</pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param sendOptions The sendOptions(from, gas, value) instance.
     * @return ContractCallResponse
     * @throws ApiException
     */
    public ContractCallResponse callContract(String contractAddress, String methodName, SendOptions sendOptions) throws ApiException {
        return callContract(contractAddress, methodName, null, sendOptions);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * String toAccount = "0x{toAccount}";
     *
     * CallArgument argument1 = new CallArgument();
     * argument1.setType("address");
     * argument1.setValue(toAccount);
     *
     * CallArgument argument2 = new CallArgument();
     * argument2.setType("uint256");
     * argument2.setValue(BigInteger.valueOf(1000000000));
     *
     * SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));
     *
     * ContractCallResponse response = caver.kas.wallet.callContract(contractAddress, "transfer", Arrays.asList(argument1, argument2),  sendOptions);
     * }</pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param callArguments The argument required to call contract's function.
     *                      `type` and `value` are defined. The ABI type can be `uint256`, `uint32`, `string`, `bool`, `address`, `uint64[2]` and `address[]`. The value can be `number`, `string`, `array` and `boolean`.
     * @param sendOptions The sendOptions(from, gas, value) instance.
     * @return ContractCallResponse
     * @throws ApiException
     */
    public ContractCallResponse callContract(String contractAddress, String methodName, List<CallArgument> callArguments, SendOptions sendOptions) throws ApiException {
        ContractCallRequest request = new ContractCallRequest();
        request.setTo(contractAddress);

        ContractCallData contractCallData = new ContractCallData();
        contractCallData.setMethodName(methodName);
        contractCallData.setArguments(callArguments);
        request.setData(contractCallData);

        if(sendOptions.getFrom() != null) {
            request.setFrom(sendOptions.getFrom());
        }

        if(sendOptions.getGas() != null) {
            request.setGas(Numeric.toBigInt(sendOptions.getGas()).longValue());
        }

        request.setValue(sendOptions.getValue());
        return getBasicTransactionApi().contractCall(chainId, request);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction asynchronously. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<ContractCallResponse> callback = new ApiCallback<ContractCallResponse> callback() {
     *   ....implement callback method.
     * };
     *
     * String kip7ContractAddress = "address";
     * caver.kas.wallet.callContractAsync(kip7ContractAddress, "totalSupply", callback);
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call callContractAsync(String contractAddress, String methodName, ApiCallback<ContractCallResponse> callback) throws ApiException {
        return callContractAsync(contractAddress, methodName, null, new SendOptions(), callback);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction asynchronously. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<ContractCallResponse> callback = new ApiCallback<ContractCallResponse> callback() {
     *   ....implement callback method.
     * };
     *
     * String ftContractAddress = "0x{contractAddress}";
     * String baseAccount = "0x{accountAddress}";
     *
     * CallArgument argument = new CallArgument();
     * argument.setType("address");
     * argument.setValue(baseAccount);
     * caver.kas.wallet.callContractAsync(ftContractAddress, "balanceOf", Collections.singletonList(argument), callback);
     *
     * }</pre>
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param callArguments The argument required to call contract's function.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call callContractAsync(String contractAddress, String methodName, List<CallArgument> callArguments, ApiCallback<ContractCallResponse> callback) throws ApiException {
        return callContractAsync(contractAddress, methodName, callArguments, new SendOptions(), callback);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction asynchronously. <br>
     * POST /v2/tx/contract/call <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<ContractCallResponse> callback = new ApiCallback<ContractCallResponse> callback() {
     *   ....implement callback method.
     * };
     *
     * String kip7ContractAddress = "address";
     * String baseAccount = "0x{accountAddress}";
     *
     * SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));
     * caver.kas.wallet.callContractAsync(kip7ContractAddress, "pause", sendOptions, callback);
     * }</pre>
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param sendOptions The sendOptions(from, gas, value) instance.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call callContractAsync(String contractAddress, String methodName, SendOptions sendOptions, ApiCallback<ContractCallResponse> callback) throws ApiException {
        return callContractAsync(contractAddress, methodName, null, sendOptions, callback);
    }

    /**
     * Call the contract. You can view certain value in the contract and validate that you can submit executable transaction asynchronously. <br>
     * POST /v2/tx/contract/call<br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<ContractCallResponse> callback = new ApiCallback<ContractCallResponse> callback() {
     *   ....implement callback method.
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String toAccount = "0x{toAccount}";
     *
     * CallArgument argument1 = new CallArgument();
     * argument1.setType("address");
     * argument1.setValue(toAccount);
     *
     * CallArgument argument2 = new CallArgument();
     * argument2.setType("uint256");
     * argument2.setValue(BigInteger.valueOf(1000000000));
     *
     * SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));
     * caver.kas.wallet.callContractAsync(contractAddress, "transfer", Arrays.asList(argument1, argument2),  sendOptions, callback);
     * }</pre>
     *
     * @param contractAddress The contract address.
     * @param methodName The contract function name.
     * @param callArguments The argument required to call contract's function.
     * @param sendOptions The sendOptions(from, gas, value) instance.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call callContractAsync(String contractAddress, String methodName, List<CallArgument> callArguments, SendOptions sendOptions, ApiCallback<ContractCallResponse> callback) throws ApiException {
        ContractCallRequest request = new ContractCallRequest();
        request.setTo(contractAddress);

        ContractCallData contractCallData = new ContractCallData();
        contractCallData.setMethodName(methodName);
        contractCallData.setArguments(callArguments);
        request.setData(contractCallData);

        if(sendOptions.getFrom() != null) {
            request.setFrom(sendOptions.getFrom());
        }

        if(sendOptions.getGas() != null) {
            request.setGas(Numeric.toBigInt(sendOptions.getGas()).longValue());
        }

        request.setValue(sendOptions.getValue());
        return getBasicTransactionApi().contractCallAsync(chainId, request, callback);
    }
    

    /**
     * Getter function for accountApi.
      * @return AccountApi
     */
    public AccountApi getAccountApi() {
        return accountApi;
    }

    /**
     * Getter function for basicTransactionApi
     * @return BasicTransactionApi
     */
    public BasicTransactionApi getBasicTransactionApi() {
        return basicTransactionApi;
    }

    /**
     * Getter function for feeDelegatedTransactionPaidByKasApi
     * @return FeeDelegatedTransactionPaidByKasApi
     */
    public FeeDelegatedTransactionPaidByKasApi getFeeDelegatedTransactionPaidByKasApi() {
        return feeDelegatedTransactionPaidByKasApi;
    }

    /**
     * Getter function for feeDelegatedTransactionPaidByUserApi
     * @return FeeDelegatedTransactionPaidByUserApi
     */
    public FeeDelegatedTransactionPaidByUserApi getFeeDelegatedTransactionPaidByUserApi() {
        return feeDelegatedTransactionPaidByUserApi;
    }

    /**
     * Getter function for multisigTransactionManagementApi
     * @return MultisigTransactionManagementApi
     */
    public MultisigTransactionManagementApi getMultisigTransactionManagementApi() {
        return multisigTransactionManagementApi;
    }

    /**
     * Getter function for statisticsApi
     * @return StatisticsApi
     */
    public StatisticsApi getStatisticsApi() {
        return statisticsApi;
    }

    /**
     * Getter function for KeyApi
     * @return KeyApi
     */
    public KeyApi getKeyApi() {
        return keyApi;
    }

    /**
     * Getter function for registrationApi
     * @return
     */
    public RegistrationApi getRegistrationApi() {
        return registrationApi;
    }

    /**
     * Getter function for chainId
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * Getter function for ApiClient
     * @return ApiClient
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Setter function for accountApi
     * @param accountApi Account API rest client object.
     */
    public void setAccountApi(AccountApi accountApi) {
        this.accountApi = accountApi;
    }

    /**
     * Setter function for basicTransactionApi
     * @param basicTransactionApi Basic transaction API rest client object.
     */
    public void setBasicTransactionApi(BasicTransactionApi basicTransactionApi) {
        this.basicTransactionApi = basicTransactionApi;
    }

    /**
     * Setter function for feeDelegatedTransactionPaidByKasApi
     * @param feeDelegatedTransactionPaidByKasApi Fee delegated transaction(fee paid by KAS) API rest client object.
     */
    public void setFeeDelegatedTransactionPaidByKasApi(FeeDelegatedTransactionPaidByKasApi feeDelegatedTransactionPaidByKasApi) {
        this.feeDelegatedTransactionPaidByKasApi = feeDelegatedTransactionPaidByKasApi;
    }

    /**
     * Setter function for feeDelegatedTransactionPaidByUserApi
     * @param feeDelegatedTransactionPaidByUserApi Fee delegated transaction(fee paid by user) API rest client object.
     */
    public void setFeeDelegatedTransactionPaidByUserApi(FeeDelegatedTransactionPaidByUserApi feeDelegatedTransactionPaidByUserApi) {
        this.feeDelegatedTransactionPaidByUserApi = feeDelegatedTransactionPaidByUserApi;
    }

    /**
     * Setter function for multisigTransactionManagementApi
     * @param multisigTransactionManagementApi Multiple signature transaction management API rest client object.
     */
    public void setMultisigTransactionManagementApi(MultisigTransactionManagementApi multisigTransactionManagementApi) {
        this.multisigTransactionManagementApi = multisigTransactionManagementApi;
    }

    /**
     * Setter function for statisticsApi
     * @param statisticsApi statistics API rest client object.
     */
    public void setStatisticsApi(StatisticsApi statisticsApi) {
        this.statisticsApi = statisticsApi;
    }

    /**
     * Setter function for keyApi
     * @param keyApi Key API rest client object.
     */
    public void setKeyApi(KeyApi keyApi) {
        this.keyApi = keyApi;
    }

    /**
     * Setter function for registrationApi
     * @param registrationApi account registration API rest client object.
     */
    public void setRegistrationApi(RegistrationApi registrationApi) {
        this.registrationApi = registrationApi;
    }

    /**
     * Setter function for chainId
     * @param chainId Klaytn network id.
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Setter function for ApiClient
     * @param apiClient The ApiClient for connecting with KAS.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setAccountApi(new AccountApi(apiClient));
        setBasicTransactionApi(new BasicTransactionApi(apiClient));
        setFeeDelegatedTransactionPaidByKasApi(new FeeDelegatedTransactionPaidByKasApi(apiClient));
        setFeeDelegatedTransactionPaidByUserApi(new FeeDelegatedTransactionPaidByUserApi(apiClient));
        setMultisigTransactionManagementApi(new MultisigTransactionManagementApi(apiClient));
        setStatisticsApi(new StatisticsApi(apiClient));
        setKeyApi(new KeyApi(apiClient));
        setRegistrationApi(new RegistrationApi(apiClient));
    }


    /**
     * Setter function for RPC
     * @param rpc The RPC for using Node API.
     */
    public void setRPC(RPC rpc) {
        this.rpc = rpc;
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

    AccountUpdateKey makeUncompressedKeyFormat(AccountUpdateKey updateKey) {
        if(updateKey instanceof EmptyUpdateKeyType) {
            return updateKey;
        }

        if(updateKey instanceof PubkeyUpdateKeyType) {
            ((KeyTypePublic)updateKey).setKey(KASUtils.addUncompressedKeyPrefix(((KeyTypePublic) updateKey).getKey()));
        } else if(updateKey instanceof MultisigUpdateKeyType) {
            MultisigUpdateKey keys = ((KeyTypeMultiSig) updateKey).getKey();
            keys.getWeightedKeys().stream().forEach(weightKey -> {
                weightKey.setPublicKey(KASUtils.addUncompressedKeyPrefix(weightKey.getPublicKey()));
            });
        } else if(updateKey instanceof RoleBasedUpdateKeyType) {
            List<AccountUpdateKey> roleKeyList = ((KeyTypeRoleBased) updateKey).getKey();
            roleKeyList.stream().forEach(roleKey -> makeUncompressedKeyFormat(roleKey));
        } else {
            throw new IllegalArgumentException("Not supported update Key type.");
        }

        return updateKey;
    }

    /**
     * Validate whether given migrationAccount is valid or not.
     * @param migrationAccount An account to be migrated to KAS Wallet
     * @return boolean
     */
    private boolean validateMigrationAccount(MigrationAccount migrationAccount) {
        if(migrationAccount.getAddress() == "") {
            System.out.println("ERROR: Address of migrationAccount must not be empty.");
            return false;
        }

        MigrationAccountKey<?> migrationAccountKey = migrationAccount.getMigrationAccountKey();
        if(migrationAccountKey == null) {
            System.out.println("ERROR: MigrationAccountKey of migrationAccount must not be empty.");
            return false;
        }

        String keyClassName = migrationAccountKey.getClass().getSimpleName();

        if(
                keyClassName.equals(SinglePrivateKey.class.getSimpleName()) == false
                        && keyClassName.equals(MultisigPrivateKeys.class.getSimpleName()) == false
                        && keyClassName.equals(RoleBasedPrivateKeys.class.getSimpleName()) == false
        ) {
            System.out.println(
                    "MigrationAccountKey of Migration Account must be one of following class " +
                    "[SinglePrivateKey, MultisigPrivateKeys, RoleBasedPrivateKeys]."
            );
            return false;
        }
        return true;
    }

    /**
     * Create an AbstractKeyring from a given migrationAccount
     * @param migrationAccount
     * @return AbstractKeyring
     * @throws IllegalArgumentException
     */
    private AbstractKeyring createKeyringFromMigrationAccount(MigrationAccount migrationAccount) throws IllegalArgumentException {
        MigrationAccountKey<?> migrationAccountKey = migrationAccount.getMigrationAccountKey();
        String keyClassName = migrationAccountKey.getClass().getSimpleName();

        if(SinglePrivateKey.class.getSimpleName().equals(keyClassName)) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((SinglePrivateKey) migrationAccountKey).getKey()
            );
        } else if(MultisigPrivateKeys.class.getSimpleName().equals(keyClassName)) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((MultisigPrivateKeys) migrationAccountKey).getKey()
            );
        } else if(RoleBasedPrivateKeys.class.getSimpleName().equals(keyClassName)) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((RoleBasedPrivateKeys) migrationAccountKey).getKey()
            );
        } else {
            throw new IllegalArgumentException("MigrationAccountKey must be one of the following class instance [SinglePrivateKey, MultisigPrivateKeys, RoleBasedPrivateKeys].");
        }
    }
}
