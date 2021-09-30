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

package xyz.groundx.caver_ext_kas.kas.kip17;

import com.squareup.okhttp.Call;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.api.Kip17ContractApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.api.Kip17TokenApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.*;

import java.math.BigInteger;

public class KIP17 {
    /**
     * KIP-17 contract API rest-client object.
     */
    Kip17ContractApi kip17ContractApi;

    /**
     * KIP-17 token API rest-client object.
     */
    Kip17TokenApi kip17TokenApi;

    /**
     * Klaytn network id.
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * Creates an KIP17API instance.
     * @param chainId A Klaytn network chain id.
     * @param client The Api client for connection with KAS.
     */
    public KIP17(String chainId, ApiClient client) {
        setChainId(chainId);
        setApiClient(client);
    }

    /**
     * Deploys a new KIP-17 contract with user submitted parameters.<br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String name = "testKIP-17";
     * String symbol = "TKIP17";
     * String alias = "kip-17test";
     * Kip17TransactionStatusResponse res = caver.kas.kip17.deploy(name, symbol, alias);
     * }
     * </pre>
     *
     * @param name The KIP-17 contract name.
     * @param symbol The KIP-17 contract symbol.
     * @param alias The KIP-17 contract alias.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17DeployResponse deploy(String name, String symbol, String alias) throws ApiException {
        return deploy(name, symbol, alias, null);
    }

    /**
     * Deploys a new KIP-17 contract with user submitted parameters.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip17/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String name = "testKIP-17";
     * String symbol = "TKIP17";
     * String alias = "kip-17test";
     *
     * Kip17FeePayerOptions option = new Kip17FeePayerOptions();
     * option.setEnableGlobalFeePayer(true);
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.deploy(name, symbol, alias, option);
     * }
     * </pre>
     *
     * @param name The KIP-17 contract name.
     * @param symbol The KIP-17 contract symbol.
     * @param alias The KIP-17 contract alias.
     * @param options The feePayer options that config to pay transaction fee logic.
     * @return Kip17DeployResponse
     * @throws ApiException
     */
    public Kip17DeployResponse deploy(String name, String symbol, String alias, Kip17FeePayerOptions options) throws ApiException {
        DeployKip17ContractRequest request = new DeployKip17ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setAlias(alias);
        request.setOptions(options);


        return kip17ContractApi.deployContract(chainId, request);
    }

