package org.hqu.vibsignal_analysis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public Date parseDate(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        Date parsedDate = formatter.parse(dateString);
        return parsedDate;
    }

    public static String parseDateString(Date date,String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return dateString;
    }
}
