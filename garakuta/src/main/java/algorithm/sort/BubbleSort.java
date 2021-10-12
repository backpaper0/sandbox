package algorithm.sort;

public class BubbleSort {

	public static void main(String[] args) {
		Sorts.sort(BubbleSort::sort);
	}

	static void sort(int[] value) {
		for (int i = 0; i < value.length - 1; i++) {
			for (int j = i + 1; j < value.length; j++) {
				if (value[i] > value[j]) {
					swap(value, i, j);
				}
			}
		}
	}

	static void swap(int[] value, int i, int j) {
		int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}
}
