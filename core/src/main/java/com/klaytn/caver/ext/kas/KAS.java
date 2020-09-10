package com.klaytn.caver.ext.kas;

import com.klaytn.caver.Caver;
import com.klaytn.caver.ext.kas.anchor.AnchorAPI;
import com.klaytn.caver.ext.kas.tokenhistory.TokenHistoryAPI;
import com.klaytn.caver.ext.kas.wallet.WalletAPI;
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

    public KAS enableAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setAnchorAPI(new AnchorAPI(chainId, apiClient));
        return this;
    }

    public KAS enableWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(url);
        apiClient.setUsername(accessKeyId);
        apiClient.setPassword(secretAccessKey);

        setWalletAPI(new WalletAPI(chainId, apiClient));
        return this;
    }

    public KAS enableNodeAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);
        this.rpc = new RPC(httpService);

        return this;
    }

    public KAS enableTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
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
