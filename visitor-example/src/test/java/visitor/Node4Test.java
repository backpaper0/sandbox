package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.Test;

public class Node4Test {

    @Test public void testCalc() throws Exception {
        Node4 node1 = new NumNode4(2);
        Node4 node2 = new NumNode4(3);
        Node4 node3 = new AddNode4(node1, node2);
        Node4 node4 = new NumNode4(4);
        Node4 node5 = new AddNode4(node3, node4);
        Calclurator4 calclurator = new Calclurator4();
        int actual = calclurator.calc(node5);
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }
}
