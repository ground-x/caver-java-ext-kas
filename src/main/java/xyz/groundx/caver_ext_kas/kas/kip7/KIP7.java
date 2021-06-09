package xyz.groundx.caver_ext_kas.kas.kip7;

import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.api.Kip7Api;

public class KIP7 {
    Kip7Api kip7Api;

    String chainId;

    ApiClient apiClient;

    public KIP7(String chainId, ApiClient apiClient) {
        this.chainId = chainId;
        this.apiClient = apiClient;
    }

    public Kip7Api getKip7Api() {
        return kip7Api;
    }

    public void setKip7Api(Kip7Api kip7Api) {
        this.kip7Api = kip7Api;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setKip7Api(new Kip7Api(apiClient));
    }
}
