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

    @Test
    public void doubleFlushTestCase() {
        StringBuffer stringBuffer = new StringBuffer();
        rleSinkTo("abbccc", (l, c) -> stringBuffer.append("" + l + c)).flush().flush();
        assertEquals("1a2b3c", stringBuffer.toString());
    }
    
    static class RleSink {
        
        public RleSink(BiConsumer<Integer, Character> drain) {
            this.drain = drain;
        }
        
        final BiConsumer<Integer, Character> drain;
        int runLength;
        Character currentChar;
        
        RleSink accumulate(char c) {
            if (currentChar != null) {
                if (currentChar == c) {
                    runLength++;
                    return this;
                }
                flush();
            }
            runLength = 1;
            currentChar = c;
            return this;
        }
        
        RleSink flush() {
            if (runLength > 0) {
                drain.accept(runLength,  currentChar);
                runLength = 0;
            }
            return this;
        }
        
    }
    
    String runLengthEncode(String s) {
        StringBuffer stringBuffer = new StringBuffer();
        rleSinkTo(s, (l, c) -> stringBuffer.append("" + l + c)).flush();
        return stringBuffer.toString();
    }

    private RleSink rleSinkTo(String s, BiConsumer<Integer, Character> drain) {
        RleSink sink = new RleSink(drain);
        for (char c : s.toCharArray()) {             
            sink.accumulate(c);
        }
        return sink;
    }
     
}