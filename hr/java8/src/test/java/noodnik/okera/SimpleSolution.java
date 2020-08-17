package noodnik.okera;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple solution that does completely in-memory aggregation. Take
 * a look at this to understand the exact computation we are looking for.
 */
class SimpleSolution {
  private static final class AggResult {
    long count;
    String max;
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: java SimpleSolution <input-file>");
      System.exit(1);
    }

    // For each key we've seen, update the current aggregated result. Note that
    // this data structure can grow proportional to the input data and
    // use arbitrarily large amounts of memory.
    Map<String, AggResult> result = new HashMap<String, AggResult>();

    // Loop through the input file line by line.
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    String line = null;
    while ((line = br.readLine()) != null) {
      // Parse the key value. It should be space separated.
      if (line.trim().length() == 0) continue;
      String[] kv = line.split(" ");
      if (kv.length != 2) {
        throw new IOException("Invalid data element: " + line);
      }

      // Either insert the new key/value or update an existing result.
      AggResult agg = result.get(kv[0]);
      if (agg == null) {
        // New key-value, add it to intermediate result.
        agg = new AggResult();
        agg.count = 1;
        agg.max = kv[1];
        result.put(kv[0], agg);
      } else {
        // Update an existing entry.
        if (agg.max.compareTo(kv[1]) < 0) agg.max = kv[1];
        agg.count++;
      }
    }

    // All done with aggregation, output the result.
    for (Map.Entry<String, AggResult> it: result.entrySet()) {
      AggResult val = it.getValue();
      System.out.println(it.getKey() + ": " + val.count + ", " + val.max);
    }
  }
}
