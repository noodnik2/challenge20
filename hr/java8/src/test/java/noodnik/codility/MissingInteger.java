package noodnik.codility;

import org.junit.Test;

import java.util.BitSet;

import static noodnik.codility.MissingInteger.Solution.MAX_VALUE;
import static noodnik.codility.MissingInteger.Solution.MIN_VALUE;
import static org.junit.Assert.assertEquals;

public class MissingInteger {

    /**
     *  Return smallest positive integer (greater than 0) that does not occur in A[]
     *
     *  Examples:
     *      [1, 3, 6, 4, 1, 2] => 5
     *      [1, 2, 3] => 4
     *      [−1, −3] => 1
     *
     *  A.length is 1..100,000
     *  A[*] are +/−10^6
     *
     */
    public static
    class Solution {

        static final int MIN_VALUE = -1_000_000;
        static final int MAX_VALUE = 1_000_000;

        BitSet valueSet;

        public int solution(int[] A) {

            int minA = MAX_VALUE;
            int maxA = MIN_VALUE;
            for (final int a : A) {
                if (a <= 0) {
                    continue;
                }
                if (a < minA) {
                    minA = a;
                }
                if (a > maxA) {
                    maxA = a;
                }
            }

            if (minA > 1) {
                return 1;
            }

            if (maxA == minA) {
                return maxA + 1;
            }

            final int nbits = maxA - minA + 1;
            valueSet = new BitSet(nbits);
            for (final int a : A) {
                if (a > 0) {
                    valueSet.set(a - minA);
                }
            }

            for (int i = 0; i < nbits; i++) {
                if (!valueSet.get(i)) {
                    return Math.max(minA + i, 1);
                }
            }

            return Math.max(maxA + 1, 1);
        }

    }

    @Test
    public void emptyInput() {
        assertEquals(1, new Solution().solution(new int[0]));
    }

    @Test
    public void singleNegativeInput() {
        assertEquals(1, new Solution().solution(new int[] { -1 }));
    }

    @Test
    public void singleZeroInput() {
        assertEquals(1, new Solution().solution(new int[] { 0 }));
    }

    @Test
    public void singlePositiveInput() {
        assertEquals(1, new Solution().solution(new int[] { 100 }));
    }

    @Test
    public void challengeInput1() {
        assertEquals(5, new Solution().solution(new int[] { 1, 3, 6, 4, 1, 2 }));
    }

    @Test
    public void challengeInput2() {
        assertEquals(4, new Solution().solution(new int[] { 1, 2, 3 }));
    }

    @Test
    public void challengeInput3() {
        assertEquals(1, new Solution().solution(new int[] { -1, -3 }));
    }

    @Test
    public void extremeMinMaxValue() {
        // minimal and maximal values [got 1 expected 6]
        assertEquals(6, new Solution().solution(new int[] { MIN_VALUE, MAX_VALUE, 1, 2, 3, 4, 5 }));
    }

    @Test
    public void positiveOnly() {
        // shuffled sequence of 0...100 and then 102...200 [got 100 expected 101]
        assertEquals(101, new Solution().solution(positiveOnlySequence()));
    }

    @Test
    public void medium() {
        // chaotic sequences length=10005 (with minus) [got 1 expected 111]
        assertEquals(111, new Solution().solution(mediumSequence()));
    }

    @Test
    public void large3() {
        // chaotic + many -1, 1, 2, 3 (with minus) [got 9998 expected 10000]
        assertEquals(10_000, new Solution().solution(large3Sequence()));
    }

    private int[] large3Sequence() {
        final int[] large3Seqence = new int[13_332];
        int seq = 1;
        for (int i = 0; i < large3Seqence.length; i++) {
            large3Seqence[i] = (i % 4) == 3 ? -1 : seq++;
        }
        return large3Seqence;
    }

    private int[] positiveOnlySequence() {
        final int[] shuffledSequence = new int[200];
        final int halfSize = 200 / 2;
        for (int i = 0; i < halfSize; i++) {
            final int index = i * 2;
            shuffledSequence[index] = i + 1;
            shuffledSequence[index + 1] = halfSize + i + 2;
        }
        return shuffledSequence;
    }

    private int[] mediumSequence() {
        final int[] chaoticSequences = new int[10_005];
        for (int i = 0; i < chaoticSequences.length; i++) {
            chaoticSequences[i] = ((i % 110) + 1);
        }
        chaoticSequences[chaoticSequences.length / 2] = -1;
        return chaoticSequences;
    }

}
