import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class JavaFXImageSample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(0, 0, 500, 500);
        BufferedImage img = robot.createScreenCapture(screenRect);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "png", out);
        InputStream is = new ByteArrayInputStream(out.toByteArray());

        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        Parent root = new Group(imageView);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
