package npnl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

/**
 * @see <a href="http://npnl.hatenablog.jp/entry/20101023/1287835025">お題：リストの内容比較</a>
 */
class ListNoNaiyoHikaku {

	List<String> l = List.of("aaa bbb ccc bbb eee hhhh".split(" "));

	List<String> r = List.of("bbb ddd eee fff ggg iiiii".split(" "));

	@Test
	void test() throws Exception {
		final StringWriter actual = new StringWriter();
		listNoNaiyoHikaku(l, r, actual);

		final StringWriter expected = new StringWriter();
		final PrintWriter out = new PrintWriter(expected);
		out.println("[left only] [aaa, ccc, hhhh]");
		out.println("[right only] [ddd, fff, ggg, iiiii]");
		out.println("[both] [bbb, eee]");

		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	void testName() throws Exception {
		final StringWriter actual = new StringWriter();
		narabeteShutsuryoku(l, r, actual);

		final StringWriter expected = new StringWriter();
		final PrintWriter out = new PrintWriter(expected);
		out.println("aaa  | ");
		out.println("bbb  | bbb");
		out.println("ccc  | ");
		out.println("     | ddd");
		out.println("eee  | eee");
		out.println("     | fff");
		out.println("     | ggg");
		out.println("hhhh | ");
		out.println("     | iiiii");

		assertEquals(expected.toString(), actual.toString());
	}

	<T extends Comparable<T>> void narabeteShutsuryoku(final List<T> l, final List<T> r,
			final Writer out) {
		int length = 0;
		for (final T t : l) {
			length = Math.max(length, t.toString().length());
		}
		final PrintWriter pw = new PrintWriter(out);
		final Iterator<T> lit = new TreeSet<>(l).iterator();
		final Iterator<T> rit = new TreeSet<>(r).iterator();
		T lt = null;
		T rt = null;
		while (true) {
			if (lt == null && lit.hasNext()) {
				lt = lit.next();
			}
			if (rt == null && rit.hasNext()) {
				rt = rit.next();
			}
			if (lt == null && rt == null) {
				break;
			}
			final int compared = lt != null ? rt != null ? lt.compareTo(rt) : -1 : 1;
			final StringBuilder sb = new StringBuilder();
			if (compared <= 0) {
				sb.append(lt);
				lt = null;
			}
			while (sb.length() < length) {
				sb.append(' ');
			}
			sb.append(" | ");
			if (compared >= 0) {
				sb.append(rt);
				rt = null;
			}
			pw.println(sb);
		}
	}

	<T extends Comparable<T>> void listNoNaiyoHikaku(final List<T> l, final List<T> r,
			final Writer out) {
		final PrintWriter pw = new PrintWriter(out);

		final TreeSet<T> ls = new TreeSet<>(l);
		ls.removeAll(r);
		pw.println("[left only] " + ls);

		final TreeSet<T> rs = new TreeSet<>(r);
		rs.removeAll(l);
		pw.println("[right only] " + rs);

		final TreeSet<T> both = new TreeSet<>(l);
		both.retainAll(r);
		pw.println("[both] " + both);
	}
}
