import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

public class Kiyoshi {

    public static void main(String[] args) {
        Kiyoshi k = new Kiyoshi();
        if (k.r.nextBoolean()) {
            k.ズン();
        } else {
            k.ドコ();
        }
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
        StackTraceElement[] xs = new Throwable().getStackTrace();
        if (Arrays.stream(xs).skip(1).limit(4).map(StackTraceElement::getMethodName)
                .filter(Predicate.isEqual("ズン")).count() == 4) {
            throw new RuntimeException("キ・ヨ・シ！");
        } else if (r.nextBoolean()) {
            ズン();
        } else {
            ドコ();
        }
    }
}
