package noodnik.nuxeo;

import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCompressor {

    @Test
    public void nullTestCase() {
        assertNull(Solution.getCompressedString(null));
    }

    @Test
    public void emptyTestCase() {
        assertEquals("", Solution.getCompressedString(""));
    }

    @Test
    public void compressTestCase111() {
        assertEquals("abc", Solution.getCompressedString("abc"));
    }

    @Test
    public void compressTestCase321() {
        assertEquals("3a2bc", Solution.getCompressedString("aaabbc"));
    }

    @Test
    public void compressTestCase11113() {
        assertEquals("abcde3f", Solution.getCompressedString("abcdefff"));
    }
    
    @Test
    public void compressTestCase3() {
        assertEquals("3a", Solution.getCompressedString("aaa"));
    }
    
    @Test
    public void compressTestCase14() {
        assertEquals("a4f", Solution.getCompressedString("affff"));
    }
    
    @Test
    public void compressTestCase12211() {
        assertEquals("a2b2aca", Solution.getCompressedString("abbaaca"));
    }

    static class Solution {

        public static String getCompressedString(String input) {
            if (input == null) {
                return null;
            }
            final StringBuffer stringBuffer = new StringBuffer();
            final CompressionSink sink = new CompressionSink(
                (l, c) -> stringBuffer.append(l == 1 ? "" + c : "" + l + c)
            );
            for (final char c : input.toCharArray()) {
                sink.accumulate(c);
            }
            sink.flush();
            return stringBuffer.toString();
        }

        static class CompressionSink {

            public CompressionSink(final BiConsumer<Integer, Character> drain) {
                this.drain = drain;
            }

            final BiConsumer<Integer, Character> drain;

            int runLength;
            Character currentChar;

            void accumulate(final char c) {
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
                    runLength = 0;
                }
            }
        }
    }
}