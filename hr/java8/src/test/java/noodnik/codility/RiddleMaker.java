package noodnik.codility;

import org.junit.Test;

import static java.lang.String.format;
import static org.junit.Assert.fail;

public class RiddleMaker {


    /**
     *  Given string {@code riddle},
     *  return string with question marks replaced by lowercase letters ('a'-'z')
     *  so that same letters do not occur next to each-other.  So long as answer
     *  satifies requirements, it's correct.
     *  
     *  Focus on correctness & efficiency.
     *
     *  Assumptions:
     *  - length of {@code riddle} is [1..100,000]
     *  - {@code riddle} contains only lowercase letters ('a'-'z') and/or question marks '?'
     *  - it's always possible to turn {@code riddle} into a string without two identical consecutive letters
     */
    
    static
    class Solution {

        public String solution(final String riddle) {
            final char[] result = riddle.toCharArray();
            for (int i = 0; i < result.length; i++) {
                if (result[i] == '?') {
                    result[i] = newChar(
                        (i > 0) ? result[i - 1] : '?',
                        ((i + 1) < result.length) ? result[i + 1] : '?'
                    );
                }
            }
            return new String(result);
        }

        private static char newChar(final char charBefore, final char charAfter) {
            final char[] subChars = { 'a', 'b', 'c', 'd' };
            for (final char resultChar : subChars) {
                if (
                    (charBefore == '?' || resultChar != charBefore)
                 && (charAfter == '?' || resultChar != charAfter)
                ) {
                    return resultChar;
                }
            }
            throw new RuntimeException();
        }

    }

    @Test
    public void testCase1() {
        // qualifying results include: "abcaba", "abzacd", "abfacf"
        assertQualifyingResult("ab?ac?");
    }

    @Test
    public void testCase2() {
        // qualifying results include: "rdveawgab"
        assertQualifyingResult("rd?e?wg??");
    }

    @Test
    public void testCase3() {
        // qualifying results include: "codility"
        assertQualifyingResult("????????");
    }

    @Test
    public void singleUnknown() {
        // qualifying results include: "a"
        assertQualifyingResult("?");
    }

    @Test
    public void doubleUnknown() {
        // qualifying results include: "ab"
        assertQualifyingResult("??");
    }

    @Test
    public void edgesUnknown() {
        // qualifying results include: "ab"
        assertQualifyingResult("?abcdef?");
    }

    @Test
    public void emptyRiddle() {
        // the only qualifying result is: ""
        assertQualifyingResult("");
    }

    private void assertQualifyingResult(final String riddle) {
        assertCorrectSolution(riddle, new Solution().solution(riddle));
    }

    private void assertCorrectSolution(final String riddle, final String solution) {
        final char[] chars = solution.toCharArray();
        for (int i = 0; i < solution.length() - 1; i++) {
            if (chars[i] == chars[i + 1]) {
                fail(format("riddle(%s) produced incorrect solution(%s)", riddle, solution));
            }
        }
    }

}
