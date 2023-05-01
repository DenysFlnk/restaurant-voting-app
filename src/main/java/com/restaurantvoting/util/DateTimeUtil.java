package com.restaurantvoting.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtil {

    public static final LocalDate currentDay = LocalDate.now();

    public static final LocalDateTime startOfCurrentDay = currentDay.atStartOfDay();

    public static final LocalDateTime endOfVoting = currentDay.atTime(11, 0, 0);
}
