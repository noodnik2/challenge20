# External Aggregation

For this problem we'd like you to implementation a solution that performs
an aggregation computation with support for when the intermediate state may 
not fit into memory. In these cases, the implementaiton should use disk to
spill intermediate results. The input data is stored in a file and consists of
key-value pairs. The program should compute, for each key the number of values
with that key and the value with the highest sort order. For example, if the
input data is:
```
key1 abcd
key2 zzz
key1 hello
key3 world
```

The result should be:
```
key1: 2, hello
key2: 1, zzz
key3: 1, world
```

The input data each key value pair on its own line with a space between the
key and value. The program should output the result to standard out, in the
format in the example. The output can be in any order (the result keys do not
need to be sorted).

The keys must be between 1 and 32 bytes (inclusive) and the value is between
1 and 100 bytes (inclusive). Your solution does not need to validate the input
and can assume this is true.

The program should also accept the maximum amount of memory it can use and
should not go over this value. If there are other configuration values that
are required, please include that.

### What we are looking for
There are a few well known ways to solve this problem and we're focused on
a **clean, robust and correct implementation**. If you need help coming up with the 
algorithm, let us know and we can go over that. **The solution should be written 
as if it was going to be used in production.**

Focus on high level performance characteristics. The solution should run in
O(n) in the case where there is enough memory and no worse than O(nlogn) if
there is not enough memory. Be mindful of things such as the number of passes
over the data and the amount of intermediate IO, not on low level optimizations.

The solution should behave well in the case where most keys are unique as well
as when most keys are duplicates.

Please include a README that describes your solution with any known limitations.
Also describe ways you could improve it if required.

### Reference simple implementation
We've provided an implementation in Java as well as a sample file that does
not respect the memory constraint. This can be used as a starting point. Please
implement the solution in java or c++.

To run the example:
```
# cd to this directory
javac SimpleSolution.java
java SimpleSolution ./small-data.txt
```
