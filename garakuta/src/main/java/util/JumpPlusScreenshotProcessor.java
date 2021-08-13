package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;

/**
 * 手持ちのAndroidスマホ(Pixel 5)で撮った週刊少年ジャンプ+アプリ版のスクリーンショットを良い感じに加工する。
 * 
 * <p>
 * アプリ版で購入した週刊少年ジャンプ2020年35号に掲載されている「アオのハコ」読切版をパソコン上でも
 * 読みたいため、スクリーンショットを撮ってみたけどそのままだと読みづらいのでいくつか加工を行う。
 * </p>
 * 
 * <ul>
 * <li>不要な上下の余白をカット</li>
 * <li>2ページ分を結合して1枚の画像にする</li>
 * </ul>
 * 
 * <p>入力・出力共に画像の形式はPNG。</p>
 *
 */
public class JumpPlusScreenshotProcessor {

	public static void main(String[] args) throws IOException {
		var processor = new JumpPlusScreenshotProcessor();
		try {
			processor.process();
		} catch (ErrorMessage e) {
			System.err.printf(e.getMessage());
		}
	}

	/**
	 * スクリーンショットが格納されているディレクトリ
	 */
	private final Path screenshotsDir = Path.of(System.getProperty("user.home"), "Downloads", "screenshots");
	/**
	 * 加工後の画像を出力するディレクトリ
	 */
	private final Path outputDir = Path.of(System.getProperty("user.home"), "Downloads", "merged-images-output");

	/**
	 * 最初のページを結合するかどうかのフラグ
	 */
	private final boolean firstPageIsMerged = false;

	/**
	 * 出力するファイル名の接頭辞
	 */
	private final String outputFileNamePrefix = "アオのハコ読切版";

	/**
	 * トリミングする上部の余白サイズ
	 */
	private final int sizeToTrimTop = 400;

	/**
	 * トリミングする下部の余白サイズ
	 */
	private final int sizeToTrimBottom = 250;

	public void process() throws IOException {
		Files.createDirectories(outputDir);
		try (DirectoryStream<Path> dir = openInputDir()) {
			Iterator<Path> inputFiles = StreamSupport.stream(dir.spliterator(), false).sorted().iterator();
			if (inputFiles.hasNext() == false) {
				String message = String.format("%s の中に入力ファイルが存在しません。%n"
						+ "入力ファイルを準備してから再実行してください。", screenshotsDir);
				throw new ErrorMessage(message);
			}
			if (inputFiles.hasNext() && firstPageIsMerged == false) {
				Path firstPage = inputFiles.next();
				BufferedImage image = getTrimmedImage(firstPage);
				writeImage(image);
			}
			while (inputFiles.hasNext()) {
				BufferedImage image = getTrimmedImage(inputFiles.next());
				if (inputFiles.hasNext()) {
					BufferedImage rightImage = image;
					BufferedImage leftImage = getTrimmedImage(inputFiles.next());
					image = merge(rightImage, leftImage);
				}
				writeImage(image);
			}
		}
	}

	private DirectoryStream<Path> openInputDir() throws IOException {
		try {
			return Files.newDirectoryStream(screenshotsDir,
					path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".png"));
		} catch (NoSuchFileException e) {
			String message = String.format("入力ファイルが格納されたディレクトリが存在しません: %s%n"
					+ "上記のパスにディレクトリと入力ファイルを準備してから再実行してください。",
					e.getMessage());
			throw new ErrorMessage(message, e);
		} catch (NotDirectoryException e) {
			String message = String.format("%s はディレクトリではありません。%n"
					+ "上記のパスは入力ファイルが格納されたディレクトリである必要があります。",
					e.getMessage());
			throw new ErrorMessage(message, e);
		}
	}

	private BufferedImage getTrimmedImage(Path inputFile) throws IOException {
		BufferedImage inImage;
		try (InputStream in = Files.newInputStream(inputFile)) {
			inImage = ImageIO.read(in);
		}
		return inImage.getSubimage(0, sizeToTrimTop, inImage.getWidth(),
				inImage.getHeight() - (sizeToTrimTop + sizeToTrimBottom));
	}

	private BufferedImage merge(BufferedImage rightImage, BufferedImage leftImage) {
		BufferedImage image = new BufferedImage(leftImage.getWidth() * 2, leftImage.getHeight(), leftImage.getType());
		Graphics2D g = image.createGraphics();
		g.drawImage(leftImage, 0, 0, null);
		g.drawImage(rightImage, leftImage.getWidth(), 0, null);
		g.dispose();
		return image;
	}

	private void writeImage(BufferedImage image) throws IOException {
		try (OutputStream out = Files.newOutputStream(nextOutputFile(),
				StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
			ImageIO.write(image, "PNG", out);
		} catch (FileAlreadyExistsException e) {
			String message = String.format("出力先のパスに既にファイルが存在します: %s%n"
					+ "上書きはしない方針なので、ファイルを消すか移動してから再実行してください。",
					e.getMessage());
			throw new ErrorMessage(message, e);
		}
	}

	private int counter = 0;

	private Path nextOutputFile() {
		String pageNumber = String.format("%02d", ++counter);
		Path file = Path.of(outputFileNamePrefix + pageNumber + ".png");
		return outputDir.resolve(file);
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
