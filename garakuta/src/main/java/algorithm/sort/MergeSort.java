package algorithm.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MergeSort {

    public static void main(final String[] args) {
        final int size = 100;
        final List<Integer> list = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        final int[] value = list.stream().mapToInt(i -> i).toArray();
        System.out.println(Arrays.toString(value));
        sort(value);
        System.out.println(Arrays.toString(value));
        if (Arrays.equals(value, IntStream.range(0, size).toArray()) == false) {
            throw new AssertionError();
        }
    }

    static void sort(final int[] value) {
        final int[] temp = new int[(value.length + 1) / 2];
        sort(value, 0, value.length, temp);
    }

    static void sort(final int[] value, final int from, final int to, final int[] temp) {
        if (from + 1 < to) {
            final int middle = (from + to) / 2;
            if (from < middle) {
                sort(value, from, middle, temp);
                sort(value, middle, to, temp);
                for (int i = 0, j = from; j < middle; i++, j++) {
                    temp[i] = value[j];
                }
                int i = 0;
                int j = middle;
                int k = from;
                while (i < (middle - from) && j < to && k < to) {
                    if (temp[i] < value[j]) {
                        value[k] = temp[i++];
                    } else {
                        value[k] = value[j++];
                    }
                    k++;
                }
                while (i < (middle - from)) {
                    value[k++] = temp[i++];
                }
            }
        }
    }
}
