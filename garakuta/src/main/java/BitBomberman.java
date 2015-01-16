/**
 * @see http://nabetani.sakura.ne.jp/hena/ord8biboma/
 */
public class BitBomberman {

    private static final int WIDTH = 6;
    private static final int HEIGHT = 5;

    public String bomb(String src) {
        String[] s = src.split("/");
        int wall = Integer.parseUnsignedInt(s[0], 16);
        int bomb = Integer.parseUnsignedInt(s[1], 16);
        int stage = 0;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (get(bomb, x, y) == 1) {
                    stage = set(stage, x, y);
                    for (int wx = x - 1; wx >= 0 && get(wall, wx, y) == 0; wx--) {
                        stage = set(stage, wx, y);
                    }
                    for (int wx = x + 1; wx < WIDTH && get(wall, wx, y) == 0; wx++) {
                        stage = set(stage, wx, y);
                    }
                    for (int wy = y - 1; wy >= 0 && get(wall, x, wy) == 0; wy--) {
                        stage = set(stage, x, wy);
                    }
                    for (int wy = y + 1; wy < HEIGHT && get(wall, x, wy) == 0; wy++) {
                        stage = set(stage, x, wy);
                    }
                }
            }
        }
        return String.format("%08x", stage);
    }

    private int get(int stage, int x, int y) {
        return stage >> (31 - (x + y * WIDTH)) & 1;
    }

    private int set(int stage, int x, int y) {
        return stage | 1 << (31 - (x + y * WIDTH));
    }
}
