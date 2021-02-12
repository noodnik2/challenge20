package noodnik.hulu;

import org.junit.Test;


import static noodnik.lib.Common.log;

public class AddBigNumbers0 {

    @Test
    public void testOne() {
        String s1 = "3.14";
        String s2 = "0.9";
        log("addStrings(%s, %s) => '%s'", s1, s2, addStrings(s1, s2));
    }

    /**
     * Take two positive numbers as strings, and return the sum of them.
     * E.g., "3.14" + "0.9" => "4.04".
     *
     * Note: Simply converting the strings to numbers and adding them
     * together or utilizing Big Decimal is not acceptable and will not get
     * full credit for the assessment.  The solution must work for numbers
     * that are very large as well.
     */
    public static String addStrings(String string1, String string2) {

        // s => (long number, int decimal place)

        int decimalPos1 = decimalPlace(string1);
        int decimalPos2 = decimalPlace(string2);
        int maxDecimalPos = Math.max(decimalPos1, decimalPos2);
        long numberOf1 = numberOf(string1 + zeroes(maxDecimalPos - decimalPos1));
        long numberOf2 = numberOf(string2 + zeroes(maxDecimalPos - decimalPos2));

        long sumNumber = numberOf1 + numberOf2;

        return decimalizeToString(sumNumber, Math.max(decimalPos1, decimalPos2));
    }

    private static String zeroes(int l) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < l; i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.toString();
    }

    private static String decimalizeToString(long sumNumber, int max) {
        String s = String.valueOf(sumNumber);
        int decimalPos = s.length() - max;
        return s.substring(0, decimalPos) + "." + s.substring(decimalPos);
    }

    private static long numberOf(String string1) {
        return Long.parseLong(string1.replaceAll("\\.", ""));
    }

    private static int decimalPlace(String string1) {
        return string1.length() - string1.indexOf('.') - 1;
    }


    /**
     * Failures:
     *
     * Test case 6:
     *
     * Exception in thread "main" java.lang.NumberFormatException: For input string: "100000000000000000000000000000001"
     * 	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
     * 	at java.lang.Long.parseLong(Long.java:592)
     * 	at java.lang.Long.parseLong(Long.java:631)
     * 	at Result.numberOf(Solution.java:56)
     * 	at Result.addStrings(Solution.java:33)
     * 	at Solution.main(Solution.java:75)
     *
     * Test case 5:
     *  Your Output: 1.2
     *
     * Test case 4:
     *  Your Output: 4.04
     *
     */

}
