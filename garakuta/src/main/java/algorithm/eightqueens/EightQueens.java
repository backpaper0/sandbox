package algorithm.eightqueens;

import static java.util.stream.IntStream.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import algorithm.eightqueens.EightQueens.NQueens;

public class EightQueens implements Iterable<NQueens> {

	public static void main(String[] args) {

		// https://ja.wikipedia.org/wiki/%E3%82%A8%E3%82%A4%E3%83%88%E3%83%BB%E3%82%AF%E3%82%A4%E3%83%BC%E3%83%B3

		var eightQueens = new EightQueens(8);
		for (var nQueens : eightQueens) {
			System.out.println(nQueens);
		}

		System.out.println(StreamSupport.stream(eightQueens.spliterator(), false).count());
	}

	private final int n;
	private final int nn;
	private final int[] divs;

	public EightQueens(int n) {
		this.n = n;
		this.nn = pow(n, n);
		this.divs = range(0, n).map(i -> pow(n, i)).toArray();
	}

	static int pow(int a, int b) {
		return IntStream.range(0, b).reduce(1, (c, i) -> c * a);
	}

	@Override
	public Iterator<NQueens> iterator() {
		return new Iterator<>() {

			private int cursor;
			private NQueens next;

			{
				nextInternal();
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public NQueens next() {
				if (next == null) {
					throw new NoSuchElementException();
				}
				var n = next;
				nextInternal();
				return n;
			}

			private void nextInternal() {
				var queens = new int[n];
				for (; cursor < nn; cursor++) {
					for (int i = 0; i < n; i++) {
						queens[i] = cursor / divs[i] % n;
					}
					if (validate(queens)) {
						next = new NQueens(queens);
						cursor++;
						return;
					}
				}
				next = null;
			}
		};
	}

	boolean validate(int[] queens) {
		for (int a = 0; a < n - 1; a++) {
			for (int b = a + 1; b < n; b++) {
				if (a != b) {
					//同じ列は(乂'ω')
					if (queens[a] == queens[b]) {
						return false;
					}
					//斜めは('ω'乂)
					if (Math.abs(queens[a] - queens[b]) == b - a) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public record NQueens(int[] queens) {

		@Override
		public String toString() {
			var s = new StringWriter();
			var out = new PrintWriter(s);
			out.println(Arrays.toString(queens));
			for (int i = 0; i < queens.length; i++) {
				for (int j = 0; j < queens.length; j++) {
					if (queens[i] == j) {
						out.print("@");
					} else {
						out.print("O");
					}
				}
				out.println();
			}
			out.flush();
			return s.toString();
		}
	}
}
