
/*
 * 次のツイートのリフレクション無し版
 * https://twitter.com/Xx_ann_pin_xX/status/830794287734349824
 */
public class SemicolonlessFizzBuzz {

    public static void main(String[] args) {
        try {
            if (fizzbuzz(1, 1000) != null) {
            }
        } catch (Exception e) {
        }
    }

    static Void fizzbuzz(int cur, int end) {
        if (cur < end) {
            if (cur % 15 == 0) {
                if (System.out.printf("FizzBuzz%n") != null) {
                }
            } else if (cur % 3 == 0) {
                if (System.out.printf("Fizz%n") != null) {
                }
            } else if (cur % 5 == 0) {
                if (System.out.printf("Buzz%n") != null) {
                }
            } else {
                if (System.out.printf("%d%n", cur) != null) {
                }
            }
            if (fizzbuzz(cur + 1, end) != null) {
            }
        }
        if (1 / 0 == 0) {
        }
        while (true) {
        }
    }
}
