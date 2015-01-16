package npnl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @see <a href="http://npnl.hatenablog.jp/entry/20101023/1287835025">お題：リストの内容比較</a>
 */
public class ListNoNaiyoHikaku {

    List<String> l = Arrays.asList("aaa bbb ccc bbb eee hhhh".split(" "));

    List<String> r = Arrays.asList("bbb ddd eee fff ggg iiiii".split(" "));

    @Test
    public void test() throws Exception {
        StringWriter actual = new StringWriter();
        listNoNaiyoHikaku(l, r, actual);

        StringWriter expected = new StringWriter();
        PrintWriter out = new PrintWriter(expected);
        out.println("[left only] [aaa, ccc, hhhh]");
        out.println("[right only] [ddd, fff, ggg, iiiii]");
        out.println("[both] [bbb, eee]");

        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testName() throws Exception {
        StringWriter actual = new StringWriter();
        narabeteShutsuryoku(l, r, actual);

        StringWriter expected = new StringWriter();
        PrintWriter out = new PrintWriter(expected);
        out.println("aaa  | ");
        out.println("bbb  | bbb");
        out.println("ccc  | ");
        out.println("     | ddd");
        out.println("eee  | eee");
        out.println("     | fff");
        out.println("     | ggg");
        out.println("hhhh | ");
        out.println("     | iiiii");

        assertThat(actual.toString(), is(expected.toString()));
    }

    <T extends Comparable<T>> void narabeteShutsuryoku(List<T> l, List<T> r,
            Writer out) {
        int length = 0;
        for (T t : l) {
            length = Math.max(length, t.toString().length());
        }
        PrintWriter pw = new PrintWriter(out);
        Iterator<T> lit = new TreeSet<>(l).iterator();
        Iterator<T> rit = new TreeSet<>(r).iterator();
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
            int compared = lt != null ? rt != null ? lt.compareTo(rt) : -1 : 1;
            StringBuilder sb = new StringBuilder();
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

    <T extends Comparable<T>> void listNoNaiyoHikaku(List<T> l, List<T> r,
            Writer out) {
        PrintWriter pw = new PrintWriter(out);

        TreeSet<T> ls = new TreeSet<>(l);
        ls.removeAll(r);
        pw.println("[left only] " + ls);

        TreeSet<T> rs = new TreeSet<>(r);
        rs.removeAll(l);
        pw.println("[right only] " + rs);

        TreeSet<T> both = new TreeSet<>(l);
        both.retainAll(r);
        pw.println("[both] " + both);
    }
}
