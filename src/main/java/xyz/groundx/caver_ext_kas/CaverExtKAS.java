package xyz.groundx.caver_ext_kas;

import com.klaytn.caver.Caver;
import com.klaytn.caver.rpc.RPC;
import com.squareup.okhttp.Credentials;
import org.web3j.protocol.http.HttpService;
import xyz.groundx.caver_ext_kas.kas.KAS;

public class CaverExtKAS extends Caver {
    KAS kas;

    public CaverExtKAS() {
        this.kas = new KAS();
    }

    public void initNodeAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        HttpService httpService = new HttpService(url);

        httpService.addHeader("Authorization", Credentials.basic(accessKeyId, secretAccessKey));
        httpService.addHeader("x-chain-id", chainId);
        this.rpc = new RPC(httpService);
    }

    public void initAnchorAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initAnchorAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    public void initWalletAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initWalletAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    public void initTokenHistoryAPI(String url, String chainId, String accessKeyId, String secretAccessKey) {
        kas.initTokenHistoryAPI(url, chainId, accessKeyId, secretAccessKey);
    }

    public KAS getKas() {
        return kas;
    }

    public void setKas(KAS kas) {
        this.kas = kas;
    }
}
