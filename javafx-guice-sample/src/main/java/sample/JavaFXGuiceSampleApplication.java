package sample;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class JavaFXGuiceSampleApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Module module = new AbstractModule() {

            @Override
            protected void configure() {
                bind(Stage.class).toInstance(primaryStage);
                bind(Hello.class).to(HelloImpl.class);
            }
        };
        Injector injector = Guice.createInjector(module);
        Callback<Class<?>, Object> controllerFactory = injector::getInstance;

        URL url = getClass().getResource("/hello.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(controllerFactory);

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
