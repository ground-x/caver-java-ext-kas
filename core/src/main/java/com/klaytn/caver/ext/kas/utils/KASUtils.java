package com.klaytn.caver.ext.kas.utils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class KASUtils {
    private static DateTimeFormatter pattern_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter pattern_dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter pattern_dateMilliTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * convert date string to timestamp in seconds
     * @param date The date to convert timestamp.
     * @return String
     */
    public static String convertDateToTimestamp(String date) {
        LocalDateTime localDateTime;
        if(checkDateFormat(date)) {
            localDateTime = LocalDate.parse(date).atStartOfDay();
        } else if(checkDateTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, pattern_dateTime);
        } else if(checkDateMilliTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, pattern_dateMilliTime);
        } else {
            throw new InvalidParameterException("Unsupported parameters");
        }

        return Long.toString(Timestamp.valueOf(localDateTime).getTime() / 1000);
    }

    private static boolean checkDateFormat(String date) {
        try {
            pattern_date.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean checkDateTimeFormat(String date) {
        try {
            pattern_dateTime.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean checkDateMilliTimeFormat(String date) {
        try {
            pattern_dateMilliTime.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
