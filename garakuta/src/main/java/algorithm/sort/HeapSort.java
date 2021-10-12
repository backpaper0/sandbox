package algorithm.sort;

public class HeapSort {

	public static void main(String[] args) {
		Sorts.sort(HeapSort::sort);
	}

	static void sort(int[] value) {
		buildHeap(value);
		for (int i = value.length - 1; i >= 1; i--) {
			swap(value, 0, i);
			heapify(value, 0, i);
		}
	}

	static void buildHeap(int[] value) {
		int size = value.length;
		for (int i = (size / 2) - 1; i >= 0; i--) {
			heapify(value, i, size);
		}
	}

	static void heapify(int[] value, int index, int size) {
		int parent = index;
		int left = index * 2 + 1;
		int right = index * 2 + 2;
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

	static void swap(int[] value, int i, int j) {
		int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}
}
