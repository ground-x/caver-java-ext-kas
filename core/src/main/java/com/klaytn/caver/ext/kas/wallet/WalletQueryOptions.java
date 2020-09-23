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

package com.klaytn.caver.ext.kas.wallet;

import com.klaytn.caver.ext.kas.utils.KASUtils;

/**
 * Representing a query parameters where using Wallet REST API.
 */
public class WalletQueryOptions {
    /**
     * Maximum number of data to query
     */
    Long size;

    /**
     * Information of the last retrieved cursor.
     */
    String cursor;

    /**
     * The from-timestamp in seconds to query.
     */
    Long fromTimestamp;

    /**
     * The to-timestamp in seconds to query.
     */
    Long toTimestamp;

    /**
     * Creates an WalletQueryOptions instance.
     */
    public WalletQueryOptions() {
    }

    /**
     * Creates an WalletQueryOptions instance.
     * @param size Maximum number of data to query.
     * @param cursor Information of the last retrieved cursor.
     * @param fromTimestamp The from-timestamp to query.
     * @param toTimestamp The to-timestamp to query.
     */
    public WalletQueryOptions(Long size, String cursor, Long fromTimestamp, Long toTimestamp) {
        this.size = size;
        this.cursor = cursor;
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
    }

    /**
     * Getter function for size.
     * @return Long
     */
    public Long getSize() {
        return size;
    }

    /**
     * Getter function for cursor.
     * @return String
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Getter function for from-timestamp.
     * @return Long
     */
    public Long getFromTimestamp() {
        return fromTimestamp;
    }

    /**
     * Getter function for to-timestamp.
     * @return Long
     */
    public Long getToTimestamp() {
        return toTimestamp;
    }

    /**
     * Setter function for size.
     * @param size Maximum number of data to query.
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Setter function for cursor.
     * @param cursor Information of the last retrieved cursor.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Setter function for fromDate
     * @param fromDate The starting date of the data to be queried.
     */
    public void setFromTimestamp(String fromDate) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(fromDate));
        setFromTimestamp(date);
    }

    /**
     * Setter function for fromDate
     * @param fromTimestamp The starting timestamp in seconds to be queried.
     */
    public void setFromTimestamp(Long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    /**
     * Setter function for toTImeStamp
     * @param toDate The end date of the data to be queried.
     */
    public void setToTimestamp(String toDate) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(toDate));
        setToTimestamp(date);
    }

    /**
     * Setter function for toTimeStamp
     * @param toDate The end timestamp in seconds to be queried.
     */
    public void setToTimestamp(Long toTimestamp) {
        this.toTimestamp = toTimestamp;
    }
}
