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

public class KAS {
    private AnchorAPI anchor;
    private TokenHistoryAPI tokenHistory;
    private WalletAPI wallet;

    public KAS() {
    }

    public KAS initAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setAnchor(new AnchorAPI(chainId, apiClient));
        return this;
    }

    public KAS initWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setWallet(new WalletAPI(chainId, apiClient));
        return this;
    }

    public KAS initTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setTokenHistory(new TokenHistoryAPI(chainId, apiClient));

        return this;
    }

    public AnchorAPI getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorAPI anchor) {
        this.anchor = anchor;
    }

    public TokenHistoryAPI getTokenHistory() {
        return tokenHistory;
    }

    public void setTokenHistory(TokenHistoryAPI tokenHistory) {
        this.tokenHistory = tokenHistory;
    }

    public WalletAPI getWallet() {
        return wallet;
    }

    public void setWallet(WalletAPI wallet) {
        this.wallet = wallet;
    }
}
