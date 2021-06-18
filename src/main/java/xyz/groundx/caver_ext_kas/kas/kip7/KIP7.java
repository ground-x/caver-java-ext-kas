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

package xyz.groundx.caver_ext_kas.kas.kip7;

import com.squareup.okhttp.Call;
import org.web3j.utils.Numeric;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7Api;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7DeployerApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model.*;

import java.math.BigInteger;

/**
 * Representing a wrapping class that connects KIP-7 API.
 */
public class KIP7 {

    /**
     * KIP-7 API rest-client object.
     */
    Kip7Api kip7Api;

    /**
     * KIP-7 deployer API rest-client object.
     */
    Kip7DeployerApi kip7DeployerApi;

    /**
     * Klaytn network id.
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * Creates a KIP-7 API instance.
     * @param chainId A Klaytn network chain id.
     * @param apiClient The Api client for connection with KAS.
     */
    public KIP7(String chainId, ApiClient apiClient) {
        this.chainId = chainId;
        this.apiClient = apiClient;

        setKip7Api(new Kip7Api(apiClient));
        setKip7DeployerApi(new Kip7DeployerApi(apiClient));
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * POST /v1/contract <br>
     *
     * <pre>Example :
     * {@code
     * String testAlias = "test-contract";
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initialSupply, testAlias);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse deploy(String name, String symbol, int decimals, BigInteger initialSupply, String alias) throws ApiException {
        return deploy(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * POST /v1/contract <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *      ....implements callback method.
     * }
     *
     * String testAlias = "test-contract";
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
     *
     * caver.kas.kip7.deployAsync(name, symbol, decimals, initialSupply, testAlias, callback);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, int decimals, BigInteger initialSupply, String alias, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return deployAsync(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias, callback);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * POST /v1/contract <br>
     *
     * <pre>Example :
     * {@code
     * String testAlias = "test-contract";
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * String initialSupply = "0x152d02c7e14af6800000";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initialSupply, testAlias);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount(hex string) of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse deploy(String name, String symbol, int decimals, String initialSupply, String alias) throws ApiException {
        DeployKip7ContractRequest request = new DeployKip7ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setDecimals((long)decimals);
        request.setInitialSupply(initialSupply);
        request.setAlias(alias);

        return kip7Api.deployContract(chainId, request);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * POST /v1/contract <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *      ....implements callback method.
     * }
     *
     * String testAlias = "test-contract" + new Date().getTime();
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * String initial_supply = "0x152d02c7e14af6800000";
     *
     * caver.kas.kip7.deployAsync(name, symbol, decimals, initial_supply, testAlias, callback);
     *
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount(hex string) of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, int decimals, String initialSupply, String alias, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        DeployKip7ContractRequest request = new DeployKip7ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setDecimals((long)decimals);
        request.setInitialSupply(initialSupply);
        request.setAlias(alias);

        return kip7Api.deployContractAsync(chainId, request, callback);
    }

    /**
     * Retrieves KIP-7 contract information by either contract address or alias. <br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     *
     * Kip7ContractMetadataResponse response = caver.kas.kip7.getContract(contractAlias);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @return Kip7ContractMetadataResponse
     * @throws ApiException
     */
    public Kip7ContractMetadataResponse getContract(String addressOrAlias) throws ApiException {
        return kip7Api.getContract(chainId, addressOrAlias);
    }

    /**
     * Retrieves KIP-7 contract information by either contract address or alias. <br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7ContractMetadataResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *       ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * caver.kas.kip7.getContractAsync(contractAlias, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback
     * @return
     * @throws ApiException
     */
    public Call getContractAsync(String addressOrAlias, ApiCallback<Kip7ContractMetadataResponse> callback) throws ApiException {
        return kip7Api.getContractAsync(chainId, addressOrAlias, callback);
    }

    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS. <br>
     * GET /v1/contract/{contract-address-or-alias} <br>
     *
     * <pre>Example :
     * {@code
     * Kip7ContractListResponse response = caver.kas.kip7.getContractList();
     * }
     * </pre>
     *
     * @return Kip7ContractListResponse
     * @throws ApiException
     */
    public Kip7ContractListResponse getContractList() throws ApiException {
        return getContractList(new KIP7QueryOptions());
    }


    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS. <br>
     * GET /v1/contract/{contract-address-or-alias} <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7ContractListResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *       ....implements callback method.
     * }
     *
     * caver.kas.kip7.getContractListAsync(callback);
     *
     * }</pre>
     *
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractListAsync(ApiCallback<Kip7ContractListResponse> callback) throws ApiException {
        return getContractListAsync(new KIP7QueryOptions(), callback);
    }

    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS. <br>
     * GET /v1/contract/{contract-address-or-alias} <br>
     *
     * <pre>Example :
     * {@code
     * KIP7QueryOptions options = new KIP7QueryOptions();
     * options.setSize(1);
     * options.setStatus(KIP7QueryOptions.STATUS_TYPE.DEPLOYED);
     * options.setCursor("cursor value");
     *
     * Kip7ContractListResponse response = caver.kas.kip7.getContractList(options);
     * }
     * </pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`.
     * @return Kip7ContractListResponse
     * @throws ApiException
     */
    public Kip7ContractListResponse getContractList(KIP7QueryOptions options) throws ApiException {
        String size = null;
        if(options.getSize() != null) {
            size = Integer.toString(options.getSize());
        }

        return kip7Api.listContractsInDeployerPool(chainId, size, options.getCursor(), options.getStatus());
    }

    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias} <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7ContractListResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *       ....implements callback method.
     * }
     *
     * KIP7QueryOptions options = new KIP7QueryOptions();
     * options.setSize(1);
     * options.setStatus(KIP7QueryOptions.STATUS_TYPE.DEPLOYED);
     * options.setCursor("cursor value");
     *
     * caver.kas.kip7.getContractListAsync(options, callback);
     *
     * }</pre>
     *
     * @param options Filters required when retrieving data. `size`, `cursor` and `status`.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getContractListAsync(KIP7QueryOptions options, ApiCallback<Kip7ContractListResponse> callback) throws ApiException {
        String size = null;
        if(options.getSize() != null) {
            size = Integer.toString(options.getSize());
        }

        return kip7Api.listContractsInDeployerPoolAsync(chainId, size, options.getCursor(), options.getStatus(), callback);
    }

    /**
     * Shows the amount of tokens approved to the `spender` by the `owner`. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/allowance/{spender} <br>
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     *
     * Kip7TokenBalanceResponse response = caver.kas.kip7.allowance(contractAlias, owner, spender);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Token owner's Klaytn account address.
     * @param spender Klaytn account address to search the amount of tokens approved by the owner.
     * @return Kip7TokenBalanceResponse
     * @throws ApiException
     */
    public Kip7TokenBalanceResponse allowance(String addressOrAlias, String owner, String spender) throws ApiException {
        return kip7Api.getTokenAllowance(chainId, addressOrAlias, owner, spender);
    }

    /**
     * Shows the amount of tokens approved to the `spender` by the `owner` asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/allowance/{spender} <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TokenBalanceResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     *
     * caver.kas.kip7.allowance(contractAlias, owner, spender, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Token owner's Klaytn account address.
     * @param spender Klaytn account address to search the amount of tokens approved by the owner.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call allowanceAsync(String addressOrAlias, String owner, String spender, ApiCallback<Kip7TokenBalanceResponse> callback) throws ApiException {
        return kip7Api.getTokenAllowanceAsync(chainId, addressOrAlias, owner, spender, callback);
    }

    /**
     * Shows the `{owner}`'s balance for the given KIP-7 contract. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/balance <br>
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "";
     * String owner = "";
     *
     * Kip7TokenBalanceResponse response = caver.kas.kip7.balance(testContractAlias, deployerAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Token owner's Klaytn account address.
     * @return Kip7TokenBalanceResponse
     * @throws ApiException
     */
    public Kip7TokenBalanceResponse balance(String addressOrAlias, String owner) throws ApiException {
        return kip7Api.getTokenBalance(chainId, addressOrAlias, owner);
    }

    /**
     * Shows the `{owner}`'s balance for the given KIP-7 contract asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/balance <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TokenBalanceResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     *
     * caver.kas.kip7.balanceAsync(contractAlias, owner, callback);
     }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Token owner's Klaytn account address.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call balanceAsync(String addressOrAlias, String owner, ApiCallback<Kip7TokenBalanceResponse> callback) throws ApiException {
        return kip7Api.getTokenBalanceAsync(chainId, addressOrAlias, owner, callback);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner`. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve <br>
     *
     * <pre>Example :
     * {@code
     * String addressOrAlias = "";
     * String owner = "";
     * String spender = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Klaytn account address of the owner delegating the token tranfer. The default is the contract deployer account.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse approve(String addressOrAlias, String owner, String spender, BigInteger amount) throws ApiException {
        return approve(addressOrAlias, owner, spender, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner` asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.approveAsync(contractAlias, owner, spender, amount, callback);
     *
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Klaytn account address of the owner delegating the token tranfer. The default is the contract deployer account.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String owner, String spender, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return approveAsync(addressOrAlias, owner, spender, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner`. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve <br>
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Klaytn account address of the owner delegating the token tranfer. The default is the contract deployer account.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse approve(String addressOrAlias, String owner, String spender, String amount) throws ApiException {
        ApproveKip7TokenRequest request = new ApproveKip7TokenRequest();
        request.setOwner(owner);
        request.setSpender(spender);
        request.setAmount(amount);

        return kip7Api.approveToken(chainId, addressOrAlias, request);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner` asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.approveAsync(contractAlias, owner, spender, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param owner Klaytn account address of the owner delegating the token tranfer. The default is the contract deployer account.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount. (in hexadecimal with the 0x prefix)
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String owner, String spender, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        ApproveKip7TokenRequest request = new ApproveKip7TokenRequest();
        request.setOwner(owner);
        request.setSpender(spender);
        request.setAmount(amount);

        return kip7Api.approveTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends tokens of the contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens.
     * @param amount The amount of tokens to transfer.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transfer(String addressOrAlias, String to, BigInteger amount) throws ApiException {
        return transfer(addressOrAlias, null, to, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Sends tokens of the contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens.
     * @param amount The amount of tokens to transfer.
     * @param callback The callback to handle response.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String to, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, null, to, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Sends tokens of the contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transfer(String addressOrAlias, String to, String amount) throws ApiException {
        return transfer(addressOrAlias, null, to, amount);
    }

    /**
     * Sends tokens of the contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String to, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, null, to, amount, callback);
    }

    /**
     * Sends tokens of the contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transfer(String addressOrAlias, String from, String to, BigInteger amount) throws ApiException {
        return transfer(addressOrAlias, from, to, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Sends tokens of the contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.transferAsync(contractAlias, owner, to, amount, callback);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String from, String to, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return transferAsync(addressOrAlias, from, to, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Sends tokens of the contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transfer(String addressOrAlias, String from, String to, String amount) throws ApiException {
        TransferKip7TokenRequest request = new TransferKip7TokenRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.transferToken(chainId, addressOrAlias, request);
    }

    /**
     * Sends tokens of the contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer <br>
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.transferAsync(contractAlias, owner, to, amount, callback);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address to send tokens.
     * @param to Klaytn account addree to receive tokens
     * @param amount The amount of tokens to transfer. (in hexadecimal with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferAsync(String addressOrAlias, String from, String to, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        TransferKip7TokenRequest request = new TransferKip7TokenRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.transferTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends tokens on behalf of the owner. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method.
     * POST /v1/contract/{contract-address-or-alias}/transfer-from
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse transferRes = caver.kas.kip7.transferFrom(contractAlias, spender, owner, to, amount);

     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to send tokens by delegated token.
     * @param owner Klaytn account address of the owner delegating the token transfer.
     * @param to Klaytn account address to receive tokens.
     * @param amount The amount of tokens to transfer.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transferFrom(String addressOrAlias, String spender, String owner, String to, BigInteger amount) throws ApiException {
        return transferFrom(addressOrAlias, spender, owner, to, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Sends tokens on behalf of the owner asynchronously. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer-from <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.transferFromAsync(contractAlias, spender, owner, to, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to send tokens by delegated token.
     * @param owner Klaytn account address of the owner delegating the token transfer.
     * @param to Klaytn account address to receive tokens.
     * @param amount The amount of tokens to transfer.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferFromAsync(String addressOrAlias, String spender, String owner, String to, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return transferFromAsync(addressOrAlias, spender, owner, to, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Sends tokens on behalf of the owner. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer-from <br>
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse transferRes = caver.kas.kip7.transferFrom(contractAlias, spender, owner, to, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to send tokens by delegated token.
     * @param owner Klaytn account address of the owner delegating the token transfer.
     * @param to Klaytn account address to receive tokens.
     * @param amount The amount of tokens to transfer. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse transferFrom(String addressOrAlias, String spender, String owner, String to, String amount) throws ApiException {
        TransferKip7TokenFromRequest request = new TransferKip7TokenFromRequest();
        request.setSpender(spender);
        request.setOwner(owner);
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.transferFromToken(chainId, addressOrAlias, request);
    }


    /**
     * Sends tokens on behalf of the owner asynchronously. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer-from <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.transferFromAsync(contractAlias, spender, owner, to, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to send tokens by delegated token.
     * @param owner Klaytn account address of the owner delegating the token transfer.
     * @param to Klaytn account address to receive tokens.
     * @param amount The amount of tokens to transfer. (in hexadecimal with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call transferFromAsync(String addressOrAlias, String spender, String owner, String to, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        TransferKip7TokenFromRequest request = new TransferKip7TokenFromRequest();
        request.setSpender(spender);
        request.setOwner(owner);
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.transferFromTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint <br>
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger amount) throws ApiException {
        return mint(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Mints a new token for a given user asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String to = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.mintAsync(contractAlias, to, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint <br>
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse mint(String addressOrAlias, String to, String amount) throws ApiException {
        MintKip7TokenRequest request = new MintKip7TokenRequest();
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.mintToken(chainId, addressOrAlias, request);
    }

    /**
     * Mints a new token for a given user asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint <br>
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String to = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.mintAsync(contractAlias, to, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create. (in hexadecimal with the 0x prefix)
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        MintKip7TokenRequest request = new MintKip7TokenRequest();
        request.setTo(to);
        request.setAmount(amount);

        return kip7Api.mintTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Burns tokens. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param amount The amount of tokens to burn.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burn(String addressOrAlias, BigInteger amount) throws ApiException {
        return burn(addressOrAlias, null, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Burns tokens asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.burnAsync(contractAlias, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param amount The amount of tokens to burn.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, null, amount, callback);
    }

    /**
     * Burns tokens. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param amount The amount of tokens to burn.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burn(String addressOrAlias, String amount) throws ApiException {
        return burn(addressOrAlias, null, amount);
    }

    /**
     * Burns tokens asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.burnAsync(contractAlias, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param amount The amount of tokens to burn.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, null, amount, callback);
    }

    /**
     * Burns tokens. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String from = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, from, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address on which the tokens will be burned.
     * @param amount The amount of tokens to burn.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burn(String addressOrAlias, String from, BigInteger amount) throws ApiException {
        return burn(addressOrAlias, from, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * Burns tokens asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String from = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.burnAsync(contractAlias, from, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address on which the tokens will be burned.
     * @param amount The amount of tokens to burn.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String from, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return burnAsync(addressOrAlias, from, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * Burns tokens. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String from = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burn(contractAlias, from, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address on which the tokens will be burned.
     * @param amount The amount of tokens to burn. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burn(String addressOrAlias, String from, String amount) throws ApiException {
        BurnKip7TokenRequest request = new BurnKip7TokenRequest();
        request.setFrom(from);
        request.setAmount(amount);

        return kip7Api.burnToken(chainId, addressOrAlias, request);
    }


    /**
     * Burns tokens asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String from = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.burnAsync(contractAlias, from, amount, callback);
     *
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param from Klaytn account address on which the tokens will be burned.
     * @param amount The amount of tokens to burn. (in hexadecimal with the 0x prefix)
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnAsync(String addressOrAlias, String from, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        BurnKip7TokenRequest request = new BurnKip7TokenRequest();
        request.setFrom(from);
        request.setAmount(amount);

        return kip7Api.burnTokenAsync(chainId, addressOrAlias, request, callback);
    }


    /**
     * `spender` burns `owner`'s tokens on the `owner`'s behalf. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn-from
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burnFrom(contractAlias, spender, owner, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address on which the tokens will be burned.
     * @param owner Klaytn account address of the owner of the tokens to be burned.
     * @param amount The amount of tokens to burn.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burnFrom(String addressOrAlias, String spender, String owner, BigInteger amount) throws ApiException {
        return burnFrom(addressOrAlias, spender, owner, Numeric.toHexStringWithPrefix(amount));
    }

    /**
     * `spender` burns `owner`'s tokens on the `owner`'s behalf asynchronously. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn-from
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.burnFromAsync(contractAlias, spender, owner, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address on which the tokens will be burned.
     * @param owner Klaytn account address of the owner of the tokens to be burned.
     * @param amount The amount of tokens to burn.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnFromAsync(String addressOrAlias, String spender, String owner, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return burnFromAsync(addressOrAlias, spender, owner, Numeric.toHexStringWithPrefix(amount), callback);
    }

    /**
     * `spender` burns `owner`'s tokens on the `owner`'s behalf. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn-from
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.burnFrom(contractAlias, spender, owner, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address on which the tokens will be burned.
     * @param owner Klaytn account address of the owner of the tokens to be burned.
     * @param amount The amount of tokens to burn. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse burnFrom(String addressOrAlias, String spender, String owner, String amount) throws ApiException {
        BurnFromKip7TokenRequest request = new BurnFromKip7TokenRequest();
        request.setSpender(spender);
        request.setOwner(owner);
        request.setAmount(amount);

        return kip7Api.burnFromToken(chainId, addressOrAlias, request);
    }

    /**
     * `spender` burns `owner`'s tokens on the `owner`'s behalf asynchronously. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn-from
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "";
     * String owner = "";
     * String spender = "";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.burnFromAsync(contractAlias, spender, owner, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address on which the tokens will be burned.
     * @param owner Klaytn account address of the owner of the tokens to be burned.
     * @param amount The amount of tokens to burn. (in hexadecimal with the 0x prefix)
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call burnFromAsync(String addressOrAlias, String spender, String owner, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        BurnFromKip7TokenRequest request = new BurnFromKip7TokenRequest();
        request.setSpender(spender);
        request.setOwner(owner);
        request.setAmount(amount);

        return kip7Api.burnFromTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pauses all token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.unpause(contractAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse pause(String addressOrAlias) throws ApiException {
        return kip7Api.pauseContract(chainId, addressOrAlias);
    }

    /**
     * Pauses all token transfers and validations for a given contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * caver.kas.kip7.pauseAsync(testContractAlias, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return kip7Api.pauseContractAsync(chainId, addressOrAlias, callback);
    }

    /**
     * Resumes token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.unpause(contractAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse unpause(String addressOrAlias) throws ApiException {
        return kip7Api.unpauseContract(chainId, addressOrAlias);
    }

    /**
     * Resumes token transfers and validations for a given contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * caver.kas.kip7.unpauseAsync(testContractAlias, callback);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return kip7Api.unpauseContractAsync(chainId, addressOrAlias, callback);
    }

    /**
     * Queries the account that deploys and manages the KIP-7 contracts. <br>
     * GET /v1/deployer/default
     *
     * <pre>Example :
     * {@code
     * Kip7DeployerResponse deployerResponse = caver.kas.kip7.getDeployer();
     * }
     * </pre>
     *
     * @return Kip7DeployerResponse
     * @throws ApiException
     */
    public Kip7DeployerResponse getDeployer() throws ApiException {
        return kip7DeployerApi.getDefaultDeployer(chainId);
    }

    /**
     * Queries the account that deploys and manages the KIP-7 contracts asynchronously. <br>
     * GET /v1/deployer/default
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7DeployerResponse> callback = new ApiCallback<Kip7DeployerResponse>() {
     *      ....implements callback method.
     * };
     *
     * caver.kas.kip7.getDeployerAsync(callback);
     * }
     * </pre>
     *
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call getDeployerAsync(ApiCallback<Kip7DeployerResponse> callback) throws ApiException {
        return kip7DeployerApi.getDefaultDeployerAsync(chainId, callback);
    }

    /**
     * Getter for kip7DeployerApi
     * @return Kip7DeployerApi
     */
    public Kip7DeployerApi getKip7DeployerApi() {
        return kip7DeployerApi;
    }

    /**
     * Setter for kip7DeployerApi
     * @param kip7DeployerApi KIP-7 deployer API rest-client object.
     */
    public void setKip7DeployerApi(Kip7DeployerApi kip7DeployerApi) {
        this.kip7DeployerApi = kip7DeployerApi;
    }

    /**
     * Getter for kip7Api
     * @return Kip7Api
     */
    public Kip7Api getKip7Api() {
        return kip7Api;
    }

    /**
     * Setter for kip7Api
     * @param kip7Api KIP-7 API rest-client object.
     */
    public void setKip7Api(Kip7Api kip7Api) {
        this.kip7Api = kip7Api;
    }

    /**
     * Getter for chain id.
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * Setter for chain id
     * @param chainId The Klaytn network id.
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Getter for ApiClient.
     * @return ApiClient
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Setter function for apiClient
     * @param apiClient The ApiClient for connecting with KAS.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setKip7Api(new Kip7Api(apiClient));
        setKip7DeployerApi(new Kip7DeployerApi());
    }
}
