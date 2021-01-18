package example.setindex;

import static example.setindex.Collectors2.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SetIndexExample {

	public static void main(final String[] args) {
		final List<Foo> list = new ArrayList<>();

		//1. 昔ながらのforを使った方法
		for (int i = 0; i < list.size(); i++) {
			final Foo obj = list.get(i);
			obj.setIndex(i);
		}

		//2. 拡張forループとカウンター
		//   counterのスコープがforのブロック外に出てしまうのが問題。
		int counter = 0;
		for (final Foo obj : list) {
			obj.setIndex(counter++);
		}
		//   どうしてもスコープを狭めたい場合は更にブロックで囲む手もある。
		{
			int counter2 = 0;
			for (final Foo obj : list) {
				obj.setIndex(counter2++);
			}
		}

		//3. Collectorを使う手もある
		list.stream()
				.collect(eachWithIndex((obj, index) -> {
					obj.setIndex(index);
				}));
	}
}

class Foo {

	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}
}

class Collectors2 {

	static <T> Collector<T, ?, List<T>> eachWithIndex(final ObjIntConsumer<T> action) {

		final Supplier<List<T>> supplier = () -> {
			return new ArrayList<>();
		};

		final BiConsumer<List<T>, T> accumulator = (list, t) -> {
			final int index = list.size();
			action.accept(t, index);
			list.add(t);
		};

		final BinaryOperator<List<T>> combiner = (a, b) -> {
			a.addAll(b);
			return a;
		};

		final Collector<T, List<T>, List<T>> collector = Collector.of(
				supplier,
				accumulator,
				combiner);
		return collector;
	}
}