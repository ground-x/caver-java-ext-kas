package com.klaytn.caver.ext.kas.wallet;

import com.klaytn.caver.ext.kas.utils.KASUtils;

public class WalletQueryOptions {
    Long size;
    String cursor;
    Long fromTimestamp;
    Long toTimestamp;

    public WalletQueryOptions() {
    }

    public WalletQueryOptions(Long size, String cursor, Long fromTimestamp, Long toDate) {
        this.size = size;
        this.cursor = cursor;
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toDate;
    }

    public Long getSize() {
        return size;
    }

    public String getCursor() {
        return cursor;
    }

    public Long getFromTimestamp() {
        return fromTimestamp;
    }

    public Long getToTimestamp() {
        return toTimestamp;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Setter function for fromDate
     * @param fromTimestamp The starting date of the data to be queried.
     */
    public void setFromTimestamp(String fromTimestamp) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(fromTimestamp));
        setFromDate(date);
    }

    /**
     * Setter function for fromDate
     * @param fromDate The starting date of the data to be queried.
     */
    public void setFromDate(Long fromDate) {
        this.fromTimestamp = fromDate;
    }

    /**
     * Setter function for toDate
     * @param toTimestamp
     */
    public void setToTimestamp(String toTimestamp) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(toTimestamp));
        setToDate(date);
    }

    /**
     * Setter function for toDate
     * @param toDate
     */
    public void setToDate(Long toDate) {
        this.toTimestamp = toDate;
    }
}
