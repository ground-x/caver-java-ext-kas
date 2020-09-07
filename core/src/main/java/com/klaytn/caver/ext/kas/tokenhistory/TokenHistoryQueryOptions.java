package com.klaytn.caver.ext.kas.tokenhistory;

import com.klaytn.caver.ext.kas.utils.KASUtils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

/**
 * Representing a query parameters where using Token history REST API.
 */
public class TokenHistoryQueryOptions {
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
     * Setter function for kind.
     * @param kind The kind of token history to query.
     */
    public void setKind(String kind) {
        setKind(Arrays.asList(kind));
    }

    /**
     * Setter function for kind.
     * @param kind The kind of token history to query.
     */
    public void setKind(List<String> kinds) {
        if(kinds.size() > 3) {
            throw new InvalidParameterException("The 'kind' option must have up to 3 items. ['klay', 'ft', 'nft']");
        }

        boolean isMatch = kinds.stream().anyMatch(item -> (!item.equals("klay") && !item.equals("ft") && !item.equals("nft")));
        if(isMatch) {
            throw new InvalidParameterException("The kind option must have one of the following: ['klay', 'ft', 'nft']");
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
        if(!status.equals("completed") && !status.equals("processing") && !status.equals("failed") && !status.equals("cancelled")) {
            throw new InvalidParameterException("The status parameter have one of the following: [completed, processing, failed, cancelled");
        }
        this.status = status;
    }

    /**
     * Setter function for type.
     * @param type The type indicated either "erc" or "kip"
     */
    public void setType(String type) {
        if(type == null || type.isEmpty()) {
            type = null;
        } else if(!type.equals("kip") && !type.equals("erc")) {
            throw new InvalidParameterException("The type parameter have one of the following: ['kip', 'erc', empty string(or null)]");
        }
        this.type = type;
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
