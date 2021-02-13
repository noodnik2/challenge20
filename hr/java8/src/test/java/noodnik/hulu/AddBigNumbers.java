package noodnik.hulu;

import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static noodnik.lib.Common.log;

@RunWith(Theories.class)
public class AddBigNumbers {

    @Theory
    public void foundExample(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "198123",
            "12",
            "198111"
        );
    }

    @Theory
    public void exampleOne(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "4.04",
            "3.14",
            "0.9"
        );
    }

    @Theory
    public void disparateDecimals(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "100000000000000000.00000000000000001",
            ".00000000000000001",
            "100000000000000000"
        );
    }

    @Theory
    public void longerThanLong(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "1000000000000000000000000000000000001",
            "1000000000000000000000000000000000000",
            "1"
        );
    }

    @Theory
    public void lotsOfZeroes(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "4.04",
            "4.04",
            "00000000000000000.00000000000000"
        );
    }

    @Theory
    public void simpleDecimalZeroes(BigStringAdder adder) {
        assertExpectedResult(
            adder,
            "0",
            "0.",
            ".0"
        );
    }

    @DataPoints
    public static BigStringAdder[] adders() {
        return new BigStringAdder[] {
            new SolutionOne(),
            new FoundSolution()
        };
    }

    private static void assertExpectedResult(
        BigStringAdder adder,
        String expectedResult,
        String string1,
        String string2
    ) {
        String actualResult = adder.addStrings(string1, string2);
        log("addStrings(%s, %s) => '%s'", string1, string2, actualResult);
        Assert.assertEquals(expectedResult, actualResult);
    }

    static abstract class BigStringAdder {
        /**
         * Take two positive numbers as strings, and return the sum of them.
         * E.g., "3.14" + "0.9" => "4.04".
         *
         * Note: Simply converting the strings to numbers and adding them
         * together or utilizing Big Decimal is not acceptable and will not get
         * full credit for the assessment.  The solution must work for numbers
         * that are very large as well.
         */
        public abstract String addStrings(String string1, String string2);

        public String toString() {
            return getClass().getSimpleName();
        }
    }

    static class SolutionOne extends BigStringAdder {

        public String addStrings(String string1, String string2) {

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

        private String zeroes(int l) {
            return "0".repeat(Math.max(0, l));
        }

        private int decimalPlace(String string1) {
            int decimalPosition = string1.indexOf('.');
            if (decimalPosition < 0) {
                return 0;
            }
            return string1.length() - decimalPosition - 1;
        }

        private int valueAt(String rs1, int i) {
            if (i >= rs1.length()) {
                return 0;
            }
            return Integer.parseInt(String.valueOf(rs1.charAt(i)));
        }

        private String reverse(String string1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = string1.length() - 1; i>= 0; i--) {
                stringBuilder.append(string1.charAt(i));
            }
            return stringBuilder.toString();
        }

        private String decimalizeToString(String sumString, int max) {
            int decimalPos = sumString.length() - max;
            return sumString.substring(0, decimalPos) + "." + sumString.substring(decimalPos);
        }

        private String trimRight(String string1, String chars) {
            int rightPos = string1.length() - 1;
            while(rightPos >= 0 && chars.contains(string1.substring(rightPos, rightPos + 1))) {
                rightPos--;
            }
            String candidate = string1.substring(0, rightPos + 1);
            return candidate.isEmpty() ? "0" : candidate;
        }

        private String noDecimalOf(String string1) {
            return string1.replaceAll("\\.", "");
        }

    }

    // found this at: https://www.geeksforgeeks.org/sum-two-large-numbers/
    static class FoundSolution extends BigStringAdder {

        public String addStrings(String string1, String string2) {
            // Before proceeding further, make sure length
            // of str2 is larger.
            if (string1.length() > string2.length()){
                String t = string1;
                string1 = string2;
                string2 = t;
            }

            // Take an empty String for storing result
            String str = "";

            // Calculate length of both String
            int n1 = string1.length(), n2 = string2.length();

            // Reverse both of Strings
            string1=new StringBuilder(string1).reverse().toString();
            string2=new StringBuilder(string2).reverse().toString();

            int carry = 0;
            for (int i = 0; i < n1; i++)
            {
                // Do school mathematics, compute sum of
                // current digits and carry
                int sum = (string1.charAt(i) - '0') +
                          (string2.charAt(i) - '0') + carry;
                str += (char)(sum % 10 + '0');

                // Calculate carry for next step
                carry = sum / 10;
            }

            // Add remaining digits of larger number
            for (int i = n1; i < n2; i++)
            {
                int sum = (string2.charAt(i) - '0') + carry;
                str += (char)(sum % 10 + '0');
                carry = sum / 10;
            }

            // Add remaining carry
            if (carry > 0)
                str += (char)(carry + '0');

            // reverse resultant String
            str = new StringBuilder(str).reverse().toString();

            return str;
        }

    }

}
