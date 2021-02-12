package noodnik.hulu;

import org.junit.Assert;
import org.junit.Test;


import static noodnik.lib.Common.log;

public class AddBigNumbers {

    @Test
    public void exampleOne() {
        assertExpectedResult("4.04", "3.14", "0.9");
    }

    @Test
    public void disparateDecimals() {
        assertExpectedResult(
            "100000000000000000.00000000000000001",
            ".00000000000000001",
            "100000000000000000"
        );
    }

    @Test
    public void longerThanLong() {
        assertExpectedResult(
            "1000000000000000000000000000000000001",
            "1000000000000000000000000000000000000",
            "1"
        );
    }

    @Test
    public void lotsOfZeroes() {
        assertExpectedResult(
            "4.04",
            "4.04",
            "00000000000000000.00000000000000"
        );
    }

    @Test
    public void simpleDecimalZeroes() {
        assertExpectedResult(
            "0",
            "0.",
            ".0"
        );
    }

    private static void assertExpectedResult(
        String expectedResult,
        String string1,
        String string2
    ) {
        String actualResult = addStrings(string1, string2);
        log("addStrings(%s, %s) => '%s'", string1, string2, actualResult);
        Assert.assertEquals(expectedResult, actualResult);
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

        int decimalPos1 = decimalPlace(string1);
        int decimalPos2 = decimalPlace(string2);
        int maxDecimalPos = Math.max(decimalPos1, decimalPos2);
        String bigString1 = string1 + zeroes(maxDecimalPos - decimalPos1);
        String bigString2 = string2 + zeroes(maxDecimalPos - decimalPos2);

        String rs1 = reverse(noDecimalOf(bigString1));
        String rs2 = reverse(noDecimalOf(bigString2));

        StringBuilder rsb = new StringBuilder();
        int r = 0;
        for (int i = 0; i < Math.max(rs1.length(), rs2.length()); i++) {
            int d1 = valueAt(rs1, i);
            int d2 = valueAt(rs2, i);
            int sd1d2 = d1 + d2 + r;
            rsb.append(sd1d2 % 10);
            r = sd1d2 / 10;
        }
        return trimRight(
            decimalizeToString(
                reverse(
                    trimRight(
                        rsb.toString(),
                        "0"
                    )
                ),
                Math.max(decimalPos1, decimalPos2)
            ),
            "\\.0"
        );
    }

    private static String zeroes(int l) {
        return "0".repeat(Math.max(0, l));
    }

    private static int decimalPlace(String string1) {
        int decimalPosition = string1.indexOf('.');
        if (decimalPosition < 0) {
            return 0;
        }
        return string1.length() - decimalPosition - 1;
    }

    private static int valueAt(String rs1, int i) {
        if (i >= rs1.length()) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(rs1.charAt(i)));
    }

    private static String reverse(String string1) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = string1.length() - 1; i>= 0; i--) {
            stringBuilder.append(string1.charAt(i));
        }
        return stringBuilder.toString();
    }

    private static String decimalizeToString(String sumString, int max) {
        int decimalPos = sumString.length() - max;
        return sumString.substring(0, decimalPos) + "." + sumString.substring(decimalPos);
    }

    private static String trimRight(String string1, String chars) {
        int rightPos = string1.length() - 1;
        while(rightPos >= 0 && chars.contains(string1.substring(rightPos, rightPos + 1))) {
            rightPos--;
        }
        String candidate = string1.substring(0, rightPos + 1);
        return candidate.isEmpty() ? "0" : candidate;
    }

    private static String noDecimalOf(String string1) {
        return string1.replaceAll("\\.", "");
    }

}
