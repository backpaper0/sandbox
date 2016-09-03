
public class ShiftSample {
    public static void main(String[] args) {
        int i = 0xabcdef;
        System.out.printf("%02x%n", i & 0xff);
        System.out.printf("%02x%n", i >> 8 & 0xff);
        System.out.printf("%02x%n", i >> 16 & 0xff);
    }
}
