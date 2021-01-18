import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComparatorStudy {

	public static void main(final String[] args) {
		final List<Hoge> list = Stream
				.of(new Hoge(2), new Hoge(null), new Hoge(3), new Hoge(null), new Hoge(1))
				.sorted(Comparator.comparing(x -> x.value,
						Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());
		System.out.println(list);
	}

	static class Hoge {

		Integer value;

		public Hoge(final Integer value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Hoge(" + value + ")";
		}
	}
}
