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

package xyz.groundx.caver_ext_kas.kas.kip37;

import xyz.groundx.caver_ext_kas.kas.kip7.KIP7QueryOptions;

import java.security.InvalidParameterException;

public class KIP37QueryOptions {
    /**
     * Enum for "status" query option.
     */
    public enum STATUS_TYPE {
        ALL("all"),
        INIT("init"),
        SUBMITTED("submitted"),
        DEPLOYED("deployed"),
        IMPORTED("imported"),
        FAILED("failed");

        String status;

        STATUS_TYPE(String type) {
            this.status = type;
        }

        /**
         * Check if there is an enum mapped to the given status string.
         * @param kind The kind string to find enum defined in STATUS_TYPE
         * @return boolean
         */
        public static boolean isExist(String kind) {
            for(STATUS_TYPE statusType : STATUS_TYPE.values()) {
                if(statusType.getStatus().equals(kind)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets all type options
         * @return String.
         */
        public static String getAllStatus() {
            String result = "";

            for(int i = 0; i < STATUS_TYPE.values().length; i++) {
                STATUS_TYPE statusType = STATUS_TYPE.values()[i];
                result += "'" + statusType.getStatus() + "'";

                if(i != (STATUS_TYPE.values().length-1)) {
                    result += ", ";
                }
            }

            return result;
        }

        /**
         * Getter function for status
         * @return String
         */
        public String getStatus() {
            return status;
        }
    }

    /**
     * Maximum number of data to query.
     */
    Integer size;

    /**
     * Information of the last retrieved cursor.
     */
    String cursor;

    /**
     * The deploy status ("all", "init", "submitted", "deployed", "imported", "failed") for the contract you wish to query. <br>
     * You can only choose one deploy status.
     */
    String status;

    /**
     * Creates a KIP17QueryOptions instance
     */
    public KIP37QueryOptions() {
    }

    /**
     * Getter function for size.
     * @return Long
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Setter function for size
     * @param size Maximum number of data to query.
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Getter function for cursor.
     * @return String
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Setter function for cursor
     * @param cursor Information of the last retrieved cursor.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Getter function for status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter function for status
     * @param status The deploy status ("all", "init", "submitted", "deployed", "imported", "failed") for the contract you wish to query.
     */
    public void setStatus(String status) {
        if(! KIP7QueryOptions.STATUS_TYPE.isExist(status)) {
            throw new InvalidParameterException("The status parameter have one of the following: [" + KIP7QueryOptions.STATUS_TYPE.getAllStatus() + "]");
        }

        this.status = status;
    }

    /**
     * Setter function for status
     * @param status The STATUS_TYPE enum value.
     */
    public void setStatus(STATUS_TYPE status) {
        this.status = status.getStatus();
    }
}
