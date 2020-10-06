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
import com.squareup.okhttp.Credentials;
import org.web3j.protocol.http.HttpService;
import xyz.groundx.caver_ext_kas.kas.KAS;

/**
 * Representing wrapping class that can use Klaytn API Service
 */
public class CaverExtKAS extends Caver {
    /**
     * The KAS instance.
     */
    KAS kas;

    /**
     * Creates a CaverExtKAS instance.
     */
    public CaverExtKAS() {
        this.kas = new KAS();
    }

    /**
     * Initialize Node API.
     * @param url An URL to request Node API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initNodeAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);
        this.rpc = new RPC(httpService);
    }

    /**
     * Initialize Anchor API.
     * @param url An URL to request Anchor API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initAnchorAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Initialize Wallet API.
     * @param url An URL to request Wallet API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initWalletAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Initialize Token History API.
     * @param url An URL to request Token History API.
     * @param chainId The Klaytn network chain id.
     * @param accessKeyId The access key provided by KAS console.
     * @param secretAccessKey The secret key provided by KAS console.
     */
    public void initTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initTokenHistoryAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    /**
     * Getter function for KAS instance.
     * @return KAS
     */
    public KAS getKas() {
        return kas;
    }

    /**
     * Setter function for KAS instance
     * @param kas The KAS instance.
     */
    public void setKas(KAS kas) {
        this.kas = kas;
    }
}
