package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AndroidNextPageAndScreencap {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String title;
			while (true) {
				System.out.println("タイトルを入力してください");
				title = scanner.nextLine();
				System.out.println("このタイトルでいいですか？(yes/no)");
				System.out.println(title);
				String yesOrNo = scanner.nextLine();
				if (yesOrNo.equals("yes")) {
					break;
				}
			}
			String direction;
			while (true) {
				System.out.println("ページ送りの方向を入力してください(left/right)");
				direction = scanner.nextLine();
				if (Set.of("left", "right").contains(direction)) {
					break;
				}
				System.out.println("leftまたはrightと入力してください。");
			}

			Path dir = Path.of(System.getProperty("user.home"), "Pictures", title);
			if (Files.exists(dir)) {
				throw new ApplicationExcetion("ディレクトリ " + dir.toAbsolutePath() + " は既に存在します");
			}
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
				throw new ApplicationExcetion("ディレクトリ " + dir.toAbsolutePath() + " の作成に失敗しました", e);
			}

			String adb = System.getProperty("user.home") + "/android-sdk/platform-tools/adb";
			String titleTemplate = title + "_%03d.png";
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				throw new AssertionError("ハッシュアルゴリズムMD5がありません", e);
			}
			byte[] prev = null;
			for (int i = 1; i < 1000; i++) {
				Path file = dir.resolve(String.format(titleTemplate, i));
				execute(adb, "shell", "screencap", "-p", "/sdcard/temp.png");

				execute(adb, "pull", "/sdcard/temp.png", file.toString());

				byte[] digest;
				try {
					digest = md.digest(Files.readAllBytes(file));
				} catch (IOException e) {
					throw new ApplicationExcetion("ファイル " + file.toAbsolutePath() + " の読み込みに失敗しました", e);
				}
				if (prev != null && Arrays.equals(digest, prev)) {
					break;
				}
				prev = digest;

				execute(adb, "shell", "input", "keyevent", "DPAD_" + direction.toUpperCase());
			}

			execute(adb, "shell", "rm", "/sdcard/temp.png");
		} catch (ApplicationExcetion e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	static void execute(String... command) throws ApplicationExcetion {
		try {
			new ProcessBuilder(command).start().waitFor();
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			throw new ApplicationExcetion("割り込みが発生しました", e);
		} catch (IOException e) {
			throw new ApplicationExcetion("I/Oで例外が発生しました", e);
		}
	}

	static class ApplicationExcetion extends Exception {

		public ApplicationExcetion(String message) {
			super(message);
		}

		public ApplicationExcetion(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
