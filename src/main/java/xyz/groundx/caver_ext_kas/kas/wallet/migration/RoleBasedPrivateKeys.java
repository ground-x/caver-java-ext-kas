/*
 * Copyright 2021 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.kas.wallet.migration;

import java.util.List;

/**
 * RoleBasedPrivateKeys presents a set of private keys of role based account type.
 */
public class RoleBasedPrivateKeys extends MigrationAccountKey<List<String[]>> {
    List<String[]> key;

    public RoleBasedPrivateKeys(List<String[]> roleBasedKey) {
        this.key = roleBasedKey;
    }

    @Override
    public List<String[]> getKey() {
        return this.key;
    }
}
