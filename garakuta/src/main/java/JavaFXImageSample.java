import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class JavaFXImageSample extends Application {

    private final Robot robot;
    private final Rectangle screenRect = new Rectangle(0, 0, 500, 500);
    private ImageView imageView;
    private long time;
    private int count;

    public JavaFXImageSample() throws AWTException {
        robot = new Robot();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        imageView = new ImageView(createImage(getCapture()));
        Parent root = new Group(imageView);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Capture().start();
    }

    private BufferedImage getCapture() {
        return robot.createScreenCapture(screenRect);
    }

    private Image createImage(BufferedImage src) {
        WritableImage img = new WritableImage(src.getWidth(), src.getHeight());
        return SwingFXUtils.toFXImage(src, img);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class Capture extends Service<BufferedImage> {

        final long started = System.nanoTime();

        @Override
        protected Task<BufferedImage> createTask() {
            Task<BufferedImage> task = new Task<BufferedImage>() {

                @Override
                protected BufferedImage call() throws Exception {
                    return getCapture();
                }

                @Override
                protected void succeeded() {
                    try {
                        imageView.setImage(createImage(get()));
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                    count++;
                    time += System.nanoTime() - started;
                    if (time >= TimeUnit.SECONDS.toNanos(1)) {
                        System.out.println(count + "(fps)");
                        count = 0;
                        time = 0;
                    }
                    new Capture().start();
                }
            };
            return task;
        }
    }
}
