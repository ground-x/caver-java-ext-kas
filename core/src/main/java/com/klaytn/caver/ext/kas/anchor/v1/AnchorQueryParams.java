package com.klaytn.caver.ext.kas.anchor.v1;

public class AnchorQueryParams {
    int size;
    int fromDate;
    int toDate;
    String cursor;

    public AnchorQueryParams() {
    }

    public AnchorQueryParams(int size, int formDate, int toDate, String cursor) {
        this.size = size;
        this.fromDate = formDate;
        this.toDate = toDate;
        this.cursor = cursor;
    }

    public int getSize() {
        return size;
    }

    public int getFromDate() {
        return fromDate;
    }

    public int getToDate() {
        return toDate;
    }

    public String getCursor() {
        return cursor;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFromDate(int fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(int toDate) {
        this.toDate = toDate;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
