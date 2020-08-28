package com.klaytn.caver.ext.kas.anchor;

import com.klaytn.caver.ext.kas.utils.KASUtils;

public class AnchorQueryParams {
    long size;
    String fromDate;
    String toDate;
    String cursor;

    public AnchorQueryParams() {
    }

    public AnchorQueryParams(long size, String fromDate, String toDate, String cursor) {
        this.size = size;
        setFromDate(fromDate);
        setToDate(toDate);
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
        this.fromDate = KASUtils.convertDateToTimestamp(fromDate);
    }

    public void setFromDate(long fromDate) {
        this.fromDate = Long.toString(fromDate);
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = KASUtils.convertDateToTimestamp(toDate);
    }

    public void setToDate(long toDate) {
        this.toDate = Long.toString(toDate);
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
