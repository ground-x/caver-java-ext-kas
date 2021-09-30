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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7ContractApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7DeployerApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7TokenApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model.*;

import java.math.BigInteger;

/**
 * Representing a wrapping class that connects KIP-7 API.
 */
public class KIP7 {

    /**
     * KIP-7 API rest-client object.
     */
    Kip7ContractApi kip7ContractApi;

    /**
     * KIP-7 token API rest-client object.
     */
    Kip7TokenApi kip7TokenApi;

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
        setApiClient(apiClient);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
    public Kip7DeployResponse deploy(String name, String symbol, int decimals, BigInteger initialSupply, String alias) throws ApiException {
        return deploy(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias, null);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
    public Call deployAsync(String name, String symbol, int decimals, BigInteger initialSupply, String alias, ApiCallback<Kip7DeployResponse> callback) throws ApiException {
        return deployAsync(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias, null, callback);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
    public Kip7DeployResponse deploy(String name, String symbol, int decimals, String initialSupply, String alias) throws ApiException {
        return deploy(name, symbol, decimals, initialSupply, alias, null);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
    public Call deployAsync(String name, String symbol, int decimals, String initialSupply, String alias, ApiCallback<Kip7DeployResponse> callback) throws ApiException {
        return deployAsync(name, symbol, decimals, initialSupply, alias, null, callback);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String testAlias = "test-contract";
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOptions option = new Kip7FeePayerOptions();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initialSupply, testAlias, option);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7DeployResponse deploy(String name, String symbol, int decimals, BigInteger initialSupply, String alias, Kip7FeePayerOptions option) throws ApiException {
        return deploy(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias, option);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOptions option = new Kip7FeePayerOptions();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * caver.kas.kip7.deployAsync(name, symbol, decimals, initialSupply, testAlias, option, callback);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, int decimals, BigInteger initialSupply, String alias, Kip7FeePayerOptions option, ApiCallback<Kip7DeployResponse> callback) throws ApiException {
        return deployAsync(name, symbol, decimals, Numeric.toHexStringWithPrefix(initialSupply), alias, option, callback);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
     *
     * <pre>Example :
     * {@code
     * String testAlias = "test-contract";
     * String name = "TEST_KIP7";
     * String symbol = "TKIP7";
     * int decimals = 18;
     * String initialSupply = "0x152d02c7e14af6800000";
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOptions option = new Kip7FeePayerOptions();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.deploy(name, symbol, decimals, initialSupply, testAlias, option);
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount(hex string) of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7DeployResponse deploy(String name, String symbol, int decimals, String initialSupply, String alias, Kip7FeePayerOptions option) throws ApiException {
        DeployKip7ContractRequest request = new DeployKip7ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setDecimals((long)decimals);
        request.setInitialSupply(initialSupply);
        request.setAlias(alias);
        request.setOptions(option);

        return kip7ContractApi.deployContract(chainId, request);
    }

    /**
     * Deploy KIP-7 token contract with a Klaytn account in KAS asynchronously.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * POST /v1/contract
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
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOption option = new Kip7FeePayerOption();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * caver.kas.kip7.deployAsync(name, symbol, decimals, initial_supply, testAlias, option, callback);
     *
     * }
     * </pre>
     *
     * @param name The name of KIP-7 token.
     * @param symbol The symbol of KIP-7 token.
     * @param decimals The number of decimal places the token uses.
     * @param initialSupply The total amount(hex string) of token to be supplied initially.
     * @param alias The alias of KIP-7 token. Your `alias` must only contain lowercase alphabets, numbers and hyphens and begin with an alphabet.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call deployAsync(String name, String symbol, int decimals, String initialSupply, String alias, Kip7FeePayerOptions option, ApiCallback<Kip7DeployResponse> callback) throws ApiException {
        DeployKip7ContractRequest request = new DeployKip7ContractRequest();
        request.setName(name);
        request.setSymbol(symbol);
        request.setDecimals((long)decimals);
        request.setInitialSupply(initialSupply);
        request.setAlias(alias);
        request.setOptions(option);

        return kip7ContractApi.deployContractAsync(chainId, request, callback);
    }


    /**
     * Retrieves KIP-7 contract information by either contract address or alias. <br>
     * GET /v1/contract/{contract-address-or-alias}
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{contract alias}";
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
        return kip7ContractApi.getContract(chainId, addressOrAlias);
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
     * String contractAlias = "{Contract alias}";
     * caver.kas.kip7.getContractAsync(contractAlias, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call getContractAsync(String addressOrAlias, ApiCallback<Kip7ContractMetadataResponse> callback) throws ApiException {
        return kip7ContractApi.getContractAsync(chainId, addressOrAlias, callback);
    }

    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS. <br>
     * GET /v1/contract/{contract-address-or-alias}
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
     * GET /v1/contract/{contract-address-or-alias}
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
     * GET /v1/contract/{contract-address-or-alias}
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

        return kip7ContractApi.listContractsInDeployerPool(chainId, size, options.getCursor(), options.getStatus());
    }

    /**
     * Search the list of deployed KIP-7 contracts using the Klaytn account in KAS asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias}
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

        return kip7ContractApi.listContractsInDeployerPoolAsync(chainId, size, options.getCursor(), options.getStatus(), callback);
    }

    /**
     * Shows the amount of tokens approved to the `spender` by the `owner`. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/allowance/{spender}
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
        return kip7TokenApi.getTokenAllowance(chainId, addressOrAlias, owner, spender);
    }

    /**
     * Shows the amount of tokens approved to the `spender` by the `owner` asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/allowance/{spender}
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TokenBalanceResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
        return kip7TokenApi.getTokenAllowanceAsync(chainId, addressOrAlias, owner, spender, callback);
    }

    /**
     * Shows the `{owner}`'s balance for the given KIP-7 contract. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/balance
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "{contract address}";
     * String owner = "{owner address}";
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
        return kip7TokenApi.getTokenBalance(chainId, addressOrAlias, owner);
    }

    /**
     * Shows the `{owner}`'s balance for the given KIP-7 contract asynchronously. <br>
     * GET /v1/contract/{contract-address-or-alias}/account/{owner}/balance
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TokenBalanceResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
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
        return kip7TokenApi.getTokenBalanceAsync(chainId, addressOrAlias, owner, callback);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner`. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String spender = "{spender address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse approve(String addressOrAlias, String spender, BigInteger amount) throws ApiException {
        return approve(addressOrAlias, null, spender, amount);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner` asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String spender = "{spender address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.approveAsync(contractAlias, spender, amount, callback);
     *
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String spender, BigInteger amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return approveAsync(addressOrAlias, null, spender, amount, callback);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner`. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String spender = "{spender address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.approve(contractAlias, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount. (in hexadecimal with the 0x prefix)
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse approve(String addressOrAlias, String spender, String amount) throws ApiException {
        return approve(addressOrAlias, null, spender, amount);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner` asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String spender = "{spender address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.approveAsync(contractAlias, spender, amount, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param spender Klaytn account address to approve delegated token transfer.
     * @param amount Approved token amount. (in hexadecimal with the 0x prefix)
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call approveAsync(String addressOrAlias, String spender, String amount, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return approveAsync(addressOrAlias, null, spender, amount, callback);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner`. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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

        return kip7TokenApi.approveToken(chainId, addressOrAlias, request);
    }

    /**
     * Authorizes the `spender` to send a certain amount of tokens on behalf of the `owner` asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/approve
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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

        return kip7TokenApi.approveTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends tokens of the contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * caver.kas.kip7.transferAsync(contractAlias, owner, to, amount, callback);
     * }
     * </pre>
     *
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
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.transfer(contractAlias, owner, spender, amount);
     * }
     * </pre>
     *
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

        return kip7TokenApi.transferToken(chainId, addressOrAlias, request);
    }

    /**
     * Sends tokens of the contract asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String to = "{to address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     *
     * caver.kas.kip7.transferAsync(contractAlias, owner, to, amount, callback);
     * }
     * </pre>
     *
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

        return kip7TokenApi.transferTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Sends tokens on behalf of the owner. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method.<br>
     * POST /v1/contract/{contract-address-or-alias}/transfer-from
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     *
     * Kip7TransactionStatusResponse transferRes = caver.kas.kip7.transferFrom(contractAlias, spender, owner, to, amount);

     * }
     * </pre>
     *
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
     * POST /v1/contract/{contract-address-or-alias}/transfer-from
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
     * String to = "{to address}";
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
     * POST /v1/contract/{contract-address-or-alias}/transfer-from
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
     * String to = "{to address}";
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

        return kip7TokenApi.transferFromToken(chainId, addressOrAlias, request);
    }


    /**
     * Sends tokens on behalf of the owner asynchronously. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/transfer-from
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
     * String to = "{to address}";
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

        return kip7TokenApi.transferFromTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
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
        return mint(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount), null);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     * String minter = "{minter address}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount, minter);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create.
     * @param minter Klaytn account address to mint the token.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse mint(String addressOrAlias, String to, BigInteger amount, String minter) throws ApiException {
        return mint(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount), minter);
    }

    /**
     * Mints a new token for a given user asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
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
        return mintAsync(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount), null, callback);
    }

    /**
     * Mints a new token for a given user asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * BigInteger amount = BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
     * String minter = "{minter address}";
     *
     * caver.kas.kip7.mintAsync(contractAlias, to, amount, minter, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create.
     * @param minter Klaytn account address to mint the token.
     * @param callback The callback to handle resposne.
     * @return Call
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, BigInteger amount, String minter, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return mintAsync(addressOrAlias, to, Numeric.toHexStringWithPrefix(amount), minter, callback);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
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
        return mint(addressOrAlias, to, amount, null);
    }

    /**
     * Mints a new token for a given user. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     * String minter = "{minter address}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.mint(contractAlias, to, amount, minter);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create.
     * @param minter Klaytn account address to mint the token.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse mint(String addressOrAlias, String to, String amount, String minter) throws ApiException {
        MintKip7TokenRequest request = new MintKip7TokenRequest();
        request.setTo(to);
        request.setFrom(minter);
        request.setAmount(amount);

        return kip7TokenApi.mintToken(chainId, addressOrAlias, request);
    }

    /**
     * Mints a new token for a given user asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
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
        return mintAsync(addressOrAlias, to, amount, null, callback);
    }

    /**
     * Mints a new token for a given user asynchronously. <br>
     * POST /v1/contract/{contract-address-or-alias}/mint
     *
     * <pre>Example :
     * {@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAlias = "{Contract alias}";
     * String to = "{to address}";
     * String amount = "0x8ac7230489e80000"; // 10 * 10^18
     * String minter = "{minter address}";
     *
     * caver.kas.kip7.mintAsync(contractAlias, to, amount, minter, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param to Klaytn account address to receive the newly created token.
     * @param amount Amount of tokens to create. (in hexadecimal with the 0x prefix)
     * @param minter Klaytn account address to mint the token.
     * @param callback
     * @return
     * @throws ApiException
     */
    public Call mintAsync(String addressOrAlias, String to, String amount, String minter, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        MintKip7TokenRequest request = new MintKip7TokenRequest();
        request.setTo(to);
        request.setFrom(minter);
        request.setAmount(amount);

        return kip7TokenApi.mintTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Burns tokens. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
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
     * String contractAlias = "{Contract alias}";
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
     * String contractAlias = "{Contract alias}";
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
     * String contractAlias = "{Contract alias}";
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
     * String contractAlias = "{Contract alias}";
     * String from = "{from address}";
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
     * String contractAlias = "{Contract alias}";
     * String from = "{from address}";
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
     * String contractAlias = "{Contract alias}";
     * String from = "{from address}";
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

        return kip7TokenApi.burnToken(chainId, addressOrAlias, request);
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
     * String contractAlias = "{Contract alias}";
     * String from = "{from address}";
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

        return kip7TokenApi.burnTokenAsync(chainId, addressOrAlias, request, callback);
    }


    /**
     * `spender` burns `owner`'s tokens on the `owner`'s behalf. <br>
     * It need to approve token transfer in advance {@link #approve(String, String, String, String)} approve} method. <br>
     * POST /v1/contract/{contract-address-or-alias}/burn-from
     *
     * <pre>Example :
     * {@code
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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

        return kip7TokenApi.burnFromToken(chainId, addressOrAlias, request);
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
     * String contractAlias = "{Contract alias}";
     * String owner = "{owner address}";
     * String spender = "{spender address}";
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

        return kip7TokenApi.burnFromTokenAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Pauses all token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "{Contract address}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.pause(contractAddress);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse pause(String addressOrAlias) throws ApiException {
        return pause(addressOrAlias, null);
    }

    /**
     * Pauses all token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/pause
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{Contract address}";
     * String pauser = "0x{pauserAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.pause(contractAddress, pauser);
     * }
     * </pre>
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The account that sends the transaction to pause the contract.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse pause(String addressOrAlias, String pauser) throws ApiException {
        PauseKip7Request request = new PauseKip7Request();
        request.setPauser(pauser);

        return kip7ContractApi.pauseContract(chainId, addressOrAlias, request);
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
        return pauseAsync(addressOrAlias, null, callback);
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
     * String contractAddress = "0x{Contract address}";
     * String pauser = "0x{pauserAddress}";
     *
     * caver.kas.kip7.pauseAsync(contractAddress, pauser, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The account that sends the transaction to pause the contract.
     * @param callback The callback function to handle response.
     * @return
     * @throws ApiException
     */
    public Call pauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        PauseKip7Request request = new PauseKip7Request();
        request.setPauser(pauser);
        return kip7ContractApi.pauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Resumes token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     * <pre>Example :
     * {@code
     * String contractAddress = "{Contract address}";
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
        return unpause(addressOrAlias, null);
    }

    /**
     * Resumes token transfers and validations for a given contract. <br>
     * POST /v1/contract/{contract-address-or-alias}/unpause
     *
     * <pre>Example :
     * {@code
     * String testContractAlias = "contract-alias";
     * String pauser = "0x{pauserAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.unpause(testContractAlias, pauser);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The Klaytn account address whose authority to resume contract operation
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse unpause(String addressOrAlias, String pauser) throws ApiException {
        UnpauseKip7Request request = new UnpauseKip7Request();
        request.setPauser(pauser);
        return kip7ContractApi.unpauseContract(chainId, addressOrAlias, request);
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
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return unpauseAsync(addressOrAlias, null, callback);
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
     * String testContractAlias = "contract-alias";
     * String pauser = "0x{pauserAddress}";
     *
     * caver.kas.kip7.unpauseAsync(testContractAlias, pauser, callback);
     * }
     * </pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The Klaytn account address whose authority to resume contract operation
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call unpauseAsync(String addressOrAlias, String pauser, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        UnpauseKip7Request request = new UnpauseKip7Request();
        request.setPauser(pauser);
        return kip7ContractApi.unpauseContractAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Grants a specified account the authority to mint and burn tokens from a contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/minter
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String accountToBeMinter = "0x{accountAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.addMinter(contractAddress, accountToBeMinter);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBeMinter The Klaytn account to be granted authority to mint and burn tokens.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse addMinter(String addressOrAlias, String accountToBeMinter) throws ApiException {
        return addMinter(addressOrAlias, accountToBeMinter, null);
    }

    /**
     * Grants a specified account the authority to mint and burn tokens from a contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/minter
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String accountToBeMinter = "0x{accountAddress}";
     *
     * caver.kas.kip7.addMinterAsync(contractAddress, accountToBeMinter, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBeMinter The Klaytn account to be granted authority to mint and burn tokens.
     * @param callback The callback to handler response.
     * @return Call
     * @throws ApiException
     */
    public Call addMinterAsync(String addressOrAlias, String accountToBeMinter, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return addMinterAsync(addressOrAlias, accountToBeMinter, null, callback);
    }

    /**
     * Grants a specified account the authority to mint and burn tokens from a contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/minter
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String accountToBeMinter = "0x{accountAddress}";
     * String minter = "0x{minterAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.addMinter(contractAddress, accountToBeMinter, minter);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBeMinter The Klaytn account to be granted authority to mint and burn tokens.
     * @param minter The Klaytn account that grants authority to mint and buran a token.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse addMinter(String addressOrAlias, String accountToBeMinter, String minter) throws ApiException {
        AddMinterKip7Request request = new AddMinterKip7Request();
        request.setMinter(accountToBeMinter);
        request.setSender(minter);

        return kip7TokenApi.addMinter(chainId, addressOrAlias, request);
    }

    /**
     * Grants a specified account the authority to mint and burn tokens from a contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/minter
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String accountToBeMinter = "0x{accountAddress}";
     * String minter = "0x{minterAddress}";
     *
     * caver.kas.kip7.addMinterAsync(contractAddress, accountToBeMinter, minter, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBeMinter The Klaytn account to be granted authority to mint and burn tokens.
     * @param minter The Klaytn account that grants authority to mint and buran a token.
     * @param callback The callback to handle response.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Call addMinterAsync(String addressOrAlias, String accountToBeMinter, String minter, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        AddMinterKip7Request request = new AddMinterKip7Request();
        request.setMinter(accountToBeMinter);
        request.setSender(minter);

        return kip7TokenApi.addMinterAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Removes the authority given to a certain account to mint and burn tokens from a contract.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/minter/{minter-address}
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String minter = "0x{minterAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.renounceMinter(contractAddress, minter);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param minter The Klaytn account address whose authority to mint and burn tokens will be removed.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse renounceMinter(String addressOrAlias, String minter) throws ApiException {
        return kip7TokenApi.renounceMinter(addressOrAlias, minter, chainId);
    }

    /**
     * Removes the authority given to a certain account to mint and burn tokens from a contract asynchronously.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/minter/{minter-address}
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String minter = "0x{minterAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.renounceMinterAsync(contractAddress, minter, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param minter The Klaytn account address whose authority to mint and burn tokens will be removed.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call renounceMinterAsync(String addressOrAlias, String minter, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return kip7TokenApi.renounceMinterAsync(addressOrAlias, minter, chainId, callback);
    }

    /**
     * Grants a specified account the authority to pause the actions of a contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/pauser
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String accountToBePauser = "0x{senderAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.addPauser(contractAddress, accountToBePauser);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBePauser The Klaytn account address to be granted authority to send and pause a contract.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse addPauser(String addressOrAlias, String accountToBePauser) throws ApiException {
        return addPauser(addressOrAlias, accountToBePauser, null);
    }

    /**
     * Grants a specified account the authority to pause the actions of a contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/pauser
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String accountToBePauser = "0x{senderAddress}";
     * String pauser = "0x{pauserAddress}";
     *
     * caver.kas.kip7.addPauserAsync(contractAddress, accountToBePauser, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBePauser The Klaytn account address to be granted authority to send and pause a contract.
     * @param callback The callback to handle response
     * @return Call
     * @throws ApiException
     */
    public Call addPauserAsync(String addressOrAlias, String accountToBePauser, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return addPauserAsync(addressOrAlias, accountToBePauser, null, callback);
    }


    /**
     * Grants a specified account the authority to pause the actions of a contract.<br>
     * POST /v1/contract/{contract-address-or-alias}/pauser
     *
     * <pre>{@code
     * String contractAddress = "0x{contractAddress}";
     * String accountToBePauser = "0x{senderAddress}";
     * String pauser = "0x{pauserAddress}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.addPauser(contractAddress, accountToBePauser, pauser);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBePauser The Klaytn account address to be granted authority to send and pause a contract.
     * @param pauser The Klaytn account that grants the autority.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse addPauser(String addressOrAlias, String accountToBePauser, String pauser) throws ApiException {
        AddPauserKip7Request request = new AddPauserKip7Request();
        request.setSender(pauser);
        request.setPauser(accountToBePauser);

        return kip7ContractApi.addPauser(chainId, addressOrAlias, request);
    }

    /**
     * Grants a specified account the authority to pause the actions of a contract asynchronously.<br>
     * POST /v1/contract/{contract-address-or-alias}/pauser
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String contractAddress = "0x{contractAddress}";
     * String accountToBePauser = "0x{senderAddress}";
     * String pauser = "0x{pauserAddress}";
     *
     * caver.kas.kip7.addPauserAsync(contractAddress, accountToBePauser, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param accountToBePauser The Klaytn account address to be granted authority to send and pause a contract.
     * @param pauser The Klaytn account that grants the autority.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call addPauserAsync(String addressOrAlias, String accountToBePauser, String pauser, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        AddPauserKip7Request request = new AddPauserKip7Request();
        request.setSender(pauser);
        request.setPauser(accountToBePauser);

        return kip7ContractApi.addPauserAsync(chainId, addressOrAlias, request, callback);
    }

    /**
     * Removes the authority given to a certain account to pause the actions of specified contract.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/pauser/{pauser-address}
     *
     * <pre>{@code
     * String address = "0x{contract-address}";
     * String pauser = "0x{pauser-address}";
     *
     * Kip7TransactionStatusResponse response = caver.kas.kip7.renouncePauser(address, pauser);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The Klaytn account address whose authoriy to pause contract will be removed.
     * @return Kip7TransactionStatusResponse
     * @throws ApiException
     */
    public Kip7TransactionStatusResponse renouncePauser(String addressOrAlias, String pauser) throws ApiException {
        return kip7ContractApi.renouncePauser(addressOrAlias, pauser, chainId);
    }

    /**
     * Removes the authority given to a certain account to pause the actions of specified contract asynchronously.<br>
     * DELETE /v1/contract/{contract-address-or-alias}/pauser/{pauser-address}
     *
     * <pre>{@code
     * ApiCallback<Kip7TransactionStatusResponse> callback = new ApiCallback<Kip7TransactionStatusResponse>() {
     *     ....implements callback method.
     * }
     *
     * String address = "0x{contract-address}";
     * String pauser = "0x{pauser-address}";
     *
     * caver.kas.kip7.renouncePauserAsync(address, pauser, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param pauser The Klaytn account address whose authoriy to pause contract will be removed.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call renouncePauserAsync(String addressOrAlias, String pauser, ApiCallback<Kip7TransactionStatusResponse> callback) throws ApiException {
        return kip7ContractApi.renouncePauserAsync(addressOrAlias, pauser, chainId, callback);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * Kip7ContractMetadataResponse response = caver.kas.kip7.updateContract("0x{contractAddress}");
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @return Kip7ContractMetadataResponse
     * @throws ApiException
     */
    public Kip7ContractMetadataResponse updateContractOptions(String addressOrAlias) throws ApiException {
        return updateContractOptions(addressOrAlias, null);
    }

    /**
     * Updates the fee payment method for a contract asynchronously.<br>
     * It sets a fee payer options that config to pay transaction fee to using only Global fee payer account.<br>
     * To see more detail, see <a href="https://refs.klaytnapi.com/en/kip7/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * ApiCallback<Kip7ContractMetadataResponse> callback = new ApiCallback<Kip7ContractMetadataResponse>() {
     *     ....implements callback method.
     * }
     *
     * caver.kas.kip7.updateContractOptionsAsync("0x{contractAddress}", callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, ApiCallback<Kip7ContractMetadataResponse> callback) throws ApiException {
        return updateContractOptionsAsync(addressOrAlias, null, callback);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOptions option = new Kip7FeePayerOptions();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * Kip7ContractMetadataResponse response = caver.kas.kip7.updateContract("0x{contractAddress}", option);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @return Kip7ContractMetadataResponse
     * @throws ApiException
     */
    public Kip7ContractMetadataResponse updateContractOptions(String addressOrAlias, Kip7FeePayerOptions option) throws ApiException {
        UpdateKip7ContractRequest request = new UpdateKip7ContractRequest();
        request.setOptions(option);
        return kip7ContractApi.updateContract(chainId, addressOrAlias, request);
    }

    /**
     * Updates the fee payment method for a contract.<br>
     * If you want to see more detail about configuring fee payer option, see <a href="https://refs.klaytnapi.com/en/kip37/latest#section/Fee-Payer-Options">Fee payer options</a><br>
     * PUT /v1/contract/{contract-address-or-alias}
     *
     * <pre>{@code
     * ApiCallback<Kip7ContractMetadataResponse> callback = new ApiCallback<Kip7ContractMetadataResponse>() {
     *     ....implements callback method.
     * }
     *
     * // Using a user fee payer options.
     * // It needs to have userFeePayer account and KRN created by KAS Wallet API.
     * String feePayer = "0x{feePayer}";
     * String feePayer_krn = "krn";
     *
     * Kip7FeePayerOptionResponseUserFeePayer userFeePayerOption = new Kip7FeePayerOptionResponseUserFeePayer();
     * userFeePayerOption.setAddress(userFeePayer.getAddress());
     * userFeePayerOption.setKrn(userFeePayer.getKrn());
     *
     * Kip7FeePayerOptions option = new Kip7FeePayerOptions();
     * option.setEnableGlobalFeePayer(false);
     * option.setUserFeePayer(userFeePayerOption);
     *
     * caver.kas.kip7.updateContractAsync("0x{contractAddress}", option, callback);
     * }</pre>
     *
     * @param addressOrAlias Contract address (in hexadecimal with the 0x prefix) or an alias.
     * @param option The feePayer options that config to pay transaction fee logic.
     * @param callback The callback to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call updateContractOptionsAsync(String addressOrAlias, Kip7FeePayerOptions option, ApiCallback<Kip7ContractMetadataResponse> callback) throws ApiException {
        UpdateKip7ContractRequest request = new UpdateKip7ContractRequest();
        request.setOptions(option);
        return kip7ContractApi.updateContractAsync(chainId, addressOrAlias, request, callback);
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
     * Getter for kip7ContractApi
     * @return Kip7ContractApi
     */
    public Kip7ContractApi getKip7ContractApi() {
        return kip7ContractApi;
    }

    /**
     * Setter for kip7ContractApi
     * @param kip7ContractApi KIP-7 contract API rest-client object
     */
    public void setKip7ContractApi(Kip7ContractApi kip7ContractApi) {
        this.kip7ContractApi = kip7ContractApi;
    }

    /**
     * Getter for kip7TokenApi
     * @return Kip7TokenApi
     */
    public Kip7TokenApi getKip7TokenApi() {
        return kip7TokenApi;
    }

    /**
     * Setter for Kip7TokenApi
     * @param kip7TokenApi KIP-7 token API rest-client object.
     */
    public void setKip7TokenApi(Kip7TokenApi kip7TokenApi) {
        this.kip7TokenApi = kip7TokenApi;
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
        setKip7ContractApi(new Kip7ContractApi(apiClient));
        setKip7TokenApi(new Kip7TokenApi(apiClient));
        setKip7DeployerApi(new Kip7DeployerApi(apiClient));
    }
}
