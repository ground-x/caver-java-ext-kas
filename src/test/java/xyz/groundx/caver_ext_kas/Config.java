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
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17ConstantData;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.kct.kip37.KIP37;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7ConstantData;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.transaction.type.SmartContractExecution;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.squareup.okhttp.Credentials;
import io.github.cdimascio.dotenv.Dotenv;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import xyz.groundx.caver_ext_kas.kas.tokenhistory.KIP37ConstantData;
import xyz.groundx.caver_ext_kas.kas.tokenhistory.TokenHistoryTestData;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class Config {
    private static String ENV = "PROD";

    public static String URL_NODE_API = "https://node-api.klaytnapi.com/v1/klaytn";
    public static String URL_ANCHOR_API = "https://anchor-api.klaytnapi.com";
    public static String URL_TH_API = "https://th-api.klaytnapi.com";
    public static String URL_WALLET_API = "https://wallet-api.klaytnapi.com";
    public static String URL_KIP17_API = "https://kip17-api.klaytnapi.com";
    public static String URL_KIP7_API = "https://kip7-api.klaytnapi.com";
    public static String URL_KIP37_API = "https://kip37-api.klaytnapi.com";

    public static final String CHAIN_ID_BAOBOB = "1001";

    static String accessKey = "";
    static String secretAccessKey = "";

    public static String feePayerAddress = "";
    public static String operatorAddress = "";

    static String klayProviderPrivateKey = "";

    public static Integer presetID;

    public static CaverExtKAS caver;
    public static KeyringContainer keyringContainer;
    public static SingleKeyring klayProviderKeyring;

    public static TokenHistoryTestData tokenHistoryTestData;

    private static String loadEnvData(Dotenv env, String envName) {

        String data = System.getenv(envName);

        if(data == null) {
            data = env.get(envName);
        }

        if(data.equals("")) {
            throw new NullPointerException(envName + " is not exist.");
        }

        return data;
    }

    public static void init() {
        loadTestData();

        caver = new CaverExtKAS();
        caver.initNodeAPI(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_NODE_API);
        caver.initAnchorAPI(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_ANCHOR_API);
        caver.initWalletAPI(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_WALLET_API);
        caver.initTokenHistoryAPI(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_TH_API);
        caver.initKIP17API(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_KIP17_API);
        caver.initKIP7API(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_KIP7_API);
        caver.initKIP37API(CHAIN_ID_BAOBOB, accessKey, secretAccessKey, URL_KIP37_API);

        keyringContainer = new KeyringContainer();
        klayProviderKeyring = (SingleKeyring)keyringContainer.add(KeyringFactory.createFromPrivateKey(klayProviderPrivateKey));
    }


    public static void loadTestData() {
        Dotenv env = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        ENV = System.getProperty("TEST_ENV");
        if(ENV == null) {
            ENV = env.get("TEST_ENV", "PROD");
        }

        if(!ENV.equals("PROD") && !ENV.equals("QA") && !ENV.equals("DEV")) {
            throw new RuntimeException("Invalid Test ENV input data.");
        }

        String identifier = "";
        if(ENV.equals("QA") || ENV.equals("DEV")) {
            identifier = "_" + ENV;

            URL_NODE_API = loadEnvData(env, "URL_NODE_API" + identifier);
            URL_WALLET_API = loadEnvData(env,"URL_WALLET_API" + identifier);
            URL_TH_API = loadEnvData(env, "URL_TH_API" + identifier);
            URL_ANCHOR_API = loadEnvData(env, "URL_ANCHOR_API" + identifier);
            URL_KIP17_API = loadEnvData(env, "URL_KIP17_API" + identifier);
            URL_KIP7_API = loadEnvData(env, "URL_KIP7_API" + identifier);
            URL_KIP37_API = loadEnvData(env, "URL_KIP37_API" + identifier);
        }

        accessKey = accessKey.equals("") ? loadEnvData(env, "ACCESS_KEY" + identifier) : accessKey;
        secretAccessKey = secretAccessKey.equals("") ? loadEnvData(env, "SECRET_ACCESS_KEY" + identifier) : secretAccessKey;
        feePayerAddress = feePayerAddress.equals("") ? loadEnvData(env, "FEE_PAYER_ADDR" + identifier) : feePayerAddress;
        operatorAddress = operatorAddress.equals("") ? loadEnvData(env, "OPERATOR" + identifier) : operatorAddress;
        klayProviderPrivateKey = klayProviderPrivateKey.equals("") ? loadEnvData(env, "SENDER_PRV_KEY" + identifier) : klayProviderPrivateKey;

        presetID = presetID == null ? Integer.parseInt(loadEnvData(env, "PRESET" + identifier)) : presetID;
    }

    public static TransactionReceipt.TransactionReceiptData sendValue(String toAddress) throws IOException, TransactionException {
        init();

        BigInteger value = new BigInteger(Utils.convertToPeb("30", Utils.KlayUnit.KLAY));

        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setKlaytnCall(caver.rpc.getKlay())
                .setFrom(klayProviderKeyring.getAddress())
                .setTo(toAddress)
                .setValue(value)
                .setGas(BigInteger.valueOf(25000))
                .build();

        keyringContainer.sign(klayProviderKeyring.getAddress(), valueTransfer);
        Bytes32 result = caver.rpc.klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
        if(result.hasError()) {
            throw new RuntimeException(result.getError().getMessage());
        }

        //Check transaction receipt.
        TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        TransactionReceipt.TransactionReceiptData transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(result.getResult());

        return transactionReceipt;
    }

    public static BigInteger getBalance(String address) {
        init();
        try {
            Quantity response = caver.rpc.klay.getBalance(address).send();
            System.out.println(Utils.convertFromPeb(new BigDecimal(response.getValue()), Utils.KlayUnit.KLAY));

            return response.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String deployKIP7(Caver caver, String deployer) {
        try {
            KIP7 kip7 = new KIP7(caver);

            BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(18)); // 100000 * 10^18
            ContractDeployParams contractDeployParams = new ContractDeployParams(KIP7ConstantData.BINARY, "TEST", "TES", 18, initialSupply);

            String input = ABI.encodeContractDeploy(kip7.getConstructor(), contractDeployParams.getBytecode(), contractDeployParams.getDeployParams());

            SmartContractDeploy deployTx = new SmartContractDeploy.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(deployer)
                    .setInput(input)
                    .setCodeFormat(CodeFormat.EVM)
                    .setHumanReadable(false)
                    .setGas(BigInteger.valueOf(5500000))
                    .build();

            keyringContainer.sign(deployer, deployTx);
            Bytes32 res = caver.rpc.klay.sendRawTransaction(deployTx).send();
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData receiptData = processor.waitForTransactionReceipt(res.getResult());

            return receiptData.getContractAddress();
        } catch (IOException | ReflectiveOperationException | TransactionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String deployKIP17(Caver caver, String deployer) {
        try {
            String contractName = "TEST_KIP17";
            String contractSymbol = "KIP17";

            KIP17 kip17 = new KIP17(caver);
            ContractDeployParams contractDeployParams = new ContractDeployParams(KIP17ConstantData.BINARY, contractName, contractSymbol);

            String input = ABI.encodeContractDeploy(kip17.getConstructor(), contractDeployParams.getBytecode(), contractDeployParams.getDeployParams());

            SmartContractDeploy deployTx = new SmartContractDeploy.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(deployer)
                    .setInput(input)
                    .setCodeFormat(CodeFormat.EVM)
                    .setHumanReadable(false)
                    .setGas(BigInteger.valueOf(5500000))
                    .build();

            keyringContainer.sign(deployer, deployTx);
            Bytes32 res = caver.rpc.klay.sendRawTransaction(deployTx).send();
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData receiptData = processor.waitForTransactionReceipt(res.getResult());

            return receiptData.getContractAddress();
        } catch (IOException | ReflectiveOperationException | TransactionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void mintKIP17Token(Caver caver, String contractAddress, String ownerAddress, BigInteger tokenId) throws Exception {
        KIP17 kip17 = new KIP17(caver, contractAddress);
        SendOptions sendOptions = new SendOptions(ownerAddress, BigInteger.valueOf(5500000));

        String input = kip17.getMethod("mint").encodeABI(Arrays.asList(ownerAddress, tokenId));

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(ownerAddress)
                .setTo(contractAddress)
                .setInput(input)
                .setGas(BigInteger.valueOf(5500000))
                .build();

        keyringContainer.sign(ownerAddress, smartContractExecution);
        Bytes32 res = caver.rpc.klay.sendRawTransaction(smartContractExecution).send();
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        TransactionReceipt.TransactionReceiptData receiptData = processor.waitForTransactionReceipt(res.getResult());
    }

    public static String deployKIP37(Caver caver, String deployer) {
        try {
            KIP37 kip37 = KIP37.deploy(caver, "uri", deployer, keyringContainer);

            return kip37.getContractAddress();
        } catch (IOException | ReflectiveOperationException | TransactionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TransactionReceipt.TransactionReceiptData createTokenKIP37(String contractAddress, String minter, BigInteger tokenId) {
        try {
            Contract contract = new Contract(caver, KIP37ConstantData.ABI, contractAddress);

            String input = contract.getMethod("create").encodeABI(Arrays.asList(tokenId, BigInteger.valueOf(1), ""));

            SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(minter)
                    .setTo(contractAddress)
                    .setInput(input)
                    .setGas(BigInteger.valueOf(5500000))
                    .build();

            keyringContainer.sign(minter, smartContractExecution);
            Bytes32 res = caver.rpc.klay.sendRawTransaction(smartContractExecution).send();
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData receiptData = processor.waitForTransactionReceipt(res.getResult());

            return receiptData;
        } catch (IOException | ReflectiveOperationException | TransactionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TransactionReceipt.TransactionReceiptData mintBatchKIP37(String contractAddress, String minter, String owner) {
        try {
            BigInteger[] ids = {BigInteger.valueOf(1), BigInteger.valueOf(2)};
            BigInteger[] amount = {BigInteger.TEN, BigInteger.TEN};

            Contract contract = new Contract(caver, KIP37ConstantData.ABI, contractAddress);

            String input = contract.getMethod("mintBatch").encodeABI(Arrays.asList(owner, ids, amount));

            SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(minter)
                    .setTo(contractAddress)
                    .setInput(input)
                    .setGas(BigInteger.valueOf(5500000))
                    .build();

            keyringContainer.sign(minter, smartContractExecution);
            Bytes32 res = caver.rpc.klay.sendRawTransaction(smartContractExecution).send();
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData receiptData = processor.waitForTransactionReceipt(res.getResult());

            return receiptData;
        } catch (IOException | ReflectiveOperationException | TransactionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CaverExtKAS getCaver() {
        return caver;
    }

    public static String getFeePayerAddress() {
        return feePayerAddress;
    }

    public static String getOperatorAddress() {
        return operatorAddress;
    }

    public static SingleKeyring getKlayProviderKeyring() {
        return klayProviderKeyring;
    }

    public static Integer getPresetID() {
        return presetID;
    }

    public static TokenHistoryTestData getTokenHistoryTestData() {
        if(ENV.equals("DEV")) {
            return TokenHistoryTestData.loadDevData();
        } else if(ENV.equals("QA")) {
            return TokenHistoryTestData.loadQAData();
        } else {
            return TokenHistoryTestData.loadProdData();
        }
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static String getSecretAccessKey() {
        return secretAccessKey;
    }
}
