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
import com.klaytn.caver.wallet.IWallet;
import com.squareup.okhttp.Credentials;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import xyz.groundx.caver_ext_kas.kas.KAS;
import xyz.groundx.caver_ext_kas.wallet.KASWallet;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Representing wrapping class that can use Klaytn API Service
 */
public class CaverExtKAS extends Caver {
    private static final String URL_NODE_API = "https://node-api.klaytnapi.com/v1/klaytn";
    private static final String URL_ANCHOR_API = "https://anchor-api.klaytnapi.com";
    private static final String URL_TH_API = "https://th-api.klaytnapi.com";
    private static final String URL_WALLET_API = "https://wallet-api.klaytnapi.com";
    private static final String URL_KIP17_API = "https://kip17-api.klaytnapi.com";
    private static final String URL_KIP7_API = "https://kip7-api.klaytnapi.com";

    /**
     * The KAS instance.
     */
    public KAS kas;

    /**
     * The KAS wallet instance.
     */
    public KASWallet wallet;

    /**
     * The boolean to check whether NodeApi is initialized or not.
     */
    private boolean nodeAPIInitialized;

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
        this();
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
        this();
        initKASAPI(chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Creates a CaverExtKAS instance.<br>
     * It init all supported KAS API(Node API, Anchor API, Wallet API).<br>
     * It can choose provider that Http or Websocket via options parameter.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param options The ConfigOptions instance to config CaverExtKAS.
     */
    public CaverExtKAS(int chainId, String accessKeyId, String secretAccessKey, ConfigOptions options) {
        this();
        initKASAPI(chainId, accessKeyId, secretAccessKey, options);
    }

    /**
     * Creates a CaverExtKAS instance.<br>
     * It init all supported KAS API(Node API, Anchor API, Wallet API).<br>
     * It can choose provider that Http or Websocket via options parameter.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param options The ConfigOptions instance to config CaverExtKAS.
     */
    public CaverExtKAS(String chainId, String accessKeyId, String secretAccessKey, ConfigOptions options) {
        this();
        initKASAPI(chainId, accessKeyId, secretAccessKey, options);
    }

    /**
     * Initialize all KAS API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKASAPI(int chainId, String accessKeyId, String secretAccessKey) {
        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        initKASAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, options);
    }

    /**
     * Initialize all KAS API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKASAPI(String chainId, String accessKeyId, String secretAccessKey) {
        ConfigOptions options = new ConfigOptions();
        options.setUseNodeAPIWithHttp(true);

        initKASAPI(chainId, accessKeyId, secretAccessKey, options);
    }

    /**
     * Initialize all KAS API.<br>
     * It can choose provider that Http or Websocket via options parameter.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param options The ConfigOptions instance to config CaverExtKAS.
     */
    public void initKASAPI(int chainId, String accessKeyId, String secretAccessKey, ConfigOptions options) {
        initKASAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, options);
    }

