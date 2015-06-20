package example.concurrent;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.stream.IntStream;

public class ForkJoinQuickSort extends RecursiveAction {

    public static void main(String[] args) {

        final int size = 100;

        Random r = new Random();
        int[] value = IntStream.range(0, size).boxed()
                .sorted((a, b) -> r.nextInt()).mapToInt(Integer::intValue)
                .toArray();
        System.out.printf("%s%n", Arrays.toString(value));

        sort(value, 0, value.length - 1);
        System.out.printf("%s%n", Arrays.toString(value));

        if (Arrays.equals(value, IntStream.range(0, size).toArray()) == false) {
            throw new AssertionError();
        }
    }

    private final int[] value;
    private final int from;
    private final int to;

    public ForkJoinQuickSort(int[] value, int from, int to) {
        this.value = value;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void compute() {
        sort(value, from, to);
    }

    static void sort(int[] value, int from, int to) {
        if (from < to) {
            int pivot = value[from];
            int left = from;
            int right = to;
            while (true) {
                while (value[left] < pivot) {
                    left++;
                }
                while (value[right] > pivot) {
                    right--;
                }
                if (left >= right) {
                    break;
                }
                int temp = value[left];
                value[left] = value[right];
                value[right] = temp;
                left++;
                right--;
            }
            ForkJoinQuickSort task = new ForkJoinQuickSort(value, right + 1, to);
            task.fork();
            sort(value, from, left - 1);
            task.join();
        }
    }
}
