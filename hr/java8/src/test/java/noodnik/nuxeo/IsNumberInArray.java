package noodnik.nuxeo;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 *  The aim of this exercise is to check the presence of a number in an array.
 * </p>
 *
 * <p />
 * <h3>Specifications:</h3>
 * <ul>
 *  <li>the items are integers arranged in ascending order
 *  <li>the array can contain up to 1 million items
 *  <li>the array is never {@code null}
 * </ul>
 *
 * <p>
 *  Implement the method {@code boolean exists(int[] ints, int k)} so that
 *  it returns {@code true} if {@code k} belongs to {@code ints},
 *  otherwise the method should return {@code false}.
 * </p>
 *
 * <p>
 *  Important note: Try to save CPU cycles if possible.
 * </p>
 *
 * <p />
 * <h3>Example:</h3>
 * <pre>
 *  {@code int[] ints = {-9, 14, 37, 102}}
 *  {@code exists(ints, 102)} returns {@code true}
 *  {@code exists(ints, 36)} returns {@code false}
 * </pre>
 *
 */
public class IsNumberInArray {

    private final static int[] INTS = {-9, 14, 37, 102};

    interface IntFinder {
        boolean exists(int[] ints, int k);
    }

    private static class UsingLibraryRoutine implements IntFinder {
        public boolean exists(int[] ints, int k) {
            return Arrays.binarySearch(ints, k) >= 0;
        }
    }

    private static class UsingHandCodedBinarySearch implements IntFinder {
        public boolean exists(int[] ints, int k) {
            int low = 0;
            int high = ints.length;
            while (low < high) {
                final int index = ((high - low) / 2) + low;
                final int v = ints[index];
                if (v == k) {
                    return true;
                }
                if (v < k) {
                    low = index + 1;
                } else {
                    high = index;
                }
            }
            return false;
        }
    }

    @Test(timeout = 5000L)
    public void testLibraryRoutine() {
        runTests(new UsingLibraryRoutine());
    }

    @Test(timeout = 5000L)
    public void testHandCodedBinarySearch() {
        runTests(new UsingHandCodedBinarySearch());
    }

    private void runTests(IntFinder intFinder) {

        assertTrue("simpleFound", intFinder.exists(INTS, 102));
        assertFalse("simpleNotFound", intFinder.exists(INTS, 36));
        assertFalse("emptyEdge", intFinder.exists(new int[0], 36));

        final int[] largeArray = createLargeArray();
        assertTrue("largeFound", intFinder.exists(largeArray, largeArray.length / 2));
        assertFalse("largeNotFound", intFinder.exists(largeArray, largeArray.length));

    }

    private static final int LARGE_ARRAY_SIZE = 1000000;
    private static int[] createLargeArray() {
        final int[] array = new int[LARGE_ARRAY_SIZE];
        for (int i = 0; i < LARGE_ARRAY_SIZE; i++) {
            array[i] = i;
        }
        return array;
    }

}
