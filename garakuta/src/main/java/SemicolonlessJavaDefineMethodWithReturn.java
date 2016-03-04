
public class SemicolonlessJavaDefineMethodWithReturn {

    public static void main(String[] args) {
        if (java.util.stream.Stream
                .of((Hello) java.lang.reflect.Proxy.newProxyInstance(Hello.class.getClassLoader(),
                        new Class<?>[] { Hello.class }, (p, m, a) -> "Hello, " + a[0] + "!"))
                .map(hello -> hello.say("world")).peek(System.out::println).count() > 0) {
        }
    }

    public interface Hello {

        default String say(String name) {
            while (true) {
            }
        }
    }
}
