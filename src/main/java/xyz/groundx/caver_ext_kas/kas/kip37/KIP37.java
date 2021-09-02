/*
 * Copyright 2021 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.kas.kip37;

import com.squareup.okhttp.Call;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.Kip37Api;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.Kip37DeployerApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.*;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Representing a wrapping class that connects KIP-37 API.
 */
public class KIP37 {
    /**
     * KIP-37 API rest-client object.
     */
    Kip37Api kip37Api;

    /**
     * KIP-37 deployer API rest-client object.
     */
    Kip37DeployerApi kip37DeployerApi;

    /**
     * Klaytn network id
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * Creates a KIP-37 API instance.
     * @param chainId A Klaytn network chain id.
     * @param apiClient The Api client for connection with KAS.
     */
    public KIP37(String chainId, ApiClient apiClient) {
        this.chainId = chainId;
        this.apiClient = apiClient;

        setKip37Api(new Kip37Api(apiClient));
        setKip37DeployerApi(new Kip37DeployerApi(apiClient));
    }

    /**
     * Deploy KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return DeployerKip37ContractResponse
     * @throws ApiException
     */
    public DeployerKip37ContractResponse deploy(String uri, String alias) throws ApiException {
        return deploy(uri, alias, null);
    }

    /**
     * Deploy KIP-37 contract.<br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return DeployerKip37ContractResponse
     * @throws ApiException
     */
    public DeployerKip37ContractResponse deploy(String uri, String alias, Kip37FeePayerOption feePayerOption) throws ApiException {
        DeployKip37ContractRequest request = new DeployKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setOptions(feePayerOption);

        return kip37Api.deployContract(chainId, request);
    }

