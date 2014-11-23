package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.io.StringWriter;
import org.junit.Test;

public class Node6Test {

    @Test public void testCalc() throws Exception {
        Node6 node1 = new NumNode6(2);
        Node6 node2 = new NumNode6(3);
        Node6 node3 = new AddNode6(node1, node2);
        Node6 node4 = new NumNode6(4);
        Node6 node5 = new AddNode6(node3, node4);
        Calclurator6 calclurator = new Calclurator6();
        int actual = node5.accept(calclurator, null);
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }

    @Test public void testPrint() throws Exception {
        Node6 node1 = new NumNode6(2);
        Node6 node2 = new NumNode6(3);
        Node6 node3 = new AddNode6(node1, node2);
        Node6 node4 = new NumNode6(4);
        Node6 node5 = new AddNode6(node3, node4);
        Printer6 printer = new Printer6();
        StringWriter out = new StringWriter();
        node5.accept(printer, out);
        String actual = out.toString();
        String expected = "((2+3)+4)";
        assertThat(actual, is(expected));
    }
}
