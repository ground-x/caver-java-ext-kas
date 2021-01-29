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
import com.klaytn.caver.rpc.RPC;
import com.klaytn.caver.wallet.IWallet;
import com.squareup.okhttp.Credentials;
import org.web3j.protocol.http.HttpService;
import xyz.groundx.caver_ext_kas.kas.KAS;
import xyz.groundx.caver_ext_kas.wallet.KASWallet;

/**
 * Representing wrapping class that can use Klaytn API Service
 */
public class CaverExtKAS extends Caver {
    private static final String URL_NODE_API = "https://node-api.klaytnapi.com/v1/klaytn";
    private static final String URL_ANCHOR_API = "https://anchor-api.klaytnapi.com";
    private static final String URL_TH_API = "https://th-api.klaytnapi.com";
    private static final String URL_WALLET_API = "https://wallet-api.klaytnapi.com";

    /**
     * The KAS instance.
     */
    public KAS kas;

    /**
     * The KAS wallet instance.
     */
    public KASWallet wallet;

    /**
     * Creates a CaverExtKAS instance.<br>
     * It need to init each KAS API manually.
     */
    public CaverExtKAS() {
        this.kas = new KAS();
    }

    /**
     * Creates a CaverExtKAS instance.<br>
     * It init all supported KAS API(Node API, Anchor API, Wallet API)
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public CaverExtKAS(int chainId, String accessKeyId, String secretAccessKey) {
        this.kas = new KAS();
        initKASAPI(chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Creates a CaverExtKAS instance.<br>
     * It init all supported KAS API(Node API, Anchor API, Wallet API)
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public CaverExtKAS(String chainId, String accessKeyId, String secretAccessKey) {
        this.kas = new KAS();
        initKASAPI(chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Initialize all KAS API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKASAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initKASAPI(String.valueOf(chainId), accessKeyId, secretAccessKey);
    }

    /**
     * Initialize all KAS API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKASAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey);
        initAnchorAPI(chainId,accessKeyId, secretAccessKey);
        initTokenHistoryAPI(chainId, accessKeyId, secretAccessKey);
        initWalletAPI(chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Initialize Node API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API);
    }

    /**
     * Initialize Node API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API);
    }

    /**
     * Initialize Node API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initNodeAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize Node API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        if(url.equals("https://node-api.klaytnapi.com")) {
            url = url + "/v1/klaytn";
        }

        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);
        this.rpc = new RPC(httpService);
    }

    /**
     * Initialize Anchor API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initAnchorAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initAnchorAPI(chainId, accessKeyId, secretAccessKey, URL_ANCHOR_API);
    }

    /**
     * Initialize Anchor API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initAnchorAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initAnchorAPI(chainId, accessKeyId, secretAccessKey, URL_ANCHOR_API);
    }

    /**
     * Initialize Anchor API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Anchor API.
     */
    public void initAnchorAPI(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initAnchorAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize Anchor API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Anchor API.
     */
    public void initAnchorAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        kas.initAnchorAPI(chainId, accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize Wallet API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initWalletAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initWalletAPI(chainId, accessKeyId, secretAccessKey, URL_WALLET_API);
    }

    /**
     * Initialize Wallet API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initWalletAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initWalletAPI(chainId, accessKeyId, secretAccessKey, URL_WALLET_API);
    }

    /**
     * Initialize Wallet API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Wallet API.
     */
    public void initWalletAPI(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initWalletAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize Wallet API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Wallet API.
     */
    public void initWalletAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        kas.initWalletAPI(chainId, accessKeyId, secretAccessKey, url);
        setWallet(new KASWallet(this.kas.wallet));
    }

    /**
     * Initialize Token History API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initTokenHistoryAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initTokenHistoryAPI(chainId, accessKeyId, secretAccessKey, URL_TH_API);
    }

    /**
     * Initialize Token History API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initTokenHistoryAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initTokenHistoryAPI(chainId, accessKeyId, secretAccessKey, URL_TH_API);
    }

    /**
     * Initialize Token History API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Token History API.
     */
    public void initTokenHistoryAPI(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initTokenHistoryAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize Token History API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Token History API.
     */
    public void initTokenHistoryAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        kas.initTokenHistoryAPI(chainId, accessKeyId, secretAccessKey, url);
    }

    /**
     * Getter function for KAS instance.
     * @return KAS
     */
    public KAS getKas() {
        return kas;
    }

    @Override
    public IWallet getWallet() {
        return this.wallet;
    }

    /**
     * Setter function for KAS instance
     * @param kas The KAS instance.
     */
    public void setKas(KAS kas) {
        this.kas = kas;
    }

    public void setWallet(KASWallet wallet) {
        this.wallet = wallet;
    }
}