    /**
     * Deploy KIP-37 contract asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String uri, String alias, ApiCallback<DeployerKip37ContractResponse> callback) throws ApiException {
        return deployAsync(uri, alias, null, callback);
    }

    /**
     * Deploy KIP-37 contract.<br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String uri, String alias, Kip37FeePayerOption feePayerOption, ApiCallback<DeployerKip37ContractResponse> callback) throws ApiException {
        DeployKip37ContractRequest request = new DeployKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setOptions(feePayerOption);

        return kip37Api.deployContractAsync(chainId, request, callback);
    }

    /**
     * Get a list of all KIP-37 contracts by account. The response items will be listed in the order of contract creation request time.
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Kip37ContractListResponse getContractList() throws ApiException {
        KIP37QueryOptions options = new KIP37QueryOptions();
        return getContractList(options);
    }

    /**
     * Get a list of all KIP-37 contracts by account. The response items will be listed in the order of contract creation request time.
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Kip37ContractListResponse getContractList(KIP37QueryOptions options) throws ApiException {
        return kip37Api.listContractsInDeployerPool(chainId, options.getSize(), options.getCursor(), options.getStatus());
    }

    /**
     * Get a list of all KIP-37 contracts by account asynchronously.<br>
     * The response items will be listed in the order of contract creation request time.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Call getContractListAsync(ApiCallback<Kip37ContractListResponse> callback) throws ApiException {
        KIP37QueryOptions options = new KIP37QueryOptions();
        return getContractListAsync(options, callback);
    }

    /**
     * Get a list of all KIP-37 contracts by account asynchronously.<br>
     * The response items will be listed in the order of contract creation request time.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Call getContractListAsync(KIP37QueryOptions options, ApiCallback<Kip37ContractListResponse> callback) throws ApiException {
        return kip37Api.listContractsInDeployerPoolAsync(chainId, options.getSize(), options.getCursor(), options.getStatus(), callback);
    }

    /**
     * Import a contract that has already been deployed.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract/import
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37ContractListResponseItem importContract(String contractAddress, String uri, String alias) throws ApiException {
        return importContract(contractAddress, uri, alias, null);
    }

    /**
     * Import a contract that has already been deployed.<br>
     * POST /v1/contract/import
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37ContractListResponseItem importContract(String contractAddress, String uri, String alias, Kip37FeePayerOption feePayerOption) throws ApiException {
        ImportKip37ContractRequest request = new ImportKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setAddress(contractAddress);
        request.setOptions(feePayerOption);

        return kip37Api.importContract(chainId, request);
    }

    /**
     * Import a contract that has already been deployed asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract/import
     *
     * <pre>Example :
     * {@code
     *
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call importContractAsync(String contractAddress, String uri, String alias, ApiCallback<Kip37ContractListResponseItem> callback) throws ApiException {
        return importContractAsync(contractAddress, uri, alias, null, callback);
    }

    /**
     * Import a contract that has already been deployed asynchronously.<br>
     * POST /v1/contract/import
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call importContractAsync(String contractAddress, String uri, String alias, Kip37FeePayerOption feePayerOption, ApiCallback<Kip37ContractListResponseItem> callback) throws ApiException {
        ImportKip37ContractRequest request = new ImportKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setAddress(contractAddress);
        request.setOptions(feePayerOption);

        return kip37Api.importContractAsync(chainId, request, callback);
    }

    /**
     * Queries a specific contract using the alias or the contract address.
     * GET /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37ContractListResponseItem getContract(String addressOrAlias) throws ApiException {
        return kip37Api.getContract(addressOrAlias, chainId);
    }

    /**
     * Queries a specific contract using the alias or the contract address asynchronously.
     * GET /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractAsync(String addressOrAlias, ApiCallback<Kip37ContractListResponseItem> callback) throws ApiException {
        return kip37Api.getContractAsync(addressOrAlias, chainId, callback);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37ContractListResponseItem updateContractOptions(String addressOrAlias) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        return updateContractOptions(addressOrAlias, null);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * PUT /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37ContractListResponseItem updateContractOptions(String addressOrAlias, Kip37FeePayerOption feePayerOption) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        request.setOptions(feePayerOption);

        return kip37Api.putContract(chainId, addressOrAlias, request);
    }


    /**
     * Updates the information of a KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, ApiCallback<Kip37ContractListResponseItem> callback) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        return updateContractOptionsAsync(addressOrAlias, null, callback);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * PUT /v1/contract/{contract-address-or-alias}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, Kip37FeePayerOption feePayerOption, ApiCallback<Kip37ContractListResponseItem> callback) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        request.setOptions(feePayerOption);

        return kip37Api.putContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Grants authorization to a third party to transfer all tokens for a specified contract.
     * POST /v1/contract/{contract-address-or-alias}/approveall
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param from The Klaytn account address of the owner.
     * @param to The Klaytn account address to be authorized for token transfer.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse setApprovalForAll(String addressOrAlias, String from, String to) throws ApiException {
        return setApprovalForAll(addressOrAlias, from, to, true);
    }


    /**
     * Grants or cancels authorization to a third party to transfer all tokens for a specified contract.
     * POST /v1/contract/{contract-address-or-alias}/approveall
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param from The Klaytn account address of the owner.
     * @param to The Klaytn account address to be authorized for token transfer.
     * @param approved True if authorization is granted or false to authorization is cancelled.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse setApprovalForAll(String addressOrAlias, String from, String to, boolean approved) throws ApiException {
        ApproveAllKip37ContractRequest request = new ApproveAllKip37ContractRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setApproved(approved);

        return kip37Api.approveAll(chainId, addressOrAlias, request);
    }

    /**
     * Grants authorization to a third party to transfer all tokens for a specified contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/approveall
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param from The Klaytn account address of the owner.
     * @param to The Klaytn account address to be authorized for token transfer.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call setApprovalForAllAsync(String addressOrAlias, String from, String to, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return setApprovalForAllAsync(addressOrAlias, from, to, true, callback);
    }

    /**
     * Grants or cancels authorization to a third party to transfer all tokens for a specified contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/approveall
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param from The Klaytn account address of the owner.
     * @param to The Klaytn account address to be authorized for token transfer.
     * @param approved True if authorization is granted or false to authorization is cancelled.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call setApprovalForAllAsync(String addressOrAlias, String from, String to, boolean approved, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        ApproveAllKip37ContractRequest request = new ApproveAllKip37ContractRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setApproved(approved);

        return kip37Api.approveAllAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pauses all operation for a specified contract.
     * It sets a pauser to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/pause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pause(String addressOrAlias) throws ApiException {
        return pause(addressOrAlias, null);
    }

    /**
     * Pauses all operation for a specified contract.
     * POST /v1/contract/{contract-address-or-alias}/pause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to pause all contract operations
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pause(String addressOrAlias, String pauser) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return kip37Api.pauseContract(chainId, addressOrAlias, request);
    }

    /**
     * Pauses all operation for a specified contract asynchronously.
     * It sets a pauser to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/pause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return pauseAsync(addressOrAlias, null, callback);
    }

    /**
     * Pauses all operation for a specified contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/pause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to pause all contract operations
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return kip37Api.pauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Resume the operations for a paused contract.
     * It sets a pauser to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/unpause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpause(String addressOrAlias) throws ApiException {
        return unpause(addressOrAlias, null);
    }

    /**
     * Resume the operations for a paused contract.
     * POST /v1/contract/{contract-address-or-alias}/unpause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to resume all contract operations
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpause(String addressOrAlias, String pauser) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return kip37Api.unpauseContract(chainId, addressOrAlias, request);
    }

    /**
     * Resume the operations for a paused contract asynchronously.
     * It sets a pauser to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/unpause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return unpauseAsync(addressOrAlias, null, callback);
    }

    /**
     * Resume the operations for a paused contract.
     * POST /v1/contract/{contract-address-or-alias}/unpause
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to resume all contract operations
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return kip37Api.unpauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token. It cannot use an existing one.
     * @param initialSupply The initial supply of new token.
     * @param uri The token uri.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse create(String addressOrAlias, BigInteger tokenId, BigInteger initialSupply, String uri) throws ApiException {
        return create(addressOrAlias, tokenId, initialSupply, uri, null);
    }

    /**
     * Create a new token from a specified KIP-37 contract.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token.(in hex, with the 0x prefix) It cannot use an existing one.
     * @param initialSupply The initial supply of new token.(in hex, with the 0x prefix)
     * @param uri The token uri.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse create(String addressOrAlias, String tokenId, String initialSupply, String uri) throws ApiException {
        return create(addressOrAlias, tokenId, initialSupply, uri, null);
    }

    /**
     * Create a new token from a specified KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token. It cannot use an existing one.
     * @param initialSupply The initial supply of new token.
     * @param uri The token uri.
     * @param sender The account to create the token.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse create(String addressOrAlias, BigInteger tokenId, BigInteger initialSupply, String uri, String sender) throws ApiException {
        return create(addressOrAlias, sender, Numeric.toHexStringWithPrefix(tokenId), Numeric.toHexStringWithPrefix(initialSupply), uri);
    }

    /**
     * Create a new token from a specified KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token.(in hex, with the 0x prefix) It cannot use an existing one.
     * @param initialSupply The initial supply of new token.(in hex, with the 0x prefix)
     * @param uri The token uri.
     * @param sender The account to create the token.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse create(String addressOrAlias, String tokenId, String initialSupply, String uri, String sender) throws ApiException {
        CreateKip37TokenRequest request = new CreateKip37TokenRequest();
        request.setSender(sender);
        request.setId(tokenId);
        request.setInitialSupply(initialSupply);
        request.setUri(uri);

        return kip37Api.createToken(chainId, addressOrAlias, request);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token. It cannot use an existing one.
     * @param initialSupply The initial supply of new token.
     * @param uri The token uri.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call createAsync(String addressOrAlias, BigInteger tokenId, BigInteger initialSupply, String uri, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return createAsync(addressOrAlias, tokenId, initialSupply, uri, null, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token.(in hex, with the 0x prefix) It cannot use an existing one.
     * @param initialSupply The initial supply of new token.(in hex, with the 0x prefix)
     * @param uri The token uri.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call createAsync(String addressOrAlias, String tokenId, String initialSupply, String uri, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return createAsync(addressOrAlias, tokenId, initialSupply, uri, null, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token. It cannot use an existing one.
     * @param initialSupply The initial supply of new token.
     * @param uri The token uri.
     * @param sender The account to create the token.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call createAsync(String addressOrAlias, BigInteger tokenId, BigInteger initialSupply, String uri, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return createAsync(addressOrAlias, sender, Numeric.toHexStringWithPrefix(tokenId), Numeric.toHexStringWithPrefix(initialSupply), uri, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token.(in hex, with the 0x prefix) It cannot use an existing one.
     * @param initialSupply The initial supply of new token.(in hex, with the 0x prefix)
     * @param uri The token uri.
     * @param sender The account to create the token.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call createAsync(String addressOrAlias, String tokenId, String initialSupply, String uri, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        CreateKip37TokenRequest request = new CreateKip37TokenRequest();
        request.setSender(sender);
        request.setId(tokenId);
        request.setInitialSupply(initialSupply);
        request.setUri(uri);

        return kip37Api.createTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Retrieve a list of KIP-37 tokens.
     * GET /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Kip37TokenInfoListResponse getTokenList(String addressOrAlias) throws ApiException {
        return getTokenList(addressOrAlias, new KIP37QueryOptions());
    }

    /**
     * Retrieve a list of KIP-37 tokens.
     * GET /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor`.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Kip37TokenInfoListResponse getTokenList(String addressOrAlias, KIP37QueryOptions queryOptions) throws ApiException {
        return kip37Api.getTokens(addressOrAlias, chainId, queryOptions.getSize(), queryOptions.getCursor());
    }

    /**
     * Retrieve a list of KIP-37 tokens asynchronously.
     * GET /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, ApiCallback<Kip37TokenInfoListResponse> callback) throws ApiException {
        return getTokenListAsync(addressOrAlias, new KIP37QueryOptions(), callback);
    }

    /**
     * Retrieve a list of KIP-37 tokens asynchronously.
     * GET /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback to handle response.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, KIP37QueryOptions queryOptions, ApiCallback<Kip37TokenInfoListResponse> callback) throws ApiException {
        return kip37Api.getTokensAsync(addressOrAlias, chainId, queryOptions.getSize(), queryOptions.getCursor(), callback);
    }

    /**
     * Burns KIP-37 tokens.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();

        return burn(addressOrAlias, ids_hex, amounts_hex, null);
    }

    /**
     * Burns KIP-37 tokens.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.(in hex, with the 0x prefix)
     * @param amounts The array of token amount to burn.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, String[] ids, String[] amounts) throws ApiException {
        return burn(addressOrAlias, ids, amounts, null);
    }

    /**
     * Burns KIP-37 tokens.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, String from) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();

        return burn(addressOrAlias, ids_hex, amounts_hex, from);
    }

    /**
     * Burns KIP-37 tokens.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.(in hex, with the 0x prefix)
     * @param amounts The array of token amount to burn.(in hex, with the 0x prefix)
     * @param from The owner of the token or the account that authorized to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, String[] ids, String[] amounts, String from) throws ApiException {
        BurnKip37TokenRequest request = new BurnKip37TokenRequest();
        request.setFrom(from);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));

        return kip37Api.burnToken(chainId, addressOrAlias, request);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();

        return burnAsync(addressOrAlias, ids_hex, amounts_hex, null, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.(in hex, with the 0x prefix)
     * @param amounts The array of token amount to burn.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String[] ids, String[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, ids, amounts, null, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, String from, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();

        return burnAsync(addressOrAlias, ids_hex, amounts_hex, from, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.(in hex, with the 0x prefix)
     * @param amounts The array of token amount to burn.(in hex, with the 0x prefix)
     * @param from The owner of the token or the account that authorized to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String[] ids, String[] amounts, String from, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        BurnKip37TokenRequest request = new BurnKip37TokenRequest();
        request.setFrom(from);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));

        return kip37Api.burnTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.
     * @param amounts Array of the token amount to mint additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger[] ids, BigInteger[] amounts) throws ApiException {
        return mint(addressOrAlias, to, ids, amounts, null);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amounts Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, String[] ids, String[] amounts) throws ApiException {
        return mint(addressOrAlias, to, ids, amounts, null);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.
     * @param amounts Array of the token amount to mint additionally.
     * @param sender The account address to mint token additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger[] ids, BigInteger[] amounts, String sender) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();
        return mint(addressOrAlias, to, ids_hex, amounts_hex, sender);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amounts Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param sender The account address to mint token additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, String[] ids, String[] amounts, String sender) throws ApiException {
        MintKip37TokenRequest request = new MintKip37TokenRequest();
        request.setSender(sender);
        request.setTo(to);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));
        return kip37Api.mintToken(chainId, addressOrAlias, request);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.
     * @param amounts Array of the token amount to mint additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger[] ids, BigInteger[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, ids, amounts, null, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amounts Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String[] ids, String[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, ids, amounts, null, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.
     * @param amounts Array of the token amount to mint additionally.
     * @param sender The account address to mint token additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger[] ids, BigInteger[] amounts, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();
        return mintAsync(addressOrAlias, to, ids_hex, amounts_hex, sender, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amounts Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param sender The account address to mint token additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String[] ids, String[] amounts, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        MintKip37TokenRequest request = new MintKip37TokenRequest();
        request.setSender(sender);
        request.setTo(to);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));
        return kip37Api.mintTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends a token for a given KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param id The token id to send.
     * @param amount The amount of token to send.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, BigInteger id, BigInteger amount) throws ApiException {
        return transfer(addressOrAlias, sender, owner, to, new BigInteger[] {id}, new BigInteger[] {amount});
    }

    /**
     * Sends a token for a given KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param id The token id to send.(in hex, with the 0x prefix)
     * @param amount The amount of token to send.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, String id, String amount) throws ApiException {
        return transfer(addressOrAlias, sender, owner, to, new String[]{id}, new String[] {amount});
    }

    /**
     * Sends multiple tokens for a given KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param ids Array of token id to send.
     * @param amounts Array of token amount to send.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, BigInteger[] ids, BigInteger[] amounts) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();
        return transfer(addressOrAlias, sender, owner, to, ids_hex, amounts_hex);
    }

    /**
     * Sends multiple tokens for a given KIP-37 contract.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param ids Array of token id to send.(in hex, with the 0x prefix)
     * @param amounts Array of token amount to send.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, String[] ids, String[] amounts) throws ApiException {
        TransferKip37TokenRequest request = new TransferKip37TokenRequest();
        request.setSender(sender);
        request.setOwner(owner);
        request.setTo(to);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));

        return kip37Api.transferToken(chainId, addressOrAlias, request);
    }

    /**
     * Sends a token for a given KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param id The token id to send.
     * @param amount The amount of token to send.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, BigInteger id, BigInteger amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, sender, owner, to, new BigInteger[] {id}, new BigInteger[] {amount}, callback);
    }

    /**
     * Sends a token for a given KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param id The token id to send.(in hex, with the 0x prefix)
     * @param amount The amount of token to send.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, String id, String amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, sender, owner, to, new String[]{id}, new String[] {amount}, callback);
    }

    /**
     * Sends multiple tokens for a given KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param ids Array of token id to send.
     * @param amounts Array of token amount to send.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, BigInteger[] ids, BigInteger[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = (String[]) Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray();
        String[] amounts_hex = (String[]) Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray();
        return transferAsync(addressOrAlias, sender, owner, to, ids_hex, amounts_hex, callback);
    }


    /**
     * Sends multiple tokens for a given KIP-37 contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param sender The account address to send the token.
     * @param owner The account address that owned the token.
     * @param to The account address to receive the token.
     * @param ids Array of token id to send.(in hex, with the 0x prefix)
     * @param amounts Array of token amount to send.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, String[] ids, String[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        TransferKip37TokenRequest request = new TransferKip37TokenRequest();
        request.setSender(sender);
        request.setOwner(owner);
        request.setTo(to);
        request.setIds(Arrays.asList(ids));
        request.setAmounts(Arrays.asList(amounts));

        return kip37Api.transferTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, BigInteger tokenId) throws ApiException {
        return pauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, String tokenId) throws ApiException {
        return pauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, BigInteger tokenId, String sender) throws ApiException {
        return pauseToken(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), sender);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, String tokenId, String sender) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return kip37Api.pauseToken(chainId, addressOrAlias, tokenId, request);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call pauseTokenAsync(String addressOrAlias, BigInteger tokenId, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return pauseTokenAsync(addressOrAlias, tokenId, null, callback);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call pauseTokenAsync(String addressOrAlias, String tokenId, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return pauseTokenAsync(addressOrAlias, tokenId, null, callback);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param sender The account address to execute token operations.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call pauseTokenAsync(String addressOrAlias, BigInteger tokenId, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return pauseTokenAsync(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), sender, callback);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call pauseTokenAsync(String addressOrAlias, String tokenId, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return kip37Api.pauseTokenAsync(chainId, addressOrAlias, tokenId, request, callback);
    }

    /**
     * Resume paused token operation for a given contract.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, BigInteger tokenId) throws ApiException {
        return unpauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Resume paused token operation for a given contract.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, String tokenId) throws ApiException {
        return unpauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Resume paused token operation for a given contract.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, BigInteger tokenId, String sender) throws ApiException {
        return unpauseToken(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), sender);
    }

    /**
     * Resume paused token operation for a given contract.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, String tokenId, String sender) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return kip37Api.unpauseToken(chainId, addressOrAlias, tokenId, request);
    }

    /**
     * Resume paused token operation for a given contract asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call unpauseTokenAsync(String addressOrAlias, BigInteger tokenId, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return unpauseTokenAsync(addressOrAlias, tokenId, null, callback);
    }

    /**
     * Resume paused token operation for a given contract asynchronously.
     * It sets a sender to contract deployer.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call unpauseTokenAsync(String addressOrAlias, String tokenId, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return unpauseTokenAsync(addressOrAlias, tokenId, null, callback);
    }

    /**
     * Resume paused token operation for a given contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @param sender The account address to execute token operations.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call unpauseTokenAsync(String addressOrAlias, BigInteger tokenId, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return unpauseTokenAsync(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), sender, callback);
    }

    /**
     * Resume paused token operation for a given contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call unpauseTokenAsync(String addressOrAlias, String tokenId, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return kip37Api.unpauseTokenAsync(chainId, addressOrAlias, tokenId, request, callback);
    }

    /**
     * Retrieves a list of tokens owned by a certain account.
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @return Kip37TokenListResponse
     * @throws ApiException
     */
    public Kip37TokenListResponse getTokenListByOwner(String addressOrAlias, String ownerAddress) throws ApiException {
        return getTokenListByOwner(addressOrAlias, ownerAddress, new KIP37QueryOptions());
    }

