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

package xyz.groundx.caver_ext_kas.kas.tokenhistory;

import xyz.groundx.caver_ext_kas.kas.utils.KASUtils;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Representing a query parameters where using Token history REST API.
 */
public class TokenHistoryQueryOptions {

    /**
     * Enum for "type" query option
     */
    public enum CONTRACT_TYPE {
        KIP7("KIP-7"), ERC20("ERC-20"),
        KIP17("KIP-17"), ERC721("ERC-721"),
        KIP37("KIP-37"), ERC1155("ERC-1155");

        String type;

        CONTRACT_TYPE(String type) {
            this.type = type;
        }

        /**
         * Check if there is an enum mapped to the given type string.
         * @param type The type string to find enum defined in CONTRACT_TYPE
         * @return boolean
         */
        public static boolean isExist(String type) {
            for(CONTRACT_TYPE contractType : CONTRACT_TYPE.values()) {
                if(contractType.getType().equals(type)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Gets all type options
         * @return String.
         */
        public static String getAllType() {
            String result = "";
            for(int i= 0; i < CONTRACT_TYPE.values().length; i++) {
                CONTRACT_TYPE type = CONTRACT_TYPE.values()[i];
                result += "'" +  type.getType() + "'";

                if(i != (CONTRACT_TYPE.values().length-1)) {
                    result += ", ";
                }
            }

            return result;
        }

        /**
         * Getter function for type.
         * @return String
         */
        public String getType() {
            return type;
        }
    }

    /**
     * Enum for "kind" query option.
     */
    public enum KIND {
        KLAY("klay"),
        FT("ft"),
        NFT("nft"),
        MT("mt");

        String kind;

        KIND(String kind) {
            this.kind = kind;
        }

        /**
         * Check if there is an enum mapped to the given kind string.
         * @param kind The kind string to find enum defined in CONTRACT_KIND
         * @return boolean
         */
        public static boolean isExist(String kind) {
            for(KIND kindType : KIND.values()) {
                if(kindType.getKind().equals(kind)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets all kind options
         * @return String.
         */
        public static String getAllKind() {
            String result = "";
            for(int i= 0; i < KIND.values().length; i++) {
                KIND kind = KIND.values()[i];
                result += "'" +  kind.getKind() + "'";

                if(i != (KIND.values().length-1)) {
                    result += ", ";
                }
            }

            return result;
        }

        public String getKind() {
            return kind;
        }
    }

    /**
     * Enum for "status" query option
     */
    public enum LABEL_STATUS {
        COMPLETED("completed"),
        PROCESSING("processing"),
        FAILED("failed"),
        CANCELLED("cancelled");

        String status;

        LABEL_STATUS(String status) {
            this.status = status;
        }

        /**
         * Check if there is an enum mapped to the given type string.
         * @param status The type string to find enum defined in LABEL_STATUS
         * @return boolean
         */
        public static boolean isExist(String status) {
            for(LABEL_STATUS labelStatus : LABEL_STATUS.values()) {
                if(labelStatus.getStatus().equals(status)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets all status options
         * @return String.
         */
        public static String getAllStatus() {
            String result = "";
            for(int i= 0; i < LABEL_STATUS.values().length; i++) {
                LABEL_STATUS status = LABEL_STATUS.values()[i];
                result += "'" +  status.getStatus() + "'";

                if(i != (LABEL_STATUS.values().length-1)) {
                    result += ", ";
                }
            }

            return result;
        }

        public String getStatus() {
            return status;
        }
    }


    /**
     * The contract address to query.
     */
    String caFilter;

    /**
     * The kind of token history to query
     */
    String kind;

    /**
     * The date to query.
     */
    String range;

    /**
     * Maximum number of data to query.
     */
    Long size;

    /**
     * Information of the last retrieved cursor.
     */
    String cursor;

    /**
     * The contract labelling status.
     */
    String status;

    /**
     * The type indicated either "erc" or "kip"
     */
    String type;

    /**
     * Creates an TokenHistoryQueryOptions instance.
     */
    public TokenHistoryQueryOptions() {
    }

    /**
     * Creates an TokenHistoryQueryOptions instance.
     * @param caFilter The contract address to query.
     * @param kind The kind of token history to query
     * @param range The date to query.
     * @param size Maximum number of data to query.
     * @param cursor Information of the last retrieved cursor.
     * @param status The contract labelling status.
     * @param type The type indicated either "erc" or "kip"
     */
    public TokenHistoryQueryOptions(String caFilter, String kind, String range, Long size, String cursor, String status, String type) {
        this.caFilter = caFilter;
        this.kind = kind;
        this.range = range;
        this.size = size;
        this.cursor = cursor;
        this.status = status;
        this.type = type;
    }

    /**
     * Getter function for caFilter.
     * @return String
     */
    public String getCaFilter() {
        return caFilter;
    }

    /**
     * Getter function for kind.
     * @return String
     */
    public String getKind() {
        return kind;
    }

    /**
     * Getter function for range.
     * @return String
     */
    public String getRange() {
        return range;
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
     * Getter function for status.
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter function for type
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Setter function for caFilter.
     * @param caFilter The contract address to query.
     */
    public void setCaFilter(String caFilter) {
        this.caFilter = caFilter;
    }

    /**
     * Setter function for kind with KIND enum
     * @param kind A enum defined in KIND.
     */
    public void setKind(KIND kind) {
        setKind(kind.getKind());
    }

    /**
     * Setter function for kind with KIND enum
     * @param kindArr A array of enum defined in KIND.
     */
    public void setKind(KIND[] kindArr) {
        List<String> kinds = Arrays.stream(kindArr).map(KIND::getKind)
                .collect(Collectors.toList());

        setKind(kinds);
    }

    /**
     * Setter function for kind.
     * @param kind The kind of token history to query.
     */
    public void setKind(String kind) {
        if(!KIND.isExist(kind)) {
            throw new InvalidParameterException("The kind option must have one of the following: [" + KIND.getAllKind() +"]");
        }
        this.kind = kind;
    }

    /**
     * Setter function for kind.
     * @param kinds The kind of token history to query.
     */
    public void setKind(List<String> kinds) {
        if(kinds.size() > KIND.values().length) {
            throw new InvalidParameterException("The 'kind' option must have up to " + KIND.values().length + "items. [" + KIND.getAllKind() +"]");
        }

        boolean isMatch = kinds.stream().anyMatch(item -> !KIND.isExist(item));
        if(isMatch) {
            throw new InvalidParameterException("The kind option must have one of the following: [" + KIND.getAllKind() +"]");
        }

        this.kind = KASUtils.parameterToString(kinds);
    }

    /**
     * Setter function for range
     * @param from The from-date to query.
     */
    public void setRange(String from) {
        this.range = convertTime(from);
    }

    /**
     * Setter function for range
     * @param from The from-date to query.
     * @param to The to-date to query.
     */
    public void setRange(String from, String to) {
        String fromData = convertTime(from);
        String toData = convertTime(to);

        if(!checkRangeValid(fromData, toData)) {
            throw new InvalidParameterException("The range parameter('from', 'to') must have same type(block number(hex) / timestamp(decimal))");
        }
        this.range = KASUtils.parameterToString(Arrays.asList(fromData, toData));
    }

    /**
     * Setter function for size
     * @param size Maximum number of data to query.
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Setter function for cursor
     * @param cursor Information of the last retrieved cursor.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Setter function for status.
     * @param status The contract labelling status.
     */
    public void setStatus(String status) {
        if(!LABEL_STATUS.isExist(status)) {
            throw new InvalidParameterException("The status parameter have one of the following: [" + LABEL_STATUS.getAllStatus() + "]");
        }

        this.status = status;
    }

    /**
     * Setter function for status
     * @param status The enum defined LABEL_STATUS.
     */
    public void setStatus(LABEL_STATUS status) {
        this.status = status.getStatus();
    }

    /**
     * Setter function for type.
     * @param type The type indicated either "KIP-7", "KIP-17", "KIP-37", "ERC-20", "ERC-721", "ERC-1155"
     */
    public void setType(String type) {
        if(!CONTRACT_TYPE.isExist(type)) {
            throw new InvalidParameterException("The type parameter have one of the following: [" + CONTRACT_TYPE.getAllType() + "]");
        }
        this.type = type;
    }

    /**
     * Setter function for type
     * @param type The enum defined in CONTRACT_TYPE
     */
    public void setType(CONTRACT_TYPE type) {
        this.type = type.getType();
    }

    boolean checkRangeValid(String from, String to) {
        if(KASUtils.isTimeStamp(from) && KASUtils.isBlockNumber(from)) {
            return false;
        }

        if(KASUtils.isTimeStamp(from)) {
            return KASUtils.isTimeStamp(to);
        } else {
            return KASUtils.isBlockNumber(to);
        }
    }

    String convertTime(String data) {
        if(KASUtils.isBlockNumber(data)) {
            return data;
        } else {
            return KASUtils.convertDateToTimestamp(data);
        }
    }

}
