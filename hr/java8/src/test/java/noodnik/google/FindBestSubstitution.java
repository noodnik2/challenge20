package noodnik.google;

import static noodnik.lib.Common.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 *  You're given an array of integers like:
 *  
 *  [1, 7, 7, 2, 3, 6, 7, 0, 8, 2, 4, 9, 8, -20].
 *  
 *  On this array, you're allowed to perform one operation
 *  to substitute any integer in the array for any other integer
 *  in existence (**ALL** of the 8s for 0s, for example).
 *  
 *  Substituting the 8s with 0s, you would get an array like so:
 *  
 *  [1, 7, 7, 2, 3, 6, 7, 0, 0, 2, 4, 9, 0, -20].
 *  
 *  Using a substitution (or not), what is the longest set of contiguous
 *  non-decreasing integers you can create?
 *  
 *  
 *  E.G. without any swaps, the longest contiguous, non-decreasing set
 *  of integers is 4:
 *  
 *           [2, 3, 6, 7]      [2, 4, 9, 8]
 *  [1, 7, 7, 2, 3, 6, 7, 0, 8, 2, 4, 9, 8, -20]
 *  
 *  
 *  If you swapped 8s for 0s, the maximum length is 5:
 *  
 *                       [0, 0, 2, 4, 9]
 *  [1, 7, 7, 2, 3, 6, 7, 0, 0, 2, 4, 9, 0, -20]
 *  
 *  But is this the maximum length?
 *  
 */

/**
 * 
 *  Pseudo-code:
 *  
 *  1. generate list0 of [startIndex, endIndex) of all sequences in original input (input0)
 *  2. get current longest sequence length from list0
 *  3. for each sequence in list0:
 *  
 *          create copy of input0 as input1 
 *          substitute all values equal to the value just prior to the sequence in input1
 *              with the value at the start of the sequence
 *          generate list1 of [startIndex, endIndex) of all sequences in input1
 *          get longest sequence length1 from list1
 *          update current longest sequence length, from & to values if length1 > current 
 *  
 *          create copy of input0 as input2 
 *          substitute all values equal to the value just after the sequence in input2
 *              with the value at the end of the sequence
 *          generate list2 of [startIndex, endIndex) of all sequences in input2
 *          get longest sequence length2 from list2
 *          update current longest sequence length, from & to values if length2 > current 
 *
 */

public class FindBestSubstitution {
    
    @Test
    public void givenTestCase() {
        
        assertCorrectAnswer(
            new int[] { 6, 7, 2, },
            new int[] { 1, 7, 7, 2, 3, 6, 7, 0, 8, 2, 4, 9, 8, -20 }
        );
        
    }
    
    @Test
    public void rightEdgeTestCase() {
        
        assertCorrectAnswer(
            new int[] { 5, -20, 9, },
            new int[] { 1, 2, 4, 9, -20 }
        );
        
    }
    
    @Test
    public void leftEdgeTestCase() {
        
        assertCorrectAnswer(
            new int[] { 4, 20, 2, },
            new int[] { 20, 2, 4, 9 }
        );
        
    }

    @Test
    public void scalarTestCase() {
        
        assertCorrectAnswer(
            new int[] { 1, -1, -1 },
            new int[] { 100 }
        );
        
    }

    @Test
    public void emptyTestCase() {
        
        assertCorrectAnswer(
            new int[] { 0, -1, -1, },
            new int[] { }
        );
        
    }

    void assertCorrectAnswer(int[] expectedAnswer, int[] input) {
        int[] output = findLongestSequenceWithOneReplacement(input);
        log("longestSequence(%s) from replacing(%s) with(%s)", output[0], output[1], output[2]);
        for (int i = 0; i < expectedAnswer.length; i++) {
            assertEquals(expectedAnswer[i], output[i]);
        }
    }

    /**
     * @returns array; [0]: longest sequence length, [1]: replacement 'from'; [2]: replacement 'to'
     */
    int[] findLongestSequenceWithOneReplacement(int[] input) {
        
        int[][] seqIndices = getSeqIndices(input);
        
        int longestSequenceLength = longestSequenceLength(seqIndices);
        int[] replacement = null;
        
        for (int tryseqNo = 0; tryseqNo < seqIndices.length; tryseqNo++) {
            
            int[] inputWithLeftReplacement = input.clone();
            int[] leftReplacement = replace(inputWithLeftReplacement, seqIndices[tryseqNo][0] - 1, seqIndices[tryseqNo][0]);
            if (leftReplacement != null) {
//                log("try left replacement(%s) => %s", listOf(leftReplacement), listOf(inputWithLeftReplacement));
                int longestSequenceLength2 = longestSequenceLength(getSeqIndices(inputWithLeftReplacement));
                if (longestSequenceLength2 > longestSequenceLength) {
                    replacement = leftReplacement;
                    longestSequenceLength = longestSequenceLength2;
                }
            }
            
            int[] inputWithRightReplacement = input.clone();
            int[] rightReplacement = replace(inputWithRightReplacement, seqIndices[tryseqNo][1], seqIndices[tryseqNo][1] - 1);
            if (rightReplacement != null) {
//                log("try right replacement(%s) => %s", listOf(rightReplacement), listOf(inputWithRightReplacement));
                int longestSequenceLength3 = longestSequenceLength(getSeqIndices(inputWithRightReplacement));
                if (longestSequenceLength3 > longestSequenceLength) {
                    replacement = rightReplacement;
                    longestSequenceLength = longestSequenceLength3;
                }
            }
            
        }
        
        return(
            new int[] {
                longestSequenceLength,
                replacement == null ? -1 : replacement[0],
                replacement == null ? -1 : replacement[1]
            }
        );
        
    }
    
    /**
     *  @param seqIndices array; [x] is array of [start, end) indices of each found sequence
     *  @return length of longest sequence found
     */
    int longestSequenceLength(int[][] seqIndices) {
//        log("seqIndices(%s)", intArrayArrayToLists(seqIndices));
        int longestSequenceLength = -1;
        for (int i = 0; i < seqIndices.length; i++) {
            int sequenceLength = seqIndices[i][1] - seqIndices[i][0];
            if (longestSequenceLength < sequenceLength) {
                longestSequenceLength = sequenceLength;
            }
        }
        return longestSequenceLength;
    }

    int[] replace(int[] input, int fromIndex, int toIndex) {
//        log("replacing from(%s) to(%s)", fromIndex, toIndex);
        if (fromIndex < 0 || fromIndex >= input.length || toIndex < 0 || toIndex >= input.length) {
            return null;
        }
        int from = input[fromIndex];
        int to = input[toIndex];
        for (int i = 0; i < input.length; i++) {
            if (input[i] == from) {
                input[i] = to;
            }
        }
        return new int[] { from, to };
    }
    
    
    int[][] getSeqIndices(int[] input) {
        List<int[]> seqs = new ArrayList<>();
        int currentIndex = 0;
        while(true) {
            int firstDecreasingIndex = findFirstDecreasing(input, currentIndex);
            seqs.add(new int[] { currentIndex, firstDecreasingIndex });
            if (firstDecreasingIndex == input.length) {
                break;
            }
            currentIndex = firstDecreasingIndex;
        }
        return intArrayListToPrimitive(seqs);
    }
    
    int findFirstDecreasing(int[] input, int currentIndex) {
        for (int i = currentIndex + 1; i < input.length; i++) {
            if (input[i] < input[i - 1]) {
                return i;
            }
        }
        return input.length;
    }

}
