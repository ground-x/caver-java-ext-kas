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

import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Config {
    static final String accessKey = "KASKPC4Y2BI5R9S102XZQ6HQ";
    static final String secretAccessKey = "A46xEUiEP72ReGfNENktb29CUkMb6VXRV0Ovq1QO";

    static KAS kas;
    public static SingleKeyring richAccount;
    static final String richAccountPrivateKey = "0xc34442bae4b74023081d8fb05003eb13f11c40d6fc79b9f30c9caa036947ffe8";

    static void init() {
        kas = new KAS();
        kas.initNodeAPI("https://node-api.dev.klaytn.com/v1/klaytn", "1001", accessKey, secretAccessKey);

        richAccount = (SingleKeyring)kas.wallet.add(KeyringFactory.createFromPrivateKey(richAccountPrivateKey));
    }

    public static void sendValue(String toAddress) throws IOException, TransactionException {
        init();

        BigInteger value = new BigInteger(Utils.convertToPeb("1", Utils.KlayUnit.KLAY));

        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setKlaytnCall(kas.rpc.getKlay())
                .setFrom(richAccount.getAddress())
                .setTo(toAddress)
                .setValue(value)
                .setGas(BigInteger.valueOf(25000))
                .build();

        kas.wallet.sign(richAccount.getAddress(), valueTransfer);
        Bytes32 result = kas.rpc.klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
        if(result.hasError()) {
            throw new RuntimeException(result.getError().getMessage());
        }

        //Check transaction receipt.
        TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(kas, 1000, 15);
        TransactionReceipt.TransactionReceiptData transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(result.getResult());
    }

    public static BigInteger getBalance(String address) {
        init();
        try {
            Quantity response = kas.rpc.klay.getBalance(address).send();
            System.out.println(Utils.convertFromPeb(new BigDecimal(response.getValue()), Utils.KlayUnit.KLAY));

            return response.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void checkBalance() {
        init();
        getBalance("0xF09900677531faAa51D9032169EBcB7CDF434B41");
    }
}
