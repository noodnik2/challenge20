package noodnik.marqeta;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

import noodnik.classic.UkkonenSuffixTree;

/**
 * 
 * Given a string, s, a substring is defined as a non-empty string that can be obtained by one of the following means:
 * 
 * Remove zero or more characters from the left side of s.
 * Remove zero or more characters from the right side of s.
 * Remove zero or more characters from the left side of s and remove zero or more characters from the right side of s.
 *  
 * 
 * For example, let s = abcde. The substrings are:
 * 
 * 
 *  1  abcde
 *  2  abcd
 *  3  bcde
 *  4  abc
 *  5  bcd
 *  6  cde
 *  7  ab
 *  8  bc
 *  9  cd
 * 10  de
 * 11  a 
 * 12  b
 * 13  c
 * 14  d
 * 15  e
 *  
 * 
 * Function Description 
 * 
 * Complete the function substringCalculator in the editor below. The function must return the number of distinct substrings of string s.
 * 
 *
 * substringCalculator has the following parameter(s):
 * 
 *     s:  the string to analyze
 * 
 * 
 * Constraints
 * 
 * String s consists of characters in the range ascii[a-z].
 * 0 ≤ |s| ≤ 105
 *  
 * 
 * Input Format for Custom Testing
 * Sample Case 0
 * Sample Input 0
 * 
 * kincenvizh
 * Sample Output 0
 * 
 * 53
 * Explanation 0
 * 
 * String s = kincenvizh has the following distinct substrings:
 * 
 *  1 kincenvizh
 *  2 kincenviz
 *  3 kincenvi
 *  4 kincenv
 *  5 kincen
 *  6 kince
 *  7 kinc
 *  8 kin
 *  9 ki
 * 10 k
 * 11 incenvizh
 * 12 incenviz
 * 13 incenvi
 * 14 incenv
 * 15 incen
 * 16 ince
 * 17 inc
 * 18 in
 * 19 i
 * 20 ncenvizh
 * 21 ncenviz
 * 22 ncenvi
 * 23 ncenv
 * 24 ncen
 * 25 nce
 * 26 nc
 * 27 n
 * 28 cenvizh
 * 29 cenviz
 * 30 cenvi
 * 31 cenv
 * 32 cen
 * 33 ce
 * 34 c
 * 35 envizh
 * 36 enviz
 * 37 envi
 * 38 env
 * 39 en
 * 40 e
 * 41 nvizh
 * 42 nviz
 * 43 nvi
 * 44 nv
 * 45 vizh
 * 46 viz
 * 47 vi
 * 48 v
 * 49 izh
 * 50 iz
 * 51 zh
 * 52 z
 * 53 h
 *
 */
public class HrSubstringCalculator {
    
    static boolean PRINT_SUBSTRINGS = false;
    
    static final int BIG_STRING_SIZE = 10000;
    static final int BIG_STRING_COUNT = 259675;
    
    @Test
    public void submittedSubstringCalculatorTestCase() {
        assertTestCases(() -> new SubmittedSubstringCalculator());
    }
    
    @Test
    public void revisedSubstringCalculatorTestCase() {
        assertTestCases(() -> new RevisedSubstringCalculator());
    }
    
    @Test
    public void ukkonenSubstringCalculatorTestCase() {
        assertTestCases(() -> new UkkonenSubstringCalculator());
    }
    
