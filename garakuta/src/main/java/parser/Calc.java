package parser;

public class Calc {

    public static int calc(String text) {
        Calc c = new Calc(text);
        return c.expr();
    }

    private char[] cs;

    private int index;

    private Calc(String text) {
        this.cs = text.toCharArray();
        this.index = 0;
    }

    private char c() {
        if (index < cs.length) {
            return cs[index];
        }
        return (char) 0;
    }

    private int expr() {
        int p = term();
        while (c() == '+' | c() == '-') {
            if (c() == '+') {
                index++;
                p += term();
            } else {
                index++;
                p -= term();
            }
        }
        return p;
    }

    private int term() {
        int p = fact();
        while (c() == '*' | c() == '/') {
            if (c() == '*') {
                index++;
                p *= fact();
            } else {
                index++;
                p /= fact();
            }
        }
        return p;
    }

    private int fact() {
        if (c() != '(') {
            return number();
        }
        index++;
        int p = expr();
        index++;
        return p;
    }

    private int number() {
        int p = 0;
        while ('0' <= c() && c() <= '9') {
            p *= 10;
            p += (c() - 48);
            index++;
        }
        return p;
    }

}