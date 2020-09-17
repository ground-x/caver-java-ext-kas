package com.klaytn.caver.ext.kas.wallet.accountkey;

import io.swagger.client.api.wallet.model.EmptyUpdateKeyType;

public class KeyTypeFail extends EmptyUpdateKeyType {
    public static final long KEY_TYPE = 3;

    public KeyTypeFail() {
        super.setKeyType(KEY_TYPE);
    }
}
