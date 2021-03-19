# External Aggregation

This is an implementation solution that performs aggregation computation 
in iterations, until all of the key/value pairs have been processed.

The approach is simple: the aggregation is done in-memory for as many 
keys as will fit, according to the amount of memory configured in the
constructor of the instance, as follows:

```
IterativeAggregationProcessor(
    String inputFilename,
    BiConsumer<String, AggResult> resultSink,
    int maxCacheSizeBytes
)
```

Where:
1. `inputFilename` is the name of the input file containing key/value pairs to be processed
1. `resultSink` is the receiver of the results obtained from processing
1. `maxCacheSizeBytes` is the number of bytes the in-memory data structure (Map) is allowed to grow while processing the file

The maximum number of entries that can be held by the in-memory data structure is calculated from `maxCacheSizeBytes`;
once that size has been reached, processing of value(s) for key(s) not yet encountered are "spilled"
into temporary file(s), which are later processed in subsequent iteration(s).

### Suggested Improvements

- In order to increase the number of key values that can be aggregated within an iteration, thereby reducing the number of
  iterations required for a given value of `maxCacheSizeBytes`:
    - The size of the `AggResult` data structure could be reduced.  For instance, a `char` (or even a `byte`) array
    could be used instead of a `String` to store the `max` value.
    - Actual key lengths could be used instead of maximum key lengths.
- In cases where processing uses a large number of iterations, it's suspected the majority of time is spent in filesystem I/O.
To better handle these cases:
    - The use of temporary files for storing and later reprocessing "spilled" records could be replaced with faster I/O (such
as to use a faster filesystem)
    - The use of `BufferedReader` and `BufferedWriter` for input and output (i.e. "spilling") could be sped up with the use of
more efficient parsing or writing mechanisms
- Temporary files could be deleted after each iteration which uses them, instead of after all iterations are complete 

### Running

Several run targets are provided.  

A recommended setup procedure is:
1. `cd` to this directory
1. `rm *.class`

To run the submission (for example, with the provided `small-data.txt` file):
1. `javac IterativeSolution.java`
1. `java IterativeSolution ./small-data.txt 700`

To run the tests:
1. `javac IterativeSolution.java`
1. `javac AggregationProcessorTests.java`
1. `java AggregationProcessorTests`

It's recommended to clean up afterwards, e.g.:
1. `rm *.class`

