package com.federicoboni.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]+$";
    // regex from https://stackoverflow.com/questions/8204680/java-regex-email
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static void isCorrectUsername(String username) throws ParseException {

        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new ParseException("", 0);
        }

    }

    public static void isCorrectEmail(String email) throws ParseException {

        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ParseException("", 0);
        }

    }
}
