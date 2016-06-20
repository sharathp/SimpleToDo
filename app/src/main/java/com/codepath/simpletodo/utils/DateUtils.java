package com.codepath.simpletodo.utils;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

    // note, we are deliberately using java.sql.Date to ignore hour, min and sec
    public static Date getToday() {
        final Calendar calendar = Calendar.getInstance();
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return new Date(gregorianCalendar.getTimeInMillis());
    }
}
