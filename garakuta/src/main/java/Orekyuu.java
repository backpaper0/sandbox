import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Orekyuu extends Throwable {

    public static void main(String[] args) throws Throwable {
        Throwable orekyuu = new Orekyuu();
        throw orekyuu;
    }

    private final AtomicReference<Integer> distance = new AtomicReference<>();

    public Orekyuu() {
        super("俺九番は%dm飛びました");
    }

    @Override
    public String toString() {
        if (distance.get() == null) {
            distance.compareAndSet(null, new Random().nextInt(60000));
        }
        return String.format(super.toString(), distance.get());
    }
}
