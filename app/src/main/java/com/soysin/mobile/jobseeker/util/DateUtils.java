package com.soysin.mobile.jobseeker.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private final static SimpleDateFormat jobDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    public static int getDiffDate(String jobDate) {
        try {
            Date date = jobDateFormat.parse(jobDate);
            Date cDate = new Date(System.currentTimeMillis());
            long diff = cDate.getTime() - date.getTime();
            return (int) (diff / (24 * 60 * 60 * 1000));
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
