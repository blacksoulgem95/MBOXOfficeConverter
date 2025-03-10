package me.sofiavicedomini.mboxconverter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static boolean isValidString(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public static boolean areValidString(String... strings) {
        boolean isValid = true;
        for (String string : strings) {
            isValid = isValid && isValidString(string);
        }
        return isValid;
    }
}
