package algorithm.sort;

public class QuickSort {

	public static void main(final String[] args) {
		Sorts.sort(QuickSort::sort);
	}

	static void sort(final int[] value) {
		sort(value, 0, value.length - 1);
	}

	static void sort(final int[] value, final int from, final int to) {
		if (from < to) {
			final int p = value[from];
			int i = from;
			int j = to;
			while (true) {
				while (value[i] < p) {
					i++;
				}
				while (value[j] > p) {
					j--;
				}
				if (i >= j) {
					break;
				}
				swap(value, i, j);
				i++;
				j--;
			}
			sort(value, from, i - 1);
			sort(value, j + 1, to);
		}
	}

	static void swap(final int[] value, final int i, final int j) {
		final int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}
}
