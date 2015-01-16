import java.lang.reflect.Field;

public class MutableString {

    public static void main(String[] args) throws Exception {
        String s = "Hello, world!";

        Field f = String.class.getDeclaredField("value");
        f.setAccessible(true);

        char[] cs = (char[]) f.get(s);
        cs[7] = 'i';
        cs[8] = 'r';
        cs[9] = 'o';
        cs[10] = 'f';
        cs[11] = '!';

        System.out.println(s);
    }
}
