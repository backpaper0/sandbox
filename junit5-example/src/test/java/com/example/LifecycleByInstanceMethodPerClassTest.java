package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 初期化・破棄をインスタンスメソッドで行うための仕掛けを考える。
 * 
 * ここではテストインスタンスをテストクラス毎に用意するようにして、
 * フラグを見て初期化・破棄を行うようにしている。
 * 
 * また、破棄に関してはAfterEachメソッドだけでは対応できないため、
 * テストメソッドを順序付けして最後のメソッドでフラグを立てることで
 * 破棄のタイミングをAfterEachメソッドへ伝えている。
 * なお、テストクラス内における最後のメソッドさえわかれば良いため
 * それ以外のメソッドにはOrderアノテーションは付けていない。
 * Orderアノテーションを付けない場合、順序のデフォルト値は
 * Integer.MAX_VALUE / 2 となっている。
 * （Orderアノテーションの定数DEFAULTを参照）
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LifecycleByInstanceMethodPerClassTest {

	boolean initialized;
	boolean destroy;

	@Test
	void foo(TestInfo testInfo) {
		log(testInfo);
	}

	@Test
	void bar(TestInfo testInfo) {
		log(testInfo);
	}

	@Test
	void baz(TestInfo testInfo) {
		log(testInfo);
	}

	@Test
	@Order(Integer.MAX_VALUE)
	void qux(TestInfo testInfo) {
		destroy = true;
		log(testInfo);
	}

	@BeforeEach
	void init(TestInfo testInfo) {
		if (initialized) {
			return;
		}
		log(testInfo, "initialize");
		initialized = true;
	}

	@AfterEach
	void destroy(TestInfo testInfo) {
		if (destroy) {
			log(testInfo, "destroy");
		}
	}

	private void log(TestInfo testInfo, String... args) {
		String s = args.length > 0 ? args[0] : "test";
		System.out.printf("%s %s %s%n", this, testInfo.getTestMethod().get().getName(), s);
	}
}
