package com.klaytn.caver.ext.kas.utils;

import com.klaytn.caver.utils.Utils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class KASUtils {

    public static String convertDate(String date) {
        LocalDateTime localDateTime;
        if(checkDateFormat(date)) {
            localDateTime = LocalDate.parse(date).atStartOfDay();
        } else if(checkDateTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if(checkDateMilliTimeFormat(date)) {
            localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        } else if(Utils.isHexStrict(date)) {
            return date;
        } else {
            throw new InvalidParameterException("Unsupported parameters");
        }

        return Long.toString(Timestamp.valueOf(localDateTime).getTime() / 1000);
    }

    private static boolean checkDateFormat(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            pattern.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean checkDateTimeFormat(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            pattern.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean checkDateMilliTimeFormat(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        try {
            pattern.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