    /**
     * Deploys a new KIP-17 contract with user submitted parameters asynchronously.<br>
     * POST /v1/contract
     *
     * <pre>Example :
     *
     * {@code
     * ApiCallback<Kip17DeployResponse> callback = new ApiCallback<Kip17DeployResponse>() {
     *     ....implements callback method
     * };
     * String name = "testKIP-17";
     * String symbol = "TKIP17";
     * String alias = "kip-17test";
     * caver.kas.kip17.deployAsync(name, symbol, alias, callback);
     * }
     * </pre>
     *
     * @param name The KIP-17 contract name.
     * @param symbol The KIP-17 contract symbol.
     * @param alias The KIP-17 contract alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, String alias, ApiCallback<Kip17DeployResponse> callback) throws ApiException {
        return deployAsync(name, symbol, alias, null, callback);
    }

    /**
     * Deploys a new KIP-17 contract with user submitted parameters asynchronously.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip17/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     *
     * {@code
     * ApiCallback<Kip17DeployResponse> callback = new ApiCallback<Kip17DeployResponse>() {
     *     ....implements callback method
     * };
     * String name = "testKIP-17";
     * String symbol = "TKIP17";
     * String alias = "kip-17test";
     *
     * Kip17FeePayerOptions option = new Kip17FeePayerOptions();
     * option.setEnableGlobalFeePayer(true);
     *
     * caver.kas.kip17.deployAsync(name, symbol, alias, option, callback);
     * }
     * </pre>
     *
     * @param name The KIP-17 contract name.
     * @param symbol The KIP-17 contract symbol.
     * @param alias The KIP-17 contract alias.
     * @param options The feePayer options that config to pay transaction fee logic.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, String alias, Kip17FeePayerOptions options, ApiCallback<Kip17DeployResponse> callback) throws ApiException {
        DeployKip17ContractRequest request = new DeployKip17ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setAlias(alias);
        request.setOptions(options);

        return kip17ContractApi.deployContractAsync(chainId, request, callback);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip17/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * Kip17ContractInfoResponse response = caver.kas.kip17.updateContractOptions(contractAddress);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @return Kip17ContractInfoResponse
     * @throws ApiException
     */
    public Kip17ContractInfoResponse updateContractOptions(String addressOrAlias) throws ApiException {
        return updateContractOptions(addressOrAlias, null);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip17/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * Kip17FeePayerOptions option = new Kip17FeePayerOptions();
     * option.setEnableGlobalFeePayer(true);
     *
     * Kip17ContractInfoResponse response = caver.kas.kip17.updateContractOptions(contractAddress, option);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param options The feePayer options that config to pay transaction fee logic.
     * @return Kip17ContractInfoResponse
     * @throws ApiException
     */
    public Kip17ContractInfoResponse updateContractOptions(String addressOrAlias, Kip17FeePayerOptions options) throws ApiException {
        UpdateKip17ContractRequest request = new UpdateKip17ContractRequest();
        request.setOptions(options);


        return kip17ContractApi.updateContract(chainId, addressOrAlias, request);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * ApiCallback<Kip17ContractInfoResponse> callback = new ApiCallback<Kip17ContractInfoResponse>() {
     *   ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * caver.kas.kip17.updateContractOptionsAsync(contractAddress, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param callback The callback to handle response.
     * @return Kip17ContractInfoResponse
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, ApiCallback<Kip17ContractInfoResponse> callback) throws ApiException {
        return updateContractOptionsAsync(addressOrAlias, null, callback);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip17/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * ApiCallback<Kip17ContractInfoResponse> callback = new ApiCallback<Kip17ContractInfoResponse>() {
     *   ....implements callback method
     * };
     *
     * String contractAddress = "0x{contractAddress}";
     * Kip17FeePayerOptions option = new Kip17FeePayerOptions();
     * option.setEnableGlobalFeePayer(true);
     *
     * caver.kas.kip17.updateContractOptionsAsync(contractAddress, option, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param options The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Kip17ContractInfoResponse
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, Kip17FeePayerOptions options, ApiCallback<Kip17ContractInfoResponse> callback) throws ApiException {
        UpdateKip17ContractRequest request = new UpdateKip17ContractRequest();
        request.setOptions(options);

        return kip17ContractApi.updateContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Get all deployed contract list in the requested deployer pool.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * Kip17ContractListResponse response = caver.kas.kip17.getContractList();
     * }
     * </pre>
     *
     * @return Kip17ContractListResponse
     * @throws ApiException
     */
    public Kip17ContractListResponse getContractList() throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getContractList(options);
    }

    /**
     * Get all deployed contract list in the requested deployer pool.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     *
     * Kip17ContractListResponse response = caver.kas.kip17.getContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return Kip17ContractListResponse
     * @throws ApiException
     */
    public Kip17ContractListResponse getContractList(KIP17QueryOptions options) throws ApiException {
        String size = options.getSize() != null ? Long.toString(options.getSize()) : null;
        return kip17ContractApi.listContractsInDeployerPool(chainId, size, options.getCursor());
    }

    /**
     * Get all deployed contract list in the requested deployer pool asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17ContractListResponse> callback = new ApiCallback<Kip17ContractListResponse>() {
     *   ....implements callback method
     * };
     *
     * Kip17ContractListResponse response = caver.kas.kip17.getContractListAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractListAsync(ApiCallback<Kip17ContractListResponse> callback) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getContractListAsync(options, callback);
    }

    /**
     * Get all deployed contract list in the requested deployer pool asynchronously.<br>
     * GET /v1/contract
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17ContractListResponse> callback = new ApiCallback<Kip17ContractListResponse>() {
     *   ....implements callback method
     * };
     *
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * caver.kas.kip17.getContractListAsync(options, callback);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractListAsync(KIP17QueryOptions options, ApiCallback<Kip17ContractListResponse> callback) throws ApiException {
        String size = options.getSize() != null ? Long.toString(options.getSize()) : null;
        return kip17ContractApi.listContractsInDeployerPoolAsync(chainId, size, options.getCursor(), callback);
    }

    /**
     * Retrieves KIP-17 contract information by either contract address or alias.<br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * Kip17ContractInfoResponse response = caver.kas.kip17.getContract(address);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @return Kip17ContractInfoResponse
     * @throws ApiException
     */
    public Kip17ContractInfoResponse getContract(String addressOrAlias) throws ApiException {
        return kip17ContractApi.getContract(chainId, addressOrAlias);
    }

    /**
     * Retrieves KIP-17 contract information by either contract address or alias asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17ContractInfoResponse> callback = new ApiCallback<Kip17ContractInfoResponse>() {
     *   ....implements callback method
     * };
     *
     * String address = "0x{address}";
     * caver.kas.kip17.getContractAsync(address, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractAsync(String addressOrAlias, ApiCallback<Kip17ContractInfoResponse> callback) throws ApiException {
        return kip17ContractApi.getContractAsync(chainId, addressOrAlias, callback);
    }

    /**
     * Mint a new KIP-17 token.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre> Example :
     * {@code
     * String address = "0x{contract_address}";
     * String to = "0x{to_address}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String uri = "uri";
     *
     * Kip17TransactionStatusResponse response = caver.kas.kip17.mint(address, to, tokenId, uri, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param to The recipient EOA address for the newly minted token.
     * @param id The Token ID for the newly minted token.
     * @param uri The Token URI for the newly minted token.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger id, String uri) throws ApiException {
        return mint(addressOrAlias, to, Numeric.toHexStringWithPrefix(id), uri);
    }

    /**
     * Mint a new KIP-17 token.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre> Example :
     * {@code
     * String address = "0x{contract_address}";
     * String to = "0x{to_address}";
     * String tokenId = "0x1"
     * String uri = "uri";
     *
     * Kip17TransactionStatusResponse response = caver.kas.kip17.mint(address, to, tokenId, uri);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param to The recipient EOA address for the newly minted token.
     * @param id The Token ID for the newly minted token.
     * @param uri The Token URI for the newly minted token.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse mint(String addressOrAlias, String to, String id, String uri) throws ApiException {
        MintKip17TokenRequest request = new MintKip17TokenRequest();
        request.setId(id);
        request.setTo(to);
        request.setUri(uri);

        return kip17TokenApi.mintToken(chainId, addressOrAlias, request, null);
    }

    /**
     * Mint a new KIP-17 token asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre> Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *  ....implements callback method
     * };
     *
     * String address = "0x{contract_address}";
     * String to = "0x{to_address}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String uri = "uri";
     *
     * caver.kas.kip17.mintAsync(address, to, tokenId, uri, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param to The recipient EOA address for the newly minted token.
     * @param id The Token ID for the newly minted token.
     * @param uri The Token URI for the newly minted token.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger id, String uri, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, Numeric.toHexStringWithPrefix(id), uri, callback);
    }

    /**
     * Mint a new KIP-17 token asynchronously.
     * POST /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *   ....implements callback method
     * };
     *
     * String address = "0x{contract_address}";
     * String to = "0x{to_address}";
     * String tokenId = "0x1"
     * String uri = "uri";
     *
     * caver.kas.kip17.mintAsync(address, to, tokenId, uri, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param to The recipient EOA address for the newly minted token.
     * @param id The Token ID for the newly minted token.
     * @param uri The Token URI for the newly minted token.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String id, String uri, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        MintKip17TokenRequest request = new MintKip17TokenRequest();
        request.setId(id);
        request.setTo(to);
        request.setUri(uri);

        return kip17TokenApi.mintTokenAsync(chainId, addressOrAlias, request, null, callback);
    }

    /**
     * Get all token list minted from a specified KIP-17 contract.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>Example :
     * {@code
     *  String contractAddress = "0x{address}";
     *  Kip17TokenListResponse res = caver.kas.kip17.getTokenList(contractAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @return Kip17TokenListResponse
     * @throws ApiException
     */
    public Kip17TokenListResponse getTokenList(String addressOrAlias) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTokenList(addressOrAlias, options);
    }

    /**
     * Get all token list minted from a specified KIP-17 contract.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>Example :
     * {@code
     *  KIP17QueryOptions options = new KIP17QueryOptions();
     *  options.setSize(1);
     *  options.setCursor("cursor data");
     *
     *  Kip17TokenListResponse res = caver.kas.kip17.getTokenList(contractAddress, options);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return Kip17TokenListResponse
     * @throws ApiException
     */
    public Kip17TokenListResponse getTokenList(String addressOrAlias, KIP17QueryOptions options) throws ApiException {
        return kip17TokenApi.listTokens(chainId, addressOrAlias, options.getSize(), options.getCursor());
    }

    /**
     * Get all token list minted from a specified KIP-17 contract asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TokenListResponse> callback = new ApiCallback<Kip17TokenListResponse>() {
     *      ....implements callback method.
     * };
     *
     * String contractAddress = "0x{address};
     * caver.kas.kip17.getTokenListAsync(contractAddress, callback);
     * }
     *
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, ApiCallback<Kip17TokenListResponse> callback) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTokenListAsync(addressOrAlias, options, callback);
    }

    /**
     * Get all token list minted from a specified KIP-17 contract asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/token
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TokenListResponse> callback = new ApiCallback<Kip17TokenListResponse>() {
     *      ....implements callback method.
     * };
     *
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursor data");
     *
     * String contractAddress = "0x{address};
     * caver.kas.kip17.getTokenListAsync(contractAddress, options, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListAsync(String addressOrAlias, KIP17QueryOptions options, ApiCallback<Kip17TokenListResponse> callback) throws ApiException {
        return kip17TokenApi.listTokensAsync(chainId, addressOrAlias, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Retrieves a token information.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String contractAddress = "0x{address}";
     *
     * GetKip17TokenResponse = caver.kas.kip17.getToken(contractAddress, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id to retrieve.
     * @return GetKip17TokenResponse
     * @throws ApiException
     */
    public GetKip17TokenResponse getToken(String addressOrAlias, BigInteger tokenId) throws ApiException {
        return getToken(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId));
    }

    /**
     * Retrieves a token information.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String tokenId = "0x1"
     * String contractAddress = "0x{address}";
     *
     * GetKip17TokenResponse = caver.kas.kip17.getToken(contractAddress, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id to retrieve.
     * @return GetKip17TokenResponse
     * @throws ApiException
     */
    public GetKip17TokenResponse getToken(String addressOrAlias, String tokenId) throws ApiException {
        return kip17TokenApi.getToken(chainId, addressOrAlias, tokenId);
    }

    /**
     * Retrieves a token information asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TokenListResponse> callback = ApiCallback<Kip17TokenListResponse>() {
     *     ...implements callback method
     * };
     *
     * BigInteger tokenId = BigInteger.valueOf(1);
     * String contractAddress = "0x{address}";
     *
     * caver.kas.kip17.getTokenAsync(contractAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id to retrieve.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenAsync(String addressOrAlias, BigInteger tokenId, ApiCallback<GetKip17TokenResponse> callback) throws ApiException {
        return getTokenAsync(addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), callback);
    }

    /**
     * Retrieves a token information asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TokenListResponse> callback = ApiCallback<Kip17TokenListResponse>() {
     *     ...implements callback method
     * };
     *
     * String tokenId = "0x1";
     * String contractAddress = "0x{address}";
     *
     * caver.kas.kip17.getTokenAsync(contractAddress, tokenId, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id to retrieve.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenAsync(String addressOrAlias, String tokenId, ApiCallback<GetKip17TokenResponse> callback) throws ApiException {
        return kip17TokenApi.getTokenAsync(chainId, addressOrAlias, tokenId, callback);
    }

    /**
     * Transfer a token.<br>
     * If sender and owner are no the same, then sender must have been approved for this token transfer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String sender = "0x{senderAddr}";
     * String owner = "0x{ownerAddr}";
     * String to = "0x{toAddr}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.transfer(contractAddress, sender, owner, to, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param sender The sender address.
     * @param owner The owner address.
     * @param to The recipient address.
     * @param tokenId The Token ID
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, BigInteger tokenId) throws ApiException {
        return transfer(addressOrAlias, sender, owner, to, Numeric.toHexStringWithPrefix(tokenId));
    }

    /**
     * Transfer a token.<br>
     * If sender and owner are no the same, then sender must have been approved for this token transfer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String sender = "0x{senderAddr}";
     * String owner = "0x{ownerAddr}";
     * String to = "0x{toAddr}";
     * String tokenId = "0x1";
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.transfer(contractAddress, sender, owner, to, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param sender The sender address.
     * @param owner The owner address.
     * @param to The recipient address.
     * @param tokenId The Token ID
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse transfer(String addressOrAlias, String sender, String owner, String to, String tokenId) throws ApiException {
        TransferKip17TokenRequest request = new TransferKip17TokenRequest();
        request.setSender(sender);
        request.setOwner(owner);
        request.setTo(to);

        return kip17TokenApi.transferToken(chainId, addressOrAlias, tokenId, request, null);
    }

    /**
     * Transfer a token asynchronously.<br>
     * If sender and owner are no the same, then sender must have been approved for this token transfer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>();
     *  ....implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String sender = "0x{senderAddr}";
     * String owner = "0x{ownerAddr}";
     * String to = "0x{toAddr}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip17.transferAsync(contractAddress, sender, owner, to, tokenId, callback);
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param sender The sender address.
     * @param owner The owner address.
     * @param to The recipient address.
     * @param tokenId The Token ID.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, BigInteger tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, sender, owner, to, Numeric.toHexStringWithPrefix(tokenId), callback);
    }

    /**
     * Transfer a token asynchronously.<br>
     * If sender and owner are no the same, then sender must have been approved for this token transfer.<br>
     * POST /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>();
     *  ....implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String sender = "0x{senderAddr}";
     * String owner = "0x{ownerAddr}";
     * String to = "0x{toAddr}";
     * String tokenId = "0x1";
     *
     * caver.kas.kip17.transferAsync(contractAddress, sender, owner, to, tokenId, callback);
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param sender The sender address.
     * @param owner The owner address.
     * @param to The recipient address.
     * @param tokenId The token ID.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String sender, String owner, String to, String tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        TransferKip17TokenRequest request = new TransferKip17TokenRequest();
        request.setSender(sender);
        request.setOwner(owner);
        request.setTo(to);

        return kip17TokenApi.transferTokenAsync(chainId, addressOrAlias, tokenId, request, null, callback);
    }

    /**
     * Burn a token.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.burn(contractAddress, from, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param tokenId The token ID.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse burn(String addressOrAlias, String from, BigInteger tokenId) throws ApiException {
        return burn(addressOrAlias, from, Numeric.toHexStringWithPrefix(tokenId));
    }

    /**
     * Burn a token.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String tokenId = "0x1"
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.burn(contractAddress, from, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param tokenId The token ID.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse burn(String addressOrAlias, String from, String tokenId) throws ApiException {
        BurnKip17TokenRequest request = new BurnKip17TokenRequest();
        request.setFrom(from);
        return kip17TokenApi.burnToken(chainId, addressOrAlias, tokenId, request, null);
    }

    /**
     * Burn a token asynchronously.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *    // implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     * }
     *
     * caver.kas.kip17.burnAsync(contractAddress, from, tokenId, callback);
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param tokenId The token ID.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String from, BigInteger tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, from, Numeric.toHexStringWithPrefix(tokenId), callback);
    }

    /**
     * Burn a token asynchronously.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/token/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *    // implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String tokenId = "0x1";
     * }
     *
     * caver.kas.kip17.burnAsync(contractAddress, from, tokenId, callback);
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param tokenId The token ID.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String from, String tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        BurnKip17TokenRequest request = new BurnKip17TokenRequest();
        request.setFrom(from);
        return kip17TokenApi.burnTokenAsync(chainId, addressOrAlias, tokenId, request, null, callback);
    }

    /**
     * Approves an EOA, to, to perform token operations on a particular token of a contract which from owns.<br>
     * If from is not the owner, then the transaction submitted from this API will be reverted.<br>
     * POST /v1/contract/{contract-address-or-alias}/approve/{token-id}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String to = "0x{toAddr}"
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.approve(contractAddress, from, to, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param tokenId The token id to approve.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse approve(String addressOrAlias, String from, String to, BigInteger tokenId) throws ApiException {

        return approve(addressOrAlias, from, to, Numeric.toHexStringWithPrefix(tokenId));
    }

    /**
     * Approves an EOA, to, to perform token operations on a particular token of a contract which from owns.<br>
     * If from is not the owner, then the transaction submitted from this API will be reverted.<br>
     * POST /v1/contract/{contract-address-or-alias}/approve/{token-id}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String to = "0x{toAddr}"
     * String tokenId = "0x1";
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.approve(contractAddress, from, to, tokenId);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param tokenId The token id to approve.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse approve(String addressOrAlias, String from, String to, String tokenId) throws ApiException {
        ApproveKip17TokenRequest request = new ApproveKip17TokenRequest();
        request.setFrom(from);
        request.setTo(to);

        return kip17TokenApi.approveToken(chainId, addressOrAlias, tokenId, request, null);
    }

    /**
     * Approves an EOA, to, to perform token operations on a particular token of a contract which from owns asynchronously.<br>
     * If from is not the owner, then the transaction submitted from this API will be reverted.<br>
     * POST /v1/contract/{contract-address-or-alias}/approve/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *     ...implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String to = "0x{toAddr}"
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip17.approveAsync(contractAddress, from, to, tokenId, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param tokenId The token id to approve.
     * @param callback The callback function to handle response.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String from, String to, BigInteger tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        return approveAsync(addressOrAlias, from, to, Numeric.toHexStringWithPrefix(tokenId), callback);
    }

    /**
     * Approves an EOA, to, to perform token operations on a particular token of a contract which from owns asynchronously.<br>
     * If from is not the owner, then the transaction submitted from this API will be reverted.<br>
     * POST /v1/contract/{contract-address-or-alias}/approve/{token-id}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *     ...implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddr}";
     * String from = "0x{fromAddr}";
     * String to = "0x{toAddr}"
     * String tokenId = "0x1";
     *
     * caver.kas.kip17.approveAsync(contractAddress, from, to, tokenId, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param tokenId The token id to approve.
     * @param callback The callback function to handle response.
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String from, String to, String tokenId, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        ApproveKip17TokenRequest request = new ApproveKip17TokenRequest();
        request.setFrom(from);
        request.setTo(to);

        return kip17TokenApi.approveTokenAsync(chainId, addressOrAlias, tokenId, request, null, callback);
    }

    /**
     * Approves an EOA, to, to perform token operations on all token of a contract which from owns.<br>
     * POST /v1/contract/{contract-address-or-alias}/approveall
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.approveAll(contractAddress, from, to, true);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param approved True if the operator is approved, false to revoke approval
     * @return Kip17TransactionStatusResponse
     * @throws ApiException
     */
    public Kip17TransactionStatusResponse approveAll(String addressOrAlias, String from, String to, boolean approved) throws ApiException {
        ApproveAllKip17Request request = new ApproveAllKip17Request();
        request.setFrom(from);
        request.setTo(to);
        request.setApproved(approved);

        return kip17TokenApi.approveAll(chainId, addressOrAlias, request, null);
    }

    /**
     * Approves an EOA, to, to perform token operations on all token of a contract which from owns asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/approveall
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip17TransactionStatusResponse> callback = new ApiCallback<Kip17TransactionStatusResponse>() {
     *     ...implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     *
     * Kip17TransactionStatusResponse res = caver.kas.kip17.approveAllAsync(contractAddress, from, to, true, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param from The token owner address.
     * @param to The EOA address receiving the approval permission.
     * @param approved True if the operator is approved, false to revoke approval
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call approveAllAsync(String addressOrAlias, String from, String to, boolean approved, ApiCallback<Kip17TransactionStatusResponse> callback) throws ApiException {
        ApproveAllKip17Request request = new ApproveAllKip17Request();
        request.setFrom(from);
        request.setTo(to);
        request.setApproved(approved);

        return kip17TokenApi.approveAllAsync(chainId, addressOrAlias, request, null, callback);
    }

    /**
     * Get list of tokens belonging to a particular token owner.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * String owner = "0x{ownerAddress}";
     *
     * GetOwnerKip17TokensResponse res = getTokenListByOwner(contractAddress, owner);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param owner The token owner address.
     * @return GetOwnerKip17TokensResponse
     * @throws ApiException
     */
    public GetOwnerKip17TokensResponse getTokenListByOwner(String addressOrAlias, String owner) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTokenListByOwner(addressOrAlias, owner, options);
    }

