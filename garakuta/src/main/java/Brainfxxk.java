import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Brainfxxk {

    public static void main(String[] args) throws IOException {
        new Brainfxxk().evaluate(
                "+++++++++[>++++++++>+++++++++++>+++++<<<-]>.>++.+++++++..+++.>-.------------.<++++++++.--------.+++.------.--------.>+.");
        System.out.println();
        new Brainfxxk().evaluate(
                "++++++++++++[->++++++>+++++++++>+++++>++++++++++>++++++++++>+++>>>>>>++++++++<<<<<<<<<<<<]>-->--->++++++>--->++>---->>>>+++>+++++>++++[>>>+[-<<[->>+>+<<<]>>>[-<<<+>>>]+<[[-]>-<<[->+>+<<]>>[-<<+>>]+<[[-]>-<<<+>->]>[-<<<--------->+++++++++>>>>>+<<<]<]>[-<+++++++[<<+++++++>>-]<++++++++>>]>>>]<<<<<<[<<<<]>-[-<<+>+>]<[->+<]+<[[-]>-<]>[->+++<<<<<<<<<.>.>>>..>>+>>]>>-[-<<<+>+>>]<<[->>+<<]+<[[-]>-<]>[->>+++++<<<<<<<<.>.>..>>+>>]<+<[[-]>-<]>[->>>>>[>>>>]<<<<[.<<<<]<]<<.>>>>>>-]");
    }

    private final int[] tape = new int[100];

    private int p = 0;

    private final OutputStream out;

    private final InputStream in;

    public Brainfxxk() {
        this(System.in, System.out);
    }

    public Brainfxxk(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void evaluate(String source) throws IOException {
        char[] cs = source.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            switch (c) {
            case '>':
                p = Math.min(p + 1, tape.length - 1);
                break;
            case '<':
                p = Math.max(p - 1, 0);
                break;
            case '+':
                tape[p]++;
                break;
            case '-':
                tape[p]--;
                break;
            case '.':
                out.write((char) (tape[p] & 0xff));
                break;
            case ',':
                tape[p] = in.read();
                break;
            case '[':
                if (tape[p] == 0) {
                    i = jumpRight(i, cs);
                }
                break;
            case ']':
                if (tape[p] != 0) {
                    i = jumpLeft(i, cs);
                }
                break;
            }
        }
        out.flush();
    }

    private static int jumpRight(int index, char[] cs) {
        int i = index;
        int counter = 0;
        while (i < cs.length) {
            i++;
            char c = cs[i];
            switch (c) {
            case '[':
                counter++;
                break;
            case ']':
                if (counter > 0) {
                    counter--;
                    break;
                }
                return i;
            }
        }
        return i;
    }

    private static int jumpLeft(int index, char[] cs) {
        int i = index;
        int counter = 0;
        while (i >= 0) {
            i--;
            char c = cs[i];
            switch (c) {
            case ']':
                counter++;
                break;
            case '[':
                if (counter > 0) {
                    counter--;
                    break;
                }
                return i;
            }
        }
        return i;
    }
}
