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
	 * 使用可能なパッケージ(サブパッケージも許可する)
	 */
	private final Set<String> allowedPackagesAndSubPackages;

	/**
	 * 使用可能なクラス
	 */
	private final Set<String> allowedClasses;

	/**
	 * コンストラクタ。
	 * 
	 * @param allowedPackages 使用可能なパッケージ
	 * @param allowedPackagesAndSubPackages 使用可能なパッケージ(サブパッケージも許可する)
	 * @param allowedClasses 使用可能なクラス
	 */
	private WhitelistApiUsageCondition(Set<String> allowedPackages,
			Set<String> allowedPackagesAndSubPackages, Set<String> allowedClasses) {
		super("use only classes in whitelist");
		this.allowedPackages = allowedPackages;
		this.allowedPackagesAndSubPackages = allowedPackagesAndSubPackages;
		this.allowedClasses = allowedClasses;
	}

	@Override
	public void check(JavaClass item, ConditionEvents events) {
		item.getAccessesFromSelf().stream()
				.map(JavaAccess::getTargetOwner)
				.distinct()
				.forEach(javaClass -> {
					String packageName = javaClass.getPackageName();
					boolean isAllowedPackage = allowedPackages.contains(packageName);
					boolean isAllowedPackage2 = allowedPackagesAndSubPackages.stream()
							.anyMatch(sp -> packageName.startsWith(sp));
					boolean isAllowedClass = allowedClasses.contains(javaClass.getFullName());
					if (!isAllowedPackage && !isAllowedPackage2 && !isAllowedClass) {
						events.add(SimpleConditionEvent.violated(item,
								item.getFullName() + " should not use " + javaClass.getFullName()));
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
		 * 使用可能なパッケージ(サブパッケージも許可する)
		 */
		private final Set<String> allowedPackagesAndSubPackages = new HashSet<>();

		/**
		 * 使用可能なクラス
		 */
		private final Set<String> allowedClasses = new HashSet<>();

		/**
		 * {@link #allowedPackages}または{@link #allowedPackagesAndSubPackages}を返す。
		 * 
		 * @param allowSubPackage サブパッケージを許可する場合はtrueを渡す
		 * @return {@link #allowedPackages}または{@link #allowedPackagesAndSubPackages}
		 */
		private Set<String> packages(boolean allowSubPackage) {
			return allowSubPackage ? allowedPackagesAndSubPackages : allowedPackages;
		}

		/**
		 * 使用可能なパッケージを追加する。
		 * 
		 * @param allowSubPackage サブパッケージを許可する場合はtrueを渡す
		 * @param packages 使用可能なパッケージ
		 * @return ビルダー自身を返す
		 */
		public Builder addPackages(boolean allowSubPackage, String... packages) {
			Set<String> ps = packages(allowSubPackage);
			Arrays.stream(packages).forEach(ps::add);
			return this;
		}

		/**
		 * 使用可能なパッケージを追加する。
		 * 
		 * @param allowSubPackage サブパッケージを許可する場合はtrueを渡す
		 * @param packages 使用可能なパッケージ
		 * @return ビルダー自身を返す
		 */
		public Builder addPackages(boolean allowSubPackage, Package... packages) {
			Set<String> ps = packages(allowSubPackage);
			Arrays.stream(packages).map(Package::getName).forEach(ps::add);
			return this;
		}

		/**
		 * 使用可能なパッケージを追加する。
		 * 追加されるパッケージは引数で渡されたクラスのパッケージ。
		 * 
		 * @param allowSubPackage サブパッケージを許可する場合はtrueを渡す
		 * @param baseClasses 使用可能なパッケージに属するクラス
		 * @return ビルダー自身を返す
		 */
		public Builder addPackagesByBaseClasses(boolean allowSubPackage, Class<?>... baseClasses) {
			Set<String> ps = packages(allowSubPackage);
			Arrays.stream(baseClasses).map(Class::getPackageName).forEach(ps::add);
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
			return new WhitelistApiUsageCondition(
					allowedPackages,
					allowedPackagesAndSubPackages,
					allowedClasses);
		}
	}
}
