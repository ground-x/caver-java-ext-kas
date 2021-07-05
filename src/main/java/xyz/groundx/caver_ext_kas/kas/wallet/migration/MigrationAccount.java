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
     * The nonce value of the account to be migrated in hexadecimal format. <br>
     * Notice: Even if you set the nonce value much higher than the original account has, the API response will be Ok. <br>
     * Therefore, it is recommended not to directly specify the nonce value.
     */
    private String nonce = "0x";

    /**
     * A instance of MigrationAccountKey representing various type of private key.
     */
    private MigrationAccountKey<?> migrationAccountKey = null;

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param singlePrivateKey A private of account to be migrated.
     */
    public MigrationAccount(String address, String singlePrivateKey) {
        this(address, singlePrivateKey, "0x");
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param multisigPrivateKeys Multiple private keys of account to be migrated.
     */
    public MigrationAccount(String address, String[] multisigPrivateKeys) {
        this(address, multisigPrivateKeys, "0x");
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param roleBasedPrivateKeys A list of multiple private keys of account to be migrated.
     */
    public MigrationAccount(String address, List<String[]> roleBasedPrivateKeys) {
        this(address, roleBasedPrivateKeys, "0x");
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param singlePrivateKey A private of account to be migrated.
     * @param nonce A nonce value in hexadecimal format of account to be migrated.
     */
    public MigrationAccount(String address, String singlePrivateKey, String nonce) {
        setAddress(address);
        setMigrationAccountKey(new SinglePrivateKey(singlePrivateKey));
        setNonce(nonce);
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param singlePrivateKey A private of account to be migrated.
     * @param nonce A nonce value of the BigInteger type of account to be migrated
     */
    public MigrationAccount(String address, String singlePrivateKey, BigInteger nonce) {
        setAddress(address);
        setMigrationAccountKey(new SinglePrivateKey(singlePrivateKey));
        setNonce(nonce);
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param multisigPrivateKeys Multiple private keys of account to be migrated.
     * @param nonce A nonce value in hexadecimal format of account to be migrated.
     */
    public MigrationAccount(String address, String[] multisigPrivateKeys, String nonce) {
        setAddress(address);
        setMigrationAccountKey(new MultisigPrivateKeys(multisigPrivateKeys));
        setNonce(nonce);
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param multisigPrivateKeys Multiple private keys of account to be migrated.
     * @param nonce A nonce value of the BigInteger type of account to be migrated
     */
    public MigrationAccount(String address, String[] multisigPrivateKeys, BigInteger nonce) {
        setAddress(address);
        setMigrationAccountKey(new MultisigPrivateKeys(multisigPrivateKeys));
        setNonce(nonce);
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param roleBasedPrivateKeys A list of multiple private keys of account to be migrated.
     * @param nonce A nonce value in hexadecimal format of account to be migrated.
     */
    public MigrationAccount(String address, List<String[]> roleBasedPrivateKeys, String nonce) {
        setAddress(address);
        setMigrationAccountKey(new RoleBasedPrivateKeys(roleBasedPrivateKeys));
        setNonce(nonce);
    }

    /**
     * Create a MigrationAccount instance.
     * @param address An address of account to be migrated.
     * @param roleBasedPrivateKeys A list of multiple private keys of account to be migrated.
     * @param nonce A nonce value of the BigInteger type of account to be migrated
     */
    public MigrationAccount(String address, List<String[]> roleBasedPrivateKeys, BigInteger nonce) {
        setAddress(address);
        setMigrationAccountKey(new RoleBasedPrivateKeys(roleBasedPrivateKeys));
        setNonce(nonce);
    }

    /**
     * Get an address of account to be migrated.
     * @return String An address of account to be migrated.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Set an address of account to be migrated.
     * @param address An address of account to be migrated.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get a nonce value in hexadecimal format.
     * @return String A nonce value of account to be migrated
     */
    public String getNonce() {
        return this.nonce;
    }

    /**
     * Set a nonce value.
     * @param nonce A nonce value of account to be migrated
     */
    public void setNonce(BigInteger nonce) {
        setNonce(Numeric.toHexStringWithPrefix(nonce));
    }

    /**
     * Set a nonce value in hexadecimal format.
     * @param nonce A nonce value in hexadecimal format of account to be migrated.
     */
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    /**
     * Get a MigrationKey of account to be migrated.
     * @return {@code MigrationAccountKey<?>} The migrationAccountKey of account to be migrated.
     */
    public MigrationAccountKey<?> getMigrationAccountKey() {
        return this.migrationAccountKey;
    }

    /**
     * Set a MigrationKey of account to be migrated.
     * @param key The migrationAccountKey of account to be migrated.
     */
    public void setMigrationAccountKey(MigrationAccountKey<?> key) {
        this.migrationAccountKey = key;
    }
}
