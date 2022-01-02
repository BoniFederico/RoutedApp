package com.federicoboni.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String DATE_PATTERN1 = "dd/MM/yyyy";
    private static final String DATE_REGEX = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$";

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(c1.getTime()).equals(sdf.format(c2.getTime()));
    }

    public static void isValidDate(String date, String datePattern) throws ParseException {
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) {
            throw new ParseException("", 0);
        }

    }

    public static Calendar fromStringToDate(String date) throws ParseException {

        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) {
            throw new ParseException("", 0);
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ITALIAN);
        sdf.setLenient(false);
        cal.setTime(sdf.parse(date));

        return cal;
    }
}
