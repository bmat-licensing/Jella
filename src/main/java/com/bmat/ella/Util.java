
package com.bmat.ella;

/**
 * Java Class Util.
 * Utility class.
 * @author Harrington Joseph (Harph)
 * */
class Util {
    /**
     * Create an String with the array elements the separator in the middle.
     * @param array A String array with words to join.
     * @param separator A String used to separate each element.
     * @return A string with the array joined.
     * */
    static String joinArray(final String[] array,
            final String separator) {
        String result = "";
        String sep = "";
        for (String value : array) {
            result += sep + value;
            sep = separator;
        }
        return result;
    }
}
