package noodnik.pramp;

import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static noodnik.lib.Common.log;
import static noodnik.lib.Common.listOf;

/**
 * K-Messed Array Sort
 *
 * Given an array of integers arr where each element is at most k
 * places away from its sorted position, code an efficient function
 * sortKMessedArray that sorts arr. For instance, for an input array
 * of size 10 and k = 2, an element belonging to index 6 in the sorted
 * array will be located at either index 4, 5, 6, 7 or 8 in the input array.
 *
 * Analyze the time and space complexities of your solution.
 *
 * Example:
 *  input:  arr = [1, 4, 5, 2, 3, 7, 8, 6, 10, 9], k = 2
 *  output: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 *
 * Constraints:
 *  [time limit] 5000ms
 *  [input] array.integer arr
 *  1 ≤ arr.length ≤ 100
 *  [input] integer k
 *  0 ≤ k ≤ 20
 *  [output] array.integer
 *
 */
public class KMessedArraySort {

    @Test
    public void testJoesSolution() {
        runTestCases(new JoesSolution());
    }

    @Test
    public void testSuggestedSolution() {
        runTestCases(new SuggestedSolution());
    }

    @Test
    public void testNaiiveSortSubarraySolution() {
        runTestCases(new NaiiveSortSubarraySolution());
    }

    /**
     *
     * Thoughts:
     *
     *  Q:  How can we take advantage of list's partially sorted order?
     *
     *  A:  Sorting N sub-arrays of size k (where k <= N) using an algorithm
     *      having average performance of k log k will result in an average
     *      performance of N * k log k.  This is expected to be faster than
     *      using a single sort of the entire array with performance N log N.
     *
     *      Due to their small sizes, it's appropriate to delegate sorting of
     *      the sub-arrays to Java's {@code Arrays.sort()} method, having
     *      an expected average performance of k log k (or better, since it's
     *      adaptive to a rumoured extent ;-).
     *
     * Approach:
     *
     *  1. Use library method for sorting sub-lists
     *
     *  k values:
     *
     *      (k = 0) => already completely sorted
     *      (k = x) => sort N (x * 2 - 1) tuple sub-array(s)
     *
     *  Time complexity: N * (k log k)
     *  Space complexity: log k
     *
     */
    static class NaiiveSortSubarraySolution implements Solution {

        public int[] sortKMessedArray(final int[] arr, final int k) {

            if (k == 0) {
                return arr;
            }

            for (int i = 0; i < arr.length - k; i++) {
                Arrays.sort(arr, i, i + k + 1);
            }

            return arr;
        }

    }

    /**
     *  Suggested solution of using a Min-Max heap - makes sense!
     *  Time complexity: n log k
     *  Space complexity: k
     */
    static class SuggestedSolution implements Solution {

        public int[] sortKMessedArray(final int[] arr, final int k) {

            if (k == 0) {
                return arr;
            }

            final Queue<Integer> minHeap = new PriorityQueue<>();
            for (int i = 0; i < k; i++) {
                minHeap.add(arr[i]);
            }

            final int[] output = new int[arr.length];
            int outputIndex = 0;
            for (int i = k; i < arr.length; i++) {
                minHeap.add(arr[i]);
                output[outputIndex++] = minHeap.remove();
            }

            while(!minHeap.isEmpty()) {
                output[outputIndex++] = minHeap.remove();
            }

            return output;
        }

    }

    /**
     *  Joe's solution
     *  Time complexity: N log k
     *  Space complexity: k
     */
    static class JoesSolution implements Solution {

        /**
         arr = [1, 4, 5, 2, 3, 7, 8, 6, 10, 9]

         i , [i - k, i + k]

         O(n*logn)

         index : [i - k, i + k]  2k + 1

         minHeap<>  [0, i + k]

         O(nlogk)
         O(k)

         */
        public
        int[] sortKMessedArray(int[] arr, int k) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            int i = 0;
            while (i <= k && i < arr.length) {
                minHeap.add(arr[i++]);
            }

            for (int j = 0; j < arr.length; j++) {
                arr[j] = minHeap.poll();
                if (i < arr.length) {
                    minHeap.add(arr[i++]);
                }
            }

            return arr;
        }

    }

    void runTestCases(final Solution solution) {

        final TestCase[] testCases = {
            new TestCase(
                "Example Test Case",
                new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
                new int[] { 1, 4, 5, 2, 3, 7, 8, 6, 10, 9 },
                2
            ),
            new TestCase(
                "Test Case #1",
                new int[] { 1 },
                new int[] { 1 },
                0
            ),
            new TestCase(
                "Test Case #2",
                new int[] { 0, 1 },
                new int[] { 0, 1 },
                1
            ),
            new TestCase(
                "Test Case #3",
                new int[] { 0,1,2,3 },
                new int[] { 1,0,3,2 },
                1
            ),
            new TestCase(
                "Test Case #4",
                new int[] { 0,1,2,3,4,5,6,7,8 },
                new int[] { 1,0,3,2,4,5,7,6,8 },
                1
            ),
            new TestCase(
                "Test Case #5",
                new int[] { 1,2,3,4,5,6,7,8,9,10 },
                new int[] { 1,4,5,2,3,7,8,6,10,9 },
                2
            ),
            new TestCase(
                "Test Case #6",
                new int[] { 0,1,2,3,4,5,6,7,8,9,10,11 },
                new int[] { 6,1,4,11,2,0,3,7,10,5,8,9 },
                6
            )
        };

        log("\n%s:", solution.getClass().getSimpleName());
        for (final TestCase testCase : testCases) {
            log(" - %s", testCase.name);
            assertCorrectAnswer(testCase, solution);
        }

    }

    interface Solution {
        int[] sortKMessedArray(int[] arr, int k);
    }

    static class TestCase {
        String name;
        int[] expectedOutput;
        int[] input;
        int k;
        TestCase(String name, int[] expectedOutput, int[] input, int k) {
            this.name = name;
            this.expectedOutput = expectedOutput;
            this.input = input;
            this.k = k;
        }
    }

    static void assertCorrectAnswer(final TestCase testCase, final Solution solution) {
        final int[] actualOutput = solution.sortKMessedArray(testCase.input, testCase.k);
        assertEquals(listOf(testCase.expectedOutput), listOf(actualOutput));
    }

}
