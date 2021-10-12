import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Dice {

	private static int TOP = 0;
	private static int BOTTOM = 1;
	private static int NORTH = 2;
	private static int SOUTH = 3;
	private static int WEST = 4;
	private static int EAST = 5;
	private static int TEMP = 6;

	public String solve(String src) {
		char[] cs = "162534x".toCharArray();
		StringBuilder buf = new StringBuilder();
		buf.append(cs[TOP]);
		src.chars().forEach(c -> {
			Direction.of(c).indexes.forEach(i -> {
				cs[i.first] = cs[i.second];
			});
			buf.append(cs[TOP]);
		});
		return buf.toString();
	}

	record IntPair(int first, int second) {
	}

	enum Direction {
		N(SOUTH, NORTH), E(WEST, EAST), S(NORTH, SOUTH), W(EAST, WEST);

		List<IntPair> indexes;

		Direction(int _1, int _3) {
			indexes = new ArrayList<>();
			IntStream.of(TEMP, TOP, _1, BOTTOM, _3, TEMP).reduce((f, s) -> {
				indexes.add(new IntPair(f, s));
				return s;
			});
		}

		static Direction of(int c) {
			return Arrays.stream(values()).filter(d -> d.name().charAt(0) == c)
					.findFirst().get();
		}
	}
}
