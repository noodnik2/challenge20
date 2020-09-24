package noodnik.pramp;

import org.junit.Test;

import static noodnik.lib.Common.listOf;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

/**
 *  Challenge template
 */
public class EmptyChallenge {

    /**
     *
     * Thoughts:
     *
     * Approach:
     *
     *  Time complexity:
     *  Space complexity:
     *
     */
    static class NaiiveSolution implements Solution {

        public Object solveProblem(final Object input) {
            return "example expectedOutput";
        }

    }

    @Test
    public void testNaiiveSolution() {
        runTestCases(new NaiiveSolution());
    }

    void runTestCases(final Solution solution) {

        final TestCase[] testCases = {
            new TestCase(
                "Example Test Case",
                "example expectedOutput",
                "example input"
            )
        };

        log("\n%s:", solution.getClass().getSimpleName());
        for (final TestCase testCase : testCases) {
            log(" - %s", testCase.name);
            assertCorrectAnswer(testCase, solution);
        }

    }

    interface Solution {
        Object solveProblem(Object input);
    }

    static class TestCase {
        String name;
        Object expectedOutput;
        Object input;
        TestCase(String name, Object expectedOutput, Object input) {
            this.name = name;
            this.expectedOutput = expectedOutput;
            this.input = input;
        }
    }

    static void assertCorrectAnswer(final TestCase testCase, final Solution solution) {
        final Object actualOutput = solution.solveProblem(testCase.input);
        assertEquals(listOf(testCase.expectedOutput), listOf(actualOutput));
    }

}
