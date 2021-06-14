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
 * Representing a MigrationAccount which is used migrate external Klaytn account to KAS Wallet.
 */
public class MigrationAccount {
    /**
     * An address of the account to be migrated.
     */
    private String address;

    /**
     * The nonce value of the account to be migrated in hexadecimal format.
     */
    private String nonce = "0x";

    /**
     * A instance of MigrationAccountKey representing various type of private key.
     */
    private MigrationAccountKey<?> migrationAccountKey = null;

    public MigrationAccount(String address, String singlePrivateKey) {
        this(address, "0x", singlePrivateKey);
    }

    public MigrationAccount(String address, String[] multisigPrivateKeys) {
        this(address, "0x", multisigPrivateKeys);
    }

    public MigrationAccount(String address, List<String[]> roleBasedPrivateKeys) {
        this(address, "0x", roleBasedPrivateKeys);
    }

    public MigrationAccount(String address, String nonce, String singlePrivateKey) {
        setAddress(address);
        setNonce(nonce);
        setMigrationAccountKey(new SinglePrivateKey(singlePrivateKey));
    }

    public MigrationAccount(String address, String nonce, String[] multisigPrivateKeys) {
        setAddress(address);
        setNonce(nonce);
        setMigrationAccountKey(new MultisigPrivateKeys(multisigPrivateKeys));
    }

    public MigrationAccount(String address, String nonce, List<String[]> roleBasedPrivateKeys) {
        setAddress(address);
        setNonce(nonce);
        setMigrationAccountKey(new RoleBasedPrivateKeys(roleBasedPrivateKeys));
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public MigrationAccountKey<?> getMigrationAccountKey() {
        return this.migrationAccountKey;
    }

    public void setMigrationAccountKey(MigrationAccountKey<?> key) {
        this.migrationAccountKey = key;
    }
}
