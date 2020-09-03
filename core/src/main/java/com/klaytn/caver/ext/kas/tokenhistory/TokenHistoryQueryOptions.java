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

    public void setKind(List<String> kind) {
        if(kind.size() > 3) {
            throw new InvalidParameterException("The 'kind' option must have up to 3 items. ['klay', 'ft', 'nft']");
        }
        this.kind = KASUtils.parameterToString(kind);
    }

    public void setRange(String from) {
        String fromData;
        if(KASUtils.isBlockNumber(from)) {
            fromData = from;
        } else {
            fromData = KASUtils.convertDateToTimestamp(from);
        }
        this.range = fromData;
    }

    public void setRange(String from, String to) {
        if(!checkRangeValid(from, to)) {
            throw new InvalidParameterException("The range parameter('from', 'to') must have same type(block number(hex) / timestamp(decimal))");
        }
        this.range = KASUtils.parameterToString(range);
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean checkRangeValid(String from, String to) {
        if(KASUtils.isTimeStamp(from) && KASUtils.isBlockNumber(from)) {
            return false;
        }

        if(KASUtils.isTimeStamp(from)) {
            return KASUtils.isTimeStamp(to);
        } else {
            return KASUtils.isBlockNumber(to);
        }
    }
}
