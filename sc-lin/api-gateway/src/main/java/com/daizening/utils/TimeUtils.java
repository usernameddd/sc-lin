package com.daizening.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {

    public static String date2Str(Date time, String format) {
        Instant instant = time.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    public static Date str2Date(String str, String format) {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime parse = LocalDateTime.parse(str, dtf);
        ZonedDateTime zdt = parse.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Date str2Date(String str) {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(str, dtf);
        ZonedDateTime zdt = parse.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * date1 是不是在date2 之前或者相等
     * @param date1
     * @param date2
     * @return
     */
    public static boolean beforeEquals(Date date1, Date date2) {
       if (date1.before(date2)) {
           return true;
       } else if (date1.getTime() <= date2.getTime()) {
           return true;
       } else {
           return false;
       }
    }

    public static boolean beforeEquals(String date1, Date date2) {
        Date date0 = str2Date(date1);
        return beforeEquals(date0, date2);
    }

    /**
     * date1 是不是在date2 之前或者相等
     * @param date1
     * @param date2
     * @return
     */
    public static boolean afterEquals(Date date1, Date date2) {
        if (date1.after(date2)) {
            return true;
        } else if (date1.getTime() >= date2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean afterEquals(String date1, Date date2) {
        Date date0 = str2Date(date1);
        return afterEquals(date0, date2);
    }
}