    /**
     * Initialize all KAS API.<br>
     * It can choose provider that Http or Websocket via options parameter.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param options The ConfigOptions instance to config CaverExtKAS.
     */
    public void initKASAPI(String chainId, String accessKeyId, String secretAccessKey, ConfigOptions options) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, options.getUseNodeAPIWithHttp());
        initAnchorAPI(chainId,accessKeyId, secretAccessKey);
        initTokenHistoryAPI(chainId, accessKeyId, secretAccessKey);
        initWalletAPI(chainId, accessKeyId, secretAccessKey);
        initKIP17API(chainId, accessKeyId, secretAccessKey);
        initKIP7API(chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Initialize Node API with Http provider.<br>
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API);
    }

    /**
     * Initialize Node API with Http provider.<br>
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API);
    }

    /**
     * Initialize Node API.<br>
     * It sets a url to default endpoint automatically.<br>
     * It can choose provider that Http or Websocket via useNodeAPIWithHttp parameter.
     *
     * <pre>
     * {@code
     * caver.initNodeAPI(1001, "accessKey", "secretAccessKey", true); // use Http provider
     * caver.initNodeAPI(1001, "accessKey", "secretAccessKey", false); // use Websocket provider
     * }
     * </pre>
     *
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param useNodeAPIWithHttp If true, HttpProvider is used. If false, WebsocketProvider is used.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey, boolean useNodeAPIWithHttp) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API, useNodeAPIWithHttp);
    }

    /**
     * Initialize Node API.<br>
     * It sets a url to default endpoint automatically.<br>
     * It can choose provider that Http or Websocket via useNodeAPIWithHttp parameter.
     *
     * <pre>
     * {@code
     * caver.initNodeAPI("1001", "accessKey", "secretAccessKey", true); // use Http provider
     * caver.initNodeAPI("1001", "accessKey", "secretAccessKey", false); // use Websocket provider
     * }
     * </pre>
     *
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param useNodeAPIWithHttp If true, HttpProvider is used. If false, WebsocketProvider is used.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey, boolean useNodeAPIWithHttp) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, URL_NODE_API, useNodeAPIWithHttp);
    }

    /**
     * Initialize Node API with Http provider.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, url, true);
    }

    /**
     * Initialize Node API with Http provider.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey, String url) {
        initNodeAPI(chainId, accessKeyId, secretAccessKey, url, true);
    }

    /**
     * Initialize Node API.<br>
     * It can choose provider that Http or Websocket via useNodeAPIWithHttp parameter.
     *
     * <pre>
     * {@code
     * caver.initNodeAPI(1001, "accessKey", "secretAccessKey", url, true); // use Http provider
     * caver.initNodeAPI(1001, "accessKey", "secretAccessKey", url, false); // use Websocket provider
     * }
     * </pre>
     *
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     * @param useNodeAPIWithHttp If true, HttpProvider is used. If false, WebsocketProvider is used.
     */
    public void initNodeAPI(int chainId, String accessKeyId, String secretAccessKey, String url, boolean useNodeAPIWithHttp) {
        initNodeAPI(String.valueOf(chainId), accessKeyId, secretAccessKey, url, useNodeAPIWithHttp);
    }

    /**
     * Initialize Node API.<br>
     * It can choose provider that Http or Websocket via useNodeAPIWithHttp parameter.
     *
     * <pre>
     * {@code
     * caver.initNodeAPI("1001", "accessKey", "secretAccessKey", url, true); // use Http provider
     * caver.initNodeAPI("1001", "accessKey", "secretAccessKey", url, false); // use Websocket provider
     * }
     * </pre>
     *
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request Node API.
     * @param useNodeAPIWithHttp If true, HttpProvider is used. If false, WebsocketProvider is used.
     */
    public void initNodeAPI(String chainId, String accessKeyId, String secretAccessKey, String url, boolean useNodeAPIWithHttp) {
        if(useNodeAPIWithHttp) {
            initNodeAPIWithHttp(chainId, accessKeyId, secretAccessKey, url);
        } else {
            initNodeAPIWithWebSocket(chainId, accessKeyId, secretAccessKey, url);
        }
        this.nodeAPIInitialized = true;
        if (this.kas.wallet != null) {
            kas.wallet.setNodeAPIInitialized(this.nodeAPIInitialized);
        }
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
        kas.wallet.setRPC(this.rpc);
        kas.wallet.setNodeAPIInitialized(this.nodeAPIInitialized);
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
     * Initialize KIP17 API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKIP17API(int chainId, String accessKeyId, String secretAccessKey) {
        initKIP17API(chainId, accessKeyId, secretAccessKey, URL_KIP17_API);
    }

    /**
     * Initialize KIP17 API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKIP17API(String chainId, String accessKeyId, String secretAccessKey) {
        initKIP17API(chainId, accessKeyId, secretAccessKey, URL_KIP17_API);
    }

    /**
     * Initialize KIP17 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP17 API.
     */
    public void initKIP17API(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initKIP17API(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize KIP17 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP17 API.
     */
    public void initKIP17API(String chainId, String accessKeyId, String secretAccessKey, String url) {
        kas.initKIP17API(chainId, accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize KIP7 API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKIP7API(int chainId, String accessKeyId, String secretAccessKey) {
        initKIP7API(chainId, accessKeyId, secretAccessKey, URL_KIP7_API);
    }

    /**
     * Initialize KIP7 API.
     * It sets a url to default endpoint automatically.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initKIP7API(String chainId, String accessKeyId, String secretAccessKey) {
        initKIP7API(chainId, accessKeyId, secretAccessKey, URL_KIP7_API);
    }

    /**
     * Initialize KIP7 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP17 API.
     */
    public void initKIP7API(int chainId, String accessKeyId, String secretAccessKey, String url) {
        initKIP7API(String.valueOf(chainId), accessKeyId, secretAccessKey, url);
    }

    /**
     * Initialize KIP7 API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     * @param url An URL to request KIP17 API.
     */
    public void initKIP7API(String chainId, String accessKeyId, String secretAccessKey, String url) {
        kas.initKIP7API(chainId, accessKeyId, secretAccessKey, url);
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

    private void initNodeAPIWithHttp(String chainId, String accessKeyId, String secretAccessKey, String url) {
        if(url.equals("https://node-api.klaytnapi.com")) {
            url = url + "/v1/klaytn";
        }

        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);

        this.setCurrentProvider(httpService);
        if (this.kas.wallet != null) {
            this.kas.wallet.setRPC(this.rpc);
        }
    }

    private void initNodeAPIWithWebSocket(String chainId, String accessKeyId, String secretAccessKey, String url) {
        Pattern regex = Pattern.compile("^[a-zA-Z0-9]*$");

        if(!regex.matcher(accessKeyId).matches() || !regex.matcher(secretAccessKey).matches()) {
            throw new InvalidParameterException("Invalid auth: To use the websocket provider, you must use an accessKey and secretAccessKey that do not contain special characters. Please obtain a new AccessKey through the KAS Console.");
        }

        String websocketUrl = url.replace("https", "wss");
        websocketUrl = websocketUrl.replace("v1/klaytn", "v1/ws/open?chain-id=" + chainId);

        try {
            Map header = new HashMap<>();
            header.put("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
            WebSocketClient webSocketClient = new WebSocketClient(new URI(websocketUrl), header);
            WebSocketService webSocketService = new WebSocketService(webSocketClient, false);

            this.setCurrentProvider(webSocketService);
            if (this.kas.wallet != null) {
                this.kas.wallet.setRPC(this.rpc);
            }
            webSocketService.connect();
        } catch(URISyntaxException e) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", websocketUrl), e);
        } catch(ConnectException e) {
            throw new RuntimeException(String.format("Failed to connect URL: '%s'", websocketUrl), e);
        }
    }
}
