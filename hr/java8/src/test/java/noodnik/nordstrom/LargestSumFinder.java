package noodnik.nordstrom;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.of;

public class LargestSumFinder {

    /**
     * @param input array of integers
     * @return largest contiguous sum found within {@code input}, or {@code null}
     */
    static Long findLargest(final int[] input)
    {
        if (input == null) {
            return null;
        }

        if (input.length == 0) {
            return null;
        }

        if (input.length == 1) {
            return (long) input[0];
        }

        Long max = null;
        Long sum = null;
        for (final int val : input) {
            if (max == null) {
                sum = (long) val;
                max = sum;
                continue;
            }
            if ((sum + val) < 0L) {
                sum = 0L;
                continue;
            }
            sum += val;
            if (sum > max) {
                max = sum;
            }
        }

        return max;
    }

    public static void main(String[] args)
    {
        assertPasses("t1", 6L, new int[] { 2, 3, -4, 5 });
        assertPasses("t2", 5L, new int[] { 2, 3, -4, 3 });
        assertPasses("t3", 5L, new int[] { 2, 3, -6, 5 });
        assertPasses("t4", 6L, new int[] { 2, 3, -6, 6 });
        assertPasses("t5", 5L, new int[] { 2, 3, -6, 4 });
        assertPasses("null input", null, null);
        assertPasses("empty", null, new int[] { });
        assertPasses("one-neg", -1L, new int[] { -1 });
        assertPasses("two-neg", -1L, new int[] { -1, -2 });
        assertPasses("min-value", (long) MIN_VALUE, new int[] { MIN_VALUE });
        assertPasses("max-value", (long) MAX_VALUE, new int[] { MAX_VALUE });
        assertPasses("minmax-values", (long) MAX_VALUE, new int[] { MAX_VALUE, MIN_VALUE });
        assertPasses("integer overflow", 2L * MAX_VALUE, new int[] { MAX_VALUE, MAX_VALUE });
        System.out.println("All tests pass");
    }

    static void assertPasses(String testCaseName, Long expectedValue, int[] input)
    {
        final Long actualValue = findLargest(input);
        final boolean failed;
        if (expectedValue == null) {
            failed = actualValue != null;
        } else {
            failed = !expectedValue.equals(actualValue);
        }
        if (!failed) {
            return;
        }
        throw new RuntimeException(
            String.format(
                "testCaseName(%s), actualValue(%s), expectedValue(%s) input(%s)",
                testCaseName,
                actualValue,
                expectedValue,
                input == null ? "null" : of(input).boxed().collect(toList())
            )
        );
    }

}

