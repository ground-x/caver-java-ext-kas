package com.klaytn.caver.ext.kas.wallet.accountkey;

import io.swagger.client.api.wallet.model.MultisigUpdateKey;
import io.swagger.client.api.wallet.model.MultisigUpdateKeyType;

public class KeyTypeMultiSig extends MultisigUpdateKeyType {
    public static final long KEY_TYPE = 4;

    public KeyTypeMultiSig() {
        super.setKeyType(KEY_TYPE);
    }

    public KeyTypeMultiSig(MultisigUpdateKey key) {
        super.setKeyType(KEY_TYPE);
        super.setKey(key);
    }
}
