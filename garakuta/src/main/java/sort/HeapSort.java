package sort;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HeapSort {

    public static void main(String[] args) {

        List<Integer> list = IntStream.rangeClosed(1, 30)
                .mapToObj(x -> x)
                .collect(Collectors.toList());

        Collections.shuffle(list, new Random(1L));

        System.out.println(list);

        sort(list);

        System.out.println(list);
    }

    static <T extends Comparable<T>> void sort(List<T> list) {
        buildHeap(list);
        for (int i = list.size() - 1; i >= 1; i--) {
            swap(list, 0, i);
            heapify(list, 0, i);
        }
    }

    static <T extends Comparable<T>> void buildHeap(List<T> list) {
        int size = list.size();
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapify(list, i, size);
        }
    }

    static <T extends Comparable<T>> void heapify(List<T> list, int index, int size) {
        int parent = index;
        final int left = index * 2 + 1;
        final int right = index * 2 + 2;
        if (left < size && list.get(left).compareTo(list.get(parent)) > 0) {
            parent = left;
        }
        if (right < size && list.get(right).compareTo(list.get(parent)) > 0) {
            parent = right;
        }
        if (parent != index) {
            swap(list, parent, index);
            heapify(list, parent, size);
        }
    }
    static <T> void swap(List<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }
}
