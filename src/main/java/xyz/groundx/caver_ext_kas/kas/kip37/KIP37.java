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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.ContractApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.DeployerApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.TokenApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api.TokenOwnershipApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.*;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Representing a wrapping class that connects KIP-37 API.
 */
public class KIP37 {
    /**
     * KIP-37 contract API rest-client object.
     */
    ContractApi contractApi;

    /**
     * KIP-37 deployer API rest-client object.
     */
    DeployerApi deployerApi;

    /**
     * KIP-37 token API rest-client object.
     */
    TokenApi tokenApi;

    /**
     * KIP-37 token ownership API rest-client object.
     */
    TokenOwnershipApi tokenOwnershipApi;

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
        setApiClient(apiClient);
    }

    /**
     * Deploy KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String alias = "my-first-kip37";
     * String uri = "https://token-cdn-domain/{id}.json";
     *
     * Kip37DeployResponse response = caver.kas.kip37.deploy(uri, alias);
     * }
     * </pre>
     *
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return DeployerKip37ContractResponse
     * @throws ApiException
     */
    public Kip37DeployResponse deploy(String uri, String alias) throws ApiException {
        return deploy(uri, alias, null);
    }

    /**
     * Deploy KIP-37 contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String alias = "my-first-kip37";
     * String uri = "https://token-cdn-domain/{id}.json";
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * Kip37DeployResponse response = caver.kas.kip37.deploy(uri, alias, option);
     * }
     * </pre>
     *
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return DeployerKip37ContractResponse
     * @throws ApiException
     */
    public Kip37DeployResponse deploy(String uri, String alias, Kip37FeePayerOption feePayerOption) throws ApiException {
        DeployKip37ContractRequest request = new DeployKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setOptions(feePayerOption);

        return contractApi.deployContract(chainId, request);
    }

    /**
     * Deploy KIP-37 contract asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip37DeployResponse> callback = new ApiCallback<Kip37DeployResponse>() {
     *     ....implements callback method
     * };
     *
     * String alias = "my-first-kip37";
     * String uri = "https://token-cdn-domain/{id}.json";
     *
     * caver.kas.kip37.deployAsync(uri, alias, option, callback);
     * }
     * </pre>
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String uri, String alias, ApiCallback<Kip37DeployResponse> callback) throws ApiException {
        return deployAsync(uri, alias, null, callback);
    }

    /**
     * Deploy KIP-37 contract asynchronously.<br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip37DeployResponse> callback = new ApiCallback<Kip37DeployResponse>() {
     *     ....implements callback method
     * };
     *
     * String alias = "my-first-kip37";
     * String uri = "https://token-cdn-domain/{id}.json";
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * caver.kas.kip37.deployAsync(uri, alias, option, callback);
     * }
     * </pre>
     *
     * @param uri The URI that stores the contract's metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String uri, String alias, Kip37FeePayerOption feePayerOption, ApiCallback<Kip37DeployResponse> callback) throws ApiException {
        DeployKip37ContractRequest request = new DeployKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setOptions(feePayerOption);

        return contractApi.deployContractAsync(chainId, request, callback);
    }

    /**
     * Get a list of all KIP-37 contracts by account. The response items will be listed in the order of contract creation request time.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * Kip37ContractListResponse response = caver.kas.kip37.getContractList();
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
     * Get a list of all KIP-37 contracts by account.<br>
     * The response items will be listed in the order of contract creation request time.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * KIP37QueryOptions options = new KIP37QueryOptions();
     * options.setSize(1);
     * options.setStatus(KIP37QueryOptions.STATUS_TYPE.DEPLOYED);
     *
     * Kip37ContractListResponse response = caver.kas.kip37.getContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Kip37ContractListResponse getContractList(KIP37QueryOptions options) throws ApiException {
        return contractApi.listContractsInDeployerPool(chainId, options.getSize(), options.getCursor(), options.getStatus());
    }

    /**
     * Get a list of all KIP-37 contracts by account asynchronously.<br>
     * The response items will be listed in the order of contract creation request time.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip37ContractListResponse> callback = new ApiCallback<Kip37ContractListResponse>() {
     *     ....implements callback method
     * };
     * caver.kas.kip37.getContractListAsync(callback);
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
     * ApiCallback<Kip37ContractListResponse> callback = new ApiCallback<Kip37ContractListResponse>() {
     *     ....implements callback method
     * };
     *
     * KIP37QueryOptions options = new KIP37QueryOptions();
     * options.setSize(1);
     * options.setStatus(KIP37QueryOptions.STATUS_TYPE.DEPLOYED);
     *
     * caver.kas.kip37.getContractListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37ContractListResponse
     * @throws ApiException
     */
    public Call getContractListAsync(KIP37QueryOptions options, ApiCallback<Kip37ContractListResponse> callback) throws ApiException {
        return contractApi.listContractsInDeployerPoolAsync(chainId, options.getSize(), options.getCursor(), options.getStatus(), callback);
    }

    /**
     * Import a contract that has already been deployed.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract/import
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * String uri = "https://token-cdn-domain/{id}.json";
     * String alias = "my-first-kip37";
     *
     * Kip37Contract response = caver.kas.kip37.importContract(contractAddress, uri, alias);
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37Contract importContract(String contractAddress, String uri, String alias) throws ApiException {
        return importContract(contractAddress, uri, alias, null);
    }

    /**
     * Import a contract that has already been deployed.<br>
     * POST /v1/contract/import
     *
     * <pre>Example :
     * {@code
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * String contractAddress = "0x{contractAddress}";
     * String uri = "https://token-cdn-domain/{id}.json";
     * String alias = "my-first-kip37";
     *
     * Kip37Contract response = caver.kas.kip37.importContract(contractAddress, uri, alias, option);
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37Contract importContract(String contractAddress, String uri, String alias, Kip37FeePayerOption feePayerOption) throws ApiException {
        ImportKip37ContractRequest request = new ImportKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setAddress(contractAddress);
        request.setOptions(feePayerOption);

        return contractApi.importContract(chainId, request);
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
     * ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String uri = "https://token-cdn-domain/{id}.json";
     * String alias = "my-first-kip37";
     *
     * caver.kas.kip37.importContractAsync(contractAddress, uri, alias, callback);
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
    public Call importContractAsync(String contractAddress, String uri, String alias, ApiCallback<Kip37Contract> callback) throws ApiException {
        return importContractAsync(contractAddress, uri, alias, null, callback);
    }

    /**
     * Import a contract that has already been deployed asynchronously.<br>
     * POST /v1/contract/import
     *
     * <pre>Example :
     * {@code
     *
     * ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
     *     ....implements callback method
     * };
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * String contractAddress = "0x{contractAddress}";
     * String uri = "https://token-cdn-domain/{id}.json";
     * String alias = "my-first-kip37";
     *
     * caver.kas.kip37.importContractAsync(contractAddress, uri, alias, option, callback);
     * }
     * </pre>
     *
     * @param contractAddress The contract address.
     * @param uri The URI for stroring contract metadata.
     * @param alias The alias of KIP-37 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call importContractAsync(String contractAddress, String uri, String alias, Kip37FeePayerOption feePayerOption, ApiCallback<Kip37Contract> callback) throws ApiException {
        ImportKip37ContractRequest request = new ImportKip37ContractRequest();
        request.setAlias(alias);
        request.setUri(uri);
        request.setAddress(contractAddress);
        request.setOptions(feePayerOption);

        return contractApi.importContractAsync(chainId, request, callback);
    }

    /**
     * Queries a specific contract using the alias or the contract address.<br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contract address}";
     * Kip37Contract response = caver.kas.kip37.getContract(contractAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37Contract getContract(String addressOrAlias) throws ApiException {
        return contractApi.getContract(addressOrAlias, chainId);
    }

    /**
     * Queries a specific contract using the alias or the contract address asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip37ContractListResponse> callback = new ApiCallback<Kip37ContractListResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contract address}";
     * caver.kas.kip37.getContractAsync(contractAddress, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractAsync(String addressOrAlias, ApiCallback<Kip37Contract> callback) throws ApiException {
        return contractApi.getContractAsync(addressOrAlias, chainId, callback);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>
     * {@code
     * Kip37Contract response = caver.kas.kip37.updateContractOptions("0x{contractAddress}");
     * }
     * </pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37Contract updateContractOptions(String addressOrAlias) throws ApiException {
        return updateContractOptions(addressOrAlias, null);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>
     * {@code
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * Kip37Contract response = caver.kas.kip37.updateContractOptions("0x{contractAddress}", option);
     * }
     * </pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @return Kip37ContractListResponseItem
     * @throws ApiException
     */
    public Kip37Contract updateContractOptions(String addressOrAlias, Kip37FeePayerOption feePayerOption) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        request.setOptions(feePayerOption);

        return contractApi.putContract(chainId, addressOrAlias, request);
    }


    /**
     * Updates the information of a KIP-37 contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>
     *{@code
     * ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.kip37.updateContractOptionsAsync("0x{contractAddress}", callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, ApiCallback<Kip37Contract> callback) throws ApiException {
        return updateContractOptionsAsync(addressOrAlias, null, callback);
    }

    /**
     * Updates the information of a KIP-37 contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>
     *{@code
     * ApiCallback<Kip37Contract> callback = new ApiCallback<Kip37Contract>() {
     *     ....implements callback method
     * };
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip37FeePayerOptionUserFeePayer userFeePayerOption = new Kip37FeePayerOptionUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip37FeePayerOption option = new Kip37FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * caver.kas.kip37.updateContractOptionsAsync("0x{contractAddress}", option, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param feePayerOption The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, Kip37FeePayerOption feePayerOption, ApiCallback<Kip37Contract> callback) throws ApiException {
        UpdateKip37ContractRequest request = new UpdateKip37ContractRequest();
        request.setOptions(feePayerOption);

        return contractApi.putContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Grants authorization to a third party to transfer all tokens for a specified contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/approveall<br>
     *
     * <pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}"
     * String to = "0x{toAddress}"
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.setApprovalForAll(contractAddress, from, to);
     * }</pre>
     *
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
     * Grants or cancels authorization to a third party to transfer all tokens for a specified contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/approveall<br>
     *
     * <pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}"
     * String to = "0x{toAddress}"
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.setApprovalForAll(contractAddress, from, to, true);
     * }</pre>
     *
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

        return contractApi.approveAll(chainId, addressOrAlias, request);
    }

    /**
     * Grants authorization to a third party to transfer all tokens for a specified contract asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/approveall
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}"
     * String to = "0x{toAddress}"
     *
     * caver.kas.kip37.setApprovalForAll(contractAddress, from, to, callback);
     * }</pre>
     *
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
     * Grants or cancels authorization to a third party to transfer all tokens for a specified contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/approveall<br>
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}"
     * String to = "0x{toAddress}"
     *
     * caver.kas.kip37.setApprovalForAll(contractAddress, from, to, true, callback);
     * }</pre>
     *
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

        return contractApi.approveAllAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pauses all operation for a specified contract.<br>
     * It sets a pauser to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     *<pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pause(contractAddress);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pause(String addressOrAlias) throws ApiException {
        return pause(addressOrAlias, null);
    }

    /**
     * Pauses all operation for a specified contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     *<pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     * String pauser = "0x{pauserAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pause(contractAddress, pauser);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to pause all contract operations
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pause(String addressOrAlias, String pauser) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return contractApi.pauseContract(chainId, addressOrAlias, request);
    }

    /**
     * Pauses all operation for a specified contract asynchronously.<br>
     * It sets a pauser to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * caver.kas.kip37.pauseAsync(contractAddress, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return pauseAsync(addressOrAlias, null, callback);
    }

    /**
     * Pauses all operation for a specified contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String puaser = "0x{pauserAddress}";
     * caver.kas.kip37.pauseAsync(contractAddress, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to pause all contract operations
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return contractApi.pauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Resume the operations for a paused contract.<br>
     * It sets a pauser to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     *<pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpause(contractAddress);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpause(String addressOrAlias) throws ApiException {
        return unpause(addressOrAlias, null);
    }

    /**
     * Resume the operations for a paused contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     *<pre>
     *{@code
     * String contractAddress = "0x{contractAddress}";
     * String pauser = "0x{pauserAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpause(contractAddress, pauser);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to resume all contract operations
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpause(String addressOrAlias, String pauser) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return contractApi.unpauseContract(chainId, addressOrAlias, request);
    }

    /**
     * Resume the operations for a paused contract asynchronously.<br>
     * It sets a pauser to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * caver.kas.kip37.unpauseAsync(contractAddress, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return unpauseAsync(addressOrAlias, null, callback);
    }

    /**
     * Resume the operations for a paused contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     *<pre>
     *{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String puaser = "0x{pauserAddress}";
     * caver.kas.kip37.unpauseAsync(contractAddress, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param pauser The pauser account to resume all contract operations
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(pauser);

        return contractApi.unpauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger initialSupply = BigInteger.valueOf(1000);
     * String uri = "https://token-cdn-domain/{0x01}.json";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri);
     * }</pre>
     *
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
     * Create a new token from a specified KIP-37 contract.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String tokenId = "0x02";
     * String initialSupply = Numeric.toHexStringWithPrefix(BigInteger.valueOf(1000));
     * String uri = "https://token-cdn-domain/{0x01}.json";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.create(testContractAddress, tokenId, initialSupply, uri);
     * }</pre>
     *
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
     * Create a new token from a specified KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger initialSupply = BigIntger.valueOf(1000);
     * String uri = "https://token-cdn-domain/0x01.json";
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.create(contractAddress, tokenId, initialSupply, uri, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The ID of new token. It cannot use an existing one.
     * @param initialSupply The initial supply of new token.
     * @param uri The token uri.
     * @param sender The account to create the token.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse create(String addressOrAlias, BigInteger tokenId, BigInteger initialSupply, String uri, String sender) throws ApiException {
        return create(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), Numeric.toHexStringWithPrefix(initialSupply), uri, sender);
    }

    /**
     * Create a new token from a specified KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String initialSupply = "0x01";
     * String uri = "https://token-cdn-domain/0x01.json";
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.create(contractAddress, tokenId, initialSupply, uri, sender);
     * }</pre>
     *
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

        return tokenApi.createToken(chainId, addressOrAlias, request);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger initialSupply = BigIntger.valueOf(1000);
     * String uri = "https://token-cdn-domain/0x01.json";
     *
     * caver.kas.kip37.createAsync(contractAddress, tokenId, initialSupply, uri, callback);
     *
     * }</pre>
     *
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
     * Create a new token from a specified KIP-37 contract asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String initialSupply = "0x01";
     * String uri = "https://token-cdn-domain/0x01.json";
     *
     * caver.kas.kip37.createAsync(contractAddress, tokenId, initialSupply, uri, callback);
     *
     * }</pre>
     *
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
     * Create a new token from a specified KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger initialSupply = BigIntger.valueOf(1000);
     * String uri = "https://token-cdn-domain/0x01.json";
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.createAsync(contractAddress, tokenId, initialSupply, uri, sender, callback);
     *
     * }</pre>
     *
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
        return createAsync(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), Numeric.toHexStringWithPrefix(initialSupply), uri, sender, callback);
    }

    /**
     * Create a new token from a specified KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String initialSupply = "0x01";
     * String uri = "https://token-cdn-domain/0x01.json";
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.createAsync(contractAddress, tokenId, initialSupply, uri, sender, callback);
     *
     * }</pre>
     *
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

        return tokenApi.createTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Retrieve a list of KIP-37 tokens.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * Kip37TokenInfoListResponse response = caver.kas.kip37.getTokenList("0x{contractAddress}");
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Kip37TokenInfoListResponse getTokenList(String addressOrAlias) throws ApiException {
        return getTokenList(addressOrAlias, new KIP37QueryOptions());
    }

    /**
     * Retrieve a list of KIP-37 tokens.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * KIP37QueryOptions options = new KIP37QueryOptions();
     * options.setSize(1);
     *
     * Kip37TokenInfoListResponse response = caver.kas.kip37.getTokenList("0x{contractAddress}", options);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor`.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Kip37TokenInfoListResponse getTokenList(String addressOrAlias, KIP37QueryOptions queryOptions) throws ApiException {
        return tokenApi.getTokens(addressOrAlias, chainId, queryOptions.getSize(), queryOptions.getCursor());
    }

    /**
     * Retrieve a list of KIP-37 tokens asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TokenInfoListResponse> callback = new ApiCallback<Kip37TokenInfoListResponse>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.kip37.getTokenListAsync("0x{contractAddress}", callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param callback The callback to handle response.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, ApiCallback<Kip37TokenInfoListResponse> callback) throws ApiException {
        return getTokenListAsync(addressOrAlias, new KIP37QueryOptions(), callback);
    }

    /**
     * Retrieve a list of KIP-37 tokens asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TokenInfoListResponse> callback = new ApiCallback<Kip37TokenInfoListResponse>() {
     *     ....implements callback method
     * };
     *
     * KIP37QueryOptions options = new KIP37QueryOptions();
     * options.setSize(1);
     *
     * caver.kas.kip37.getTokenListAsync("0x{contractAddress}", options, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback to handle response.
     * @return Kip37TokenInfoListResponse
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, KIP37QueryOptions queryOptions, ApiCallback<Kip37TokenInfoListResponse> callback) throws ApiException {
        return tokenApi.getTokensAsync(addressOrAlias, chainId, queryOptions.getSize(), queryOptions.getCursor(), callback);
    }

    /**
     * Burns KIP-37 token.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenId, amount);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.
     * @param amount The token amount to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger id, BigInteger amount) throws ApiException {
        return burn(addressOrAlias, id, amount, null);
    }

    /**
     * Burns KIP-37 token.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenId, amount);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.(in hex, with the 0x prefix)
     * @param amount The token amount to burn.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, String id, String amount) throws ApiException {
        return burn(addressOrAlias, id, amount, null);
    }

    /**
     * Burns KIP-37 token.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     * String from = "0x{fromAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenId, amount, from);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.
     * @param amount The token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger id, BigInteger amount, String from) throws ApiException {
        return burn(addressOrAlias, new BigInteger[]{id}, new BigInteger[]{amount}, from);
    }

    /**
     * Burns KIP-37 tokens.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     * String from = "0x{fromAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenId, amount, from);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.(in hex, with the 0x prefix)
     * @param amount The token amount to burn.(in hex, with the 0x prefix)
     * @param from The owner of the token or the account that authorized to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, String id, String amount, String from) throws ApiException {
        return burn(addressOrAlias, new String[]{id}, new String[]{amount}, from);
    }

    /**
     * Burns KIP-37 tokens.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenIdArray, burnAmountArray);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts) throws ApiException {
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);

        return burn(addressOrAlias, ids_hex, amounts_hex, null);
    }

    /**
     * Burns KIP-37 tokens.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] burnAmountArray = new String[]{"0x01", "0x01"};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenIdArray, burnAmountArray);
     * }</pre>
     *
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
     * Burns KIP-37 tokens.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     * String from = "0x{fromAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenIdArray, burnAmountArray, from);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse burn(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, String from) throws ApiException {
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);

        return burn(addressOrAlias, ids_hex, amounts_hex, from);
    }

    /**
     * Burns KIP-37 tokens.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] burnAmountArray = new String[]{"0x01", "0x01"};
     * String from = "0x{fromAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.burn(contractAddress, tokenIdArray, burnAmountArray, from);
     * }</pre>
     *
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

        return tokenApi.burnToken(chainId, addressOrAlias, request);
    }

    /**
     * Burns KIP-37 tokens asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenId, amount, callback);
     *
     *
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.
     * @param amount The token amount to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger id, BigInteger amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, id, amount, null, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenId, amount, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.(in hex, with the 0x prefix)
     * @param amount The token amount to burn.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String id, String amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, id, amount, null, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     * String from = "0x{fromAddress}";
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenId, amount, from, callback);
     *
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.
     * @param amount The token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger id, BigInteger amount, String from, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, new BigInteger[]{id}, new BigInteger[]{amount}, from, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     * String from = "0x{fromAddress}";
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenId, amount, from, callback);
     *
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param id The token id to burn.(in hex, with the 0x prefix)
     * @param amount The token amount to burn.(in hex, with the 0x prefix)
     * @param from The owner of the token or the account that authorized to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String id, String amount, String from, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, new String[]{id}, new String[]{amount}, from, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenIdArray, burnAmountArray, callback);
     *
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);

        return burnAsync(addressOrAlias, ids_hex, amounts_hex, null, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * It sets a sender to contract deployer.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] burnAmountArray = new String[]{"0x01", "0x01"};
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenIdArray, burnAmountArray, callback);
     * }</pre>
     *
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
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] burnAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     * String from = "0x{fromAddress}";
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenIdArray, burnAmountArray, from, callback);
     *
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ids The array of token id to burn.
     * @param amounts The array of token amount to burn.
     * @param from The owner of the token or the account that authorized to burn.
     * @param callback The callback to handle response.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger[] ids, BigInteger[] amounts, String from, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);

        return burnAsync(addressOrAlias, ids_hex, amounts_hex, from, callback);
    }

    /**
     * Burns KIP-37 tokens asynchronously.
     * DELETE /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] burnAmountArray = new String[]{"0x01", "0x01"};
     * String from = "0x{fromAddress}";
     *
     * caver.kas.kip37.burnAsync(contractAddress, tokenIdArray, burnAmountArray, from, callback);
     * }</pre>
     *
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

        return tokenApi.burnTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Mint token for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenId, amount);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id The token id to mint additionally.
     * @param amount The token amount to mint additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger id, BigInteger amount) throws ApiException {
        return mint(addressOrAlias, to, id, amount, null);
    }

    /**
     * Mint token for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenId, amount);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id The token id to mint additionally.(in hex, with the 0x prefix)
     * @param amount The token amount to mint additionally.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, String id, String amount) throws ApiException {
        return mint(addressOrAlias, to, id, amount, null);
    }

    /**
     * Mint token for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenId, amount, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id The token id to mint additionally.
     * @param amount The token amount to mint additionally.
     * @param sender The account address to mint token additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger id, BigInteger amount, String sender) throws ApiException {
        return mint(addressOrAlias, to, new BigInteger[]{id}, new BigInteger[]{amount}, sender);
    }

    /**
     * Mint token for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenId, amount, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amount Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param sender The account address to mint token additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, String id, String amount, String sender) throws ApiException {
        return mint(addressOrAlias, to, new String[]{id}, new String[]{amount}, sender);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenIdArray, mintAmountArray);
     * }</pre>
     *
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
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] mintAmountArray = new String[]{"0x01", "0x01"};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenIdArray, mintAmountArray);
     * }</pre>
     *
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
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenIdArray, mintAmountArray, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param ids Array of the token ids to mint additionally.
     * @param amounts Array of the token amount to mint additionally.
     * @param sender The account address to mint token additionally.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger[] ids, BigInteger[] amounts, String sender) throws ApiException {
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        return mint(addressOrAlias, to, ids_hex, amounts_hex, sender);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] mintAmountArray = new String[]{"0x01", "0x01"};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.mint(contractAddress, to, tokenIdArray, mintAmountArray, sender);
     * }</pre>
     *
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
        return tokenApi.mintToken(chainId, addressOrAlias, request);
    }

    /**
     * Mint token for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenId, amount. callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id Array of the token ids to mint additionally.
     * @param amount Array of the token amount to mint additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger id, BigInteger amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, id, amount, null, callback);
    }

    /**
     * Mint token for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenId, amount, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amount Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String id, String amount, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, id, amount, null, callback);
    }

    /**
     * Mint token for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInteger amount = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenId, amount, sender, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id Array of the token ids to mint additionally.
     * @param amount Array of the token amount to mint additionally.
     * @param sender The account address to mint token additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger id, BigInteger amount, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, new BigInteger[]{id}, new BigInteger[]{amount}, sender, callback);
    }

    /**
     * Mint token for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * String tokenId = "0x01";
     * String amount = "0x01";
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenId, amount, sender, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param to The account address of the token owner.
     * @param id Array of the token ids to mint additionally.(in hex, with the 0x prefix)
     * @param amount Array of the token amount to mint additionally.(in hex, with the 0x prefix)
     * @param sender The account address to mint token additionally.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String id, String amount, String sender, ApiCallback<Kip37TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, new String[]{id}, new String[]{amount}, sender, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenIdArray, mintAmountArray, callback);
     * }</pre>
     *
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
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] mintAmountArray = new String[]{"0x01", "0x01"};
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenIdArray, mintAmountArray, callback);
     * }</pre>
     *
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
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] mintAmountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenIdArray, mintAmountArray, sender, callback);
     * }</pre>
     *
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
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        return mintAsync(addressOrAlias, to, ids_hex, amounts_hex, sender, callback);
    }

    /**
     * Mint multiple tokens for a given KIP-37 contract asynchronously. Minting is possible after having created a token with {@link KIP37#create(String, BigInteger, BigInteger, String, String)}<br>
     * POST /v1/contract/{contract-address-or-alias}/token/mint
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String to = "0x{toAddress}";
     * String sender = "0x{senderAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] mintAmountArray = new String[]{"0x01", "0x01"};
     *
     * caver.kas.kip37.mintAsync(contractAddress, to, tokenIdArray, mintAmountArray, sender, callback);
     * }</pre>
     *
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
        return tokenApi.mintTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends a token for a given KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInger amount = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(contractAddress, sender, owner, to, tokenId, amount);
     * }</pre>
     *
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
     * Sends a token for a given KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * String amount = "0x1";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(contractAddress, sender, owner, to, tokenId, amount);
     * }</pre>
     *
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
     * Sends multiple tokens for a given KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] amountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(contractAddress, sender, owner, to, tokenIdArray, amountArray);
     * }</pre>
     *
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
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        return transfer(addressOrAlias, sender, owner, to, ids_hex, amounts_hex);
    }

    /**
     * Sends multiple tokens for a given KIP-37 contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] amountArray = new String[]{"0x01", "0x01"};
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.transfer(contractAddress, sender, owner, to, tokenIdArray, amountArray);
     * }</pre>
     *
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

        return tokenApi.transferToken(chainId, addressOrAlias, request);
    }

    /**
     * Sends a token for a given KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * BigInger amount = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.transferAsync(contractAddress, sender, owner, to, tokenId, amount, callback);
     *
     * }</pre>
     *
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
     * Sends a token for a given KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * String amount = "0x1";
     *
     * caver.kas.kip37.transferAsync(contractAddress, sender, owner, to, tokenId, amount, callback);
     *
     * }</pre>
     *
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
     * Sends multiple tokens for a given KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIdArray = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)};
     * BigInteger[] amountArray = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
     *
     * caver.kas.kip37.transferAsync(contractAddress, sender, owner, to, tokenIdArray, amountArray, callback);
     * }</pre>
     *
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
        String[] ids_hex = Arrays.stream(ids).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        String[] amounts_hex = Arrays.stream(amounts).map(Numeric::toHexStringWithPrefix).toArray(String[]::new);
        return transferAsync(addressOrAlias, sender, owner, to, ids_hex, amounts_hex, callback);
    }


    /**
     * Sends multiple tokens for a given KIP-37 contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/transfer
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String sender = "0x{senderAddress}";
     * String owner = "0x{ownerAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIdArray = new String[]{"0x01", "0x02"};
     * String[] amountArray = new String[]{"0x01", "0x01"};
     *
     * caver.kas.kip37.transferAsync(contractAddress, sender, owner, to, tokenIdArray, amountArray, callback);
     * }</pre>
     *
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

        return tokenApi.transferTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, BigInteger tokenId) throws ApiException {
        return pauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, String tokenId) throws ApiException {
        return pauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}<br>
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(contractAddress, tokenId, sender);
     * }</pre>
     *
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
     * Pause the operations of a specified token, such as minting and creating tokens.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}<br>
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.pauseToken(contractAddress, tokenId, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse pauseToken(String addressOrAlias, String tokenId, String sender) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return tokenApi.pauseToken(chainId, addressOrAlias, tokenId, request);
    }

    /**
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.pauseTokenAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
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
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.kip37.pauseTokenAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
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
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.pauseTokenAsync(contractAddress, tokenId, sender, callback);
     * }</pre>
     *
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
     * Pause the operations of a specified token, such as minting and creating tokens asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/pause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.pauseTokenAsync(contractAddress, tokenId, sender, callback);
     * }</pre>
     *
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

        return tokenApi.pauseTokenAsync(chainId, addressOrAlias, tokenId, request, callback);
    }

    /**
     * Resume paused token operation for a given contract.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, BigInteger tokenId) throws ApiException {
        return unpauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Resume paused token operation for a given contract.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, String tokenId) throws ApiException {
        return unpauseToken(addressOrAlias, tokenId, null);
    }

    /**
     * Resume paused token operation for a given contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(contractAddress, tokenId, sender);
     * }</pre>
     *
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
     * Resume paused token operation for a given contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     * String sender = "0x{senderAddress}";
     *
     * Kip37TransactionStatusResponse response = caver.kas.kip37.unpauseToken(contractAddress, tokenId, sender);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param tokenId The token id to pause operation.(in hex, with the 0x prefix)
     * @param sender The account address to execute token operations.
     * @return Kip37TransactionStatusResponse
     * @throws ApiException
     */
    public Kip37TransactionStatusResponse unpauseToken(String addressOrAlias, String tokenId, String sender) throws ApiException {
        OperateKip37ContractRequest request = new OperateKip37ContractRequest();
        request.setSender(sender);

        return tokenApi.unpauseToken(chainId, addressOrAlias, tokenId, request);
    }

    /**
     * Resume paused token operation for a given contract asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip37.unpauseTokenAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
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
     * Resume paused token operation for a given contract asynchronously.<br>
     * It sets a sender to contract deployer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.kip37.unpauseTokenAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
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
     * Resume paused token operation for a given contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.unpauseTokenAsync(contractAddress, tokenId, sender, callback);
     * }</pre>
     *
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
     * Resume paused token operation for a given contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/unpause/{token-id}
     *
     * <pre>{@code
     * ApiCallback<Kip37TransactionStatusResponse> callback = new ApiCallback<Kip37TransactionStatusResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     * String sender = "0x{senderAddress}";
     *
     * caver.kas.kip37.unpauseTokenAsync(contractAddress, tokenId, sender, callback);
     * }</pre>
     *
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

        return tokenApi.unpauseTokenAsync(chainId, addressOrAlias, tokenId, request, callback);
    }

    /**
     * Retrieves a list of tokens owned by a certain account.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * Kip37TokenListResponse response = caver.kas.kip37.getTokenListByOwner(contractAddress, ownerAddress);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @return Kip37TokenListResponse
     * @throws ApiException
     */
    public Kip37TokenListResponse getTokenListByOwner(String addressOrAlias, String ownerAddress) throws ApiException {
        return getTokenListByOwner(addressOrAlias, ownerAddress, new KIP37QueryOptions());
    }

    /**
     * Retrieves a list of tokens owned by a certain account.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     *
     * <pre>{@code
     * KIP37QueryOptions queryOptions = new KIP37QueryOptions();
     * queryOptions.setSize(1);
     *
     * String contractAddress = "0x{contractAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * Kip37TokenListResponse response = caver.kas.kip37.getTokenListByOwner(contractAddress, ownerAddress, queryOptions);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor` and `status`
     * @return Kip37TokenListResponse
     * @throws ApiException
     */
    public Kip37TokenListResponse getTokenListByOwner(String addressOrAlias, String ownerAddress, KIP37QueryOptions queryOptions) throws ApiException {
        return tokenOwnershipApi.getTokensByOwner(addressOrAlias, ownerAddress, chainId, queryOptions.getSize(), queryOptions.getCursor());
    }

    /**
     * Retrieves a list of tokens owned by a certain account asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TokenListResponse> callback = new ApiCallback<Kip37TokenListResponse>() {
     *     ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * caver.kas.kip37.getTokenListByOwnerAsync(contractAddress, ownerAddress, callback);
     * }</pre>
     *
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
     * Retrieves a list of tokens owned by a certain account asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}/token
     *
     * <pre>{@code
     * ApiCallback<Kip37TokenListResponse> callback = new ApiCallback<Kip37TokenListResponse>() {
     *     ....implements callback method
     * };
     *
     * KIP37QueryOptions queryOptions = new KIP37QueryOptions();
     * queryOptions.setSize(1);
     *
     * String contractAddress = "0x{contractAddress}";
     * String ownerAddress = "0x{ownerAddress}";
     *
     * caver.kas.kip37.getTokenListByOwnerAsync(contractAddress, ownerAddress, queryOptions, callback);
     * }</pre>
     *
     * @param addressOrAlias The contract address(in hex, with the 0x prefix) or alias.
     * @param ownerAddress The owner account to query.
     * @param queryOptions Filters required when retrieving data. `size`, `cursor` and `status`
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String addressOrAlias, String ownerAddress, KIP37QueryOptions queryOptions, ApiCallback<Kip37TokenListResponse> callback) throws ApiException {
        return tokenOwnershipApi.getTokensByOwnerAsync(addressOrAlias, ownerAddress, chainId, queryOptions.getSize(), queryOptions.getCursor(), callback);
    }

    /**
     * Retrieves a contract deployer account.<br>
     * GET /v1/deployer/default
     *
     * <pre>{@code
     * Kip37DeployerResponse response = caver.kas.kip37.getDeployer();
     * }</pre>
     *
     * @return Kip37DeployerResponse
     * @throws ApiException
     */
    public Kip37DeployerResponse getDeployer() throws ApiException {
        return deployerApi.getDefaultDeployer(chainId);
    }

    /**
     * Retrieving a contract deployer account asynchronously.<br>
     * GET /v1/deployer/default
     *
     * <pre>{@code
     * ApiCallback<Kip37DeployerResponse> callback = new ApiCallback<Kip37DeployerResponse>() {
     *     ....implements callback method
     * };
     *
     * caver.kas.kip37.getDeployerAsync(callback);
     * }</pre>
     *
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getDeployerAsync(ApiCallback<Kip37DeployerResponse> callback) throws ApiException {
        return deployerApi.getDefaultDeployerAsync(chainId, callback);
    }

    /**
     * Getter function for contractApi
     * @return ContractApi
     */
    public ContractApi getContractApi() {
        return contractApi;
    }

    /**
     * Setter function for contractApi
     * @param contractApi KIP-37 contract API rest-client object.
     */
    public void setContractApi(ContractApi contractApi) {
        this.contractApi = contractApi;
    }

    /**
     * Getter function for deployerApi
     * @return DeployerApi
     */
    public DeployerApi getDeployerApi() {
        return deployerApi;
    }

    /**
     * Setter function for deployerApi
     * @param deployerApi KIP-37 deployer API rest-client object.
     */
    public void setDeployerApi(DeployerApi deployerApi) {
        this.deployerApi = deployerApi;
    }

    /**
     * Getter function for tokenApi
     * @return TokenApi
     */
    public TokenApi getTokenApi() {
        return tokenApi;
    }

    /**
     * Setter function for tokenApi
     * @param tokenApi KIP-37 token API rest-client object.
     */
    public void setTokenApi(TokenApi tokenApi) {
        this.tokenApi = tokenApi;
    }

    /**
     * Getter function for tokenOwnershipApi.
     * @return TokenOwnershipApi
     */
    public TokenOwnershipApi getTokenOwnershipApi() {
        return tokenOwnershipApi;
    }

    /**
     * Setter function for tokenOwnershipApi
     * @param tokenOwnershipApi KIP-37 token ownership API rest-client object.
     */
    public void setTokenOwnershipApi(TokenOwnershipApi tokenOwnershipApi) {
        this.tokenOwnershipApi = tokenOwnershipApi;
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
        setContractApi(new ContractApi(apiClient));
        setDeployerApi(new DeployerApi(apiClient));
        setTokenApi(new TokenApi(apiClient));
        setTokenOwnershipApi(new TokenOwnershipApi(apiClient));
    }
}