    /**
     * Get list of tokens belonging to a particular token owner.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * String owner = "0x{ownerAddress}";
     *
     * GetOwnerKip17TokensResponse res = getTokenListByOwner(contractAddress, owner, options);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param owner The token owner address.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @return GetOwnerKip17TokensResponse
     * @throws ApiException
     */
    public GetOwnerKip17TokensResponse getTokenListByOwner(String addressOrAlias, String owner, KIP17QueryOptions options) throws ApiException {
        return kip17TokenApi.getOwnerTokens(chainId, addressOrAlias, owner, options.getSize(), options.getCursor());
    }

    /**
     * Get list of tokens belonging to a particular token owner asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetOwnerKip17TokensResponse>() callback = new ApiCallback<GetOwnerKip17TokensResponse>() {
     *     ...implements callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String owner = "0x{ownerAddress}";
     *
     * GetOwnerKip17TokensResponse res = getTokenListByOwnerAsync(contractAddress, owner, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param owner The token owner address.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String addressOrAlias, String owner, ApiCallback<GetOwnerKip17TokensResponse> callback) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTokenListByOwnerAsync(addressOrAlias, owner, options, callback);
    }

    /**
     * Get list of tokens belonging to a particular token owner asynchronously.<br>
     * GET /v1/contract/{contract-address-or-alias}/owner/{owner-address}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetOwnerKip17TokensResponse>() callback = new ApiCallback<GetOwnerKip17TokensResponse>() {
     *     ...implements callback methods.
     * }
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * String owner = "0x{ownerAddress}";
     *
     * GetOwnerKip17TokensResponse res = getTokenListByOwnerAsync(contractAddress, owner, options, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param owner The token owner address.
     * @param options Filters required when retrieving data. `size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTokenListByOwnerAsync(String addressOrAlias, String owner, KIP17QueryOptions options, ApiCallback<GetOwnerKip17TokensResponse> callback) throws ApiException {
        return kip17TokenApi.getOwnerTokensAsync(chainId, addressOrAlias, owner, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Get list of specified token transfer history.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * GetKip17TokenHistoryResponse response = caver.kas.kip17.getTransferHistory(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @return GetKip17TokenHistoryResponse
     * @throws ApiException
     */
    public GetKip17TokenHistoryResponse getTransferHistory(String addressOrAlias, BigInteger tokenId) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTransferHistory(addressOrAlias, tokenId, options);
    }

    /**
     * Get list of specified token transfer history.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * GetKip17TokenHistoryResponse response = caver.kas.kip17.getTransferHistory(contractAddress, tokenId, options);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data.`size`, `cursor`.
     * @return GetKip17TokenHistoryResponse
     * @throws ApiException
     */
    public GetKip17TokenHistoryResponse getTransferHistory(String addressOrAlias, BigInteger tokenId, KIP17QueryOptions options) throws ApiException {
        return kip17TokenApi.getTokenHistory(chainId, addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), options.getSize(), options.getCursor());
    }

    /**
     * Get list of specified token transfer history.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * GetKip17TokenHistoryResponse response = caver.kas.kip17.getTransferHistory(contractAddress, tokenId);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @return GetKip17TokenHistoryResponse
     * @throws ApiException
     */
    public GetKip17TokenHistoryResponse getTransferHistory(String addressOrAlias, String tokenId) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTransferHistory(addressOrAlias, tokenId, options);
    }

    /**
     * Get list of specified token transfer history.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example
     * {@code
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = BigInteger.valueOf(1);
     *
     * GetKip17TokenHistoryResponse response = caver.kas.kip17.getTransferHistory(contractAddress, tokenId, options);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data.`size`, `cursor`.
     * @return GetKip17TokenHistoryResponse
     * @throws ApiException
     */
    public GetKip17TokenHistoryResponse getTransferHistory(String addressOrAlias, String tokenId, KIP17QueryOptions options) throws ApiException {
        return kip17TokenApi.getTokenHistory(chainId, addressOrAlias, tokenId, options.getSize(), options.getCursor());
    }

    /**
     * Get list of specified token transfer history asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetKip17TokenHistoryResponse> callback = new ApiCallback<GetKip17TokenHistoryResponse> callback() {
     *     implement callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip17.getTransferHistoryAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(String addressOrAlias, BigInteger tokenId, ApiCallback<GetKip17TokenHistoryResponse> callback) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTransferHistoryAsync(addressOrAlias, tokenId, options, callback);
    }

    /**
     * Get list of specified token transfer history asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetKip17TokenHistoryResponse> callback = new ApiCallback<GetKip17TokenHistoryResponse> callback() {
     *     implement callback methods.
     * }
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * BigInteger tokenId = BigInteger.valueOf(1);
     *
     * caver.kas.kip17.getTransferHistoryAsync(contractAddress, tokenId, options, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data.`size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(String addressOrAlias, BigInteger tokenId, KIP17QueryOptions options, ApiCallback<GetKip17TokenHistoryResponse> callback) throws ApiException {
        return kip17TokenApi.getTokenHistoryAsync(chainId, addressOrAlias, Numeric.toHexStringWithPrefix(tokenId), options.getSize(), options.getCursor(), callback);
    }

    /**
     * Get list of specified token transfer history asynchronously.<br>
     * It will send a request without filter options.<br>
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetKip17TokenHistoryResponse> callback = new ApiCallback<GetKip17TokenHistoryResponse> callback() {
     *     implement callback methods.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.kip17.getTransferHistoryAsync(contractAddress, tokenId, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(String addressOrAlias, String tokenId, ApiCallback<GetKip17TokenHistoryResponse> callback) throws ApiException {
        KIP17QueryOptions options = new KIP17QueryOptions();
        return getTransferHistoryAsync(addressOrAlias, tokenId, options, callback);
    }

    /**
     * Get list of specified token transfer history asynchronously.
     * GET /v1/contract/{contract-address-or-alias}/token/{token-id}/history
     *
     * <pre>Example :
     * {@code
     * ApiCallback<GetKip17TokenHistoryResponse> callback = new ApiCallback<GetKip17TokenHistoryResponse> callback() {
     *     implement callback methods.
     * }
     *
     * KIP17QueryOptions options = new KIP17QueryOptions();
     * options.setSize(1);
     * options.setCursor("cursorValue");
     *
     * String contractAddress = "0x{contractAddress}";
     * String tokenId = "0x1";
     *
     * caver.kas.kip17.getTransferHistoryAsync(contractAddress, tokenId, options, callback);
     * }</pre>
     *
     * @param addressOrAlias The KIP-17 contract address or alias.
     * @param tokenId The token id.
     * @param options Filters required when retrieving data.`size`, `cursor`.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getTransferHistoryAsync(String addressOrAlias, String tokenId, KIP17QueryOptions options, ApiCallback<GetKip17TokenHistoryResponse> callback) throws ApiException {
        return kip17TokenApi.getTokenHistoryAsync(chainId, addressOrAlias, tokenId, options.getSize(), options.getCursor(), callback);
    }

    /**
     * Getter function for kip17ContractApi
     * @return Kip17ContractApi
     */
    public Kip17ContractApi getKip17ContractApi() {
        return kip17ContractApi;
    }

    /**
     * Setter function for kip17ContractApi
     * @param kip17ContractApi The KIP-17 contract API rest-client object.
     */
    public void setKip17ContractApi(Kip17ContractApi kip17ContractApi) {
        this.kip17ContractApi = kip17ContractApi;
    }

    /**
     * Getter function for kip17TokenApi
     * @return Kip17TokenApi
     */
    public Kip17TokenApi getKip17TokenApi() {
        return kip17TokenApi;
    }

    /**
     * Setter function for kip17TokenApi
     * @param kip17TokenApi The KIP-17 token API rest-client object.
     */
    public void setKip17TokenApi(Kip17TokenApi kip17TokenApi) {
        this.kip17TokenApi = kip17TokenApi;
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
        setKip17ContractApi(new Kip17ContractApi(apiClient));
        setKip17TokenApi(new Kip17TokenApi(apiClient));
    }
}
