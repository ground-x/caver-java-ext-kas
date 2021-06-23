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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDAccountUpdateTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDAnchorTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDCancelTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDContractDeployTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDContractExecutionTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDProcessRLPRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDTransactionResult;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDValueTransferTransactionRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeeDelegatedTransactionPaidByKasApi {
    private ApiClient apiClient;

    public FeeDelegatedTransactionPaidByKasApi() {
        this(Configuration.getDefaultApiClient());
    }

    public FeeDelegatedTransactionPaidByKasApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for fDAccountUpdateTransactionResponse
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDAccountUpdateTransactionResponseCall(String xChainId, FDAccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/account";

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
    private com.squareup.okhttp.Call fDAccountUpdateTransactionResponseValidateBeforeCall(String xChainId, FDAccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDAccountUpdateTransactionResponse(Async)");
        }
        
        com.squareup.okhttp.Call call = fDAccountUpdateTransactionResponseCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Global fee delegation account update transaction
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in  [Klaytn docs](https://docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDAccountUpdateTransactionResponse(String xChainId, FDAccountUpdateTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDAccountUpdateTransactionResponseWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Global fee delegation account update transaction
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in  [Klaytn docs](https://docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDAccountUpdateTransactionResponseWithHttpInfo(String xChainId, FDAccountUpdateTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDAccountUpdateTransactionResponseValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Global fee delegation account update transaction (asynchronously)
     * Create a transaction that updates Klaytn account keys. You can find Klaytn account key types in  [Klaytn docs](https://docs.klaytn.com/klaytn/design/accounts).&lt;p&gt;&lt;/p&gt; If you update the account to Legacy key type (1), you can start using your account once it is enabled. If you update the account to Public key type (2), the account cannot be used within the wallet. To restore your account, use Global Fee Delegation RLP API, or update to legacy externally and before enabling the account. If you update the key to Fail key type (3), the account will automatically be disabled.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDAccountUpdateTransactionResponseAsync(String xChainId, FDAccountUpdateTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDAccountUpdateTransactionResponseValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDAnchorTransaction
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDAnchorTransactionCall(String xChainId, FDAnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/anchor";

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
    private com.squareup.okhttp.Call fDAnchorTransactionValidateBeforeCall(String xChainId, FDAnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDAnchorTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = fDAnchorTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Anchoring transaction with global fee delegation
     * Create a transaction for anchoring blockchain data using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDAnchorTransaction(String xChainId, FDAnchorTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDAnchorTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Anchoring transaction with global fee delegation
     * Create a transaction for anchoring blockchain data using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDAnchorTransactionWithHttpInfo(String xChainId, FDAnchorTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDAnchorTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Anchoring transaction with global fee delegation (asynchronously)
     * Create a transaction for anchoring blockchain data using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDAnchorTransactionAsync(String xChainId, FDAnchorTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDAnchorTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDCancelTransactionResponse
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDCancelTransactionResponseCall(String xChainId, FDCancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd";

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
    private com.squareup.okhttp.Call fDCancelTransactionResponseValidateBeforeCall(String xChainId, FDCancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDCancelTransactionResponse(Async)");
        }
        
        com.squareup.okhttp.Call call = fDCancelTransactionResponseCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Global fee delegation cancel transaction
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDCancelTransactionResponse(String xChainId, FDCancelTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDCancelTransactionResponseWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Global fee delegation cancel transaction
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDCancelTransactionResponseWithHttpInfo(String xChainId, FDCancelTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDCancelTransactionResponseValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Global fee delegation cancel transaction (asynchronously)
     * Create a transaction for cancelling a pending transaction that had been sent to Klaytn with the KAS global fee payer account. To cancel, you need either the nonce or the transaction hash.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDCancelTransactionResponseAsync(String xChainId, FDCancelTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDCancelTransactionResponseValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDContractDeployTransaction
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDContractDeployTransactionCall(String xChainId, FDContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/contract/deploy";

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
    private com.squareup.okhttp.Call fDContractDeployTransactionValidateBeforeCall(String xChainId, FDContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDContractDeployTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = fDContractDeployTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Global Fee Delegation contract deploy transaction
     * Create a transaction for deploying contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDContractDeployTransaction(String xChainId, FDContractDeployTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDContractDeployTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Global Fee Delegation contract deploy transaction
     * Create a transaction for deploying contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDContractDeployTransactionWithHttpInfo(String xChainId, FDContractDeployTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDContractDeployTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Global Fee Delegation contract deploy transaction (asynchronously)
     * Create a transaction for deploying contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDContractDeployTransactionAsync(String xChainId, FDContractDeployTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDContractDeployTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDContractExecutionTransaction
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDContractExecutionTransactionCall(String xChainId, FDContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/contract/execute";

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
    private com.squareup.okhttp.Call fDContractExecutionTransactionValidateBeforeCall(String xChainId, FDContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDContractExecutionTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = fDContractExecutionTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Global fee delegation contract execution transaction
     * Create a transaction for executing contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDContractExecutionTransaction(String xChainId, FDContractExecutionTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDContractExecutionTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Global fee delegation contract execution transaction
     * Create a transaction for executing contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDContractExecutionTransactionWithHttpInfo(String xChainId, FDContractExecutionTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDContractExecutionTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Global fee delegation contract execution transaction (asynchronously)
     * Create a transaction for executing contracts using KAS global fee payer account.
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDContractExecutionTransactionAsync(String xChainId, FDContractExecutionTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDContractExecutionTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDProcessRLP
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDProcessRLPCall(String xChainId, FDProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/rlp";

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
    private com.squareup.okhttp.Call fDProcessRLPValidateBeforeCall(String xChainId, FDProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDProcessRLP(Async)");
        }
        
        com.squareup.okhttp.Call call = fDProcessRLPCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Transaction using global fee delegation RLP
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature. You can create a SigRLP without signature easily using caver. If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDProcessRLP(String xChainId, FDProcessRLPRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDProcessRLPWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Transaction using global fee delegation RLP
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature. You can create a SigRLP without signature easily using caver. If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDProcessRLPWithHttpInfo(String xChainId, FDProcessRLPRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDProcessRLPValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Transaction using global fee delegation RLP (asynchronously)
     * Create transaction using rlp (SigRLP or TxHashRLP). The rlp from the transaction API of Wallet API is in the TxHashRLP format, which includes signature. You can create a SigRLP without signature easily using caver. If you want to create a SigRLP for each transaction method on caver, use &#x60;getRLPEncodingForSignature()&#x60;, and &#x60;getRLPEncoding()&#x60; to create TxHashRLP. For SigRLP, you sign with the private key of the &#x60;from&#x60; account, For more details on SigRLP and TxHashRLP by each transaction type, please refer to [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDProcessRLPAsync(String xChainId, FDProcessRLPRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDProcessRLPValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for fDValueTransferTransaction
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call fDValueTransferTransactionCall(String xChainId, FDValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd/value";

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
    private com.squareup.okhttp.Call fDValueTransferTransactionValidateBeforeCall(String xChainId, FDValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling fDValueTransferTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = fDValueTransferTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Global fee delegation KLAY transfer transaction
     * Create a transaction for KLAY transfers with/without memos using KAS Global Fee Payer account.\&quot;
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult fDValueTransferTransaction(String xChainId, FDValueTransferTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = fDValueTransferTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Global fee delegation KLAY transfer transaction
     * Create a transaction for KLAY transfers with/without memos using KAS Global Fee Payer account.\&quot;
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> fDValueTransferTransactionWithHttpInfo(String xChainId, FDValueTransferTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = fDValueTransferTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Global fee delegation KLAY transfer transaction (asynchronously)
     * Create a transaction for KLAY transfers with/without memos using KAS Global Fee Payer account.\&quot;
     * @param xChainId Klaytn Chain Network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call fDValueTransferTransactionAsync(String xChainId, FDValueTransferTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = fDValueTransferTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
