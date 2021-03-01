package noodnik.disney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import noodnik.lib.Common;
import org.junit.Test;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

public class TripletFinderTests {

    /**
     * Given an array nums of n integers, are there elements
     * a, b, c in nums such that a + b + c = 0?
     *
     * Find all unique triplets in the array which gives the
     * sum of zero.
     *
     * Example: Given array nums = [-1, 0, 1, 2, -1, -4]
     *
     * Solution set is: [ [-1, 0, 1], [-1, -1, 2] ]
     *
     */

    @Test
    public void testBruteForceTripletFinderSimpleCase() {

        assertExpectedTripletSet(
            new BruteForceTripletFinder(),
            new int[][] {
                new int[] { -1, 0, 1 },
                new int[] { -1, -1, 2 }
            },
            new int[] { -1, 0, 1, 2, -1, -4 }
        );

    }

    private static void assertExpectedTripletSet(
        TripletFinder tripletFinder,
        int[][] expectedTriplets,
        int[] values
    ) {

        int[][] actualTriplets = tripletFinder.findUniqueTriplets(values);
        assertEquals("unexpected length", expectedTriplets.length, actualTriplets.length);

        Set<List<Integer>> expectedSet = new HashSet<>();
        for (int[] expectedValue : expectedTriplets) {
            expectedSet.add(Common.listOfInts(expectedValue));
        }

        Set<List<Integer>> actualSet = new HashSet<>();
        for (int[] actualValue : actualTriplets) {
            actualSet.add(Common.listOfInts(actualValue));
        }

        log("expected(%s), actual(%s)", expectedSet, actualSet);
        assertEquals("unexpected values", expectedSet, actualSet);
    }

    interface TripletFinder {
        int[][] findUniqueTriplets(int[] values);
    }

    private static class BruteForceTripletFinder implements TripletFinder {

        // eliminate duplicates => n values
        // n take 3 => n * (n - 1) * (
        // go over all of those keeping those which sum to 0

        public int[][] findUniqueTriplets(int[] values) {
            return getZeroSumTriplets(
                getUniqueTriplets(values)
            );
        }

        private int[][] getUniqueTriplets(int[] values) {

            List<int[]> uniqueTriplets = new ArrayList<>();
            Set<List<Integer>> resultSet = new HashSet<>();

            int [] sortedValues = values.clone();
            Arrays.sort(sortedValues);
            for (int i = 0; i < sortedValues.length; i++) {
                for (int j = i + 1; j < sortedValues.length; j++) {
                    for (int k = j + 1; k < sortedValues.length; k++) {
                        int[] triplet = {
                            sortedValues[i],
                            sortedValues[j],
                            sortedValues[k]
                        };
                        List<Integer> comparableTriplet = Common.listOfInts(triplet);
                        if (resultSet.contains(comparableTriplet)) {
                            continue;
                        }
                        resultSet.add(comparableTriplet);
                        uniqueTriplets.add(triplet);
                    }
                }
            }

            return uniqueTriplets.toArray(new int[0][0]);
        }

        private int[][] getZeroSumTriplets(int[][] allTriplets) {
            List<int[]> resultList = new ArrayList<>();
            for (int[] sample : allTriplets) {
                int sum = 0;
                for (int value : sample) {
                    sum += value;
                }
                if (sum == 0) {
                    resultList.add(sample);
                }
            }
            return resultList.toArray(new int[0][0]);
        }

    }

}


