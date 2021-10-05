/*
 * KIP-17 API
 * # Introduction The KIP-17 API helps BApp (Blockchain Application) developers to manage contracts and tokens created in accordance with the [KIP-17](https://docs.klaytnapi.com/v/en/api#kip-17-api) standard, which is Klaytn's technical speficication for Non-Fungible Tokens.  The functionality of the multiple endpoints enables you to do the following actions: - deploy smart contracts - manage the entire life cycle of an NFT from minting, to sending and burning - get contract or token data - authorize a third party to execute token transfers - view token ownership history  For more details on KAS, please refer to [KAS Docs](https://docs.klaytnapi.com/). If you have any questions or comments, please leave them in the [Klaytn Developers Forum](http://forum.klaytn.com).    **alias**  When a method of the KIP-17 API requires a contract address, you can use the contract **alias**. You can give the contract an alias when deploying, and use it in place of the complicated address.  # Fee Payer Options KAS KIP-17 supports four ways to pay the transaction fees.<br />  **1. Only using KAS Global FeePayer Account** <br /> Sends all transactions using KAS Global FeePayer Account. ``` {     \"options\": {       \"enableGlobalFeePayer\": true     } } ``` <br />  **2. Using User FeePayer Account** <br /> Sends all transactions using User FeePayer Account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ``` <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account** <br /> Sends transactions using User FeePayer Account by default, and switches to the KAS Global FeePayer Account when balances are insufficient. ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ``` <br />  **4. Not using FeePayer Account** <br /> Sends transactions the default way, paying the transaction fee from the user's account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` <br />  # Error Code This section contains the errors that might occur when using the KIP-17 API. KAS uses HTTP status codes. More details can be found in this [link](https://developer.mozilla.org/en/docs/Web/HTTP/Status).
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.api;

import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.Configuration;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.Pair;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ProgressRequestBody;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.DeployKip17ContractRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.ErrorResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.Kip17ContractInfoResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.Kip17ContractListResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.Kip17DeployResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.UpdateKip17ContractRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kip17ContractApi {
    private ApiClient apiClient;

    public Kip17ContractApi() {
        this(Configuration.getDefaultApiClient());
    }

    public Kip17ContractApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for deployContract
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deployContractCall(String xChainId, DeployKip17ContractRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v1/contract";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (xChainId != null)
        localVarHeaderParams.put("x-chain-id", apiClient.parameterToString(xChainId));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call deployContractValidateBeforeCall(String xChainId, DeployKip17ContractRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling deployContract(Async)");
        }
        
        com.squareup.okhttp.Call call = deployContractCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Deploy Contract
     * Deploys a new KIP-17 contract with the given parameters. You can find the tutorial for KIP-17 contract deployment [here](https://docs.klaytnapi.com/tutorial/kip17-api#kip-17).     Even if you see &#x60;Submitted&#x60; in the response, it doesn&#x27;t mean that the transaction is &#x60;Committed&#x60;. To confirm transaction status, use Get Contract List [/v1/contract](#operation/ListContractsInDeployerPool) or Get Transaction Receipt from the Wallet API [/v2/tx/{transaction-hash}](https://refs.klaytnapi.com/en/wallet/latest#operation/TransactionReceipt).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return Kip17DeployResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Kip17DeployResponse deployContract(String xChainId, DeployKip17ContractRequest body) throws ApiException {
        ApiResponse<Kip17DeployResponse> resp = deployContractWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Deploy Contract
     * Deploys a new KIP-17 contract with the given parameters. You can find the tutorial for KIP-17 contract deployment [here](https://docs.klaytnapi.com/tutorial/kip17-api#kip-17).     Even if you see &#x60;Submitted&#x60; in the response, it doesn&#x27;t mean that the transaction is &#x60;Committed&#x60;. To confirm transaction status, use Get Contract List [/v1/contract](#operation/ListContractsInDeployerPool) or Get Transaction Receipt from the Wallet API [/v2/tx/{transaction-hash}](https://refs.klaytnapi.com/en/wallet/latest#operation/TransactionReceipt).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;Kip17DeployResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Kip17DeployResponse> deployContractWithHttpInfo(String xChainId, DeployKip17ContractRequest body) throws ApiException {
        com.squareup.okhttp.Call call = deployContractValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<Kip17DeployResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deploy Contract (asynchronously)
     * Deploys a new KIP-17 contract with the given parameters. You can find the tutorial for KIP-17 contract deployment [here](https://docs.klaytnapi.com/tutorial/kip17-api#kip-17).     Even if you see &#x60;Submitted&#x60; in the response, it doesn&#x27;t mean that the transaction is &#x60;Committed&#x60;. To confirm transaction status, use Get Contract List [/v1/contract](#operation/ListContractsInDeployerPool) or Get Transaction Receipt from the Wallet API [/v2/tx/{transaction-hash}](https://refs.klaytnapi.com/en/wallet/latest#operation/TransactionReceipt).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deployContractAsync(String xChainId, DeployKip17ContractRequest body, final ApiCallback<Kip17DeployResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deployContractValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Kip17DeployResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getContract
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getContractCall(String xChainId, String contractAddressOrAlias, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v1/contract/{contract-address-or-alias}"
            .replaceAll("\\{" + "contract-address-or-alias" + "\\}", apiClient.escapeString(contractAddressOrAlias.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (xChainId != null)
        localVarHeaderParams.put("x-chain-id", apiClient.parameterToString(xChainId));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getContractValidateBeforeCall(String xChainId, String contractAddressOrAlias, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling getContract(Async)");
        }
        // verify the required parameter 'contractAddressOrAlias' is set
        if (contractAddressOrAlias == null) {
            throw new ApiException("Missing the required parameter 'contractAddressOrAlias' when calling getContract(Async)");
        }
        
        com.squareup.okhttp.Call call = getContractCall(xChainId, contractAddressOrAlias, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Contract
     * Returns the data for a specified KIP-17 contract. You can use either the contract alias or contract address.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @return Kip17ContractInfoResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Kip17ContractInfoResponse getContract(String xChainId, String contractAddressOrAlias) throws ApiException {
        ApiResponse<Kip17ContractInfoResponse> resp = getContractWithHttpInfo(xChainId, contractAddressOrAlias);
        return resp.getData();
    }

    /**
     * Get Contract
     * Returns the data for a specified KIP-17 contract. You can use either the contract alias or contract address.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @return ApiResponse&lt;Kip17ContractInfoResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Kip17ContractInfoResponse> getContractWithHttpInfo(String xChainId, String contractAddressOrAlias) throws ApiException {
        com.squareup.okhttp.Call call = getContractValidateBeforeCall(xChainId, contractAddressOrAlias, null, null);
        Type localVarReturnType = new TypeToken<Kip17ContractInfoResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Contract (asynchronously)
     * Returns the data for a specified KIP-17 contract. You can use either the contract alias or contract address.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getContractAsync(String xChainId, String contractAddressOrAlias, final ApiCallback<Kip17ContractInfoResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getContractValidateBeforeCall(xChainId, contractAddressOrAlias, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Kip17ContractInfoResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for listContractsInDeployerPool
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size The number of items to return (min&#x3D;1, max&#x3D;1000, default&#x3D;100). (optional)
     * @param cursor The pointer for the next request, after which the result will be returned. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call listContractsInDeployerPoolCall(String xChainId, String size, String cursor, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v1/contract";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cursor", cursor));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (xChainId != null)
        localVarHeaderParams.put("x-chain-id", apiClient.parameterToString(xChainId));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call listContractsInDeployerPoolValidateBeforeCall(String xChainId, String size, String cursor, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling listContractsInDeployerPool(Async)");
        }
        
        com.squareup.okhttp.Call call = listContractsInDeployerPoolCall(xChainId, size, cursor, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get Contract List
     * Returns a list of KIP-17 contracts created from a KAS account.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size The number of items to return (min&#x3D;1, max&#x3D;1000, default&#x3D;100). (optional)
     * @param cursor The pointer for the next request, after which the result will be returned. (optional)
     * @return Kip17ContractListResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Kip17ContractListResponse listContractsInDeployerPool(String xChainId, String size, String cursor) throws ApiException {
        ApiResponse<Kip17ContractListResponse> resp = listContractsInDeployerPoolWithHttpInfo(xChainId, size, cursor);
        return resp.getData();
    }

    /**
     * Get Contract List
     * Returns a list of KIP-17 contracts created from a KAS account.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size The number of items to return (min&#x3D;1, max&#x3D;1000, default&#x3D;100). (optional)
     * @param cursor The pointer for the next request, after which the result will be returned. (optional)
     * @return ApiResponse&lt;Kip17ContractListResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Kip17ContractListResponse> listContractsInDeployerPoolWithHttpInfo(String xChainId, String size, String cursor) throws ApiException {
        com.squareup.okhttp.Call call = listContractsInDeployerPoolValidateBeforeCall(xChainId, size, cursor, null, null);
        Type localVarReturnType = new TypeToken<Kip17ContractListResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get Contract List (asynchronously)
     * Returns a list of KIP-17 contracts created from a KAS account.<p></p>
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size The number of items to return (min&#x3D;1, max&#x3D;1000, default&#x3D;100). (optional)
     * @param cursor The pointer for the next request, after which the result will be returned. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call listContractsInDeployerPoolAsync(String xChainId, String size, String cursor, final ApiCallback<Kip17ContractListResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = listContractsInDeployerPoolValidateBeforeCall(xChainId, size, cursor, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Kip17ContractListResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateContract
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateContractCall(String xChainId, String contractAddressOrAlias, UpdateKip17ContractRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v1/contract/{contract-address-or-alias}"
            .replaceAll("\\{" + "contract-address-or-alias" + "\\}", apiClient.escapeString(contractAddressOrAlias.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (xChainId != null)
        localVarHeaderParams.put("x-chain-id", apiClient.parameterToString(xChainId));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateContractValidateBeforeCall(String xChainId, String contractAddressOrAlias, UpdateKip17ContractRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling updateContract(Async)");
        }
        // verify the required parameter 'contractAddressOrAlias' is set
        if (contractAddressOrAlias == null) {
            throw new ApiException("Missing the required parameter 'contractAddressOrAlias' when calling updateContract(Async)");
        }
        
        com.squareup.okhttp.Call call = updateContractCall(xChainId, contractAddressOrAlias, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Update Contract
     * Updates the fee payment method for a contract.  ##### Options With &#x60;options&#x60; you can set the transaction fee payment method. You can find more details in [Fee Payer Options](#section/Fee-Payer-Options).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param body  (optional)
     * @return Kip17ContractInfoResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Kip17ContractInfoResponse updateContract(String xChainId, String contractAddressOrAlias, UpdateKip17ContractRequest body) throws ApiException {
        ApiResponse<Kip17ContractInfoResponse> resp = updateContractWithHttpInfo(xChainId, contractAddressOrAlias, body);
        return resp.getData();
    }

    /**
     * Update Contract
     * Updates the fee payment method for a contract.  ##### Options With &#x60;options&#x60; you can set the transaction fee payment method. You can find more details in [Fee Payer Options](#section/Fee-Payer-Options).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param body  (optional)
     * @return ApiResponse&lt;Kip17ContractInfoResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Kip17ContractInfoResponse> updateContractWithHttpInfo(String xChainId, String contractAddressOrAlias, UpdateKip17ContractRequest body) throws ApiException {
        com.squareup.okhttp.Call call = updateContractValidateBeforeCall(xChainId, contractAddressOrAlias, body, null, null);
        Type localVarReturnType = new TypeToken<Kip17ContractInfoResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update Contract (asynchronously)
     * Updates the fee payment method for a contract.  ##### Options With &#x60;options&#x60; you can set the transaction fee payment method. You can find more details in [Fee Payer Options](#section/Fee-Payer-Options).
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param contractAddressOrAlias Contract address (in hex.) or alias. (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateContractAsync(String xChainId, String contractAddressOrAlias, UpdateKip17ContractRequest body, final ApiCallback<Kip17ContractInfoResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateContractValidateBeforeCall(xChainId, contractAddressOrAlias, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Kip17ContractInfoResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
