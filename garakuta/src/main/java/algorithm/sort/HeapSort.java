package algorithm.sort;

public class HeapSort {

	public static void main(final String[] args) {
		Sorts.sort(HeapSort::sort);
	}

	static void sort(final int[] value) {
		buildHeap(value);
		for (int i = value.length - 1; i >= 1; i--) {
			swap(value, 0, i);
			heapify(value, 0, i);
		}
	}

	static void buildHeap(final int[] value) {
		final int size = value.length;
		for (int i = (size / 2) - 1; i >= 0; i--) {
			heapify(value, i, size);
		}
	}

	static void heapify(final int[] value, final int index, final int size) {
		int parent = index;
		final int left = index * 2 + 1;
		final int right = index * 2 + 2;
		if (left < size && value[left] > value[parent]) {
			parent = left;
		}
		if (right < size && value[right] > value[parent]) {
			parent = right;
		}
		if (parent != index) {
			swap(value, parent, index);
			heapify(value, parent, size);
		}
	}

	static void swap(final int[] value, final int i, final int j) {
		final int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}
}
