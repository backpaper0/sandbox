package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * ホワイトリスト形式のAPI使用可否チェック。
 * 使用可能なパッケージと使用可能なクラスを定義できて、
 * いずれかにマッチする場合はチェックOKとする。
 *
 */
public class WhitelistApiUsageCondition extends ArchCondition<JavaClass> {

	/**
	 * 使用可能なパッケージ
	 */
	private final Set<String> allowedPackages;

	/**
	 * 使用可能なクラス
	 */
	private final Set<String> allowedClasses;

	/**
	 * コンストラクタ。
	 * 
	 * @param allowedPackages 使用可能なパッケージ
	 * @param allowedClasses 使用可能なクラス
	 */
	private WhitelistApiUsageCondition(Set<String> allowedPackages, Set<String> allowedClasses) {
		super("use only classes in whitelist");
		this.allowedPackages = allowedPackages;
		this.allowedClasses = allowedClasses;
	}

	@Override
	public void check(JavaClass item, ConditionEvents events) {
		item.getAccessesFromSelf().stream()
				.map(JavaAccess::getTargetOwner)
				.distinct()
				.forEach(javaClass -> {
					boolean isAllowedPackage = allowedPackages.contains(javaClass.getPackageName());
					boolean isAllowedClass = allowedClasses.contains(javaClass.getFullName());
					if (!isAllowedPackage && !isAllowedClass) {
						events.add(SimpleConditionEvent.violated(item,
								javaClass.getFullName() + " can't be used."));
					}
				});
	}

	/**
	 * ビルダーのインスタンスを返す。
	 * 
	 * @return ビルダーのインスタンス
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * ビルダー。
	 *
	 */
	public static class Builder {

		/**
		 * 使用可能なパッケージ
		 */
		private final Set<String> allowedPackages = new HashSet<>();

		/**
		 * 使用可能なクラス
		 */
		private final Set<String> allowedClasses = new HashSet<>();

		/**
		 * 使用可能なパッケージを追加する。
		 * 
		 * @param packages 使用可能なパッケージ
		 * @return ビルダー自身を返す
		 */
		public Builder addPackages(String... packages) {
			Arrays.stream(packages).forEach(allowedPackages::add);
			return this;
		}

		/**
		 * 使用可能なパッケージを追加する。
		 * 
		 * @param packages 使用可能なパッケージ
		 * @return ビルダー自身を返す
		 */
		public Builder addPackages(Package... packages) {
			Arrays.stream(packages).map(Package::getName).forEach(allowedPackages::add);
			return this;
		}

		/**
		 * 使用可能なパッケージを追加する。
		 * 追加されるパッケージは引数で渡されたクラスのパッケージ。
		 * 
		 * @param baseClasses 使用可能なパッケージに属するクラス
		 * @return ビルダー自身を返す
		 */
		public Builder addPackagesByBaseClasses(Class<?>... baseClasses) {
			Arrays.stream(baseClasses).map(Class::getPackageName).forEach(allowedPackages::add);
			return this;
		}

		/**
		 * 使用可能なクラスを追加する。
		 * 
		 * @param packages 使用可能なクラス
		 * @return ビルダー自身を返す
		 */
		public Builder addClasses(String... classes) {
			Arrays.stream(classes).forEach(allowedClasses::add);
			return this;
		}

		/**
		 * 使用可能なクラスを追加する。
		 * 
		 * @param packages 使用可能なクラス
		 * @return ビルダー自身を返す
		 */
		public Builder addClasses(Class<?>... classes) {
			Arrays.stream(classes).map(Class::getName).forEach(allowedClasses::add);
			return this;
		}

		/**
		 * 構築する。
		 * 
		 * @return 構築されたインスタンス
		 */
		public WhitelistApiUsageCondition build() {
			return new WhitelistApiUsageCondition(allowedPackages, allowedClasses);
		}
	}
}
