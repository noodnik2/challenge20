package noodnik.avenuecode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static noodnik.lib.Common.listOfInts;
import static noodnik.lib.Common.listOfLongs;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

public class Scratchpad2 {

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

    /**
     * Strategy:
     *  First find list of primes <= 1/2 n
     *  for each prime which evenly divides n, add both it and the result
     */
    private static long sumFactors(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 3;
        }
        final List<Integer> halfPrimes = findPrimes(n / 2);
        Set<Integer> factors = new HashSet<>();
        factors.add(1);
        factors.add(n);
        for (int prime : halfPrimes) {
            if (n % prime != 0) {
                continue;
            }
            factors.add(prime);
            factors.add(n / prime);
        }
        return factors.stream().mapToInt(Integer::intValue).sum();
    }

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

    public static List<Integer> findPrimes(int maxNumber) {
        final List<Integer> primes = new ArrayList<>();
        for (int n = 1; n <= maxNumber; n++) {
            if (isPrime(n)) {
                primes.add(n);
            }
        }
        return primes;
    }


}
