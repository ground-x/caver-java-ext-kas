/*
 * Wallet API
 * # Introduction Wallet API is used to create and manage Klaytn accounts and transfer transactions. If you create a Klaytn account with Wallet API, you do not need to manage private keys separately. Wallet API provides a secure wallet to keep your Klaytn account’s private keys for BApp. For more details on Wallet API, refer to our [tutorial](https://docs.klaytnapi.com/v/ko/tutorial).  Wallet API features an “Account” section for creating and managing Klaytn accounts and a “Transaction” section for transferring transactions. Wallet API creates, deletes, and monitors Klaytn accounts; updates multisig accounts; and manages the privates keys of all accounts registered to KAS.  In addition, Wallet API creates transactions and transfers them to Klaytn. They include transactions that are sent through the multisig accounts. A transaction will be automatically transferred to Klaytn if the threshold is met for the number of signatures. For more details on multisignatures, refer to [the followings](https://docs.klaytnapi.com/v/ko/tutorial).  Transactions include basic and fee delegation transactions. In particular, fee delegation transactions include global and user fee delegation transactions. In the global fee delegation transaction, Ground X’s KAS account first pays the transaction fee and charges the users later. Meanwhile, in the user fee delegation transaction, a user creates an account to pay for transaction fees when sending transactions.  Wallet API has the following functions and limitations.  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limitations | Support for Cypress (mainnet) and Baobab (testnet) (Service Chain not supported) | |  |  | Account management for external management keys not supported | |  |  | Multisignatures of RLP-encoded transactions not supported | |  | Account management  | Account creation, search, and deletion | |  |  | Multisignature account updates | |  | Transaction management | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) Transaction Creation and Transfer | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer | |  |  | RLP-encoded transaction \\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer \\) | |  |  | Multisignature transaction management and transfer | |  | Administrator | Resource pool management (creation, pool search, deletion, and account search) |    # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |   | 1061010 | data don't exist</br>data don't exist; krn:1001:wallet:68ec0e4b-0f61-4e6f-ae35-be865ab23187:account-pool:default:0x9b2f4d85d7f7abb14db229b5a81f1bdca0aa24c8ff0c4c100b3f25098b7a6152 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0xc9bFDDabf2c38396b097C8faBE9151955413995D</br>account is invalid to sign the transaction; 0x35Cc4921B17Dfa67a58B93c9F8918f823e58b77e 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; tx_id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; input</br>cannot be empty or zero value; address</br>cannot be empty or zero value; keyId 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | feeration must be between 1 and 99; feeRatio 1061903 | failed to decode account keys 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0xb9612ec6ec39bfd3f2841daa7ab062fc94cf33f23503606c979b2f81e50b2cb1 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account from AMS</br>failed to get an account from AMS; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |   # Authentication  <!-- ReDoc-Inject: <security-definitions> -->
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


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.AccountUpdateTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.AnchorTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.CancelTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ContractCallRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ContractCallResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ContractDeployTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ContractExecutionTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ErrorResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.LegacyTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ProcessRLPRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.TransactionReceipt;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.TransactionResult;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ValueTransferTransactionRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicTransactionApi {
    private ApiClient apiClient;

    public BasicTransactionApi() {
        this(Configuration.getDefaultApiClient());
    }

    public BasicTransactionApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for accountUpdateTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call accountUpdateTransactionCall(String xChainId, AccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/account";

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call accountUpdateTransactionValidateBeforeCall(String xChainId, AccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling accountUpdateTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = accountUpdateTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Account Update Transaction
     * Create a transaction for updating Klaytn account keys. For more details about the types of Klaytn account keys, refer to the following.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult accountUpdateTransaction(String xChainId, AccountUpdateTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = accountUpdateTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Account Update Transaction
     * Create a transaction for updating Klaytn account keys. For more details about the types of Klaytn account keys, refer to the following.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> accountUpdateTransactionWithHttpInfo(String xChainId, AccountUpdateTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = accountUpdateTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Account Update Transaction (asynchronously)
     * Create a transaction for updating Klaytn account keys. For more details about the types of Klaytn account keys, refer to the following.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call accountUpdateTransactionAsync(String xChainId, AccountUpdateTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = accountUpdateTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for anchorTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call anchorTransactionCall(String xChainId, AnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/anchor";

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
    private com.squareup.okhttp.Call anchorTransactionValidateBeforeCall(String xChainId, AnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling anchorTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = anchorTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Anchoring Transaction
     * Create a transaction for anchoring service chain data to the Klaytn main chain.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult anchorTransaction(String xChainId, AnchorTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = anchorTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Anchoring Transaction
     * Create a transaction for anchoring service chain data to the Klaytn main chain.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> anchorTransactionWithHttpInfo(String xChainId, AnchorTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = anchorTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Anchoring Transaction (asynchronously)
     * Create a transaction for anchoring service chain data to the Klaytn main chain.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call anchorTransactionAsync(String xChainId, AnchorTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = anchorTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for cancelTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call cancelTransactionCall(String xChainId, CancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx";

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
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call cancelTransactionValidateBeforeCall(String xChainId, CancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling cancelTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = cancelTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Cancel Transaction
     * Create a transaction for canceling a pending transaction. Either a nonce or transaction hash is required for cancellation.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult cancelTransaction(String xChainId, CancelTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = cancelTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Cancel Transaction
     * Create a transaction for canceling a pending transaction. Either a nonce or transaction hash is required for cancellation.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> cancelTransactionWithHttpInfo(String xChainId, CancelTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = cancelTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Cancel Transaction (asynchronously)
     * Create a transaction for canceling a pending transaction. Either a nonce or transaction hash is required for cancellation.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call cancelTransactionAsync(String xChainId, CancelTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = cancelTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for contractCall
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call contractCallCall(String xChainId, ContractCallRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/contract/call";

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
    private com.squareup.okhttp.Call contractCallValidateBeforeCall(String xChainId, ContractCallRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling contractCall(Async)");
        }
        
        com.squareup.okhttp.Call call = contractCallCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Contract Call
     * You can view certain value in the contract and validate that you can submit executable transaction.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ContractCallResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ContractCallResponse contractCall(String xChainId, ContractCallRequest body) throws ApiException {
        ApiResponse<ContractCallResponse> resp = contractCallWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Contract Call
     * You can view certain value in the contract and validate that you can submit executable transaction.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;ContractCallResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ContractCallResponse> contractCallWithHttpInfo(String xChainId, ContractCallRequest body) throws ApiException {
        com.squareup.okhttp.Call call = contractCallValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<ContractCallResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Contract Call (asynchronously)
     * You can view certain value in the contract and validate that you can submit executable transaction.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call contractCallAsync(String xChainId, ContractCallRequest body, final ApiCallback<ContractCallResponse> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = contractCallValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ContractCallResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for contractDeployTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call contractDeployTransactionCall(String xChainId, ContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/contract/deploy";

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
    private com.squareup.okhttp.Call contractDeployTransactionValidateBeforeCall(String xChainId, ContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling contractDeployTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = contractDeployTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Contract Deployment Transaction
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult contractDeployTransaction(String xChainId, ContractDeployTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = contractDeployTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Contract Deployment Transaction
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> contractDeployTransactionWithHttpInfo(String xChainId, ContractDeployTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = contractDeployTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Contract Deployment Transaction (asynchronously)
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call contractDeployTransactionAsync(String xChainId, ContractDeployTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = contractDeployTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for contractExecutionTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call contractExecutionTransactionCall(String xChainId, ContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/contract/execute";

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
    private com.squareup.okhttp.Call contractExecutionTransactionValidateBeforeCall(String xChainId, ContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling contractExecutionTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = contractExecutionTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Contract Execution Transaction
     * Create a transaction for executing a released contract function.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult contractExecutionTransaction(String xChainId, ContractExecutionTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = contractExecutionTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Contract Execution Transaction
     * Create a transaction for executing a released contract function.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> contractExecutionTransactionWithHttpInfo(String xChainId, ContractExecutionTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = contractExecutionTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Contract Execution Transaction (asynchronously)
     * Create a transaction for executing a released contract function.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call contractExecutionTransactionAsync(String xChainId, ContractExecutionTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = contractExecutionTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for legacyTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call legacyTransactionCall(String xChainId, LegacyTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/legacy";

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
    private com.squareup.okhttp.Call legacyTransactionValidateBeforeCall(String xChainId, LegacyTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling legacyTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = legacyTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Legacy transaction invocation
     * Create a transaction that supports legacy accounts (which have a public key that is derived from a private key) and transaction formats. Through KAS, any Klaytn account will be created as a legacy account by default.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult legacyTransaction(String xChainId, LegacyTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = legacyTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Legacy transaction invocation
     * Create a transaction that supports legacy accounts (which have a public key that is derived from a private key) and transaction formats. Through KAS, any Klaytn account will be created as a legacy account by default.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> legacyTransactionWithHttpInfo(String xChainId, LegacyTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = legacyTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Legacy transaction invocation (asynchronously)
     * Create a transaction that supports legacy accounts (which have a public key that is derived from a private key) and transaction formats. Through KAS, any Klaytn account will be created as a legacy account by default.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call legacyTransactionAsync(String xChainId, LegacyTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = legacyTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for processRLP
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call processRLPCall(String xChainId, ProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/rlp";

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
    private com.squareup.okhttp.Call processRLPValidateBeforeCall(String xChainId, ProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling processRLP(Async)");
        }
        
        com.squareup.okhttp.Call call = processRLPCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Transaction Using RLP
     * Create a transaction using the rlp(SigRLP or TxHashRLP). Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult processRLP(String xChainId, ProcessRLPRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = processRLPWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Transaction Using RLP
     * Create a transaction using the rlp(SigRLP or TxHashRLP). Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> processRLPWithHttpInfo(String xChainId, ProcessRLPRequest body) throws ApiException {
        com.squareup.okhttp.Call call = processRLPValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Transaction Using RLP (asynchronously)
     * Create a transaction using the rlp(SigRLP or TxHashRLP). Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call processRLPAsync(String xChainId, ProcessRLPRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = processRLPValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for transactionReceipt
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionHash Transaction hash value (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call transactionReceiptCall(String xChainId, String transactionHash, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/tx/{transaction-hash}"
            .replaceAll("\\{" + "transaction-hash" + "\\}", apiClient.escapeString(transactionHash.toString()));

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
    private com.squareup.okhttp.Call transactionReceiptValidateBeforeCall(String xChainId, String transactionHash, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling transactionReceipt(Async)");
        }
        // verify the required parameter 'transactionHash' is set
        if (transactionHash == null) {
            throw new ApiException("Missing the required parameter 'transactionHash' when calling transactionReceipt(Async)");
        }
        
        com.squareup.okhttp.Call call = transactionReceiptCall(xChainId, transactionHash, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Search Transaction
     * Search for the transaction execution result using the transaction hash value. The status field of the response indicates if the execution is successful.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionHash Transaction hash value (required)
     * @return TransactionReceipt
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionReceipt transactionReceipt(String xChainId, String transactionHash) throws ApiException {
        ApiResponse<TransactionReceipt> resp = transactionReceiptWithHttpInfo(xChainId, transactionHash);
        return resp.getData();
    }

    /**
     * Search Transaction
     * Search for the transaction execution result using the transaction hash value. The status field of the response indicates if the execution is successful.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionHash Transaction hash value (required)
     * @return ApiResponse&lt;TransactionReceipt&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionReceipt> transactionReceiptWithHttpInfo(String xChainId, String transactionHash) throws ApiException {
        com.squareup.okhttp.Call call = transactionReceiptValidateBeforeCall(xChainId, transactionHash, null, null);
        Type localVarReturnType = new TypeToken<TransactionReceipt>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Search Transaction (asynchronously)
     * Search for the transaction execution result using the transaction hash value. The status field of the response indicates if the execution is successful.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionHash Transaction hash value (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call transactionReceiptAsync(String xChainId, String transactionHash, final ApiCallback<TransactionReceipt> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = transactionReceiptValidateBeforeCall(xChainId, transactionHash, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionReceipt>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for valueTransferTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call valueTransferTransactionCall(String xChainId, ValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/value";

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
    private com.squareup.okhttp.Call valueTransferTransactionValidateBeforeCall(String xChainId, ValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling valueTransferTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = valueTransferTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * KLAY Transfer Transaction
     * Create a transaction for transferring KLAYs with or without a memo.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult valueTransferTransaction(String xChainId, ValueTransferTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = valueTransferTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * KLAY Transfer Transaction
     * Create a transaction for transferring KLAYs with or without a memo.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;TransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TransactionResult> valueTransferTransactionWithHttpInfo(String xChainId, ValueTransferTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = valueTransferTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * KLAY Transfer Transaction (asynchronously)
     * Create a transaction for transferring KLAYs with or without a memo.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call valueTransferTransactionAsync(String xChainId, ValueTransferTransactionRequest body, final ApiCallback<TransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = valueTransferTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
