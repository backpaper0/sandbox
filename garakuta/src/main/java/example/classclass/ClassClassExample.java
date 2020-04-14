package example.classclass;

public class ClassClassExample {

    public static void main(final String[] args) {
        p("クラス", ExampleClass.class);
        p("抽象クラス", ExampleAbstractClass.class);
        p("インターフェース", ExampleInterface.class);
        p("アノテーション", ExampleAnnotation.class);
        p("列挙型", ExampleEnum.class);
        p("列挙型(値)", ExampleEnum.VALUE1.getClass());
        p("列挙型(値,メソッドオーバーライド)", ExampleEnum.VALUE2.getClass());
    }

    @ExampleAnnotation
    Object a;

    private static void p(final String desc, final Class<?> clazz) {
        System.out.printf("# %s%n", desc);
        System.out.printf("        クラス名: %s%n", clazz.getName());
        System.out.printf("    isAnnotation: %s%n", clazz.isAnnotation());
        System.out.printf("isAnonymousClass: %s%n", clazz.isAnonymousClass());
        System.out.printf("          isEnum: %s%n", clazz.isEnum());
        System.out.printf("     isInterface: %s%n", clazz.isInterface());
        System.out.printf("%n");
    }
}
