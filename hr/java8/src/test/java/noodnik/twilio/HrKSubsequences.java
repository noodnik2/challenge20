package noodnik.twilio;
import static java.util.Arrays.copyOfRange;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static noodnik.lib.Common.*;

import org.junit.Test;

public class HrKSubsequences {
	
	/**
	 * 	A k-subsequence of an array is defined as follows:
	 * 	<ul>
	 * 		<li>it is a sub-array, i.e. a subsequence of contiguous elements in the array</li>
	 * 		<li>the <i>sum</i> of the subsequence's elements, s, is evenly divisible by k, i.e.: <i>sum mod k = 0</i>.
	 * 	</ul>
	 */
	
	@Test
	public void testCase0() {
		/**
			{ 5 },
			{ 5, 10 },
			{ 5, 10, 11, 9 },
			{ 5, 10, 11, 9, 5 },
			{ 10 },
			{ 10, 11, 9 },
			{ 10, 11, 9, 5 },
			{ 11, 9 },
			{ 11, 9, 5 
		 */
		assertCorrectResult(9, 5, new int[] { 5, 10, 11, 9, 5 });
	}
	
	long kSub(int k, int[] nums) {
		// every node of the tree contains the sum of each of its children
		// return the number of nodes whose value is evenly divisible by k
		int subCount = 0;
		for (int[] subSequence : subSequences(nums)) {
//            log("subseq(%s)", listOf(subSequence));
			if ((IntStream.of(subSequence).sum() % k) == 0) {
	            log("yes! subseq(%s)", listOf(subSequence));
				subCount++;
			}
		}
		return subCount;
	}
	
	static class IW {
	    int[] ia;
	    IW(int[] iwo) {
	        ia = iwo;
	    }
	    public boolean equals(Object io) {
	        if (!(io instanceof IW)) {
	            return false;
	        }
	        final IW iwo = (IW) io;
	        return Arrays.equals(ia, iwo.ia);
	    }
        public int hashCode() {
            return Arrays.hashCode(ia);
        }
	}
	
	int[][] subSequences(int[] sequence) {
		
		int[][] previous;
		int[][] next;
		int sequenceLength = sequence.length;
		if (sequenceLength < 2) {
			previous = new int[0][];
			next = new int[0][];
		} else {
			previous = subSequences(copyOfRange(sequence, 0, sequenceLength - 1));
			next = subSequences(copyOfRange(sequence, 1, sequenceLength));
		}

        final Set<IW> iwSet = new HashSet<>(previous.length + next.length + 1);
		for (int[] p : previous) iwSet.add(new IW(p));
		iwSet.add(new IW(sequence));
		for (int[] n : next) iwSet.add(new IW(n));
		
		int[][] result = new int[iwSet.size()][];
		
		int i = 0;
		for (final IW iw : iwSet) {
		    result[i++] = iw.ia;
		}
		return result;
	}
	
	void assertCorrectResult(long expectedResult, int k, int[] testInput) {
		assertEquals(expectedResult, kSub(k, testInput));
	}

}
