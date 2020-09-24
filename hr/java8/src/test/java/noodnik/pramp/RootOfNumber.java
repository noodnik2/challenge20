package noodnik.pramp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static noodnik.lib.Common.log;

/**
 *   Root of Number
 *
 *   Implement a function root that calculates the n’th root of a number.
 *   The function takes a non-negative number x and a positive integer n,
 *   and returns the positive n’th root of x within an error of 0.001 (i.e.
 *   suppose the real root is y, then the error is: |y-root(x,n)| and must
 *   satisfy |y-root(x,n)| < 0.001).
 *
 *   While there are many algorithms to calculate roots that require prior
 *   knowledge in numerical analysis (some of them are mentioned here),
 *   there is also an elementary method which doesn’t require more than
 *   guessing-and-checking. Try to think more in terms of the latter.
 *
 *   Make sure your algorithm is efficient, and analyze its time and space
 *   complexities.
 *
 *   Examples:
 *
 *      input:  x = 7, n = 3; expected output: 1.913
 *      input:  x = 9, n = 2; expected output: 3
 *
 *  Constraints:
 *
 *      [time limit] 5000ms
 *      [input] float x; 0 ≤ x
 *      [input] integer n; 0 < n
 *      [output] float
 *
 */

public class RootOfNumber {

    @Test(timeout = 5000L)
    public void testNaiiveSolution() {
        runTestCases(new NaiiveSolution());
    }

    @Test(timeout = 5000L)
    public void testLibrarySolution() {
        runTestCases(new LibrarySolution());
    }

    @Test(timeout = 5000L)
    public void testSuggestedSolution() {
        runTestCases(new SuggestedSolution());
    }

    /**
     *
     *  The Goal:
     *
     *      Find the real number which when taken to the 'n'th power will
     *      produce the given 'x' value.
     *
     *  The Naiive Approach:
     *
     *      Leverage the obvious "shortcut" cases: n={0, 1}, x={0, 1}
     *      Iterate over a series of "guesses", converging upon an acceptable answer
     *          Set the initial "guess" and "step" values;
     *          While the guess to the power of n is not an acceptable answer;
     *              if the guess to the power of n is too large;
     *                  increment the "guess" value by the "step" value
     *              else (the guess to the power of n is too small);
     *                  decrement the "guess" value by the "step" value
     *                  half the "step" value (in order to effect convergence)
     *
     *  Considerations:
     *
     *    - The initial "guess" and "step" values were set to 1.0 rather arbitrarily,
     *      given paper & pencil solutions using some of the example problems.  These
     *      obviously won't work well or efficiently for very large input values.
     *
     *    - We need a better way to calculate & set the initial "guess" and "step"
     *      values, and to increment / decrement then within iterations.
     *
     *  Retrospective:
     *
     *    - While a "binary search" approach was correct, the lack of "bounding"
     *      (i.e., the "upper" and "lower" bounds used in the suggested approach)
     *      made finding good starting and converging values illusive.
     *
     *    - The problem constraints weren't read carefully enough; there was no
     *      reason to implement a solution for the "n==0" case (which is technically
     *      "undefined", anyways).
     *
     */
    static class NaiiveSolution implements Solution {

        public double root(final double x, final int n) {

            if (x == 0d || x == 1d || n == 1) {
                return x;
            }

            double guess = 1.0d;
            double step = 1.0d;

            while(true) {
                final double resultFromGuess = Math.pow(guess, n);
                if (Math.abs(resultFromGuess - x) <= 0.001d) {
                    break;
                }
                if (resultFromGuess > x) {
                    guess -= step;
                    step /= 2;
                } else {
                    guess += step;
                }
            }

            return guess;
        }

    }

    /**
     *  If we have the ability to take a number to a real power,
     *  then the answer is simply the number to the 1/nth power.
     */
    static class LibrarySolution implements Solution {

        public double root(final double x, final int n) {

            if (x == 0d || x == 1d || n == 1) {
                return x;
            }

            return Math.pow(x, 1d / n);
        }

    }

    /**
     *  Algorithm from suggested answer
     */
    static class SuggestedSolution implements Solution {

        public double root(final double x, final int n) {

            if (x == 0d || x == 1d || n == 1) {
                return x;
            }

            double lowerBound = 0;
            double upperBound = Math.max(1d, x);
            double approxRoot = (upperBound + lowerBound) / 2;

            while(approxRoot - lowerBound >= 0.001d) {
                final double approxRootToNthPower = Math.pow(approxRoot, n);
                if (approxRootToNthPower == x) {
                    break;
                }
                if (approxRootToNthPower > x) {
                    upperBound = approxRoot;
                } else {
                    lowerBound = approxRoot;
                }
                approxRoot = (upperBound + lowerBound) / 2;
            }

            return approxRoot;
        }

    }

    void runTestCases(final Solution solution) {

        final TestCase[] testCases = {
            new TestCase("Example Case #1", 7.0d, 3),
            new TestCase("Example Case #2", 9.0d, 2),
            new TestCase("Edge Case #1", 1d, 1),
            new TestCase("Edge Case #2", 0d, 1),
            new TestCase("Edge Case #3", 0.5d, 1),
            new TestCase("Edge Case #4", 1d, 2),
            new TestCase("Edge Case #5", 0d, 2),
            new TestCase("Edge Case #6", 0.5d, 2),
            new TestCase("Test Case #1", 4d, 2),
            new TestCase("Test Case #2", 27d, 3),
            new TestCase("Test Case #3", 16d, 4),
            new TestCase("Test Case #4", 3d, 2),
            new TestCase("Test Case #5", 10d, 3),
            new TestCase("Test Case #6", 160d, 3),
            new TestCase("Marty's Case #1", 90000.0d, 2),
            new TestCase("Marty's Case #2", 900000.0d, 5),
        };

        log("\n%s:", solution.getClass().getSimpleName());
        for (final TestCase testCase : testCases) {
            assertCorrectAnswer(testCase, solution);
        }

    }

    interface Solution {
        double root(double x, int n);
    }

    static class TestCase {
        String name;
        double x;
        int n;
        TestCase(String name, double x, int n) {
            if (n <= 0 || x < 0) {
                throw new RuntimeException(String.format("invalid input(x=%s,n=%s)", x, n));
            }
            this.name = name;
            this.x = x;
            this.n = n;
        }
    }

    static void assertCorrectAnswer(final TestCase testCase, final Solution solution) {
        final double actualRoot = solution.root(testCase.x, testCase.n);
        log(" - %s (%s, %s) => %s", testCase.name, testCase.x, testCase.n, actualRoot);
        assertEquals(Math.pow(testCase.x, 1d / testCase.n), actualRoot, 0.001);
    }

}
