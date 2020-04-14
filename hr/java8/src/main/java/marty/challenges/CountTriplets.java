package marty.challenges;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static marty.lib.Common.factorial;
import static marty.lib.Common.filledLongArray;
import static marty.lib.Common.listOf;
import static marty.lib.Common.log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CountTriplets {

    @Test
    public void fifthApproach() {
        runTests(new FifthApproach());
    }

    @Test
    public void fourthApproach() {
        runTests(new FourthApproach());
    }

    @Test
    public void thirdApproach() {
        runTests(new ThirdApproach());
    }

    @Test
    public void secondApproach() {
        runTests(new SecondApproach());
    }

    @Test
    public void firstApproach() {
        runTests(new FirstApproach());
    }

    static class FifthApproach implements TripletCounter {


        @Override
        public long countTriplets(List<Long> arr, long r) {

            // my favorite approach from HR Discussion...
            //
            //    v2 = defaultdict(int)
            //    v3 = defaultdict(int)
            //    count = 0
            //    for v in arr:
            //        count += v3[v]
            //        v3[v*r] += v2[v]
            //        v2[v*r] += 1
            //
            //    return count

            final Map<Long, Long> v2 = new HashMap<>();
            final Map<Long, Long> v3 = new HashMap<>();

            long count = 0;
            for (final long v : arr) {
                final long v2v = ofNullable(v2.get(v)).orElse(0L);
                final long v3v = ofNullable(v3.get(v)).orElse(0L);
                final long vr = v * r;
                final long v2vr = ofNullable(v2.get(vr)).orElse(0L);
                final long v3vr = ofNullable(v3.get(vr)).orElse(0L);
                count += v3v;
                v2.put(vr, v2vr + 1L);
                v3.put(vr, v3vr + v2v);
            }

            return count;
        }

    }

    static class FourthApproach implements TripletCounter {

        @Override
        public long countTriplets(List<Long> array, long r) {

            final Map<Long, Collection<Integer>> map = new HashMap<>();
            long totalCount = 0;

            for (int index = 0; index < array.size(); index++) {

                long currentVal = array.get(index);
                final Collection<Integer> priorValIndices = map.get(currentVal);
                if (priorValIndices != null) {
                    final Collection<Integer> priorPriorValIndices = map.get(
                        array.get(priorValIndices.iterator().next())
                    );
                    if (priorPriorValIndices != null) {
                        for (final int priorValIndex : priorValIndices) {
                            for (final int priorPriorValIndex : priorPriorValIndices) {
                                if (priorPriorValIndex < priorValIndex) {
                                    totalCount++;
                                }
                            }
                        }
                    }
                }

                // map entry contains indices of prior values to key
                map.computeIfAbsent(currentVal * r, k -> new HashSet<>()).add(index);
            }

            return totalCount;
        }

    }

    static class ThirdApproach implements TripletCounter {

        @Override
        public long countTriplets(List<Long> array, long r) {
            final Map<Long, Collection<Integer>> map = new HashMap<>();
            long totalCount = 0;
            for (int index = 0; index < array.size(); index++) {

                long currentVal = array.get(index);
                final long nextVal = currentVal * r;

                final Collection<Integer> priorValIndices = map.get(currentVal);
                if (priorValIndices != null) {
                    final long priorVal = array.get(priorValIndices.iterator().next());
                    final Collection<Integer> priorPriorValIndices = map.get(priorVal);
                    if (priorPriorValIndices != null) {
                        for (final int priorValIndex : priorValIndices) {
                            for (final int priorPriorValIndex : priorPriorValIndices) {
                                if (priorPriorValIndex < priorValIndex) {
                                    totalCount++;
                                }
                            }
                        }
                    }
                }

                // map entry contains indices of prior values to key
                final Collection<Integer> currentValRefs0 = map.get(nextVal);
                final Collection<Integer> currentValRefs;
                if (currentValRefs0 == null) {
                    currentValRefs = new HashSet<>();
                    map.put(nextVal, currentValRefs);
                } else {
                    currentValRefs = currentValRefs0;
                }
                currentValRefs.add(index);

                log(
                    "map(%s), totalCount(%s), currentVal(%s), nextVal(%s)",
                    map,
                    totalCount,
                    currentVal,
                    nextVal
                );
            }

            return totalCount;
        }

    }

    static class SecondApproach implements TripletCounter {

        @Override
        public long countTriplets(List<Long> array, long r) {
            final Map<Long, Long> map = new HashMap<>();
            final Map<Long, Long> counts = new HashMap<>();
            long totalCount = 0;
            for (int index = 0; index < array.size(); index++) {
                long currentVal = array.get(index);
                final long nextVal = currentVal * r;
                final Long currentValCount = counts.get(currentVal);
                counts.put(currentVal, currentValCount == null ? 1 : currentValCount + 1);
                map.put(nextVal, currentVal);
                final Long priorVal = map.get(currentVal);
                final Long priorPriorVal = map.get(priorVal);
                if (priorPriorVal != null) {
                    totalCount += counts.get(priorVal) * counts.get(priorPriorVal);
                }
                log(
                    "map(%s), counts(%s), count(%s), v(%s), vr(%s), vref(%s), vrefvref(%s)",
                    map,
                    counts,
                    totalCount,
                    currentVal,
                    nextVal,
                    priorVal,
                    priorPriorVal
                );
            }
            return totalCount;
        }

    }

    static class FirstApproach implements TripletCounter {

        public long countTriplets(List<Long> arr, long r) {

            if (r < 1) {
                return 0;
            }

            final Map<Long, Long> freqs = arr.stream().collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting())
            );

            return arr.stream().distinct().mapToLong(e -> tripletCountFor(e, freqs, r)).sum();
        }

        private long tripletCountFor(long e, Map<Long, Long> freqs, long r) {

            final long firstValueFreq = freqs.get(e);

            if (r == 1) {
                if (firstValueFreq < 3) {
                    return 0;
                }
                // binomial coefficient: freq choose r => freq! / 3! * (freq - 3)!
                return factorial(firstValueFreq) / (factorial(3) * factorial(firstValueFreq - 3));
            }

            final Long secondValueFreq = freqs.get(e * r);
            final Long thirdValueFreq = freqs.get(e * r * r);
            if (secondValueFreq == null || thirdValueFreq == null) {
                return 0L;
            }
            return firstValueFreq * secondValueFreq * thirdValueFreq;
        }

    }

    interface TripletCounter {
        /**
         * arr: an array of integers
         * r: an integer, the common ratio
         * @param arr an array of integers
         * @param r an integer, the common ratio
         * @return number of triplets forming a geometric progression
         * for a given {@code r} as an integer.
         */
        long countTriplets(List<Long> arr, long r);
    }

    static class TripletCounterTester {
        final TripletCounter tripletCounter;
        TripletCounterTester(TripletCounter tripletCounter) {
            this.tripletCounter = tripletCounter;
        }
        void testCase(long e, long r, long... array) {
            final List<Long> arr = listOf(array);
            log("testCase(e(%s), r(%s), array(%s))", e, r, arr);
            final long startTimeMillis = System.currentTimeMillis();
            assertEquals("incorrect result", e, tripletCounter.countTriplets(arr, r));
            final long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
            assertTrue("too slow (" + elapsedMillis + "ms > 5000ms)", elapsedMillis < 5000L);
        }
    }

    public static void runTests(final TripletCounter tripletCounter) {

        final TripletCounterTester t = new TripletCounterTester(tripletCounter);

        // simple
        t.testCase(2, 2, 1, 2, 2, 4);
        t.testCase(6, 3, 1, 3, 9, 9, 27, 81);
        t.testCase(4, 5, 1, 5, 5, 25, 125);
        t.testCase(6, 5, 1, 1, 5, 5, 25, 125);
        t.testCase(2, 5, 1, 5, 25, 125);
        t.testCase(3, 10, 1000, 10000, 100000, 1000000, 10000000);
        t.testCase(3, 100, 1, 100, 10000, 1000000, 100000000);
        t.testCase(3, 2, 1, 2, 1, 2, 4);
        t.testCase(4, 2, 1, 2, 4, 1, 2, 4);
        t.testCase(9, 2, 1, 2, 2, 4, 1, 2, 1, 2, 4);
        t.testCase(0, 5,125, 125, 125, 25, 5);

        // r == 1
        t.testCase(10, 1, 1, 1, 1, 1, 1);
        t.testCase(4, 1, 1, 1, 1, 1);
        t.testCase(1, 1, 1, 1, 1);
        t.testCase(0, 1, 1, 1);
        t.testCase(0, 1, 1);
        t.testCase(0, 1, 1, 2, 3, 4);
        t.testCase(0, 1, 1, 2, 3);
        t.testCase(1, 1, 1, 2, 2, 3, 2);
        t.testCase(4, 1, 1, 2, 2, 3, 2, 2, 1);
        t.testCase(0, 1, 1, 2);
        t.testCase(1, 1, 1, 2, 4, 3, 3, 3, 1, 4);
        t.testCase(0, 1, 1, 2, 2);

        t.testCase(1, 1, 1000000,1000000000, 1000000000, 1000000000);

        // time efficiency
        t.testCase(561375500, 1, filledLongArray(1500, () -> 1L));
        t.testCase(166661666700000L, 1, filledLongArray(100000, () -> 1237L));

    }

}
