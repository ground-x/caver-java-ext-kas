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

package xyz.groundx.caver_ext_kas;

import com.klaytn.caver.Caver;
import xyz.groundx.caver_ext_kas.tokenhistory.TokenHistoryAPI;
import xyz.groundx.caver_ext_kas.anchor.AnchorAPI;
import xyz.groundx.caver_ext_kas.wallet.WalletAPI;
import com.klaytn.caver.rpc.RPC;
import com.squareup.okhttp.Credentials;
import io.swagger.client.ApiClient;
import org.web3j.protocol.http.HttpService;

public class KAS extends Caver {
    private AnchorAPI anchorAPI;
    private TokenHistoryAPI tokenHistoryAPI;
    private WalletAPI walletAPI;

    public KAS() {
    }

    public KAS initAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setAnchorAPI(new AnchorAPI(chainId, apiClient));
        return this;
    }

    public KAS initWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setWalletAPI(new WalletAPI(chainId, apiClient));
        return this;
    }

    public KAS initNodeAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);
        this.rpc = new RPC(httpService);

        return this;
    }

    public KAS initTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setTokenHistoryAPI(new TokenHistoryAPI(chainId, apiClient));

        return this;
    }

    public AnchorAPI getAnchorAPI() {
        return anchorAPI;
    }

    public void setAnchorAPI(AnchorAPI anchorAPI) {
        this.anchorAPI = anchorAPI;
    }

    public TokenHistoryAPI getTokenHistoryAPI() {
        return tokenHistoryAPI;
    }

    public void setTokenHistoryAPI(TokenHistoryAPI tokenHistoryAPI) {
        this.tokenHistoryAPI = tokenHistoryAPI;
    }

    public WalletAPI getWalletAPI() {
        return walletAPI;
    }

    public void setWalletAPI(WalletAPI walletAPI) {
        this.walletAPI = walletAPI;
    }
}
