package reverseproxy;

import java.net.URI;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.util.Headers;

/**
 * Undertowを使った単純なリバースプロキシのサンプルです。
 *
 */
public class ReverseProxySample {

    public static void main(String[] args) {

        /*
         * mainを実行して http://localhost:8080/ にアクセスしてください。
         * アクセスするたびに Server #1 と Server #2 が交互に表示されます。
         * 
         * curl http://localhost:8080/
         */

        //--------------------------------
        //1つめのサーバーを立てる
        Undertow server1 = buildUndertow(8081, "Server #1");
        server1.start();

        //--------------------------------
        //2つめのサーバーを立てる
        Undertow server2 = buildUndertow(8082, "Server #2");
        server2.start();

        //--------------------------------
        //リバプロを立てる

        //LoadBalancingProxyClientはデフォルトだとラウンドロビンです。
        //addHostメソッドでリクエストを移譲するサーバーを追加します。
        ProxyClient client = new LoadBalancingProxyClient()
                .addHost(URI.create("http://localhost:8081"))
                .addHost(URI.create("http://localhost:8082"));

        //第2引数のHttpHandlerはProxyClient.findTargetの戻り値がnullの
        //場合に使われます。
        //LoadBalancingProxyClientを利用している場合はnullは帰らないので
        //使われる事は無いっぽいです。
        HttpHandler handler = new ProxyHandler(client,
                ResponseCodeHandler.HANDLE_404);

        Undertow proxy = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(handler).build();
        proxy.start();
    }

    static Undertow buildUndertow(int port, String responseBody) {
        return Undertow.builder().addHttpListener(port, "localhost")
                .setHandler(ex -> {
                    ex.getResponseHeaders().put(Headers.CONTENT_TYPE,
                            "text/plain");
                    ex.getResponseSender().send(responseBody);
                }).build();
    }
}
