package noodnik.agari;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

/**
 *  Given two positive integers start and end.
 *  The task is to print all Prime numbers in an Interval.
 */
public class Primes {

    public static final int INTERVAL_MIN_PRINTING_NUMBER = 60;
    public static final int INTERVAL_MAX_PRINTING_NUMBER = 1000;

    public static final int INTERVAL_MIN_TIMING_NUMBER = 60;
    public static final int INTERVAL_MAX_TIMING_NUMBER = 1000;

    public static final int ALL_MAX_PRINTING_NUMBER = 100;
    public static final int ALL_MAX_TIMING_NUMBER = 1000;

    @Test
    public void printAllPrimesUsingBruteForce() {
        printAllPrimes(new BruteForceAllPrimeFinder(new WikiPrimalitySinglePrimeFinder()));
    }

    @Test
    public void printAllPrimesUsingRecursiveBruteForce() {
        printAllPrimes(new BruteForceRecursiveAllPrimeFinder());
    }

    @Test
    public void printAllPrimesUsingSieveOfEratosthenes() {
        printAllPrimes(new SieveOfEratosthenesAllPrimeFinder());
    }

    @Test
    public void printIntervalPrimesUsingBruteForce() {
        printIntervalPrimes(new BruteForceIntervalPrimeFinder());
    }

    @Test
    public void printIntervalPrimesUsingDelegatedBruteForceAllFinder() {
        printIntervalPrimes(
            new DelegatedIntervalPrimeFinder(new BruteForceRecursiveAllPrimeFinder())
        );
    }

    @Test
    public void testAllPrimesFinders() {
        final AllPrimeFinder[] finders = {
            new BruteForceAllPrimeFinder(new WikiPrimalitySinglePrimeFinder()),
            new BruteForceRecursiveAllPrimeFinder(),
            new SieveOfEratosthenesAllPrimeFinder()
        };
        printPrimeFinderRuntimes(finders, assertAllPrimeFinderResultsAndGetRuntimes(finders));
    }

    @Test
    public void testIntervalPrimesFinders() {
        final IntervalPrimeFinder[] finders = {
            new BruteForceIntervalPrimeFinder(),
            new DelegatedIntervalPrimeFinder(new SieveOfEratosthenesAllPrimeFinder())
        };
        printPrimeFinderRuntimes(finders, assertIntervalPrimeFinderResultsAndGetRuntimes(finders));
    }

    interface PrimeFinder {
        // marker interface for a prime finder
    }

    interface AllPrimeFinder extends PrimeFinder {
        /**
         * @param maxNumber maximum number to consider for primes
         * @return list of primes found less than or equal to {@code maxNumber}
         */
        List<Integer> findPrimes(int maxNumber);
    }

    static class SieveOfEratosthenesAllPrimeFinder implements AllPrimeFinder {

        /**
         *  To find all the prime numbers less than or equal to a given integer n by Eratosthenes' method:
         *
         *  <ol>
         *  <li>Create a list of consecutive integers from 2 through n: (2, 3, 4, ..., n).</li>
         *  <li>Initially, let p equal 2, the smallest prime number.</li>
         *  <li>Enumerate the multiples of p by counting in increments of p from 2p to n,
         *      and mark them in the list (these will be 2p, 3p, 4p, ...; the p itself
         *      should not be marked).</li>
         *  <li>Find the smallest number in the list greater than p that is not marked.
         *      If there was no such number, stop. Otherwise, let p now equal this new
         *      number (which is the next prime), and repeat from step 3.</li>
         *  <li>When the algorithm terminates, the numbers remaining not marked in
         *      the list are all the primes below n.</li>
         *  </ol>
         */
        public List<Integer> findPrimes(int maxNumber) {
            final boolean[] marks = new boolean[maxNumber + 1];
            int p = 2;
            while(true) {
                for (int n = 2; n * p <= maxNumber; n++) {
                    marks[n * p] = true;
                }
                int nextp = p + 1;
                while(nextp <= maxNumber && marks[nextp]) {
                    nextp++;
                }
                if (nextp > maxNumber) {
                    break;
                }
                p = nextp;
            }
            final List<Integer> primes = new ArrayList<>();
            for (int n = 2; n <= maxNumber; n++) {
                if (!marks[n]) {
                    primes.add(n);
                }
            }
            return primes;
        }

    }

    interface SinglePrimeFinder extends PrimeFinder {
        /**
         *  @param n number to test for being prime
         *  @return {@code true} iff {@code n} is prime
         */
        boolean isPrime(int n);
    }

    static class WikiPrimalitySinglePrimeFinder implements SinglePrimeFinder {

