package circuit;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class CircuitMain {

    static int nand(final int a, final int b) {
        return 1 - (a & b);
    }

    static int not(final int a) {
        return nand(a, 1);
    }

    static int and(final int a, final int b) {
        return nand(nand(a, b), 1);
    }

    static int or(final int a, final int b) {
        return nand(not(a), not(b));
    }

    static int xor(final int a, final int b) {
        return and(nand(a, b), or(a, b));
    }

    static int mux(final int a, final int b, final int sel) {
        return or(and(a, not(sel)), and(b, sel));
    }

    static int[] dmux(final int in, final int sel) {
        return new int[] { and(in, not(sel)), and(in, sel) };
    }

    public static void main(final String[] args) {
        System.out.println("Nand");
        test(() -> nand(0, 0), 1);
        test(() -> nand(0, 1), 1);
        test(() -> nand(1, 0), 1);
        test(() -> nand(1, 1), 0);

        System.out.println("Not");
        test(() -> not(0), 1);
        test(() -> not(1), 0);

        System.out.println("And");
        test(() -> and(0, 0), 0);
        test(() -> and(0, 1), 0);
        test(() -> and(1, 0), 0);
        test(() -> and(1, 1), 1);

        System.out.println("Or");
        test(() -> or(0, 0), 0);
        test(() -> or(0, 1), 1);
        test(() -> or(1, 0), 1);
        test(() -> or(1, 1), 1);

        System.out.println("Xor");
        test(() -> xor(0, 0), 0);
        test(() -> xor(0, 1), 1);
        test(() -> xor(1, 0), 1);
        test(() -> xor(1, 1), 0);

        System.out.println("Mux");
        test(() -> mux(0, 0, 0), 0);
        test(() -> mux(0, 1, 0), 0);
        test(() -> mux(1, 0, 0), 1);
        test(() -> mux(1, 1, 0), 1);
        test(() -> mux(0, 0, 1), 0);
        test(() -> mux(0, 1, 1), 1);
        test(() -> mux(1, 0, 1), 0);
        test(() -> mux(1, 1, 1), 1);

        System.out.println("DMux");
        test2(() -> dmux(0, 0), 0, 0);
        test2(() -> dmux(1, 0), 1, 0);
        test2(() -> dmux(0, 1), 0, 0);
        test2(() -> dmux(1, 1), 0, 1);
    }

    static void test(final IntSupplier supplier, final int expected) {
        final int actual = supplier.getAsInt();
        if (actual != expected) {
            throw new AssertionError(String.format("%s != %s", actual, expected));
        }
    }

    static void test2(final Supplier<int[]> supplier, final int expected1, final int expected2) {
        final int[] actual = supplier.get();
        if (actual[0] != expected1 || actual[1] != expected2) {
            throw new AssertionError(String.format("(%s, %s) != (%s, %s)", actual[0], actual[1],
                    expected1, expected2));
        }
    }
}
