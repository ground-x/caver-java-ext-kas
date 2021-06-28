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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Account update transaction
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in [Accounts](https://ko.docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult accountUpdateTransaction(String xChainId, AccountUpdateTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = accountUpdateTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Account update transaction
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in [Accounts](https://ko.docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Account update transaction (asynchronously)
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in [Accounts](https://ko.docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Anchor transaction
     * Create a transaction for anchoring service chain data to Klaytn main chain.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult anchorTransaction(String xChainId, AnchorTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = anchorTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Anchor transaction
     * Create a transaction for anchoring service chain data to Klaytn main chain.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Anchor transaction (asynchronously)
     * Create a transaction for anchoring service chain data to Klaytn main chain.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Cancel transaction
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult cancelTransaction(String xChainId, CancelTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = cancelTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Cancel transaction
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Cancel transaction (asynchronously)
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Calls contract
     * Can parse data within the deloyed contract or decide whether the transaction can be executed.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ContractCallResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ContractCallResponse contractCall(String xChainId, ContractCallRequest body) throws ApiException {
        ApiResponse<ContractCallResponse> resp = contractCallWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Calls contract
     * Can parse data within the deloyed contract or decide whether the transaction can be executed.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Calls contract (asynchronously)
     * Can parse data within the deloyed contract or decide whether the transaction can be executed.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Contract deploy transaction
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult contractDeployTransaction(String xChainId, ContractDeployTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = contractDeployTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Contract deploy transaction
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Contract deploy transaction (asynchronously)
     * Create a transaction for deploying a contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Contract execution transaction
     * Create a transaction for executing the function of a deployed contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult contractExecutionTransaction(String xChainId, ContractExecutionTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = contractExecutionTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Contract execution transaction
     * Create a transaction for executing the function of a deployed contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Contract execution transaction (asynchronously)
     * Create a transaction for executing the function of a deployed contract.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Legacy transaction occurrence
     * Create a transaction that supports the legacy account (for which the public key is derived from the private key) and transaction formats. All newly created Klaytn accounts on KAS are legacy accounts.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult legacyTransaction(String xChainId, LegacyTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = legacyTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Legacy transaction occurrence
     * Create a transaction that supports the legacy account (for which the public key is derived from the private key) and transaction formats. All newly created Klaytn accounts on KAS are legacy accounts.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Legacy transaction occurrence (asynchronously)
     * Create a transaction that supports the legacy account (for which the public key is derived from the private key) and transaction formats. All newly created Klaytn accounts on KAS are legacy accounts.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Transaction using rlp
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature.  You can create a SigRLP without signature easily using caver.&lt;p&gt;&lt;/p&gt; If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, as long as the accounts have been created in the account pool. For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult processRLP(String xChainId, ProcessRLPRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = processRLPWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Transaction using rlp
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature.  You can create a SigRLP without signature easily using caver.&lt;p&gt;&lt;/p&gt; If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, as long as the accounts have been created in the account pool. For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Transaction using rlp (asynchronously)
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature.  You can create a SigRLP without signature easily using caver.&lt;p&gt;&lt;/p&gt; If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, as long as the accounts have been created in the account pool. For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Retrieve transaction
     * Retrieve transaction result with transaction hash value. You can find out whether the transaction was successful by checking the &#x60;status&#x60; field in the response.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param transactionHash Transaction hash value (required)
     * @return TransactionReceipt
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionReceipt transactionReceipt(String xChainId, String transactionHash) throws ApiException {
        ApiResponse<TransactionReceipt> resp = transactionReceiptWithHttpInfo(xChainId, transactionHash);
        return resp.getData();
    }

    /**
     * Retrieve transaction
     * Retrieve transaction result with transaction hash value. You can find out whether the transaction was successful by checking the &#x60;status&#x60; field in the response.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * Retrieve transaction (asynchronously)
     * Retrieve transaction result with transaction hash value. You can find out whether the transaction was successful by checking the &#x60;status&#x60; field in the response.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * KLAY transfer transaction
     * Create a transaction for KLAY transfers with/without memos.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return TransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TransactionResult valueTransferTransaction(String xChainId, ValueTransferTransactionRequest body) throws ApiException {
        ApiResponse<TransactionResult> resp = valueTransferTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * KLAY transfer transaction
     * Create a transaction for KLAY transfers with/without memos.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
     * KLAY transfer transaction (asynchronously)
     * Create a transaction for KLAY transfers with/without memos.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
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
