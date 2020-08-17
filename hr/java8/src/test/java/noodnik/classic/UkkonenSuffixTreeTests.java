package noodnik.classic;

import static org.junit.Assert.assertEquals;
import static noodnik.lib.Common.log;

import org.junit.Test;

/**
 * see:
 * - https://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf
 * - https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 */
public class UkkonenSuffixTreeTests {

    @Test
    public void firstTestCase() {       
        assertEquals(53, new UkkonenSuffixTree("kincenvizh").calcSum());        
    }

    @Test
    public void singleCharTestCase() {       
        assertEquals(67, new UkkonenSuffixTree("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").calcSum());        
    }

    @Test
    public void emptyTestCase() {       
        assertEquals(0, new UkkonenSuffixTree("").calcSum());
    }

    @Test
    public void multiWalkTreeTestCase0() {
        assertEquals(15, new UkkonenSuffixTree("banana").calcSum());
    }

    @Test
    public void multiWalkTreeTestCase1() {
        assertEquals(1, new UkkonenSuffixTree("banana").calcSum(0, 0));
    }

    @Test
    public void multiWalkTreeTestCase2() {
        assertEquals(1, new UkkonenSuffixTree("kincenvizh").calcSum(1, 1)); 
    }

    @Test
    public void multiWalkTreeTestCase3() {
        assertEquals(6, new UkkonenSuffixTree("kincenvizh").calcSum(1, 3)); // inc
    }

    @Test
    public void multiWalkTreeTestCase4() {
        assertEquals(15, new UkkonenSuffixTree("kincenvizh").calcSum(3, 7)); // TODO fix
    }

    @Test
    public void multiWalkTreeTestCase5() {
        assertEquals(46, new UkkonenSuffixTree("abcabxabcd").calcSum(0, 9)); // TODO fix
    }    

    @Test
    public void multiWalkTreeTestCase6() {
        assertEquals(3, new UkkonenSuffixTree("abcabxabcd").calcSum(0, 1));
    }    
    
    @Test
    public void drawTreeTestCase() {       
        new UkkonenSuffixTree("banana", s -> log(s)).draw();        
    }

}
