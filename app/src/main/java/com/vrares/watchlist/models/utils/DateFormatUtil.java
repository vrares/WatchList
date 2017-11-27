package com.vrares.watchlist.models.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatUtil {

    private String pattern;

    public DateFormatUtil(String pattern) {
        this.pattern = pattern;
    }

    public String getFormatDate(String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        return dateFormat.format(calendar.getTime());
    }
}
