package com.terrideboer.bookbase.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormat= DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");

        return dateTime.format(dateTimeFormat);
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(dateFormat);
    }
}
