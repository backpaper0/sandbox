/*
 * 列挙定数でメソッドをオーバーライドする(というか { } を付けるだけでも)と
 * その列挙型の匿名サブクラスとなりClass.isEnumでfalseを返すようになります。
 * 
 * アプリケーションの基盤部分などでリフレクションを利用して列挙型を扱うコードを
 * 書くこともあるとは思いますが、その際Class.isEnumをチェックしていると
 * Hoge.BARは列挙型ではないと判断されてしまいます。
 * 
 * そのような思いもよらないバグが発生する可能性があると考えるので私は
 * なるべく列挙定数でメソッドをオーバーライドしないようにしています。
 * 
 * ※列挙定数でメソッドをオーバーライドしない理由は他にもありますが、
 * 　ここでは語りません。
 */
public class EnumReflectionSample {

	enum Hoge {

		FOO,

		BAR {
			@Override
			public String toString() {
				return "bar";
			}
		}
	}

	public static void main(String[] args) {
		log(Hoge.FOO);
		log(Hoge.BAR);
	}

	static void log(Hoge constant) {

		Class<?> clazz = constant.getClass();
		String fqcn = clazz.getName();
		boolean isEnum = clazz.isEnum();

		System.out.printf("%s: class=%s, enum=%s%n", constant, fqcn, isEnum);
	}
}
