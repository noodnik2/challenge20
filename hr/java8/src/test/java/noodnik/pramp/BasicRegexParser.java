package noodnik.pramp;

import org.junit.Test;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;


/**
 *  Basic Regex Parser
 *  Implement a regular expression function isMatch that supports the '.' and '*' symbols.
 *  The function receives two strings - text and pattern - and should return true if the
 *  text matches the pattern as a regular expression. For simplicity, assume that the actual
 *  symbols '.' and '*' do not appear in the text string and are used as special symbols only
 *  in the pattern string.
 *  
 *  In case you aren’t familiar with regular expressions, the function determines if the text
 *  and pattern are the equal, where the '.' is treated as a single a character wildcard (see
 *  third example), and '*' is matched for a zero or more sequence of the previous letter (see
 *  fourth and fifth examples). For more information on regular expression matching, see the
 *  Regular Expression Wikipedia page.
 *  
 *  Explain your algorithm, and analyze its time and space complexities.
 *  
 *  Provided Examples:
 *  
 *      input:  text = "aa", pattern = "a" => output: false
 *      input:  text = "aa", pattern = "aa" => output: true
 *      input:  text = "abc", pattern = "a.c" => output: true
 *      input:  text = "abbb", pattern = "ab*" => output: true
 *      input:  text = "acd", pattern = "ab*c." => output: true
 *
 *  New Examples:
 *      input:  text = "abbbbbbbcd", pattern = "ab*c." => output: true
 *      input:  text = "ac", pattern = "ab*c" => output: true
 *
 *  Constraints:
 *  
 *      [time limit] 5000ms
 *      [input] string text
 *      [input] string pattern
 *      [output] boolean
 *
 *  Initial Tests & Results:
 *
 *      Test Case #1 - Input: "", "", Expected: true, Actual: true ✔︎
 *      Test Case #2 - Input: "aa", "a", Expected: false, Actual: false ✔︎
 *      Test Case #3 - Input: "bb", "bb", Expected: true, Actual: true ✔︎
 *      Test Case #4 - Input: "", "a*", Expected: true, Actual: false ✘
 *      Test Case #5 - Input: "abbdbb", "ab*d", Expected: false, Actual: false ✔︎
 *      Test Case #6 - Input: "aba", "a.a", Expected: true, Actual: true ✔︎
 *      Test Case #7 - Input: "acd", "ab*c.", Expected: true, Actual: true ✔︎
 *      Test Case #8 - Input: "abaa", "a.*a*", Expected: true, Actual: false ✘
 *
 */
public class BasicRegexParser {

    @Test
    public void testNaiiveSolution() {
        runTestCases(new NaiiveSolution());
    }

    @Test
    public void testRecursiveSolution() {
        runTestCases(new RecursiveSolution());
    }

    static class NaiiveSolution implements Solution {

        public
        boolean isMatch(final String text, final String pattern) {

            int text_offset = 0;
            int pattern_offset = 0;

            while(text_offset < text.length() && pattern_offset < pattern.length()) {
                char ic = text.charAt(text_offset);
                char pc = pattern.charAt(pattern_offset);
                if (pc == '.') {
                    text_offset++;
                    pattern_offset++;
                    continue;
                }
                if (pc == '*') {
                    if (pattern_offset == 0) {
                        throw new RuntimeException();
                    }
                    char match_c = pattern.charAt(pattern_offset - 1);
                    if (ic == match_c) {
                        text_offset++;
                        continue;
                    }
                    pattern_offset++;
                    continue;
                }
                if (ic != pc) {
                    if (pattern_offset < pattern.length() - 1) {
                        char next_pattern_char = pattern.charAt(pattern_offset + 1);
                        if (next_pattern_char == '*') {
                            pattern_offset += 2;
                            continue;
                        }
                    }
                    return false;
                }
                text_offset++;
                pattern_offset++;
            }

            return (
                text_offset == text.length()
             && (pattern_offset == pattern.length() || pattern.charAt(pattern.length() - 1) == '*')
            );

        }

    }

    static class RecursiveSolution implements Solution {

        public
        boolean isMatch(final String text, final String pattern) {

            if (pattern.length() == 0) {
                return text.length() == 0;
            }

            final char firstPatternChar = pattern.charAt(0);

            if (pattern.length() > 1 && pattern.charAt(1) == '*') {

                final char matchedChar;
                if (firstPatternChar == '.') {
                    if (text.length() == 0) {
                        return true;
                    }
                    matchedChar = text.charAt(0);
                } else {
                    matchedChar = firstPatternChar;
                }

                int text_offset = 0;
                while(text_offset < text.length() && text.charAt(text_offset) == matchedChar) {
                    text_offset++;
                }
                return isMatch(text.substring(text_offset), pattern.substring(2));
            }

            final char firstTextChar = text.charAt(0);
            if (firstPatternChar == '.' || firstPatternChar == firstTextChar) {
                return isMatch(text.substring(1), pattern.substring(1));
            }

            return false;
        }

    }

    void runTestCases(final Solution solution) {

        final TestCase[] testCases = {
            new TestCase("Given Example #1", false, "aa", "a"),
            new TestCase("Given Example #2", true, "aa", "aa"),
            new TestCase("Given Example #3", true, "abc", "a.c"),
            new TestCase("Given Example #4", true, "abbb", "ab*"),
            new TestCase("Given Example #5", true, "acd", "ab*c."),
            new TestCase("New Example #1", true, "abbbbbbbcd", "ab*c."),
            new TestCase("New Example #2", true, "ac", "ab*c"),
            new TestCase("New Example #3", true, "", ".*"),
            new TestCase("Test Case #1", true, "", ""),
            new TestCase("Test Case #2", false, "aa", "a"),
            new TestCase("Test Case #3", true, "bb", "bb"),
            new TestCase("Test Case #4", true, "", "a*"),
            new TestCase("Test Case #5", false, "abbdbb", "ab*d"),
            new TestCase("Test Case #6", true, "aba", "a.a"),
            new TestCase("Test Case #7", true, "acd", "ab*c."),
            new TestCase("Test Case #8", true, "abaa", "a.*a*"),
        };

        log("\n%s:", solution.getClass().getSimpleName());
        for (final TestCase testCase : testCases) {
            log(" - %s (\"%s\", \"%s\")", testCase.name, testCase.text, testCase.pattern);
            assertCorrectAnswer(testCase, solution);
        }

    }

    interface Solution {
        boolean isMatch(String text, String pattern);
    }

    static class TestCase {
        final String name;
        final boolean expectedMatched;
        final String text;
        final String pattern;
        TestCase(String name, boolean expectedMatched, String text, String pattern) {
            this.name = name;
            this.expectedMatched = expectedMatched;
            this.text = text;
            this.pattern = pattern;
        }
    }

    static void assertCorrectAnswer(final TestCase testCase, final Solution solution) {
        final boolean matched = solution.isMatch(testCase.text, testCase.pattern);
        assertEquals(testCase.expectedMatched, matched);
    }

}
