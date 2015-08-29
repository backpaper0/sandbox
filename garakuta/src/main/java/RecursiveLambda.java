import java.util.function.UnaryOperator;

public class RecursiveLambda {

    /*
     * ラムダ式は再帰呼び出しできない。
     * 次のラムダ式は自身のメソッドapplyを呼び出そうとしているので
     * コンパイルエラーになる。
     * UnaryOperator<Integer> sum = n -> (n > 0) ? n + apply(n - 1) : 0;
     * 
     * 再帰したい関数インターフェースを拡張した関数インターフェースを用意する。
     * ここでは UnaryOperator を拡張した RecursiveUnaryOperator を用意した。
     *  
     * その関数インターフェースでは第一引数に自分自身を渡すようにする。
     *  
     * すると次のようなコードで再帰できる。
     */
    public static void main(String[] args) {
        RecursiveUnaryOperator<Integer> sum = (f, n) -> (n > 0)
                ? n + f.apply(n - 1) : 0;

        Integer result = sum.apply(10);

        //55
        System.out.println(result);
    }

    interface RecursiveUnaryOperator<T> extends UnaryOperator<T> {
        T apply(UnaryOperator<T> self, T t);

        @Override
        default T apply(T t) {
            return apply(this, t);
        }
    }
}
