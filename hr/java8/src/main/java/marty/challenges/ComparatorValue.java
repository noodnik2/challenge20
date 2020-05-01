//package marty.challenges;
//
//import marty.lib.Common;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//import static marty.lib.Common.randomIntArray;
//import static org.junit.Assert.assertEquals;
//
//public class ComparatorValue {
//
//    static int[][] testCase0 = { { 1 }, { 1 } };
//    static int[][] testCase1 = { { 1, 2, 3 }, { 1 } };
//    static int[][] testCase2 = { { 1, 2, 3 }, { 1, 2, 3 } };
//
//    static int[][] testCase3 = { { 1 }, { 1 } };
//    static int[][] testCase4 = { { 1, 2, 3 }, { 1 } };
//    static int[][] testCase5 = { { 1, 2, 3 }, { 1, 2, 3 } };
//
//    public static void main(String[] args) {
//
//        System.out.println("basic: reworked");
//        assertEquals(0, testRunner(reworkedFn, testCase0, 1));
//        assertEquals(1, testRunner(reworkedFn, testCase1, 1));
//        assertEquals(2, testRunner(reworkedFn, testCase2, 1));
//
//        assertEquals(0, testRunner(reworkedFn, testCase0, 2));
//        assertEquals(0, testRunner(reworkedFn, testCase1, 2));
//        assertEquals(0, testRunner(reworkedFn, testCase2, 2));
//
//        System.out.println("basic: original");
//        assertEquals(0, testRunner(submittedFn, testCase0, 1));
//        assertEquals(1, testRunner(submittedFn, testCase1, 1));
//        assertEquals(2, testRunner(submittedFn, testCase2, 1));
//
//        assertEquals(0, testRunner(submittedFn, testCase0, 2));
//        assertEquals(0, testRunner(submittedFn, testCase1, 2));
//        assertEquals(0, testRunner(submittedFn, testCase2, 2));
//
//        final int[][] largeTestCase = {
//            randomIntArray(100000, 0, 100),
//            randomIntArray(100000, 0, 100)
//        };
//        final int randomD = Common.randomInt(0, 100);
//
//        System.out.println("large: submitted");
//        final int originalResult = testRunner(submittedFn, largeTestCase, randomD);
//        System.out.printf("submittedResult(%s)\n", originalResult);
//
//        System.out.println("large: reworked");
//        final int submittedResult = testRunner(reworkedFn, largeTestCase, randomD);
//        System.out.printf("reworkedResult(%s)\n", submittedResult);
//
//    }
//
//    interface ComparatorValueFn {
//        int getComparatorValue(List<Integer> a, List<Integer> b, int d);
//    }
//
//    static ComparatorValueFn reworkedFn = ComparatorValue::comparatorValueLaterRework;
//    static ComparatorValueFn submittedFn = ComparatorValue::comparatorValueSubmitted;
//
//    static int testRunner(ComparatorValueFn testFn, int[][] testData, int d) {
//        final long startTimeMillis = System.currentTimeMillis();
//        final int[] a = testData[0];
//        final int[] b = testData[1];
//        final int comparatorValue = testFn.getComparatorValue(Common.listOf(a), Common.listOf(b), d);
//        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
//        System.out.printf("a[%s], b[%s] took(%sms)\n", a.length, b.length, elapsedTimeMillis);
//        return comparatorValue;
//    }
//
//    static int comparatorValueLaterRework(List<Integer> a, List<Integer> b, int d) {
//
//        // want to "disqualify" each element of a (aval)
//        // means find elements in b most different from aval
//        // want to find an integer in b "most different from aval"
//
//        // for each a
//        //   for each b
//        //     if |aval - bval| > d increment count
//        // return count
//
//        final List<Integer> sortedA = new ArrayList<>(a);
//        sortedA.sort(Comparator.naturalOrder());
//
//        final List<Integer> sortedB = new ArrayList<>(b);
//        sortedB.sort(Comparator.naturalOrder());
//
//        // if neither |a[0] - b[_n-1]| > d nor |a[_n-1] - b[0]| > d return 0
//
//        int returnValue = 0;
//
//        // count avals on left while |aval-b[0]| > d
//        final int maxBval = sortedB.get(sortedB.size() - 1);
//        for (int i = 0; i < sortedA.size(); i++) {
//            if (Math.abs(sortedA.get(i) - maxBval) <= d) {
//                break;
//            }
//            returnValue++;
//        }
//
//        // count avals on right while |aval-b[_n-1]| > d
//        final int minBval = sortedB.get(0);
//        for (int i = sortedA.size() - 1; i >= 0; i--) {
//            if (Math.abs(sortedA.get(i) - minBval) <= d) {
//                break;
//            }
//            returnValue++;
//        }
//
//        return returnValue;
//    }
//
//    static int comparatorValueSubmitted(List<Integer> a, List<Integer> b, int d) {
//
//        // want to "disqualify" each element of a (aval)
//        // means find elements in b most different from aval
//        // want to find an integer in b "most different from aval"
//
//        // for each a
//        //   for each b
//        //     if |aval - bval| > d increment count
//        // return count
//
//        final List<Integer> sortedA = new ArrayList<>(a);
//        sortedA.sort(Comparator.naturalOrder());
//
//        final List<Integer> sortedB = new ArrayList<>(b);
//        sortedB.sort(Comparator.reverseOrder());
//
//        int returnValue = 0;
//        for (final int aval : sortedA) {
//            boolean foundDifferenceGreaterThanD = false;
//            for (final int bval : sortedB) {
//                final int badiff = Math.abs(aval - bval);
//                if (badiff > d) {
//                    foundDifferenceGreaterThanD = true;
//                    break;
//                }
//            }
//            if (foundDifferenceGreaterThanD) {
//                returnValue++;
//            }
//        }
//
//        return returnValue;
//    }
//
//}