    /**
     * Retrieves a list of tokens owned by a certain account.
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37TokenListResponse
     * @throws ApiException
     */
    public Kip37TokenListResponse getTokenListByOwner(String addressOrAlias, String ownerAddress, KIP37QueryOptions queryOptions) throws ApiException {
        return kip37Api.getTokensByOwner(addressOrAlias, ownerAddress, chainId, queryOptions.getSize(), queryOptions.getCursor());
    }

    /**
     * Retrieves a list of tokens owned by a certain account asynchronously.
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String addressOrAlias, String ownerAddress, ApiCallback<Kip37TokenListResponse> callback) throws ApiException {
        return getTokenListByOwnerAsync(addressOrAlias, ownerAddress, new KIP37QueryOptions(), callback);
    }

    /**
     * Retrieves a list of tokens owned by a certain account asynchronously.
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor` and `status`
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String addressOrAlias, String ownerAddress, KIP37QueryOptions queryOptions, ApiCallback<Kip37TokenListResponse> callback) throws ApiException {
        return kip37Api.getTokensByOwnerAsync(addressOrAlias, ownerAddress, chainId, queryOptions.getSize(), queryOptions.getCursor(), callback);
    }

    /**
     * Retrieves a contract deployer account.
     * GET /v1/deployer/default
     * @return Kip37DeployerResponse
     * @throws ApiException
     */
    public Kip37DeployerResponse getDeployer() throws ApiException {
        return kip37DeployerApi.getDefaultDeployer(chainId);
    }

