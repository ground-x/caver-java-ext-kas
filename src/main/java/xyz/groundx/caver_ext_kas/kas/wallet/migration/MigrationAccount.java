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

import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * Representing a MigrationAccount which is used migrate external Klaytn account to KAS Wallet.
 */
public class MigrationAccount {
    private String address;
    private String nonce = "0x";
    private MigrationAccountKey<?> migrationAccountKey = null;

    public MigrationAccount(MigrationAccount.Builder builder) {
        this(builder.address,
                builder.nonce,
                builder.migrationAccountKey
        );
    }

    public MigrationAccount(String address, String nonce, MigrationAccountKey<?> migrationAccountKey) {
        setAddress(address);
        setNonce(nonce);
        setMigrationAccountKey(migrationAccountKey);
    }

    /**
     * Represents a MigrationAccount class builder.
     */
    public static class Builder {
        private String address;
        private String nonce = "0x";
        private MigrationAccountKey<?> migrationAccountKey = null;

        public MigrationAccount.Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public MigrationAccount.Builder setNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public MigrationAccount.Builder setNonce(BigInteger nonce) {
            this.nonce = Numeric.toHexStringWithPrefix(nonce);
            return this;
        }

        public MigrationAccount.Builder setMigrationAccountKey(MigrationAccountKey<?> migrationAccountKey) {
            this.migrationAccountKey = migrationAccountKey;
            return this;
        }

        public MigrationAccount build() {
            return new MigrationAccount(this);
        }
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
