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

import com.klaytn.caver.rpc.RPC;
import xyz.groundx.caver_ext_kas.kas.anchor.Anchor;
import xyz.groundx.caver_ext_kas.kas.kip17.KIP17;
import xyz.groundx.caver_ext_kas.kas.kip37.KIP37;
import xyz.groundx.caver_ext_kas.kas.kip7.KIP7;
import xyz.groundx.caver_ext_kas.kas.tokenhistory.TokenHistory;
import xyz.groundx.caver_ext_kas.kas.wallet.Wallet;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;

/**
 * Representing a wrapping class to use KAS API in rest_client package.
 */
public class KAS {
    /**
     * The Anchor API instance.
     */
    public Anchor anchor;

    /**
     * The Token history API instance.
     */
    public TokenHistory tokenHistory;

    /**
     * The Wallet API instance.
     */
    public Wallet wallet;

    /**
     * The KIP17 API instance.
     */
    public KIP17 kip17;

    /**
     * The KIP7 API instance.
     */
    public KIP7 kip7;

    /**
     * The KIP37 API instance.
     */
    public KIP37 kip37;

    /**
     * Creates a KAS instance.
     */
    public KAS() {
    }

    /**
     * Initialize Anchor API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Anchor API.
     */
    public KAS initAnchorAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setAnchor(new Anchor(chainId, apiClient));
        return this;
    }

    /**
     * Initialize Wallet API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Wallet API.
     */
    public KAS initWalletAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setWallet(new Wallet(chainId, apiClient));
        return this;
    }

    /**
     * Initialize Token History API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Token History API.
     */
    public KAS initTokenHistoryAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setTokenHistory(new TokenHistory(chainId, apiClient));

        return this;
    }

    /**
     * Initialize KIP17 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP17 API.
     */
    public KAS initKIP17API(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setKip17(new KIP17(chainId, apiClient));

        return this;
    }

    /**
     * Initialize KIP7 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP7 API.
     */
    public KAS initKIP7API(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setKip7(new KIP7(chainId, apiClient));

        return this;
    }

    /**
     * Initialize KIP37 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP37 API.
     */
    public KAS initKIP37API(String chainId, String accessKeyId, String secretAccessKey, String url) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setKip37(new KIP37(chainId, apiClient));

        return this;
    }

    /**
     * Getter function for anchor.
     * @return Anchor API
     */
    public Anchor getAnchor() {
        return anchor;
    }

    /**
     * Setter function for anchor.
     * @param anchor The Anchor API instance.
     */
    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    /**
     * Getter function for tokenHistory.
     * @return TokenHistory
     */
    public TokenHistory getTokenHistory() {
        return tokenHistory;
    }

    /**
     * Setter function for Token History.
     * @param tokenHistory The TokenHistory API instance.
     */
    public void setTokenHistory(TokenHistory tokenHistory) {
        this.tokenHistory = tokenHistory;
    }

    /**
     * Getter function for wallet.
     * @return Wallet
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Setter function for Wallet API instance.
     * @param wallet The WalletAPI instance.
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Getter function for kip17.
     * @return KIP17
     */
    public KIP17 getKip17() {
        return kip17;
    }

    /**
     * Setter function for KIP17.
     * @param kip17 The KIP17 API instance.
     */
    public void setKip17(KIP17 kip17) {
        this.kip17 = kip17;
    }

    /**
     * Getter function for kip7.
     * @return KIP7
     */
    public KIP7 getKip7() {
        return kip7;
    }

    /**
     * Setter function for KIP7.
     * @param kip7 The KIP7 API instance.
     */
    public void setKip7(KIP7 kip7) {
        this.kip7 = kip7;
    }

    /**
     * Getter function for kip37.
     * @return KIP37
     */
    public KIP37 getKip37() {
        return kip37;
    }

    /**
     * Setter function for KIP37.
     * @param kip37 The KIP37 API instance.
     */
    public void setKip37(KIP37 kip37) {
        this.kip37 = kip37;
    }
}
