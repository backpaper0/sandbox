package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.io.StringWriter;
import org.junit.Test;
import visitor.AddNode2;
import visitor.Node2;
import visitor.NumNode2;

public class Node2Test {

    @Test public void testCalc() throws Exception {
        Node2 node1 = new NumNode2(2);
        Node2 node2 = new NumNode2(3);
        Node2 node3 = new AddNode2(node1, node2);
        Node2 node4 = new NumNode2(4);
        Node2 node5 = new AddNode2(node3, node4);
        int actual = node5.calc();
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }

    @Test public void testPrint() throws Exception {
        Node2 node1 = new NumNode2(2);
        Node2 node2 = new NumNode2(3);
        Node2 node3 = new AddNode2(node1, node2);
        Node2 node4 = new NumNode2(4);
        Node2 node5 = new AddNode2(node3, node4);
        StringWriter out = new StringWriter();
        node5.print(out);
        String actual = out.toString();
        String expected = "((2+3)+4)";
        assertThat(actual, is(expected));
    }
}
