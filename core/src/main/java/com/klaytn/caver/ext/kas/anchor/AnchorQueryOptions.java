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
    Long fromDate;

    /**
     * The ending date of the data to be queried.
     */
    Long toDate;

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
     * @param fromDate The starting date of the data to be queried.
     * @param toDate The ending date of the data to be queried.
     * @param cursor Information of the last retrieved cursor.
     */
    public AnchorQueryOptions(long size, String fromDate, String toDate, String cursor) {
        this.size = size;
        setFromDate(fromDate);
        setToDate(toDate);
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
    public Long getFromDate() {
        return fromDate;
    }

    /**
     * Setter function for fromDate
     * @param fromDate The starting date of the data to be queried.
     */
    public void setFromDate(String fromDate) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(fromDate));
        setFromDate(date);
    }

    /**
     * Setter function for fromDate
     * @param fromDate The starting date of the data to be queried.
     */
    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter function for toDate
     * @return String
     */
    public Long getToDate() {
        return toDate;
    }

    /**
     * Setter function for toDate
     * @param toDate
     */
    public void setToDate(String toDate) {
        long date = Long.parseLong(KASUtils.convertDateToTimestamp(toDate));
        setToDate(date);
    }

    /**
     * Setter function for toDate
     * @param toDate
     */
    public void setToDate(Long toDate) {
        this.toDate = toDate;
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
