import java.util.Optional;

//実行時エラーになる( 'ω' )
//なんで？
//
//foo
//Exception in thread "main" java.lang.BootstrapMethodError: call site initialization exception
//    at java.lang.invoke.CallSite.makeSite(CallSite.java:341)
//    at java.lang.invoke.MethodHandleNatives.linkCallSiteImpl(MethodHandleNatives.java:307)
//    at java.lang.invoke.MethodHandleNatives.linkCallSite(MethodHandleNatives.java:297)
//    at LambdaRuntimeError.get2(LambdaRuntimeError.java:15)
//    at LambdaRuntimeError.main(LambdaRuntimeError.java:7)
//Caused by: java.lang.invoke.LambdaConversionException: Invalid receiver type class LambdaRuntimeError$Abs; not a subtype of implementation type interface LambdaRuntimeError$If
//    at java.lang.invoke.AbstractValidatingLambdaMetafactory.validateMetafactoryArgs(AbstractValidatingLambdaMetafactory.java:233)
//    at java.lang.invoke.LambdaMetafactory.metafactory(LambdaMetafactory.java:303)
//    at java.lang.invoke.CallSite.makeSite(CallSite.java:302)
//    ... 4 more
//
public class LambdaRuntimeError {

    public static void main(String[] args) {
        System.out.println(get1(new Obj("foo")));
        System.out.println(get2(new Obj("bar")));
    }

    static String get1(Obj t) {
        return Optional.of(t).map(If::get).orElse("empty");
    }

    static <T extends Abs & If> String get2(T t) {
        return Optional.of(t).map(If::get).orElse("empty");
    }

    public static class Obj extends Abs implements If {
        private final String text;

        public Obj(String text) {
            this.text = text;
        }

        @Override
        public String get() {
            return text;
        }
    }

    public static abstract class Abs {
    }

    public interface If {
        String get();
    }
}
