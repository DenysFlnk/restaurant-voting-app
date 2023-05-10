package com.restaurantvoting.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final LocalDate currentDay = LocalDate.now();

    public static final LocalDateTime startOfCurrentDay = currentDay.atStartOfDay();

    public static final LocalDateTime endOfVoting = currentDay.atTime(10, 59, 59);
}
