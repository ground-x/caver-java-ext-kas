package xyz.groundx.caver_ext_kas.node;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Quantity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.protocol.websocket.WebSocketService;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.ConfigOptions;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class WebSocketTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void init() {
        Config.init();
    }

    @Test
    public void initializeWebSocketTest() throws IOException {
        CaverExtKAS caver = new CaverExtKAS();
        caver.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API, false);

        assertTrue(caver.currentProvider instanceof WebSocketService);
        caver.currentProvider.close();
    }

    @Test
    public void checkAccessKeyWithSpecialCharacter() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Invalid auth: To use the websocket provider, you must use an accessKey and secretAccessKey that do not contain special characters. Please obtain a new AccessKey through the KAS Console.");

        CaverExtKAS caver = new CaverExtKAS();

        String id = "KASFAKEACCESSKEYID++++****&&&&&";
        String pwd = "KASFAKESECRETACCESSKEY";

        caver.initNodeAPI(Config.CHAIN_ID_BAOBOB, id, pwd, Config.URL_NODE_API, false);
    }

    @Test
    public void checkSecretKeyWithSpecialCharacter() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Invalid auth: To use the websocket provider, you must use an accessKey and secretAccessKey that do not contain special characters. Please obtain a new AccessKey through the KAS Console.");

        CaverExtKAS caver = new CaverExtKAS();

        String id = "KASFAKEACCESSKEYID";
        String pwd = "KASFAKESECRETACCESSKEY=";

        caver.initNodeAPI(Config.CHAIN_ID_BAOBOB, id, pwd, Config.URL_NODE_API, false);
    }

    @Test
    public void getBlockNumber() throws IOException {
        CaverExtKAS caver = new CaverExtKAS();
        caver.initNodeAPI(Config.CHAIN_ID_BAOBOB, Config.getAccessKey(), Config.getSecretAccessKey(), Config.URL_NODE_API, false);

        Quantity blockNumberRes = caver.rpc.klay.getBlockNumber().send();
        assertNotNull(blockNumberRes.getValue());

        caver.currentProvider.close();
    }

    @Test
    public void checkSecretAccessKeyRegex() {
        Pattern regexSecretAccessKey = Pattern.compile("^[^?=&+\\s]+$"); // allow to include ?, ,=,&,+

        String pwd = "KASFAKESECRETACCESSKEY";
        assertEquals(true, !regexSecretAccessKey.matcher(pwd+"=").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"[").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"]").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"$").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"_").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"-").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"~").matches());
        assertEquals(true, !regexSecretAccessKey.matcher(pwd+"?").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"<").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"\\").matches());
        assertEquals(false, !regexSecretAccessKey.matcher(pwd+"/").matches());
        assertEquals(true, !regexSecretAccessKey.matcher(pwd+" ").matches());
        assertEquals(true, !regexSecretAccessKey.matcher(pwd+"+").matches());
        assertEquals(true, !regexSecretAccessKey.matcher(pwd+"&").matches());
    }
}
