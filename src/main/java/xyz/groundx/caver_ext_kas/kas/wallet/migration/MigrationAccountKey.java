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

/**
 * MigrationAccountKey represents a type of private key.
 * @param <T> Basic data types that make up the various types of Migration Account Key types.<br>
 * e.g. {@code String, String[], List<String[]>}
 */
public abstract class MigrationAccountKey<T> {
    /**
     * Get private key of account to be migrated to KAS Wallet.
     * @return T Generic type which can be one of {@code String, String[], List<String[]>}.
     */
    abstract public T getKey();
}