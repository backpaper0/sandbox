package example;

import java.util.function.Supplier;

public class ClassNameExample {

	public static void main(final String[] args) {
		p("トップレベルのクラス", ClassNameExample.class);
		p("配列", ClassNameExample[].class);
		p("プリミティブ", int.class);
		p("void", void.class);
		p("staticにネストしたクラス", StaticNested.class);
		p("インナークラス", Inner.class);
		p("staticにネストしたクラス(エンクロージングクラスのサブクラスからの参照)", Sub.StaticNested.class);
		p("インナークラス(エンクロージングクラスのサブクラスからの参照)", Sub.Inner.class);
		p("匿名クラス", new Object() {
		}.getClass());
		p("ラムダ式", ((Supplier<Object>) () -> null).getClass());
		class MethodLocal {
		}
		p("メソッドローカルクラス", MethodLocal.class);
	}

	private static void p(final String desc, final Class<?> clazz) {
		System.out.printf("# %s%n", desc);
		System.out.printf("         getName: %s%n", clazz.getName());
		System.out.printf("     getTypeName: %s%n", clazz.getTypeName());
		System.out.printf("   getSimpleName: %s%n", clazz.getSimpleName());
		System.out.printf("getCanonicalName: %s%n", clazz.getCanonicalName());
		System.out.printf("    isAnnotation: %s%n", clazz.isAnnotation());
		System.out.printf("isAnonymousClass: %s%n", clazz.isAnonymousClass());
		System.out.printf("         isArray: %s%n", clazz.isArray());
		System.out.printf("    isLocalClass: %s%n", clazz.isLocalClass());
		System.out.printf("   isMemberClass: %s%n", clazz.isMemberClass());
		System.out.printf("     isPrimitive: %s%n", clazz.isPrimitive());
		System.out.printf("     isSynthetic: %s%n", clazz.isSynthetic());
		System.out.printf("%n");
	}

	static class StaticNested {
	}

	class Inner {
	}
}

class Sub extends ClassNameExample {
}
