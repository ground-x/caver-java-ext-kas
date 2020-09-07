package com.klaytn.caver.ext.kas.tokenhistory;

import com.klaytn.caver.ext.kas.utils.KASUtils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class TokenHistoryQueryOptions {
    String caFilter;
    String kind;
    String range;
    Long size;
    String cursor;
    String status;
    String type;

    public TokenHistoryQueryOptions() {
    }

    public String getCaFilter() {
        return caFilter;
    }

    public String getKind() {
        return kind;
    }

    public String getRange() {
        return range;
    }

    public Long getSize() {
        return size;
    }

    public String getCursor() {
        return cursor;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public void setCaFilter(String caFilter) {
        this.caFilter = caFilter;
    }

    public void setKind(String kind) {
        setKind(Arrays.asList(kind));
    }

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

    public void setRange(String from) {
        this.range = convertTime(from);
    }

    public void setRange(String from, String to) {
        String fromData = convertTime(from);
        String toData = convertTime(to);

        if(!checkRangeValid(fromData, toData)) {
            throw new InvalidParameterException("The range parameter('from', 'to') must have same type(block number(hex) / timestamp(decimal))");
        }
        this.range = KASUtils.parameterToString(Arrays.asList(fromData, toData));
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public void setStatus(String status) {
        if(!status.equals("completed") && !status.equals("processing") && !status.equals("failed") && !status.equals("cancelled")) {
            throw new InvalidParameterException("The status parameter have one of the following: [completed, processing, failed, cancelled");
        }
        this.status = status;
    }

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
