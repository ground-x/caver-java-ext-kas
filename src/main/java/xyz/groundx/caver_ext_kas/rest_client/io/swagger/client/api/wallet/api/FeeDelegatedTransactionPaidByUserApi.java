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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDTransactionResult;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserAccountUpdateTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserAnchorTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserCancelTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserContractDeployTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserContractExecutionTransactionRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserProcessRLPRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FDUserValueTransferTransactionRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeeDelegatedTransactionPaidByUserApi {
    private ApiClient apiClient;

    public FeeDelegatedTransactionPaidByUserApi() {
        this(Configuration.getDefaultApiClient());
    }

    public FeeDelegatedTransactionPaidByUserApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for uFDAccountUpdateTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDAccountUpdateTransactionCall(String xChainId, FDUserAccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/account";

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
    private com.squareup.okhttp.Call uFDAccountUpdateTransactionValidateBeforeCall(String xChainId, FDUserAccountUpdateTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDAccountUpdateTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDAccountUpdateTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation Account Update Transaction
     * Create a transaction for updating Klaytn account keys with a user fee delegation account. For details about different Klaytn account keys, refer to [Klaytn Docs](https://ko.docs.klaytn.com/klaytn/design/accounts)<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDAccountUpdateTransaction(String xChainId, FDUserAccountUpdateTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDAccountUpdateTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation Account Update Transaction
     * Create a transaction for updating Klaytn account keys with a user fee delegation account. For details about different Klaytn account keys, refer to [Klaytn Docs](https://ko.docs.klaytn.com/klaytn/design/accounts)<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDAccountUpdateTransactionWithHttpInfo(String xChainId, FDUserAccountUpdateTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDAccountUpdateTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation Account Update Transaction (asynchronously)
     * Create a transaction for updating Klaytn account keys with a user fee delegation account. For details about different Klaytn account keys, refer to [Klaytn Docs](https://ko.docs.klaytn.com/klaytn/design/accounts)<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDAccountUpdateTransactionAsync(String xChainId, FDUserAccountUpdateTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDAccountUpdateTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDAnchorTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDAnchorTransactionCall(String xChainId, FDUserAnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/anchor";

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
    private com.squareup.okhttp.Call uFDAnchorTransactionValidateBeforeCall(String xChainId, FDUserAnchorTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDAnchorTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDAnchorTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation Anchoring Transaction
     * Create a transaction for anchoring service chain data to the main chain using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDAnchorTransaction(String xChainId, FDUserAnchorTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDAnchorTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation Anchoring Transaction
     * Create a transaction for anchoring service chain data to the main chain using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDAnchorTransactionWithHttpInfo(String xChainId, FDUserAnchorTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDAnchorTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation Anchoring Transaction (asynchronously)
     * Create a transaction for anchoring service chain data to the main chain using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDAnchorTransactionAsync(String xChainId, FDUserAnchorTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDAnchorTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDContractDeployTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDContractDeployTransactionCall(String xChainId, FDUserContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/contract/deploy";

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
    private com.squareup.okhttp.Call uFDContractDeployTransactionValidateBeforeCall(String xChainId, FDUserContractDeployTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDContractDeployTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDContractDeployTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation Contract Release Transaction
     * Create a transaction for releasing a contract using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDContractDeployTransaction(String xChainId, FDUserContractDeployTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDContractDeployTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation Contract Release Transaction
     * Create a transaction for releasing a contract using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDContractDeployTransactionWithHttpInfo(String xChainId, FDUserContractDeployTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDContractDeployTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation Contract Release Transaction (asynchronously)
     * Create a transaction for releasing a contract using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDContractDeployTransactionAsync(String xChainId, FDUserContractDeployTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDContractDeployTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDContractExecutionTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDContractExecutionTransactionCall(String xChainId, FDUserContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/contract/execute";

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
    private com.squareup.okhttp.Call uFDContractExecutionTransactionValidateBeforeCall(String xChainId, FDUserContractExecutionTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDContractExecutionTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDContractExecutionTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation Contract Execution Transaction
     * Create a transaction for executing a released contract function using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDContractExecutionTransaction(String xChainId, FDUserContractExecutionTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDContractExecutionTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation Contract Execution Transaction
     * Create a transaction for executing a released contract function using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDContractExecutionTransactionWithHttpInfo(String xChainId, FDUserContractExecutionTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDContractExecutionTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation Contract Execution Transaction (asynchronously)
     * Create a transaction for executing a released contract function using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDContractExecutionTransactionAsync(String xChainId, FDUserContractExecutionTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDContractExecutionTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDProcessRLP
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDProcessRLPCall(String xChainId, FDUserProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/rlp";

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
    private com.squareup.okhttp.Call uFDProcessRLPValidateBeforeCall(String xChainId, FDUserProcessRLPRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDProcessRLP(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDProcessRLPCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Transaction Using User Fee Delegation RLP
     * Create a transaction using the rlp(SigRLP or TxHashRLP) with user fee payer account. Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDProcessRLP(String xChainId, FDUserProcessRLPRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDProcessRLPWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * Transaction Using User Fee Delegation RLP
     * Create a transaction using the rlp(SigRLP or TxHashRLP) with user fee payer account. Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDProcessRLPWithHttpInfo(String xChainId, FDUserProcessRLPRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDProcessRLPValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Transaction Using User Fee Delegation RLP (asynchronously)
     * Create a transaction using the rlp(SigRLP or TxHashRLP) with user fee payer account. Rlp value from transaction API is TxHashRLP format which contains signatures. SigRLP which does not contain signatures can easily be made from caver.<p></p>  If you want to make SigRLP, you can use method &#x60;getRLPEncodingForSignature()&#x60; of certain transaction object. If you want to make TxHashRLP, you can use method &#x60;getRLPEncoding()&#x60; of certain transaction object. If you give SigRLP in rlp value, we sign the trasnaction using &#x60;from&#x60; address in your account pool. If you need detail description about SigRLP, TxHashRLP of each of transaction, you can refer [Klaytn Docs](https://docs.klaytn.com/klaytn/design/transactions).<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDProcessRLPAsync(String xChainId, FDUserProcessRLPRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDProcessRLPValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDUserCancelTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDUserCancelTransactionCall(String xChainId, FDUserCancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user";

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
    private com.squareup.okhttp.Call uFDUserCancelTransactionValidateBeforeCall(String xChainId, FDUserCancelTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDUserCancelTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDUserCancelTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation Cancellation Transaction
     * Create a transaction for canceling a pending transaction for a transfer to Klaytn using the user fee delegation account. Either a nonce or transaction hash is required for cancellation.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDUserCancelTransaction(String xChainId, FDUserCancelTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDUserCancelTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation Cancellation Transaction
     * Create a transaction for canceling a pending transaction for a transfer to Klaytn using the user fee delegation account. Either a nonce or transaction hash is required for cancellation.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDUserCancelTransactionWithHttpInfo(String xChainId, FDUserCancelTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDUserCancelTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation Cancellation Transaction (asynchronously)
     * Create a transaction for canceling a pending transaction for a transfer to Klaytn using the user fee delegation account. Either a nonce or transaction hash is required for cancellation.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDUserCancelTransactionAsync(String xChainId, FDUserCancelTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDUserCancelTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for uFDValueTransferTransaction
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call uFDValueTransferTransactionCall(String xChainId, FDUserValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/v2/tx/fd-user/value";

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
    private com.squareup.okhttp.Call uFDValueTransferTransactionValidateBeforeCall(String xChainId, FDUserValueTransferTransactionRequest body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'xChainId' is set
        if (xChainId == null) {
            throw new ApiException("Missing the required parameter 'xChainId' when calling uFDValueTransferTransaction(Async)");
        }
        
        com.squareup.okhttp.Call call = uFDValueTransferTransactionCall(xChainId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * User Fee Delegation KLAY Transfer Transaction
     * Create a transaction for transferring KLAYs with a memo using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return FDTransactionResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FDTransactionResult uFDValueTransferTransaction(String xChainId, FDUserValueTransferTransactionRequest body) throws ApiException {
        ApiResponse<FDTransactionResult> resp = uFDValueTransferTransactionWithHttpInfo(xChainId, body);
        return resp.getData();
    }

    /**
     * User Fee Delegation KLAY Transfer Transaction
     * Create a transaction for transferring KLAYs with a memo using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @return ApiResponse&lt;FDTransactionResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FDTransactionResult> uFDValueTransferTransactionWithHttpInfo(String xChainId, FDUserValueTransferTransactionRequest body) throws ApiException {
        com.squareup.okhttp.Call call = uFDValueTransferTransactionValidateBeforeCall(xChainId, body, null, null);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * User Fee Delegation KLAY Transfer Transaction (asynchronously)
     * Create a transaction for transferring KLAYs with a memo using the user fee delegation account.<p></p>  If you want to use this API, you need two kind of &#x60;x-krn&#x60; about &#x60;account-pool&#x60; and &#x60;feepayer-pool&#x60;. Two kind of &#x60;x-krn&#x60; can be written with comma like below example.<p></p>  &#x60;&#x60;&#x60; x-krn: krn:1001:wallet:{{account-id}}:account-pool:{{account-pool-id}},krn:1001:wallet:{{account-id}}:feepayer-pool:{{feepayer-pool-id}} &#x60;&#x60;&#x60;
     * @param xChainId Klaytn chain network ID (1001 or 8217) (required)
     * @param body  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call uFDValueTransferTransactionAsync(String xChainId, FDUserValueTransferTransactionRequest body, final ApiCallback<FDTransactionResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = uFDValueTransferTransactionValidateBeforeCall(xChainId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FDTransactionResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
