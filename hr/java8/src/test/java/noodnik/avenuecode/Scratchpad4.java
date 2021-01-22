package noodnik.avenuecode;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static noodnik.lib.Common.listOfInts;
import static noodnik.lib.Common.listOfLongs;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

public class Scratchpad4 {

    @Test
    public void sampleCase1() {
        assertEquals(
            listOfLongs(3L, 7L),
            maxSubsetSum(listOfInts(2, 4))
        );
    }

    @Test
    public void sampleCase2() {
        assertEquals(
            listOfLongs(28L),
            maxSubsetSum(listOfInts(12))
        );
    }

    @Test
    public void sampleTestCase3() {

        assertExpectedResult(
            "sampleTestCase3",
            new long[] {
                98, 36, 186, 1, 14, 13, 56, 128, 144, 121, 1, 98, 140, 127, 112, 4, 54, 156, 98, 1, 180, 54, 80, 18,
                60, 18, 60, 90, 38, 60, 3, 62, 132, 42, 56, 63, 140, 24, 98, 28, 1, 48, 60, 96, 32, 224, 36, 8, 98,
                32, 14, 96, 36, 168, 180, 32, 114, 32, 124, 8, 126, 121, 168, 3, 57, 68, 80, 60, 42, 171, 156, 60,
                3, 112, 24, 168, 224, 54, 12, 13, 120, 96, 78, 32, 40, 121, 60, 128, 36, 96, 24, 74, 72, 68, 195,
                93, 13, 39, 80, 14, 186, 96, 144, 72, 96, 180, 62, 40, 171, 14, 140, 28, 234, 72, 80, 120, 72, 156,
                132, 217, 60, 42, 91, 171, 120, 121, 91, 6, 72, 24, 96, 32, 12, 112, 80, 108, 31, 4, 24, 124, 128,
                48, 168, 195, 252, 96, 84, 128, 217, 186, 60, 48, 124, 120, 72, 42, 60, 217, 84, 12, 96, 72, 14, 96,
                84, 38, 91, 42, 80, 80, 28, 15, 96, 48, 15, 132, 96, 168, 30, 186, 224, 91, 144, 6, 39, 132, 20, 108,
                195, 171, 8, 78, 195, 90, 120, 120, 112, 217, 124, 38, 84, 32, 168, 30, 171, 62, 72, 91, 24, 128, 31,
                234, 80, 56, 120, 156, 74, 15, 24, 72, 18, 127, 80, 18, 32, 6, 42, 96, 140, 56, 91, 6, 195, 98, 40,
                12, 91, 74, 96, 90, 56, 120, 42, 96, 84, 96, 195, 32, 120, 98, 80, 144, 144, 60, 12, 93, 40, 72, 32,
                126, 28, 42, 14, 96, 13, 60, 62, 168, 168, 120, 104, 132, 6, 24, 7, 32, 156, 3, 144, 98, 252, 144,
                120, 54, 54, 90, 98, 62, 108, 234, 54, 48, 96, 4, 168, 20, 56, 12, 98, 12, 18, 20, 128, 42, 72, 144,
                32, 98, 20, 114, 120, 57, 168, 8, 124, 12, 127, 126, 62, 31, 62, 80, 112, 60, 32, 126, 56, 195, 24,
                20, 30, 8, 12, 38, 31, 180, 24, 13, 171, 7, 1, 128, 18, 7, 72, 104, 252, 72, 144, 54, 14, 18, 4, 168,
                54, 57, 120, 40, 114, 234, 63, 126, 48, 168, 39, 96, 96, 120, 60, 39, 54, 90, 93, 120, 168, 62, 124,
                12, 96, 96, 68, 30, 56, 44, 171, 15, 114, 1, 72, 7, 124, 140, 42, 40, 217, 132, 90, 124, 57, 180, 54,
                144, 6, 39, 74, 180, 84, 31, 96, 14, 44, 186, 91, 28, 32, 180, 72, 38, 90, 144, 127, 96, 12, 96, 132,
                4, 63, 18, 72, 112, 96, 90, 28, 54, 7, 120, 1, 18, 4, 54, 30, 31, 96, 121, 120, 54, 18, 168, 121, 72,
                90, 36, 114, 30, 24, 127, 84, 13, 127, 98, 24, 74, 72, 13, 20, 72, 120, 98, 7, 224, 72, 60, 30, 132,
                96, 168, 114, 96, 144, 104, 30, 126, 127, 60, 72, 32, 38, 40, 40, 96, 12, 31, 32, 48, 48, 68, 42, 108,
                90, 98, 78, 114, 56, 96, 171, 186, 80, 120, 60, 127, 44, 57, 44, 18, 84, 96, 98, 60, 84, 1, 84, 108,
                114, 96, 24, 124, 128, 124, 168, 44, 84, 195, 42, 124, 224, 32, 156, 30, 31, 42, 140, 144, 56, 24
            },
            new int[] {
                97, 22, 80, 1, 13, 9, 39, 93, 70, 81, 1, 52, 76, 64, 91, 3, 53, 99, 97, 1, 88, 34, 79, 10,
                59, 17, 24, 40, 37, 38, 2, 61, 86, 20, 39, 32, 76, 15, 97, 12, 1, 35, 38, 62, 31, 84, 22, 7, 52,
                31, 13, 77, 22, 78, 88, 21, 74, 21, 48, 7, 82, 81, 92, 2, 49, 67, 57, 38, 20, 98, 99, 38, 2, 91,
                14, 60, 84, 34, 6, 9, 95, 69, 45, 31, 27, 81, 24, 93, 22, 69, 14, 73, 55, 67, 72, 50, 9, 18, 57,
                13, 80, 42, 66, 30, 42, 88, 61, 27, 98, 13, 76, 12, 90, 71, 57, 95, 71, 99, 86, 100, 59, 20, 36,
                98, 95, 81, 36, 5, 71, 14, 42, 31, 11, 91, 57, 85, 16, 3, 15, 75, 93, 47, 92, 72, 96, 42, 65, 93,
                100, 80, 59, 33, 48, 95, 55, 26, 38, 100, 44, 11, 62, 55, 13, 69, 83, 37, 36, 41, 57, 79, 12, 8,
                69, 35, 8, 86, 42, 60, 29, 80, 84, 36, 66, 5, 18, 86, 19, 85, 72, 98, 7, 45, 72, 40, 87, 87, 91,
                100, 48, 37, 65, 21, 92, 29, 98, 61, 30, 36, 23, 93, 25, 90, 57, 39, 56, 99, 73, 8, 15, 46, 17,
                64, 57, 10, 31, 5, 26, 69, 76, 28, 36, 5, 72, 52, 27, 11, 36, 73, 42, 40, 28, 56, 20, 69, 65,
                77, 72, 31, 56, 97, 57, 94, 70, 59, 11, 50, 27, 71, 31, 82, 12, 41, 13, 62, 9, 24, 61, 78, 92,
                87, 63, 86, 5, 14, 4, 21, 99, 2, 66, 97, 96, 70, 56, 53, 53, 89, 52, 61, 85, 90, 34, 47, 77, 3,
                78, 19, 28, 11, 97, 6, 17, 19, 93, 20, 46, 70, 31, 52, 19, 74, 87, 49, 92, 7, 75, 6, 64, 68, 61,
                16, 61, 57, 91, 24, 21, 82, 28, 72, 23, 19, 29, 7, 6, 37, 25, 88, 14, 9, 98, 4, 1, 93, 10, 4, 30,
                63, 96, 51, 70, 34, 13, 10, 3, 60, 53, 49, 54, 27, 74, 90, 32, 68, 47, 60, 18, 69, 42, 54, 38, 18,
                34, 58, 50, 87, 60, 61, 48, 6, 69, 62, 67, 29, 28, 43, 98, 8, 74, 1, 71, 4, 48, 76, 20, 27, 100,
                86, 58, 75, 49, 88, 53, 66, 5, 18, 73, 88, 44, 25, 62, 13, 43, 80, 36, 12, 31, 88, 51, 37, 89, 66,
                64, 42, 11, 42, 86, 3, 32, 17, 71, 91, 69, 40, 12, 53, 4, 56, 1, 17, 3, 34, 29, 16, 62, 81, 95,
                34, 17, 92, 81, 46, 89, 22, 74, 29, 14, 64, 83, 9, 64, 52, 14, 73, 30, 9, 19, 30, 87, 52, 4, 84,
                71, 38, 29, 86, 42, 60, 74, 77, 94, 63, 29, 68, 64, 38, 55, 21, 37, 27, 27, 77, 6, 25, 21, 33,
                33, 67, 26, 85, 58, 97, 45, 74, 39, 77, 98, 80, 79, 95, 59, 64, 43, 49, 43, 10, 44, 77, 97, 59,
                65, 1, 65, 85, 74, 42, 14, 48, 93, 75, 60, 43, 44, 72, 41, 48, 84, 21, 99, 29, 25, 20, 76, 94, 28, 23
            }
        );

    }

