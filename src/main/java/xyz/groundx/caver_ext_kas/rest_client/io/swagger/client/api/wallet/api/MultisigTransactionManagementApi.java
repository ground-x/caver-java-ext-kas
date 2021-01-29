/*
 * Wallet API
 * # Introduction Wallet API is used to create and manage Klaytn accounts and transfer transactions. If you create a Klaytn account with Wallet API, you do not need to manage private keys separately. Wallet API provides a secure wallet to keep your Klaytn account’s private keys for BApp. For more details on Wallet API, refer to our [tutorial](https://docs.klaytnapi.com/v/ko/tutorial).  Wallet API features an “Account” section for creating and managing Klaytn accounts and a “Transaction” section for transferring transactions. Wallet API creates, deletes, and monitors Klaytn accounts; updates multisig accounts; and manages the privates keys of all accounts registered to KAS.  In addition, Wallet API creates transactions and transfers them to Klaytn. They include transactions that are sent through the multisig accounts. A transaction will be automatically transferred to Klaytn if the threshold is met for the number of signatures. For more details on multisignatures, refer to [the followings](https://docs.klaytnapi.com/v/ko/tutorial).  Transactions include basic and fee delegation transactions. In particular, fee delegation transactions include global and user fee delegation transactions. In the global fee delegation transaction, Ground X’s KAS account first pays the transaction fee and charges the users later. Meanwhile, in the user fee delegation transaction, a user creates an account to pay for transaction fees when sending transactions.  Wallet API has the following functions and limitations.  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limitations | Support for Cypress (mainnet) and Baobab (testnet) (Service Chain not supported) | |  |  | Account management for external management keys not supported | |  |  | Multisignatures of RLP-encoded transactions not supported | |  | Account management  | Account creation, search, and deletion | |  |  | Multisignature account updates | |  | Transaction management | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) Transaction Creation and Transfer | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer | |  |  | RLP-encoded transaction \\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer \\) | |  |  | Multisignature transaction management and transfer | |  | Administrator | Resource pool management (creation, pool search, deletion, and account search) |    # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |   | 1061010 | data don't exist 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0xc9bFDDabf2c38396b097C8faBE9151955413995D</br>account is invalid to sign the transaction; 0x35Cc4921B17Dfa67a58B93c9F8918f823e58b77e 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; tx_id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; input 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | feeration must be between 1 and 99; feeRatio 1061903 | failed to decode account keys 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0xb9612ec6ec39bfd3f2841daa7ab062fc94cf33f23503606c979b2f81e50b2cb1 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account from AMS</br>failed to get an account from AMS; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |  
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


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.ErrorResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.MultisigTransactionStatus;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.MultisigTransactions;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.SignPendingTransactionBySigRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultisigTransactionManagementApi {
    private ApiClient apiClient;

    public MultisigTransactionManagementApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MultisigTransactionManagementApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for retrieveMultisigTransactions
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Account address which has multisig keys or signer&#x27;s account address (required)
     * @param size Maximum size of account to search (optional, default to 100)
     * @param cursor Information on last searched cursor (optional)
     * @param toTimestamp Timestamp of the end time to be searched (in seconds) (optional)
     * @param fromTimestamp Timestamp of the start time to be searched (in seconds) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call retrieveMultisigTransactionsCall(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/multisig/account/{address}/tx"
            .replaceAll("\\{" + "address" + "\\}", apiClient.escapeString(address.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("size", size));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("cursor", cursor));
        if (toTimestamp != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("to-timestamp", toTimestamp));
        if (fromTimestamp != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("from-timestamp", fromTimestamp));

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
    private com.squareup.okhttp.Call retrieveMultisigTransactionsValidateBeforeCall(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling retrieveMultisigTransactions(Async)");
        }
        // verify the required parameter 'address' is set
        if (address == null) {
            throw new ApiException("Missing the required parameter 'address' when calling retrieveMultisigTransactions(Async)");
        }
        
        com.squareup.okhttp.Call call = retrieveMultisigTransactionsCall(xChainId, address, size, cursor, toTimestamp, fromTimestamp, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * RetrieveMultisigTransactions
     * List of Pending Transactions<p></p>  ## Size<p></p>  * The &#x60;size&#x60; query parameter is optional (minimum &#x3D; 1, maximum &#x3D; 1000, default &#x3D; 100).<br> * Submitting negative values result in errors.<br> * Submitting zero results in a query with &#x60;size&#x3D;100&#x60;, which is the default value.<br> * Submitting values greater than 1000 result in queries with &#x60;size&#x3D;1000&#x60;, which is the maximum value.<br>
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Account address which has multisig keys or signer&#x27;s account address (required)
     * @param size Maximum size of account to search (optional, default to 100)
     * @param cursor Information on last searched cursor (optional)
     * @param toTimestamp Timestamp of the end time to be searched (in seconds) (optional)
     * @param fromTimestamp Timestamp of the start time to be searched (in seconds) (optional)
     * @return MultisigTransactions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactions retrieveMultisigTransactions(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp) throws ApiException {
        ApiResponse<MultisigTransactions> resp = retrieveMultisigTransactionsWithHttpInfo(xChainId, address, size, cursor, toTimestamp, fromTimestamp);
        return resp.getData();
    }

    /**
     * RetrieveMultisigTransactions
     * List of Pending Transactions<p></p>  ## Size<p></p>  * The &#x60;size&#x60; query parameter is optional (minimum &#x3D; 1, maximum &#x3D; 1000, default &#x3D; 100).<br> * Submitting negative values result in errors.<br> * Submitting zero results in a query with &#x60;size&#x3D;100&#x60;, which is the default value.<br> * Submitting values greater than 1000 result in queries with &#x60;size&#x3D;1000&#x60;, which is the maximum value.<br>
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Account address which has multisig keys or signer&#x27;s account address (required)
     * @param size Maximum size of account to search (optional, default to 100)
     * @param cursor Information on last searched cursor (optional)
     * @param toTimestamp Timestamp of the end time to be searched (in seconds) (optional)
     * @param fromTimestamp Timestamp of the start time to be searched (in seconds) (optional)
     * @return ApiResponse&lt;MultisigTransactions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MultisigTransactions> retrieveMultisigTransactionsWithHttpInfo(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp) throws ApiException {
        com.squareup.okhttp.Call call = retrieveMultisigTransactionsValidateBeforeCall(xChainId, address, size, cursor, toTimestamp, fromTimestamp, null, null);
        Type localVarReturnType = new TypeToken<MultisigTransactions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * RetrieveMultisigTransactions (asynchronously)
     * List of Pending Transactions<p></p>  ## Size<p></p>  * The &#x60;size&#x60; query parameter is optional (minimum &#x3D; 1, maximum &#x3D; 1000, default &#x3D; 100).<br> * Submitting negative values result in errors.<br> * Submitting zero results in a query with &#x60;size&#x3D;100&#x60;, which is the default value.<br> * Submitting values greater than 1000 result in queries with &#x60;size&#x3D;1000&#x60;, which is the maximum value.<br>
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Account address which has multisig keys or signer&#x27;s account address (required)
     * @param size Maximum size of account to search (optional, default to 100)
     * @param cursor Information on last searched cursor (optional)
     * @param toTimestamp Timestamp of the end time to be searched (in seconds) (optional)
     * @param fromTimestamp Timestamp of the start time to be searched (in seconds) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call retrieveMultisigTransactionsAsync(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp, final ApiCallback<MultisigTransactions> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = retrieveMultisigTransactionsValidateBeforeCall(xChainId, address, size, cursor, toTimestamp, fromTimestamp, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MultisigTransactions>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for signPendingTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId ID of pending transaction (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call signPendingTransactionCall(String xChainId, String address, String transactionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/v2/multisig/account/{address}/tx/{transaction-id}/sign"
            .replaceAll("\\{" + "address" + "\\}", apiClient.escapeString(address.toString()))
            .replaceAll("\\{" + "transaction-id" + "\\}", apiClient.escapeString(transactionId.toString()));

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call signPendingTransactionValidateBeforeCall(String xChainId, String address, String transactionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling signPendingTransaction(Async)");
        }
        // verify the required parameter 'address' is set
        if (address == null) {
            throw new ApiException("Missing the required parameter 'address' when calling signPendingTransaction(Async)");
        }
        // verify the required parameter 'transactionId' is set
        if (transactionId == null) {
            throw new ApiException("Missing the required parameter 'transactionId' when calling signPendingTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = signPendingTransactionCall(xChainId, address, transactionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Sign to pending transaction
     * Sign to pending transaction from valid signer
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId ID of pending transaction (required)
     * @return MultisigTransactionStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactionStatus signPendingTransaction(String xChainId, String address, String transactionId) throws ApiException {
        ApiResponse<MultisigTransactionStatus> resp = signPendingTransactionWithHttpInfo(xChainId, address, transactionId);
        return resp.getData();
    }

    /**
     * Sign to pending transaction
     * Sign to pending transaction from valid signer
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId ID of pending transaction (required)
     * @return ApiResponse&lt;MultisigTransactionStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MultisigTransactionStatus> signPendingTransactionWithHttpInfo(String xChainId, String address, String transactionId) throws ApiException {
        com.squareup.okhttp.Call call = signPendingTransactionValidateBeforeCall(xChainId, address, transactionId, null, null);
        Type localVarReturnType = new TypeToken<MultisigTransactionStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Sign to pending transaction (asynchronously)
     * Sign to pending transaction from valid signer
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId ID of pending transaction (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call signPendingTransactionAsync(String xChainId, String address, String transactionId, final ApiCallback<MultisigTransactionStatus> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = signPendingTransactionValidateBeforeCall(xChainId, address, transactionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MultisigTransactionStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for signPendingTransactionBySig
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionId ID of pending transaction (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call signPendingTransactionBySigCall(String xChainId, String transactionId, SignPendingTransactionBySigRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/multisig/tx/{transaction-id}/sign"
            .replaceAll("\\{" + "transaction-id" + "\\}", apiClient.escapeString(transactionId.toString()));

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
    private com.squareup.okhttp.Call signPendingTransactionBySigValidateBeforeCall(String xChainId, String transactionId, SignPendingTransactionBySigRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling signPendingTransactionBySig(Async)");
        }
        // verify the required parameter 'transactionId' is set
        if (transactionId == null) {
            throw new ApiException("Missing the required parameter 'transactionId' when calling signPendingTransactionBySig(Async)");
        }
        
        com.squareup.okhttp.Call call = signPendingTransactionBySigCall(xChainId, transactionId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Sign pending transaction from signatures
     * Add Sign to pending transaction using prepared signatures. This API can be used when signer is not your own account but you got from signature from signer.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionId ID of pending transaction (required)
     * @param body  (optional)
     * @return MultisigTransactionStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactionStatus signPendingTransactionBySig(String xChainId, String transactionId, SignPendingTransactionBySigRequest body) throws ApiException {
        ApiResponse<MultisigTransactionStatus> resp = signPendingTransactionBySigWithHttpInfo(xChainId, transactionId, body);
        return resp.getData();
    }

    /**
     * Sign pending transaction from signatures
     * Add Sign to pending transaction using prepared signatures. This API can be used when signer is not your own account but you got from signature from signer.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionId ID of pending transaction (required)
     * @param body  (optional)
     * @return ApiResponse&lt;MultisigTransactionStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MultisigTransactionStatus> signPendingTransactionBySigWithHttpInfo(String xChainId, String transactionId, SignPendingTransactionBySigRequest body) throws ApiException {
        com.squareup.okhttp.Call call = signPendingTransactionBySigValidateBeforeCall(xChainId, transactionId, body, null, null);
        Type localVarReturnType = new TypeToken<MultisigTransactionStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Sign pending transaction from signatures (asynchronously)
     * Add Sign to pending transaction using prepared signatures. This API can be used when signer is not your own account but you got from signature from signer.
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param transactionId ID of pending transaction (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call signPendingTransactionBySigAsync(String xChainId, String transactionId, SignPendingTransactionBySigRequest body, final ApiCallback<MultisigTransactionStatus> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = signPendingTransactionBySigValidateBeforeCall(xChainId, transactionId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MultisigTransactionStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
