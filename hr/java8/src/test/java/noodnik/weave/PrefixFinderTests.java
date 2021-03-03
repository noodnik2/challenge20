package noodnik.weave;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;


import static noodnik.lib.Common.listOf;
import static noodnik.lib.Common.listOfInts;
import static org.junit.Assert.assertEquals;

public class PrefixFinderTests {

    @Test
    public void testPrefixFinder() {
        assertCorrectPrefixesFound(
            new BruteForcePrefixFinder(),
            listOfInts(2, 1, 0, 1, 1),
            listOf(
                "steve",
                "stevens",
                "danny",
                "steves",
                "dan",
                "john",
                "johnny",
                "joe",
                "alex",
                "alexander"
            ),
            listOf(
                "steve",
                "alex",
                "joe",
                "john",
                "dan"
            )
        );
    }

    void assertCorrectPrefixesFound(
        PrefixFinder prefixFinder,
        List<Integer> expectedResults,
        List<String> names,
        List<String> query
    ) {
        assertEquals(
            expectedResults,
            prefixFinder.findCompletePrefixes(names, query)
        );
    }

    /**
     *  Given a list of names, determine the number of names in that list
     *  for which a given query string is a prefix.  The prefix must be
     *  at least 1 character less than the entire name string.
     *
     *  Example:
     *      names = ['jackson', 'jacques', 'jack]
     *      query = 'jack'
     *
     *  Returns:
     *      int[q]: each value[i] is the answer to query[i]
     *
     *  Constraints:
     *      1 <= n <= 20000
     *      2 <= names[i], query[i] <= 30
     *      1 <= sum of lengths of all names[i] <= 5 x 10^5
     *      1 <= q <= 200
     *
     */
    interface PrefixFinder {

        /**
         * @param names list of n name strings
         * @param query list of q query strings
         * @return list of answers[i: 0..q] for query[i]
         */
        List<Integer> findCompletePrefixes(
            List<String> names, // list of name strings
            List<String> query  // list of query strings
        );
    }


    private static class BruteForcePrefixFinder implements PrefixFinder {
        public List<Integer> findCompletePrefixes(List<String> names, List<String> queries) {
            List<Integer> queryCounts = new ArrayList<>();
            for (String query : queries) {
                int count = 0;
                for (String name : names) {
                    if (isPrefix(name, query)) {
                        count++;
                    }
                }
                queryCounts.add(count);
            }
            return queryCounts;
        }

        boolean isPrefix(String name, String prefix) {
            return name.startsWith(prefix) && name.length() > prefix.length();
        }

    }

}
