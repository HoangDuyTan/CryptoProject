package com.crypto.utils;

public class AlphabetUtils {
    public static String EN_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static String EN_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String VI_LOWER = "aàáảãạăằắẳẵặâầấẩẫậbcdđeèéẻẽẹêềếểễệghiìíỉĩịklmnoòóỏõọôồốổỗộơờớởỡợpqrstuùúủũụưừứửữựvxyỳýỷỹỵ";
    public static String VI_UPPER = "AÀÁẢÃẠĂẰẮẲẴẶÂẦẤẨẪẬBCDĐEÈÉẺẼẸÊỀẾỂẼỆGHIÌÍỈĨỊKLMNOÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢPQRSTUÙÚỦŨỤƯỪỨỬỮỰVXYỲÝỶỸỴ";

    public static String getAlphabet(boolean isUpper, boolean isVI) {
        if (isVI) {
            return isUpper ? VI_UPPER : VI_LOWER;
        } else {
            return isUpper ? EN_UPPER : EN_LOWER;
        }
    }

    public static int getCharValue(char c, boolean isVI) {
        String lowerDict = getAlphabet(false, isVI);
        return lowerDict.indexOf(Character.toLowerCase(c));
    }

    public static boolean isUpperCase(char c, boolean isVI) {
        String upperDict = getAlphabet(true, isVI);
        return upperDict.indexOf(c) != -1;
    }

    public static char getCharByIndex(int index, boolean isUpper, boolean isVI) {
        String dict = getAlphabet(isUpper, isVI);
        int len = dict.length();

        int normalizedIndex = (index % len + len) % len;
        return dict.charAt(normalizedIndex);
    }

    // Caesar
    public static char shiftCharacter(char c, int shift, boolean isVI) {
        int index = getCharValue(c, isVI);

        if (index == -1) {
            return c;
        }

        boolean isUpper = isUpperCase(c, isVI);
        return getCharByIndex(index + shift, isUpper, isVI);
    }
}
