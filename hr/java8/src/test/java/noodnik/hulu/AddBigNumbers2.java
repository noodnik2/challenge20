package noodnik.hulu;

import org.junit.Test;


import static noodnik.lib.Common.log;

public class AddBigNumbers2 {

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

        // The idea is based on school mathematics. We traverse both strings from end,
        // one by one add digits and keep track of carry. To simplify the process, we do following:
        //1) Reverse both strings.
        //2) Keep adding digits one by one from 0’th index (in reversed strings) to end of smaller
        //string, append the sum % 10 to end of result and keep track of carry as sum/10.
        //3) Finally reverse the result.

        String rs1 = reverse(string1);
        String rs2 = reverse(string2);

        StringBuilder rsb = new StringBuilder();
        int r = 0;
        for (int i = 0; i < Math.max(rs1.length(), rs2.length()); i++) {
            int d1 = valueAt(rs1, i);
            int d2 = valueAt(rs2, i);
            int sd1d2 = d1 + d2 + r;
            rsb.append(sd1d2 % 10);
            r = sd1d2 / 10;
        }
        return rsb.toString();
    }

    private static int valueAt(String rs1, int i) {
        if (i >= rs1.length()) {
            return 0;
        }
        return rs1.charAt(i);
    }

    private static String reverse(String string1) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = string1.length() - 1; i>= 0; i--) {
            stringBuilder.append(string1.charAt(i));
        }
        return stringBuilder.toString();
    }


}
