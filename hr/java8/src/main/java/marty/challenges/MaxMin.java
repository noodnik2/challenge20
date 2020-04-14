package marty.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaxMin {

    public static void main(String[] args) {
        final int ten9th = 1000000000;
        System.out.printf("10^9(%s)\n", ten9th);
        System.out.printf("maxInt(%s)\n", Integer.MAX_VALUE);

        final int result = reworkedMaxMin(
            3,
            new int[] {
                100,
                200,
                300,
                350,
                400,
                401,
                402
            }
        );
        System.out.printf("result(%s)\n", result);
    }

    static int reworkedMaxMin(int k, int[] arr) {

        final int[] newArr = arr.clone();
        Arrays.sort(newArr);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i <= newArr.length - k; i++) {
            if (min > newArr[i + k - 1] - newArr[i]) {
                min = newArr[i + k - 1] - newArr[i];
            }
        }
        return min;
    }

    // Complete the maxMin function below.
    static int maxMinSubmitted(int k, int[] arr) {

        final List<Integer> sortedArrayList = (
            Arrays
            .stream(arr)
            .boxed()
            .sorted()
            .collect(Collectors.toList())
        );

        int minUnfairness = 0;
        for (int i = 0; i < arr.length - k; i++) {
            final int unfairness = (
                sortedArrayList.get(i + k - 1)
              - sortedArrayList.get(i)
            );
            if (unfairness < minUnfairness) {
                minUnfairness = unfairness;
            }
        }

        return minUnfairness;
    }

}
