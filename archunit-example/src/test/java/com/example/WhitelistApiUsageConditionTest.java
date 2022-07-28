package com.example;

import static com.example.Classes.*;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

public class WhitelistApiUsageConditionTest {

	@Test
	void test() {
		ArchRuleDefinition
				.classes()
				.should(WhitelistApiUsageCondition.builder()
						.addPackagesByBaseClasses(true, Foo.class)
						.addPackages(false, "java.lang", "java.util")
						.addClasses(PrintStream.class)
						.build())
				.because("ホワイトリスト内のAPIのみを使用しなければいけません")
				.check(classes);
	}
}
