import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private byte[] getCapture() throws IOException {
        BufferedImage img = robot.createScreenCapture(screenRect);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "png", out);
        return out.toByteArray();
    }

    private Image createImage(byte[] buf) {
        InputStream is = new ByteArrayInputStream(buf);
        return new Image(is);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class Capture extends Service<byte[]> {

        final long started = System.nanoTime();

        @Override
        protected Task<byte[]> createTask() {
            Task<byte[]> task = new Task<byte[]>() {

                @Override
                protected byte[] call() throws Exception {
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
