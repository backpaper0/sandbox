import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * CPUバウンドな処理における並列実行の性能比較デモ
 *
 * シングルスレッド、プラットフォームスレッド、仮想スレッドの3つの実行モードで
 * 素数カウント処理を実行し、それぞれの性能を比較します。
 */
public class CpuBoundsDemo {

    public static void main(String[] args) throws Exception {
        // 素数を探索する範囲の上限値
        var defaultLimit = 100_000_000;
        var limit = defaultLimit;
        // 処理を分割するタスク数
        var defaultSize = 8;
        var size = defaultSize;
        // 実行モード
        var defaultMode = "virtual";
        var mode = defaultMode;
        var argsIter = Arrays.asList(args).iterator();
        while (argsIter.hasNext()) {
            var arg = argsIter.next();
            switch (arg) {
                case "--size":
                    size = Integer.parseInt(argsIter.next());
                    break;
                case "--limit":
                    limit = Integer.parseInt(argsIter.next());
                    break;
                case "--mode":
                    mode = argsIter.next();
                    break;
            }
        }
        System.out.printf("size: %s%n", size);
        System.out.printf("limit: %s%n", limit);
        System.out.printf("mode: %s%n", mode);
        switch (mode) {
            case "single": {
                // シングルスレッドモード: すべてのタスクを順次実行
                run(limit, size, c -> {
                    var future = new FutureTask<>(c);
                    future.run();
                    return future;
                });
                break;
            }
            case "platform": {
                // プラットフォームスレッドモード: 固定サイズのスレッドプールで並列実行
                try (var executor = Executors.newFixedThreadPool(size)) {
                    run(limit, size, executor::submit);
                }
                break;
            }
            case "virtual": {
                // 仮想スレッドモード: タスクごとに仮想スレッドを作成して並列実行
                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    run(limit, size, executor::submit);
                }
                break;
            }
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * 指定された実行戦略で素数カウント処理を実行し、経過時間を測定します。
     *
     * @param limit    素数を探索する範囲の上限値
     * @param size     処理を分割するタスク数
     * @param executor タスクを実行する関数（シングルスレッド/プラットフォームスレッド/仮想スレッドの実行戦略）
     */
    static void run(int limit, int size, Function<Callable<Integer>, Future<Integer>> executor) throws Exception {
        var startTime = System.nanoTime();

        // 範囲を均等に分割するためのチャンクサイズを計算
        var chunk = limit / size;
        var start = 0;
        var futures = new ArrayList<Future<Integer>>();

        // タスクを作成して実行
        for (var i = 0; i < size; i++) {
            // 最後のタスクは余りも含めて処理
            var end = i < size - 1 ? start + chunk : limit;
            var future = executor.apply(callable(start, end));
            futures.add(future);
            start = end;
        }

        // すべてのタスクの結果を集計
        var prime_count = 0;
        for (var future : futures) {
            prime_count += future.get();
        }

        // 結果と経過時間を出力
        System.out.printf("%,d未満の素数の個数: %,d%n", limit, prime_count);
        var endTime = System.nanoTime();
        var elapsed = TimeUnit.NANOSECONDS.toMillis(endTime - startTime) / 1000d;
        System.out.printf("経過時間: %.3f秒%n", elapsed);
    }

    /**
     * 指定された範囲の素数をカウントするCallableタスクを生成します。
     *
     * @param start 範囲の開始値（含む）
     * @param end   範囲の終了値（含まない）
     * @return 素数の個数を返すCallableタスク
     */
    static Callable<Integer> callable(int start, int end) {
        return () -> countPrimesRange(start, end);
    }

    /**
     * 指定された数が素数かどうかを判定します。
     *
     * @param n 判定する数
     * @return 素数の場合true、それ以外はfalse
     */
    static boolean isPrime(int n) {
        // 2未満は素数ではない
        if (n < 2) {
            return false;
        }
        // 2は唯一の偶数の素数
        if (n % 2 == 0) {
            return n == 2;
        }
        // 平方根までの奇数で割り切れるかチェック
        var limit = ((int) Math.sqrt((double) n)) + 1;
        for (var i = 3; i < limit; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定された範囲内の素数の個数をカウントします。
     *
     * @param startInclude 範囲の開始値（含む）
     * @param endExclude   範囲の終了値（含まない）
     * @return 範囲内の素数の個数
     */
    static int countPrimesRange(int startInclude, int endExclude) {
        var count = 0;
        for (var n = startInclude; n < endExclude; n++) {
            if (isPrime(n)) {
                count++;
            }
        }
        return count;
    }
}
