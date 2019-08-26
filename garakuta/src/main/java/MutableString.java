import java.lang.reflect.Field;

public class MutableString {

    public static void main(final String[] args) throws Exception {
        final String s = "Hello, world!";

        final Field f = String.class.getDeclaredField("value");
        f.setAccessible(true);

        final char[] cs = (char[]) f.get(s);
        cs[7] = 'i';
        cs[8] = 'r';
        cs[9] = 'o';
        cs[10] = 'f';
        cs[11] = '!';

        System.out.println(s);
    }
}
