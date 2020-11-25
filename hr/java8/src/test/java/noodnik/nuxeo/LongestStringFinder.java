package noodnik.nuxeo;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *  
 *
 */
public class LongestStringFinder {

    interface Solution {
        /**
         * return the longest string contained inside the input array
         * can be array of array of array
         * @param array input Array of values
         * @return null if input is null and the longest string otherwise
         */
        String getLongestString(Object[] array);
    }

    static class SimpleSolution implements Solution {

        /**
         * return the longest string contained inside the input array
         * @param array input Array of values
         * @return null if input is null and the longest string otherwise
         */
        public String getLongestString(Object[] array) {
            if (array == null) {
                return null;
            }
            String longestString = null;
            for (final Object o : array) {
                if (o instanceof String) {
                    final String s = (String) o;
                    if (longestString == null || s.length() > longestString.length()) {
                        longestString = s;
                    }
                }
                else if (o instanceof Object[]) {
                    for (final Object on : (Object[]) o) {
                        if (on instanceof String) {
                            final String sn = (String) on;
                            if (longestString == null || sn.length() > longestString.length()) {
                                longestString = sn;
                            }
                        }
                    }
                }
                // NOTE: requirements do not cover this case; ignored
            }
            return longestString;

        }
    }

    static class LibrarySolution1 implements Solution {

        // https://github.com/soffiane/CodingameAndCodewars/blob/master/src/main/java/codingame/LongestString.java
        public String getLongestString(Object[] array) {
            if (array == null || array.length == 0) {
                return null;
            }
            final String s = Arrays.deepToString(array);
            final String[] split = s.replaceAll("[\\[\\](){}]", "").split(", ");
            return Arrays.stream(split).max(Comparator.comparingInt(String::length)).get();
        }

    }

    @Test
    public void testSimpleSolution() {
        runTests(new SimpleSolution());
    }

    @Test
    public void testLibrarySolution1() {
        runTests(new LibrarySolution1());
    }

    private void runTests(final Solution solution) {
        assertEquals("abc", solution.getLongestString(new Object[] { "a", "ab", "abc" }));
        assertEquals("abcd     ddd", solution.getLongestString(new Object[] { "a", "abcd     ddd", "abc" })); // expected
        assertNull(solution.getLongestString(null));
        assertEquals("defefg", solution.getLongestString(new Object[] { "a", new Object[] { "ab", "defefg"}, "abc" }));
        assertEquals("dedd  fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd  fdddefg"} }));
//        assertEquals("dedd , fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd , fdddefg"} }));
//        assertEquals("dedd [ fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd [ fdddefg"} }));
//        assertEquals("dedd ] fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd ] fdddefg"} }));
//        assertEquals("dedd ( fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd ( fdddefg"} }));
//        assertEquals("dedd ) fdddefg", solution.getLongestString(new Object[] { new Object[] { null, "dedd ) fdddefg"} }));
    }

}
