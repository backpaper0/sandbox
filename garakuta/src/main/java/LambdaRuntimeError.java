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

	public static void main(final String[] args) {
		System.out.println(get3(new IfImpl("hoge")));
		System.out.println(get4(new Obj("fuga")));
		System.out.println(get1(new Obj("foo")));
		System.out.println(get2(new Obj("bar")));
	}

	static String get1(final Obj t) {
		return Optional.of(t).map(If::get).orElse("empty");
	}

	static <T extends Abs & If> String get2(final T t) {
		return Optional.of(t).map(If::get).orElse("empty");
	}

	/*
	 * If2 & Ifだと実行時エラーになった。
	 * If & If2だと通る。
	 * Class & Interfaceは書けるけどInterface & Classは
	 * コンパイルエラーになるっぽいので片方が抽象クラスだと
	 * どうしようもない感じする。
	 */
	//    static <T extends If2 & If> String get3(T t) {
	static <T extends If & If2> String get3(final T t) {
		return Optional.of(t).map(If::get).orElse("empty");
	}

	static <T extends Abs & If> String get4(final T t) {
		return Optional.of(t).map(a -> a.get()).orElse("empty");
	}

	public static class Obj extends Abs implements If {
		private final String text;

		public Obj(final String text) {
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

	public interface If2 {
	}

	public static class IfImpl implements If, If2 {
		private final String text;

		public IfImpl(final String text) {
			this.text = text;
		}

		@Override
		public String get() {
			return text;
		}
	}
}
