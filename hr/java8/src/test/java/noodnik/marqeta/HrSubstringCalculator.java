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
            return new UkkonenSuffixTree().calcSum(s);
        }
        
    }
    
}
