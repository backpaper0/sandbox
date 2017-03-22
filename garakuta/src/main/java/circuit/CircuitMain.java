package circuit;

import java.util.function.IntSupplier;

public class CircuitMain {

    static int nand(int a, int b) {
        return 1 - (a & b);
    }
    static int not(int a) {
        return nand(a, 1);
    }
    static int and(int a, int b) {
        return nand(nand(a, b), 1);
    }
    static int or(int a, int b) {
        return nand(not(a), not(b));
    }
    static int xor(int a, int b) {
        return and(nand(a, b), or(a, b));
    }

    public static void main(String[] args) {
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
    }
    static void test(IntSupplier supplier, int expected) {
        int actual = supplier.getAsInt();
        if (actual != expected) {
            throw new AssertionError(String.format("%s != %s", actual, expected));
        }
    }
}
