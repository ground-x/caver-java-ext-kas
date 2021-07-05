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

package xyz.groundx.caver_ext_kas.kas.utils;

import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.Utils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

public class KASUtils {
    private static DateTimeFormatter pattern_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter pattern_dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter pattern_dateMilliTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * convert date string to timestamp in seconds
     *
     * <pre>Example
     * {@code
     * String timeFormat1 = "2020-08-01 00:00:00";
     * String timeFormat2 = "2020-08-01";
     * String timeFormat3 = "2020-08-01 00:00:00:111";
     *
     * String timestamp;
     * timestamp = KASUtils.convertDateToTimestamp(timeFormat1); // timestamp: "1596207600"
     * timestamp = KASUtils.convertDateToTimestamp(timeFormat2); // timestamp: "1596207600"
     * timestamp = KASUtils.convertDateToTimestamp(timeFormat3); // timestamp: "1596207600"
     * }
     * </pre>
     *
     * @param date The date to convert timestamp.
     * @return String
     */
    public static String convertDateToTimestamp(String date) {
        LocalDateTime localDateTime;
        if(KASUtils.isTimeStamp(date)) {
            return date;
        }

        if(checkDateFormat(date)) {
            localDateTime = LocalDate.parse(date).atStartOfDay();
        } else if(checkDateTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, pattern_dateTime);
        } else if(checkDateMilliTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, pattern_dateMilliTime);
        } else {
            throw new InvalidParameterException("Unsupported parameters");
        }

        return Long.toString(Timestamp.valueOf(localDateTime).getTime() / 1000);
    }

    /**
     * make query string using passed as a param.
     *
     * <pre>Example
     * {@code
     * String result = KASUtils.parameterToString(Arrays.asList("param1", "param2", "param3");
     * // result: "param1,param2,param3"
     * }
     * </pre>
     *
     * @param param a query parameter data.
     * @return String
     */
    public static String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection)param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * Check whether passed string data is unix time stamp format.
     *
     * <pre>Example
     * {@code
     * boolean isTimestamp
     * isTimestamp = KASUtils.isTimeStamp("1596207600"); // isTimestamp: true
     * isTimestamp = KASUtils.isTimeStamp("0x3401"); // isTimestamp: false
     * }
     * </pre>
     *
     * @param data The string data to check.
     * @return boolean
     */
    public static boolean isTimeStamp(String data) {
        try {
            Long.parseLong(data, 10);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Check whether passed string data is block number format.
     *
     * <pre>Example
     * {@code
     * boolean isBlockNumber;
     * isBlockNumber = KASUtils.isBlockNumber("0x5"); // isBlockNumber: true
     * isBlockNumber = KASUtils.isBlockNumber("125"); // isBlockNumber: false
     * }
     * </pre>
     *
     * @param data The string data to check
     * @return boolean
     */
    public static boolean isBlockNumber(String data) {
        return Utils.isHexStrict(data);
    }

    /**
     * Converts uncompressed public key string to compressed form public key string by adding prefix.
     *
     * <pre>Example
     * {@code
     * String x = "ee6de5c9fc0fc05ad43a55b80a3bdb4d6f40aecef7c288d640125feab31f8fcf";
     * String y = "45d6849dbce543168c93dbac3f11d3aac126cf7349d92ed4a7c4ea57e0993bd0";
     *
     * String unCompressedPublicKey = String.format("%s%s", x, y);
     * String unCompressedPublicKeyWithHexPrefix = String.format("0x%s%s", x, y);
     *
     * String compressedPublicKey;
     *
     * // compressedPublicKey: "0x04ee6de5c9fc0fc05ad43a55b80a3bdb4d6f40aecef7c288d640125feab31f8fcf45d6849dbce543168c93dbac3f11d3aac126cf7349d92ed4a7c4ea57e0993bd0"
     * compressedPublicKey = KASUtils.addUncompressedKeyPrefix(publicKeyWithHexPrefix);
     * compressedPublicKey = KASUtils.addUncompressedKeyPrefix(publicKey);
     * }
     * </pre>
     *
     * @param publicKey The public key
     * @return
     */
    public static String addUncompressedKeyPrefix(String publicKey) {
        if(AccountKeyPublicUtils.isCompressedPublicKey(publicKey)){
            return publicKey;
        }

        if(!AccountKeyPublicUtils.isUncompressedPublicKey(publicKey)) {
            throw new IllegalArgumentException("publicKey must have uncompressed format.");
        }

        String noPrefixStr = Utils.stripHexPrefix(publicKey);
        if(noPrefixStr.length() == 128) {
            noPrefixStr = "04" + noPrefixStr;
        }

        return Utils.addHexPrefix(noPrefixStr);
    }

    static boolean checkDateFormat(String date) {
        try {
            pattern_date.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static boolean checkDateTimeFormat(String date) {
        try {
            pattern_dateTime.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static boolean checkDateMilliTimeFormat(String date) {
        try {
            pattern_dateMilliTime.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
