import java.util.Random;

public class RandomSample {
    public static void main(final String[] args) {
        final long seed = 1234567890L;
        for (int i = 0; i < 5; i++) {
            final Random r = new Random(seed);
            System.out.printf("%d, %d, %d%n", r.nextInt(), r.nextInt(), r.nextInt());
        }
    }
}
