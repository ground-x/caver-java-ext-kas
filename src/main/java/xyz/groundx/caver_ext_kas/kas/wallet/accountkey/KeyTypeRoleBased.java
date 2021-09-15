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

package xyz.groundx.caver_ext_kas.kas.wallet.accountkey;


import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.OneOfRoleBasedUpdateKeyTypeKeyItems;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.RoleBasedUpdateKeyType;

import java.util.List;

public class KeyTypeRoleBased extends RoleBasedUpdateKeyType {
    public static final long KEY_TYPE = 5;

    public KeyTypeRoleBased() {
        super.setKeyType(KEY_TYPE);
    }

    public KeyTypeRoleBased(List<OneOfRoleBasedUpdateKeyTypeKeyItems> keyList) {
        super.setKeyType(KEY_TYPE);
        super.setKey(keyList);
    }
}
