package noodnik.avenuecode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static noodnik.lib.Common.listOfInts;
import static noodnik.lib.Common.listOfLongs;
import static org.junit.Assert.assertEquals;

public class Scratchpad {

    @Test
    public void sampleCase0() {
        assertEquals(
            listOfLongs(3L, 7L),
            maxSubsetSum(listOfInts(2, 4))
        );
    }

    @Test
    public void sampleCase1() {
        assertEquals(
            listOfLongs(28L),
            maxSubsetSum(listOfInts(12))
        );
    }

    /**
     *  For each number in the array, get the sum of its factors.
     *  Return an array of results.
     *
     *  Example:
     *    arr = [12]
     *
     *  The factors of arr[0] = 12 are [1, 2, 3, 4, 6, 12].
     *  The sum of these factors is 28.  Return the array [28].
     *
     * Constraints:
     *    1 <= n <= 10^3
     *    1 <= arr[i] <= 10^9
     * @param k array of n integers
     * @return sums calculated for each array[i]
     */
    public static List<Long> maxSubsetSum(List<Integer> k) {
        final List<Long> sums = new ArrayList<>(k.size());
        for (int n : k) {
            sums.add(sumFactors(n));
        }
        return sums;
    }

    private static long sumFactors(int n) {
        if (n == 1) {
            return 1;
        }
        return sumFactors(n / 2) + n;
    }

}
