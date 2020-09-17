package com.klaytn.caver.ext.kas.wallet.accountkey;

import io.swagger.client.api.wallet.model.PubkeyUpdateKeyType;

public class KeyTypePublic extends PubkeyUpdateKeyType {
    public static final long KEY_TYPE = 2;

    public KeyTypePublic() {
        super.setKeyType(KEY_TYPE);
    }

    public KeyTypePublic(String key) {
        super.setKeyType(KEY_TYPE);
        super.setKey(key);
    }
}
