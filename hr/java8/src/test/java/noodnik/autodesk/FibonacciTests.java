package noodnik.autodesk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FibonacciTests {

    int fibonacci(int i) {
        if (i < 2) {
            return i;
        }
        return fibonacci(i - 1) + fibonacci(i - 2);
    }
    
    @Test
    public void testCase0() {
        assertEquals(0, fibonacci(0));
    }
    
    @Test
    public void testCase1() {
        assertEquals(1, fibonacci(1));
    }
    
    @Test
    public void testCase2() {
        assertEquals(1, fibonacci(2));
    }
    
    @Test
    public void testCase3() {
        assertEquals(2, fibonacci(3));
    }
    
    @Test
    public void testCase4() {
        assertEquals(3, fibonacci(4));
    }
    
    
}