    private static void assertExpectedResult(
        final String message,
        final long[] expectedOutput,
        final int[] testInput
    ) {
        Assert.assertEquals(testInput.length, expectedOutput.length);

        int failureCount = 0;
        for (int i = 0; i < testInput.length; i++) {
            final int n = testInput[i];
            List<Long> r = maxSubsetSum(listOfInts(n));
            Assert.assertEquals(1, r.size());
            final long actual = r.get(0);
            final long expected = expectedOutput[i];
            if (expected != actual) {
                log("%s: expected(%s) actual(%s)", n, expected, actual);
                failureCount++;
            }
        }
        log("failureCount(%s) out of(%s)", failureCount, testInput.length);

        assertEquals(
            listOfLongs(expectedOutput),
            maxSubsetSum(listOfInts(testInput))
        );

    }

    @Test
    public void sampleCase3a() {
        assertEquals(
            listOfLongs(36),
            maxSubsetSum(listOfInts(22))
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
     *
     * @param k array of n integers
     * @return sums calculated for each array[i]
     */
    public static List<Long> maxSubsetSum(List<Integer> k) {
        return k.stream().map(Scratchpad4::sumFactors).collect(Collectors.toList());
    }

    /**
     * Strategy:
     *  First find list of primes <= 1/2 n
     *  for each prime which evenly divides n,
     *  add both it and all factors of the result
     */
    private static long sumFactors(int n) {

        // handle degenerate cases
        if (n == 1) return 1;
        if (n == 2) return 3;

        // collect set of multiplicands of each prime
        // less than n/2 that evenly divides n
        final Collection<Integer> factors = new HashSet<>();
        loadFactors(factors, n, findPrimes(n / 2));

        // return sum of unique factors
        return 1 + n + factors.stream().mapToLong(Integer::intValue).sum();
    }

    private static void loadFactors(
        final Collection<Integer> factors,
        final int n,
        final List<Integer> primes
    ) {
        for (final int prime : primes) {
            if (n != prime && n % prime == 0) {
                factors.add(prime);
                factors.add(n / prime);
                loadFactors(factors, n / prime, primes);
            }
        }
    }

    /** @return list of primes <= maxNumber */
    private static List<Integer> findPrimes(int maxNumber) {
        final List<Integer> primes = new ArrayList<>();
        for (int n = 1; n <= maxNumber; n++) {
            if (isPrime(n)) {
                primes.add(n);
            }
        }
        return primes;
    }

    // use primality single prime finder
    public static boolean isPrime(final int n) {
        if (n <= 3) {
            return n > 1;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

}
