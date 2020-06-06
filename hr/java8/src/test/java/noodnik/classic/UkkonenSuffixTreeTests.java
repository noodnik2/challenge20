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
        assertEquals(1, new UkkonenSuffixTree("banana").calcSum(0, 1)); // TODO fix
    }    

    @Test
    public void drawTreeTestCase() {       
        new UkkonenSuffixTree("banana", s -> log(s)).draw();        
    }    

}
