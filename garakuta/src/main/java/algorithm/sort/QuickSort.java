package algorithm.sort;

public class QuickSort {

	public static void main(String[] args) {
		Sorts.sort(QuickSort::sort);
	}

	static void sort(int[] value) {
		sort(value, 0, value.length - 1);
	}

	static void sort(int[] value, int from, int to) {
		if (from < to) {
			int p = value[from];
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

	static void swap(int[] value, int i, int j) {
		int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}
}