    /**
     * Retrieving a contract deployer account asynchronously.
     * GET /v1/deployer/default
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getDeployerAsync(ApiCallback<Kip37DeployerResponse> callback) throws ApiException {
        return kip37DeployerApi.getDefaultDeployerAsync(chainId, callback);
    }

    /**
     * Getter function for kip37Api.
     * @return Kip37Api
     */
    public Kip37Api getKip37Api() {
        return kip37Api;
    }

    /**
     * Setter function for kip37Api
     * @param kip37Api The Kip37Api instance.
     */
    public void setKip37Api(Kip37Api kip37Api) {
        this.kip37Api = kip37Api;
    }

    /**
     * Getter function for kip37DeployerApi
     * @return Kip37DeployerApi
     */
    public Kip37DeployerApi getKip37DeployerApi() {
        return kip37DeployerApi;
    }

    /**
     * Setter function for kip37DeployerApi
     * @param kip37DeployerApi The Kip37DeployerApi instance.
     */
    public void setKip37DeployerApi(Kip37DeployerApi kip37DeployerApi) {
        this.kip37DeployerApi = kip37DeployerApi;
    }

    /**
     * Getter function for chain id.
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * Setter function for chain id.
     * @param chainId The Klaytn network id
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Getter function for ApiClient
     * @return ApiClient
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Setter function for ApiClient
     * @param apiClient The ApiClient for connecting with KAS.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setKip37Api(new Kip37Api(apiClient));
        setKip37DeployerApi(new Kip37DeployerApi(apiClient));
    }
}
