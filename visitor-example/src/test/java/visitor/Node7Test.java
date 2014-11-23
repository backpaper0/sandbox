package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.io.StringWriter;
import org.junit.Test;

public class Node7Test {

    @Test public void testCalc() throws Exception {
        Node7 node1 = new NumNode7(2);
        Node7 node2 = new NumNode7(3);
        Node7 node3 = new AddNode7(node1, node2);
        Node7 node4 = new NumNode7(4);
        Node7 node5 = new AddNode7(node3, node4);
        Calclurator7 calclurator = new Calclurator7();
        int actual = node5.accept(calclurator, null);
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }

    @Test public void testPrint() throws Exception {
        Node7 node1 = new NumNode7(2);
        Node7 node2 = new NumNode7(3);
        Node7 node3 = new AddNode7(node1, node2);
        Node7 node4 = new NumNode7(4);
        Node7 node5 = new AddNode7(node3, node4);
        Printer7 printer = new Printer7();
        StringWriter out = new StringWriter();
        node5.accept(printer, out);
        String actual = out.toString();
        String expected = "((2+3)+4)";
        assertThat(actual, is(expected));
    }
}
