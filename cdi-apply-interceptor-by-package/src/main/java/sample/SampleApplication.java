package sample;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * JAX-RSを有効化するクラスです。
 * 
 * 動作確認のためJAX-RSを使っています。
 * curlで確認できるので楽ちん。
 * 
 */
@ApplicationScoped
@ApplicationPath("api")
public class SampleApplication extends Application {
}
