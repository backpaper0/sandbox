package el;

import java.util.Objects;

import javax.el.ELProcessor;

public class ELLambdaCalculus {

    public static void main(String[] args) {
        ELProcessor p = new ELProcessor();

        //ラムダ計算をEL式で表現するサンプル
        p.eval("ZERO    = f -> x -> x");
        p.eval("ONE     = f -> x -> f(x)");
        p.eval("THREE   = f -> x -> f(f(f(x)))");
        p.eval("FIVE    = f -> x -> f(f(f(f(f(x)))))");
        p.eval("FIFTEEN = f -> x -> f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(x)))))))))))))))");

        //補助関数
        p.eval("toInt = n -> n(x -> x + 1)(0)");

        //結果の確認
        assertEquals(p.eval("toInt(ZERO)"), 0L);
        assertEquals(p.eval("toInt(ONE)"), 1L);
        assertEquals(p.eval("toInt(THREE)"), 3L);
        assertEquals(p.eval("toInt(FIVE)"), 5L);
        assertEquals(p.eval("toInt(FIFTEEN)"), 15L);
    }

    private static void assertEquals(Object a, Object b) {
        if (Objects.equals(a, b) == false) {
            throw new AssertionError(String.format(
                    "%s(%s) is not equal to %s(%s)", a, a.getClass()
                            .getSimpleName(), b, b.getClass().getSimpleName()));
        }
    }
}
