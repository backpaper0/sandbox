import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * I/Oバウンドな処理における並列実行の性能比較デモ
 *
 * シングルスレッド、プラットフォームスレッド、仮想スレッドの3つの実行モードで
 * HTTP遅延リクエスト処理を実行し、それぞれの性能を比較します。
 */
public class IoBoundsDemo {

    public static void main(String[] args) throws Exception {
        // 処理を分割するタスク数
        var size = 4;
        // コマンドライン引数で実行モードを選択
        switch (args[0]) {
            case "single": {
                // シングルスレッドモード: すべてのタスクを順次実行
                run(size, c -> {
                    var future = new FutureTask<>(c);
                    future.run();
                    return future;
                });
                break;
            }
            case "platform": {
                // プラットフォームスレッドモード: 固定サイズのスレッドプールで並列実行
                try (var executor = Executors.newFixedThreadPool(size)) {
                    run(size, executor::submit);
                }
                break;
            }
            case "virtual": {
                // 仮想スレッドモード: タスクごとに仮想スレッドを作成して並列実行
                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    run(size, executor::submit);
                }
                break;
            }
        }
    }

    /**
     * 指定された実行戦略でHTTPリクエスト処理を実行し、経過時間を測定します。
     *
     * @param size     処理を分割するタスク数
     * @param executor タスクを実行する関数（シングルスレッド/プラットフォームスレッド/仮想スレッドの実行戦略）
     */
    static void run(int size, Function<Callable<String>, Future<String>> executor) throws Exception {
        var startTime = System.nanoTime();

        var futures = new ArrayList<Future<String>>();

        // タスクを作成して実行
        for (var i = 0; i < size; i++) {
            var future = executor.apply(callable());
            futures.add(future);
        }

        // すべてのタスクの結果を取得して出力
        for (var future : futures) {
            String responseBody = future.get();
            System.out.println(responseBody);
        }

        // 結果と経過時間を出力
        var endTime = System.nanoTime();
        var elapsed = TimeUnit.NANOSECONDS.toMillis(endTime - startTime) / 1000d;
        System.out.printf("経過時間: %.3f秒%n", elapsed);
    }

    /**
     * HTTPリクエストを実行するCallableタスクを生成します。
     *
     * @return レスポンスボディの文字列を返すCallableタスク
     */
    static Callable<String> callable() {
        return () -> doHttpRequest();
    }

    /**
     * ローカルサーバーに遅延リクエストを送信し、レスポンスを取得します。
     *
     * @return レスポンスボディの文字列
     * @throws IOException ネットワークエラーが発生した場合
     */
    static String doHttpRequest() throws IOException {
        var url = URI.create("http://localhost:8080/delay/3").toURL();
        var conn = (HttpURLConnection) url.openConnection();
        try (var in = conn.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
