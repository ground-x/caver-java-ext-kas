package com.klaytn.caver.ext.kas.wallet.accountkey;

import io.swagger.client.api.wallet.model.AccountUpdateKey;
import io.swagger.client.api.wallet.model.RoleBasedUpdateKeyType;

import java.util.List;

public class KeyTypeRoleBased extends RoleBasedUpdateKeyType {
    public static final long KEY_TYPE = 5;

    public KeyTypeRoleBased() {
        super.setKeyType(KEY_TYPE);
    }

    public KeyTypeRoleBased(List<AccountUpdateKey> keyList) {
        super.setKeyType(KEY_TYPE);
        super.setKey(keyList);
    }
}
