package algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sorts {

	public static void main(String[] args) {
		var list = new LinkedHashMap<Class<?>, Consumer<int[]>>();
		//バブルソートは遅すぎるので除外
		//list.put(BubbleSort.class, BubbleSort::sort);
		list.put(HeapSort.class, HeapSort::sort);
		list.put(MergeSort.class, MergeSort::sort);
		list.put(QuickSort.class, QuickSort::sort);
		int[] originalValue = value(10_000_000);
		for (Entry<Class<?>, Consumer<int[]>> entry : list.entrySet()) {
			var name = entry.getKey().getSimpleName();
			var sa = entry.getValue();
			var value = Arrays.copyOf(originalValue, originalValue.length);
			var start = System.nanoTime();
			sa.accept(value);
			var end = System.nanoTime();
			if (Arrays.equals(value, IntStream.rangeClosed(1, value.length).toArray()) == false) {
				throw new AssertionError(name);
			}
			System.out.printf("%1$s  %2$s(msec)%n", name,
					TimeUnit.NANOSECONDS.toMillis(end - start));
		}
	}

	private static int[] value(int size) {
		var list = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(list);
		return list.stream().mapToInt(Integer::intValue).toArray();
	}

	static void sort(Consumer<int[]> sa) {
		var value = value(100);
		System.out.println(Arrays.toString(value));
		sa.accept(value);
		System.out.println(Arrays.toString(value));
		if (Arrays.equals(value, IntStream.rangeClosed(1, value.length).toArray()) == false) {
			throw new AssertionError();
		}
	}
}
