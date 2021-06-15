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
 * RoleBasedPrivateKeys presents a list of multiple private keys for role based account type.
 */
public class RoleBasedPrivateKeys extends MigrationAccountKey<List<String[]>> {
    /**
     * A list of multiple private keys of external Klaytn account to be migrated to KAS Wallet.
     */
    List<String[]> key;

    /**
     * Create a RoleBasedPrivateKeys instance which will be used as member variable of MigrationAccount.
     * @param roleBasedKey A list of multiple private keys of external Klaytn account to be migrated to KAS Wallet.
     */
    public RoleBasedPrivateKeys(List<String[]> roleBasedKey) {
        this.key = roleBasedKey;
    }

    /**
     * Get a list of multiple private keys of account to be migrated to KAS Wallet.
     * @return {@code List<String[]>} A list of multiple private key.
     */
    @Override
    public List<String[]> getKey() {
        return this.key;
    }
}
