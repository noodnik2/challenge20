package noodnik.turbonomic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static noodnik.lib.Common.listOfLongs;
import static org.junit.Assert.assertEquals;

public class MaxDoublerTests {

    /**
     * Given an array of long integers (arr) and a number (num),
     * iterate through the elements in arr and double the value
     * of num whenever an element equals num.  Arr can be re-
     * ordered before the iteration to maximize the value of num.
     * Find the maximum possible value of num.
     *
     * Example:
     * arr = [1, 2, 4, 11, 12, 8]
     * num = 2
     *
     * Maximal value of num is 16
     *
     *     2
     *  1  2
     *  2  4
     *  4  8
     * 11  8
     * 12  8
     *  8 16
     *
     *  long doubleSize(List<Long> arr, long num)
     *  returns the maximal value of num
     *
     *  constraints:
     *  1 <= n <= 10^6
     *  0 <= arr[i] <= 10^16
     *  0 <= num <= 10^4
     *
     *  Sample cases:
     *
     *  0: arr = [1, 2, 3, 1, 2], num = 1 should produce 4 (requires rearranging to [1, 1, 2, 2, 3])
     *  1: arr = [1, 1, 1], num = 1 should produce 2
     *  2: arr = [2, 5, 4, 6, 8], num = 2 should produce 16 (rearrange to [2, 4, 5, 6, 8])
     *
     */
    @Test
    public void testExample1() {
        assertEquals(
            4,
            new FirstSolution().doubleSize(listOfLongs(1, 2, 3, 1, 2), 1)
        );
    }

    @Test
    public void testExample2() {
        assertEquals(
            2,
            new FirstSolution().doubleSize(listOfLongs(1, 1, 1), 1)
        );
    }

    @Test
    public void testExample3() {
        assertEquals(
            16,
            new FirstSolution().doubleSize(listOfLongs(2, 5, 4, 6, 8), 2)
        );
    }

    static class FirstSolution {

        public long doubleSize(final List<Long> arr, final long numInitial) {

            long num = numInitial;

            final Map<Long, Long> counts = arr.stream().collect(
                groupingBy(identity(), HashMap::new, counting())
            );

            for (int i = 0; i < arr.size(); i++) {
                final long targetNum = num;
                final Long countForNum = counts.get(targetNum);
                if (countForNum != null && countForNum > 0) {
                    num *= 2;
                    counts.put(targetNum, countForNum - 1);
                }
            }

            return num;
        }

    }

}
