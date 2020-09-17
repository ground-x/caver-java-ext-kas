package com.klaytn.caver.ext.kas.wallet.accountkey;

import io.swagger.client.api.wallet.model.EmptyUpdateKeyType;

public class KeyTypeLegacy extends EmptyUpdateKeyType {
    public static final long KEY_TYPE = 1;

    public KeyTypeLegacy() {
        super.setKeyType(KEY_TYPE);
    }
}
