import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

public class Kiyoshi extends RuntimeException {

    public static void main(final String[] args) {
        final Kiyoshi k = new Kiyoshi();
        if (k.r.nextBoolean()) {
            k.ズン();
        } else {
            k.ドコ();
        }
    }

    public Kiyoshi() {
        super();
    }

    public Kiyoshi(final String message) {
        super(message);
    }

    Random r = new Random();

    void ズン() {
        if (r.nextBoolean()) {
            ズン();
        } else {
            ドコ();
        }
    }

    void ドコ() {
        final StackTraceElement[] xs = new Throwable().getStackTrace();
        if (Arrays.stream(xs).skip(1).limit(4).map(StackTraceElement::getMethodName)
                .filter(Predicate.isEqual("ズン")).count() == 4) {
            throw new Kiyoshi("キ・ヨ・シ！");
        } else if (r.nextBoolean()) {
            ズン();
        } else {
            ドコ();
        }
    }
}
