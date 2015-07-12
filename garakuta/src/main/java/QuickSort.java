import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    public static void main(String[] args) {
        Random r = new Random();
        int[] value = r.ints(0, 100).limit(10).toArray();
        QuickSort qs = new QuickSort();
        qs.sort(value, 0, value.length - 1);
        System.out.printf("%s%n", Arrays.toString(value));
    }

    private void sort(int[] value, int from, int to) {
        if (from < to) {
            int p = value[from];
            int l = from;
            int r = to;
            while (true) {
                while (value[l] < p) {
                    l++;
                }
                while (value[r] > p) {
                    r--;
                }
                if (l >= r) {
                    break;
                }
                int temp = value[l];
                value[l] = value[r];
                value[r] = temp;
                l++;
                r--;
            }
            sort(value, from, l - 1);
            sort(value, r + 1, to);
        }
    }
}
