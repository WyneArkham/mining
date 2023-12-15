package io.github.ardryck.mining.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {

    private static final TimeZone zone = TimeZone.getTimeZone("GMT-3");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    static {
        simpleDateFormat.setTimeZone(zone);
    }

    public static boolean isNight() {
        int hour = getHour();

        return hour > 18 || hour < 6;
    }

    public static int getHour() {
        return simpleDateFormat.getCalendar().getTime().getHours();
    }
}
