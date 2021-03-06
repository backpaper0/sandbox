package app;

import java.net.URI;
import java.util.logging.Level;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

    public static void main(String[] args) {

        //ベースとなるURL
        URI uri = URI.create("http://localhost:8080/");

        //リソースクラスなどを登録する
        //以下は一例
        ResourceConfig config = new ResourceConfig();

        //appパッケージ以下のリソースクラスなどJAX-RSに関係するクラスを登録する
        //パッケージは再帰的にスキャンされる
        config.packages(true, "app");

        //リクエストとレスポンスに関する情報をログ出力するフィルターを登録する
        config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.INFO.getName());
        config.register(LoggingFeature.class);

        //サーバー起動
        JdkHttpServerFactory.createHttpServer(uri, config);

        //http://localhost:8080/hello?name=foobar にアクセスして動作確認
        //control + cでJVM落としてサーバも停止する
    }
}
