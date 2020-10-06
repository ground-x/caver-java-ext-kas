/*
 * Copyright 2020 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.kas;

import xyz.groundx.caver_ext_kas.kas.anchor.AnchorAPI;
import xyz.groundx.caver_ext_kas.kas.tokenhistory.TokenHistoryAPI;
import xyz.groundx.caver_ext_kas.kas.wallet.WalletAPI;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;

/**
 * Representing a wrapping class to use KAS API in rest_client package.
 */
public class KAS {
    /**
     * The Anchor API instance.
     */
    public AnchorAPI anchor;

    /**
     * The Token history API instance.
     */
    public TokenHistoryAPI tokenHistory;

    /**
     * The Wallet API instance.
     */
    public WalletAPI wallet;

    /**
     * Creates a KAS instance.
     */
    public KAS() {
    }

    /**
     * Initialize Anchor API.
     * @param url An URL to request Anchor API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public KAS initAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setAnchor(new AnchorAPI(chainId, apiClient));
        return this;
    }

    /**
     * Initialize Wallet API.
     * @param url An URL to request Wallet API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public KAS initWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setWallet(new WalletAPI(chainId, apiClient));
        return this;
    }

    /**
     * Initialize Token History API.
     * @param url An URL to request Token History API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public KAS initTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setTokenHistory(new TokenHistoryAPI(chainId, apiClient));

        return this;
    }

    /**
     * Getter function for anchor.
     * @return Anchor API
     */
    public AnchorAPI getAnchor() {
        return anchor;
    }

    /**
     * Setter function for anchor.
     * @param anchor The Anchor API instance.
     */
    public void setAnchor(AnchorAPI anchor) {
        this.anchor = anchor;
    }

    /**
     * Getter function for Token History
     * @return TokenHistoryAPI.
     */
    public TokenHistoryAPI getTokenHistory() {
        return tokenHistory;
    }

    /**
     * Setter function for Token History.
     * @param tokenHistory The Token History API Instance
     */
    public void setTokenHistory(TokenHistoryAPI tokenHistory) {
        this.tokenHistory = tokenHistory;
    }

    /**
     * Getter function for wallet
     * @return WalletAPI
     */
    public WalletAPI getWallet() {
        return wallet;
    }

    /**
     * Setter function for Wallet
     * @param wallet The WalletAPI instance.
     */
    public void setWallet(WalletAPI wallet) {
        this.wallet = wallet;
    }
}
