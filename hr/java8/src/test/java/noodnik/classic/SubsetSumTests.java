package noodnik.classic;

import static org.junit.Assert.assertEquals;
import static noodnik.lib.Common.log;

import org.junit.Test;

/**
 *  Determine if there exists a subset of positive integers having a sum of S.
 */
public class SubsetSumTests {

    @Test
    public void emptyTestCase() {
        assertCorrectResult(true, 0);
    }
    
    @Test
    public void easyTestCase() {
        assertCorrectResult(true, 6, 3, 2, 7, 1);
    }

    @Test
    public void hardTestCase() {
        assertCorrectResult(true, 45000, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000);
    }
    
    void assertCorrectResult(boolean expectedResult, int sum, int... set) {
        assertEquals(expectedResult, new DynamicSubsetWithSumFinder().canFind(set,  sum));
    }

    interface SubsetWithSumFinder {
        boolean canFind(int[] set, int s);
    }
    
    /**
     *  Use "dynamic programming" method
     *  Complexity:
     *      memory: O(Sn)
     *      time: O(Sn)
     */
    static class DynamicSubsetWithSumFinder implements SubsetWithSumFinder {

        @Override
        public boolean canFind(int[] set, int sum) {
            
            // (x, y) => (values in set, possible sums up to sum)
            boolean[][] sumTracker = new boolean[set.length + 1][sum + 1];      
                        
            // empty subset has sum 0
            for (int i = 0; i <= set.length; i++) {
                sumTracker[i][0] = true;
            }

            //
            for (int i = 1; i <= set.length; i++) {
                for (int j = 1; j <= sum; j++) {                
                    
                    // copy from above
                    sumTracker[i][j] = sumTracker[i - 1][j];
                    
                    // if !solution[i][j] check if can be made
                    if (!sumTracker[i][j] && j >= set[i - 1]) {
                        sumTracker[i][j] = sumTracker[i - 1][j - set[i - 1]];
                    }
                    
                }
            }
            
            // dump(set, sumTracker);
            
            return sumTracker[set.length][sum];
        }

        void dump(int[] set, boolean[][] sumTracker) {
            for (int i = 0; i < sumTracker.length; i++) {
                boolean[] sums = sumTracker[i];
                char[] sumStatuses = new char[sums.length];
                for (int j = 0; j < sums.length; j++) {
                    sumStatuses[j] = sums[j] ? '1' : '_';
                }
                log(
                    "[%s] %s",
                    i == 0 ? "0" : set[i - 1],
                    String.copyValueOf(sumStatuses)
                );
            }
        }
        
    }

}
