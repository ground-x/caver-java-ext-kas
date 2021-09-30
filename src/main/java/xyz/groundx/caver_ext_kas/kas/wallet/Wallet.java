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
import java.util.Optional;
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
     * Fee payer API rest client object.
     */
    FeepayerApi feepayerApi;

    /**
     * Transaction history API rest client object.
     */
    TransactionHistoryApi transactionHistoryApi;

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
     * The nodeAPIInitialized exists for checking if NodeApi is initialized or not.
     */
    private boolean nodeAPIInitialized;

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
     * accountsToBeMigrated.add(new MigrationAccount("0x{address}", "0x{privateKeyOfAccountToBeMigrated}"));
     * accountsToBeMigrated.add(new MigrationAccount("0x{address}", "0x{privateKeyOfAccountToBeMigrated}", "0x{nonce}"));
     *
     * RegistrationStatusResponse response = caver.kas.wallet.migrateAccounts(accountsToBeMigrated);
     * }</pre>
     *
     * @param accountsToBeMigrated A list of accounts to be migrated to KAS Wallet Service.
     * @return RegistrationStatusResponse
     * @throws ApiException
     * @throws IOException
     */
    public RegistrationStatusResponse migrateAccounts(List<MigrationAccount> accountsToBeMigrated) throws ApiException, IOException {
        if (!nodeAPIInitialized) {
            throw new RuntimeException("You should initialize Node API with working url(e.g. https://node-api.klaytnapi.com/v1/klaytn) first.");
        }

        // Need to validate whether given list of migration accounts is valid or not.
        for (int i=0; i<accountsToBeMigrated.size(); i++) {
            validateMigrationAccount(accountsToBeMigrated.get(i));
        }

        AccountRegistrationRequest request = new AccountRegistrationRequest();

        KeyCreationResponse keyCreationResponse = this.createKeys(accountsToBeMigrated.size());
        List<Key> createdKeys = keyCreationResponse.getItems();

        String gasPrice = this.rpc.klay.getGasPrice().send().getResult();
        for (int i = 0; i < createdKeys.size(); i++) {
            MigrationAccount accountToBeMigrated = accountsToBeMigrated.get(i);
            Key newKey = createdKeys.get(i);

            FeeDelegatedAccountUpdate tx = new FeeDelegatedAccountUpdate.Builder()
                    .setChainId(BigInteger.valueOf(Integer.parseInt(chainId)))
                    .setFrom(accountToBeMigrated.getAddress())
                    .setNonce(accountToBeMigrated.getNonce())
                    .setAccount(
                            com.klaytn.caver.account.Account.createWithAccountKeyPublic(
                                    accountToBeMigrated.getAddress(),
                                    newKey.getPublicKey()
                            )
                    )
                    .setGas(BigInteger.valueOf(1000000))
                    .setGasPrice(gasPrice)
                    .setKlaytnCall(this.rpc.getKlay())
                    .build();

            AbstractKeyring keyring = createKeyringFromMigrationAccount(accountToBeMigrated);
            tx.sign(keyring);

            AccountRegistration accountRegistration = new AccountRegistration();
            accountRegistration.setKeyId(newKey.getKeyId());
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
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.createAccount();
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * ApiCallback<Account> callback = new ApiCallback<Account>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.createAccountAsync(callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * Accounts accounts = caver.kas.wallet.getAccountList();
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * ApiCallback<Accounts> callback = new ApiCallback<Accounts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountListAsync(callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(5l);
     * options.setFromTimestamp("2021-01-01 00:00:00");
     * options.setToTimestamp(new Date().getTime() / 1000);
     *
     * Accounts accounts = caver.kas.wallet.getAccountList(options);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(5l);
     * options.setFromTimestamp("2021-01-01 00:00:00");
     * options.setToTimestamp(new Date().getTime() / 1000);
     *
     * ApiCallback<Accounts> callback = new ApiCallback<Accounts>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountListAsync(options, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * Account account = caver.kas.wallet.getAccount(address);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     *
     * ApiCallback<Account> callback = new ApiCallback<Account>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountAsync(address, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * AccountStatus accountStatus = caver.kas.wallet.deleteAccount(address);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     *
     * ApiCallback<AccountStatus> callback = new ApiCallback<AccountStatus>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.deleteAccountAsync(address, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * AccountSummary status = caver.kas.wallet.disableAccount(address);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     *
     * ApiCallback<AccountSummary> callback = new ApiCallback<AccountSummary>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.disableAccountAsync(address, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * AccountSummary status = caver.kas.wallet.enableAccount(address);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * ApiCallback<AccountSummary> callback = new ApiCallback<AccountSummary>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.enableAccountAsync(address, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String transactionId = "0x{txId}";
     *
     * Signature signature = caver.kas.wallet.signTransaction(address, transactionId);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String transactionId = "0x{txId}";
     *
     * ApiCallback<Signature> callback = new ApiCallback<Signature>() {
     *     ....implements callback method
     * };
     * caver.kas.wallet.signTransactionAsync(address, transactionId, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * Account account1 = caver.kas.wallet.createAccount();
     * Account account2 = caver.kas.wallet.createAccount();
     * Account account3 = caver.kas.wallet.createAccount();
     * List<Account> accountList = Arrays.asList(account1, account2, account3);
     * List<MultisigKey> multisigKeys = accountList.stream().map(account -> {
     *         MultisigKey multisigKey = new MultisigKey();
     *         multisigKey.setWeight((long)2);
     *         multisigKey.setPublicKey(account.getPublicKey());
     *
     *         return multisigKey;
     * }).collect(Collectors.toList());
     *
     * MultisigAccountUpdateRequest request = new MultisigAccountUpdateRequest();
     * request.setThreshold((long)3);
     * request.setWeightedKeys(multiSigKeys);
     *
     * MultisigAccount account = caver.kas.wallet.updateToMultiSigAccount(address, request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * Account account1 = caver.kas.wallet.createAccount();
     * Account account2 = caver.kas.wallet.createAccount();
     * Account account3 = caver.kas.wallet.createAccount();
     * List<Account> accountList = Arrays.asList(account1, account2, account3);
     * List<MultisigKey> multisigKeys = accountList.stream().map(account -> {
     *         MultisigKey multisigKey = new MultisigKey();
     *         multisigKey.setWeight((long)2);
     *         multisigKey.setPublicKey(account.getPublicKey());
     *
     *         return multisigKey;
     * }).collect(Collectors.toList());
     *
     * MultisigAccountUpdateRequest request = new MultisigAccountUpdateRequest();
     * request.setThreshold((long)3);
     * request.setWeightedKeys(multiSigKeys);
     *
     * ApiCallback<MultisigAccount> callback = new ApiCallback<MultisigAccount>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.updateToMultiSigAccountAsync(address, request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String publicKey = "0x{publicKey}";
     * AccountsByPubkey accounts = caver.kas.wallet.getAccountListByPublicKey(publicKey);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String publicKey = "0x{publicKey}";
     *
     * ApiCallback<AccountsByPubkey> callback = new ApiCallback<AccountsByPubkey>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountListByPublicKeyAsync(publicKey, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * LegacyTransactionRequest request = new LegacyTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * TransactionResult result = caver.kas.wallet.requestLegacyTransaction(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * LegacyTransactionRequest request = new LegacyTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestLegacyTransactionAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * TransactionResult transactionResult = caver.kas.wallet.requestValueTransfer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * ValueTransferTransactionRequest request = new ValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestValueTransferAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{fromAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(5500000L);
     * request.submit(true);
     *
     * TransactionResult transactionResult = caver.kas.wallet.requestSmartContractDeploy(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{fromAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * ContractDeployTransactionRequest request = new ContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(5500000L);
     * request.submit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestSmartContractDeployAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{deployerAddress}";
     * String contractAddress = "0x{contractAddress}";
     * String contractAbi = "{abi}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = contract.encodeABI("method", param1, param2, ...);
     *
     * ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setTo(contractAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(5500000L);
     * request.submit(true);
     *
     * TransactionResult result = caver.kas.wallet.requestSmartContractExecution(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * ContractExecutionTransactionRequest request = new ContractExecutionTransactionRequest();
     * String deployerAddress = "0x{deployerAddress}";
     * String contractAddress = "0x{contractAddress}";
     * String contractAbi = "{abi}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = contract.encodeABI("method", param1, param2, ...);
     *
     * request.setFrom(deployerAddress);
     * request.setTo(contractAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestSmartContractExecutionAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * CancelTransactionRequest request = new CancelTransactionRequest();
     * request.setFrom(address);
     * request.setNonce(1l);
     *
     * TransactionResult result = caver.kas.wallet.requestCancel(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * CancelTransactionRequest request = new CancelTransactionRequest();
     * request.setFrom(address);
     * request.setNonce(1l);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestCancelAsync(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * AnchorTransactionRequest request = new AnchorTransactionRequest();
     * request.setFrom(address);
     * request.setInput("0x0111");
     * request.setSubmit(true);
     *
     * TransactionResult result = caver.kas.wallet.requestChainDataAnchoring(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * AnchorTransactionRequest request = new AnchorTransactionRequest();
     * request.setFrom(address);
     * request.setInput("0x0111");
     * request.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestChainDataAnchoringAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String rlp = "0x{rlp}";
     *
     * ProcessRLPRequest requestRLP = new ProcessRLPRequest();
     * requestRLP.setRlp(rlp);
     * requestRLP.setSubmit(true);
     *
     * TransactionResult transactionResult = caver.kas.wallet.requestRawTransaction(requestRLP);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String rlp = "0x{rlp}";
     *
     * ProcessRLPRequest requestRLP = new ProcessRLPRequest();
     * requestRLP.setRlp(rlp);
     * requestRLP.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestRawTransactionAsync(requestRLP, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.getAccountList().getItems().get(0).getAddress();
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setSubmit(true);
     *
     * TransactionResult result = caver.kas.wallet.requestAccountUpdate(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.getAccountList().getItems().get(0).getAddress();
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * AccountUpdateTransactionRequest request = new AccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setSubmit(true);
     *
     * ApiCallback<TransactionResult> callback = new ApiCallback<TransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestAccountUpdateAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String txHash = "0x{txHash}";
     * TransactionReceipt receipt = caver.kas.wallet.getTransaction(txHash);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String txHash = "0x{txHash}";
     *
     * ApiCallback<TransactionReceipt> callback = new ApiCallback<TransactionReceipt>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getTransactionAsync(txHash);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     *
     * FDValueTransferTransactionRequest request = new FDValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDValueTransferPaidByGlobalFeePayerAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{deployerAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(1500000L);
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDSmartContractDeployPaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{deployerAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * FDContractDeployTransactionRequest request = new FDContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(1500000L);
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDSmartContractDeployPaidByGlobalFeePayerAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String contractAddress = "0x{contractAddress}";
     * String contractAbi = "{abi}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = contract.encodeABI("method", param1, param2, ...);
     *
     * FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(contractAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDSmartContractExecutionPaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String contractAddress = "0x{contractAddress}";
     * String contractAbi = "{abi}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = contract.encodeABI("method", param1, param2, ...);
     *
     *
     * FDContractExecutionTransactionRequest request = new FDContractExecutionTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(contractAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDSmartContractExecutionPaidByGlobalFeePayerAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * FDCancelTransactionRequest request = new FDCancelTransactionRequest();
     * request.setFrom(address);
     * request.setNonce(1l);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDCancelPaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * FDCancelTransactionRequest request = new FDCancelTransactionRequest();
     * request.setFrom(address);
     * request.setNonce(1l);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDCancelPaidByGlobalFeePayerAsync(request, callBack);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
     * request.setFrom(address);
     * request.setInput("0x1111");
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDChainDataAnchoringPaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     *
     * FDAnchorTransactionRequest request = new FDAnchorTransactionRequest();
     * request.setFrom(address);
     * request.setInput("0x1111");
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDChainDataAnchoringPaidByGlobalFeePayerAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String feeDelegatedRlp = "0x{feeDelegatedRlp}";
     *
     * FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
     * requestRLP.setRlp(feeDelegatedRlp);
     * requestRLP.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDRawTransactionPaidByGlobalFeePayer(requestRLP);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String feeDelegatedRlp = "0x{feeDelegatedRlp}";
     *
     * FDProcessRLPRequest requestRLP = new FDProcessRLPRequest();
     * requestRLP.setRlp(feeDelegatedRlp);
     * requestRLP.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDRawTransactionPaidByGlobalFeePayerAsync(requestRLP, callback);
     * }
     * </pre>
     *
     * @param request The FDProcessRLPRequest instance to send a transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call requestFDRawTransactionPaidByGlobalFeePayerAsync(FDProcessRLPRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByKasApi().fDProcessRLPAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedAccountUpdate(WithRatio) transaction.<br>
     * KAS pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * PUT /v2/tx/fd/account
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.createAccount();
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayer(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.createAccount();
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * FDAccountUpdateTransactionRequest request = new FDAccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDAccountUpdatePaidByGlobalFeePayerAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDValueTransferPaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String toAddress = "0x{toAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserValueTransferTransactionRequest request = new FDUserValueTransferTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(toAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setValue("0x1");
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDValueTransferPaidByUserAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{deployerAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(1500000L);
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDSmartContractDeployPaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String deployerAddress = "0x{deployerAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     * String bytecode = "{bytecode}";
     *
     * Contract contract = caver.contract.create(contractAbi);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("constructor", bytecode, param1, param2, ...);
     *
     * FDUserContractDeployTransactionRequest request = new FDUserContractDeployTransactionRequest();
     * request.setFrom(deployerAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setGas(1500000L);
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDSmartContractDeployPaidByUserAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     * String contractAddress = "0x{contractAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     *
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("method", param1, param2, ...);
     *
     * FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(contractAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDSmartContractExecutionPaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String fromAddress = "0x{fromAddress}";
     * String feePayerAddress = "0x{feePayerAddress}";
     * String contractAddress = "0x{contractAddress}";
     *
     * // You can get abi and bytecode of smart contract by compiling the contract.
     * String contractAbi = "{abi}";
     *
     * Contract contract = caver.contract.create(contractAbi, contractAddress);
     *
     * // The number of params is based on your contract abi and bytecode...
     * String input = String input = contract.encodeABI("method", param1, param2, ...);
     *
     * FDUserContractExecutionTransactionRequest request = new FDUserContractExecutionTransactionRequest();
     * request.setFrom(fromAddress);
     * request.setTo(contractAddress);
     * request.setFeePayer(feePayerAddress);
     * request.setInput(Utils.addHexPrefix(input));
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDSmartContractExecutionPaidByUserAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
     * request.setFrom(address);
     * request.setFeePayer(feePayerAddress);
     * request.setNonce((long)1);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDCancelPaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserCancelTransactionRequest request = new FDUserCancelTransactionRequest();
     * request.setFrom(address);
     * request.setFeePayer(feePayerAddress);
     * request.setNonce((long)1);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDCancelPaidByUserAsync(request, callback);
     * }
     * </pre>
     *
     * @param request The FDUserCancelTransactionRequest instance to send a request.
     * @param callback The callback function to handle response
     * @return Call
     * @throws ApiException
     */
    public Call requestFDCancelPaidByUserAsync(FDUserCancelTransactionRequest request, ApiCallback<FDTransactionResult> callback) throws ApiException {
        return getFeeDelegatedTransactionPaidByUserApi().uFDUserCancelTransactionAsync(chainId, request, callback);
    }

    /**
     * Send a FeeDelegatedChainDataAnchoring(WithRatio) transaction.<br>
     * The feePayer defined by the user pays the fee for this transaction.<br>
     * If you want to send withRatio Transaction, you can set feeRatio field in request.<br>
     * POST /v2/tx/fd-user/anchor
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
     * request.setFrom(address);
     * request.setFeePayer(feePayerAddress);
     * request.setInput("0x123456");
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDChainDataAnchoringPaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String address = "0x{address}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserAnchorTransactionRequest request = new FDUserAnchorTransactionRequest();
     * request.setFrom(address);
     * request.setFeePayer(feePayerAddress);
     * request.setInput("0x123456");
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDChainDataAnchoringPaidByUserAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String = feeDelegatedRlp = "0x{feeDelegatedRlp}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
     * processRLPRequest.setRlp(feeDelegatedRlp);
     * processRLPRequest.setFeePayer(feePayerAddress);
     * processRLPRequest.setSubmit(true);
     * FDTransactionResult resultRLP = caver.kas.wallet.requestFDRawTransactionPaidByUser(processRLPRequest);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String = feeDelegatedRlp = "0x{feeDelegatedRlp}";
     * String feePayerAddress = "0x{feePayerAddress}";
     *
     * FDUserProcessRLPRequest processRLPRequest = new FDUserProcessRLPRequest();
     * processRLPRequest.setRlp(feeDelegatedRlp);
     * processRLPRequest.setFeePayer(feePayerAddress);
     * processRLPRequest.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDRawTransactionPaidByUserAsync(processRLPRequest, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String feePayerAddress = "0x{feePayerAddress}";
     * Account account = caver.kas.wallet.createAccount();
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setFeePayer(feePayerAddress);
     * request.setSubmit(true);
     *
     * FDTransactionResult result = caver.kas.wallet.requestFDAccountUpdatePaidByUser(request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * Account account = caver.kas.wallet.createAccount();
     * String feePayerAddress = "0x{feePayerAddress}";
     * KeyTypePublic updateKeyType = new KeyTypePublic(account.getPublicKey());
     *
     * FDUserAccountUpdateTransactionRequest request = new FDUserAccountUpdateTransactionRequest();
     * request.setFrom(account.getAddress());
     * request.setAccountKey(updateKeyType);
     * request.setFeePayer(feePayerAddress);
     * request.setSubmit(true);
     *
     * ApiCallback<FDTransactionResult> callback = new ApiCallback<FDTransactionResult>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.requestFDAccountUpdatePaidByUserAsync(request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     * MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     *
     * ApiCallback<MultisigTransactions> callback = new ApiCallback<MultisigTransactions>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getMultiSigTransactionListAsync(multiSigAddress, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     *
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(3l);
     *
     * MultisigTransactions transactions = caver.kas.wallet.getMultiSigTransactionList(multiSigAddress, options);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     *
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(3l);
     *
     * ApiCallback<MultisigTransactions> callback = new ApiCallback<MultisigTransactions>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getMultiSigTransactionListAsync(multiSigAddress, options, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     * String transactionId = "0x{transactionId}";
     *
     * MultisigTransactionStatus status = caver.kas.wallet.signMultiSigTransaction(multiSigAddress, transactionId);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String multiSigAddress = "0x{multiSigAddress}";
     *
     * ApiCallback<MultisigTransactionStatus> callback = new ApiCallback<MultisigTransactionStatus>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.signMultiSigTransactionAsync(multiSigAddress, transactionId, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String signerAddress = "0x{signerAddress}";
     * String transactionId = "0x{transactionId}";
     *
     * Signature signature = caver.kas.wallet.signTransaction(singerAddress, transactionId);
     *
     * SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
     * request.setSignatures(Arrays.asList(signature));
     *
     * MultisigTransactionStatus transactionStatus = caver.kas.wallet.appendSignatures(transactionId, request);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * String signerAddress = "0x{signerAddress}";
     * String transactionId = "0x{transactionId}";
     *
     * Signature signature = caver.kas.wallet.signTransaction(singerAddress, transactionId);
     *
     * SignPendingTransactionBySigRequest request = new SignPendingTransactionBySigRequest();
     * request.setSignatures(Arrays.asList(signature));
     *
     * ApiCallback<MultisigTransactionStatus> callback = new ApiCallback<MultisigTransactionStatus>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.appendSignaturesAsync(transactionId, request, callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * AccountCountByAccountID countByAccountID = caver.kas.wallet.getAccountCount();
     * }
     * </pre>
     *
     * @return AccountCountByAccountID
     * @throws ApiException
     */
    public AccountCountByAccountID getAccountCount() throws ApiException {
        return getStatisticsApi().getAccountCountByAccountID(getChainId());
    }

    /**
     * Return the number of accounts in KAS asynchronously.<br>
     * GET /v2/stat/count
     *
     * <pre>Example:
     * {@code
     *
     * ApiCallback<AccountCountByAccountID> callback = new ApiCallback<AccountCountByAccountID>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountCountAsync(callback);
     * }
     * </pre>
     *
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
     *
     * <pre>Example:
     * {@code
     * AccountCountByKRN accountCountByKRN = caver.kas.wallet.getAccountCountByKRN();
     * }
     * </pre>
     *
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public AccountCountByKRN getAccountCountByKRN() throws ApiException {
        return getStatisticsApi().getAccountCountByKRN(getChainId());
    }

    /**
     * Return the number of accounts by passed as KRN in KAS asynchronously.<br>
     * It use default krn.<br>
     * GET /v2/stat/count/krn
     *
     * <pre>Example:
     * {@code
     *
     * ApiCallback<AccountCountByKRN> callback = new ApiCallback<AccountCountByKRN>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.wallet.getAccountCountByKRNAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return AccountCountByKRN
     * @throws ApiException
     */
    public Call getAccountCountByKRNAsync(ApiCallback<AccountCountByKRN> callback) throws ApiException {
        return getStatisticsApi().getAccountCountByKRNAsync(getChainId(), callback);
    }

//    /**
//     * Return the number of accounts by passed as KRN in KAS.<br>
//     * GET /v2/stat/count/krn
//     *
//     * <pre>Example:
//     * {@code
//     * String krn = "{krn}";
//     * AccountCountByKRN accountCountByKRN = caver.kas.wallet.getAccountCountByKRN(krn);
//     * }
//     * </pre>
//     *
//     * @param krn The krn string to search
//     * @return AccountCountByKRN
//     * @throws ApiException
//     */
//    public AccountCountByKRN getAccountCountByKRN(String krn) throws ApiException {
//        return getStatisticsApi().getAccountCountByKRN(getChainId(), krn);
//    }
//
//    /**
//     * Return the number of accounts by passed as KRN in KAS asynchronously.<br>
//     * GET /v2/stat/count/krn
//     *
//     * <pre>Example:
//     * {@code
//     * String krn = "{krn}";
//     *
//     * ApiCallback<AccountCountByKRN> callback = new ApiCallback<AccountCountByKRN>() {
//     *     ....implements callback method
//     * };
//     *
//     * caver.kas.wallet.getAccountCountByKRNAsync(krn, callback);
//     * }
//     * </pre>
//     *
//     * @param krn The krn string to search
//     * @param callback The callback function to handle response.
//     * @return AccountCountByKRN
//     * @throws ApiException
//     */
//    public Call getAccountCountByKRNAsync(String krn, ApiCallback<AccountCountByKRN> callback) throws ApiException {
//        return getStatisticsApi().getAccountCountByKRNAsync(getChainId(), krn, callback);
//    }

    /**
     * Create keys in KAS. <br>
     * POST /v2/key
     *
     * <pre>Example :
     * {@code
     * KeyCreationResponse response = caver.kas.wallet.createKeys(2);
     * }
     * </pre>
     *
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
     * POST /v2/key
     *
     * <pre>Example :
     * {@code
     * ApiCallback<KeyCreationResponse> callback = new ApiCallback<KeyCreationResponse> callback() {
     *    ....implements callback method
     * };
     * caver.kas.wallet.createKeysAsync(2, callback);
     * }
     * </pre>
     *
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
     * GET /v2/key/{key-id}
     *
     * <pre>Example :
     * {@code
     * String keyId = "keyId";
     * Key key = caver.kas.wallet.getKey(keyId);
     * }
     * </pre>
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
     * GET /v2/key/{key-id}
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
     * }
     * </pre>
     *
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
     * POST /v2/key/{key-id}/sign
     *
     * <pre>Example :
     * {@code
     * String keyId = "key Id";
     * String data = "data";
     *
     * caver.kas.wallet.signMessage(keyId, data);
     * }
     * </pre>
     *
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @return KeySignDataResponse
     * @throws ApiException
     */
    public KeySignDataResponse signMessage(String keyId, String data) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return getKeyApi().keySignData(chainId, keyId, request);
    }

    /**
     * Sign a message using a key create by KAS asynchronously. <br>
     * The default KRN will be used. <br>
     * POST /v2/key/{key-id}/sign
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
     * }
     * </pre>
     *
     * @param keyId The key id to use for signing.
     * @param data The data to sign.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call signMessageAsync(String keyId, String data, ApiCallback<KeySignDataResponse> callback) throws ApiException {
        KeySignDataRequest request = new KeySignDataRequest();
        request.setData(data);

        return getKeyApi().keySignDataAsync(chainId, keyId, request, callback);
    }

    /**
     * Register accounts which used before. <br>
     * POST /v2/registration/account
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
     * }
     * </pre>
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
     * POST /v2/registration/account
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
     * }
     * </pre>
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
     * POST /v2/registration/account
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
     * }
     * </pre>
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
     * POST /v2/registration/account
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
     * }
     * </pre>
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
     * }
     * </pre>
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
     * POST /v2/tx/contract/call
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
     * }
     * </pre>
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
     * POST /v2/tx/contract/call
     *
     * <pre>Example :
     * {@code
     * String kip7ContractAddress = "address";
     * String baseAccount = "0x{accountAddress}";
     *
     * SendOptions sendOptions = new SendOptions(baseAccount, BigInteger.valueOf(200000));
     * ContractCallResponse response = caver.kas.wallet.callContract(kip7ContractAddress, "pause", sendOptions);
     * }
     * </pre>
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
     * POST /v2/tx/contract/call
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
     * }
     * </pre>
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
     * POST /v2/tx/contract/call
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
     * POST /v2/tx/contract/call
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
     * }
     * </pre>
     *
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
     * POST /v2/tx/contract/call
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
     * }
     * </pre>
     *
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
     * POST /v2/tx/contract/call
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
     * }
     * </pre>
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
     * Create a Klaytn fee payer account.<br>
     * Generate a Klaytn account address and random private/public key pair and get ID of public key and private key returned.<br>
     * Klaytn fee payer account should be updated to AccountKeyRoleBased and can only be used for fee delegation.<br>
     * It sets the `withoutAccountUpdate` field as false.<br>
     * It means the feePayer account will be updated to Role-based account key type with all roles other than RoleFeePayer will be set to AccountKeyFail type.<br>
     * POST /v2/feepayer
     *
     * <pre>Example
     * {@code
     * Account feePayerAccount = caver.kas.wallet.createFeePayer();
     * }
     * </pre>
     *
     * @return Account
     * @throws ApiException
     */
    public Account createFeePayer() throws ApiException {
        return createFeePayer(false);
    }

    /**
     * Create a Klaytn fee payer account.<br>
     * Generate a Klaytn account address and random private/public key pair and get ID of public key and private key returned.<br>
     * Klaytn fee payer account should be updated to AccountKeyRoleBased and can only be used for fee delegation.<br>
     * POST /v2/feepayer
     *
     * <pre>Example
     * {@code
     * Account feePayerAccount = caver.kas.wallet.createFeePayer(true);
     * }
     * </pre>
     * @param withoutAccountUpdate Whether the feePayer account update to Role-based account key type. If false, it updated account to Role-based account with all roles other than fee payer roles will be set to AccountKeyFail type.
     * @return Account
     * @throws ApiException
     */
    public Account createFeePayer(boolean withoutAccountUpdate) throws ApiException {
        V2FeepayerBody body = new V2FeepayerBody();
        body.setWithoutAccountUpdate(withoutAccountUpdate);
        return getFeepayerApi().creatFeePayerAccount(chainId, body);
    }

    /**
     * Create a Klaytn fee payer account asynchronously.<br>
     * Generate a Klaytn account address and random private/public key pair and get ID of public key and private key returned.<br>
     * Klaytn fee payer account should be updated to AccountKeyRoleBased and can only be used for fee delegation.<br>
     * It sets a withoutAccountUpdate field to false.<br>
     * It means the feePayer account will be updated to Role-based account key type with all roles other than RoleFeePayer will be set to AccountKeyFail type.<br>
     * POST /v2/feepayer
     *
     * <pre>Example
     * {@code
     * ApiCallback<Account> callback = new ApiCallback<Account> callback() {
     *   ....implement callback method.
     * };
     *
     * caver.kas.wallet.createFeePayerAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call createFeePayerAsync(ApiCallback<Account> callback) throws ApiException {
        return createFeePayerAsync(false, callback);
    }

    /**
     * Create a Klaytn fee payer account asynchronously.<br>
     * Generate a Klaytn account address and random private/public key pair and get ID of public key and private key returned.<br>
     * Klaytn fee payer account should be updated to AccountKeyRoleBased and can only be used for fee delegation.<br>
     * POST /v2/feepayer
     *
     * <pre>Example
     * {@code
     * ApiCallback<Account> callback = new ApiCallback<Account> callback() {
     *   ....implement callback method.
     * };
     *
     * caver.kas.wallet.createFeePayerAsync(true, callback);
     * }
     * </pre>
     *
     * @param withoutAccountUpdate Whether the feePayer account update to Role-based account key type. If false, it updated account to Role-based account with all roles other than fee payer roles will be set to AccountKeyFail type.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call createFeePayerAsync(boolean withoutAccountUpdate, ApiCallback<Account> callback) throws ApiException {
        V2FeepayerBody body = new V2FeepayerBody();
        body.setWithoutAccountUpdate(withoutAccountUpdate);
        return getFeepayerApi().creatFeePayerAccountAsync(chainId, body, callback);
    }

    /**
     * Delete a certain Klaytn fee payer account.<br>
     * DELETE /v2/feepayer/{address}
     *
     * <pre>Example
     * {@code
     * String feePayerAddress = "0x{feePayer address}";
     * AccountStatus status = caver.wallet.kas.deleteFeePayer(feePayerAddress);
     * }
     * </pre>
     *
     * @param address Klaytn account address.
     * @return AccountStatus
     * @throws ApiException
     */
    public AccountStatus deleteFeePayer(String address) throws ApiException {
        return getFeepayerApi().deleteFeePayerAccount(chainId, address);
    }

    /**
     * Delete a certain Klaytn fee payer account asynchronously.<br>
     * DELETE /v2/feepayer/{address}
     *
     * <pre> Example
     * {@code
     * ApiCallback<AccountStatus> callback = new ApiCallback<AccountStatus> callback() {
     *   ....implement callback method.
     * };
     *
     * String feePayerAddress = "0x{feePayer address}";
     * caver.wallet.kas.deleteFeePayerAsync(feePayerAddress, callback);
     * }
     * </pre>
     *
     * @param address Klaytn account address.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deleteFeePayerAsync(String address, ApiCallback<AccountStatus> callback) throws ApiException {
        return getFeepayerApi().deleteFeePayerAccountAsync(chainId, address, callback);
    }

    /**
     * Retrieve a certain Klaytn fee payer account.<br>
     * GET /v2/feepayer/{address}
     *
     * <pre>Example
     * {@code
     * String address = "0x{address}";
     * Account account = caver.kas.wallet.getFeePayer(address);
     * }
     * </pre>
     *
     * @param address Klaytn account address.
     * @return Account
     * @throws ApiException
     */
    public Account getFeePayer(String address) throws ApiException {
        return getFeepayerApi().retrieveFeePayerAccount(chainId, address);
    }

    /**
     * Retrieve a certain Klaytn fee payer account asynchronously.<br>
     * GET /v2/feepayer/{address}
     *
     * <pre>Example
     * {@code
     * ApiCallback<Account> callback = new ApiCallback<Account> callback() {
     *   ....implement callback method.
     * };
     *
     * String address = "0x{address}";
     * caver.kas.wallet.getFeePayerAsync(address, callback);
     * }
     * </pre>
     *
     * @param address Klaytn account address.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFeePayerAsync(String address, ApiCallback<Account> callback) throws ApiException {
        return getFeepayerApi().retrieveFeePayerAccountAsync(chainId, address, callback);
    }

    /**
     * Retrieve a list of all Klaytn fee payer accounts.<br>
     * GET /v2/feepayer
     *
     * <pre>Example
     * {@code
     * Accounts accounts = caver.kas.wallet.getFeePayerList();
     * }
     * </pre>
     *
     * @return Accounts
     * @throws ApiException
     */
    public Accounts getFeePayerList() throws ApiException {
        return getFeePayerList(new WalletQueryOptions());
    }

    /**
     * Retrieve a list of all Klaytn fee payer accounts.<br>
     * GET /v2/feepayer
     *
     * <pre>Example
     * {@code
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(5l);
     * options.setFromTimestamp("2021-01-01 00:00:00");
     * options.setToTimestamp(new Date().getTime() / 1000);
     *
     * Accounts accounts = caver.kas.wallet.getFeePayerList(options);
     * }
     * </pre>
     *
     * @param options ilters required when retrieving data. `to-timestamp`, `from-timestamp`, `size`, and `cursor`.
     * @return Accounts
     * @throws ApiException
     */
    public Accounts getFeePayerList(WalletQueryOptions options) throws ApiException {
        String fromTimeStamp = Optional.ofNullable(options.getFromTimestamp()).map(value -> Long.toString(value)).orElseGet(()-> null);
        String toTimeStamp = Optional.ofNullable(options.getToTimestamp()).map(value -> Long.toString(value)).orElseGet(()-> null);

        return getFeepayerApi().retrieveFeePayerAccounts(chainId, options.getSize(), options.getCursor(), fromTimeStamp, toTimeStamp);
    }

    /**
     * Retrieve a list of all Klaytn fee payer accounts asynchronously.<br>
     * GET /v2/feepayer
     *
     * <pre>Example
     * {@code
     * ApiCallback<Accounts> callback = new ApiCallback<Accounts> callback() {
     *   ....implement callback method.
     * };
     *
     * caver.kas.wallet.getFeePayerListAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFeePayerListAsync(ApiCallback<Accounts> callback) throws ApiException {
        return getFeePayerListAsync(new WalletQueryOptions(), callback);
    }

    /**
     * Retrieve a list of all Klaytn fee payer accounts asynchronously.<br>
     * GET /v2/feepayer
     *
     * <pre>Example
     * {@code
     * ApiCallback<Accounts> callback = new ApiCallback<Accounts> callback() {
     *   ....implement callback method.
     * };
     *
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(5l);
     * options.setFromTimestamp("2021-01-01 00:00:00");
     * options.setToTimestamp(new Date().getTime() / 1000);
     *
     * caver.kas.wallet.getFeePayerListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options
     * @param callback
     * @return
     * @throws ApiException
     */
    public Call getFeePayerListAsync(WalletQueryOptions options, ApiCallback<Accounts> callback) throws ApiException {
        String fromTimeStamp = Optional.ofNullable(options.getFromTimestamp()).map(value -> Long.toString(value)).orElseGet(()-> null);
        String toTimeStamp = Optional.ofNullable(options.getToTimestamp()).map(value -> Long.toString(value)).orElseGet(()-> null);

        return getFeepayerApi().retrieveFeePayerAccountsAsync(chainId, options.getSize(), options.getCursor(), fromTimeStamp, toTimeStamp, callback);
    }

    /**
     * Delete a key.<br>
     * DELETE /v2/key/{key-id}
     *
     * <pre>Example
     * {@code
     * String keyId = "{key id}";
     * KeyStatus status = caver.kas.wallet.deleteKey(keyId);
     * }
     * </pre>
     *
     * @param keyId The key ID
     * @return KeyStatus
     * @throws ApiException
     */
    public KeyStatus deleteKey(String keyId) throws ApiException {
        return getKeyApi().keyDeletion(chainId, keyId);
    }

    /**
     * Delete a key asynchronously.<br>
     * DELETE /v2/key/{key-id}
     *
     * <pre>Example
     * {@code
     * ApiCallback<KeyStatus> callback = new ApiCallback<KeyStatus> callback() {
     *   ....implement callback method.
     * };
     *
     * String keyId = "{key id}";
     * KeyStatus status = caver.kas.wallet.deleteKeyAsync(keyId, callback);
     * }
     * </pre>
     *
     * @param keyId The key ID.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deleteKeyAsync(String keyId, ApiCallback<KeyStatus> callback) throws ApiException {
        return getKeyApi().keyDeletionAsync(chainId, keyId, callback);
    }

    /**
     * Retrieve a list of keys.<br>
     * GET /v2/key
     *
     * <pre>{@code
     * String krn = "krn";
     * KeyList response = caver.wallet.getKeyListByKRN(krn);
     * }</pre>
     *
     * @param krn The KAS resource name.
     * @return KeyList
     * @throws ApiException
     */
    public KeyList getKeyListByKRN(String krn) throws ApiException {
        return getKeyListByKRN(krn, new WalletQueryOptions());
    }

    /**
     * Retrieve a list of keys asynchronously.<br>
     * GET /v2/key
     *
     * <pre>{@code
     * ApiCallback<KeyList> callback = new ApiCallback<KeyList> callback() {
     *   ....implement callback method.
     * };
     *
     * String krn = "krn";
     * caver.wallet.getKeyListByKRNAsync(krn, callback);
     *
     * }</pre>
     *
     * @param krn The KAS resource name.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getKeyListByKRNAsync(String krn, ApiCallback<KeyList> callback) throws ApiException {
        return getKeyListByKRNAsync(krn, new WalletQueryOptions(), callback);
    }

    /**
     * Retrieve a list of keys.<br>
     * GET /v2/key
     *
     * <pre>{@code
     * String krn = "krn";
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(1);
     *
     * KeyList response = caver.wallet.getKeyListByKRN(krn, options);
     * }</pre>
     *
     * @param krn The KAS resource name.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return KeyList
     * @throws ApiException
     */
    public KeyList getKeyListByKRN(String krn, WalletQueryOptions options) throws ApiException {
        String size = options.getSize() != null ? Long.toString(options.getSize()) : null;
        return this.keyApi.retrieveKeys(krn, chainId, options.getCursor(), size);
    }

    /**
     * Retrieve a list of keys asynchronously.<br>
     * GET /v2/key
     *
     * <pre>{@code
     * ApiCallback<KeyList> callback = new ApiCallback<KeyList> callback() {
     *   ....implement callback method.
     * };
     *
     * String krn = "krn";
     * WalletQueryOptions options = new WalletQueryOptions();
     * options.setSize(1);
     *
     * KeyList response = caver.wallet.getKeyListByKRN(krn, options, callback);
     * }</pre>
     *
     * @param krn The KAS resource name.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getKeyListByKRNAsync(String krn, WalletQueryOptions options, ApiCallback<KeyList> callback) throws ApiException {
        String size = options.getSize() != null ? Long.toString(options.getSize()) : null;
        return this.keyApi.retrieveKeysAsync(krn, chainId, options.getCursor(), size, callback);
    }

    /**
     * Retrieve the histories of fee delegation transactions.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * GET /v2/history/fd/tx
     *
     * <pre>{@code
     * FDTransactionWithCurrencyResultList response = caver.wallet.getFDTransactionList();
     * }</pre>
     *
     * @return FDTransactionWithCurrencyResultList
     * @throws ApiException
     */
    public FDTransactionWithCurrencyResultList getFDTransactionList() throws ApiException {
        return getFDTransactionList(null);
    }

    /**
     * Retrieve the histories of fee delegation transactions asynchronously.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * GET /v2/history/fd/tx
     *
     * <pre>{@code
     * ApiCallback<FDTransactionWithCurrencyResultList> callback = new ApiCallback<FDTransactionWithCurrencyResultList> callback() {
     *   ....implement callback method.
     * };
     *
     * caver.wallet.getFDTransactionListAsync(callback);
     * }</pre>
     *
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getFDTransactionListAsync(ApiCallback<FDTransactionWithCurrencyResultList> callback) throws ApiException {
        return getFDTransactionListAsync(null, callback);
    }

    /**
     * Retrieve the histories of fee delegation transactions.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * If you passed from passed as a parameter, retrieves transaction only from a certain address.<br>
     * GET /v2/history/fd/tx
     *
     * <pre>{@code
     * String from = "0x{fromAddress}";
     * FDTransactionWithCurrencyResultList response = caver.wallet.getFDTransactionList(from);
     * }</pre>
     *
     * @param from The Klaytn account address of the sender.
     * @return FDTransactionWithCurrencyResultList
     * @throws ApiException
     */
    public FDTransactionWithCurrencyResultList getFDTransactionList(String from) throws ApiException {
        return this.transactionHistoryApi.getV2HistoryFdTx(chainId, from);
    }

    /**
     * Retrieve the histories of fee delegated transactions asynchronously.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * If you passed from passed as a parameter, retrieves transaction only from a certain address.<br>
     * GET /v2/history/fd/tx
     *
     * <pre>{@code
     * ApiCallback<FDTransactionWithCurrencyResultList> callback = new ApiCallback<FDTransactionWithCurrencyResultList> callback() {
     *   ....implement callback method.
     * };
     *
     * String from = "0x{fromAddress}";
     * caver.wallet.getFDTransactionListAsync(from, callback);
     * }</pre>
     *
     * @param from The Klaytn account address of the sender.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFDTransactionListAsync(String from, ApiCallback<FDTransactionWithCurrencyResultList> callback) throws ApiException {
        return this.transactionHistoryApi.getV2HistoryFdTxAsync(chainId, from, callback);
    }

    /**
     * Retrieves a fee delegated transaction by transaction hash.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * GET /v2/history/fd/tx/{transaction-hash}
     *
     * <pre>{@code
     * String txHash = "0x{transactionHash}";
     * FDTransactionWithCurrencyResult response = caver.wallet.getFDTransaction(txHash);
     * }</pre>
     *
     * @param txHash The transaction hash to get a fee delegated transaction.
     * @return FDTransactionWithCurrencyResult
     * @throws ApiException
     */
    public FDTransactionWithCurrencyResult getFDTransaction(String txHash) throws ApiException {
        return this.transactionHistoryApi.getV2HistoryFdTxTransactionHash(txHash, chainId);
    }

    /**
     * Retrieves a fee delegated transaction by transaction hash asynchronously.<br>
     * You can find out the KRW and USD price of the fees at the time of sending the transaction.<br>
     * GET /v2/history/fd/tx/{transaction-hash}
     *
     * <pre>{@code
     * ApiCallback<FDTransactionWithCurrencyResult> callback = new ApiCallback<FDTransactionWithCurrencyResultList> callback() {
     *   ....implement callback method.
     * };
     *
     * String txHash = "0x{transactionHash}";
     * caver.wallet.getFDTransactionAsync(txHash, callback);
     * }</pre>
     *
     * @param txHash The transaction hash to get a fee delegated transaction.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getFDTransactionAsync(String txHash, ApiCallback<FDTransactionWithCurrencyResult> callback) throws ApiException {
        return this.transactionHistoryApi.getV2HistoryFdTxTransactionHashAsync(txHash, chainId, callback);
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
     * @return RegistrationApi
     */
    public RegistrationApi getRegistrationApi() {
        return registrationApi;
    }

    /**
     * Getter function for feepayerApi.
     * @return FeepayerApi
     */
    public FeepayerApi getFeepayerApi() {
        return feepayerApi;
    }

    /**
     * Getter function for transactionHistoryApi
     * @return transactionHistoryApi
     */
    public TransactionHistoryApi getTransactionHistoryApi() {
        return transactionHistoryApi;
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
     * Setter function for feepayerApi
     * @param feepayerApi fee payer API rest client object.
     */
    public void setFeepayerApi(FeepayerApi feepayerApi) {
        this.feepayerApi = feepayerApi;
    }

    /**
     * Setter function for transactionHistoryApi
     * @param transactionHistoryApi Transaction History API rest client object.
     */
    public void setTransactionHistoryApi(TransactionHistoryApi transactionHistoryApi) {
        this.transactionHistoryApi = transactionHistoryApi;
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
        setFeepayerApi(new FeepayerApi(apiClient));
        setTransactionHistoryApi(new TransactionHistoryApi(apiClient));
    }


    /**
     * Setter function for RPC
     * @param rpc The RPC for using Node API.
     */
    public void setRPC(RPC rpc) {
        this.rpc = rpc;
    }

    /**
     * Setter function for nodeAPIInitialized
     * @param nodeAPIInitialized The boolean for checking NodeAPI is initialized or not.
     */
    public void setNodeAPIInitialized(boolean nodeAPIInitialized) {
        this.nodeAPIInitialized = nodeAPIInitialized;
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

    OneOfAccountUpdateTransactionRequestAccountKey makeUncompressedKeyFormat(OneOfAccountUpdateTransactionRequestAccountKey updateKey) {
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
            List<OneOfRoleBasedUpdateKeyTypeKeyItems> roleKeyList = ((KeyTypeRoleBased) updateKey).getKey();
            roleKeyList.stream().forEach(roleKey -> makeUncompressedKeyFormat((OneOfAccountUpdateTransactionRequestAccountKey)roleKey));
        } else {
            throw new IllegalArgumentException("Not supported update Key type.");
        }

        return updateKey;
    }

    OneOfFDAccountUpdateTransactionRequestAccountKey makeUncompressedKeyFormat(OneOfFDAccountUpdateTransactionRequestAccountKey updateKey) {
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
            List<OneOfRoleBasedUpdateKeyTypeKeyItems> roleKeyList = ((KeyTypeRoleBased) updateKey).getKey();
            roleKeyList.stream().forEach(roleKey -> makeUncompressedKeyFormat((OneOfFDAccountUpdateTransactionRequestAccountKey)roleKey));
        } else {
            throw new IllegalArgumentException("Not supported update Key type.");
        }

        return updateKey;
    }

    OneOfFDUserAccountUpdateTransactionRequestAccountKey makeUncompressedKeyFormat(OneOfFDUserAccountUpdateTransactionRequestAccountKey updateKey) {
        if(updateKey instanceof EmptyUpdateKeyType) {
            return updateKey;
        }

        if(updateKey instanceof PubkeyUpdateKeyType) {
            ((KeyTypePublic) updateKey).setKey(KASUtils.addUncompressedKeyPrefix(((KeyTypePublic) updateKey).getKey()));
        } else if(updateKey instanceof MultisigUpdateKeyType) {
            MultisigUpdateKey keys = ((KeyTypeMultiSig) updateKey).getKey();
            keys.getWeightedKeys().stream().forEach(weightKey -> {
                weightKey.setPublicKey(KASUtils.addUncompressedKeyPrefix(weightKey.getPublicKey()));
            });
        } else if(updateKey instanceof RoleBasedUpdateKeyType) {
            List<OneOfRoleBasedUpdateKeyTypeKeyItems> roleKeyList = ((KeyTypeRoleBased) updateKey).getKey();
            roleKeyList.stream().forEach(roleKey -> makeUncompressedKeyFormat((OneOfFDAccountUpdateTransactionRequestAccountKey) roleKey));
        } else {
            throw new IllegalArgumentException("Not supported update Key type.");
        }

        return updateKey;
    }

    /**
     * Validate whether given migrationAccount is valid or not.
     * @param migrationAccount An account to be migrated to KAS Wallet
     */
    private void validateMigrationAccount(MigrationAccount migrationAccount) {
        if(migrationAccount.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address of migrationAccount must not be empty.");
        }

        MigrationAccountKey<?> migrationAccountKey = migrationAccount.getMigrationAccountKey();
        if(migrationAccountKey == null) {
            throw new IllegalArgumentException("MigrationAccountKey of migrationAccount must not be empty.");
        }

        if(
                migrationAccountKey instanceof SinglePrivateKey == false
                        && migrationAccountKey instanceof MultisigPrivateKeys == false
                        && migrationAccountKey instanceof RoleBasedPrivateKeys == false
        ) {
            throw new IllegalArgumentException(
                    "MigrationAccountKey of Migration Account must be one of following class " +
                    "[SinglePrivateKey, MultisigPrivateKeys, RoleBasedPrivateKeys]."
            );
        }
    }

    /**
     * Create an AbstractKeyring from a given migrationAccount
     * @param migrationAccount account to be migrated to KAS Wallet
     * @return AbstractKeyring
     */
    private AbstractKeyring createKeyringFromMigrationAccount(MigrationAccount migrationAccount) {
        MigrationAccountKey<?> migrationAccountKey = migrationAccount.getMigrationAccountKey();

        if(migrationAccountKey instanceof SinglePrivateKey) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((SinglePrivateKey) migrationAccountKey).getKey()
            );
        } else if(migrationAccountKey instanceof MultisigPrivateKeys) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((MultisigPrivateKeys) migrationAccountKey).getKey()
            );
        } else if(migrationAccountKey instanceof RoleBasedPrivateKeys) {
            return KeyringFactory.create(
                    migrationAccount.getAddress(),
                    ((RoleBasedPrivateKeys) migrationAccountKey).getKey()
            );
        } else {
            throw new IllegalArgumentException("MigrationAccountKey must be one of the following class instance [SinglePrivateKey, MultisigPrivateKeys, RoleBasedPrivateKeys].");
        }
    }
}
