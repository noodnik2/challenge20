import static java.lang.String.format;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AggregationProcessorTests {
    
    private static final String SMALL_DATA_TXT_FILENAME = "./small-data.txt";

    @Test
    public void smallTestCase() {
        // test with provided sample dataset, using 1K memory
        final Map<String, IterativeSolution.AggResult> expectedResultsMap = new HashMap<>();
        expectedResultsMap.put("key1", new IterativeSolution.AggResult(2, "hello"));
        expectedResultsMap.put("key2", new IterativeSolution.AggResult(1, "zzz"));
        expectedResultsMap.put("key3", new IterativeSolution.AggResult(1, "world"));
        assertCorrectResult(
            new TestDataDescriptor(new File(SMALL_DATA_TXT_FILENAME), expectedResultsMap), 
            1024
        );
    }
    
    @Test
    public void largeTestCase() throws IOException {        
        // test with 1,000 keys each with between 1 to 1,000 value(s),
        // using enough memory to aggregate values for at least 10 keys
        assertCorrectResult(
            newTestData(1000, 1000), 
            10 * IterativeSolution.IterativeAggregationProcessor.CACHED_ITEM_SIZE
        );
    }
    
    @Test
    public void lowMemoryConfigurationTestCase() {
        try {
            new IterativeSolution.IterativeAggregationProcessor(
                SMALL_DATA_TXT_FILENAME, 
                (key, aggResult) -> {}, 
                IterativeSolution.IterativeAggregationProcessor.CACHED_ITEM_SIZE - 1
            );
            throw new RuntimeException("expected exception was not thrown");
        } catch(final Exception e) {
            assertEquals("unexpected message", "invalid memory configuration", e.getMessage());
        }
    }

    private void assertCorrectResult(
        final TestDataDescriptor testDataDescriptor,
        final int testCaseCacheSizeBytes
    ) {

        final Map<String, IterativeSolution.AggResult> actualResultsMap = new HashMap<>();
        final Runnable testAggregationProcessor = new IterativeSolution.IterativeAggregationProcessor(
            testDataDescriptor.testDataFile.getAbsolutePath(), 
            (key, aggResult) -> assertNull("duplicate key received", actualResultsMap.put(key,  aggResult)), 
            testCaseCacheSizeBytes
        );
        
        log("running aggregation processor...");
        testAggregationProcessor.run();
        
//        log("expectedResults(%s)", testDataDescriptor.resultsMap);
//        log("actualResultsMap(%s)", actualResultsMap);
        log("expectedSize(%s), actualSize(%s)", testDataDescriptor.resultsMap.size(), actualResultsMap.size());
        log("expectedCount(%s), actualCount(%s)", countResults(testDataDescriptor.resultsMap.values()), countResults(actualResultsMap.values()));
        assertEquals("actual results differ from expected", testDataDescriptor.resultsMap, actualResultsMap);
    }

    private TestDataDescriptor newTestData(final int nKeys, final int nValuesMax) throws IOException {
        final File testDataFile = File.createTempFile("large-data.txt", null);
        testDataFile.deleteOnExit();
        final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testDataFile));
        final Map<String, IterativeSolution.AggResult> resultsMap = new HashMap<>();
        log("building input file with(%s) key(s) each with random(1..%s) value(s)", nKeys, nValuesMax);
        for (int keyNumber = 1; keyNumber <= nKeys; keyNumber++) {
            final String key = "key" + keyNumber; 
            final int nValues = 1 + (int) ((Math.random() * nValuesMax) + 0.5d);
            String maxValue = null;
            for (int valueNumber = 1; valueNumber <= nValues; valueNumber++) {
                final String value = "value" + valueNumber;
                if (maxValue == null || value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
                bufferedWriter.write(key + " " + value);
                bufferedWriter.newLine();
            }
            resultsMap.put(key, new IterativeSolution.AggResult(nValues, maxValue));
        }
        bufferedWriter.close();        
        return new TestDataDescriptor(testDataFile, resultsMap);
    }    
    
    private static class TestDataDescriptor {
        File testDataFile;
        Map<String, IterativeSolution.AggResult> resultsMap;
        TestDataDescriptor(File testDataFile, Map<String, IterativeSolution.AggResult> resultsMap) {
            this.testDataFile = testDataFile;
            this.resultsMap = resultsMap;
        }
    }

    private long countResults(Collection<IterativeSolution.AggResult> aggResults) {
        return aggResults.stream().map(r -> r.count).reduce(0L, Long::sum);
    }
    
    private void assertNull(String string, Object o) {
        if (o != null) {
            throw new RuntimeException(format("FAILED: value(%s) was not null as expected", o));
        }
    }

    private <K, V> void assertEquals(String message, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new RuntimeException(format("FAILED: %s; expected(%s) != actual(%s)", message, expected, actual));
        }        
    }

    private static void log(String format, Object... args) {
        System.out.println(format(format, args));
    }
    
    @Retention(RUNTIME)
    @interface Test {
        
    }
    
    public static void main(String[] args) throws Exception {
        final AggregationProcessorTests aggregationProcessorTests = new AggregationProcessorTests();
        for (Method m : aggregationProcessorTests.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                log("--->  running: %s", m.getName());
                m.invoke(aggregationProcessorTests);
                log("<---  passed: %s", m.getName());
            }
        }
    }

}
