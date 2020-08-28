package com.klaytn.caver.ext.kas.anchor;

public class AnchorQueryParams {
    long size;
    String fromDate;
    String toDate;
    String cursor;

    public AnchorQueryParams() {
    }

    public AnchorQueryParams(long size, String formDate, String toDate, String cursor) {
        this.size = size;
        this.fromDate = formDate;
        this.toDate = toDate;
        this.cursor = cursor;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
