package in.ineuron.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    // This regex pattern matches a basic email address format
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isEmail(String text) {
        // Check if the input matches the email address pattern
        return Pattern.matches(EMAIL_REGEX, text);
    }
}

