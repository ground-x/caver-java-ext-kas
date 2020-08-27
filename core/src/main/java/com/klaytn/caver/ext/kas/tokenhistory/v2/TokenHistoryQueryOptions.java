package com.klaytn.caver.ext.kas.tokenhistory.v2;

public class TokenHistoryQueryOptions {
    String caFilter;
    String kind;
    String range;
    Integer size;
    String cursor;
    Integer preset;
    String status;
    String type;

    public TokenHistoryQueryOptions() {
    }

    public String getCaFilter() {
        return caFilter;
    }

    public void setCaFilter(String caFilter) {
        this.caFilter = caFilter;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Integer getPreset() {
        return preset;
    }

    public void setPreset(Integer preset) {
        this.preset = preset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
