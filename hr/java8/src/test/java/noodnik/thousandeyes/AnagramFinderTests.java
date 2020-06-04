package noodnik.thousandeyes;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static noodnik.lib.Common.*;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnagramFinderTests {
           
   /**
    * input: ["acb", "ret", "bac", "Abc", "ter", "bca"]
    * desired output: [["abc", "bac", "bca"], ["ret", "ter"], ["Abc"]]
    * actual output: [[acb, bac, bca], [Abc], [ret, ter]]
    */
    
    @Test
    public void submittedTestCase() {   // was procedural
        assertCorrectOutput(
            asList(
                asList("abc", "bac", "bca"),
                asList("Abc"),
                asList("ret", "ter")
            ),
            asList("abc", "ret", "bac", "Abc", "ter", "bca"),
            new SubmittedAnagramFinder()
        );
    }
    
    @Test
    public void functionalTestCase() {
        assertCorrectOutput(
            asList(
                asList("abc", "bac", "bca"),
                asList("Abc"),
                asList("ret", "ter")
            ),
            asList("abc", "ret", "bac", "Abc", "ter", "bca"),
            new FunctionalAnagramFinder()
        );
    }
    
    void assertCorrectOutput(
        Collection<List<String>> expectedOutput,
        List<String> input,
        AnagramFinder finder
    ) {
        Collection<List<String>> actualOutput = finder.answer(input);
        log("input(%s) => output(%s)", input, actualOutput);
        assertEquals(listsToSets(expectedOutput), listsToSets(actualOutput));
    }
    
    // turn list of lists into set of sets, for comparability
    Set<Set<String>> listsToSets(Collection<List<String>> lists) {
        return lists.stream().map(list -> new HashSet<String>(list)).collect(Collectors.toSet());
    }
    
    interface AnagramFinder {
        Collection<List<String>> answer(List<String> input);
    }
      
    // procedural anagram finder
    static class SubmittedAnagramFinder implements AnagramFinder {

        public Collection<List<String>> answer(List<String> input) {

            Map<Map<Integer, Integer>, List<String>> sForSig = new HashMap<>();

            for (String s : input) {
                Map<Integer, Integer> sSig = signature(s);
                if (sForSig.get(sSig) == null) {
                    sForSig.put(sSig, new LinkedList<>());
                }
                sForSig.get(sSig).add(s);          
            }

            return sForSig.values();    

        }

        Map<Integer, Integer> signature(String s) {

            Map<Integer, Integer> charCountMap = new HashMap<>();

            for (int c : s.toCharArray()) {
                if (charCountMap.get(c) == null) {
                    charCountMap.put(c, 0);
                }
                charCountMap.put(c, charCountMap.get(c) + 1);
            }

            return charCountMap;    

        }

    }

    static class FunctionalAnagramFinder implements AnagramFinder {
        
        static class E<K, V> {
            E(K k, V v) { this.k = k; this.v = v; }
            K k;
            V v;
        }

        public Collection<List<String>> answer(List<String> input) {

            return(
                input
                .stream()
                .map(s -> new E<Map<Integer, Integer>, String>(signature(s), s))
                .collect(groupingBy(e -> e.k))  // group by signature
                .values()
                .stream()
                .map(
                    e -> (
                        e
                        .stream()
                        .map(x -> x.v)  
                        .collect(toList())  // list of all values for signature
                    )
                )
                .collect(toList())  // list of anagram lists
            );

        }

        // produce "signature" of the string
        Map<Integer, Integer> signature(String s) {
            return(
                s
                .chars()
                .boxed()
                .collect(groupingBy(c -> c, reducing(0, e -> 1, Integer::sum)))
            );

        }

    }

}

    
