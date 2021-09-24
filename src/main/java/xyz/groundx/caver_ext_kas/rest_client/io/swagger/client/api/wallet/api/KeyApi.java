/*
 * Wallet API
 * # Introduction Wallet API is an API for creating and managing Klaytn accounts as well as sending transactions. If you create your Klaytn account using Wallet API, you don't have to manage your private key yourself. Wallet API provides a wallet for safe storage of your Klaytn account private keys that you would need to use BApps. For more details on how to use Wallet API, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial).  Wallet API can be divided into the Account part, which creates and manages Klaytn accounts, and the Transaction part, which sends different kinds of transactions.  Wallet API creates, deletes and monitors Klaytn accounts and updates the accounts to multisig, and manages all private keys for all accounts registered on KAS.  Wallet API can also create transaction to send it to Klaytn. These include transactions sent from multisig accounts. In case of muiltisig accounts, a transaction will automatically be sent to Klaytn once \\(Threshold\\) is met. For more detail, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial).  There are mainly two types of transactions: basic transactions and fee delegation transactions. Fee delegation transactions include Global Fee Delegation transaction and user fee deletation transaction. With the Global Fee Delegation transaction scheme, the transaction fee will initially be paid by GroundX and then be charged to you at a later date. With the User Fee Delegation transaction scheme, you create an account that pays the transaction fees on behalf of the users when a transaction.  The functionalities and limits of Wallet API are shown below:  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limits | Supports Cypress(Mainnet), Baobab(Testnet) \\ Doesn't support (Service Chain \\) | |  |  | Doesn't support account management for external custodial keys | |  |  | Doesn't support multisig for RLP encoded transactions | |  | Account management | Create, retrieve and delete account | |  |  | Multisig account update | |  | Managing transaction | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) creating and sending transaction | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) creating and sending transaction | |  |  | RLP encoded transaction\\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation)\\) creating and sending | |  |  | Managing and sending multisig transactions | |  | Administrator | Manage resource pool\\(create, query pool, delete, retrieve account \\) | 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.api;

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


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Key;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeyCreationRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeyCreationResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeyList;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeySignDataRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeySignDataResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.KeyStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyApi {
    private ApiClient apiClient;

    public KeyApi() {
        this(Configuration.getDefaultApiClient());
    }

    public KeyApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getKey
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getKeyCall(String xChainId, String keyId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/key/{key-id}"
            .replaceAll("\\{" + "key-id" + "\\}", apiClient.escapeString(keyId.toString()));

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
    private com.squareup.okhttp.Call getKeyValidateBeforeCall(String xChainId, String keyId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling getKey(Async)");
        }
        // verify the required parameter 'keyId' is set
        if (keyId == null) {
            throw new ApiException("Missing the required parameter 'keyId' when calling getKey(Async)");
        }
        
        com.squareup.okhttp.Call call = getKeyCall(xChainId, keyId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get key
     * Retrieve the created keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @return Key
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Key getKey(String xChainId, String keyId) throws ApiException {
        ApiResponse<Key> resp = getKeyWithHttpInfo(xChainId, keyId);
        return resp.getData();
    }

    /**
     * Get key
     * Retrieve the created keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @return ApiResponse&lt;Key&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Key> getKeyWithHttpInfo(String xChainId, String keyId) throws ApiException {
        com.squareup.okhttp.Call call = getKeyValidateBeforeCall(xChainId, keyId, null, null);
        Type localVarReturnType = new TypeToken<Key>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get key (asynchronously)
     * Retrieve the created keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getKeyAsync(String xChainId, String keyId, final ApiCallback<Key> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getKeyValidateBeforeCall(xChainId, keyId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Key>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for keyCreation
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call keyCreationCall(String xChainId, KeyCreationRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/key";

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
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
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
    private com.squareup.okhttp.Call keyCreationValidateBeforeCall(String xChainId, KeyCreationRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling keyCreation(Async)");
        }
        
        com.squareup.okhttp.Call call = keyCreationCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Key Creation
     * Creates up to 100 keys in batches.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return KeyCreationResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public KeyCreationResponse keyCreation(String xChainId, KeyCreationRequest body) throws ApiException {
        ApiResponse<KeyCreationResponse> resp = keyCreationWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Key Creation
     * Creates up to 100 keys in batches.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;KeyCreationResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<KeyCreationResponse> keyCreationWithHttpInfo(String xChainId, KeyCreationRequest body) throws ApiException {
        com.squareup.okhttp.Call call = keyCreationValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<KeyCreationResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Key Creation (asynchronously)
     * Creates up to 100 keys in batches.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call keyCreationAsync(String xChainId, KeyCreationRequest body, final ApiCallback<KeyCreationResponse> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = keyCreationValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<KeyCreationResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for keyDeletion
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call keyDeletionCall(String xChainId, String keyId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/key/{key-id}"
            .replaceAll("\\{" + "key-id" + "\\}", apiClient.escapeString(keyId.toString()));

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
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call keyDeletionValidateBeforeCall(String xChainId, String keyId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling keyDeletion(Async)");
        }
        // verify the required parameter 'keyId' is set
        if (keyId == null) {
            throw new ApiException("Missing the required parameter 'keyId' when calling keyDeletion(Async)");
        }
        
        com.squareup.okhttp.Call call = keyDeletionCall(xChainId, keyId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Key deletion
     * Delete a key.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @return KeyStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public KeyStatus keyDeletion(String xChainId, String keyId) throws ApiException {
        ApiResponse<KeyStatus> resp = keyDeletionWithHttpInfo(xChainId, keyId);
        return resp.getData();
    }

    /**
     * Key deletion
     * Delete a key.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @return ApiResponse&lt;KeyStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<KeyStatus> keyDeletionWithHttpInfo(String xChainId, String keyId) throws ApiException {
        com.squareup.okhttp.Call call = keyDeletionValidateBeforeCall(xChainId, keyId, null, null);
        Type localVarReturnType = new TypeToken<KeyStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Key deletion (asynchronously)
     * Delete a key.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call keyDeletionAsync(String xChainId, String keyId, final ApiCallback<KeyStatus> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = keyDeletionValidateBeforeCall(xChainId, keyId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<KeyStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for keySignData
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call keySignDataCall(String xChainId, String keyId, KeySignDataRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/key/{key-id}/sign"
            .replaceAll("\\{" + "key-id" + "\\}", apiClient.escapeString(keyId.toString()));

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
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
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
    private com.squareup.okhttp.Call keySignDataValidateBeforeCall(String xChainId, String keyId, KeySignDataRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling keySignData(Async)");
        }
        // verify the required parameter 'keyId' is set
        if (keyId == null) {
            throw new ApiException("Missing the required parameter 'keyId' when calling keySignData(Async)");
        }
        
        com.squareup.okhttp.Call call = keySignDataCall(xChainId, keyId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Sign data using keys
     * Sign the data using keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param body  (optional)
     * @return KeySignDataResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public KeySignDataResponse keySignData(String xChainId, String keyId, KeySignDataRequest body) throws ApiException {
        ApiResponse<KeySignDataResponse> resp = keySignDataWithHttpInfo(xChainId, keyId, body);
        return resp.getData();
    }

    /**
     * Sign data using keys
     * Sign the data using keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param body  (optional)
     * @return ApiResponse&lt;KeySignDataResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<KeySignDataResponse> keySignDataWithHttpInfo(String xChainId, String keyId, KeySignDataRequest body) throws ApiException {
        com.squareup.okhttp.Call call = keySignDataValidateBeforeCall(xChainId, keyId, body, null, null);
        Type localVarReturnType = new TypeToken<KeySignDataResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Sign data using keys (asynchronously)
     * Sign the data using keys.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param keyId key ID (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call keySignDataAsync(String xChainId, String keyId, KeySignDataRequest body, final ApiCallback<KeySignDataResponse> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = keySignDataValidateBeforeCall(xChainId, keyId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<KeySignDataResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for retrieveKeys
     * @param krn KAS resource name (required)
     * @param xChainId Klaytn chain network ID (1001 or 8217) (optional)
     * @param cursor Last cursor record (optional)
     * @param size Maximum query size (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call retrieveKeysCall(String krn, String xChainId, String cursor, String size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/key";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (krn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("krn", krn));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cursor", cursor));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));

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
    private com.squareup.okhttp.Call retrieveKeysValidateBeforeCall(String krn, String xChainId, String cursor, String size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'krn' is set
        if (krn == null) {
            throw new ApiException("Missing the required parameter 'krn' when calling retrieveKeys(Async)");
        }
        
        com.squareup.okhttp.Call call = retrieveKeysCall(krn, xChainId, cursor, size, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Retrieve a list of keys.
     * Returns a list of keys
     * @param krn KAS resource name (required)
     * @param xChainId Klaytn chain network ID (1001 or 8217) (optional)
     * @param cursor Last cursor record (optional)
     * @param size Maximum query size (optional)
     * @return KeyList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public KeyList retrieveKeys(String krn, String xChainId, String cursor, String size) throws ApiException {
        ApiResponse<KeyList> resp = retrieveKeysWithHttpInfo(krn, xChainId, cursor, size);
        return resp.getData();
    }

    /**
     * Retrieve a list of keys.
     * Returns a list of keys
     * @param krn KAS resource name (required)
     * @param xChainId Klaytn chain network ID (1001 or 8217) (optional)
     * @param cursor Last cursor record (optional)
     * @param size Maximum query size (optional)
     * @return ApiResponse&lt;KeyList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<KeyList> retrieveKeysWithHttpInfo(String krn, String xChainId, String cursor, String size) throws ApiException {
        com.squareup.okhttp.Call call = retrieveKeysValidateBeforeCall(krn, xChainId, cursor, size, null, null);
        Type localVarReturnType = new TypeToken<KeyList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve a list of keys. (asynchronously)
     * Returns a list of keys
     * @param krn KAS resource name (required)
     * @param xChainId Klaytn chain network ID (1001 or 8217) (optional)
     * @param cursor Last cursor record (optional)
     * @param size Maximum query size (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call retrieveKeysAsync(String krn, String xChainId, String cursor, String size, final ApiCallback<KeyList> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = retrieveKeysValidateBeforeCall(krn, xChainId, cursor, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<KeyList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
