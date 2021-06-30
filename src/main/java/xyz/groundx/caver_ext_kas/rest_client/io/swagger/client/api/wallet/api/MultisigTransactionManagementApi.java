/*
 * Wallet API
 * # Introduction Wallet API is an API for creating and managing Klaytn accounts as well as sending transactions. If you create your Klaytn account using Wallet API, you don't have to manage your private key yourself. Wallet API provides a wallet for safe storage of your Klaytn account private keys that you would need to use BApps. For more details on how to use Wallet API, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial). Wallet API can be divided into the Account part, which creates and manages Klaytn accounts, and the Transaction part, which sends different kinds of transactions. Wallet API creates, deletes and monitors Klaytn accounts and updates the accounts to multisig, and manages all private keys for all accounts registered on KAS. Wallet API can also create transaction to send it to Klaytn. These include transactions sent from multisig accounts. In case of muiltisig accounts, a transaction will automatically be sent to Klaytn once \\(Threshold\\) is met. For more detail, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial). There are mainly two types of transactions: basic transactions and fee delegation transactions. Fee delegation transactions include Global Fee Delegation transaction and user fee deletation transaction. With the Global Fee Delegation transaction scheme, the transaction fee will initially be paid by GroundX and then be charged to you at a later date. With the User Fee Delegation transaction scheme, you create an account that pays the transaction fees on behalf of the users when a transaction. The functionalities and limits of Wallet API are shown below: | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limits | Supports Cypress(Mainnet), Baobab(Testnet) \\ Doesn't support (Service Chain \\) | |  |  | Doesn't support account management for external custodial keys | |  |  | Doesn't support multisig for RLP encoded transactions | |  | Account management | Create, retrieve and delete account | |  |  | Multisig account update | |  | Managing transaction | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) creating and sending transaction | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) creating and sending transaction | |  |  | RLP encoded transaction\\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation)\\) creating and sending | |  |  | Managing and sending multisig transactions | |  | Administrator | Manage resource pool\\(create, query pool, delete, retrieve account \\) | # Error Codes ## 400: Bad Request  | Code | Messages |   | --- | --- |   | 1061010 | data don't exist</br>data don't exist; krn:1001:wallet:68ec0e4b-0f61-4e6f-ae35-be865ab23187:account-pool:default:0x9b2f4d85d7f7abb14db229b5a81f1bdca0aa24c8ff0c4c100b3f25098b7a6152 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0x18925BDD724614bF13Bd5d53a74adFd228903796</br>account is invalid to sign the transaction; 0x6d06e7cA9F26d6D30B3b4Dff6084E74C51908fef 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; transaction-id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; keyId</br>cannot be empty or zero value; address 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | fee ratio must be between 1 and 99; feeRatio 1061903 | failed to decode account keys; runtime error: slice bounds out of range [:64] with length 4 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0x6f2e9235a48a86c3a7912b4237f83e760609c7ca609bbccbf648c8617a3a980c</br>already submitted transaction. Confirm transaction hash; 0xfb1fae863da42bcefdde3d572404bf5fcb89c1809e9253d5fff7c07a4bb5210f 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account</br>failed to get an account; data don't exist</br>failed to get an account; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |  
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Account address or user&#x27;s account address with multisig keys (required)
     * @param size Maximum size of the accounts to be queried (optional, default to 100)
     * @param cursor Information on the last cursor (optional)
     * @param toTimestamp Limit of the time range to be queried (Timestamp in seconds) (optional)
     * @param fromTimestamp Starting point of the time range to be queried (Timestamp in seconds) (optional)
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
     * Retrieve pending transactions
     * Retrieve pending transactions that had been send from a multisig account.<p></p> ## Size<p></p> * The query parameter &#x60;size&#x60; is optional. (Min &#x3D; 1, Max &#x3D; 1000, Default &#x3D; 100)<br> * Returns an error when given a negative number<br> * Uses default value (&#x60;size&#x3D;100&#x60;) when &#x60;size&#x3D;0&#x60;<br> * Uses the maximum value (&#x60;size&#x3D;1000&#x60;) when given a value higher than the maximum value.<br>
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Account address or user&#x27;s account address with multisig keys (required)
     * @param size Maximum size of the accounts to be queried (optional, default to 100)
     * @param cursor Information on the last cursor (optional)
     * @param toTimestamp Limit of the time range to be queried (Timestamp in seconds) (optional)
     * @param fromTimestamp Starting point of the time range to be queried (Timestamp in seconds) (optional)
     * @return MultisigTransactions
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactions retrieveMultisigTransactions(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp) throws ApiException {
        ApiResponse<MultisigTransactions> resp = retrieveMultisigTransactionsWithHttpInfo(xChainId, address, size, cursor, toTimestamp, fromTimestamp);
        return resp.getData();
    }

    /**
     * Retrieve pending transactions
     * Retrieve pending transactions that had been send from a multisig account.<p></p> ## Size<p></p> * The query parameter &#x60;size&#x60; is optional. (Min &#x3D; 1, Max &#x3D; 1000, Default &#x3D; 100)<br> * Returns an error when given a negative number<br> * Uses default value (&#x60;size&#x3D;100&#x60;) when &#x60;size&#x3D;0&#x60;<br> * Uses the maximum value (&#x60;size&#x3D;1000&#x60;) when given a value higher than the maximum value.<br>
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Account address or user&#x27;s account address with multisig keys (required)
     * @param size Maximum size of the accounts to be queried (optional, default to 100)
     * @param cursor Information on the last cursor (optional)
     * @param toTimestamp Limit of the time range to be queried (Timestamp in seconds) (optional)
     * @param fromTimestamp Starting point of the time range to be queried (Timestamp in seconds) (optional)
     * @return ApiResponse&lt;MultisigTransactions&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MultisigTransactions> retrieveMultisigTransactionsWithHttpInfo(String xChainId, String address, Long size, String cursor, Long toTimestamp, Long fromTimestamp) throws ApiException {
        com.squareup.okhttp.Call call = retrieveMultisigTransactionsValidateBeforeCall(xChainId, address, size, cursor, toTimestamp, fromTimestamp, null, null);
        Type localVarReturnType = new TypeToken<MultisigTransactions>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve pending transactions (asynchronously)
     * Retrieve pending transactions that had been send from a multisig account.<p></p> ## Size<p></p> * The query parameter &#x60;size&#x60; is optional. (Min &#x3D; 1, Max &#x3D; 1000, Default &#x3D; 100)<br> * Returns an error when given a negative number<br> * Uses default value (&#x60;size&#x3D;100&#x60;) when &#x60;size&#x3D;0&#x60;<br> * Uses the maximum value (&#x60;size&#x3D;1000&#x60;) when given a value higher than the maximum value.<br>
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Account address or user&#x27;s account address with multisig keys (required)
     * @param size Maximum size of the accounts to be queried (optional, default to 100)
     * @param cursor Information on the last cursor (optional)
     * @param toTimestamp Limit of the time range to be queried (Timestamp in seconds) (optional)
     * @param fromTimestamp Starting point of the time range to be queried (Timestamp in seconds) (optional)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId Pending transaction ID (required)
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
     * Sign pending transaction
     * A valid signer Signs a pending transaction.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId Pending transaction ID (required)
     * @return MultisigTransactionStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactionStatus signPendingTransaction(String xChainId, String address, String transactionId) throws ApiException {
        ApiResponse<MultisigTransactionStatus> resp = signPendingTransactionWithHttpInfo(xChainId, address, transactionId);
        return resp.getData();
    }

    /**
     * Sign pending transaction
     * A valid signer Signs a pending transaction.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId Pending transaction ID (required)
     * @return ApiResponse&lt;MultisigTransactionStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MultisigTransactionStatus> signPendingTransactionWithHttpInfo(String xChainId, String address, String transactionId) throws ApiException {
        com.squareup.okhttp.Call call = signPendingTransactionValidateBeforeCall(xChainId, address, transactionId, null, null);
        Type localVarReturnType = new TypeToken<MultisigTransactionStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Sign pending transaction (asynchronously)
     * A valid signer Signs a pending transaction.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param address Signer&#x27;s account address (required)
     * @param transactionId Pending transaction ID (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param transactionId Pending transaction ID (required)
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
     * Sign transaction with a signature value
     * Add a signature to a pending transaction with a signature value. You can obtain a signature value externally from an account that you don&#x27;t own, and use it to add to transactions.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param transactionId Pending transaction ID (required)
     * @param body  (optional)
     * @return MultisigTransactionStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MultisigTransactionStatus signPendingTransactionBySig(String xChainId, String transactionId, SignPendingTransactionBySigRequest body) throws ApiException {
        ApiResponse<MultisigTransactionStatus> resp = signPendingTransactionBySigWithHttpInfo(xChainId, transactionId, body);
        return resp.getData();
    }

    /**
     * Sign transaction with a signature value
     * Add a signature to a pending transaction with a signature value. You can obtain a signature value externally from an account that you don&#x27;t own, and use it to add to transactions.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param transactionId Pending transaction ID (required)
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
     * Sign transaction with a signature value (asynchronously)
     * Add a signature to a pending transaction with a signature value. You can obtain a signature value externally from an account that you don&#x27;t own, and use it to add to transactions.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param transactionId Pending transaction ID (required)
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
