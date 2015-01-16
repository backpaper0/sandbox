import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Dice {

    private final static int TOP = 0;
    private final static int BOTTOM = 1;
    private final static int NORTH = 2;
    private final static int SOUTH = 3;
    private final static int WEST = 4;
    private final static int EAST = 5;
    private final static int TEMP = 6;

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

    static class IntPair {
        final int first;
        final int second;

        public IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    enum Direction {
        N(SOUTH, NORTH), E(WEST, EAST), S(NORTH, SOUTH), W(EAST, WEST);

        final List<IntPair> indexes;

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
