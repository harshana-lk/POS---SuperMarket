package lk.crewx.pos.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String DECIMAL_PATTERN = "^\\-?[0-9]+(\\.[0-9]+)?$";
    private static final String STRING_PATTERN = "^[a-zA-Z\\s]*$";

    public static boolean isPriceMatch(String text) {
        Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isPhoneNumberMatch(String text) {
        Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isNameMatch(String text) {
        Pattern pattern = Pattern.compile(STRING_PATTERN);

        Matcher nameMatcher = pattern.matcher(text);
        return nameMatcher.matches();
    }

    public static boolean isAddressMatch(String text) {
        Pattern pattern = Pattern.compile(STRING_PATTERN);

        Matcher nameMatcher = pattern.matcher(text);
        return nameMatcher.matches();
    }

    public static boolean isQtyMatch(String text) {
        Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