        /**
         *  @see <a href="https://en.wikipedia.org/wiki/Primality_test">Primality Test</a>
         */
        public boolean isPrime(final int n) {
            if (n <= 3) {
                return n > 1;
            }
            if (n % 2 == 0 || n % 3 == 0) {
                return false;
            }
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) {
                    return false;
                }
            }
            return true;
        }

    }

    static class BruteForceAllPrimeFinder implements AllPrimeFinder {

        private final SinglePrimeFinder singlePrimeFinder;

        public BruteForceAllPrimeFinder(final SinglePrimeFinder singlePrimeFinder) {
            this.singlePrimeFinder = singlePrimeFinder;
        }

        /**
         *  Calls the supplied {@link SinglePrimeFinder} for each number
         *  between 1 and {@code maxNumber}, returning all which evaluate
         *  to being prime numbers.
         */
        public List<Integer> findPrimes(int maxNumber) {
            final List<Integer> primes = new ArrayList<>();
            for (int n = 1; n <= maxNumber; n++) {
                if (singlePrimeFinder.isPrime(n)) {
                    primes.add(n);
                }
            }
            return primes;
        }

    }

    static class BruteForceRecursiveAllPrimeFinder implements AllPrimeFinder {

        /**
         *  Brute force "divide and conquer" approach.  Recursively calls self
         *  to obtain a list of all prime values from 1 to one half of {@code maxNumber},
         *  and uses "trial division" to check if {@code maxNumber} is prime.
         */
        public List<Integer> findPrimes(final int maxNumber) {

            if (maxNumber < 2) {
                return emptyList();
            }

            if (maxNumber == 2) {
                return singletonList(2);
            }

            final int halfMaxNumber = maxNumber / 2;
            final List<Integer> halfPrimes = findPrimes(halfMaxNumber);
            final List<Integer> fullPrimes = new LinkedList<>(halfPrimes);
            for (int nextNumber = halfMaxNumber; nextNumber <= maxNumber; nextNumber++) {
                if (nextNumber == 1) {
                    continue;
                }
                boolean isPrime = true;
                for (final int prime : halfPrimes) {
                    if (nextNumber % prime == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    fullPrimes.add(nextNumber);
                }
            }
            return fullPrimes;
        }

    }

    private void printAllPrimes(final AllPrimeFinder primeFinder) {
        for (int n = 0; n < ALL_MAX_PRINTING_NUMBER; n++) {
            printPrimes(String.valueOf(n), primeFinder.findPrimes(n));
        }
    }

    interface IntervalPrimeFinder extends PrimeFinder {
        /**
         *  @param low low end of range to be tested (inclusive)
         *  @param high high end of range to be tested (inclusive)
         *  @return list of prime numbers within the given range
         */
        List<Integer> findIntervalPrimes(int low, int high);
    }

    static class BruteForceIntervalPrimeFinder implements IntervalPrimeFinder {

        public List<Integer> findIntervalPrimes(final int lowRange, final int highRange) {
            int low = Math.max(lowRange, 2);
            final List<Integer> primeList = new ArrayList<>();
            while(low <= highRange) {
                boolean isPrime = true;
                for (int number = 2; number < low / 2; number++) {
                    if (low % number == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    primeList.add(low);
                }
                low++;
            }
            return primeList;
        }
    }

    static class DelegatedIntervalPrimeFinder implements IntervalPrimeFinder {

        final AllPrimeFinder allPrimeFinder;

        DelegatedIntervalPrimeFinder(final AllPrimeFinder allPrimeFinder) {
            this.allPrimeFinder = allPrimeFinder;
        }

        public List<Integer> findIntervalPrimes(int low, int high) {
            final List<Integer> primes = allPrimeFinder.findPrimes(high);
            primes.removeIf(n -> n < low);
            return primes;
        }
    }

    private long[] assertAllPrimeFinderResultsAndGetRuntimes(final AllPrimeFinder[] finders) {
        final long[] runtimes = new long[finders.length];
        for (int n = 0; n < ALL_MAX_TIMING_NUMBER; n++) {
            List<Integer> previousTestResult = null;
            for (int i = 0; i < finders.length; i++) {
                final long startTime = System.currentTimeMillis();
                final List<Integer> testResult = finders[i].findPrimes(n);
                runtimes[i] += System.currentTimeMillis() - startTime;
                if (previousTestResult != null) {
                    assertEquals(finders[i].getClass().getName(), previousTestResult, testResult);
                }
                previousTestResult = testResult;
            }
        }
        return runtimes;
    }

    private long[] assertIntervalPrimeFinderResultsAndGetRuntimes(final IntervalPrimeFinder[] finders) {
        final long[] runtimes = new long[finders.length];
        for (int n = INTERVAL_MIN_TIMING_NUMBER; n < INTERVAL_MAX_TIMING_NUMBER; n++) {
            List<Integer> previousTestResult = null;
            for (int i = 0; i < finders.length; i++) {
                final long startTime = System.currentTimeMillis();
                final List<Integer> testResult = finders[i].findIntervalPrimes(n, INTERVAL_MAX_TIMING_NUMBER);
                runtimes[i] += System.currentTimeMillis() - startTime;
                if (previousTestResult != null) {
                    assertEquals(finders[i].getClass().getName(), previousTestResult, testResult);
                }
                previousTestResult = testResult;
            }
        }
        return runtimes;
    }

    private void printIntervalPrimes(final IntervalPrimeFinder primeFinder) {
        for (int n = INTERVAL_MIN_PRINTING_NUMBER; n < INTERVAL_MAX_PRINTING_NUMBER; n++) {
            printPrimes(
                format("%s-%s", n, INTERVAL_MAX_PRINTING_NUMBER),
                primeFinder.findIntervalPrimes(n, INTERVAL_MAX_PRINTING_NUMBER)
            );
        }
    }

    private void printPrimes(String id, List<Integer> primes) {
        log("%s[%s] %s", id, primes.size(), primes);
    }

    private void printPrimeFinderRuntimes(final PrimeFinder[] finders, final long[] runtimes) {
        for (int i = 0; i < finders.length; i++) {
            log("finder(%s) took(%sms)", finders[i].getClass().getSimpleName(), runtimes[i]);
        }
    }

}
