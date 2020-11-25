package noodnik.nuxeo;

import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCompressor {

    @Test
    public void testSinkStreamSolution() {
        runTests(new SinkStreamSolution());
    }

    private void runTests(final Solution solution) {
        assertNull("nullTestCase", solution.getCompressedString(null));
        assertEquals("emptyTestCase", "", solution.getCompressedString(""));
        assertEquals("compressTestCase111", "abc", solution.getCompressedString("abc"));
        assertEquals("compressTestCase321", "3a2bc", solution.getCompressedString("aaabbc"));
        assertEquals("compressTestCase11113", "abcde3f", solution.getCompressedString("abcdefff"));
        assertEquals("compressTestCase3", "3a", solution.getCompressedString("aaa"));
        assertEquals("compressTestCase14", "a4f", solution.getCompressedString("affff"));
        assertEquals("compressTestCase12211", "a2b2aca", solution.getCompressedString("abbaaca"));
    }

    interface Solution {
        String getCompressedString(String input);
    }

    static class CompressionBuffer implements Supplier<String> {

        private final StringBuilder stringBuilder = new StringBuilder();
        private final BiConsumer<Integer, Character> drain = (
            (l, c) -> stringBuilder.append(l == 1 ? "" + c : "" + l + c)
        );

        private int runLength;
        private Character currentChar;

        public void accumulate(final char c) {
            if (currentChar == null) {
                runLength = 1;
                currentChar = c;
                return;
            }
            if (currentChar == c) {
                runLength++;
                return;
            }
            flush();
        }

        public void flush() {
            if (runLength > 0) {
                drain.accept(runLength,  currentChar);
                runLength = 0;
            }
        }

        public String get() {
            flush();
            return stringBuilder.toString();
        }

    }

    static class SinkStreamSolution implements Solution {

        public String getCompressedString(final String input) {
            if (input == null) {
                return null;
            }
            return (
                input
                .chars()
                .mapToObj(ci -> (char) ci)
                .collect(
                    Collector.of(
                        CompressionBuffer::new,
                        CompressionBuffer::accumulate,
                        (sink1, sink2) -> sink2, // only used in single-threaded collectors like this one
                        CompressionBuffer::get
                    )
                )
            );
        }
    }

}