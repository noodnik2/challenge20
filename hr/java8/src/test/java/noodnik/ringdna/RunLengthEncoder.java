package noodnik.ringdna;

import static org.junit.Assert.assertEquals;

import java.util.function.BiConsumer;

import org.junit.Test;

public class RunLengthEncoder {    
    
    @Test
    public void givenTestCase() {
        assertEquals("3a2b1c", runLengthEncode("aaabbc"));
    }
    
    @Test
    public void edgeTestCase1() {
        assertEquals("", runLengthEncode(""));
    }
    
    @Test
    public void edgeTestCase2() {
        assertEquals("1a", runLengthEncode("a"));
    }
    
    @Test
    public void edgeTestCase3() {
        assertEquals("1a1b1c1d1e3f", runLengthEncode("abcdefff"));
    }
    
    @Test
    public void edgeTestCase4() {
        assertEquals("3a", runLengthEncode("aaa"));
    }
    
    @Test
    public void edgeTestCase5() {
        assertEquals("1a4f", runLengthEncode("affff"));
    }
    
    @Test
    public void edgeTestCase6() {
        assertEquals("1a2b2a1c1a", runLengthEncode("abbaaca"));
    }
    
    static class RleSink {
        public RleSink(BiConsumer<Integer, Character> drain) {
            this.drain = drain;
        }
        final BiConsumer<Integer, Character> drain;
        int runLength;
        Character currentChar;
        void accumulate(char c) {
            if (currentChar != null) {
                if (currentChar == c) {
                    runLength++;
                    return;
                }
                flush();
            }
            runLength = 1;
            currentChar = c;
        }
        void flush() {
            if (runLength > 0) {
                drain.accept(runLength,  currentChar);
            }
        }
    }
    
    String runLengthEncode(String s) {
        StringBuffer stringBuffer = new StringBuffer();
        RleSink sink = new RleSink((l, c) -> stringBuffer.append("" + l + c));         
        for (char c : s.toCharArray()) {             
            sink.accumulate(c);
        }
        sink.flush();
        return stringBuffer.toString();
    }
     
}