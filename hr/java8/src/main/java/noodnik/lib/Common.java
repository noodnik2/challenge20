package noodnik.lib;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Common {

    public static void log(String format, Object... values) {
        System.out.printf(format + "\n", values);
    }

    public static int randomInt(int from, int to) {
        return from + (int) (Math.random() * (to - from + 1));
    }

    public static long randomLong(long from, long to) {
        return from + (long) (Math.random() * (to - from + 1));
    }

    public static int[] randomIntArray(int size, int from, int to) {
        final int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = randomInt(from, to);
        }
        return a;
    }

    public static long[] randomLongArray(int size, long from, long to) {
        return filledLongArray(size, () -> randomLong(from, to));
    }

    public static int[] filledIntArray(int size, Supplier<Integer> intSupplier) {
        final int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = intSupplier.get();
        }
        return a;
    }

    public static long[] filledLongArray(int size, Supplier<Long> longSupplier) {
        final long[] a = new long[size];
        for (int i = 0; i < size; i++) {
            a[i] = longSupplier.get();
        }
        return a;
    }

    public static List<Integer> listOf(int... testDatum) {
        return Arrays.stream(testDatum).boxed().collect(Collectors.toList());
    }

    public static List<Long> listOf(long... testDatum) {
        return Arrays.stream(testDatum).boxed().collect(Collectors.toList());
    }

    public static long factorial(final long n) {
        return (
            LongStream
            .rangeClosed(1, n)
            .reduce(1, (long x, long y) -> x * y)
        );
    }

}
