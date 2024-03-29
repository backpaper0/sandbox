import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Brainfxxk {

	public static void main(final String[] args) throws IOException {
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

	public Brainfxxk(final InputStream in, final OutputStream out) {
		this.in = in;
		this.out = out;
	}

	@SuppressWarnings("incomplete-switch")
	public void evaluate(final String source) throws IOException {
		final char[] cs = source.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			final char c = cs[i];
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

	@SuppressWarnings("incomplete-switch")
	private static int jumpRight(final int index, final char[] cs) {
		int i = index;
		int counter = 0;
		while (i < cs.length) {
			i++;
			final char c = cs[i];
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

	@SuppressWarnings("incomplete-switch")
	private static int jumpLeft(final int index, final char[] cs) {
		int i = index;
		int counter = 0;
		while (i >= 0) {
			i--;
			final char c = cs[i];
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
