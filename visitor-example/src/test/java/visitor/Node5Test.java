package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.io.StringWriter;
import org.junit.Test;

public class Node5Test {

    @Test public void testCalc() throws Exception {
        Node5 node1 = new NumNode5(2);
        Node5 node2 = new NumNode5(3);
        Node5 node3 = new AddNode5(node1, node2);
        Node5 node4 = new NumNode5(4);
        Node5 node5 = new AddNode5(node3, node4);
        Calclurator5 calclurator = new Calclurator5();
        int actual = node5.accept(calclurator);
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }

    @Test public void testPrint() throws Exception {
        Node5 node1 = new NumNode5(2);
        Node5 node2 = new NumNode5(3);
        Node5 node3 = new AddNode5(node1, node2);
        Node5 node4 = new NumNode5(4);
        Node5 node5 = new AddNode5(node3, node4);
        StringWriter out = new StringWriter();
        Printer5 printer = new Printer5(out);
        node5.accept(printer);
        String actual = out.toString();
        String expected = "((2+3)+4)";
        assertThat(actual, is(expected));
    }
}
