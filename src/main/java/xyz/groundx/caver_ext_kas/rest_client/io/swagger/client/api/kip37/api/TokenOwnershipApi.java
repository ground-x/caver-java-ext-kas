/*
 * KIP-37 API
 * ## Introduction The KIP-37 API helps Blockchain app (BApp) developers to easily deploy smart contracts and send tokens of the [KIP-37 Multi Token Standard](https://kips.klaytn.com/KIPs/kip-37).  You can use the default contract managing account (`deployer`) and `alias`.    You can also manage the contracts and tokens created on the klaytn network using the caver SDK, using contract address and the [Wallet API](https://refs.klaytnapi.com/ko/wallet/latest) account.    ## Fee Payer Options  KAS KIP-37 supports four scenarios for paying transactin fees:      **1. Using only KAS Global FeePayer Account**   Sends all transactions using the KAS global FeePayer Account.       ``` {     \"options\": {       \"enableGlobalFeePayer\": true     }     } ```    <br />    **2. Using User FeePayer account**   Sends all transactions using the KAS User FeePayer Account.      ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account**   Uses User FeePayer Account as default. When the balance runs out, KAS Global FeePayer Account will be used.     ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **4. Not using FeePayer Account**   Sends a transaction via normal means where the sender pays the transaction fee.       ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.api;

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


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.ErrorResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.Kip37TokenListResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenOwnershipApi {
    private ApiClient apiClient;

    public TokenOwnershipApi() {
        this(Configuration.getDefaultApiClient());
    }

    public TokenOwnershipApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getTokensByOwner
     * @param contractAddressOrAlias Contract address (in hex. with the 0x prefix) or alias (required)
     * @param ownerAddress Klaytn account address to query. (required)
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size Number of items to return (optional)
     * @param cursor The pointer after which the results will be returned. &#x60;cursor&#x60; will be included in the result when you request a query of a specific number (&#x60;size&#x60;) of items. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getTokensByOwnerCall(String contractAddressOrAlias, String ownerAddress, String xChainId, Integer size, String cursor, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v1/contract/{contract-address-or-alias}/owner/{owner-address}/token"
            .replaceAll("\\{" + "contract-address-or-alias" + "\\}", apiClient.escapeString(contractAddressOrAlias.toString()))
            .replaceAll("\\{" + "owner-address" + "\\}", apiClient.escapeString(ownerAddress.toString()));

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
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
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
    private com.squareup.okhttp.Call getTokensByOwnerValidateBeforeCall(String contractAddressOrAlias, String ownerAddress, String xChainId, Integer size, String cursor, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'contractAddressOrAlias' is set
        if (contractAddressOrAlias == null) {
            throw new ApiException("Missing the required parameter 'contractAddressOrAlias' when calling getTokensByOwner(Async)");
        }
        // verify the required parameter 'ownerAddress' is set
        if (ownerAddress == null) {
            throw new ApiException("Missing the required parameter 'ownerAddress' when calling getTokensByOwner(Async)");
        }
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling getTokensByOwner(Async)");
        }
        
        com.squareup.okhttp.Call call = getTokensByOwnerCall(contractAddressOrAlias, ownerAddress, xChainId, size, cursor, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get KIP-37 Token List of an Account
     * Returns a list of tokens owned by a certain account. 
     * @param contractAddressOrAlias Contract address (in hex. with the 0x prefix) or alias (required)
     * @param ownerAddress Klaytn account address to query. (required)
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size Number of items to return (optional)
     * @param cursor The pointer after which the results will be returned. &#x60;cursor&#x60; will be included in the result when you request a query of a specific number (&#x60;size&#x60;) of items. (optional)
     * @return Kip37TokenListResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Kip37TokenListResponse getTokensByOwner(String contractAddressOrAlias, String ownerAddress, String xChainId, Integer size, String cursor) throws ApiException {
        ApiResponse<Kip37TokenListResponse> resp = getTokensByOwnerWithHttpInfo(contractAddressOrAlias, ownerAddress, xChainId, size, cursor);
        return resp.getData();
    }

    /**
     * Get KIP-37 Token List of an Account
     * Returns a list of tokens owned by a certain account. 
     * @param contractAddressOrAlias Contract address (in hex. with the 0x prefix) or alias (required)
     * @param ownerAddress Klaytn account address to query. (required)
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size Number of items to return (optional)
     * @param cursor The pointer after which the results will be returned. &#x60;cursor&#x60; will be included in the result when you request a query of a specific number (&#x60;size&#x60;) of items. (optional)
     * @return ApiResponse&lt;Kip37TokenListResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Kip37TokenListResponse> getTokensByOwnerWithHttpInfo(String contractAddressOrAlias, String ownerAddress, String xChainId, Integer size, String cursor) throws ApiException {
        com.squareup.okhttp.Call call = getTokensByOwnerValidateBeforeCall(contractAddressOrAlias, ownerAddress, xChainId, size, cursor, null, null);
        Type localVarReturnType = new TypeToken<Kip37TokenListResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get KIP-37 Token List of an Account (asynchronously)
     * Returns a list of tokens owned by a certain account. 
     * @param contractAddressOrAlias Contract address (in hex. with the 0x prefix) or alias (required)
     * @param ownerAddress Klaytn account address to query. (required)
     * @param xChainId Klaytn Network Chain ID (1001 or 8217) (required)
     * @param size Number of items to return (optional)
     * @param cursor The pointer after which the results will be returned. &#x60;cursor&#x60; will be included in the result when you request a query of a specific number (&#x60;size&#x60;) of items. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getTokensByOwnerAsync(String contractAddressOrAlias, String ownerAddress, String xChainId, Integer size, String cursor, final ApiCallback<Kip37TokenListResponse> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getTokensByOwnerValidateBeforeCall(contractAddressOrAlias, ownerAddress, xChainId, size, cursor, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Kip37TokenListResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
