package com.klaytn.caver.kas;

import com.klaytn.caver.Caver;
import com.klaytn.caver.kas.anchor.v1.AnchorAPIV1;
import com.klaytn.caver.kas.node.v1.NodeAPIV1;
import com.squareup.okhttp.Credentials;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

public class KAS extends Caver {
    private AnchorAPIV1 anchorAPI;

    public KAS() {
    }

    public KAS(AnchorAPIV1 anchorAPI) {
        this.anchorAPI = anchorAPI;
    }

    public KAS enableAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        this.anchorAPI = new AnchorAPIV1(url, Credentials.basic(accessKeyId, secretAccessKey), chainId);

        return this;
    }

    public KAS enableNodeAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-krn", "krn:"+chainId+":node");

        //TODO : Temporary code.
        httpService.addHeader("x-chain-id", chainId);
        this.getRpc().setWeb3jService(httpService);

        return this;
    }

    public AnchorAPIV1 getAnchorAPI() {
        return anchorAPI;
    }

    public void setAnchorAPI(AnchorAPIV1 anchorAPI) {
        this.anchorAPI = anchorAPI;
    }
}
