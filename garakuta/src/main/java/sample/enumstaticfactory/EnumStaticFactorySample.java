package sample.enumstaticfactory;

import java.util.Arrays;

public class EnumStaticFactorySample {

    public static void main(String[] args) {
        Status status = Status.of(2);
        System.out.println(status);
    }

    enum Status {
        TODO(0), DOING(1), DONE(2);
        private final int value;

        private Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status of(int value) {

            //Java 7まではこんな感じで書いてた
            // for (Status status : values()) {
            //     if (status.value == value) {
            //         return status;
            //     }
            // }
            // throw new IllegalArgumentException();

            return
                    //このenumのすべての値から
                    Arrays.stream(values())
                    //valueが一致するものに絞って(valueが一致するのはひとつしかない)
                    .filter(status -> status.value == value)
                    //ひとつ取り出して
                    .findAny()
                    //返す。valueが一致するものが無ければ例外を投げる
                    .orElseThrow(IllegalArgumentException::new);

            /*
             * findFirstはStreamの一番先頭を取ってきます(※順序付けされていない
             * Streamの場合は任意の要素)。
             * findAnyは順序に関わらず任意の要素を取ってきます。
             * 順序を気にしなくて良い分、並列処理のときはfindAnyの方がコストの低い操作です。
             * (findFirstは並列処理で使うと複数のスレッド間で順序を気にしないといけない分、高コスト)
             * シングルスレッドの場合はどちらのメソッドを利用するか気にしなくて良いです。
             * (ただし並列処理にした場合にどちらのメソッドの方が良いか考えてみるのは良い習慣だと思います)
             * 
             * 順序付けされたStreamについては次のURLを参照してください。
             * https://docs.oracle.com/javase/jp/8/docs/api/java/util/stream/package-summary.html#Ordering
             */
        }
    }
}
