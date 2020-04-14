package example.classclass;

import java.lang.reflect.Proxy;

public class ClassClassExample {

    public static void main(final String[] args) throws Exception {
        p("クラス", ExampleClass.class);
        p("抽象クラス", ExampleAbstractClass.class);
        p("インターフェース", ExampleInterface.class);
        p("アノテーション", ExampleAnnotation.class);
        p("アノテーション(実体)",
                ClassClassExample.class.getDeclaredField("a").getAnnotation(ExampleAnnotation.class)
                        .getClass());
        p("列挙型", ExampleEnum.class);
        p("列挙型(値)", ExampleEnum.VALUE1.getClass());
        p("列挙型(値,メソッドオーバーライド)", ExampleEnum.VALUE2.getClass());
    }

    @ExampleAnnotation
    Object a;

    private static void p(final String desc, final Class<?> clazz) {
        System.out.printf("# %s - %s%n", desc, clazz.getName());
        System.out.printf("    Class.isAnnotation: %s%n", clazz.isAnnotation());
        System.out.printf("Class.isAnonymousClass: %s%n", clazz.isAnonymousClass());
        System.out.printf("          Class.isEnum: %s%n", clazz.isEnum());
        System.out.printf("     Class.isInterface: %s%n", clazz.isInterface());
        System.out.printf("    Proxy.isProxyClass: %s%n", Proxy.isProxyClass(clazz));
        System.out.printf("%n");
    }
}