    void assertTestCases(Supplier<SubstringCalculator> substringCalculatorFactory) {

        log("testing(%s)", substringCalculatorFactory.get().getClass().getSimpleName());
        long startTimeMillis = System.currentTimeMillis();
        assertCorrectResult(53, "kincenvizh", substringCalculatorFactory.get());
        assertCorrectResult(15, "banana", substringCalculatorFactory.get());
        assertCorrectResult(0, "", substringCalculatorFactory.get());
        assertCorrectResult(BIG_STRING_COUNT, longString(BIG_STRING_SIZE), substringCalculatorFactory.get());
        assertCorrectResult(BIG_STRING_SIZE, longStringOf(BIG_STRING_SIZE, "a"), substringCalculatorFactory.get());
        assertCorrectResult(
            5555, 
            "thisisaverylongstringsowhatwillyoudoaboutthathuhpleasecontinuemesothatIcanbeaverylongstringthankyouverymuch", 
            substringCalculatorFactory.get()
        );
        assertCorrectResult(46, "abcabxabcd", substringCalculatorFactory.get());
        assertCorrectResult(480, "dedododeeodoeodooedeeododooodoede", substringCalculatorFactory.get());
        assertCorrectResult(9, "ooooooooo", substringCalculatorFactory.get());
        assertCorrectResult(53, "mississippi", substringCalculatorFactory.get());
        log("  elapsed(%sms)", System.currentTimeMillis() - startTimeMillis);
        
    }
    
    void assertCorrectResult(int expectedResult, String s, SubstringCalculator resultCalculator) {
        long actualResult = resultCalculator.substringCalculator(s);
        assertEquals(expectedResult, actualResult);
    }
    
    String longString(int size) {
        return longStringOf(size, "abcdefghijklmnopqrstuvwxyz");
    }
    
    String longStringOf(int size, String charset) {
        char[] longchars = new char[size];
        for (int i = 0; i < size; i++) {
            int c = (int) charset.charAt(i % charset.length());
            longchars[i] = (char) c;
        }
        return String.valueOf(longchars);
    }

    static void println(String s) {
        if (!PRINT_SUBSTRINGS) {
            return;
        }
        System.out.println(s);
    }
    
    interface SubstringCalculator {
        int substringCalculator(String s);
    }
    
    static class SubmittedSubstringCalculator implements SubstringCalculator {

        Set<String> seenBefore = new HashSet<>();

        //
        // how to get rid of recursion
        // instead of the program stack, use a Queue<String> 
        // inside a loop, push (left, right) (separately) onto the queue (stack)
        // and process them, until the queue is empty
        //
        
        public int substringCalculator(String s) {

            // if s null or empty return
            // if |s| is 1 add s, return
            // split s into two, add both sides, recurse on both l, r, return
            
            if (s == null || s.isEmpty()) {
                return 0;
            }
        
            if (seenBefore.contains(s)) {
                return 0;
            }
            seenBefore.add(s);
            println(s);
            
            int len = s.length(); // abcd
            if (len == 1) {
                return 1;
            }
            
            String left = s.substring(0, len - 1);   // abc
            String right = s.substring(1);   // bcd

            return 1 + substringCalculator(left) + substringCalculator(right);

        }

    }

    static class RevisedSubstringCalculator implements SubstringCalculator {
        
        public int substringCalculator(String s0) {

            Set<String> seenBefore = new HashSet<>();
            Queue<String> inProgressQ = new LinkedList<>();

            // if s null or empty return
            // if |s| is 1 add s, return
            // split s into two, add both sides, recurse on both l, r, return
            
            inProgressQ.offer(s0);
            
            while(!inProgressQ.isEmpty()) {
                
                String s = inProgressQ.poll();
                if (s == null || s.isEmpty()) {
                    continue;
                }
        
                if (seenBefore.contains(s)) {
                    continue;
                }
                
                seenBefore.add(s);
                println(s);
                
                int len = s.length(); // abcd
                if (len == 1) {
                    continue;
                }
                
                String left = s.substring(0, len - 1);   // abc
                String right = s.substring(1);   // bcd
                
                inProgressQ.add(left);
                inProgressQ.add(right);
            }
            
            return seenBefore.size();

        }

    }
    
    static class UkkonenSubstringCalculator implements SubstringCalculator {

        public int substringCalculator(String s) {
            return new UkkonenSuffixTree(s).calcSum();
        }
        
    }
    
}
