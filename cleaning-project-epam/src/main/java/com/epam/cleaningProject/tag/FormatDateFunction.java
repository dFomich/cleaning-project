package com.epam.cleaningProject.tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDateFunction {
    private FormatDateFunction() {
    }
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
