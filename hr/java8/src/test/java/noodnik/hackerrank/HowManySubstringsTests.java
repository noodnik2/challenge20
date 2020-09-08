package noodnik.hackerrank;

import static java.lang.String.format;
import static noodnik.lib.Common.listOf;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

import noodnik.classic.UkkonenSuffixTree;

public class HowManySubstringsTests {

    @Test(timeout = 5000L)
    public void testCase0() {
        String s = "kincenvizh";
        assertCorrectResults(                           
            new int[] { 1, 3, 53, 34, 15, 1 },
            new int[][] {
                { 0, 0 },
                { 0, 1 },
                { 0, s.length() - 1 },
                { 1, s.length() - 2 },
                { 3, s.length() - 3 },
                { s.length() - 1, s.length() - 1 },
            },
            new SimpleSubSubstringCounter(s)
        );
    }

    @Test(timeout = 1000L)
    public void testCaseExp() {
        String s = "abcde";
        assertCorrectResults(                           
            new int[] { 1 },
            new int[][] { { 1, 1 } },
            new SimpleSubSubstringCounter(s)
        );
    }
    
    static class HrInput {
        int[][] queries;
        String s;
        public String toString() { return format("s(%s), q(%s)", s.length(), queries.length); }
    }
    
    @Test(timeout = 10000L)
    public void testCase10() throws FileNotFoundException {
        HrInput hrInput = readInput("noodnik/hackerrank/HowManySubstringsTestsData/input10.txt");
        log("%s", hrInput);
        SimpleSubSubstringCounter simpleSubSubstringCounter = new SimpleSubSubstringCounter(hrInput.s);
        int[] countSubstrings = simpleSubSubstringCounter.countSubstrings(hrInput.queries);
        log("a(%s)", listOf(countSubstrings));
    }    
    
    HrInput readInput(String sourceName) throws FileNotFoundException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(sourceName);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(sourceName);
        }
        
        try(Scanner scanner = new Scanner(resourceAsStream)) {
            HrInput hrInput = new HrInput();
    
            String[] nq = scanner.nextLine().split(" ");
    
            int n = Integer.parseInt(nq[0].trim());
            int q = Integer.parseInt(nq[1].trim());
    
            String s = scanner.nextLine();
            if (s.length() != n) {
                throw new RuntimeException("invalid input - n does not agree with length of s");
            }
    
            int[][] queries = new int[q][2];
    
            for (int queriesRowItr = 0; queriesRowItr < q; queriesRowItr++) {
                String[] queriesRowItems = scanner.nextLine().split(" ");
    
                for (int queriesColumnItr = 0; queriesColumnItr < 2; queriesColumnItr++) {
                    int queriesItem = Integer.parseInt(queriesRowItems[queriesColumnItr].trim());
                    queries[queriesRowItr][queriesColumnItr] = queriesItem;
                }
            }
    
            hrInput.queries = queries;
            hrInput.s = s;
            
            return hrInput;
        }
        
    }
    
    static void assertCorrectResults(
        int[] expectedResults,
        int[][] queries,
        SubSubstringCounter subSubstringCounter
    ) {
        int[] actualResults = subSubstringCounter.countSubstrings(queries);
        assertEquals(listOf(expectedResults), listOf(actualResults));
    }
        
    interface SubstringCalculator {
        int substringCalculator(int left, int right);
    }

    interface SubSubstringCounter {
        int[] countSubstrings(int[][] queries);
    }

    static class SimpleSubSubstringCounter implements SubSubstringCounter {
        
        final SubstringCalculator substringCalculator;
        
        SimpleSubSubstringCounter(String s) {
            substringCalculator = new UkkonenSubstringCalculator(s);
        }
        
        public int[] countSubstrings(int[][] queries) {
            int[] results = new int[queries.length];
            for (int i = 0; i < queries.length; i++) {
                results[i] = substringCalculator.substringCalculator(queries[i][0], queries[i][1]);
            }
            return results;
        }
        
    }

    static class UkkonenSubstringCalculator implements SubstringCalculator {
        final String s;
        UkkonenSubstringCalculator(String s) {
            this.s = s;
        }        
        public int substringCalculator(int left, int right) {
            return new UkkonenSuffixTree(s.substring(left, right + 1)).calcSum();
//            return new UkkonenSuffixTree(s).calcSum(left, right + 1);  // TODO fix
        }        
    }

    static class SimpleSubstringCalculator implements SubstringCalculator {

        final String s;
        SimpleSubstringCalculator(String s) {
            this.s = s;
        }        

        public int substringCalculator(int left, int right) {
            
            String s0 = s.substring(left, right + 1);

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
                
                int len = s.length();                       // abcd
                if (len == 1) {
                    continue;
                }
                
                inProgressQ.add(s.substring(0, len - 1));   // abc
                inProgressQ.add(s.substring(1));            // bcd
            }
            
            return seenBefore.size();

        }

    }

}
