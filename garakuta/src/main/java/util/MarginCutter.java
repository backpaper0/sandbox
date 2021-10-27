package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;

/**
 * 指定されたディレクトリ内にある画像の上下左右の余白をカットする。
 * 
 */
public class MarginCutter {

	public static void main(String[] args) throws IOException {
		var targetDir = readTargetDir();
		var processor = new MarginCutter(targetDir);
		try {
			processor.process();
		} catch (ErrorMessage e) {
			System.err.printf(e.getMessage());
		}
	}

	private static Path readTargetDir() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("対象ディレクトリを入力してください。");
			String targetDir = scanner.nextLine();
			if (targetDir.isBlank() == false) {
				while (true) {
					System.out.println("このディレクトリで良いですか？(yes/no)");
					System.out.println(targetDir);
					String yesOrNo = scanner.nextLine();
					switch (yesOrNo) {
					case "yes":
						return Path.of(targetDir);
					case "no":
						break;
					default:
					}
				}
			}
		}
	}

	private final int sizeToTrimLeft = 0;
	private final int sizeToTrimTop = 360;
	private final int sizeToTrimRight = 0;
	private final int sizeToTrimBottom = 360;
	private final Path targetDir;

	public MarginCutter(Path targetDir) {
		this.targetDir = targetDir;
	}

	public void process() throws IOException {
		try (DirectoryStream<Path> dir = openInputDir()) {
			Iterator<Path> inputFiles = StreamSupport.stream(dir.spliterator(), false).sorted().iterator();
			if (inputFiles.hasNext() == false) {
				String message = String.format("カレントディレクトリの中にPNGファイルが存在しません。");
				throw new ErrorMessage(message);
			}
			Path backupDir = targetDir.resolve("backup");
			Files.createDirectories(backupDir);
			while (inputFiles.hasNext()) {
				Path inputFile = inputFiles.next();
				BufferedImage image = getTrimmedImage(inputFile);
				doBackup(inputFile, backupDir);
				writeImage(image, inputFile);
			}
		}
	}

	private DirectoryStream<Path> openInputDir() throws IOException {
		return Files.newDirectoryStream(targetDir,
				path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".png"));
	}

	private BufferedImage getTrimmedImage(Path inputFile) throws IOException {
		BufferedImage inImage;
		try (InputStream in = Files.newInputStream(inputFile)) {
			inImage = ImageIO.read(in);
		}
		return inImage.getSubimage(
				sizeToTrimLeft,
				sizeToTrimTop,
				inImage.getWidth() - (sizeToTrimLeft + sizeToTrimRight),
				inImage.getHeight() - (sizeToTrimTop + sizeToTrimBottom));
	}

	private void doBackup(Path inputFile, Path backupDir) throws IOException {
		Path backupFile = backupDir.resolve(inputFile.getFileName());
		Files.move(inputFile, backupFile, StandardCopyOption.ATOMIC_MOVE);
	}

	private void writeImage(BufferedImage image, Path outputFile) throws IOException {
		try (OutputStream out = Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW,
				StandardOpenOption.WRITE)) {
			ImageIO.write(image, "PNG", out);
		} catch (FileAlreadyExistsException e) {
			String message = String.format("出力先のパスに既にファイルが存在します: %s%n"
					+ "上書きはしない方針なので、ファイルを消すか移動してから再実行してください。",
					e.getMessage());
			throw new ErrorMessage(message, e);
		}
	}

	private static class ErrorMessage extends RuntimeException {

		public ErrorMessage(String message) {
			super(message);
		}

		public ErrorMessage(String message, Throwable e) {
			super(message, e);
		}
	}
}
