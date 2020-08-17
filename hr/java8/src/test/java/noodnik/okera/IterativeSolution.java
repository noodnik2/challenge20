package noodnik.okera;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This is an iterative solution that does in-memory aggregation until reaching
 * the maximum size, spilling the remaining entries for subsequent processing
 * in additional iteration(s).
 */
public class IterativeSolution {

    public static final class AggResult {

        long count;
        String max;
        
        public AggResult(long count, String max) {
            this.count = count;
            this.max = max;
        }
        
        public String toString() {
            return String.format("r(%s:%s)", count, max);
        }
        
        public boolean equals(Object other) {
            return (
                other instanceof AggResult 
             && ((AggResult) other).count == this.count 
             && ((AggResult) other).max.equals(this.max)
            ); 
        }

        public int hashCode() {
            return Objects.hash(count, max);
        }
        
    }
    
    public static final class IterativeAggregationProcessor implements Runnable {
        
        private final BiConsumer<String, AggResult> resultSink;
        private final String inputFilename;
        private final Map<String, AggResult> resultsMap;
        private final int maxMapSize;
        
        //
        // The keys must be between 1 and 32 bytes (inclusive) and the value
        // is between 1 and 100 bytes (inclusive).  Java8 has an overhead of
        // approximately 40 characters per String, and uses 2 bytes per character
        // within each String; we must take these into account when reserving
        // memory for keys and values.
        //
        
        private static final int MAX_KEY_SIZE = 104;    // ~max(sizeof(key)): <= 2 * 32 + ~40
        private static final int MAX_VALUE_SIZE = 240;  // ~max(sizeof(AggResult)): `.max`(2 * <= 100 + ~40) + `.count`(8)
        
        public static final int CACHED_ITEM_SIZE = MAX_KEY_SIZE + MAX_VALUE_SIZE;
        
        IterativeAggregationProcessor(
            String inputFilename, 
            BiConsumer<String, AggResult> resultSink, 
            int maxCacheSizeBytes
        ) {
            maxMapSize = maxCacheSizeBytes / CACHED_ITEM_SIZE;
            if (maxMapSize < 1) {
                throw new RuntimeException("invalid memory configuration");
            }            
            resultsMap = new HashMap<String, AggResult>();
            this.inputFilename = inputFilename;
            this.resultSink = resultSink;
        }

        public void run() {

            String fileName = inputFilename;
            while(fileName != null) {
                try {
                    fileName = processNext(fileName);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            
        }

        /**
         *  @param fileName name of the file to process
         *  @return {@code null} if all keys were processed, or name of "spill" file needing subsequent processing
         *  @throws IOException un-handled exception
         */
        private String processNext(final String fileName) throws IOException {
                        
            File spillFile = null;
            BufferedWriter bw = null;
            
            try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    
                resultsMap.clear();
                String line = null;
                while ((line = br.readLine()) != null) {
    
                    // Parse the key value. It should be space separated.
                    if (line.trim().length() == 0) continue;
                    String[] kv = line.split(" ");
                    if (kv.length != 2) {
                        throw new IOException("Invalid data element: " + line);
                    }
    
                    // Either insert the new key/value or update an existing result.
                    AggResult agg = resultsMap.get(kv[0]);
                    if (agg != null) {
                        // Update an existing entry
                        if (agg.max.compareTo(kv[1]) < 0) agg.max = kv[1];
                        agg.count++;
                        continue;
                    }
    
                    if (resultsMap.size() >= maxMapSize) {
                        // spill the line if we've reached the maximum number of entries we can handle
                        if (bw == null) {
                            spillFile = File.createTempFile(inputFilename, null);
                            spillFile.deleteOnExit();
                            bw = new BufferedWriter(new FileWriter(spillFile));
                        }
                        bw.write(line);
                        bw.newLine();
                        continue;
                    }            
                    
                    // Add new key-value to intermediate, in-memory result
                    resultsMap.put(kv[0], new AggResult(1, kv[1]));
                }
                    
            } finally {
                if (bw != null) {
                    bw.close();
                }
            }
            
            // All done with aggregation, output the result for this iteration
            for (final Map.Entry<String, AggResult> it: resultsMap.entrySet()) {
                resultSink.accept(it.getKey(), it.getValue());
            }

            return spillFile == null ? null : spillFile.getAbsolutePath();
        }

    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java SimpleSolution <input-file> <max-memory-size>");
            System.exit(1);
        }

        new IterativeAggregationProcessor(
            args[0], 
            (key, result) -> System.out.println(key + ": " + result.count + ", " + result.max),
            Integer.valueOf(args[1])
        ).run();

    }    

}
