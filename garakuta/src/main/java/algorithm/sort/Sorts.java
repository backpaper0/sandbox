package algorithm.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sorts {

    public static void main(final String[] args) {
        final Map<Class<?>, SortAlgorithm> list = new LinkedHashMap<>();
        //        list.put(BubbleSort.class, BubbleSort::sort);
        list.put(HeapSort.class, HeapSort::sort);
        list.put(MergeSort.class, MergeSort::sort);
        list.put(QuickSort.class, QuickSort::sort);
        final int[] originalValue = value(10_000_000);
        for (final Entry<Class<?>, SortAlgorithm> entry : list.entrySet()) {
            final String name = entry.getKey().getSimpleName();
            final SortAlgorithm sa = entry.getValue();
            final int[] value = Arrays.copyOf(originalValue, originalValue.length);
            final long start = System.nanoTime();
            sa.sort(value);
            final long end = System.nanoTime();
            System.out.printf("%1$s  %2$s(msec)%n", name,
                    TimeUnit.NANOSECONDS.toMillis(end - start));
            if (Arrays.equals(value, IntStream.range(0, value.length).toArray()) == false) {
                throw new AssertionError();
            }
        }
    }

    private static int[] value(final int size) {
        final List<Integer> list = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        final int[] value = list.stream().mapToInt(i -> i).toArray();
        return value;
    }

    static void sort(final SortAlgorithm sa) {
        final int[] value = value(100);
        System.out.println(Arrays.toString(value));
        sa.sort(value);
        System.out.println(Arrays.toString(value));
        if (Arrays.equals(value, IntStream.range(0, value.length).toArray()) == false) {
            throw new AssertionError();
        }
    }

    interface SortAlgorithm {
        void sort(int[] value);
    }
}
