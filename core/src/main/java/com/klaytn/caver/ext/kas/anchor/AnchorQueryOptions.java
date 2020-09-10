package com.klaytn.caver.ext.kas.anchor;

import com.klaytn.caver.ext.kas.utils.KASUtils;

/**
 * Representing a query parameters where using Anchor REST API.
 */
public class AnchorQueryOptions {

    /**
     * Maximum number of data to query.
     */
    Long size;

    /**
     * The starting date of the data to be queried.
     */
    Long fromTimestamp;

    /**
     * The ending date of the data to be queried.
     */
    Long toTimestamp;

    /**
     * Information of the last retrieved cursor.
     */
    String cursor;

    /**
     * Creates an AnchorQueryOptions instance.
     */
    public AnchorQueryOptions() {
    }

    /**
     * Creates an AnchorQueryOptions instance.
     * @param size Maximum number of data to query.
     * @param fromTimestamp The starting date of the data to be queried.
     * @param toTimestamp The ending date of the data to be queried.
     * @param cursor Information of the last retrieved cursor.
     */
    public AnchorQueryOptions(long size, String fromTimestamp, String toTimestamp, String cursor) {
        this.size = size;
        setFromTimestamp(fromTimestamp);
        setToTimestamp(toTimestamp);
        this.cursor = cursor;
    }

    /**
     * Getter function for size.
     * @return long
     */
    public Long getSize() {
        return size;
    }

    /**
     * Setter function for size.
     * @param size Maximum number of data to query.
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Getter function for fromDate.
     * @return String
     */
    public Long getFromTimestamp() {
        return fromTimestamp;
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
     * Getter function for toDate
     * @return String
     */
    public Long getToTimestamp() {
        return toTimestamp;
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

    /**
     * Getter function for cursor
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
}
